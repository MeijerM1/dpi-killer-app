package table;

import domain.*;
import messaging.gateway.ErrorGateway;
import messaging.gateway.TableOrderGateway;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Max Meijer
 * Created on 27/02/2019
 */
public class TableClient {
    private static final Logger LOGGER = Logger.getLogger("TableClient");
    private static TableOrderGateway gateway = new TableOrderGateway("TableHub", "HubTable", false, true);
    private static ErrorGateway errorGateway = new ErrorGateway("DeadTable");
    private static List<Item> items = new ArrayList<>();
    private static int tableNumber;

    public static void main(String[] args) {
        tableNumber  = Integer.parseInt(args[0]);
        LOGGER.log(Level.INFO, "Started table: " + tableNumber);
        initialiseItems();
        for (Item item : items) {
            System.out.println(item);
        }


        gateway.addListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                int messageTableNumber = -1;
                try {
                    messageTableNumber = message.getIntProperty("tableNumber");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                if(messageTableNumber != tableNumber) {
                    LOGGER.log(Level.INFO, "Got a message for table: " + messageTableNumber + ", ignoring");
                } else {
                    TextMessage text = (TextMessage) message;
                    try {
                        handleMessage(text.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        errorGateway.receiver.addReceiver(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    int table =message.getIntProperty("tableNumber");

                    LOGGER.log(Level.INFO, "Received an error for table: " + table);
                    if(table != tableNumber) {
                        return;
                    }

                    LOGGER.log(Level.INFO, "Something went wrong, please contact a employee");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        Order order = new Order();
        order.addItem(items.get(0)); // Pizza
        // order.addItem(items.get(1)); // Coffee
        order.setOrderTime(new Date());
        order.setTableNumber(tableNumber);

        gateway.sendOrder(order);
    }

    private static void handleMessage(String message) {
        LOGGER.log(Level.INFO, message);
    }

    private static void initialiseItems() {
        Dish pizza = new Dish();
        pizza.setName("Pizza");
        Ingredient dough = new Ingredient();
        dough.setName("dough");
        dough.setAmount(1);
        Ingredient sauce = new Ingredient();
        sauce.setName("sauce");
        sauce.setAmount(1);
        Ingredient cheese = new Ingredient();
        cheese.setName("cheese");
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

        Drink vodka = new Drink();
        vodka.setName("Vodka");
        Ingredient potato = new Ingredient();
        potato.setName("Potato");
        potato.setAmount(1);
        vodka.setIngredients(Arrays.asList(potato));

        items.add(vodka);

    }
}
