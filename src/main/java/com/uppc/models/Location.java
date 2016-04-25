package com.uppc.models;

import java.io.Serializable;

/**
 * Created by songlinwei on 16/4/12.
 */
public class Location implements Serializable {

    private int x;
    private int y;
    private int z;
    private int satNo;

    public Location (int[] coords)
    {
        this.x = coords[0];
        this.y = coords[1];
        this.z = coords[2];
        this.satNo = coords[3];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getSatNo() {
        return satNo;
    }

    public String toString()
    {
        return "x=" + this.x +"cm, y=" + this.y + "cm, z=" + this.z + "cm";
    }
}
