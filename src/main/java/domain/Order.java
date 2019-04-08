package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Max Meijer
 * Created on 03/04/2019
 */
public class Order implements Serializable {
    private int id = -1;
    private List<Item> items = new ArrayList<>();
    private Date orderTime;
    private Date completeTime;
    private int tableNumber;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", items=" + items +
                ", orderTime=" + orderTime +
                ", completeTime=" + completeTime +
                ", tableNumber=" + tableNumber +
                '}';
    }

    public Order copy() {
        Order order = new Order();
        order.setTableNumber(tableNumber);
        order.setOrderTime(orderTime);
        order.setId(id);

        return order;
    }
}
