package id.ac.itb.if2010.model;

public class ServingCounter extends Station {
    private OrderManager orderManager;
    private PlateStorage linkedStorage;

    public ServingCounter(Position position, OrderManager manager, PlateStorage storage) {
        super("Serving Counter", position);
        this.orderManager = manager;
        this.linkedStorage = storage;
    }

    @Override
    public void interact(ChefPlayer chef) {
        if (chef.isBusy()) return;

        Item item = chef.getInventory();

        if (item instanceof Plate) {
            Plate plate = (Plate) item;
            
            if (plate.getContents().isEmpty()) {
                System.out.println("You can't serve an empty plate!");
                return;
            }
            String dishName = RecipeBook.validateDish(plate.getContents());
            
            if (dishName == null) {
                dishName = "Unknown Dish";
            }
            boolean success = orderManager.deliverDish(dishName);
            
            chef.setInventory(null); 
            
            if (linkedStorage != null) {
                linkedStorage.addDirtyPlate();
                System.out.println("Served " + dishName + "! Plate returned to storage (Dirty).");
            }
            
        } else {
            System.out.println("You need a Plate to serve!");
        }
    }
}