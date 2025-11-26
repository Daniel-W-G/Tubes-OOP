package id.ac.itb.if2010.model;

public class IngredientStorage extends Station {
    private String ingredientName;

    public IngredientStorage(Position position, String ingredientName) {
        super("Crate of " + ingredientName, position);
        this.ingredientName = ingredientName;
    }

    @Override
    public void interact(ChefPlayer chef) {
        if (chef.getInventory() == null) {
            Ingredient newIngredient = new Ingredient(this.ingredientName, chef.getPosition());
            chef.setInventory(newIngredient);
            System.out.println("Chef picked up " + ingredientName);
        } else {
            System.out.println("Chef's hands are full! Cannot take " + ingredientName);
        }
    }
}