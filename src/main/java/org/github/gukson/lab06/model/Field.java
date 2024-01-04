package org.github.gukson.lab06.model;

import java.util.ArrayList;

public class Field {
    private Integer freeSpace;
    private Integer occupiedSpace;
    private ArrayList<Plant> plants; //synchronized?

    public Field(Integer freeSpace) {
        this.freeSpace = freeSpace;
        this.occupiedSpace = 0;
    }

    public void grow(){

    }
}
