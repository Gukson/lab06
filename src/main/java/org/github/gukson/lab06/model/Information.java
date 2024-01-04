package org.github.gukson.lab06.model;

public class Information {

    private Integer x;
    private Integer y;
    private String facing;
    private Integer port;

    public Information(Integer y, Integer x, String facing, Integer port) {
        this.x = x;
        this.y = y;
        this.facing = facing;
        this.port = port;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }


    public String getFacing() {
        return facing;
    }

    public Integer getPort() {
        return port;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
