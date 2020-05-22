package com.lin.missyou.core.money;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

//@Service
public class HalfUpRound implements IMoneyDiscount {
    @Override
    public BigDecimal discount(BigDecimal original, BigDecimal discount) {
        BigDecimal actualMoney = original.multiply(discount);
        BigDecimal finalMoney = actualMoney.setScale(2, RoundingMode.HALF_UP);
        return finalMoney;
    }

}
