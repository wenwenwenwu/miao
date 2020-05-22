package com.lin.missyou.model.dataTransferObject.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

//自定义注解
@Documented //公共API的一部分
@Retention(RetentionPolicy.RUNTIME) //被保留到运行时
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD}) //可被用在类和方法和字段上
@Constraint(validatedBy = PasswordValidator.class) //关联实现类
public @interface PasswordEqual {
    //这些参数可以在注解使用处传入
    //所有的自定义校验注解都会有message方法，定义了一个消息，当错误调用的时候自动加入到异常错误信息中
    String message() default "两次密码不相同";
    int min() default 4; //注解中只能使用基本类型而不能使用包装类型
    int max() default 6;
    //注解必须有这两个方法
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
