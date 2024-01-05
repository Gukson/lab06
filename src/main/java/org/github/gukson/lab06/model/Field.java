package org.github.gukson.lab06.model;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Field {
    private Integer freeSpace;
    private Integer occupiedSpace;
    private Plant[] plants; //synchronized?
    private Plant[] seedsQueue;
    private Integer x,y;


    public Field(Integer freeSpace, Integer y, Integer x) {
        this.x = x;
        this.y = y;
        this.freeSpace = freeSpace;
        this.occupiedSpace = 0;
        this.plants = new Plant[4];
        this.seedsQueue = new Plant[1];
    }

    public void setPlantInQueue(String name){
        seedsQueue[0] = new Plant(1,name);
    }

    public Integer getFreeSpace() {
        return freeSpace;
    }

    public Integer getOccupiedSpace() {
        return occupiedSpace;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Plant[] getPlants() {
        return plants;
    }


    public void setFreeSpace(Integer freeSpace) {
        this.freeSpace = freeSpace;
    }

    public Plant[] getSeedsQueue() {
        return seedsQueue;
    }

    public void setOccupiedSpace(Integer occupiedSpace) {
        this.occupiedSpace = occupiedSpace;
    }

    public void setPlants(Plant[] plants) {
        this.plants = plants;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
