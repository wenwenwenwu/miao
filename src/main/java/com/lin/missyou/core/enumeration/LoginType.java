package com.lin.missyou.core.enumeration;

public enum LoginType {
    //枚举是包含有限个实例对象的类
    USER_WX(0, "微信登录"),
    USER_Email(1, "邮箱登录");

    //添加成员变量
    private Integer value;
    private String description;

    //构造函数
    //private可加可不加，但不能写成public
    private LoginType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    //添加实例方法
    public void test(){

    }
}
