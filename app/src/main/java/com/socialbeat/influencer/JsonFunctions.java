package com.socialbeat.influencer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class JsonFunctions {
	private static final int TIMEOUT = 100000;
	// the timeout until a connection is established
	private static final int CONNECTION_TIMEOUT = 100000; 
	// the timeout for waiting for data
	private static final int SOCKET_TIMEOUT = 100000;
	// from ClientConnectionRequest
	private static final long MCC_TIMEOUT = 100000; 
	private Context context;
	//private ResponseCode err = ResponseCode.OK;
	//String AUTH_USERNAME="chronoscene", AUTH_PASSWORD="9yq4cw31x32z15j";
	public JsonFunctions(Context context) {
		this.context = context;
	}
	
	public final String callWebService(String url){
		InputStream is = null;
		String result = "";
		//JSONObject jArray = null;	
		if(isConnected()) {
			DefaultHttpClient client;
		      //prepare for the https connection
		      //call this in the constructor of the class that does the connection if
		      //it's used multiple times
			 KeyStore trustStore;
			 SSLSocketFactory sf = null;
			try {
				trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				sf = new CustomSSLSocketFactory(trustStore);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        // Setting up parameters
	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, "utf-8");
	        params.setBooleanParameter("http.protocol.expect-continue", false);
	        // Setting timeout
	        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
	        HttpConnectionParams.setSoTimeout(params, TIMEOUT);
	        // Registering schemes for both HTTP and HTTPS
	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));
	        // Creating thread safe client connection manager
	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
	        // Creating HTTP client
	        client = new DefaultHttpClient(ccm, params);
	        // Registering user name and password for authentication
//	        client.getCredentialsProvider().setCredentials(
//	                new AuthScope(null, -1),
	               // new UsernamePasswordCredentials(APIHeader.AUTH_USERNAME, APIHeader.AUTH_PASSWORD));
			//http post
		    try{	
		    	url = convertSpaceToEncode(url);
		    	Log.d("Web URL","==="+url);
		    	HttpGet get = new HttpGet(url);
		    	//get.addHeader(APIHeader.INPUT_KEY, APIHeader.INPUT_VALUE);
		    	setTimeouts(get.getParams());
		    	HttpResponse response = client.execute(get);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
		    }catch(Exception e){
		     	Log.e("log_tag", "Error in http connection "+e.toString());
		    }
		  //convert response to string
		    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	            	sb.append(line + "\n");
	            }
	            is.close();
	            result=sb.toString();
	            Log.d("Web Responce", "==="+result);
		    }catch(Exception e){
		    	Log.e("log_tag", "Error converting result "+e.toString());
		    }
		} 
	    return result;
	}
	public final String getJsonFromUrl(String url){
		InputStream is = null;
		String result = "";
		//JSONObject jArray = null;	
		if(isConnected()) {
			DefaultHttpClient client;
		      //prepare for the https connection
		      //call this in the constructor of the class that does the connection if
		      //it's used multiple times
			 KeyStore trustStore;
			 SSLSocketFactory sf = null;
			try {
				trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				sf = new CustomSSLSocketFactory(trustStore);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        // Setting up parameters
	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, "utf-8");
	        params.setBooleanParameter("http.protocol.expect-continue", false);
	        // Setting timeout
	        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
	        HttpConnectionParams.setSoTimeout(params, TIMEOUT);
	        // Registering schemes for both HTTP and HTTPS
	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));
	        // Creating thread safe client connection manager
	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
	        // Creating HTTP client
	        client = new DefaultHttpClient(ccm, params);
	        // Registering user name and password for authentication
