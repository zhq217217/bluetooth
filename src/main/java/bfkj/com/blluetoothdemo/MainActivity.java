package bfkj.com.blluetoothdemo;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements BlueToothManager.OnBluetoothStateChangedListener, BlueToothManager.OnDiscoveryStateChangedListener, BlueToothManager.OnNewDeviceFoundListener {
    private boolean mBlueToothIsOpen = false;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BlueToothManager.getBlueToothManager().onCreate();
        if (mBlueToothIsOpen = BlueToothManager.getBlueToothManager().getBoothToothIsEnable()) {
        } else {
            BlueToothManager.getBlueToothManager().openBluetooth(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BlueToothManager.getBlueToothManager().setOnBluetoothStateChangedListener(this);
        BlueToothManager.getBlueToothManager().setOnDiscoveryStateChangedListener(this);
        BlueToothManager.getBlueToothManager().setOnNewDeviceFoundListener(this);
        BlueToothManager.getBlueToothManager().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BlueToothManager.getBlueToothManager().onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BlueToothManager.REQUEST_OPEN_BLUETOOTH && resultCode == RESULT_OK) {
            mBlueToothIsOpen = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startScan(View view) {
        BlueToothManager.getBlueToothManager().startDiscoveryDevice();
    }

    public void connectDevice(View view) {
        BlueToothManager.getBlueToothManager().connectDevice(this, address);
    }

    @Override
    public void onBluetoothStateOn() {
        Log.e("TAG", "onBluetoothStateOn: 1111111111111");
    }

    @Override
    public void onBluetoothStateOff() {
        Log.e("TAG", "onBluetoothStateOff: 2222222222");
    }

    @Override
    public void onBluetoothStateTurnningOn() {
        Log.e("TAG", "onBluetoothStateTurnningOn: 33333333333");
    }

    @Override
    public void onBluetoothSateTurnningOff() {
        Log.e("TAG", "onBluetoothSateTurnningOff: 444444444444");
    }

    @Override
    public void onDiscoveryStart() {
        Log.e("TAG", "onDiscoveryStart: 11111111111");
    }

    @Override
    public void onDiscoveryFinished() {
        Log.e("TAG", "onDiscoveryFinished: 2222222222222");
    }

    /**
     * 获取设备信息以及信号强度
     *
     * @param bluetoothDevice
     * @param rssi
     */
    @Override
    public void onNewDeviceFound(BluetoothDevice bluetoothDevice, int rssi) {
        Log.e("TAG", "onNewDeviceFound: " + rssi);
        address = bluetoothDevice.getAddress();
    }
}
