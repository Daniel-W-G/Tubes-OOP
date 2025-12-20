package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;

public abstract class KitchenUtensil extends Item {
    protected List<Preparable> contents;

    public KitchenUtensil(String name, Position position) {
        super(name, position);
        this.contents = new ArrayList<>();
    }

    public List<Preparable> getContents() {
        return contents;
    }

    public boolean hasItems() {
        return !contents.isEmpty();
    }

    public void clearContents() {
        this.contents.clear();
    }
}