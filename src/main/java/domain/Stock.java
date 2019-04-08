package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Max Meijer
 * Created on 04/04/2019
 */
public class Stock {
    private static List<Ingredient> currentStock = new ArrayList<>();

    public Stock(int amount) {
        initStock(amount);
    }

    private int getIngredientAmount(Ingredient i) {
        return currentStock.stream()
                .filter(s -> s.getName().equalsIgnoreCase(i.getName()))
                .findFirst().get().getAmount();
    }


    // Initialise current stock with some ingredients
    private void initStock(int amount) {
        Ingredient dough = new Ingredient();
        dough.setName("dough");
        dough.setAmount(amount);
        currentStock.add(dough);
        Ingredient cheese = new Ingredient();
        cheese.setName("cheese");
        cheese.setAmount(amount);
        currentStock.add(cheese);
        Ingredient sauce = new Ingredient();
        sauce.setName("sauce");
        sauce.setAmount(amount);
        currentStock.add(sauce);
        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        sugar.setAmount(amount);
        currentStock.add(sugar);
    }

    private void useIngredientForItem(Item i) {
        for (Ingredient ing : i.getIngredients()) {
            Ingredient stock = currentStock.stream().filter(s -> s.getName().equalsIgnoreCase(ing.getName())).findFirst().get();
            stock.setAmount(stock.getAmount() - ing.getAmount());
        }
    }

    public void useIngredientsForOrder(Order order) {
        for (Item i : order.getItems()) {
            useIngredientForItem(i);
        }
    }

    // Check whether there are enough ingredients for a dish.
    public boolean hasStock(Item item) {
        for (Ingredient i : item.getIngredients()) {
            if(getIngredientAmount(i) < i.getAmount()) {
                return false;
            }
        }

        return true;
    }

    // Return a list f all ingredients that are empty
    public List<Ingredient> checkEmpty() {
        return currentStock.stream()
                .filter(i -> i .getAmount() <= 0)
                .collect(Collectors.toList());
    }

}
