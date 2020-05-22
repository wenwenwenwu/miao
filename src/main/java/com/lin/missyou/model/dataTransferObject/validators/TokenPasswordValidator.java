package com.lin.missyou.model.dataTransferObject.validators;

import com.lin.missyou.model.dataTransferObject.PersonDTO;
import com.lin.missyou.model.dataTransferObject.TokenGetDTO;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;

public class TokenPasswordValidator implements ConstraintValidator<TokenPassword, String> {
    private Integer min;
    private Integer max;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(s)) { //小程序登录无需输入password
            return true;
        }
        return s.length() >= this.min && s.length() <= this.max;
    }

    @Override
    public void initialize(TokenPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }
}
