package id.ac.itb.if2010.model;

public class TrashStation extends Station {

    public TrashStation(Position position) {
        super("Trash Station", position);
    }

    @Override
    public void interact(ChefPlayer chef) {
        Item item = chef.getInventory();

        if (item != null) {
            if (item instanceof KitchenUtensil) {
                KitchenUtensil utensil = (KitchenUtensil) item;

                if (utensil.hasItems()) {
                    utensil.clearContents(); 
                    System.out.println("Emptied the " + utensil.getName() + " into the trash.");
                } else {
                    System.out.println("The " + utensil.getName() + " is already empty!");
                }
            } 
            else {
                System.out.println("Threw away " + item.getName());
                chef.setInventory(null);
            }
        } else {
            System.out.println("Your hands are empty, nothing to trash.");
        }
    }
}