package hub;

import domain.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Meijer
 * Created on 08/04/2019
 */
public class OrderAggregator {
    private List<OrderAggregation> orders = new ArrayList<>();

    public OrderAggregator() {
    }

    public void addOrder(OrderAggregation order) {
        orders.add(order);
    }

    public void completeKitchen(int orderId) {
        orders.forEach(o -> {
            if(o.getOrderId() == orderId) {
                o.completeKitchen();
            }
        });
    }

    public boolean orderComplete(int id) {
        return orders.stream().filter(o -> o.getOrderId() == id).findFirst().get().isComplete();
    }

    public void completeBar(int orderId) {
        orders.forEach(o -> {
            if(o.getOrderId() == orderId) {
                o.completeBar();
            }
        });
    }

    public int getTableNumberForOrder(int orderId) {
        return orders.stream().filter(o -> o.getOrderId() == orderId).findFirst().get().getTableNumber();
    }
}
