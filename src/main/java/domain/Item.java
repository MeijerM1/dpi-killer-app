package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Meijer
 * Created on 27/03/2019
 */
public abstract class Item implements Serializable {
    private int id = 0;
    private String name;
    private boolean isAvailable = true;
    private List<Ingredient> ingredients = new ArrayList<>();

    public Item() {
        id++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", isAvailable=" + isAvailable +
                ", ingredients=" + ingredients +
                '}';
    }
}
