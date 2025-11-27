package id.ac.itb.if2010.model;

import java.util.ArrayList;
import java.util.List;

public class Dish extends Item {
    private List<Preparable> components;

    public Dish(String name, Position position, List<Preparable> components) {
        super(name, position);
        this.components = new ArrayList<>(components); 
    }

    public List<Preparable> getComponents() {
        return components;
    }
}