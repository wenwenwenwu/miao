/**
 * @作者 7七月
 * @微信公号 林间有风
 * @开源项目 $ http://7yue.pro
 * @免费专栏 $ http://course.7yue.pro
 * @我的课程 $ http://imooc.com/t/4294850
 * @创建时间 2019-07-12 13:44
 */
package com.lin.missyou.model.dataTransferObject;

import com.lin.missyou.core.enumeration.LoginType;
import com.lin.missyou.model.dataTransferObject.validators.TokenPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TokenGetDTO {
    @NotBlank(message = "account不允许为空")
    private String account;
    @TokenPassword(max=30, message = "{token.password}")
    private String password;
    private LoginType type; //登录方式（微信登录account为微信code、password为空，邮箱登录、手机号登录）
}
