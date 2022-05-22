package com.xuge.controller;

import com.xuge.common.ResponseData;
import com.xuge.entity.Order;
import com.xuge.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Order)表控制层
 *
 * @author makejava
 * @since 2022-05-21 15:42:13
 */
@RestController
@RequestMapping("order")
@Api(tags = {"订单管理"})
public class OrderController {
  /**
   * 服务对象
   */
  @Resource
  private OrderService orderService;

  @GetMapping("getOrderByState")
  public ResponseData getOrderByState(String orderstate, String token) {
    System.out.println("orderstate = " + orderstate);
    System.out.println("token = " + token);
    return orderService.getOrderByState(orderstate, token);
  }

  @PostMapping("createOrder")
  public ResponseData createOrder(Order order, String token) {
    System.out.println("token = " + token);
    System.out.println("order = " + order);

    return orderService.createOrder(order, token);
  }

  /**
   * 分页查询
   *
   * @param order       筛选条件
   * @param pageRequest 分页对象
   * @return 查询结果
   */
  @GetMapping
  public ResponseEntity<Page<Order>> queryByPage(Order order, PageRequest pageRequest) {
    return ResponseEntity.ok(this.orderService.queryByPage(order, pageRequest));
  }

  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("{id}")
  public ResponseEntity<Order> queryById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(this.orderService.queryById(id));
  }

  /**
   * 新增数据
   *
   * @param order 实体
   * @return 新增结果
   */
  @PostMapping
  public ResponseEntity<Order> add(Order order) {
    return ResponseEntity.ok(this.orderService.insert(order));
  }

  /**
   * 编辑数据
   *
   * @param order 实体
   * @return 编辑结果
   */
  @PutMapping
  public ResponseEntity<Order> edit(Order order) {
    return ResponseEntity.ok(this.orderService.update(order));
  }

  /**
   * 删除数据
   *
   * @param id 主键
   * @return 删除是否成功
   */
  @DeleteMapping
  public ResponseEntity<Boolean> deleteById(Long id) {
    return ResponseEntity.ok(this.orderService.deleteById(id));
  }

}

