package com.damuzhi.travel.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

public class CheckNet extends BroadcastReceiver
{
	private static final String TAG = "CheckNet";

	/**
     * ����ConnectivityManager ConnectivityManager��Ҫ���������������صĲ���
     * ��ص�TelephonyManager�������ֻ�����Ӫ�̵ȵ������Ϣ��WifiManager������wifi��ص���Ϣ��
     * ���������״̬�����ȵ����Ȩ��<uses-permission
     * android:name="android.permission.ACCESS_NETWORK_STATE"/>
     * NetworkInfo������˶�wifi��mobile��������ģʽ���ӵ���ϸ����,ͨ����getState()������ȡ��State�����������
     * ���ӳɹ�����״̬��
     *
     */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		boolean available = false;
		// TODO Auto-generated method stub
          ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);;
          // ��ȡ��������״̬��NetWorkInfo����
          NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
          // ��ȡ��ǰ�����������Ƿ����
          if (null == networkInfo)
          {
              Toast.makeText(context, "��ǰ�����粻����", Toast.LENGTH_SHORT).show();
              //�����粻����ʱ����ת����������ҳ��
          } else
          {
               available = networkInfo.isAvailable();
              if (!available)
              {
            	  Log.d(TAG, "��ǰ�����粻����");
                  Toast.makeText(context, "��ǰ�����粻����", Toast.LENGTH_SHORT).show();
              } 
          }
          State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
          if (State.CONNECTED == state)
          {
              Log.d(TAG, "GPRS����������");
              //Toast.makeText(context, "GPRS����������", Toast.LENGTH_SHORT).show();
          }
          state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
          if (State.CONNECTED == state)
          {
              Log.d(TAG, "WIFI����������");
              //Toast.makeText(context, "WIFI����������", Toast.LENGTH_SHORT).show();
          }

          // // ��ת�������������ý���
          //startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
          // // ��ת������wifi�������ý���
          // startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
           //return START_STICKY;
	}
}
