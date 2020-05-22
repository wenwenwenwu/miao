/**
 * @作者 7七月
 * @微信公号 林间有风
 * @开源项目 $ http://7yue.pro
 * @免费专栏 $ http://course.7yue.pro
 * @我的课程 $ http://imooc.com/t/4294850
 * @创建时间 2019-08-05 05:19
 */
package com.lin.missyou.model.dataAccessObject;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Where(clause = "delete_time is null")
public class Coupon extends BaseEntity {
    @Id
    private Long id;
    private Long activityId;
    private String title;
    private Date startTime;
    private Date endTime;
    private String description;
    private BigDecimal fullMoney; //满减券的满
    private BigDecimal minus; //满减券的减
    private BigDecimal rate; //折扣券的折扣
    private String remark; //适用范围描述
    private Boolean wholeStore; //是否全场券
    private Integer type;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "couponList")
    private List<Category> categoryList;
}
