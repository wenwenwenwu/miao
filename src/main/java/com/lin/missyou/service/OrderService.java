package com.lin.missyou.service;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.core.money.IMoneyDiscount;
import com.lin.missyou.exception.exception.ForbiddenException;
import com.lin.missyou.exception.exception.NotFoundException;
import com.lin.missyou.exception.exception.ParameterException;
import com.lin.missyou.logic.CouponChecker;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.dataAccessObject.*;
import com.lin.missyou.model.dataTransferObject.OrderDTO;
import com.lin.missyou.model.dataTransferObject.SkuInfoDTO;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.SkuRepository;
import com.lin.missyou.repository.UserCouponRepository;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IMoneyDiscount iMoneyDiscount;

    @Value("${missyou.order.max-sku-limit}")
    private int maxSkuLimit;

    @Value("${missyou.order.pay-time-limit}")
    private Integer payTimeLimit;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public OrderChecker isOk(Long uid, OrderDTO orderDTO) {
        List<Long> skuIdList = orderDTO.getSkuInfoList()
                .stream()
                .map(SkuInfoDTO::getId)
                .collect(Collectors.toList());

        List<Sku> skuList = this.skuService.getSkuListByIds(skuIdList);

        Long couponId = orderDTO.getCouponId();
        CouponChecker couponChecker = null;
        if (couponId != null) {
            Coupon coupon = this.couponRepository.findById(couponId)
                    .orElseThrow(() -> new NotFoundException(40004));
            UserCoupon userCoupon = this.userCouponRepository.findFirstByUserIdAndCouponIdAndStatusAndOrderId(uid, couponId, 1, null)
                    .orElseThrow(() -> new NotFoundException(50006));
            couponChecker = new CouponChecker(coupon, iMoneyDiscount);
        }
        OrderChecker orderChecker = new OrderChecker(this.maxSkuLimit, orderDTO, skuList, couponChecker);
        orderChecker.isOK();
        return orderChecker;
    }

    @Transactional //事务
    public Long placeOrder(Long uid, OrderDTO orderDTO, OrderChecker orderChecker) {
        //生成订单
        String orderNo = OrderUtil.makeOrderNo();
        Calendar now = Calendar.getInstance(); 
        Calendar placedTime = (Calendar) now.clone(); //保留原始的now
        Date expiredTime = CommonUtil.addSomeSeconds(now,this.payTimeLimit).getTime(); //加工过的now
        Order order = Order.builder()
                .orderNo(orderNo)
                .totalPrice(orderDTO.getTotalPrice()) //orderDTO已经经过了校验
                .finalTotalPrice(orderDTO.getFinalTotalPrice())
                .userId(uid)
                .totalCount(orderChecker.getTotalCount().longValue())
                .snapImg((orderChecker.getLeaderImg()))
                .snapTitle(orderChecker.getLeaderTitle())
                .status(OrderStatus.UNPAID.value())
                .expiredTime(expiredTime)
                .placedTime(placedTime.getTime())
                .build();
        order.setCreateTime(now.getTime());
        order.setSnapAddress(orderDTO.getAddress());
        order.setSnapItems(orderChecker.getOrderSkuList());
        this.orderRepository.save(order);
        //减库存
        this.reduceStock(orderChecker);
        //核销优惠券
        Long couponId = -1L;
        if (orderDTO.getCouponId() != null) {
            this.writeOffCoupon(orderDTO.getCouponId(), order.getId(), uid);
            couponId = orderDTO.getCouponId();
        }
        //加入到延迟消息队列
        //往redis里写入一个key，并设置过期时间
        this.sendToRedis(order.getId(),uid, couponId);
        //返回订单Id
        return order.getId();
    }


    private void sendToRedis(Long oid, Long uid, Long couponId){
        String key = uid.toString()+","+oid.toString()+","+couponId.toString();
        //不一定成功，redis当机或者网络环境不好
        try{
            //keyEvent事件只返回key、value(不重要设为1)、过期时间、过期时间单位
            stringRedisTemplate.opsForValue().set(key,"1",this.payTimeLimit, TimeUnit.SECONDS);
        } catch (Exception e){
            //不抛出异常，会导致所有用户无法下单
            e.printStackTrace();
        }
    }

    private void reduceStock(OrderChecker orderChecker) {
        List<OrderSku> orderSkuList = orderChecker.getOrderSkuList();
        for (OrderSku orderSku : orderSkuList) {
            int result = this.skuRepository.reduceStock(orderSku.getId(), orderSku.getCount().longValue());
            if (result != 1) {
                throw new ParameterException(50003); //订单中有商品库存不足
            }
        }
    }

    private void writeOffCoupon(Long couponId, Long orderId, Long uid) {
        int result = this.userCouponRepository.writeOff(couponId, orderId, uid);
        if (result != 1) {
            throw new ForbiddenException(40012);
        }
    }

    public Page<Order> getUnpaid(Integer page, Integer size){
        Date now = new Date();
        int status = OrderStatus.UNPAID.value();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        return this.orderRepository.findByExpiredTimeGreaterThanAndStatusAndUserId(now,status,uid,pageable);
    }

    public Page<Order> getByStatus(int status, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        if (status == OrderStatus.ALL.value()){
            return this.orderRepository.findByUserId(uid,pageable);
        }
        return this.orderRepository.findByUserIdAndStatus(uid,status,pageable);
    }

    public Optional<Order> getOrderDetail(Long oid){
        Long uid = LocalUser.getUser().getId();
        return this.orderRepository.findFirstByUserIdAndId(uid,oid);
    }

    public void updateOrderPrepayId(Long orderId, String prePayId){
        Optional<Order> orderOptional = this.orderRepository.findById(orderId);
        orderOptional.ifPresent(o->{
            o.setPrepayId(prePayId);
            this.orderRepository.save(o);
        });
        orderOptional.orElseThrow(()->new ParameterException(10007));
    }

}
