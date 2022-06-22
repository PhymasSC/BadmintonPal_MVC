/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badpal.Model;

import java.util.List;

/**
 *
 * @author ASUS
 */
public class Court {

    private String id;
    private String name;
    private String state;
    private String city;
    private float latitude;
    private float longitude;
    private CourtImage images;
    private int priceInCents;
    private List<CourtAvailability> availability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public CourtImage getImages() {
        return images;
    }

    public void setImages(CourtImage images) {
        this.images = images;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public void setPriceInCents(int priceInCents) {
        this.priceInCents = priceInCents;
    }

    public List<CourtAvailability> getAvailability() {
        return availability;
    }

    public void setAvailability(List<CourtAvailability> availability) {
        this.availability = availability;
    }
}
