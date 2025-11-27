package id.ac.itb.if2010.model;

public class WashingStation extends Station {
    private int dirtyPlatesInSink;
    private int cleanPlatesReady;
    private int washProgress;
    private boolean isWashing;

    public WashingStation(Position position) {
        super("Sink", position);
        this.dirtyPlatesInSink = 0;
        this.cleanPlatesReady = 0;
        this.washProgress = 0;
        this.isWashing = false;
    }
    
    public boolean hasPlates() { return dirtyPlatesInSink > 0 || cleanPlatesReady > 0; }

    @Override
    public void interact(ChefPlayer chef) {
        Item item = chef.getInventory();

        if (item instanceof Plate) {
            Plate p = (Plate) item;
            if (!p.isClean()) {
                dirtyPlatesInSink += p.getStackSize();
                chef.setInventory(null);
                System.out.println("Put " + p.getStackSize() + " dirty plates in sink.");
            } else {
                System.out.println("That plate is already clean!");
            }
        }
        else if (item == null) {
            if (cleanPlatesReady > 0) {
                Plate p = new Plate(chef.getPosition());
                chef.setInventory(p);
                cleanPlatesReady--;
                System.out.println("Picked up a washed plate.");
            }
            else if (dirtyPlatesInSink > 0) {
                wash();
            } else {
                System.out.println("Sink is empty.");
            }
        }
    }
    
    private void wash() {
        washProgress += 25;
        System.out.println("Scrubbing... " + washProgress + "%");
        
        if (washProgress >= 100) {
            dirtyPlatesInSink--;
            cleanPlatesReady++; 
            washProgress = 0;
            System.out.println("Plate Cleaned! (" + dirtyPlatesInSink + " dirty left)");
        }
    }
}