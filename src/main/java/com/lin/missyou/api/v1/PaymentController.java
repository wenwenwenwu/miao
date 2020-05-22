package com.lin.missyou.api.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.lib.LinWxNotify;
import com.lin.missyou.model.dataAccessObject.Order;
import com.lin.missyou.repository.UserCouponRepository;
import com.lin.missyou.service.WxPaymentNotifyService;
import com.lin.missyou.service.WxPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("payment")
@Validated
public class PaymentController {

    @Autowired
    private WxPaymentService wxPaymentService;

    @Autowired
    private WxPaymentNotifyService wxPaymentNotifyService;

    @ScopeLevel
    @PostMapping("/pay/order/{id}")
    public Map<String,String> preWxOrder(@PathVariable(name = "id") @Positive Long oid){
        Map<String,String> miniPayParams = this.wxPaymentService.preOrder(oid);
        return miniPayParams;
    }


    @RequestMapping("/wx/notify")
    //微信服务器的通知机制：微信服务器不断调用本接口，如果成功处理了返回SUCCESS，没有的话不返回。微信的调用就不会间断
    //微信服务器调用这个接口的时候，提交的不是JSON格式的数据，因此要用request和response读取
    public String payCallBack(HttpServletRequest request, HttpServletResponse response){
        //从request读取stream
        InputStream s;
        try {
            s = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return LinWxNotify.fail();
        }
        //从stream读取xml
        String xml;
        xml = LinWxNotify.readNotify(s);
        try {
            this.wxPaymentNotifyService.processPayNotify(xml);
        } catch (Exception e){
            return LinWxNotify.fail();
        }
        return LinWxNotify.success();
    }
}
