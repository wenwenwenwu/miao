/**
 * @作者 7七月
 * @微信公号 林间有风
 * @开源项目 $ http://7yue.pro
 * @免费专栏 $ http://course.7yue.pro
 * @我的课程 $ http://imooc.com/t/4294850
 * @创建时间 2019-08-19 17:32
 */
package com.lin.missyou.model.dataTransferObject;

import com.lin.missyou.util.HttpRequestProxy;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessDTO {
    private Integer code = 0;
    private String message = "ok";
    private String request = HttpRequestProxy.getRequestUrl();
}
