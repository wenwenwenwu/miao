/**
 * @作者 7七月
 * @微信公号 林间有风
 * @开源项目 $ http://7yue.pro
 * @免费专栏 $ http://course.7yue.pro
 * @我的课程 $ http://imooc.com/t/4294850
 * @创建时间 2019-07-18 10:55
 */
package com.lin.missyou.model.viewObject;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.lin.missyou.model.dataAccessObject.Theme;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class ThemePureVO {
    private Long id;
    private String title;
    private String description;
    private String name;
    private String entranceImg;
    private String extend;
    private String internalTopImg;
    private String titleImg;
    private String tplName;
    private Boolean online;

    public ThemePureVO(Theme theme){
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        mapper.map(theme,this);
    }
}
