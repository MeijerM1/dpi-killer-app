package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Meijer
 * Created on 10/04/2019
 */
public class StoreOrder implements Serializable {
    private List<Ingredient> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient i) {
        ingredients.add(i);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
