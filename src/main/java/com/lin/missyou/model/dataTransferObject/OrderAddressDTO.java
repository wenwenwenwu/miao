package com.lin.missyou.model.dataTransferObject;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderAddressDTO {
    private String userName;
    private String privince;
    private String city;
    private String country;
    private String mobile;
    private String nationalCode;
    private String postalCode;
    private String detail;
}
