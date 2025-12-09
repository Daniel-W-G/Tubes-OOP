package id.ac.itb.if2010.model;


   public class ChefPlayer {

    private String name;
    private Position position;
    private Direction direction;
    private Item heldItem;
    private boolean active;
    private final KitchenMap map;

    public ChefPlayer(String name, Position spawn, KitchenMap map) {
        this.name = name;
        this.position = spawn;
        this.direction = Direction.DOWN;
        this.heldItem = null;
        this.active = true;
        this.map = map;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public Item getHeldItem() {
        return heldItem;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void moveUp() {
        move(Direction.UP);
    }

    public void moveDown() {
        move(Direction.DOWN);
    }

    public void moveLeft() {
        move(Direction.LEFT);
    }

    public void moveRight() {
        move(Direction.RIGHT);
    }

    private void move(Direction dir) {
        if (!active) return;
        this.direction = dir;
        Position target = position.next(dir);
        if (map.isWalkable(target)) {
            this.position = target;
        }
    }

    public void interact() {
        if (!active) return;
        Tile frontTile = map.getTileInFrontOf(position, direction);
        if (frontTile == null) return;
        Station station = frontTile.getStation();
        if (station != null) {
            station.interact(this);
        }
    }

    public void actionHold() {
        if (!active) return;

        Tile frontTile = map.getTileInFrontOf(position, direction);
        if (frontTile == null) return;

        Station station = frontTile.getStation();
        Item itemOnTile = frontTile.getItem();

        if (heldItem == null) {
            if (station instanceof ItemProviderStation) {
                ItemProviderStation provider = (ItemProviderStation) station;
                Item taken = provider.provideItem(this);
                if (taken != null) {
                    heldItem = taken;
                    return;
                }
            }

            if (itemOnTile != null) {
                heldItem = itemOnTile;
                frontTile.setItem(null);
            }
        } else {
            if (station instanceof ItemReceiverStation) {
                ItemReceiverStation receiver = (ItemReceiverStation) station;
                if (receiver.canReceive(heldItem)) {
                    receiver.receiveItem(heldItem, this);
                    heldItem = null;
                    return;
                }
            }

            if (itemOnTile == null) {
                frontTile.setItem(heldItem);
                heldItem = null;
            }
        }
    }

    public void actionUse() {
        if (!active) return;
        Tile frontTile = map.getTileInFrontOf(position, direction);
        if (frontTile == null) return;
        Station station = frontTile.getStation();
        if (station instanceof UsableStation) {
            UsableStation usable = (UsableStation) station;
            usable.use(this);
        }
    }

    public void actionServe() {
        if (!active) return;
        if (!(heldItem instanceof Dish)) return;

        Tile frontTile = map.getTileInFrontOf(position, direction);
        if (frontTile == null) return;

        Station station = frontTile.getStation();
        if (station instanceof ServingCounter) {
            ServingCounter counter = (ServingCounter) station;
            Dish dish = (Dish) heldItem;
            counter.serve(dish, this);
            heldItem = null;
        }
    }

    public void actionTrash() {
        if (!active || heldItem == null) return;

        Tile frontTile = map.getTileInFrontOf(position, direction);
        if (frontTile == null) return;

        Station station = frontTile.getStation();
        if (station instanceof TrashStation) {
            TrashStation trash = (TrashStation) station;
            trash.throwAway(heldItem, this);
            heldItem = null;
        }
    }
}
