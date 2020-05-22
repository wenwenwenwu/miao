package com.lin.missyou.model.businessObject;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PageInfo {
    public Integer size;
    public Integer page;
}
