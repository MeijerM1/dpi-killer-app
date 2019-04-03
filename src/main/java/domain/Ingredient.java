package domain;

import java.io.Serializable;

/**
 * @author Max Meijer
 * Created on 03/04/2019
 */
public class Ingredient implements Serializable {
    private String name;
    private int amount;

    public Ingredient(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
