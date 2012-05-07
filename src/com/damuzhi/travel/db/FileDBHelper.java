package com.damuzhi.travel.db;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * ҵ��bean
 *
 */
public class FileDBHelper {
	private DBOpenHelper openHelper;

	public FileDBHelper(Context context) {
		openHelper = new DBOpenHelper(context);
	}
	/**
	 * ��ȡÿ���߳��Ѿ����ص��ļ�����
	 * @param path
	 * @return
	 */
	public Map<Integer, Integer> getData(String path){
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select threadid, downlength from FileDownlog where downpath=?", new String[]{path});
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		while(cursor.moveToNext()){
			data.put(cursor.getInt(0), cursor.getInt(1));
		}
		cursor.close();
		db.close();
		return data;
	}
	/**
	 * ����ÿ���߳��Ѿ����ص��ļ�����
	 * @param path
	 * @param map
	 */
	public void save(String path,  Map<Integer, Integer> map){//int threadid, int position
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		try{
			for(Map.Entry<Integer, Integer> entry : map.entrySet()){
				db.execSQL("insert into FileDownlog(downpath, threadid,filelength, downlength) values(?,?,?,?)",
						new Object[]{path, entry.getKey(), entry.getValue()});
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		db.close();
	}
	/**
	 * ʵʱ����ÿ���߳��Ѿ����ص��ļ�����
	 * @param path
	 * @param map
	 */
	public void update(String path, Map<Integer, Integer> map){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		try{
			for(Map.Entry<Integer, Integer> entry : map.entrySet()){
				db.execSQL("update FileDownlog set downlength=? where downpath=? and threadid=?",new Object[]{entry.getValue(), path, entry.getKey()});
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		db.close();
	}
	/**
	 * ���ļ�������ɺ�ɾ����Ӧ�����ؼ�¼
	 * @param path
	 */
	public void delete(String path){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.execSQL("delete from FileDownlog where downpath=?", new Object[]{path});
		db.close();
	}
	
}
