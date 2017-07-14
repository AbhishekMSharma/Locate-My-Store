package com.abhishekmsharma.locatemystore2;

/**
 * Created by Abhishek Sharma on 23/04/2015.
 */
public class LocationHistory {
    private int lId;
    private String lName;
    private String lLat;
    private String lLong;

    public LocationHistory(int i, String n, String lati, String longi)
    {
        lId = i;
        lName = n;
        lLat = lati;
        lLong = longi;
    }
    public int getlId(){return lId;}
    public String getlName(){return lName;}
    public String getlLat(){return lLat;}
    public String getlLong(){return lLong;}
}
