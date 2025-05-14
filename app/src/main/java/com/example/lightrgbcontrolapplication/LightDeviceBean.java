package com.example.lightrgbcontrolapplication;

public class LightDeviceBean {
    private String name;
    private String ieee;
    private int ep;
    private int deviceType;
    private boolean linkStatus;
    private int brightness;
    private int colorTemp;

    public LightDeviceBean(String name, String ieee, int ep, int deviceType, boolean linkStatus, int brightness, int colorTemp) {
        this.name = name;
        this.ieee = ieee;
        this.ep = ep;
        this.deviceType = deviceType;
        this.linkStatus = linkStatus;
        this.brightness = brightness;
        this.colorTemp = colorTemp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIeee() {
        return ieee;
    }

    public void setIeee(String ieee) {
        this.ieee = ieee;
    }

    public int getEp() {
        return ep;
    }

    public void setEp(int ep) {
        this.ep = ep;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(boolean linkStatus) {
        this.linkStatus = linkStatus;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getColorTemp() {
        return colorTemp;
    }

    public void setColorTemp(int colorTemp) {
        this.colorTemp = colorTemp;
    }
}