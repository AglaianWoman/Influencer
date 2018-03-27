package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<CampValues> campValuesItem;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

	CustomListAdapter(Activity activity, List<CampValues> campValuesItem) {
		this.activity = activity;
		this.campValuesItem = campValuesItem;
	}

	@Override
	public int getCount() {
		return campValuesItem.size();
	}

	@Override
	public Object getItem(int location) {
		return campValuesItem.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			assert inflater != null;
			convertView = inflater.inflate(R.layout.alllivecamplist, null);
		}

		if (imageLoader == null)
			imageLoader = MyApplication.getInstance().getImageLoader();
		TextView campid = (TextView) convertView.findViewById(R.id.campid);
		TextView campName = (TextView) convertView.findViewById(R.id.campName);
		NetworkImageView campImg = (NetworkImageView) convertView.findViewById(R.id.campImg);
		TextView campCat = (TextView) convertView.findViewById(R.id.campCat);
		TextView campShortNote = (TextView) convertView.findViewById(R.id.campShortNote);

		// getting campaign data for the row
		CampValues cv = campValuesItem.get(position);
		//normal values
		campShortNote.setText(cv.getCampShortNote());
		campCat.setText(cv.getCampCat());
		campid.setText(cv.getCampid());
		campName.setText(cv.getCampName());
		// thumbnail image
		campImg.setImageUrl(cv.getCampImg(), imageLoader);
		campCat.setText(cv.getCampCat());
		campShortNote.setText(cv.getCampShortNote());
		return convertView;
	}

}