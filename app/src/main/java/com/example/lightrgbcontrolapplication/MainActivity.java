package com.example.lightrgbcontrolapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.lightrgbcontrolapplication.owon.sdk.util.Constants;
import com.example.lightrgbcontrolapplication.owon.sdk.util.DeviceMessagesManager;
import com.example.lightrgbcontrolapplication.owon.sdk.util.SocketMessageListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SocketMessageListener {

    private static final String TAG = "MainActivity";

    // UI components
    private TextView tvLightStatus;
    private SeekBar seekBarBrightness;
    private SeekBar seekBarColorTemp;
    private TextView tvBrightnessValue;
    private TextView tvColorTempValue;
    private View viewLightIndicator;
    private Button btnRefresh;

    // Device manager
    private DeviceMessagesManager deviceManager;
    private LightDeviceBean currentLight;

    // Flags to prevent infinite loop of updates
    private boolean isUserAdjustingBrightness = false;
    private boolean isUserAdjustingColorTemp = false;

    // Handler for UI updates
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize handler
        handler = new Handler(Looper.getMainLooper());

        // Initialize UI components
        tvLightStatus = findViewById(R.id.tvLightStatus);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);
        seekBarColorTemp = findViewById(R.id.seekBarColorTemp);
        tvBrightnessValue = findViewById(R.id.tvBrightnessValue);
        tvColorTempValue = findViewById(R.id.tvColorTempValue);
        viewLightIndicator = findViewById(R.id.viewLightIndicator);
        btnRefresh = findViewById(R.id.btnRefresh);

        // Initialize device manager
        deviceManager = DeviceMessagesManager.getInstance();
        deviceManager.registerMessageListener(this);

        // Set up the brightness SeekBar
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    tvBrightnessValue.setText("亮度值: " + progress);
                    updateLightIndicator();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserAdjustingBrightness = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserAdjustingBrightness = false;
                if (currentLight != null) {
                    deviceManager.SmartLightSetBrightness(
                            currentLight.getIeee(),
                            currentLight.getEp(),
                            seekBar.getProgress());
                }
            }
        });

        // Set up the color temperature SeekBar
        seekBarColorTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    tvColorTempValue.setText("色温值: " + progress);
                    updateLightIndicator();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserAdjustingColorTemp = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserAdjustingColorTemp = false;
                if (currentLight != null) {
                    deviceManager.SmartLightSetColorTemp(
                            currentLight.getIeee(),
                            currentLight.getEp(),
                            seekBar.getProgress());
                }
            }
        });

        // Set up the refresh button
        btnRefresh.setOnClickListener(v -> {
            if (currentLight != null) {
                deviceManager.getDeviceState(currentLight, 0);
            } else {
                deviceManager.GetEpList();
            }
        });

        // Get initial list of devices
        deviceManager.GetEpList();
    }

    @Override
    protected void onDestroy() {
        deviceManager.unregisterMessageListener(this);
        super.onDestroy();
    }

    /**
     * Implements SocketMessageListener.getMessage to receive updates from the device manager.
     */
    @Override
    public void getMessage(int commandID, Object bean) {
        Log.d(TAG, "Received message with command ID: " + commandID);

        switch (commandID) {
            case Constants.ZigBeeGetEPList:
                handleDeviceListUpdate((DeviceListBean) bean);
                break;

            case Constants.LIGHT_EXTEND_LO_COLOR_TEMP_GOODVB:
                handleBrightnessUpdate((DeviceListBean) bean);
                break;

            default:
                Log.d(TAG, "Unhandled command ID: " + commandID);
                break;
        }
    }

    /**
     * Handle updates to the list of devices.
     */
    private void handleDeviceListUpdate(DeviceListBean deviceListBean) {
        List<LightDeviceBean> devices = deviceListBean.getDevices();
        if (devices != null && !devices.isEmpty()) {
            // For simplicity, just use the first light device
            LightDeviceBean device = devices.get(0);
            if (device.getDeviceType() == Constants.LIGHT_EXTEND_LO_COLOR_TEMP_GOODVB) {
                currentLight = device;

                // Update UI on main thread
                handler.post(() -> {
                    // Update status
                    tvLightStatus.setText(device.isLinkStatus() ? "在线" : "离线");
                    tvLightStatus.setTextColor(device.isLinkStatus() ? Color.GREEN : Color.RED);

                    // Get current state
                    deviceManager.getDeviceState(currentLight, 0);
                });
            }
        }
    }

    /**
     * Handle updates to the light brightness.
     */
    private void handleBrightnessUpdate(DeviceListBean deviceListBean) {
        List<LightDeviceBean> devices = deviceListBean.getDevices();
        if (devices != null && !devices.isEmpty() && !isUserAdjustingBrightness) {
            LightDeviceBean device = devices.get(0);

            // Update UI on main thread
            handler.post(() -> {
                seekBarBrightness.setProgress(device.getBrightness());
                tvBrightnessValue.setText("亮度值: " + device.getBrightness());
                updateLightIndicator();
            });
        }
    }

    /**
     * Handle updates to the light color temperature.
     */
    private void handleColorTempUpdate(DeviceListBean deviceListBean) {
        List<LightDeviceBean> devices = deviceListBean.getDevices();
        if (devices != null && !devices.isEmpty() && !isUserAdjustingColorTemp) {
            LightDeviceBean device = devices.get(0);

            // Update UI on main thread
            handler.post(() -> {
                seekBarColorTemp.setProgress(device.getColorTemp());
                tvColorTempValue.setText("色温值: " + device.getColorTemp());
                updateLightIndicator();
            });
        }
    }

    /**
     * Update the light indicator view based on current brightness and color temperature.
     */
    private void updateLightIndicator() {
        if (viewLightIndicator != null) {
            // Calculate color based on brightness and color temperature
            // For simplicity:
            // - Brightness affects alpha (transparency)
            // - Color temperature affects the actual color (from cool/blue to warm/yellow)

            int brightness = seekBarBrightness.getProgress();
            int colorTemp = seekBarColorTemp.getProgress();

            // Convert brightness (0-255) to alpha (0-255)
            int alpha = brightness;

            // Convert color temperature (0-370) to a color (blue to yellow)
            // Lower value = cooler/blue, higher value = warmer/yellow
            float colorRatio = colorTemp / 370.0f;

            // Interpolate between cool (blueish) and warm (yellowish) colors
            int red = (int) (255 * Math.min(1.0f, colorRatio * 1.5f));
            int green = (int) (255 * Math.min(1.0f, colorRatio));
            int blue = (int) (255 * Math.max(0.0f, 1.0f - colorRatio * 1.5f));

            // Create color with calculated components
            int color = Color.argb(alpha, red, green, blue);

            // Apply color to the light indicator
            viewLightIndicator.setBackgroundColor(color);
        }
    }
}