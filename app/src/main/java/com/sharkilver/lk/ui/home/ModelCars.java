package com.sharkilver.lk.ui.home;

public class ModelCars {


    private String idcar;
    private double lat;
    private double lng;


    public ModelCars(String idcar, double lat, double lng) {
        this.idcar = idcar;
        this.lat = lat;
        this.lng = lng;
    }

    public String getIdcar() {
        return idcar;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
