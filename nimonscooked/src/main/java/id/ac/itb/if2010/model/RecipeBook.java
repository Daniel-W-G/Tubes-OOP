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
        
        Recipe sakanaMaki = new Recipe("Sakana Maki");
        sakanaMaki.addRequirement("Nori", IngredientState.RAW);
        sakanaMaki.addRequirement("Rice", IngredientState.COOKED);
        sakanaMaki.addRequirement("Fish", IngredientState.RAW);
        recipes.add(sakanaMaki);

        Recipe ebiMaki = new Recipe("Ebi Maki");
        ebiMaki.addRequirement("Nori", IngredientState.RAW);
        ebiMaki.addRequirement("Rice", IngredientState.COOKED);
        ebiMaki.addRequirement("Shrimp", IngredientState.COOKED);
        recipes.add(ebiMaki);

        Recipe fishcucumberRoll = new Recipe("Fish Cucumber Roll");
        fishcucumberRoll.addRequirement("Nori", IngredientState.RAW);
        fishcucumberRoll.addRequirement("Rice", IngredientState.COOKED);
        fishcucumberRoll.addRequirement("Fish", IngredientState.RAW);
        fishcucumberRoll.addRequirement("Cucumber", IngredientState.CHOPPED);
    }

    public static List<Recipe> getAllRecipes() {
        return recipes;
    }

    public static String validateDish(List<Preparable> ingredients) {
        for (Recipe r : recipes) {
            if (r.isMatch(ingredients)) {
                return r.getName();
            }
        }
        return null;
    }
}