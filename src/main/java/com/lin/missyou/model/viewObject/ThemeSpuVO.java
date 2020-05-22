/**
 * @作者 7七月
 * @微信公号 林间有风
 * @开源项目 $ http://7yue.pro
 * @免费专栏 $ http://course.7yue.pro
 * @我的课程 $ http://imooc.com/t/4294850
 * @创建时间 2019-07-18 11:22
 */
package com.lin.missyou.model.viewObject;

import com.lin.missyou.model.dataAccessObject.Theme;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@NoArgsConstructor
public class ThemeSpuVO extends ThemePureVO {

    private List<SpuSimplify> spuList;

}
