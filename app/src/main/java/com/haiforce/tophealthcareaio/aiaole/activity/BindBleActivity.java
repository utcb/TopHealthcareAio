package com.haiforce.tophealthcareaio.aiaole.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haiforce.tophealthcareaio.BaseActivity;
import com.haiforce.tophealthcareaio.R;
import com.haiforce.tophealthcareaio.aiaole.Receiver.BleReceiver;
import com.haiforce.tophealthcareaio.aiaole.modle.DeviceModle;
import com.haiforce.tophealthcareaio.aiaole.modle.EquipmentModle;
import com.haiforce.tophealthcareaio.aiaole.util.CommonUtil;
import com.xtremeprog.sdk.ble.IBle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 绑定蓝牙设备界面
 * 
 * @author sunshine
 * 
 */
public class BindBleActivity extends BaseActivity implements
		OnClickListener {

	public static final String ACTIVITY_FLA = "BindBleActivity";

	public static final String bindType = "bindType";

	int type;

	public static final int BP_TYPE = 1;// 血压
	public static final int BS_TYPE = 2;// 血糖
	public static final int EW_TYPE = 5;// 血糖

	List<EquipmentModle> list = new ArrayList<EquipmentModle>();

	TextView right_btn_text;

	StringBuffer bufferSB;

	LinearLayout scan_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 保持屏幕一直亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		setActivity_fla(ACTIVITY_FLA);
		setContentView(R.layout.activity_bindble);
		setBackOnclickListener();

		type = getIntent().getIntExtra(bindType, 2);

		initView();
		initListener();
		updateListView();
		// 注册设备绑定成功接收
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BleApplication.SCAN_STATE);
		intentFilter.addAction(BleReceiver.UPDATE_SCANVIEW);
		registerReceiver(scanStateReceiver, intentFilter);
		updateScan();
		// 进行扫描
		if (!BleApplication.isScaning) {
			CommonUtil.scan(this, true);
		}

	}

	public void updateListView() {
		list.clear();
		Iterator it = BleReceiver.dMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			DeviceModle modle = (DeviceModle) entry.getValue();
			byte[] message = modle.getMessageByte();
			EquipmentModle eqModle = new EquipmentModle();

			if (type == BP_TYPE
					&& "Bioland-BPM".equals(modle.getdName().trim())) {

				eqModle.setDeviceSerial(key);
				eqModle.setAndroid_mac(key);
				list.add(eqModle);
			} else if (type == BS_TYPE
					&& "Bioland-BGM".equals(modle.getdName().trim())) {
				eqModle.setDeviceSerial(key);
				eqModle.setAndroid_mac(key);
				list.add(eqModle);
			}

			if (null != message && message[5] == type) {
				String deviceSerial = CommonUtil.getDeviceSerial(message);
				eqModle.setDeviceSerial(deviceSerial);
				eqModle.setAndroid_mac(key);
			}

		}
		// 如果有数据进行更新
		if (list.size() > 0) {
			hideLoadingView();
		}
		adapter.notifyDataSetChanged();
	}

	/*
	 * 设备绑定成功接收
	 */
	private BroadcastReceiver scanStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (BleReceiver.SCAN_STATE.equals(action)) {
				updateScan();
			} else if (BleReceiver.UPDATE_SCANVIEW.equals(action)) {
				updateListView();
			}
		}
	};

	private void updateScan() {

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

	}

	IBle mBle;
	String mDeviceAddress;

	/*
	 * 操作蓝牙设备
	 */

	@Override
	protected void onResume() {
		super.onResume();

		// Ensures Bluetooth is enabled on the device. If Bluetooth is not
		// currently enabled,
		// fire an intent to display a dialog asking the user to grant
		// permission to enable it.

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		unregisterReceiver(scanStateReceiver);
	}

	private void initListener() {

		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// Intent manageDetailIntent = new Intent(BindBleActivity.this,
		// EquipManageDetailActivity.class);
		// EquipmentModle modle = adapter.getItem(arg2);
		// bindBle(modle.getDeviceSerial());
		// startActivity(manageDetailIntent);
		//
		// }
		//
		// });
		scan_btn.setOnClickListener(this);

	}

	ListView listView;
	EquipManageAdapter adapter;

	/*
 * 设置头部标题
 */
	protected void setHeadTitle(String title) {
		TextView headTitle = (TextView) findViewById(R.id.head_title);
		headTitle.setText(title);
		headTitle.setVisibility(View.VISIBLE);

		ImageView headIcon = (ImageView) findViewById(R.id.head_icon);
		if (null != headIcon) {
			headIcon.setVisibility(View.GONE);
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		right_btn_text = (TextView) findViewById(R.id.right_btn_text);
		scan_btn = (LinearLayout) findViewById(R.id.scan_btn);
		listView = (ListView) findViewById(R.id.listView);
		adapter = new EquipManageAdapter(list);
		listView.setAdapter(adapter);

		if (type == BP_TYPE) {
			setHeadTitle(getResources().getString(
					R.string.addequipmanage_boolpresure_bl));
			right_btn_text.setText(getResources().getString(
					R.string.addequipmanage_search_bpt));

			// showLoadingView();
		} else if (type == EW_TYPE) {
			setHeadTitle(getResources().getString(R.string.addequipmanage_bet));
			right_btn_text.setText(getResources().getString(
					R.string.addequipmanage_search_bet));

			// showLoadingView();
		} else {
			setHeadTitle(getResources().getString(
					R.string.addequipmanage_heartrate_bl));
			right_btn_text.setText(getResources().getString(
					R.string.addequipmanage_search_bbg));
			// showLoadingView();
		}

	}

	class EquipManageAdapter extends BaseAdapter {

		List<EquipmentModle> eqModleList;

		public EquipManageAdapter(List<EquipmentModle> eqModleList) {
			this.eqModleList = eqModleList;
		}

		@Override
		public int getCount() {
			if (null == eqModleList) {
				return 0;
			}
			return eqModleList.size();
		}

		@Override
		public EquipmentModle getItem(int arg0) {
			// TODO Auto-generated method stub
			return eqModleList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}



		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			if (view == null) {
				view = mInflater.inflate(R.layout.item_bind, null);
			}
			final EquipmentModle modle = getItem(arg0);
			TextView name_text = (TextView) view.findViewById(R.id.name_text);
			// 左边图标
			int iconType = modle.getIcon_type();
			ImageView leftIcon = (ImageView) view.findViewById(R.id.icon);
			TextView bind_state = (TextView) view.findViewById(R.id.bind_state);
			switch (type) {
			case BP_TYPE:
				leftIcon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.eq_boolpressure_blue));
				name_text.setText(getResources().getString(
						R.string.homepage_01bpm));

				break;
			case BS_TYPE:
				leftIcon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.eq_hearrate_icon));
				name_text.setText(getResources().getString(
						R.string.homepage_02bgm));

				break;

			case EW_TYPE:
				leftIcon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.eq_boolpressure));
				name_text.setText(getResources().getString(
						R.string.homepage_erwenji));

				break;
			}

			TextView deviceSerial_text = (TextView) view
					.findViewById(R.id.deviceSerial_text);
			final String deviceSerial = modle.getDeviceSerial().trim();

			// if (deviceSerial.length() > 6) {
			// deviceSerial = deviceSerial.substring(
			// deviceSerial.length() - 6, deviceSerial.length());
			// }
			deviceSerial_text.setText(deviceSerial);
			LinearLayout bind_btn = (LinearLayout) view
					.findViewById(R.id.bind_btn);

			final String android_mac = modle.getAndroid_mac();
			if (null != android_mac) {
				if (android_mac.equals(deviceSerial)) {
					bind_state.setText(getResources().getString(
							R.string.ble_connect));
				} else {
					bind_state.setText("");
					bind_state.setVisibility(View.GONE);
				}
			}

			// 绑定按钮
			bind_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					if (!android_mac.equals(deviceSerial)) {
						bindBle(modle.getDeviceSerial(), modle.getAndroid_mac());
					} else {
						showToast(getResources()
								.getString(R.string.ble_connect));
					}

				}
			});

			return view;
		}
	}

	private void bindBle(String deviceSerial, final String android_mac) {
        // 不需要调用NetRestClient.post远程绑定，总是返回成功
        // 绑定成功
        disMissLoadDiaLog();
        bindSuccess();
        DeviceModle deviceModle = BleReceiver.dMap.get(android_mac);
        // 更新状态
        if (null != deviceModle) {
            deviceModle.setFla(DeviceModle.LINK_STATE);
        }
	}

	private void bindSuccess() {
		Intent intentBindSuccess = new Intent(BindBleActivity.this, BindSuccessActivity.class);
		startActivity(intentBindSuccess);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 跳转添加设备界面
		case R.id.right_btn: {
			Intent addEquipIntent = new Intent(BindBleActivity.this,
					AddEquipActivity.class);
			startActivity(addEquipIntent);
		}
			break;
		case R.id.scan_btn:

			if (!CommonUtil.checeBule(this)) {
				return;
			}

			if (BleApplication.isScaning) {
				CommonUtil.scan(BindBleActivity.this, false);
			} else {
				CommonUtil.scan(BindBleActivity.this, true);
			}
			break;

		}

	}

}
