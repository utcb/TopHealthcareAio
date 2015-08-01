package com.haiforce.tophealthcareaio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiforce.tophealthcareaio.aiaole.Receiver.BleReceiver;
import com.haiforce.tophealthcareaio.aiaole.activity.BindSuccessActivity;
import com.haiforce.tophealthcareaio.aiaole.activity.BleApplication;
import com.haiforce.tophealthcareaio.aiaole.modle.DeviceModle;
import com.haiforce.tophealthcareaio.aiaole.modle.Gloal;
import com.haiforce.tophealthcareaio.aiaole.modle.NumericalModle;
import com.haiforce.tophealthcareaio.aiaole.util.CommonUtil;
import com.haiforce.tophealthcareaio.aiaole.util.NetRestClient;
import com.haiforce.tophealthcareaio.cvr100b.Cvr100bActivity;
import com.xtremeprog.sdk.ble.IBle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity  implements View.OnClickListener {
    public static final String DEBUG = Gloal.DEBUG + "MainActivity";

    // 打开蓝牙服务
    private static final int REQUEST_ENABLE_BT = 1;

    IBle mBle; // 蓝牙接口

    private Timer timer = null;
    private TimerTask timeTask = null;
    private boolean isExit = false; // 标记是否要退出

    public static final String CHANGE_DATA = "changeData";

    private static TextView bleStatus = null;
    public static void setBleStatusText(String status) {
        if (bleStatus != null) {
            bleStatus.setText(status);
        } else {
            Log.d(DEBUG, "bleStatus is null.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG, "(BleApplication) getApplication() == " + getApplication().toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        // 检测更新
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,
                                         UpdateResponse updateInfo) {
                Gloal.updateStatus = updateStatus;
                Gloal.updateInfo = updateInfo;
            }
        });
        UmengUpdateAgent.update(this);
        */

        /*
        // 启动网络监听
        ConnectionChangeReceiver mConnectivityReceiver = ConnectionChangeReceiver
                .getInstence();
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnectivityReceiver, filter);
        */

        // initUnload();

        // 保持屏幕一直亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        timer = new Timer();
        initView();
        initListener();

        // 注册设备绑定成功接收
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BindSuccessActivity.UPDATE_EQ_DATA);
        intentFilter.addAction(BleApplication.SCAN_STATE);
        intentFilter.addAction(CHANGE_DATA);
        intentFilter.addAction(BleReceiver.ENTER_MEASURE);
        intentFilter.addAction(Gloal.update_measure_result);
        intentFilter.addAction(Gloal.update_Serial);
        registerReceiver(mReceiver, intentFilter);

        // CommonUtil.checeBule(this);

        bleStatus = (TextView) findViewById(R.id.bleStatus);
        setBleStatusText("Waiting for ble service ready...");
    }

    /*
 * 设备绑定成功接收
 */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BleApplication.SCAN_STATE.equals(action)) {
                Log.d(DEBUG, "Receive SCAN_STATE, try to updateScan");
                updateScan();
            } else if (BindSuccessActivity.UPDATE_EQ_DATA.equals(action)) {
                Log.d(DEBUG, "Receive UPDATE_EQ_DATA, try to initData");
                // initData();
            } else if (CHANGE_DATA.equals(action)) {
                Log.d(DEBUG, "Receive CHANGE_DATA...");
                NumericalModle numModle = (NumericalModle) intent.getSerializableExtra(BleReceiver.UNLOAD);
                Log.d(DEBUG, "Receive CHANGE_DATA: " + (numModle != null ? numModle.toString() : "null"));
                                    /*
                if (null != numModle) {
                    adapter.updateShowState();
                    List<EquipmentModle> list = adapter.getEqList();
                    if (null == list || list.size() == 0) {
                        return;
                    }
                    for (EquipmentModle emModle : list) {
                        String deviceSerial = emModle.getDeviceSerial();
                        if (null != deviceSerial
                                && deviceSerial.equals(numModle
                                .getDeviceSerial())) {
                            List<NumericalModle> numList = emModle
                                    .getNumericalList();
                            if (null == numList) {
                                numList = new ArrayList<NumericalModle>();
                                emModle.setNumericalList(numList);
                            }
                            emModle.getNumericalList().add(0, numModle);
                        }
                    }
                }*/
                // adapter.notifyDataSetChanged();
            } else if (BleReceiver.ENTER_MEASURE.equals(action)) {
                TextView deviceSerialTxt = (TextView) findViewById(R.id.deviceSerialTxt);
                deviceSerialTxt.setText("no serial");
                TextView result2Txt = (TextView) findViewById(R.id.result2Txt);
                result2Txt.setText("");
                TextView result3Txt = (TextView) findViewById(R.id.result3Txt);
                result3Txt.setText("");
                TextView result4Txt = (TextView) findViewById(R.id.result4Txt);
                result4Txt.setText("");
                // 进入测量页面
                    /*
                String mDeviceAddress = intent
                        .getStringExtra(Gloal.send_address);
                Intent measure_intent = new Intent(getActivity(),
                        MeasuredActivity.class);
                measure_intent.putExtra(Gloal.send_address, mDeviceAddress);
                startActivity(measure_intent);
                */
            } else if (Gloal.update_measure_result.equals(action)) {
                Log.d(DEBUG, "Receive update_measure_result, try to display new result");
                byte[] messageByte = intent.getByteArrayExtra(Gloal.send_messageByte);
                byte[] resuleByte = intent.getByteArrayExtra(Gloal.send_resuleByte);
                String result2, result3, result4;
                result2 = result3 = result4 = "";
                result2 = CommonUtil.getShort(resuleByte, 8) + ""; // [8,9]: 收缩压，低位在前
                result3 = resuleByte[10] + ""; // [10]: 舒张压
                result4 = resuleByte[11] + ""; // [11]: 心率
                TextView result2Txt = (TextView) findViewById(R.id.result2Txt);
                result2Txt.setText(result2);
                TextView result3Txt = (TextView) findViewById(R.id.result3Txt);
                result3Txt.setText(result3);
                TextView result4Txt = (TextView) findViewById(R.id.result4Txt);
                result4Txt.setText(result4);
            } else if (Gloal.update_Serial.equals(action)) {
                Log.d(DEBUG, "Receive update_Serial, try to display device serial");
                String deviceSerail = intent.getStringExtra("deviceSerial");
                TextView deviceSerialTxt = (TextView) findViewById(R.id.deviceSerialTxt);
                deviceSerialTxt.setText(deviceSerail);
            }

        }
    };

    private void updateScan() {
        EditText scanStatus = (EditText) findViewById(R.id.scanStatus);
        if (BleApplication.isScaning) {
            scanStatus.setText("scanning......");
            // scan_text.setText(getResources().getString(R.string.scan_stop));
            // showLoadingView();
            // scan_pro.setVisibility(View.VISIBLE);
        } else {
            scanStatus.setText("Not scanning......");
            // scan_text.setText(getResources().getString(R.string.scan_start));
            // scan_pro.setVisibility(View.GONE);
            // hideLoadingView();
        }
        /*
        ProgressBar scan_pro = (ProgressBar) findViewById(R.id.scan_pro);
        TextView scan_text = (TextView) findViewById(R.id.scan_text);
        if (BleApplication.isScaning) {
            scan_text.setText(getResources().getString(R.string.scan_stop));
            // showLoadingView();
            scan_pro.setVisibility(View.VISIBLE);
        } else {
            scan_text.setText(getResources().getString(R.string.scan_start));
            scan_pro.setVisibility(View.GONE);
            // hideLoadingView();
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击"scan BP"按钮
     * @param view
     */
    public void scanBp(View view) {
        if (!CommonUtil.checeBule(this)) {
            return;
        }

        if (BleApplication.isScaning) {
            CommonUtil.scan(this, false);
        } else {
            CommonUtil.scan(this, true);
        }
    }

    /**
     * 点击"idreadBtn"
     * @param view
     */
    public void readID(View view) {
        Intent intent = new Intent(MainActivity.this, Cvr100bActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        return;
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();
            // 获取PID
        } else {
            isExit = true;
            Toast.makeText(MainActivity.this,
                    getResources().getString(R.string.press_again_exit),
                    Toast.LENGTH_SHORT).show();
            timeTask = new TimerTask() {

                @Override
                public void run() {
                    isExit = false;
                }
            };
            timer.schedule(timeTask, 2000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                CommonUtil.scan(this, true);
            } else if (resultCode == RESULT_CANCELED) {
                CommonUtil.scan(this, false);
            }
        }
    }

   @Override
    protected void onDestroy() {
       // 取消蓝牙监听
        Intent colseBle = new Intent(BleApplication.CONTROL_ACTION);
        colseBle.putExtra(BleApplication.CONTROL_FLA,
                BleApplication.CONTROL_COLSE);
        sendBroadcast(colseBle);
       /*
        // 取消监听网络状态
        ConnectionChangeReceiver mConnectivityReceiver = ConnectionChangeReceiver
                .getInstence();
        unregisterReceiver(mConnectivityReceiver);
        */
        BleApplication app = (BleApplication) getApplication();
        mBle = app.getIBle();
        if (mBle != null) {
            for (Map.Entry<String, DeviceModle> entry : BleReceiver.dMap
                    .entrySet()) {
                DeviceModle deviceModle = entry.getValue();
                String adress = deviceModle.getDevice().getAddress();
                if (null != adress) {
                    mBle.disconnect(adress);
                }

            }
            BleReceiver.dMap.clear();
        }

        // BleReceiver.dMap.clear();

        unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    private void initUnload() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String jsonStr = preferences.getString(NetRestClient.locUnload,
                        "[]");
                try {
                    JSONArray array = new JSONArray(jsonStr);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject item = array.getJSONObject(i);
                        saveUnUpload(item.getString("result1"),
                                item.getString("result2"),
                                item.getString("result3"),
                                item.getString("result4"),
                                item.getString("meaSureType"),
                                item.getString("deviceSerial"),
                                item.getString("date"));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateHomePageData();
                    }
                });
            }
        }).start();
    }

    private void saveUnUpload(String result1, String result2, String result3,
                              String result4, String meaSureType, String deviceSerial,
                              String dateStr) {
        // 保存未上传数值
        NumericalModle unUploadModle = new NumericalModle();
        unUploadModle.setResult1(result1);
        unUploadModle.setResult2(result2);
        unUploadModle.setResult3(result3);
        unUploadModle.setResult4(result4);
        unUploadModle.setMeasureType(meaSureType);
        unUploadModle.setDate(dateStr);
        unUploadModle.setDeviceSerial(deviceSerial);
        unUploadModle.setMeasure_id("unknow");
        BleReceiver.unUpLoadList.add(unUploadModle);
        unUploadModle.setShowFla(false);
    }

    private void initView() {
        // setHeadIcon();

        /*
        // 主页 亲友 发现 设置 按钮
        homepage_button = (LinearLayout) findViewById(R.id.homepage_button);
        friends_button = (LinearLayout) findViewById(R.id.friends_button);
        find_button = (LinearLayout) findViewById(R.id.find_button);
        set_button = (LinearLayout) findViewById(R.id.set_button);
        */
        initButtonLayout();
        /*
        // 内容显示
        content_view = (RelativeLayout) findViewById(R.id.content_view);
        homePage();
        */
    }

    // 调整底部按钮大小
    private void initButtonLayout() {
        // 获取屏幕密度（方法3）
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int btnWith = dm.widthPixels / 4;
        int btnHeight = btnWith * 143 / 180;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                btnWith, btnHeight);

        /*
        homepage_button.setLayoutParams(params);
        friends_button.setLayoutParams(params);
        find_button.setLayoutParams(params);
        set_button.setLayoutParams(params);
        */
    }

    private void initListener() {
        // TODO Auto-generated method stub
        // setBtnThouch(homepage_button);
        // setBtnThouch(friends_button);
        // setBtnThouch(find_button);
        // setBtnThouch(set_button);

        /*
        homepage_button.setOnClickListener(this);
        friends_button.setOnClickListener(this);
        find_button.setOnClickListener(this);
        set_button.setOnClickListener(this);
        */
    }
}
