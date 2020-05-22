package com.lin.missyou.api.v1;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.exception.exception.NotFoundException;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.businessObject.PageInfo;
import com.lin.missyou.model.dataAccessObject.Order;
import com.lin.missyou.model.dataTransferObject.OrderDTO;
import com.lin.missyou.model.viewObject.*;
import com.lin.missyou.service.OrderService;
import com.lin.missyou.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("order")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${missyou.order.pay-time-limit}")
    private Long payTimeLimit;

    @ScopeLevel
    @PostMapping
    public OrderIdVO placeOrder(@RequestBody @Validated OrderDTO orderDTO) {
        Long uid = LocalUser.getUser().getId();
        OrderChecker orderChecker = orderService.isOk(uid, orderDTO);
        Long orderId = this.orderService.placeOrder(uid, orderDTO, orderChecker);
        return new OrderIdVO(orderId);
    }

    @ScopeLevel
    @GetMapping("/status/unpaid")
    public DozerPaging getUnpaid(@RequestParam(defaultValue = "0") Integer start,
                                 @RequestParam(defaultValue = "10") Integer count) {
        PageInfo pageInfo = CommonUtil.convertToPageParameter(start, count);
        Page<Order> page = this.orderService.getUnpaid(pageInfo.page, pageInfo.size);
        DozerPaging dozerPaging = new DozerPaging<>(page, OrderSimplifyVO.class);
        dozerPaging.getItems().forEach((o) -> ((OrderSimplifyVO) o).setPeriod(this.payTimeLimit));
        return dozerPaging;
    }

    //ALL/PAID/DELIVERED/FINISHED状态查询
    @ScopeLevel
    @GetMapping("/by/status/{status}")
    public DozerPaging getByStatus(
            @PathVariable int status,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer count) {
        PageInfo pageInfo = CommonUtil.convertToPageParameter(start, count);
        Page<Order> page = this.orderService.getByStatus(status, pageInfo.page, pageInfo.size);
        DozerPaging dozerPaging = new DozerPaging<>(page, OrderSimplifyVO.class);
        dozerPaging.getItems().forEach((o) -> ((OrderSimplifyVO) o).setPeriod(this.payTimeLimit));
        return dozerPaging;
    }

    @ScopeLevel
    @GetMapping("/detail/{id}")
    public OrderPuroVO getOrderDetail(@PathVariable(name = "id") long oid) {
        Optional<Order> orderOptional = this.orderService.getOrderDetail(oid);
        return orderOptional.map((order) -> new OrderPuroVO(order, this.payTimeLimit))
                .orElseThrow(() -> new NotFoundException(50009));
    }

}
