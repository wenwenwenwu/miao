package com.lin.missyou.model.dataAccessObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass//Entity的基类
public abstract class BaseEntity {
    @JsonIgnore //序列化时忽略
    @Column(insertable = false,updatable = false) //防止插入默认的null
    private Date createTime;
    @JsonIgnore
    @Column(insertable = false,updatable = false)
    private Date updateTime;
    @JsonIgnore
    private Date deleteTime;
}