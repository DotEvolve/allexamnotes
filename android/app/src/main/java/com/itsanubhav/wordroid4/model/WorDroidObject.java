package com.itsanubhav.wordroid4.model;

public class WorDroidObject {

    private Object object;
    private int isAdvertisement;
    private int layoutType;
    private int position;

    public WorDroidObject(Object object, int layoutType) {
        this.object = object;
        this.layoutType = layoutType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getIsAdvertisement() {
        return isAdvertisement;
    }

    public void setIsAdvertisement(int isAdvertisement) {
        this.isAdvertisement = isAdvertisement;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }
}
