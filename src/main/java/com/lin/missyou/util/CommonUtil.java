package com.lin.missyou.util;

import com.lin.missyou.model.businessObject.PageInfo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class CommonUtil {
    public static PageInfo convertToPageParameter(Integer start, Integer count) {
        int page = start / count;
        PageInfo pageInfo = PageInfo.builder()
                .page(page)
                .size(count)
                .build();
        return pageInfo;
    }

    public static Boolean isInTimeLine(Date date, Date start, Date end) {
        Long time = date.getTime();
        Long startTime = start.getTime();
        Long endTime = end.getTime();
        return startTime < time && time < endTime;
    }

    public static Boolean isOutOfDate(Date expiredTime) {
        Long now = Calendar.getInstance().getTimeInMillis();
        Long placedTimeTime = expiredTime.getTime();
        if (now > placedTimeTime) {
            return true;
        }
        return false;
    }

    public static Calendar addSomeSeconds(Calendar calendar, Integer second) {
        calendar.add(Calendar.SECOND, second);
        return calendar;
    }

    public static String yuanToFenPlainString(BigDecimal p){
        p=p.multiply(new BigDecimal("100"));
        return CommonUtil.toPlain(p);
    }

    public static String toPlain(BigDecimal p){
        return p.stripTrailingZeros().toPlainString();
    }

    public static String timeStamp10(){
        Long timeStamp13 = Calendar.getInstance().getTimeInMillis();
        String timeStamp13Str = timeStamp13.toString();
        return timeStamp13Str.substring(0,timeStamp13Str.length()-3);
    }
}
