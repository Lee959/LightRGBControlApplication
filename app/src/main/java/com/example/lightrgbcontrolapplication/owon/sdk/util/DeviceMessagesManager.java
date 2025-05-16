package com.example.lightrgbcontrolapplication.owon.sdk.util;

import android.util.Log;

import com.example.lightrgbcontrolapplication.DeviceListBean;
import com.example.lightrgbcontrolapplication.LightRGBDeviceBean;

import java.util.ArrayList;
import java.util.List;


public class DeviceMessagesManager {

    private static final String TAG = "DeviceMessagesManager";
    private static DeviceMessagesManager instance;
    private List<SocketMessageListener> listeners;
    private LightRGBDeviceBean currentLight;

    private DeviceMessagesManager() {
        listeners = new ArrayList<>();
        currentLight = new LightRGBDeviceBean(
                "RGB Light",
                "AA:BB:CC:DD:EE:FF",
                1,
                Constants.LIGHT_EXTEND_LO_COLOR_TEMP_GOODVB,
                true,
                128, // Default brightness (medium)
                185  // Default color temp (medium)
        );
    }

    public static synchronized DeviceMessagesManager getInstance() {
        if (instance == null) {
            instance = new DeviceMessagesManager();
        }
        return instance;
    }

    public void registerMessageListener(SocketMessageListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregisterMessageListener(SocketMessageListener listener) {
        listeners.remove(listener);
    }

    /**
     * Get a list of light devices.
     */
    public void GetEpList() {
        simulateLightDeviceListResponse();
    }

    /**
     * Set the brightness of a light.
     *
     * @param ieee The device's IEEE address.
     * @param ep The device's endpoint.
     * @param brightnessValue The brightness value (0-255).
     */
    public void SmartLightSetBrightness(String ieee, int ep, int brightnessValue) {
        Log.d(TAG, "Setting brightness to: " + brightnessValue);
        currentLight.setBrightness(brightnessValue);

        // Notify listeners
        for (SocketMessageListener listener : listeners) {
            listener.getMessage(Constants.LIGHT_EXTEND_LO_COLOR_TEMP_GOODVB, createLightResponseWithBrightness());
        }
    }

    /**
     * Set the color temperature of a light.
     *
     * @param ieee The device's IEEE address.
     * @param ep The device's endpoint.
     * @param colorTempValue The color temperature value (0-370).
     */
    public void SmartLightSetColorTemp(String ieee, int ep, int colorTempValue) {
        Log.d(TAG, "Setting color temperature to: " + colorTempValue);

        currentLight.setColorTemp(colorTempValue);

        // Notify listeners
        for (SocketMessageListener listener : listeners) {
            listener.getMessage(Constants.LIGHT_EXTEND_LO_COLOR_TEMP_GOODVB, createLightResponseWithColorTemp());
        }
    }

    /**
     * Get the current state of the light device.
     */
    public void getDeviceState(LightRGBDeviceBean device, int cache) {

        for (SocketMessageListener listener : listeners) {
            // Send brightness update
            listener.getMessage(Constants.LIGHT_EXTEND_LO_COLOR_TEMP_GOODVB, createLightResponseWithBrightness());

            // Send color temp update
            listener.getMessage(Constants.LIGHT_EXTEND_LO_COLOR_TEMP_GOODVB, createLightResponseWithColorTemp());
        }
    }

    /* ---------- Helper methods for creating simulated responses ---------- */

    private void simulateLightDeviceListResponse() {
        List<LightRGBDeviceBean> lights = new ArrayList<>();
        lights.add(currentLight);

        DeviceListBean deviceListBean = new DeviceListBean(lights);

        for (SocketMessageListener listener : listeners) {
            listener.getMessage(Constants.ZigBeeGetEPList, deviceListBean);
        }
    }

    private DeviceListBean createLightResponseWithBrightness() {
        List<LightRGBDeviceBean> lights = new ArrayList<>();
        lights.add(currentLight);
        return new DeviceListBean(lights);
    }

    private DeviceListBean createLightResponseWithColorTemp() {
        List<LightRGBDeviceBean> lights = new ArrayList<>();
        lights.add(currentLight);
        return new DeviceListBean(lights);
    }

    /**
     * Get the current light device.
     */
    public LightRGBDeviceBean getCurrentLight() {
        return currentLight;
    }
}