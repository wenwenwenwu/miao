package com.lin.missyou.api.v1;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.lin.missyou.model.businessObject.PageInfo;
import com.lin.missyou.exception.exception.NotFoundException;
import com.lin.missyou.model.dataAccessObject.Spu;
import com.lin.missyou.model.viewObject.DozerPaging;
import com.lin.missyou.service.SpuService;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.model.viewObject.SpuSimplify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

//收发数据
@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {
    @Autowired
    private SpuService spuService;

    @GetMapping("/id/{id}/detail")
    public Spu getDetail(@PathVariable @Positive(message = "{id.positive}") Long id){
        Spu spu = spuService.getSpu(id);
        if (spu == null){
            throw new NotFoundException(30002);
        }
        return spu;
    }

    @GetMapping("simplify/id/{id}")
    public SpuSimplify getSimplifySpu(@PathVariable @Positive(message = "{id.positive}") Long id){
        Spu spu = spuService.getSpu(id);
        if (spu == null){
            throw new NotFoundException(30002);
        }
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        SpuSimplify spuSimplify = mapper.map(spu, SpuSimplify.class);
        return spuSimplify;
    }

    @GetMapping("allList")
    public List<Spu>getAllList(){
        return spuService.getAll();
    }



    @GetMapping("/latest")
    public DozerPaging<Spu,SpuSimplify> getLatestSpuList(
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "20") Integer count){
        PageInfo pageInfo = CommonUtil.convertToPageParameter(start, count);
        Page<Spu> page = this.spuService.getPagingSpu(pageInfo.getPage(), pageInfo.getSize());
        return new DozerPaging<>(page, SpuSimplify.class);
    }

    @GetMapping("/by/category/{categoryId}")
    public DozerPaging<Spu, SpuSimplify> getSpuList(
            @PathVariable Long categoryId,
            @RequestParam(name = "is_root",defaultValue = "false") Boolean isRoot,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "20") Integer count){
    PageInfo pageInfo = CommonUtil.convertToPageParameter(start,count);
    Page<Spu> page = this.spuService.getPagingSpu(categoryId, isRoot, pageInfo.getPage(), pageInfo.getSize());
    return  new DozerPaging<>(page, SpuSimplify.class);
    }
}
