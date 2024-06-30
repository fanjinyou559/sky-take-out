package com.sky.task;


import com.sky.dto.OrdersConfirmDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;
    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 1 * * * ?")
    public void processTimeoutOrder(){
        log.info("处理超时订单: {}" ,new Date());
        //1.每隔一分钟查询超时订单
        LocalDateTime orderTime = LocalDateTime.now();
        orderTime.plusMinutes(-15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrdertimeLT(Orders.PENDING_PAYMENT, orderTime);

        if (ordersList != null && ordersList.size()>0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("支付超时 ,已取消");
                orderMapper.update(orders);
            }
        }
    }


    /**
     * 处理"派送中"状态订单
     */
    @Scheduled(cron = "* * 1 * * ?")
    public void processDeliveryOrder(){
        log.info("处理派送中订单: ",new Date());

        LocalDateTime orderTime = LocalDateTime.now();
        //处理昨天的订单
        orderTime.plusMinutes(-60);

        List<Orders> ordersList = orderMapper.getByStatusAndOrdertimeLT(Orders.DELIVERY_IN_PROGRESS, orderTime);

        if (ordersList != null && ordersList.size()>0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
