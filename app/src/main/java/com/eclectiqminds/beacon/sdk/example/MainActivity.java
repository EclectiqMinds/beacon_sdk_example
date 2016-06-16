package com.eclectiqminds.beacon.sdk.example;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.eclectiqminds.beacon.sdk.local.app.ScanSettings;
import com.eclectiqminds.beacon.sdk.local.app.ScannerService;
import com.eclectiqminds.beacon.sdk.local.app.ScannerServiceListener;
import com.eclectiqminds.beacon.sdk.remote.beacon.Beacon;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ScannerServiceListener {

    @Bind(R.id.listview)
    ListView listView;

    @Bind(R.id.state_textview)
    TextView stateTextView;

    private ListAdapter adapter;
    private ScannerService scannerService;
    private boolean shouldServiceShouldDownByOnStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter = new ListAdapter(this);
        listView.setAdapter(adapter);
        scannerService = ScannerService.instance(getApplication(), true);
    }

    @Override
    protected void onStop() {
        if (shouldServiceShouldDownByOnStop) {
            scannerService.stopForegroundScanning();
        }
        super.onStop();
    }

    @Override
    public void onBeaconDetected(Beacon beacon) {
        if (adapter != null) {
            adapter.add(beacon);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        Log.d("TAG", throwable.getMessage());
        showStopState();
    }

    @OnClick(R.id.button_normal_start)
    public void onStartForegroundScanClicked() {
        shouldServiceShouldDownByOnStop = true;
        showActiveForegroundScanning();
        startForegroundScanning();
    }

    @OnClick(R.id.button_silent_start)
    public void onStartBackgroundScanningClicked() {
        shouldServiceShouldDownByOnStop = false;
        showActiveBackgroundScanning();
        startBackgroundScanning();
    }

    @OnClick(R.id.button_normal_stop)
    public void onStopNormalScanningClicked() {
        showStopState();
        scannerService.stopForegroundScanning();
    }

    @OnClick(R.id.button_silent_stop)
    public void onStopBackgroundScanningClicked() {
        showStopState();
        scannerService.stopBackgroundScanning();
    }

    /**
     * Starts a new service in a separate process. The results are delivered through a listener interface.
     * Please note, that Android OS may kill your app's process if it's in the background. Please call the StopScanning() method in your
     * application's onStop() callback method.
     */
    private void startForegroundScanning() {
        scannerService.startForegroundScanning(this, normalScanSettings);
    }

    /**
     * Starts a remote service in a new process which scans for beacons in background independently of the phones state.
     * Scanning is also triggered in deep-sleep mode.
     *
     * @param scanSettings            settings.
     * @param shouldStartAfterBooting if true background scanner service starts when device is booted up.
     */
    private void startBackgroundScanning() {
        scannerService.startBackgroundScanning(backgroundScanSettings, false);
    }

    private void resetAdapter() {
        if (adapter != null) {
            adapter.reset();
        }
    }

    private void showStopState() {
        shouldServiceShouldDownByOnStop = false;
        setStateText("Scanning stopped.");
        setStateColor(ContextCompat.getColor(this, R.color.red));
    }

    private void showActiveForegroundScanning() {
        setStateText("Normal scan is in progress.");
        setStateColor(ContextCompat.getColor(this, R.color.green));
        resetAdapter();
    }

    private void showActiveBackgroundScanning() {
        setStateText("Background scan is in progress.");
        setStateColor(ContextCompat.getColor(this, R.color.green));
        resetAdapter();
    }

    /**
     * @param uuidFilter             (required) - filter String for beacon scanning
     * Format: "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX"
     * @param scanInterval           (required) - interval of one scanning period in ms.
     * Example: 5000 - the service will scan 5000 ms after Beacon devices.
     * Recommended value is: 5000 ms.
     * @param scanPeriod             (required) - frequency of the scanning in ms.
     * Example: 10000 - the time interval between two scanning is 5000 ms.
     * Recommended value is: 10000 ms.
     * If you start a new background scanning, you could add -1 as a value and
     * then an optimizing algorithm is setting the period depend on the user's activity. (Running, travelling, not moving)
     * @param userActivityScanPeriod (optional) - frequency of user's activity state scanning in ms.
     * Used while backgrounds scanning for optimizing battery consumption.
     * Recommended value is: 60000 ms.
     */
    private ScanSettings normalScanSettings = new ScanSettings("c9407f30-f5f8-466e-aff9-25556b57fe6d", 5000, 10000, 60000);

    private ScanSettings backgroundScanSettings = new ScanSettings("c9407f30-f5f8-466e-aff9-25556b57fe6d", 5000, -1, 60000);

    private void setStateText(String state) {
        stateTextView.setText(state);
    }

    private void setStateColor(int resId) {
        stateTextView.setBackgroundColor(resId);
    }

}