//	        client.getCredentialsProvider().setCredentials(
//	                new AuthScope(null, -1),
	               // new UsernamePasswordCredentials(APIHeader.AUTH_USERNAME, APIHeader.AUTH_PASSWORD));
			//http post
		    try{	
		    	url = convertSpaceToEncode(url);
		    	Log.d("Web URL","==="+url);
		    	HttpPost get = new HttpPost(url);
		    	//get.addHeader(APIHeader.INPUT_KEY, APIHeader.INPUT_VALUE);
//		    	get.addHeader("API_KEY",context.getResources().getString(R.string.header_api_key));
		    	/*get.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
		    	get.setHeader("Cookie", "API_KEY="+context.getResources().getString(R.string.header_api_key));*/
		    	setTimeouts(get.getParams());
		    	HttpResponse response = client.execute(get);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
		    }catch(Exception e){
		     	Log.e("log_tag", "Error in http connection "+e.toString());
		    }
		  //convert response to string
		    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	            	sb.append(line + "\n");
	            }
	            is.close();
	            result=sb.toString();
	            Log.d("Web Responce", "==="+result);
		    }catch(Exception e){
		    	Log.e("log_tag", "Error converting result "+e.toString());
		    }
		} 
	    return result;
	}
	public String postFileWebservice(String url, HashMap<String, String> val, File file) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);			
			Log.d("POST VALUES", "===="+val.toString());
			Log.d("POST URL", "===="+url);			
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);			
			entity.addPart("photo", new FileBody(file));
			Iterator myVeryOwnIterator = val.keySet().iterator();
	    	while(myVeryOwnIterator.hasNext()) {
	    	    String key=(String)myVeryOwnIterator.next();
	    	    String value=(String)val.get(key);
	    	    entity.addPart(key, new StringBody(value));
	    	}	
	    	Log.d("ContentLength=========",""+entity.getContentLength());
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String sResponse = reader.readLine();
			Log.d("Responce", "===="+sResponse);
			return sResponse;
		} catch (Exception e) {
			Log.e(e.getClass().getName(), e.getMessage(), e);
			return "";
		}
	}
	public String postToUrl(String url, HashMap<String, String> val){
		Log.d("POST VALUES", "==="+val.toString());
		InputStream is = null;
		String result = "";		
		DefaultHttpClient client;
	
		KeyStore trustStore;
		SSLSocketFactory sf = null;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			sf = new CustomSSLSocketFactory(trustStore);
		} catch (Exception e2) {
			e2.printStackTrace();
			result="failed";
		}
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        // Setting up parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
        params.setBooleanParameter("http.protocol.expect-continue", false);
        // Setting timeout
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT);
        // Registering schemes for both HTTP and HTTPS
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));
        // Creating thread safe client connection manager
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        // Creating HTTP client
        client = new DefaultHttpClient(ccm, params);
        // Registering user name and password for authentication
//        client.getCredentialsProvider().setCredentials(
//                new AuthScope(null, -1),
              //  new UsernamePasswordCredentials(APIHeader.AUTH_USERNAME, APIHeader.AUTH_PASSWORD));
		//http post
	    try{	
	    	url = convertSpaceToEncode(url);
	    	Log.d("Web URL","==="+url);
	    	HttpPost post = new HttpPost(url);       		
	    	//post.addHeader(APIHeader.INPUT_KEY, APIHeader.INPUT_VALUE);
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    	Iterator myVeryOwnIterator = val.keySet().iterator();
	    	while(myVeryOwnIterator.hasNext()) {
	    	    String key=(String)myVeryOwnIterator.next();
	    	    String value=(String)val.get(key);
	    	    Log.i(""+key, ""+value);
	    	    nameValuePairs.add(new BasicNameValuePair(""+key, ""+value));
	    	}	        
	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
	    	setTimeouts(post.getParams());
	    	HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
	    }catch(Exception e){
	    	result="failed";
	    	e.printStackTrace();
	     	Log.e("log_tag", "Error in http connection "+e.toString());
	    }
	  //convert response to string
	    try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
            	sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
            Log.d("Web Responce", "==="+result);
	    }catch(Exception e){
	    	result="failed";
	    	Log.e("log_tag", "Error converting result "+e.toString());
	    }
	    return result;
	}
	public String callHttpDelete(String url, HashMap<String, String> val){
		InputStream is = null;
		String result = "";		
		DefaultHttpClient client;
		Log.d("POST VALUES", "==="+val.toString());
		KeyStore trustStore;
		SSLSocketFactory sf = null;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			sf = new CustomSSLSocketFactory(trustStore);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        // Setting up parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
        params.setBooleanParameter("http.protocol.expect-continue", false);
        // Setting timeout
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT);
        // Registering schemes for both HTTP and HTTPS
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));
        // Creating thread safe client connection manager
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        // Creating HTTP client
        client = new DefaultHttpClient(ccm, params);
        // Registering user name and password for authentication
