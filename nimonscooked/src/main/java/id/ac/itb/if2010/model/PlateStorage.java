package id.ac.itb.if2010.model;

public class PlateStorage extends Station {
    private int cleanPlateCount;
    private int dirtyPlateCount; 

    public PlateStorage(Position position) {
        super("Plate Storage", position);
        this.cleanPlateCount = 4; 
        this.dirtyPlateCount = 0;
    }
    
    public void addDirtyPlate() {
        this.dirtyPlateCount++;
    }

    public void addCleanPlate() {
        this.cleanPlateCount++;
    }

    public void reduceCleanPlate() {
        if (this.cleanPlateCount > 0) {
            this.cleanPlateCount--;
        }
    }

    public void reduceDirtyPlate() {
        if (this.dirtyPlateCount > 0) {
            this.dirtyPlateCount--;
        }
    }

    public String getStatus() {
        if (dirtyPlateCount > 0) return "DIRTY x" + dirtyPlateCount;
        return "CLEAN x" + cleanPlateCount;
    }

    public int getCleanPlateCount() {
        return cleanPlateCount;
    }

    public int getDirtyPlateCount() {
        return dirtyPlateCount;
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
                    System.out.println("Returned clean plate. (Total: " + cleanPlateCount + ")");
                    chef.setInventory(null);
                } else {
                    System.out.println("Don't put dirty plates here manually. Put them in the Sink!");
                }
            } else {
                System.out.println("Only Plates belong here.");
            }
            return;
        }

        
        if (dirtyPlateCount > 0) {
            Plate dirtyStack = new Plate(chef.getPosition());
            dirtyStack.setClean(false);
            dirtyStack.setStackSize(dirtyPlateCount);
            
            chef.setInventory(dirtyStack);
            System.out.println("Took " + dirtyPlateCount + " dirty plates.");
            
            dirtyPlateCount = 0; 
        } 
        else if (cleanPlateCount > 0) {
            Plate cleanPlate = new Plate(chef.getPosition());
            chef.setInventory(cleanPlate);
            System.out.println("Took 1 clean plate. (" + (cleanPlateCount - 1) + " left)");
            
            cleanPlateCount--;
        } 
        else {
            System.out.println("Storage is empty!");
        }
    }
}