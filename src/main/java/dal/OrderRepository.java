package dal;

import domain.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Meijer
 * Created on 03/04/2019
 */
public class OrderRepository {
    private List<Order> orderList = new ArrayList<>();
    private int nextId = 0;

    public void add(Order order) {
        order.setId(nextId);
        orderList.add(order);
        nextId++;
    }
    public Order get(int id) {
        return this.orderList.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .get();
    }
}
