package com.damuzhi.travel.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class PicUtill {
	/**
	 * ����һ����������(URL)��ȡbitmapDrawableͼ��
	 * @param imageUri
	 * @return
	 */
	public static BitmapDrawable getfriendicon(URL imageUri) {

		BitmapDrawable icon = null;
		try {
			HttpURLConnection hp = (HttpURLConnection) imageUri.openConnection();
			icon = new BitmapDrawable(hp.getInputStream());// ��������ת����bitmap
			hp.disconnect();// �ر�����
		} catch (Exception e) {
		}
		return icon;
	}

	/**
	 * ����һ����������(String)��ȡbitmapDrawableͼ��
	 * @param imageUri
	 * @return
	 */
	public static BitmapDrawable getcontentPic(String imageUri) {
		URL imgUrl = null;
		try {
			imgUrl = new URL(imageUri);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		BitmapDrawable icon = null;
		try {
			HttpURLConnection hp = (HttpURLConnection) imgUrl.openConnection();
			icon = new BitmapDrawable(hp.getInputStream());// ��������ת����bitmap
			hp.disconnect();// �ر�����
		} catch (Exception e) {
		}
		return icon;
	}

	/**
	 *  ����һ����������(URL)��ȡbitmapͼ��
	 * @param imageUri
	 * @return
	 */
	public static Bitmap getusericon(URL imageUri) {
		// ��ʾ�����ϵ�ͼƬ
		URL myFileUrl = imageUri;
		Bitmap bitmap = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	/**
	 *  ����һ����������(String)��ȡbitmapͼ��
	 * @param imageUri
	 * @return
	 * @throws Exception 
	 * @throws MalformedURLException 
	 */
	public static Bitmap getbitmap(String imageUri)  {
		// ��ʾ�����ϵ�ͼƬ
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
						
		}
		return bitmap;
	}
	
	
	/**
	 *  ����һ����������(String)��ȡbitmapͼ��
	 * @param imageUri
	 * @return
	 * @throws Exception 
	 * @throws MalformedURLException 
	 */
	public static Drawable getDrawable(String imageUri)  {
		// ��ʾ�����ϵ�ͼƬ
		Drawable drawable = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			drawable = Drawable.createFromStream(is, "src");
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
						
		}
		return drawable;
	}
	
	/**
	 * ����ͼƬ ͬʱд�����ػ����ļ���
	 * @param context
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmapAndwrite(Context context,String imageUri) throws MalformedURLException {
		// ��ʾ�����ϵ�ͼƬ
		URL myFileUrl = new URL(imageUri);
		Bitmap bitmap = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			String name=MD5Util.MD5(imageUri);
			BufferedOutputStream bufferedOutputStream=null;
			bufferedOutputStream=new BufferedOutputStream(context.openFileOutput(name, Context.MODE_PRIVATE));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100,bufferedOutputStream);
			is.close();
			bufferedOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	public static boolean  downpic(String picName,Bitmap bitmap) {
		boolean nowbol=false;
		try {
			File saveFile = new File("/mnt/sdcard/download/damuzhi/"+picName+".png");
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			FileOutputStream saveFileOutputStream;
			saveFileOutputStream = new FileOutputStream(saveFile);
		    nowbol = bitmap.compress(Bitmap.CompressFormat.PNG, 100, saveFileOutputStream);
			saveFileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
	}
     return nowbol;
	} 
	
	public static void writeTofiles(Context context,Bitmap bitmap,String filename){
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(context.openFileOutput(
					filename, Context.MODE_PRIVATE));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ���ļ�д�뻺��ϵͳ��
	 * 
	 * @param filename
	 * @param is
	 * @return
	 */
	public static String writefile(Context context,String filename, InputStream is) {
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			inputStream = new BufferedInputStream(is);
			outputStream = new BufferedOutputStream(context.openFileOutput(
					filename, Context.MODE_PRIVATE));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
		} catch (Exception e) {
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return context.getFilesDir() + "/" + filename+".jpg";
	}
}
