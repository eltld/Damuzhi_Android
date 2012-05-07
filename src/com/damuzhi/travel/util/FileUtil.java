package com.damuzhi.travel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.damuzhi.travel.model.constant.ConstantField;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class FileUtil
{
	private static final String TAG = "FileUtil";
	private List<String> lstFile = new ArrayList<String>();  //��� List
	private ArrayList<FileInputStream> fileInput = new ArrayList<FileInputStream>();
	
	public List<String> GetFiles(String Path, String Extension, boolean IsIterative)  //����Ŀ¼����չ�����Ƿ�������ļ���
	{
	    File[] files = new File(Path).listFiles(); 
	    for (int i = 0; i < files.length; i++)
	    {
	        File f = files[i];
	        if (f.isFile())
	        {
	            if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension))
	            {
	            	lstFile.add(f.getPath());
	            }  //�ж���չ�� 
	            if (!IsIterative)
	                break;
	        }
	        else if (f.isDirectory() && f.getPath().indexOf("/.") == -1)  //���Ե��ļ��������ļ�/�ļ��У�
	            {
	        		GetFiles(f.getPath(), Extension, IsIterative);
	            }
	    }
	    return lstFile;
	}	
	
	//��ȡplacedata�ļ�
	public List<String> GetFiles(String Path, String type,String Extension, boolean IsIterative)  //����Ŀ¼����չ�����Ƿ�������ļ���
	{
	    File[] files = new File(Path).listFiles(); 
	    for (int i = 0; i < files.length; i++)
	    {
	        File f = files[i];
	        if (f.isFile())
	        {
	        	String fileExtension = f.getPath().substring(f.getPath().length() - Extension.length());
	        	String fileType = f.getPath().substring(f.getPath().lastIndexOf("/")+1,f.getPath().lastIndexOf("."));
	            if (fileExtension.equals(Extension)&&fileType.contains(type))
	            {
	            	lstFile.add(f.getPath());
	            }  //�ж���չ�� 
	            if (!IsIterative)
	                break;
	        }
	        else if (f.isDirectory() && f.getPath().indexOf("/.") == -1)  //���Ե��ļ��������ļ�/�ļ��У�
	            {
	        		GetFiles(f.getPath(), Extension, IsIterative);
	            }
	    }
	    return lstFile;
	}
	
	//��ȡplacedata�ļ�����������
		public ArrayList<FileInputStream> getFileInputStreams(String Path, String type,String Extension, boolean IsIterative)  //����Ŀ¼����չ�����Ƿ�������ļ���
		{
			
		    File[] files = new File(Path).listFiles(); 
		    for (int i = 0; i < files.length; i++)
		    {
		        File f = files[i];
		        if (f.isFile())
		        {
		        	String fileExtension = f.getPath().substring(f.getPath().length() - Extension.length());
		        	String fileType = f.getPath().substring(f.getPath().lastIndexOf("/")+1,f.getPath().lastIndexOf("."));
		            if (fileExtension.equals(Extension)&&fileType.contains(type))
		            {
		            	//lstFile.add(f.getPath());
		            	FileInputStream fileInputStream;
						try
						{
							//Log.d(TAG, f.getPath());
							fileInputStream = new FileInputStream(new File(f.getPath()));
							fileInput.add(fileInputStream);
						} catch (FileNotFoundException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			
		            }   
		            if (!IsIterative)
		                break;
		        }
		        else if (f.isDirectory() && f.getPath().indexOf("/.") == -1)  //���Ե��ļ��������ļ�/�ļ��У�
		            {
		        	getFileInputStreams(f.getPath(),type, Extension, IsIterative);
		            }
		    }
		    return fileInput;
		}
	
	
		public static boolean checkFileIsExits(String fileName)
		{
			String filePath = ConstantField.SAVE_PATH+fileName;
			File file = new File(filePath);
			if(file.exists())
			{
				return true;
			}
			return false;
		}
		
	
     
	    public static int freeSpaceOnSd() { 
	    	StatFs stat = new StatFs(Environment.getExternalStorageDirectory() .getPath());
	        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / 1024; 
	        return (int) sdFreeMB; 
	    } 
}
