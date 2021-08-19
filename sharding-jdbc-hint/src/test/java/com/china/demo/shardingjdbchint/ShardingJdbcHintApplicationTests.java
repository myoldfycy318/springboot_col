package com.china.demo.shardingjdbchint;

import com.alibaba.fastjson.JSONObject;
import com.china.demo.shardingjdbchint.entity.Order;
import com.china.demo.shardingjdbchint.entity.OrderItem;
import com.china.demo.shardingjdbchint.repository.OrderItemRepository;
import com.china.demo.shardingjdbchint.repository.OrderRepository;
import io.shardingsphere.api.HintManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class  ShardingJdbcHintApplicationTests {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test1() {
        orderRepository.createTableIfNotExists();
        orderItemRepository.createTableIfNotExists();
        orderRepository.truncateTable();
        orderItemRepository.truncateTable();
        processSuccess(false);
    }

    @Test
    public void 不适用sharding框架的生成主键() {
        orderRepository.createTableIfNotExists();
        orderItemRepository.createTableIfNotExists();
        orderRepository.truncateTable();
        orderItemRepository.truncateTable();
        insertData2();
    }

    /**
     * 多张表都扫
     */
    @Test
    public void 不适用分片列查表() {
        String status = "INSERT_TEST6";
        Order order = orderRepository.queryByStatus(status);
        System.out.println(JSONObject.toJSONString(order));
    }

    @Test
    public void test2() {
        orderRepository.createTableIfNotExists();
        orderItemRepository.createTableIfNotExists();
        orderRepository.truncateTable();
        orderItemRepository.truncateTable();
        List<Long> result = new ArrayList<>(3);
        for (int i = 1; i <= 3; i++) {
            Order order = new Order();
            order.setUserId(i);
            order.setStatus("INSERT_TEST");
            HintManager hintManager = HintManager.getInstance();
            hintManager.addDatabaseShardingValue("t_order", i);
            orderRepository.insert(order);


            OrderItem item = new OrderItem();
            item.setOrderId(order.getOrderId());
            item.setUserId(i);
            item.setStatus("INSERT_TEST");
            HintManager.getInstance().addDatabaseShardingValue("t_order_item", i);
            orderItemRepository.insert(item);
            hintManager.close();
            result.add(order.getOrderId());
        }
        printDataAll();
    }

    public void cleanEnvironment() {
        orderRepository.dropTable();
        orderItemRepository.dropTable();
    }

    public void processSuccess(final boolean isRangeSharding) {
        System.out.println("-------------- Process Success Begin ---------------");
        List<Long> orderIds = insertData();
        printData(isRangeSharding);
        //deleteData(orderIds);
        //printData(isRangeSharding);
        System.out.println("-------------- Process Success Finish --------------");
    }

    public void processFailure() {
        System.out.println("-------------- Process Failure Begin ---------------");
        insertData();
        System.out.println("-------------- Process Failure Finish --------------");
        throw new RuntimeException("Exception occur for transaction test.");
    }

    private List<Long> insertData() {
        System.out.println("---------------------------- Insert Data ----------------------------");
        List<Long> result = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            Order order = new Order();
            order.setUserId(i);
            order.setStatus("INSERT_TEST");
            orderRepository.insert(order);

            OrderItem item = new OrderItem();
            item.setOrderId(order.getOrderId());
            item.setUserId(i);
            item.setStatus("INSERT_TEST");
            orderItemRepository.insert(item);

            result.add(order.getOrderId());
        }
        return result;
    }

    private List<Long> insertData2() {
        System.out.println("---------------------------- Insert Data ----------------------------");
        List<Long> result = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            Order order = new Order();
            order.setOrderId(i);
            order.setUserId(i);
            order.setStatus("INSERT_TEST" + i);
            orderRepository.insert2(order);

            OrderItem item = new OrderItem();
            item.setOrderId(order.getOrderId());
            item.setUserId(i);
            item.setStatus("INSERT_TEST");
            orderItemRepository.insert(item);

            result.add(order.getOrderId());
        }
        return result;
    }

    private void deleteData(final List<Long> orderIds) {
        System.out.println("---------------------------- Delete Data ----------------------------");
        for (Long each : orderIds) {
            orderRepository.delete(each);
            orderItemRepository.delete(each);
        }
    }

    public void printData(final boolean isRangeSharding) {
        if (isRangeSharding) {
            printDataRange();
        } else {
            printDataAll();
        }
    }

    private void printDataRange() {
        System.out.println("---------------------------- Print Order Data -----------------------");
        for (Object each : orderRepository.selectRange()) {
            System.out.println(each);
        }
        System.out.println("---------------------------- Print OrderItem Data -------------------");
        for (Object each : orderItemRepository.selectRange()) {
            System.out.println(each);
        }
    }

    private void printDataAll() {
        System.out.println("---------------------------- Print Order Data -----------------------");
        for (Object each : orderRepository.selectAll()) {
            System.out.println(each);
        }
        System.out.println("---------------------------- Print OrderItem Data -------------------");
        for (Object each : orderItemRepository.selectAll()) {
            System.out.println(each);
        }
    }


}
