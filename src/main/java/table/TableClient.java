package table;

import domain.*;
import messaging.gateway.TableOrderGateway;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Max Meijer
 * Created on 27/02/2019
 */
public class TableClient {
    private static TableOrderGateway gateway = new TableOrderGateway("TableOrder", "OrderTable");
    private static List<Item> items = new ArrayList<>();

    public static void main(String[] args) {
        initialiseItems();
        for (Item item : items) {
            System.out.println(item);
        }

        Order order = new Order();
        order.addItem(items.get(0));
        order.setOrderTime(new Date());
        order.setTableNumber(1);

        gateway.sendOrder(order);
    }

    private static void initialiseItems() {
        Dish pizza = new Dish();
        pizza.setName("Pizza");
        Ingredient dough = new Ingredient();
        dough.setName("Dough");
        dough.setAmount(1);
        Ingredient sauce = new Ingredient();
        sauce.setName("Sauce");
        sauce.setAmount(1);
        Ingredient cheese = new Ingredient();
        cheese.setName("Cheese");
        cheese.setAmount(1);
        pizza.setIngredients(Arrays.asList(dough, sauce, cheese));

        items.add(pizza);

        Drink coffee = new Drink();
        coffee.setName("Coffee");
        Ingredient sugar = new Ingredient();
        sugar.setName("Sugar");
        sugar.setAmount(1);
        coffee.setIngredients(Arrays.asList(sugar));

        items.add(coffee);
    }
}
