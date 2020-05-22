package com.lin.missyou.model.dataAccessObject;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class SpuImg extends BaseEntity {
    @Id
    private Long id;
    private String img;
    private Long spuId;
}
