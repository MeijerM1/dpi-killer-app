package hub;

/**
 * @author Max Meijer
 * Created on 08/04/2019
 */
public class OrderAggregation {
    private int orderId;
    private int tableNumber;
    private boolean kitchenOrder = false;
    private boolean barOrder= false;
    private boolean kitchenComplete = false;
    private boolean barComplete= false;

    public OrderAggregation() {
    }

    public OrderAggregation(int orderId, int tableNumber, boolean isKitchen, boolean isBar) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.kitchenOrder = isKitchen;
        this.barOrder = isBar;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setKitchenOrder(boolean kitchenOrder) {
        this.kitchenOrder = kitchenOrder;
    }

    public void setBarOrder(boolean barOrder) {
        this.barOrder = barOrder;
    }

    public void completeKitchen() {
        kitchenComplete = true;
    }

    public void completeBar() {
        barComplete = true;
    }

    public boolean isKitchen() {
        return kitchenOrder;
    }

    public boolean isBar() {
        return barOrder;
    }

    public boolean isComplete() {
        if(kitchenOrder && !barOrder) {
            return kitchenComplete;
        } else if(!kitchenOrder && barOrder) {
            return barComplete;
        } else {
            return (kitchenComplete && barComplete);
        }
    }
}
