package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;

public class RecipeBook {
    private static List<Recipe> recipes = new ArrayList<>();

    static {
        Recipe kappaMaki = new Recipe("Kappa Maki");
        kappaMaki.addRequirement("Nori", IngredientState.RAW);
        kappaMaki.addRequirement("Rice", IngredientState.COOKED);
        kappaMaki.addRequirement("Cucumber", IngredientState.CHOPPED);
        recipes.add(kappaMaki);

        Recipe burger = new Recipe("Burger");
        burger.addRequirement("Bun", IngredientState.RAW);
        burger.addRequirement("Meat", IngredientState.COOKED);
        burger.addRequirement("Cheese", IngredientState.CHOPPED);
        recipes.add(burger);

        Recipe pizza = new Recipe("Pizza");
        pizza.addRequirement("Dough", IngredientState.COOKED);
        pizza.addRequirement("Tomato", IngredientState.COOKED);
        pizza.addRequirement("Cheese", IngredientState.COOKED);
        recipes.add(pizza);
    }

    public static List<Recipe> getAllRecipes() {
        return recipes;
    }

    public static Dish createDishIfValid(List<Preparable> ingredients, Position pos) {
        for (Recipe r : recipes) {
            if (r.isMatch(ingredients)) {
                return new Dish(r.getName(), pos, ingredients);
            }
        }
        return null;
    }
}