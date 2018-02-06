package com.socialbeat.influencer;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.util.Log;


public class GCMIntentService extends IntentService {

	private int NOTIFCATION_ID = 0;
	Bundle bundle;
	public static final int NOTIFICATION_ID = 1;
	public static final String TAG = "GCMNotificationIntentService";
	private NotificationManager mNotificationManager;
	public GCMIntentService() {
		super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
			mBuilder.setSmallIcon(R.mipmap.logo);
			mBuilder.setContentTitle(getString(R.string.app_name));
			mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
			mBuilder.setAutoCancel(true);
			mBuilder.setVibrate(new long[] { 100, 100, 100, 100, 100 });
			mBuilder.setLights(Color.WHITE, 3000, 3000);
			Uri notificationsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			mBuilder.setSound(notificationsound);

			Log.d("RESPONCE", intent.getExtras().toString());
			String message = "";
			String id = "";
			try {
				if (message!=null) {
					message = intent.getExtras().getString("message");
					if(message.length() == 0)
						message = "";
					id = intent.getExtras().getString("id");
					if(id.length()>0){
						Log.d("PUSH ID==============",""+id);
						PreferenceManager.setPushCatID(id, this);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("===="+intent.getExtras().getString("message"));
			BigTextStyle bigStyle = new BigTextStyle();
			bigStyle.bigText(message);
			mBuilder.setContentText(message);
			mBuilder.setStyle(bigStyle);

			Intent newsIntent = new Intent(this, SplashScreenActivity.class);
			PendingIntent notificationIntent = PendingIntent.getActivity(this, 0,newsIntent,PendingIntent.FLAG_UPDATE_CURRENT);

			mBuilder.setContentIntent(notificationIntent);

			Notification notification = mBuilder.build();
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.defaults |= Notification.DEFAULT_LIGHTS;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;

			if (message!=null) {
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				NOTIFCATION_ID++;
				mNotificationManager.notify(""+System.currentTimeMillis(), NOTIFCATION_ID, mBuilder.build());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}

