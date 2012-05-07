package com.damuzhi.travel.service;

import java.util.ArrayList;
import com.damuzhi.travel.R;
import com.damuzhi.travel.activity.common.PlaceActivity;
import com.damuzhi.travel.activity.common.TravelApplication;
import com.damuzhi.travel.activity.place.SceneryActivity;
import com.damuzhi.travel.model.constant.ConstantField;
import com.damuzhi.travel.model.place.PlaceManager;
import com.damuzhi.travel.protos.PlaceListProtos.Place;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MainService extends Service implements Runnable
{
	private static final String TAG = "IndexControlService";
	private static TravelApplication application;
	public static  String dataPath ="";
	
	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();
	// ����������ŵ����񼯺���
	public static ArrayList<Task> allTask = new ArrayList<Task>();

	// ��������activity ���������� allActivity ���ҵ���Ҫ��activity
	public static Activity getActivityByName(String name) {
		for (Activity ac : allActivity) {
			if (ac.getClass().getName().indexOf(name) >= 0) {
				return ac;
			}
		}
		return null;
	}

	// ����ǰ������ӵ����񼯺���
	public static void newTask(Task task) {
		allTask.add(task);
	}

	public boolean isrun = true;// �߳̿���
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Task.TASK_LOGIN_SCENERY:// 
				DataService placeDataService = new DataService(application);
				placeDataService.getPlace(ConstantField.SCENERY, application.getCityID(), ConstantField.LANG_HANS);
				PlaceActivity placeActivity = (PlaceActivity) MainService.getActivityByName(ConstantField.SCENERY_ACTIVITY);
				placeActivity.refresh();
				break;
			
			}

		}
	};

	
	
	private void doTask(Task task) {
		Message mess = handler.obtainMessage();
		mess.what = task.getTaskID();// ����ǰ�����ID �ŵ�Message��		
		switch (task.getTaskID()) {
		case Task.TASK_LOGIN_SCENERY:// 
			break;
		
		}
		handler.sendMessage(mess);// ���͵�ǰ��Ϣ
		allTask.remove(task);// ��ǰ����ִ����� ����������񼯺���remove ��Ȼ���ظ�ִ��
	}

	
	@Override
	public void run() {
		while (isrun) {
			Task lastTask = null;
			synchronized (allTask) {
				if (allTask.size() > 0) {
					lastTask = allTask.get(0);
					Log.i(TAG, "task = " + lastTask.getTaskID());
					doTask(lastTask);
				}
			}
			// ÿ��һ���Ӽ���Ƿ�������
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		application = (TravelApplication) this.getApplication();
		dataPath = String.format(ConstantField.DATA_PATH,application.getCityID());
		isrun = true;// �����߳�
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.stopSelf();// ֹͣ����
		isrun = false;// �ر��߳�
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	

	/**
	 * �˳�Ӧ�ó���
	 * 
	 * @param context
	 */
	public static void exitAPP(Context context) {
		Intent it = new Intent(ConstantField.MAIN_SERVICE);
		context.stopService(it);// ֹͣ����		
		for (Activity activity : allActivity) {// ��������activity һ��һ��ɾ��
			activity.finish();
		}
		// ɱ������ �Ҹо����ַ�ʽ��ֱ���˵�
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
   public static void finshall(){
		for (Activity activity : allActivity) {// ��������activity һ��һ��ɾ��
			activity.finish();
		}
   }
	/**
	 * ���������쳣
	 * 
	 * @param context
	 */
	public static void AlertNetError(final Context context) {
		AlertDialog.Builder alerError = new AlertDialog.Builder(context);
		alerError.setTitle(R.string.main_fetch_fail);
		alerError.setMessage(R.string.NoSignalException);
		alerError.setNegativeButton(R.string.apn_is_wrong1_exit,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						exitAPP(context);
					}
				});
		alerError.setPositiveButton(R.string.apn_is_wrong1_setnet,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						context.startActivity(new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
		alerError.create().show();
	}
	


}
