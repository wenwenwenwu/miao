package com.lin.missyou.model.dataTransferObject;

import com.lin.missyou.model.dataTransferObject.validators.PasswordEqual;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@PasswordEqual(min = 1)
public class PersonDTO {
    private Integer age;

    @Length(min = 2, max = 10, message = "姓名只能是2-10个字符")
    private String name;

    private String password1;
    private String password2;
}
