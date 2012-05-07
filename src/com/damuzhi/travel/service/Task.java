package com.damuzhi.travel.service;

import java.util.Map;

public class Task
{
	  private int taskID;//����ID
	  @SuppressWarnings("rawtypes")
	 // private Map taskParam;//���ݲ���
	  public static final int TASK_USER_LOGIN=1;//�û���¼���� 
	  public static final int TASK_LOGIN_SCENERY=2;// ���뾰��ҳ��
	  public static final int TASK_LOGIN_HOTEL=3;// ����Ƶ�ҳ��
	  public static final int TASK_LOGIN_SHOPPING=4;// ���빺��ҳ��
	  public static final int TASK_LOGIN_RESTAURANT=5;// ����͹�ҳ��
	  public static final int TASK_LOGIN_FUN=6;// ��������ҳ��
	  public static final int TASK_LOGIN_ROUND=7;// ���븽��ҳ��
	  public static final int CITY_ABOUT=2;// ������иſ�ҳ��
	  public static final int TRAVEL_READY=3;// ��������׼��ҳ��
	  public static final int USEFUL_INFO=4;// ����ʵ����Ϣҳ��
	  public static final int CITY_TRAFFIC=5;// ������н�ͨҳ��
	  public static final int TRAVEL_NOTE=6;// �����μǹ���ҳ��
	  public static final int TRAVEL_COMMEND=7;// ������·�Ƽ�ҳ��

	/**
	 * @param taskID
	 * @param taskParam
	 */
	public Task(int taskID)
	{
		super();
		this.taskID = taskID;
	}
	public int getTaskID()
	{
		return taskID;
	}
	public void setTaskID(int taskID)
	{
		this.taskID = taskID;
	}
}
