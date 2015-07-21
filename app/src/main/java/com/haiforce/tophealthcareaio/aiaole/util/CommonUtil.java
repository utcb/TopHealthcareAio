package com.haiforce.tophealthcareaio.aiaole.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.haiforce.tophealthcareaio.BaseActivity;
import com.haiforce.tophealthcareaio.R;
import com.haiforce.tophealthcareaio.aiaole.Receiver.BleReceiver;
import com.haiforce.tophealthcareaio.aiaole.Receiver.ConnectionChangeReceiver;
import com.haiforce.tophealthcareaio.aiaole.modle.Gloal;
import com.xtremeprog.sdk.ble.BleService;
import com.xtremeprog.sdk.ble.BleService.BLESDK;

import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class CommonUtil {
	public static final String DEBUG = Gloal.DEBUG + "CommonUtil";

	public static String readTextFile(InputStream inputStream) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		byte buf[] = new byte[1024];

		int len;

		try {

			while ((len = inputStream.read(buf)) != -1) {

				outputStream.write(buf, 0, len);

			}

			outputStream.close();

			inputStream.close();

		} catch (IOException e) {

		}

		return outputStream.toString();

	}

	public static String getType(String val) {
		if (null == val) {
			return "";
		}
		return val.substring(5 * 2, 6 * 2);
	}

	public static boolean isBsp(String name) {
		if (null != name && name.startsWith("Bioland")) {
			return true;
		} else {
			return false;
		}

	}

	public static String getVersion(Context context)// 获取版本号
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return context
					.getString(R.string.version_unknown);
		}
	}

	public static boolean notNull(String str) {
		if (null == str || "".equals(str.trim()) || "null".equals(str.trim())) {
			return false;
		}
		return true;
	}

	public static void scan(Context ctx, boolean fla) {

		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		if (null != mBluetoothAdapter && !mBluetoothAdapter.isEnabled()) {
			return;
		}

		Intent scanIntent = new Intent(BleReceiver.SCAN_ACTION);

		if (!fla) {
			scanIntent.putExtra(BleReceiver.SCAN_FLA, BleReceiver.SCAN_COLSE);
		} else {
			scanIntent.putExtra(BleReceiver.SCAN_FLA, BleReceiver.SCAN_OPEN);

		}
		ctx.sendBroadcast(scanIntent);
	}

	public static boolean checeBule(BaseActivity activity) {
		boolean fla = false;

		if (BleService.goble_BleSDK == BLESDK.ANDROID) {

			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
				// Toast.makeText(this, "本机没有找到蓝牙硬件或驱动！",
				// Toast.LENGTH_SHORT).show();
				activity.finish();
				fla = false;
			}
			// 如果本地蓝牙没有开启，则开启
			if (!mBluetoothAdapter.isEnabled()) {
				Intent mIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(mIntent, 1);
			} else {
				fla = true;
			}

		} else {
			activity.showToast(activity.getResources().getString(
					R.string.ble_tishi));
		}
		return fla;
	}

	// 判断当前是否使用的是 WIFI网络
	public static boolean isWifiActive(Context icontext) {
		Context context = icontext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// public static float switch

	/**
	 * 获取和保存当前屏幕的截图
	 */
	public static String GetandSaveCurrentImage(Activity ctx) {
		// 构建Bitmap
		WindowManager windowManager = ctx.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();
		Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		// 获取屏幕
		View decorview = ctx.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();
		// 图片存储路径
		String SavePath = getSDCardPath() + "/ScreenImages";
		// 保存Bitmap
		String filepath = "";
		try {
			File path = new File(SavePath);
			// 文件
			filepath = SavePath + "/Screen_1.png";
			File file = new File(filepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
				Toast.makeText(ctx, "截屏文件已保存至SDCard/ScreenImages/目录下",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return filepath;
	}

	public static String swithXueTang(String result) {
		double resultValue = Double.parseDouble(result);
		resultValue = resultValue / 18;
		return MathExtend.round(resultValue, 1) + "";
	}

	public static String swithXueTang_KD(String result) {
		double resultValue = Double.parseDouble(result);
		resultValue = resultValue / 18;
		return MathExtend.round(resultValue, 0) + "";
	}

	/**
	 * 获取SDCard的目录路径功能
	 * 
	 * @return
	 */
	private static String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	/**
	 * 通过byte数组取到short
	 * 
	 * @param b
	 * @param index
	 *            第几位开始取
	 * @return
	 */
	public static short getShort(byte[] b, int index) {
		return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
	}

	public static String getDeviceSerial(byte[] val) {
		if (null == val || val.length < 13) {
			return "";
		}
		byte[] xlh = new byte[3];
		xlh[0] = val[12];
		xlh[1] = val[11];
		xlh[2] = val[10];

		String xlhStr = new String(Hex.encodeHex(xlh));
		Log.d(DEBUG, "序列号:" + xlhStr);
		return xlhStr;
	}

	public static boolean isZh(Context ctx) {
		Locale locale = ctx.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			return true;
		else
			return false;
	}

	public static void isNetWork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (activeNetInfo != null && activeNetInfo.isAvailable()) {
			ConnectionChangeReceiver.connectionFla = true;
		} else {
			ConnectionChangeReceiver.connectionFla = false;
		}
	}

}
