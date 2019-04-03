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
    private boolean isAvialable = true;
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

    public boolean isAvialable() {
        return isAvialable;
    }

    public void setAvialable(boolean avialable) {
        isAvialable = avialable;
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
                ", isAvialable=" + isAvialable +
                ", ingredients=" + ingredients +
                '}';
    }
}
