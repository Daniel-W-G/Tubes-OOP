package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;

public class RecipeBook {
    private static List<Recipe> recipes = new ArrayList<>();

    static {
        // sushimate MAP
        Recipe kappaMaki = new Recipe("Kappa Maki");
        kappaMaki.addRequirement("Nori", IngredientState.RAW);
        kappaMaki.addRequirement("Rice", IngredientState.COOKED); // beras  sudah dimasak
        kappaMaki.addRequirement("Cucumber", IngredientState.CHOPPED); // timun  sudah dipotong
        recipes.add(kappaMaki);

        Recipe sakanaMaki = new Recipe("Sakana Maki");
        sakanaMaki.addRequirement("Nori", IngredientState.RAW);
        sakanaMaki.addRequirement("Rice", IngredientState.COOKED);
        sakanaMaki.addRequirement("Fish", IngredientState.RAW); // ikannya mentah
        recipes.add(sakanaMaki);

        Recipe ebiMaki = new Recipe("Ebi Maki");
        ebiMaki.addRequirement("Nori", IngredientState.RAW);
        ebiMaki.addRequirement("Rice", IngredientState.COOKED);
        ebiMaki.addRequirement("Shrimp", IngredientState.COOKED); // udang yang sudah dimasak
        recipes.add(ebiMaki);

        Recipe fishCucumberRoll = new Recipe("Fish Cucumber Roll");
        fishCucumberRoll.addRequirement("Nori", IngredientState.RAW);
        fishCucumberRoll.addRequirement("Rice", IngredientState.COOKED);
        fishCucumberRoll.addRequirement("Fish", IngredientState.RAW); // ikan mentah
        fishCucumberRoll.addRequirement("Cucumber", IngredientState.CHOPPED); // timun udah di chopped 
        recipes.add(fishCucumberRoll);
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
        // dish valid walaupun tidak ada dalam resep
        // contoh nori dan udang di map sushi hidangan yang VALID 
        if (ingredients != null && !ingredients.isEmpty()) {
            return new Dish("Custom Dish", pos, ingredients);
        }
        return null;
    }
    
    /**
     * cari recipe yang cocok sama list preparable
     * dipake untuk dapet nama recipe dari dish
     */
    public static Recipe findMatchingRecipe(List<Preparable> ingredients) {
        for (Recipe r : recipes) {
            if (r.isMatch(ingredients)) {
                return r;
            }
        }
        return null;
    }
}