package com.lin.missyou.model.dataAccessObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lin.missyou.util.GenericAndJson;
import com.lin.missyou.util.ListAndJson;
import com.lin.missyou.util.MapAndJson;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@Where(clause = "delete_time is null and online = 1")
public class Sku extends BaseEntity {
    @Id
    private Long id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean online;
    private String img;
    private String title;
    private Long spuId;

    private String specs;
    private String code;
    private Long stock;
    private Long categoryId;
    private Long rootCategoryId;
//    @Convert(converter = MapAndJson.class)
//    private Map<String, Object> test;


    public List<Spec> getSpecs() {
        if (this.specs == null) {
            return Collections.emptyList();
        }
        return GenericAndJson.jsonToObject(this.specs, new TypeReference<List<Spec>>() {
        });
    }

    public void setSpecs(List<Spec> specs) {
        if (specs.isEmpty()) {
            return;
        }
        this.specs = GenericAndJson.objectToJson(specs);

    }

    @JsonIgnore
    public List<String> getSpecValueList(){
        return this.getSpecs()
                .stream()
                .map(Spec::getValue)
                .collect(Collectors.toList());
    }


    public BigDecimal getActualPrice() {
        return discountPrice == null ? price : discountPrice;
    }
}