//        client.getCredentialsProvider().setCredentials(
//                new AuthScope(null, -1),
              //  new UsernamePasswordCredentials(APIHeader.AUTH_USERNAME, APIHeader.AUTH_PASSWORD));
		//http post
	    try{	
	    	url = convertSpaceToEncode(url);
	    	Log.d("Web URL","==="+url);
	    	HttpDelete post = new HttpDelete(url);       		
	    	//post.addHeader(APIHeader.INPUT_KEY, APIHeader.INPUT_VALUE);
	    	setTimeouts(post.getParams());
	    	HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
	    }catch(Exception e){
	    	e.printStackTrace();
	     	Log.e("log_tag", "Error in http connection "+e.toString());
	    }
	  //convert response to string
	    try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
            	sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();
            Log.d("Web Responce", "==="+result);
	    }catch(Exception e){
	    	Log.e("log_tag", "Error converting result "+e.toString());
	    }
	    return result;
	}
	public String callHttpPutTypeJson(String url,HashMap<String, String> val){ 
		InputStream is = null;
		String result = "";		
		DefaultHttpClient client;
		int code=0;
		Log.d("Put VALUES", "==="+val.toString());
		KeyStore trustStore;
		SSLSocketFactory sf = null;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			sf = new CustomSSLSocketFactory(trustStore);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        // Setting up parameters
        HttpParams params = new BasicHttpParams();       
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
        params.setBooleanParameter("http.protocol.expect-continue", false);
        // Setting timeout
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT);
        // Registering schemes for both HTTP and HTTPS
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));
        // Creating thread safe client connection manager
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        // Creating HTTP client
        client = new DefaultHttpClient(ccm, params);
        // Registering user name and password for authentication
        client.getCredentialsProvider().setCredentials(
                new AuthScope(null, -1),
                new UsernamePasswordCredentials("", ""));	       		
		//http post
	    try{	
	    	url = convertSpaceToEncode(url);
	    	Log.d("Web URL","==="+url);
	    	HttpPut put = new HttpPut(url);
	    	//put.addHeader(APIHeader.INPUT_KEY, APIHeader.INPUT_VALUE);
//	    	post.setHeader("Content-Type", "application/x-www-form-urlencoded");  
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    	Iterator myVeryOwnIterator = val.keySet().iterator();
	    	while(myVeryOwnIterator.hasNext()) {
	    	    String key=(String)myVeryOwnIterator.next();
	    	    String value=(String)val.get(key);
	    	    Log.i(""+key, ""+value);
//	    	    Log.d("KEY","==="+key);
//	    	    Log.d("VALUE","==="+value);
	    	    nameValuePairs.add(new BasicNameValuePair(key, value));
	    	}	        
	    	put.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
	    	setTimeouts(put.getParams());
	    	HttpResponse response = client.execute(put);
	    	StatusLine statusLine = response.getStatusLine();
	    	code = statusLine.getStatusCode();
	    	Log.e("log_tag", "Reponse Code "+Integer.toString(code));
            HttpEntity entity = response.getEntity();
            Log.d("Web Responce==> 13", "==="+result);
            is = entity.getContent();
	    }catch(Exception e){
	     	Log.e("log_tag", "Error in http connection "+e.toString());
	    }
	  //convert response to string
	    try{
	    	if(code==200){
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	            	sb.append(line + "\n");
	            }
	            is.close();
	            result=sb.toString();
	    	}else{
	    		result="error";
	    	}
            Log.d("Web Responce==> 14", "==="+result);
	    }catch(Exception e){
	    	Log.e("log_tag", "Error converting result "+e.toString());
	    }
	    return result;
	}
	public String uploadFileUsingHttpPost(String url,File myFile){
		String resStr = "";
    	try {	
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setHttpElementCharset(params, HTTP.UTF_8);
            // Setting timeout
            HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, TIMEOUT);
			DefaultHttpClient httpClient = new DefaultHttpClient(params);        			
            HttpPost postRequest = new HttpPost(url);
//            postRequest.addHeader(BasicScheme.authenticate(
//           		 new UsernamePasswordCredentials(APIHeader.AUTH_USERNAME, APIHeader.AUTH_PASSWORD),
//           		 "UTF-8", false));
//            postRequest.addHeader(APIHeader.INPUT_KEY, APIHeader.INPUT_VALUE);
            System.out.println("URL:"+url);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            if(myFile != null && myFile.isFile()){
            	FileBody filebodyPhoto = new FileBody(myFile);
            	reqEntity.addPart("photo", filebodyPhoto);
            	Log.d("ContentLength==============",""+reqEntity.getContentLength());
            	//as a custom file name
            	try {
            		InputStream is = null;
            		ByteArrayBody bab;
                    ByteArrayOutputStream bos;
					if(myFile.isFile()){
						is = new BufferedInputStream(new FileInputStream(myFile));
						bos = new ByteArrayOutputStream();
		            	while (is.available() > 0) {
		            		bos.write(is.read());
		            	}
		            	Log.d("ContentLength==============",""+reqEntity.getContentLength());
		            	byte[] data = bos.toByteArray();
		                bab = new ByteArrayBody(data, String.valueOf(System.currentTimeMillis()) +".jpg");
		                reqEntity.addPart("photo", bab);
		                if(is != null){
		                	 is.close();
		                }
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
            reqEntity.addPart("session", new StringBody(PreferenceManager.getSession_id(context)));
            postRequest.setEntity(reqEntity);                           
            HttpResponse response = httpClient.execute(postRequest);                           
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            resStr = s.toString();
            Log.i("Response",resStr);
    	} catch (Exception e) {
    		//err = ResponseCode.ERR_CONNEXION;
			e.printStackTrace();
		}
    	return resStr;
    }
	private static void setTimeouts(HttpParams params) {
	    params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
	    params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
	    params.setLongParameter(ConnManagerPNames.TIMEOUT, MCC_TIMEOUT);
	}
	private class CustomSSLSocketFactory extends SSLSocketFactory {
	    public SSLContext sslContext = SSLContext.getInstance("TLS");
	    public CustomSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
	        super(truststore);
	        TrustManager tm = new X509TrustManager() {
	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	        }
	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }
	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        };
	        sslContext.init(null, new TrustManager[] {tm}, null);
	    }
	    @Override
	    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
	        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	    }
	    @Override
	    public Socket createSocket() throws IOException {
	        return sslContext.getSocketFactory().createSocket();
	    }
	}
	public String convertSpaceToEncode(String str) {
	    String url = null;
	    try{
	    url = new String(str.trim().replace(" ", "%20").replace("<", "%3C").replace(">", "%3E"));
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	    return url;
	}
	/*** Check the Internet connectivity ***/
    public boolean isConnected() {		
    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo[] info = cm.getAllNetworkInfo();
    	if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiNetwork != null && wifiNetwork.isConnected()) {
	      return true;
	    }
	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (mobileNetwork != null && mobileNetwork.isConnected()) {
	      return true;
	    }
	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	    if (activeNetwork != null && activeNetwork.isConnected()) {
	      return true;
	    }
	    return false;
	}
}
