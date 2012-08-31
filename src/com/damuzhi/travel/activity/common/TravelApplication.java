package com.damuzhi.travel.activity.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.damuzhi.travel.R;
import com.damuzhi.travel.model.constant.ConstantField;
import com.damuzhi.travel.network.HttpTool;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.URLConnectionImageDownloader;

public class TravelApplication extends Application
{
	private static final String TAG = "TravelApplication";
	private DefaultHttpClient defaultHttpClient;
	private static HashMap<String, Double> location = new HashMap<String, Double>();	
	private static TravelApplication travelApplication;
	private List<Activity> activityList = new LinkedList<Activity>();
	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public String address = "";
	public BDLocation bdLocation;
	public Map<String, Integer> downloadStatusMap = new HashMap<String, Integer>();
	public String deviceId;
	public   Map<Integer, Integer> installCityData;
	public  Map<Integer, String> newVersionCityData;
	//public ImageLoader imageLoader;
	public static TravelApplication getInstance()
	{
		return travelApplication;
	}
	
	
	
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		travelApplication = this;
		//defaultHttpClient = createHttpClient();
		mLocationClient = new LocationClient( this );
		mLocationClient.registerLocationListener( myListener );
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = tm.getDeviceId();
		
		
		/*ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.threadPoolSize(3)
		.threadPriority(Thread.NORM_PRIORITY - 2)
        .denyCacheImageMultipleSizesInMemory()
        .offOutOfMemoryHandling()
        .memoryCacheSize(3*1024*1024)
        .memoryCache(new WeakMemoryCache()) 
        .discCache(new UnlimitedDiscCache(new File(ConstantField.IMAGE_CACHE_PATH)))
        .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
        .imageDownloader(new URLConnectionImageDownloader(5 * 1000, 20 * 1000)) // connectTimeout (5 s), readTimeout (20 s)
        .enableLogging()
        .build();
		 ImageLoader.getInstance().init(config);

		 imageLoader = ImageLoader.getInstance();*/
	}
	
	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
		shutdownHttpClient();
	}


	@Override
	public void onTerminate()
	{
		super.onTerminate();
		shutdownHttpClient();
	}
	
	
	
	private void shutdownHttpClient()
	{
		if(defaultHttpClient !=null && defaultHttpClient.getConnectionManager() !=null)
		{
			defaultHttpClient.getConnectionManager().shutdown();
		}
	}
	 
    public void addActivity(Activity activity){  
        activityList.add(activity);  
    }  
        
    public void exit(){  
        for(Activity activity:activityList){  
            activity.finish();  
        }  
        if(mLocationClient !=null)
		{
        	mLocationClient.stop();
		}
        System.exit(0);  
    }  

	
	public HashMap<String, Double> getLocation()
	{
		return location;
	}

	public void setLocation(HashMap<String, Double> location)
	{
		this.location = location;
	}
	
	public  boolean checkNetworkConnection()
	{
		int size = activityList.size();
		Activity activity = activityList.get(size-1);
		return HttpTool.checkNetworkConnection(activity);
	}
	
	public void makeToast()
	{
		Thread thread = new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				 
					 Looper.prepare();
					 Toast.makeText(getApplicationContext(), travelApplication.getString(R.string.conn_fail_exception), Toast.LENGTH_SHORT).show();
					 Looper.loop();
				 				
			}
		});
		thread.start();
		
	}
	
	
	public void downloadFailToast()
	{
		Thread thread = new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				 
					 Looper.prepare();
					 Toast.makeText(getApplicationContext(), travelApplication.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
					 Looper.loop();
				 			
			}
		});
		thread.start();
		
	}
	
	
	public void notEnoughMemoryToast()
	{
		Thread thread = new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				
					 Looper.prepare();
					 Toast.makeText(getApplicationContext(), travelApplication.getString(R.string.not_enough_memory), Toast.LENGTH_LONG).show();
					 Looper.loop();
				 				
			}
		});
		thread.start();
		
	}
	
	
	public void getSDcardFailToast()
	{
		Thread thread = new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				
					 Looper.prepare();
					 Toast.makeText(getApplicationContext(), travelApplication.getString(R.string.get_sdcard_fail), Toast.LENGTH_LONG).show();
					 Looper.loop();
				 			
			}
		});
		thread.start();
		
	}
	
	/*baidu location */
	
	
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			bdLocation = location;
			address = location.getAddrStr();	
			/*if(address !=null)
			{
				getCoordinate(address);
			}else
			{
				Double latitude = location.getLatitude()-0.0060;
				Double longitude = location.getLongitude()-0.0065;
				initLocation(latitude, longitude);
			}
			*/
			Double latitude = location.getLatitude()-0.0060;
			Double longitude = location.getLongitude()-0.0065;
			initLocation(latitude, longitude);
		}
		
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	
	
	
	
	public  HashMap<String, Double> initLocation(Double latitude,Double longitude)
	{
		 location.put(ConstantField.LATITUDE, latitude);
		 location.put(ConstantField.LONGITUDE, longitude);
		 return location;
	}
		
	private String getLocationAddress(double latitude,double longitude) {
		String resultString = "";
		String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", latitude, longitude);
		Log.i("URL", urlString);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(urlString);
		try {
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
		StringBuffer strBuff = new StringBuffer();
		String result = null;
		while ((result = buffReader.readLine()) != null) {
			strBuff.append(result);
		}
		resultString = strBuff.toString();
		Log.d(TAG, "google address = "+resultString);
		if (resultString != null && resultString.length() > 0) {
			JSONObject jsonobject = new JSONObject(resultString);
			JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
			resultString = "";
			for (int i = 0; i < jsonArray.length(); i++) {
				resultString = jsonArray.getJSONObject(i).getString("address");
			}
		}
		} catch (Exception e) {
		} finally {
		get.abort();
		client = null;
		}
		
		return resultString;
		}
	
	
	 public  void getCoordinate(String addr)  
	 {  
	     String address = null;  
	     try{
			address = java.net.URLEncoder.encode(addr,"UTF-8");  
	        String output = "csv";  
	        String key = "abc";  
	        String url = String.format("http://maps.google.com/maps/geo?q=%s&output=%s&key=%s", address, output, key);  
	        URL myURL = null;  
	        URLConnection httpsConn = null;  
	        myURL = new URL(url);  
	        httpsConn = (URLConnection) myURL.openConnection();  
	        if (httpsConn != null) {  
	        	InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");  
	        	BufferedReader br = new BufferedReader(insr);  
	        	String data = null;  
	        	if ((data = br.readLine()) != null) {   
	        		String[] retList = data.split(",");  	      
	        		double latitude = Double.parseDouble(retList[2]); 
	        		double longitude = Double.parseDouble(retList[3]); 
	        		initLocation(latitude, longitude);
	        		}  
	        	insr.close();  
	        	}  
	     	} catch (Exception e) {  
	     		Log.e(TAG, "<getCoordinate> but catch exception = "+e.toString(),e);
	     	}        
	 }  
	 
	 
	 public String getDeviceId()
	{
		return deviceId;
	}




	public Map<Integer, Integer> getInstallCityData()
	{
		return installCityData;
	}




	public Map<Integer, String> getNewVersionCityData()
	{
		return newVersionCityData;
	}




	public void setInstallCityData(Map<Integer, Integer> installCityData)
	{
		this.installCityData = installCityData;
	}




	public void setNewVersionCityData(Map<Integer, String> newVersionCityData)
	{
		this.newVersionCityData = newVersionCityData;
	}
	 
	
}
