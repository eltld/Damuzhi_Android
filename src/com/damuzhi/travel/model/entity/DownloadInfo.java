package com.damuzhi.travel.model.entity;

import android.R.integer;

public class DownloadInfo {

//	public long threadId;// ������id
//	public long startPos;// ��ʼ��
//	public long endPos;// ������
//	public long compeleteSize;// ��ɶ�
	public String url;// �����������ʶ
	public long currentPosition;//
	public long totalBytes;//�������ļ���С
	public long speed;//�����ٶ�
	/**
	 * @param url
	 * @param currentPosition
	 * @param totalBytes
	 * @param speed
	 */
	public DownloadInfo(String url, long currentPosition, long totalBytes,
			long speed)
	{
		super();
		this.url = url;
		this.currentPosition = currentPosition;
		this.totalBytes = totalBytes;
		this.speed = speed;
	}
	
	
	
}
