package com.example.lightrgbcontrolapplication.owon.sdk.util;

/**
 * Constants class that defines various constants used in the application.
 * This includes command IDs for callbacks and device type codes.
 */
public class Constants {

    // Callback command IDs
    public static final int UpdateEPList = 1001;              // Get gateway list callback
    public static final int ZigBeeGetEPList = 1002;           // Get device list callback
    public static final int SmartLightSetupSwitchgear = 1003; // Set light result callback
    public static final int UpdateSwitchgear = 1004;          // Query or control light status callback
    public static final int UpdateLight = 1005;               // Physical control light status callback
    public static final int THI_UPDATE = 1006;                // Temperature and humidity sensor data callback
    public static final int ILLUM_UPDATE = 1007;              // Light sensor data callback
    public static final int MotionSensorUpdate = 1008;        // Motion sensor query status callback
    public static final int MotionSensor = 1009;              // Motion sensor trigger data callback
    public static final int WarningSensor = 1010;             // Smoke detection data callback

    // Device type codes
    public static final int LIGHT_601 = 0x0101;               // Light with only switch
    public static final int LIGHT_EXTEND_LO_COLOR_TEMP_GOODVB = 0x0102; // Adjustable brightness and color temperature light
}