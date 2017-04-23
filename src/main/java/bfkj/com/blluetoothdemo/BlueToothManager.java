package bfkj.com.blluetoothdemo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by zhq on 2017-04-23.
 */
public class BlueToothManager {
    public static final int REQUEST_OPEN_BLUETOOTH = 0;
    private static BlueToothManager blueToothManager;
    private Activity mActivity;

    private BluetoothAdapter mBlueToothAdatper;

    public static BlueToothManager getBlueToothManager() {
        if (null == blueToothManager) {
            blueToothManager = new BlueToothManager();
        }
        return blueToothManager;
    }

    public void onCreate() {
        if (null == mBlueToothAdatper) {
            mBlueToothAdatper = BluetoothAdapter.getDefaultAdapter();
        }
    }

    public boolean getBoothToothIsEnable() {
        return mBlueToothAdatper.isEnabled();
    }

    public void openBluetooth(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, REQUEST_OPEN_BLUETOOTH);
    }

    public void startDiscoveryDevice() {
        mBlueToothAdatper.cancelDiscovery();
        mBlueToothAdatper.startDiscovery();
    }

    public void connectDevice(Context context, String address) {
        mBlueToothAdatper.cancelDiscovery();
        BluetoothDevice device = mBlueToothAdatper.getRemoteDevice(address);
        device.connectGatt(context, true, mBluetoothGettCallBack);
    }

    private android.bluetooth.BluetoothGattCallback mBluetoothGettCallBack = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.e("TAG", "onConnectionStateChange: ffffffffffffff");
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.e("TAG", "onServicesDiscovered: ggggggggggggg");
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.e("TAG", "onCharacteristicRead: tttttttttttttt");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.e("TAG", "onCharacteristicWrite: wwwwwwwwwwwwwww");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.e("TAG", "onCharacteristicChanged: ssssssssssssssss");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.e("TAG", "onDescriptorRead: pppppppppppppppp");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.e("TAG", "onDescriptorWrite: oooooooooooooooo");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Log.e("TAG", "onReliableWriteCompleted: kkkkkkkkkkkkkkkkk");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.e("TAG", "onReadRemoteRssi: rrrrrrrrrrrrrrr");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.e("TAG", "onMtuChanged: sssssssssssssssssffffffffffffff");
        }
    };


    public void onResume(Activity activity) {
        activity.registerReceiver(mBluetoothSatateReciver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        activity.registerReceiver(mBluetoothDiscoverStartedReciver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        activity.registerReceiver(mBluetoothDiscoverFinishedReciver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        activity.registerReceiver(mBluetoothDeviceFoundReciver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        activity.registerReceiver(mBluetoothConnectedStateChangeReciver, new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED));
    }

    public void onPause(Activity activity) {
        activity.unregisterReceiver(mBluetoothSatateReciver);
        activity.unregisterReceiver(mBluetoothDiscoverStartedReciver);
        activity.unregisterReceiver(mBluetoothDiscoverFinishedReciver);
        activity.unregisterReceiver(mBluetoothDeviceFoundReciver);
        activity.unregisterReceiver(mBluetoothConnectedStateChangeReciver);
    }

    private BroadcastReceiver mBluetoothConnectedStateChangeReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == onConnectStateChangeListener)
                return;
            int connectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED);
            switch (connectState) {
                case BluetoothAdapter.STATE_CONNECTING:
                    onConnectStateChangeListener.onStateConnecting();
                    break;
                case BluetoothAdapter.STATE_CONNECTED:
                    onConnectStateChangeListener.onStateConnected();
                    break;
                case BluetoothAdapter.STATE_DISCONNECTED:
                    onConnectStateChangeListener.onStateUnconnected();
                    break;
            }
        }
    };


    /**
     * 扫描发现新设备,注意不一定是新的，可以重复
     */
    private BroadcastReceiver mBluetoothDeviceFoundReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == onNewDeviceFoundListener) {
                return;
            }
            onNewDeviceFoundListener.onNewDeviceFound((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    , Math.abs(intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI)));
        }
    };

    /**
     * 扫描结束
     */
    private BroadcastReceiver mBluetoothDiscoverFinishedReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == onDiscoveryStateChangedListener) {
                return;
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                onDiscoveryStateChangedListener.onDiscoveryFinished();
            }
        }
    };
    /**
     * 开始扫描
     */
    private BroadcastReceiver mBluetoothDiscoverStartedReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == onDiscoveryStateChangedListener) {
                return;
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())) {
                onDiscoveryStateChangedListener.onDiscoveryStart();
            }
        }
    };
    /**
     * 蓝牙开关状态
     */
    private BroadcastReceiver mBluetoothSatateReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == onBluetoothStateChangedListener) {
                return;
            }
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        onBluetoothStateChangedListener.onBluetoothStateOn();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        onBluetoothStateChangedListener.onBluetoothStateOff();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        onBluetoothStateChangedListener.onBluetoothStateTurnningOn();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        onBluetoothStateChangedListener.onBluetoothSateTurnningOff();
                        break;
                }
            }
        }
    };

    /**
     * 蓝牙状态改变的监听
     */
    public interface OnBluetoothStateChangedListener {
        void onBluetoothStateOn();

        void onBluetoothStateOff();

        void onBluetoothStateTurnningOn();

        void onBluetoothSateTurnningOff();
    }

    private OnBluetoothStateChangedListener onBluetoothStateChangedListener;

    public void setOnBluetoothStateChangedListener(OnBluetoothStateChangedListener onBluetoothStateChangedListener) {
        this.onBluetoothStateChangedListener = onBluetoothStateChangedListener;
    }

    /**
     * 扫描周围设备状态监听
     */
    public interface OnDiscoveryStateChangedListener {
        void onDiscoveryStart();

        void onDiscoveryFinished();
    }

    private OnDiscoveryStateChangedListener onDiscoveryStateChangedListener;

    public void setOnDiscoveryStateChangedListener(OnDiscoveryStateChangedListener onDiscoveryStateChangedListener) {
        this.onDiscoveryStateChangedListener = onDiscoveryStateChangedListener;
    }

    public interface OnNewDeviceFoundListener {
        void onNewDeviceFound(BluetoothDevice bluetoothDevice, int blueToothRssi);
    }

    private OnNewDeviceFoundListener onNewDeviceFoundListener;

    public void setOnNewDeviceFoundListener(OnNewDeviceFoundListener onNewDeviceFoundListener) {
        this.onNewDeviceFoundListener = onNewDeviceFoundListener;
    }


    public interface OnConnectStateChangeListener {
        void onStateConnecting();

        void onStateConnected();

        void onStateUnconnected();
    }

    private OnConnectStateChangeListener onConnectStateChangeListener;

    public void setOnConnectStateChangeListener(OnConnectStateChangeListener onConnectStateChangeListener) {
        this.onConnectStateChangeListener = onConnectStateChangeListener;
    }


}
