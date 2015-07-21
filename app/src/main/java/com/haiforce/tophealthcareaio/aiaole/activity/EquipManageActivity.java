package com.haiforce.tophealthcareaio.aiaole.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haiforce.tophealthcareaio.BaseActivity;
import com.haiforce.tophealthcareaio.R;
import com.haiforce.tophealthcareaio.aiaole.modle.EquipmentModle;

import java.util.List;

/**
 * 设备管理
 * 
 * @author sunshine
 * 
 */
public class EquipManageActivity extends BaseActivity implements
		OnClickListener {

	public static final String ACTIVITY_FLA = "EquipManageActivity";

	public static final String SEND_MODLE = "sendModle";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipmanage);
		setBackOnclickListener();

		initView();
		initListener();
		initData();
		setActivity_fla(ACTIVITY_FLA);

		// 注册设备绑定成功接收
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BindSuccessActivity.UPDATE_EQ_DATA);
		registerReceiver(bindSuccessReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		// 解除绑定
		unregisterReceiver(bindSuccessReceiver);
		super.onDestroy();
	}

	private void initListener() {
		right_btn.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent manageDetailIntent = new Intent(
						EquipManageActivity.this,
						EquipManageDetailActivity.class);
				EquipmentModle modle = adapter.getItem(arg2);
				manageDetailIntent.putExtra(SEND_MODLE, modle);
				startActivity(manageDetailIntent);

			}

		});

	}

	/*
	 * 设备绑定成功接收
	 */
	private BroadcastReceiver bindSuccessReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			initData();
		}
	};

	ListView listView;
	EquipManageAdapter adapter;

	LinearLayout right_btn;

	private void initView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.listView);
		right_btn = (LinearLayout) findViewById(R.id.right_btn);

	}

	private String swtichData(String oriData) {
		oriData = oriData.replace("-", "/");
		oriData = oriData.substring(0, oriData.length() - 3);
		return oriData;
	}

	private void initData() {

		// EquipmentModle e1 = new EquipmentModle();
		// e1.setIcon_type(EquipmentModle.BLOOD_PRESSURE);
		// e1.setName("血压计");
		// e1.setState("正在同步...");
		// e1.setShowMode(EquipmentModle.MODE_EQUIPMENT);
		//
		// EquipmentModle e2 = new EquipmentModle();
		// e2.setName("血压计");
		// e2.setState("正在同步...");
		// e2.setIcon_type(EquipmentModle.BLOOD_PRESSURE_BLUETOOTH);
		// e2.setShowMode(EquipmentModle.MODE_EQUIPMENT);
		//
		// EquipmentModle e3 = new EquipmentModle();
		// e3.setName("温度计");
		// e3.setState("正在同步...");
		// e3.setIcon_type(EquipmentModle.HEART_RATE);
		// e3.setShowMode(EquipmentModle.MODE_EQUIPMENT);
		//
		// EquipmentModle e4 = new EquipmentModle();
		// e4.setName("血糖计");
		// e4.setState("正在同步...");
		// e4.setIcon_type(EquipmentModle.BLOOD_PRESSURE_BLUETOOTH);
		// e4.setShowMode(EquipmentModle.MODE_EQUIPMENT);
		//
		// EquipmentModle e5 = new EquipmentModle();
		// e5.setName("血糖计");
		// e5.setState("正在同步...");
		// e5.setIcon_type(EquipmentModle.BLOOD_PRESSURE_BLUETOOTH);
		// e5.setShowMode(EquipmentModle.MODE_EQUIPMENT);
		//
		// EquipmentModle e6 = new EquipmentModle();
		// e6.setName("血糖计");
		// e6.setState("正在同步...");
		// e6.setIcon_type(EquipmentModle.BLOOD_PRESSURE_BLUETOOTH);
		// e6.setShowMode(EquipmentModle.MODE_EQUIPMENT);
		//
		// EquipmentModle e7 = new EquipmentModle();
		// e7.setName("血糖计");
		// e7.setState("正在同步...");
		// e7.setIcon_type(EquipmentModle.BLOOD_PRESSURE_BLUETOOTH);
		// e7.setShowMode(EquipmentModle.MODE_EQUIPMENT);
		//
		// eqModleList.add(e1);
		// eqModleList.add(e2);
		// eqModleList.add(e3);
		/*
		 * eqModleList.add(e4); eqModleList.add(e5); eqModleList.add(e6);
		 * eqModleList.add(e7);
		 */

		showLoadingView();
		// 禁止NetRestClient.post远程同步
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
				view = mInflater.inflate(R.layout.item_equipmanage, null);
			}
			EquipmentModle modle = getItem(arg0);
			// 左边图标
			int iconType = modle.getIcon_type();
			ImageView leftIcon = (ImageView) view.findViewById(R.id.icon);
			switch (iconType) {
			case EquipmentModle.BLOOD_PRESSURE_BLUETOOTH:
				leftIcon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.eq_boolpressure_blue));
				break;
			case EquipmentModle.BLOOD_PRESSURE:
				leftIcon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.eq_boolpressure));
				break;
			case EquipmentModle.HEART_RATE:
				leftIcon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.eq_boolpressure));
				break;
			case EquipmentModle.HEART_RATE_BLUETOOTH:
				leftIcon.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.eq_hearrate_icon));
				break;
			}

			TextView equip_text = (TextView) view.findViewById(R.id.equip_text);
			TextView username_text = (TextView) view
					.findViewById(R.id.username_text);
			TextView username_role = (TextView) view
					.findViewById(R.id.username_role);
			TextView last_updatetime = (TextView) view
					.findViewById(R.id.last_updatetime);
			String isAdmin = modle.getIsAdmin();

			equip_text.setText(modle.getName());
			String deviceSerial = modle.getDeviceSerial();
			if (deviceSerial.length() > 6) {
				deviceSerial = deviceSerial.substring(
						deviceSerial.length() - 6, deviceSerial.length());
			}
			username_text.setText(deviceSerial);
			last_updatetime.setText(modle.getTime());
			if ("1".equals(isAdmin)) {
				username_role.setText(getResources().getString(
						R.string.actt_manager));
			} else {
				username_role.setText(getResources().getString(
						R.string.actt_user));
			}

			return view;
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 跳转添加设备界面
		case R.id.right_btn: {
			Intent addEquipIntent = new Intent(EquipManageActivity.this,
					AddEquipActivity.class);
			startActivity(addEquipIntent);
		}
			break;

		}

	}

}
