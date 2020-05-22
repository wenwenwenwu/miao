package com.lin.missyou.model.dataTransferObject;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class SchoolDTO {
    @Length(min = 2)
    private String schoolName;
}
