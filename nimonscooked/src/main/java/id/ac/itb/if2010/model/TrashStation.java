package id.ac.itb.if2010.model;

public class TrashStation extends Station {
    private PlateStorage linkedStorage;

    public TrashStation(Position position, PlateStorage storage) {
        super("Trash Station", position);
        this.linkedStorage = storage;
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
                    if (utensil instanceof Plate) {
                        linkedStorage.addDirtyPlate();
                        linkedStorage.reduceCleanPlate();
                        System.out.println("Plate returned to storage (Dirty).");
                        chef.setInventory(null);
                    }
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