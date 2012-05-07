package com.damuzhi.travel.activity.common.imageCache;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;



import com.damuzhi.travel.util.PicUtill;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class ImageLoader {
	
	
	//һ��map�����ڻ������ع���ͼƬ
		private HashMap<String, SoftReference<Bitmap>> caches;
		//�������
		private ArrayList<Task> taskQueue;
		
		
		private Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				//���߳��з��ص�������ɵ�����
				Task task = (Task)msg.obj;
				//����callback�����loadedImage��������ͼƬ·����ͼƬ�ش���adapter
				task.callback.loadedImage(task.path, task.bitmap);
			}
			
		};
		
		
		//���������߳�
		private Thread thread = new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//������ѯ
				while(true){
					//����������л���δ��������ʱ��ִ����������
					while(taskQueue.size()>0){
						//��ȡ��һ�����񣬲���֮����������Ƴ�
						Task task = taskQueue.remove(0);
						//�����ص�ͼƬ��ӵ�����
						task.bitmap=PicUtill.getbitmap(task.path);
						caches.put(task.path, new SoftReference<Bitmap>(task.bitmap));
						//���handler����Ϊnull
						if(handler!=null){
							//������Ϣ���󣬲�����ɵ�������ӵ���Ϣ������
							Message msg = handler.obtainMessage();
							msg.obj = task;
							//������Ϣ�����߳�
							handler.sendMessage(msg);
						}
					}
					//����������Ϊ�գ������̵߳ȴ�
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		};
		
		
		
		//��������ʱ����ʼ��map
		public ImageLoader(){
			caches =new  HashMap<String, SoftReference<Bitmap>>();
			taskQueue = new ArrayList<ImageLoader.Task>();
			//�������������߳�
			thread.start();
		}
		
		
	   //�жϵ�ǰ�������Ƿ������ͼƬ
		public boolean ishavekey(String url){
			return caches.containsKey(url);
		}
		
		
		
		//ͼƬ���ط���
		public Bitmap loadImage(final String path,final ImageCallback callback){
			//���ͼƬ·�����ڻ����д�����������
			if(caches.containsKey(path)){
				//ȡ��������
				SoftReference<Bitmap> rf = caches.get(path);
				//ͨ�������ã���ȡͼƬ
				Bitmap bitmap = rf.get();
				//�����ͼƬ�Ѿ����ͷţ��򽫸�path��Ӧ�ļ�ֵ�Դ�map���Ƴ�
				if(bitmap==null){
					caches.remove(path);				
				}else{
					 //Log.i("size", "-------------------"+caches.size());
					//�����ͼƬδ���ͷţ��򷵻ظ�ͼƬ
					return bitmap;
				}
			}
			if(!caches.containsKey(path)){
				//�����·����ͼƬδ�ڻ�����
				//�򴴽���������ӵ��������
				Task task = new Task();
				task.path = path;
				task.callback = callback;
				if(!taskQueue.contains(task)){
					taskQueue.add(task);
					//�������������߳�
					synchronized(thread){
						thread.notify();
					}
				}
			}
			//���������û��ͼƬ�򷵻�null
			return null;
		}
		
		
		
		public interface ImageCallback{
			void loadedImage(String path,Bitmap bitmap);
		}
		
		
		
		//������
		class Task{
			String path;//�������������·��
			Bitmap bitmap;//���ص�ͼƬ
			ImageCallback callback;//�ص�����
			@Override
			public boolean equals(Object o) {
				// TODO Auto-generated method stub				
				return ((Task)o).path.equals(path);
			}
			
			
		}
}
