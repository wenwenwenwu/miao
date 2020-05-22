package com.lin.missyou.model.dataAccessObject;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Category extends BaseEntity {
    @Id
    private Long id;
    private String name;
    private String description;
    private Boolean isRoot;
    private Long parentId;
    private String img;
    private Long index;
    private Boolean online;
    private Long level;

    @ManyToMany
    @JoinTable(name = "coupon_category",joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns =  @JoinColumn(name = "coupon_id"))
    private List<Spu> couponList;



}
