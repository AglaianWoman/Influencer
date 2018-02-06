package com.socialbeat.influencer;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

	public static void setStringPreference(String key, String value, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString(key, value).commit();
	}
	public static String getStringPreference(String key, String defaultvalue, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString(key, defaultvalue);
	}

	public static void setIntPreference(String key, int value, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putInt(key, value).commit();
	}
	public static int getIntPreference(String key, int defaultvalue, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getInt(key, defaultvalue);
	}

	public static void setBooleanPreference(String key, boolean value, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putBoolean(key, value).commit();
	}
	public static boolean getBooleanPreference(String key, boolean defaultvalue, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getBoolean(key, defaultvalue);
	}

	public static void setKey(String session, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("key", session).commit();
	}
	public static String getKey(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("key", "");
	}

	public static void saveSessionId(String session, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("session", session).commit();
	}
	public static String getSessionID(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("session", "");
	}
	public static void saveLogin(String login, Context context){
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("login", login).commit();
	}

	public static String getLogin(Context context){
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("login", "");
	}
	public static void setUserConnected(boolean connect, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putBoolean("connect", connect).commit();
	}
	public static boolean isUserConnected(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getBoolean("connect", false);
	}

	public static void setMenuScroll(boolean menu_scroll, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putBoolean("menu_scroll", menu_scroll).commit();
	}
	public static boolean isMenuScroll(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getBoolean("menu_scroll", false);
	}

	public static void setAcceptTerms(boolean accept, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putBoolean("accept", accept).commit();
	}
	public static boolean getAcceptTerms(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getBoolean("accept", false);
	}
	public static void setuserLatitude(String lati, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("lati", lati).commit();
	}
	public static String getuserLatitude(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("lati", "null");
	}
	public static void setuserLongitude(String longi, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("longi", longi).commit();
	}
	public static String getuserLongitude(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("longi", "null");
	}

	// User Register Details

	public static void setCustomer_id(String Customer_id, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Customer_id", Customer_id).commit();
	}
	public static String getCustomer_id(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Customer_id", "");
	}
	public static void setemail_id(String email_id, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("email_id", email_id).commit();
	}
	public static String getemail_id(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("email_id", "");
	}
	public static void setSession_id(String Session_id, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Session_id", Session_id).commit();
	}
	public static String getSession_id(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Session_id", "");
	}
	public static void setFirstname(String Firstname, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Firstname", Firstname).commit();
	}
	public static String getFirstname(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Firstname", "");
	}
	public static void setLastname(String Lastname, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Lastname", Lastname).commit();
	}
	public static String getLastname(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Lastname", "");
	}
	public static void setbirthdate(String birthdate, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("birthdate", birthdate).commit();
	}
	public static String getbirthdate(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("birthdate", "");
	}
	public static void setPhoto(String Photo, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("Photo", Context.MODE_PRIVATE);
		preferences.edit().putString("Photo", Photo).commit();
	}
	public static String getPhoto(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("Photo", Context.MODE_PRIVATE);
		return preferences.getString("Photo", "");
	}
	public static void setAddress(String Address, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("Address", Context.MODE_PRIVATE);
		preferences.edit().putString("Address", Address).commit();
	}
	public static String getAddress(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("Address", Context.MODE_PRIVATE);
		return preferences.getString("Address", "");
	}
	public static void setPostal_code(String Postal_code, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("Postal_code", Context.MODE_PRIVATE);
		preferences.edit().putString("Postal_code", Postal_code).commit();
	}
	public static String getPostal_code(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("Postal_code", Context.MODE_PRIVATE);
		return preferences.getString("Postal_code", "");
	}
	public static void setCity(String City, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("City", City).commit();
	}
	public static String getCity(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("City", "");
	}
	public static void setPhone(String Phone, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Phone", Phone).commit();
	}
	public static String getPhone(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Phone", "");
	}
	public static void setLast_connect(String Last_connect, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Last_connect", Last_connect).commit();
	}
	public static String getLast_connect(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Last_connect", "");
	}
	public static void setLast_purchase(String Last_purchase, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Last_purchase", Last_purchase).commit();
	}
	public static String getLast_purchase(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Last_purchase", "");
	}
	public static void setFacebook_connect(String Facebook_connect, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Facebook_connect", Facebook_connect).commit();
	}
	public static String getFacebook_connect(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Facebook_connect", "");
	}
	public static void setFb_uid(String Fb_uid, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Fb_uid", Fb_uid).commit();
	}
	public static String getFb_uid(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Fb_uid", "");
	}

	public static void setFb_username(String Fb_userName, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Fb_userName", Fb_userName).commit();
	}
	public static String getFb_username(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Fb_userName", "");
	}
	public static void setFb_useremail(String Fb_useremail, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Fb_useremail", Fb_useremail).commit();
	}
	public static String getFb_useremail(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Fb_useremail", "");
	}
	public static void setFb_usergender(String Fb_usergender, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Fb_usergender", Fb_usergender).commit();
	}
	public static String getFb_usergender(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Fb_usergender", "");
	}
	public static void setFb_userlastname(String Fb_userlastname, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Fb_userlastname", Fb_userlastname).commit();
	}
	public static String getFb_userlastname(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Fb_userlastname", "");
	}
	public static void setReceive_notification(String rec_nofi, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("rec_nofi", rec_nofi).commit();
	}
	public static String getReceive_notification(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("rec_nofi", "");
	}

	public static void setIs_already_registered_user(String fb_id, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("fb_id", fb_id).commit();
	}
	public static String getIs_already_registered_user(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("fb_id", "");
	}

	public static void setSearch(String Search, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Search", Search).commit();
	}
	public static String getSearch(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Search", "");
	}
	public static void setBooleanUpcoming(boolean connect, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putBoolean("Upcoming", connect).commit();
	}
	public static boolean getBooleanUpcoming(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getBoolean("Upcoming", false);
	}

	public static void setBooleanRefersh(boolean connect, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putBoolean("Refersh", connect).commit();
	}
	public static boolean getBooleanRefersh(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getBoolean("Refersh", false);
	}

	public static void setAPIRefersh(boolean connect, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putBoolean("Refersh", connect).commit();
	}
	public static boolean getAPIRefersh(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getBoolean("Refersh", false);
	}

	public static void setRefernce(String Search, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		preferences.edit().putString("Reference", Search).commit();
	}
	public static String getRefernce(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		return preferences.getString("Reference", "");
	}

	public static void setAppCount(int appCount, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		preferences.edit().putInt("appCount", appCount).commit();
	}

	public static int getAppCount(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		return preferences.getInt("appCount", 0);
	}

	public static void setBooleanNaver(boolean connect, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		preferences.edit().putBoolean("Refersh", connect).commit();
	}

	public static boolean getBooleanNaver(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		return preferences.getBoolean("Refersh", false);
	}

	public static void setFirstLaunched(String launch, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		preferences.edit().putString("launch", launch).commit();
	}

	public static String getFirstLaunched(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		return preferences.getString("launch", "");
	}

	public static void setPushCatID(String login, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		preferences.edit().putString("push_id", login).commit();
	}

	public static String getPushCatID(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		return preferences.getString("push_id", "");
	}


	public static void setAndroidDeviceId(String app, Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		preferences.edit().putString("device_id", app).commit();
	}

	public static String getAndroidDeviceId(Context context) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("settings",
				Context.MODE_PRIVATE);
		return preferences.getString("device_id", null);
	}
}
