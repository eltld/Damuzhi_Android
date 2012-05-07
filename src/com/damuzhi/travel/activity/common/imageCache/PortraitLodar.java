package com.damuzhi.travel.activity.common.imageCache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.damuzhi.travel.util.MD5Util;
import com.damuzhi.travel.util.PicUtill;


public class PortraitLodar {
	//�������
		Context context;
		//һ��map�����ڻ������ع���ͼƬ
		private HashMap<String, SoftReference<Bitmap>> caches;
		private ArrayList<DownTask> DownTaskQueue;
		private Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				//���߳��з��ص�������ɵ�����
				DownTask DownTask = (DownTask)msg.obj;
				//����callback�����loadedImage��������ͼƬ·����ͼƬ�ش���adapter
				DownTask.callback.loadedImage(DownTask.path, DownTask.bitmap);
			}
			
		};
		//���������߳�
		private Thread thread = new Thread(){

			@Override
			public void run() {
				//������ѯ
				while(true){
					//����������л���δ��������ʱ��ִ����������
					while(DownTaskQueue.size()>0){
						//��ȡ��һ�����񣬲���֮����������Ƴ�
						DownTask downTask = DownTaskQueue.remove(0);
						try {
							//����ͼƬ
							//�����ص�ͼƬ��ӵ��ļ�����
							downTask.bitmap=PicUtill.getbitmapAndwrite(context, downTask.path);
							//���handler����Ϊnull
							caches.put(downTask.path, new SoftReference<Bitmap>(downTask.bitmap));
							if(handler!=null){
								//������Ϣ���󣬲�����ɵ�������ӵ���Ϣ������
								Message msg = handler.obtainMessage();
								msg.obj = downTask;
								//������Ϣ�����߳�
								handler.sendMessage(msg);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					//����������Ϊ�գ������̵߳ȴ�
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		//��������ʱ����ʼ��map
		public PortraitLodar(Context context){
			caches =new  HashMap<String, SoftReference<Bitmap>>();
			DownTaskQueue = new ArrayList<PortraitLodar.DownTask>();
			//�������������߳�
			this.context=context;
			thread.start();
		}
		//ͼƬ���ط���
		public Bitmap loadImage(final String path,final PortraitImgCallback callback){
			//���ͼƬ·�����ڻ����д�����������
	       Bitmap bitmap=null;
		   bitmap=getbitmapMap(path);
		   if (bitmap==null) {
			  bitmap=getFromFile(path);
			  caches.put(path, new SoftReference<Bitmap>(bitmap));
			   if (bitmap!=null) {
				return bitmap;
			   }else {
				 //�����·����ͼƬδ�ڻ�����
					//�򴴽���������ӵ��������
				    DownTask DownTask = new DownTask();
					DownTask.path = path;
					Log.i("path", path);
					DownTask.callback = callback;
					if(!DownTaskQueue.contains(DownTask)){
						DownTaskQueue.add(DownTask);
						//�������������߳�
						synchronized(thread){
							thread.notify();
						}
					}
			}
		}else {
			return bitmap;
		}
			return null;
		}
		
		
		public Bitmap getbitmapMap(String path){
			  Bitmap bitmap=null;
			  if (caches.containsKey(path)) {
				  SoftReference<Bitmap> rf = caches.get(path);
					//ͨ�������ã���ȡͼƬ
					 bitmap = rf.get();
					//�����ͼƬ�Ѿ����ͷţ��򽫸�path��Ӧ�ļ�ֵ�Դ�map���Ƴ�
					if(bitmap==null){
						caches.remove(path);
						return null;
					}else{
						//�����ͼƬδ���ͷţ��򷵻ظ�ͼƬ
						return bitmap;
					}
			}else {
				return null;
			}
				
		}
		public interface PortraitImgCallback{
			void loadedImage(String path,Bitmap bitmap);
		}
		/**
		 * ���ļ���ȡBitmap
		 * @param url
		 *            �ļ�����
		 * @return
		 */
		private Bitmap getFromFile(String url) {
			/* �˴����ļ��������зָ��� */
			String name = MD5Util.MD5(url);
			FileInputStream inputStream = null;
			try {
				inputStream = context.openFileInput(name);
				return BitmapFactory.decodeStream(inputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} finally {
				if (null != inputStream) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		//������
		class DownTask{
			String path;//�������������·��
			Bitmap bitmap;//���ص�ͼƬ
			PortraitImgCallback callback;//�ص�����
			@Override
			public boolean equals(Object o) {
				return ((DownTask)o).path.equals(path);
			}
		}
}
