package orders;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order {
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Order() {}

    public static Order createIngredients() {
        return new Order(new ArrayList<>(Arrays.asList("59f21i9330xl2r380a1jr9vb", "59f21i9330xl2r380a1jl55y")));
    }

    public static Order createIngredientsWithoutCorrectHash() {
        return new Order(new ArrayList<>(Arrays.asList(RandomStringUtils.randomAlphabetic(24), RandomStringUtils.randomAlphabetic(24))));
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}