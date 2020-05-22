package com.lin.missyou.model.dataTransferObject;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class OrderDTO {
    @DecimalMin(value = "0.00", message = "不在合法范围内")
    @DecimalMax(value = "99999999.99", message = "不在合法范围内")
    private BigDecimal totalPrice;
    @DecimalMin(value = "0.00", message = "不在合法范围内")
    @DecimalMax(value = "99999999.99", message = "不在合法范围内")
    private BigDecimal finalTotalPrice;
    private Long couponId;
    private List<SkuInfoDTO> skuInfoList;
    private OrderAddressDTO address;

}
