package com.haiforce.tophealthcareaio;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haiforce.tophealthcareaio.aiaole.activity.BindSuccessActivity;

public class BaseActivity extends ActionBarActivity implements Callback {

	protected String activity_fla = "BaseActivity";

	public String getActivity_fla() {
		return activity_fla;
	}

	public void setActivity_fla(String activity_fla) {
		this.activity_fla = activity_fla;
	}

	public SharedPreferences preferences;
	public static final String SHARE_NAME = "aiaole";

	public static final int LOADING = 1;
	public static final int DIALOG1_KEY = 0;
	public static final int SCAN_KEY = 2;

	public static final String ACTIVITY_FLA = "activityFla";
	public static final String ACTIVITY_FINISH = "finishActivity";

	// 提示信息
	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public LayoutInflater mInflater;
	public Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mHandler = new Handler();
		preferences = getSharedPreferences(SHARE_NAME, 0);

		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTIVITY_FINISH);
		registerReceiver(finishReceiver, intentFilter);

		// View dia_view=m

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(finishReceiver);
		super.onDestroy();
	}

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

	/*
	 * 让图片显示
	 */
	protected void setHeadIcon() {
		ImageView headIcon = (ImageView) findViewById(R.id.head_icon);
		headIcon.setVisibility(View.VISIBLE);

		TextView headTitle = (TextView) findViewById(R.id.head_title);
		headTitle.setVisibility(View.GONE);

	}

	// 更新主页数据
	protected void updateHomePageData() {
		Intent bindSuccessBroad = new Intent(BindSuccessActivity.UPDATE_EQ_DATA);
		sendBroadcast(bindSuccessBroad);

	}

	protected void setBackOnclickListener() {
		ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
		backBtn.setVisibility(View.VISIBLE);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
	}

	@Override
	public void finish() {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		if (null != imm) {

			imm.hideSoftInputFromWindow(this.getWindow().getDecorView()
					.getWindowToken(), 0);
		}
		super.finish();
	}

	// 设置右边按钮
	protected void setRightBtn(String text) {
		LinearLayout right_btn = (LinearLayout) findViewById(R.id.right_btn);
		final TextView right_btn_text = (TextView) findViewById(R.id.right_btn_text);
		right_btn_text.setText(text);
		right_btn.setVisibility(View.VISIBLE);
		right_btn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					right_btn_text.setTextColor(getResources().getColor(
							R.color.white));
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					right_btn_text.setTextColor(getResources().getColor(
							R.color.off_white));
				}
				return false;
			}
		});

	}

	protected boolean isValueNull(String value) {
		if (null == value || "".equals(value) || "null".equals(value)) {
			return true;
		}
		return false;
	}

	String proString;

	public void showLoadDiaLog(String content) {
		proString = content;
		showDialog(LOADING);
	}

	public void disMissLoadDiaLog() {
		if (null != dialog && dialog.isShowing()) {
			dismissDialog(LOADING);
		}
	}

	public void disScanDiaLog() {
		if (null != dialog && dialog.isShowing()) {
			dismissDialog(SCAN_KEY);
		}

	}

	ProgressDialog dialog;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			dialog = new ProgressDialog(this);
			dialog.setTitle("Indeterminate");
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		case LOADING: {
			dialog = new ProgressDialog(this);
			dialog.setMessage(proString);
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);

			return dialog;
		}

		}
		return null;
	}

	private BroadcastReceiver finishReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String array[] = intent.getStringArrayExtra(ACTIVITY_FLA);
			if (null != array && array.length > 0) {
				for (int i = 0; i < array.length; i++) {
					if (activity_fla.equals(array[i])) {
						// 结束当前activity;
						finish();
					}
				}
			}

		}
	};

	/**
	 * 结束activity
	 * 
	 * @param activity
	 *            要结束activity的Fla
	 */
	protected void finishActivity(String[] activity) {
		Intent intent = new Intent(ACTIVITY_FINISH);
		intent.putExtra(ACTIVITY_FLA, activity);
		sendBroadcast(intent);
	}

	protected void showLoadingView() {
		View loadView = findViewById(R.id.loading);
		loadView.setVisibility(View.VISIBLE);
	}

	protected void hideLoadingView() {
		View loadView = findViewById(R.id.loading);
		loadView.setVisibility(View.GONE);
	}

	private String notifyTitle;
	private int notifyIcon;

	private static final int MSG_CANCEL_NOTIFY = 3;

	/** 分享时Notification的图标和文字 */
	public void setNotification(int icon, String title) {
		notifyIcon = icon;
		notifyTitle = title;
	}

	// 在状态栏提示分享操作
	public void showNotification(long cancelTime, String text) {
		try {
			Context app = getApplicationContext();
			NotificationManager nm = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
			final int id = Integer.MAX_VALUE / 13 + 1;
			nm.cancel(id);

			long when = System.currentTimeMillis();
			Notification notification = new Notification(notifyIcon, text, when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(),
					0);
			notification.setLatestEventInfo(app, notifyTitle, text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(id, notification);

			if (cancelTime > 0) {
				Message msg = new Message();
				msg.what = MSG_CANCEL_NOTIFY;
				msg.obj = nm;
				msg.arg1 = id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {

		switch (msg.what) {
		case MSG_CANCEL_NOTIFY: {
			NotificationManager nm = (NotificationManager) msg.obj;
			if (nm != null) {
				nm.cancel(msg.arg1);
			}
		}
			break;
		}
		return false;
	}
}
