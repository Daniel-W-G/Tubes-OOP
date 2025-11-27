package id.ac.itb.if2010.model;

public class CuttingStation extends Station {

    public CuttingStation(Position position) {
        super("Cutting Board", position);
    }

    @Override
    public void interact(ChefPlayer chef) {
        Item item = chef.getInventory();

        if (item != null) {
            if (item instanceof Ingredient) {
                Ingredient ingredient = (Ingredient) item;
                
                if (ingredient.canBeChopped()) {
                    System.out.println("Chopping " + ingredient.getName() + "...");
                    ingredient.chop(); 
                } else {
                    System.out.println("Cannot chop " + ingredient.getName() + " (It might already be chopped/cooked).");
                }
            } else {
                System.out.println("You can't chop a " + item.getName());
            }
        } else {
            System.out.println("You need to hold an ingredient to chop it!");
        }
    }
}