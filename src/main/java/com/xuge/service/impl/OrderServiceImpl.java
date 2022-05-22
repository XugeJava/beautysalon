package com.xuge.service.impl;

import com.xuge.common.ResponseData;
import com.xuge.common.StringUtil;
import com.xuge.dao.UserDao;
import com.xuge.entity.Order;
import com.xuge.dao.OrderDao;
import com.xuge.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * (Order)表服务实现类
 *
 * @author makejava
 * @since 2022-05-21 15:42:15
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private UserDao userDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Order queryById(Long id) {
        return this.orderDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param order 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Order> queryByPage(Order order, PageRequest pageRequest) {
        long total = this.orderDao.count(order);
        return new PageImpl<>(this.orderDao.queryAllByLimit(order, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    @Override
    public Order insert(Order order) {
        this.orderDao.insert(order);
        return order;
    }

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    @Override
    public Order update(Order order) {
        this.orderDao.update(order);
        return this.queryById(order.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.orderDao.deleteById(id) > 0;
    }

    @Override
    public ResponseData createOrder(Order order, String token) {
        String username = order.getUsername();
        String usertell = order.getUsertell();
        if(StringUtil.isNull(username)||StringUtil.isNull(usertell)){
            return new ResponseData("9003","参数为空");
        }

        try{
            //1.定义dao层 根据token获取到openid
            String openid = userDao.queryOpenidByToken(token);

            //2.获取系统当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            order.setOpenid(openid);
            order.setPlacedate(sdf.format(new Date()));
            order.setOrderstate("0");//状态默认为0

            log.info("order={}",order.toString());
            //3.生成订单
            orderDao.insert(order);
            return new ResponseData("0","恭喜你,购买成功!");
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseData("9999","网络异常");
        }

    }

    /**
     ● 根据订单状态 和 token 获取订单
     ● @param orderstate
     ● @param token
     ● @return
     */

    @Override
    public ResponseData getOrderByState(String orderstate, String token) {



        try{
//1.根据token获取oipenid
            String openid = userDao.queryOpenidByToken(token);

//2.根据openid和orderstate查询订单表 获取数据

            Order order = new Order(); order.setOpenid(openid); order.setOrderstate(orderstate);
            List<Order> orders = orderDao.queryOrders(order);

            return new ResponseData("0","请求成功！",orders);
        }catch (Exception e){ System.out.println(e);
            return new ResponseData("9999","网络异常！");
        }

    }
}
