package com.haiforce.tophealthcareaio.cvr100b;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.*;
import android.bluetooth.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import java.io.*;
import java.util.*;
import com.haiforce.tophealthcareaio.R;
import com.ivsign.android.IDCReader.IDCReaderSDK;

public class Cvr100bActivity extends Activity {
	
	BluetoothAdapter myBluetoothAdapter = null; 
    BluetoothServerSocket mBThServer = null; 
    BluetoothSocket mBTHSocket = null; 
    InputStream mmInStream = null;
    OutputStream mmOutStream = null;
    int Readflage = -99;

    byte[] cmd_SAM = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x12, (byte) 0xFF, (byte) 0xEE  };
    byte[] cmd_find  = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x01, 0x22  };
	byte[] cmd_selt  = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x02, 0x21  };
	byte[] cmd_read  = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x30, 0x01, 0x32 };
	byte[] cmd_sleep  = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x02, 0x00, 0x02};
	byte[] cmd_weak  = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x02, 0x01, 0x03 };
	byte[] recData = new byte[1500];
	
    String DEVICE_NAME1 = "CVR-100B";
    String DEVICE_NAME2 = "IDCReader";
    String DEVICE_NAME3 = "COM2";
    String DEVICE_NAME4 = "BOLUTEK";
    
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String[] decodeInfo = new String[10];
            
    private static final String TAG = "IDCReaderSDK";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cvr100b_main);
        
        
        Button btconn = (Button)findViewById(R.id.btconn);
        btconn.getBackground().setAlpha(160);
        Button btread = (Button)findViewById(R.id.btread);
        btread.getBackground().setAlpha(160);
        Button btclose = (Button)findViewById(R.id.btclose);
        btclose.getBackground().setAlpha(160);
        Button btsleep = (Button)findViewById(R.id.btsleep);
        btsleep.getBackground().setAlpha(160);
        Button btweak  = (Button)findViewById(R.id.btweak);
        btweak.getBackground().setAlpha(160);
        //final EditText ett = (EditText)findViewById(R.id.editText1);
        final TextView ett = (TextView)findViewById(R.id.textView1);
        final ImageView image = (ImageView)findViewById(R.id.imageView1);   
        //image.getBackground().setAlpha(50);
        
                
        btconn.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
            {    
        		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
        		Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        		if (pairedDevices.size() > 0)
        		{
        			for (Iterator<BluetoothDevice> iterator = pairedDevices.iterator(); iterator.hasNext();)
        			{
        				BluetoothDevice device = (BluetoothDevice)iterator.next();
        				if (DEVICE_NAME1.equals(device.getName())||DEVICE_NAME2.equals(device.getName())||DEVICE_NAME3.equals(device.getName())||DEVICE_NAME4.equals(device.getName()))
        				{        					
        			        try 
        			        {
        			            myBluetoothAdapter.enable();
        			            Intent discoverableIntent = new Intent( 
        			                        BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);//使得蓝牙处于可发现模式，持续时间150s 
        			            discoverableIntent.putExtra( 
        			                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 150); 
        			            mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        			            
        			            int sdk = Integer.parseInt(Build.VERSION.SDK);
        			            if (sdk >= 10) {
        			            	mBTHSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
        			            } else {
        			            	mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        			            }
        			            
        			            mBThServer = myBluetoothAdapter.listenUsingRfcommWithServiceRecord("myServerSocket",MY_UUID);
        			            mBTHSocket.connect();         			            
        			        	mmInStream = mBTHSocket.getInputStream();
        			            mmOutStream = mBTHSocket.getOutputStream();
        			        	
        			        } catch (IOException e)
        			        {
        			        	ett.setText("设备连接异常！");
        			        }
        			        if((mmInStream != null)&&(mmInStream != null))
        			        {
        			        	ett.setText("设备连接成功！");
        			        }
        			        else
        			        {
        			        	ett.setText("设备连接失败！");
        			        }
        					break;
        				}
        			}
        		}
            }
        });
        
        btread.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
            { 
        		int readcount = 15;
        		try 
        		{ 
        			while(readcount > 1)
        			{
        				ReadCard();
        				readcount = readcount - 1;
        				if(Readflage > 0)
        				{
        					readcount = 0;
        					ett.setText("姓名：" + decodeInfo[0] + "\n" + "性别：" + decodeInfo[1] + "\n" + "民族：" + decodeInfo[2] + "\n"
	    			        		+ "出生日期：" + decodeInfo[3] + "\n" + "地址：" + decodeInfo[4] + "\n" + "身份号码：" + decodeInfo[5] + "\n"
	    			        		+ "签发机关：" + decodeInfo[6] + "\n" + "有效期限：" + decodeInfo[7] + "-" + decodeInfo[8] + "\n"
	    			        		+ decodeInfo[9] + "\n");        					
        					if(Readflage == 1)
        					{
        						FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/wltlib/zp.bmp");
								Bitmap bmp = BitmapFactory.decodeStream(fis);
								fis.close();
								image.setImageBitmap(bmp);
        					}
        					else
        					{
        						ett.append("照片解码失败，请检查路径" + Environment.getExternalStorageDirectory() + "/wltlib/");
        						image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face));
        					}        					
        				}
        				else
        				{        					
        					image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face));
        					if(Readflage == -2)
        					{
        						ett.setText("蓝牙连接异常");
        					}
        					if(Readflage == -3)
        					{
        						ett.setText("无卡或卡片已读过");
        					}
        					if(Readflage == -4)
        					{
        						ett.setText("无卡或卡片已读过");
        					}
        					if(Readflage == -5)
        					{
        						ett.setText("读卡失败");
        					}
        					if(Readflage == -99)
        					{
        						ett.setText("操作异常");
        					}
        				}        				
        				Thread.sleep(100);	
        			}        				
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					ett.setText("读取数据异常！");
					image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					ett.setText("读取数据异常！");
					image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face));
				} 
            }
        });
        
        btsleep.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
            {
        		try {
        			if((mmInStream == null)||(mmInStream == null))
			        {
			        	ett.setText("设备未正常连接！");
			        	return;
			        }
					mmOutStream.write(cmd_sleep);
					ett.setText("睡眠模式！");
					image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        btweak.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
            {
        		try {
        			if((mmInStream == null)||(mmInStream == null))
			        {
			        	ett.setText("设备未正常连接！");
			        	return;
			        }
					mmOutStream.write(cmd_weak);
					ett.setText("唤醒模式！");
					image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        btclose.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
            {   
        		try {
        			if((mmInStream == null)||(mmInStream == null))
			        {			        	
			        	return;
			        }
        			mmOutStream.close();
        			mmInStream.close();
					mBTHSocket.close();
					mBThServer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
            }
        });      
    }

    private void ReadCard()
    {    	
		try 
		{        			
			if((mmInStream == null)||(mmInStream == null))
	        {	        	
				Readflage = -2;//连接异常
				return;
	        }			
			mmOutStream.write(cmd_find);
			Thread.sleep(200);					
			int datalen = mmInStream.read(recData);
			if(recData[9] == -97)
			{						
				mmOutStream.write(cmd_selt);
				Thread.sleep(200);					
				datalen = mmInStream.read(recData);
				if(recData[9] == -112)
				{
					mmOutStream.write(cmd_read);
					Thread.sleep(1000);	
					byte[] tempData = new byte[1500];
					if(mmInStream.available()>0)
					{
						datalen = mmInStream.read(tempData);
					}
					else
					{
						Thread.sleep(500);	
						if(mmInStream.available()>0)
						{
							datalen = mmInStream.read(tempData);
						}
					}
					int flag = 0;
					if(datalen <1294)
					{
						for(int i = 0;i<datalen ;i++,flag++)
						{									
							recData[flag] = tempData[i];
						}								
						Thread.sleep(1000);
						if(mmInStream.available()>0)
						{
							datalen = mmInStream.read(tempData);
						}
						else
						{
							Thread.sleep(500);
							if(mmInStream.available()>0)
							{
								datalen = mmInStream.read(tempData);
							}									
						}
						for(int i = 0;i<datalen ;i++,flag++)
						{									
							recData[flag] = tempData[i];
						}
						
					}
					else
					{
						for(int i = 0;i<datalen ;i++,flag++)
						{									
							recData[flag] = tempData[i];
						}	
					}
					tempData = null;
					if(flag == 1295)
					{								
						if(recData[9] == -112)
						{		
							
							byte[] dataBuf = new byte[256];								
							for(int i = 0; i < 256; i++)
							{
								dataBuf[i] = recData[14 + i];
							}
							String TmpStr = new String(dataBuf, "UTF16-LE");
							TmpStr = new String(TmpStr.getBytes("UTF-8"));
							decodeInfo[0] = TmpStr.substring(0, 15);
							decodeInfo[1] = TmpStr.substring(15, 16);
							decodeInfo[2] = TmpStr.substring(16, 18);
							decodeInfo[3] = TmpStr.substring(18, 26);
							decodeInfo[4] = TmpStr.substring(26, 61);
							decodeInfo[5] = TmpStr.substring(61, 79);
							decodeInfo[6] = TmpStr.substring(79, 94);
							decodeInfo[7] = TmpStr.substring(94, 102);
							decodeInfo[8] = TmpStr.substring(102, 110);
							decodeInfo[9] = TmpStr.substring(110, 128);
							if (decodeInfo[1].equals("1"))
								decodeInfo[1] = "男";
							else
								decodeInfo[1] = "女";
							try
							{
								int code = Integer.parseInt(decodeInfo[2].toString());
								decodeInfo[2] = decodeNation(code);
							}
							catch (Exception e)
							{
								decodeInfo[2] = "";
							}
							
							//照片解码									
							try
							{	
								int ret = IDCReaderSDK.Init();
								if (ret == 0)
								{	
									byte[] datawlt = new byte[1384];
									byte[] byLicData = {(byte)0x05,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x5B,(byte)0x03,(byte)0x33,(byte)0x01,(byte)0x5A,(byte)0xB3,(byte)0x1E,(byte)0x00};
									for(int i = 0; i < 1295; i++)
									{
									 	datawlt[i] = recData[i];
									}
									int t = IDCReaderSDK.unpack(datawlt,byLicData);
									if(t == 1)
									{
										Readflage = 1;//读卡成功
									}
									else
									{
										Readflage = 6;//照片解码异常
									}											
								}
								else
								{
									Readflage = 6;//照片解码异常
								}										
							}
							catch(Exception e)
							{								
								Readflage = 6;//照片解码异常
							}								
							
						}
						else
						{								
							Readflage = -5;//读卡失败！
						}
					}
					else
					{
						Readflage = -5;//读卡失败
					}					
				}
				else
				{							
					Readflage = -4;//选卡失败
				}
			}
			else
			{						
				Readflage = -3;//寻卡失败		        
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Readflage = -99;//读取数据异常
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Readflage = -99;//读取数据异常
		}
    }
    private String decodeNation(int code)
	{
		String nation;
		switch (code)
		{
		case 1: 
			nation = "汉";
			break;
		case 2: 
			nation = "蒙古";
			break;
		case 3:
			nation = "回";
			break;
		case 4:
			nation = "藏";
			break;
		case 5: 
			nation = "维吾尔";
			break;
		case 6:
			nation = "苗";
			break;
		case 7:
			nation = "彝";
			break;
		case 8: 
			nation = "壮";
			break;
		case 9:
			nation = "布依";
			break;
		case 10:
			nation = "朝鲜";
			break;
		case 11:
			nation = "满";
			break;
		case 12:
			nation = "侗";
			break;
		case 13:
			nation = "瑶";
			break;
		case 14:
			nation = "白";
			break;
		case 15:
			nation = "土家";
			break;
		case 16:
			nation = "哈尼";
			break;
		case 17:
			nation = "哈萨克";
			break;
		case 18:
			nation = "傣";
			break;
		case 19:
			nation = "黎";
			break;
		case 20:
			nation = "傈僳";
			break;
		case 21:
			nation = "佤";
			break;
		case 22:
			nation = "畲";
			break;
		case 23:
			nation = "高山";
			break;
		case 24:
			nation = "拉祜";
			break;
		case 25: 
			nation = "水";
			break;
		case 26: 
			nation = "东乡";
			break;
		case 27: 
			nation = "纳西";
			break;
		case 28: 
			nation = "景颇";
			break;
		case 29: 
			nation = "柯尔克孜";
			break;
		case 30: 
			nation = "土";
			break;
		case 31: 
			nation = "达斡尔";
			break;
		case 32: 
			nation = "仫佬";
			break;
		case 33: 
			nation = "羌";
			break;
		case 34: 
			nation = "布朗";
			break;
		case 35: 
			nation = "撒拉";
			break;
		case 36: 
			nation = "毛南";
			break;
		case 37: 
			nation = "仡佬";
			break;
		case 38: 
			nation = "锡伯";
			break;
		case 39:
			nation = "阿昌";
			break;
		case 40: 
			nation = "普米";
			break;
		case 41:
			nation = "塔吉克";
			break;
		case 42:
			nation = "怒";
			break;
		case 43:
			nation = "乌孜别克";
			break;
		case 44:
			nation = "俄罗斯";
			break;
		case 45: 
			nation = "鄂温克";
			break;
		case 46: 
			nation = "德昂";
			break;
		case 47: 
			nation = "保安";
			break;
		case 48:
			nation = "裕固";
			break;
		case 49: 
			nation = "京";
			break;
		case 50: 
			nation = "塔塔尔";
			break;
		case 51:
			nation = "独龙";
			break;
		case 52:
			nation = "鄂伦春";
			break;
		case 53: 
			nation = "赫哲";
			break;
		case 54: 
			nation = "门巴";
			break;
		case 55: 
			nation = "珞巴";
			break;
		case 56:
			nation = "基诺";
			break;
		case 97:
			nation = "其他";
			break;
		case 98: 
			nation = "外国血统中国籍人士";
			break;
		default:
			nation = "";
			break;
		}
		return nation;
	}
    /*
    static 
	{
	    try
		{		
			System.loadLibrary("wltdecode");		
		}
		catch(Exception e)
		{
			Log.d(TAG, "异常");
		}
	}
    
    public static native int wltInit(String s);

	public static native int wltGetBMP(byte abyte0[], byte abyte1[]);
*/
}