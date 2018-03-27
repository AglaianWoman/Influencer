package com.socialbeat.influencer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;

public class CampAdapter extends ArrayAdapter<CampValues> {
	ArrayList<CampValues> campList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	public CampAdapter(Context context, int resource, ArrayList<CampValues> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		campList = objects;
	}
 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.campid = (TextView) v.findViewById(R.id.campid);
			holder.campName = (TextView) v.findViewById(R.id.campName);
			holder.campImg = (ImageView) v.findViewById(R.id.campImg);
			holder.campCat = (TextView) v.findViewById(R.id.campCat);
			holder.campShortNote = (TextView) v.findViewById(R.id.campShortNote);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		holder.campid.setText(campList.get(position).getCampid());
		holder.campName.setText(campList.get(position).getCampName());
		holder.campImg.setImageResource(R.mipmap.influencerlistimg);
		new DownloadImageTask(holder.campImg).execute(campList.get(position).getCampImg());
		holder.campCat.setText(campList.get(position).getCampCat());
		holder.campShortNote.setText(campList.get(position).getCampShortNote());
		return v;
	}

	static class ViewHolder {

		public TextView campid;
		public TextView campName;
		public ImageView campImg;
		public TextView campShortNote;
		public TextView campCat;


	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}