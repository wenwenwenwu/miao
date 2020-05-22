package com.lin.missyou.model.dataAccessObject;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long couponId;
    private Long orderId; //使用优惠券的订单
    private Integer status; //用户的优惠券使用状态
    private Date createTime; //领用时间
    }
