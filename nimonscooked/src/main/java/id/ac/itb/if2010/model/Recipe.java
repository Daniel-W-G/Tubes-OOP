package id.ac.itb.if2010.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recipe {
    private String name;
    private Map<String, IngredientState> requirements;

    public Recipe(String name) {
        this.name = name;
        this.requirements = new HashMap<>();
    }

    public void addRequirement(String ingredientName, IngredientState state) {
        requirements.put(ingredientName, state);
    }

    public String getName() {
        return name;
    }

    public boolean isMatch(List<Preparable> components) {
        if (components.size() != requirements.size()) return false;

        Map<String, IngredientState> checklist = new HashMap<>(requirements);

        for (Preparable p : components) {
            if (p instanceof Ingredient) {
                Ingredient ing = (Ingredient) p;
                String key = ing.getName();
                
                if (checklist.containsKey(key) && checklist.get(key) == ing.getState()) {
                    checklist.remove(key); 
                } else {
                    return false; 
                }
            } else {
                return false; 
            }
        }
        return checklist.isEmpty();
    }
}