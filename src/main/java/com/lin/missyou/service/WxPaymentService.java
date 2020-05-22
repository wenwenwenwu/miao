package com.lin.missyou.service;

import com.github.wxpay.sdk.LinWXPayConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.lin.missyou.core.LocalUser;
import com.lin.missyou.exception.exception.ForbiddenException;
import com.lin.missyou.exception.exception.NotFoundException;
import com.lin.missyou.exception.exception.ParameterException;
import com.lin.missyou.exception.exception.ServerErrorException;
import com.lin.missyou.model.dataAccessObject.Order;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.util.HttpRequestProxy;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WxPaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Value("missyou.order.pay-callback-host")
    private String payCallbackHost;

    @Value("missyou.order.pay-callback-path")
    private String payCallbackPath;

    private static LinWXPayConfig linWXPayConfig = new LinWXPayConfig();

    public Map<String, String> preOrder(Long oid) {
        Long uid = LocalUser.getUser().getId();
        Optional<Order> orderOptional = this.orderRepository.findFirstByUserIdAndId(uid, oid);
        Order order = orderOptional.orElseThrow(() -> new NotFoundException(50009)); //订单不存在
        //剔除（没来得及修改为）CANCEL态的order
        if (order.needCancel()) {
            throw new ForbiddenException(50010); //订单过期
        }
        //生成微信下单所需参数
        WXPay wxPay = this.assmbleWxPayConfig();
        Map<String, String> params = this.makePreOrderParams(order.getFinalTotalPrice(), order.getOrderNo());
        Map<String, String> wxOrder;
        //微信下单请求发送成功
        try {
            wxOrder = wxPay.unifiedOrder(params);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        //微信下单成功
        if (this.unifiedOrderSuccess(wxOrder)) {
            //更新数据库中订单据的prepayId
            this.orderService.updateOrderPrepayId(order.getId(), wxOrder.get("prepay_id"));
        }
        //返回小程序调用微信支付接口所需要的数据
        return this.makePaySignature(wxOrder);
    }

    public WXPay assmbleWxPayConfig() {
        WXPay wxPay = null;
        try {
            wxPay = new WXPay(WxPaymentService.linWXPayConfig);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        return wxPay;
    }

    private Map<String, String> makePreOrderParams(BigDecimal serverFinalPrice, String orderNO) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "Sleeve");
        data.put("out_trade_no", orderNO);
        data.put("device_info", "Sleeve");
        data.put("fee_type", "CNY");
        data.put("trade_type", "JSAPI");
        data.put("total_fee", CommonUtil.yuanToFenPlainString(serverFinalPrice));
        data.put("openid", LocalUser.getUser().getOpenid());
        //远程访问的IP地址（用户手机IP地址）
        //HttpRequestProxy是在SpringBoot中获取到远程访问的IP地址的工具类
        data.put("spbill_reate_ip", HttpRequestProxy.getRemoteRealIp());
        //服务端接收微信支付结果通知的地址
        String payCallbackUrl = this.payCallbackHost + this.payCallbackPath;
        data.put("notify_url", payCallbackUrl);
        return data;
    }

    private boolean unifiedOrderSuccess(Map<String, String> wxOrder) {
        if (!wxOrder.get("return_code").equals("SUCCESS") || !wxOrder.get("result_code").equals("SUCCESS")) {
            throw new ParameterException(10007);
        }
        return true;
    }

    private Map<String, String> makePaySignature(Map<String, String> wxOrder) {
        String packages = "prepay_id=" + wxOrder.get("prepay_id"); //package为java的保留关键字
        //创建生成sing方法所需的参数
        Map<String, String> wxPayMap = new HashMap<>();
        wxPayMap.put("appId", WxPaymentService.linWXPayConfig.getAppID());
        wxPayMap.put("timeStamp", CommonUtil.timeStamp10());
        wxOrder.put("nonceStr", RandomStringUtils.randomAlphabetic(32));
        wxOrder.put("package", packages);
        wxOrder.put("signType", "HMAC-SHA256");
        //生成sign
        String sign;
        try {
            sign = WXPayUtil.generateSignature(wxPayMap, WxPaymentService.linWXPayConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
        } catch (Exception e) {
            throw new ServerErrorException(9999);
        }
        //创建返回前端的数据
        Map<String,String> miniPayParams = new HashMap<>();
        miniPayParams.put("paySign",sign);
        miniPayParams.putAll(wxPayMap);
        miniPayParams.remove("appId"); //比wxPayMap多了个sign，少了个appId
        return miniPayParams;
    }

}
