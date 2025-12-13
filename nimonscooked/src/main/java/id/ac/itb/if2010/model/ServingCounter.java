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
            
            plate.clearContents();
            chef.setInventory(null);

            if (linkedStorage != null) {
                new Thread(() -> {
                    try {
                        Thread.sleep(10000); 
                        linkedStorage.addDirtyPlate();
                        System.out.println("Dirty plate returned to storage after 10 seconds!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                
                System.out.println("Served " + dishName + "! Plate will return in 10 seconds."); 
            }
            
        } else {
            System.out.println("You need a Plate to serve!");
        }
    }
}
