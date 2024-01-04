package org.github.gukson.lab06.model;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Field {
    private Integer freeSpace;
    private Integer occupiedSpace;
    private ArrayList<Plant> plants; //synchronized?
    private ArrayList<Plant> seedsQueue;
    private Integer x,y;


    public Field(Integer freeSpace, Integer y, Integer x) {
        this.x = x;
        this.y = y;
        this.freeSpace = freeSpace;
        this.occupiedSpace = 0;
        seedsQueue = new ArrayList<>();
        plants = new ArrayList<>();
    }

//    public Field(String json){
//
//    }

//    public void grow(){
//
//    }


    public Integer getFreeSpace() {
        return freeSpace;
    }

    public Integer getOccupiedSpace() {
        return occupiedSpace;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public ArrayList<Plant> getSeedsQueue() {
        return seedsQueue;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
