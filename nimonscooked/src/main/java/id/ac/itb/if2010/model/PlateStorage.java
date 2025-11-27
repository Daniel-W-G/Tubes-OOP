package id.ac.itb.if2010.model;

public class PlateStorage extends Station {
    private int cleanPlateCount;

    public PlateStorage(Position position) {
        super("Plate Storage", position);
        this.cleanPlateCount = 5; 
    }
    
    public String getStatus() {
        return "CLEAN x" + cleanPlateCount;
    }

    @Override
    public void interact(ChefPlayer chef) {
        Item item = chef.getInventory();

        if (item != null) {
            if (item instanceof Plate) {
                Plate p = (Plate) item;
                
                if (p.hasItems()) {
                    System.out.println("Empty the plate first!");
                    return;
                }

                if (p.isClean()) {
                    cleanPlateCount++;
                    System.out.println("Returned clean plate to storage. (Total: " + cleanPlateCount + ")");
                    chef.setInventory(null);
                } else {
                    System.out.println("Can't put dirty plates here! Put them on a table.");
                }
            } else {
                System.out.println("You can only store Plates here.");
            }
            return;
        }

        if (cleanPlateCount > 0) {
            Plate cleanPlate = new Plate(chef.getPosition());
            chef.setInventory(cleanPlate);
            System.out.println("Took 1 clean plate. (" + (cleanPlateCount - 1) + " left)");
            cleanPlateCount--;
        } 
        else {
            System.out.println("Storage is empty! Wash some plates!");
        }
    }
}