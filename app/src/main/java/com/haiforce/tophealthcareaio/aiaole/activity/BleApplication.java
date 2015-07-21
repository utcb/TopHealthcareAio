package com.haiforce.tophealthcareaio.aiaole.activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.haiforce.tophealthcareaio.MainActivity;
import com.haiforce.tophealthcareaio.aiaole.Receiver.BleReceiver;
import com.haiforce.tophealthcareaio.aiaole.modle.Gloal;
import com.xtremeprog.sdk.ble.BleService;
import com.xtremeprog.sdk.ble.BleService.BLESDK;
import com.xtremeprog.sdk.ble.IBle;

import java.util.ArrayList;

public class BleApplication extends Application {
    public static final String DEBUG = Gloal.DEBUG + "BleApplication";

    private BleService mService;
    private IBle mBle;
    public Handler mHandler;

    // Stops scanning after 5 seconds.
    private static final long STOP_PERIOD = 5 * 1000;

    public static final String SCAN_ACTION = "BleApplication.scanAction";
    public static final String CONTROL_ACTION = "BleApplication.controlAction";
    public static final String SCAN_FLA = "scanFla";
    public static final String CONTROL_FLA = "controlFla";

    public static final int SCAN_OPEN = 101;
    public static final int SCAN_COLSE = 102;

    public static final int CONTROL_OPEN = 201;
    public static final int CONTROL_COLSE = 202;

    public static final String SCAN_STATE = "BleApplication.scanState";

    public static final String UPLOAD_TESTDATA = "BleApplication.updateTestData";

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            Log.d(DEBUG, "onServiceConnected...");
            MainActivity.setBleStatusText("Ble service connected.");
            if (rawBinder instanceof BleService.LocalBinder) {
                mService = ((BleService.LocalBinder) rawBinder).getService();
                mBle = mService.getBle();
                if (mBle == null || !mBle.adapterEnabled()) {
                    Log.d(DEBUG, "mBle is null  or no adapater enabled");
                }

                mBleReceiver = new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        String action = arg1.getAction();

                        if (SCAN_ACTION.equals(action)) {
                            int scanFla = arg1.getIntExtra(SCAN_FLA, SCAN_COLSE);
                            if (scanFla == SCAN_OPEN) {
                                scanDevice(true);
                            } else {
                                scanDevice(false);
                            }
                        } else if (CONTROL_ACTION.equals(action)) {
                            Log.d(DEBUG, "receive control action...");
                            int controlFla = arg1.getIntExtra(CONTROL_FLA, CONTROL_COLSE);
                            BleReceiver bleReceiver = BleReceiver.getInstence();

                            if (controlFla == CONTROL_COLSE) {
                                try {
                                    unregisterReceiver(bleReceiver);
                                } catch (Exception e) {
                                    Log.d(DEBUG, e.getMessage());
                                }
                                Log.d(DEBUG, "关闭接收器");
                            } else {
                                registerReceiver(bleReceiver, BleService.getIntentFilter());
                                bleReceiver = null;
                                Log.d(DEBUG, "开启接收器");
                                MainActivity.setBleStatusText("Ble接收器开启成功");
                            }

                        } else if ("UPLOAD_TESTDATA".equals(action)) {
                            // saveUnLoad();
                        }
                    }
                };

                IntentFilter filter = new IntentFilter();
                filter.addAction(SCAN_ACTION);
                filter.addAction(CONTROL_ACTION);
                filter.addAction(UPLOAD_TESTDATA);
                registerReceiver(mBleReceiver, filter);

                // 启用蓝牙接收（BleReceiver）：send controlAction after service registration
                Intent openBle = new Intent(BleApplication.CONTROL_ACTION);
                openBle.putExtra(BleApplication.CONTROL_FLA, BleApplication.CONTROL_OPEN);
                sendBroadcast(openBle);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName classname) {
            mBle = null;
            mService = null;
            Log.d(DEBUG, "关闭服务");
            if (null != mBleReceiver) {
                BleReceiver bleReceiver = BleReceiver.getInstence();
                try {
                    unregisterReceiver(bleReceiver);
                } catch (Exception e) {
                    Log.d(DEBUG, e.getMessage());
                }
                Log.d(DEBUG, "关闭接收器");
                unregisterReceiver(mBleReceiver);
            }
        }
    };

    @Override
    public void onCreate() {
        Log.d(DEBUG, "BleApplication onCreate.");
        super.onCreate();
        mHandler = new Handler();
        bindService();
        // 判断网络情况
        // CommonUtil.isNetWork(this);
    }

    public void bindService() {
        Log.d(DEBUG, "try to bind mServiceConnection");
        if (getBleSDK() == BLESDK.ANDROID) {
            Intent bindIntent = new Intent(this, BleService.class);
            bindService(bindIntent, mServiceConnection,
                    Context.BIND_AUTO_CREATE);
        }
    }

    public void unbindService() {
        if (getBleSDK() == BLESDK.ANDROID) {
            Intent bindIntent = new Intent(this, BleService.class);
            stopService(bindIntent);
        }
    }

    public IBle getIBle() {
        return mBle;
    }

    private BLESDK getBleSDK() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            // android 4.3
            return BLESDK.ANDROID;
        }

        ArrayList<String> libraries = new ArrayList<String>();
        for (String i : getPackageManager().getSystemSharedLibraryNames()) {
            libraries.add(i);
        }

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            // android 4.2.2
            if (libraries.contains("com.samsung.android.sdk.bt")) {
                return BLESDK.SAMSUNG;
            } else if (libraries.contains("com.broadcom.bt")) {
                return BLESDK.BROADCOM;
            }
        }

        return BLESDK.NOT_SUPPORTED;
    }

    public static boolean isScaning = false;

    Runnable stop = new Runnable() {
        @Override
        public void run() {
            if (mBle != null) {
                stopScan();
            }
            // invalidateOptionsMenu();
        }
    };

    private void scanDevice(boolean able) {
        // Toast.makeText(BleApplication.this, mService.mBleSDK.toString(),
        // Toast.LENGTH_LONG).show();

        mBle = getIBle();
        if (mBle == null) {
            return;
        }
        if (able) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(stop, STOP_PERIOD);
            if (mBle != null) {
                if (!isScaning) {

                    mBle.startScan();
                    isScaning = true;
                    Intent scanState = new Intent(SCAN_STATE);
                    sendBroadcast(scanState);
                }
            }
        } else {
            stopScan();
        }

    }

    private void stopScan() {
        if (mBle != null) {
            mHandler.removeCallbacks(stop);
            mBle.stopScan();
            isScaning = false;
            Intent scanState = new Intent(SCAN_STATE);
            sendBroadcast(scanState);
        }
    }

    // 更新主页数据
    protected void updateHomePageData() {
        Intent bindSuccessBroad = new Intent(BindSuccessActivity.UPDATE_EQ_DATA);
        sendBroadcast(bindSuccessBroad);

    }

    BroadcastReceiver mBleReceiver;
    BroadcastReceiver controbleReceiver;

}
