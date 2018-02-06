package com.socialbeat.influencer;

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
	ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<CampValues> campValuesItem) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = inflater.inflate(R.layout.alllivecamplist, null);

		if (imageLoader == null)
			imageLoader = MyApplication.getInstance().getImageLoader();
		NetworkImageView campImg = (NetworkImageView) convertView.findViewById(R.id.campImg);
		TextView campName = (TextView) convertView.findViewById(R.id.campName);
		TextView campShortNote = (TextView) convertView.findViewById(R.id.campShortNote);
		TextView campCat = (TextView) convertView.findViewById(R.id.campCat);
		TextView campLongNote = (TextView) convertView.findViewById(R.id.campLongNote);
		TextView campGoal = (TextView) convertView.findViewById(R.id.campGoal);
		TextView campDos = (TextView) convertView.findViewById(R.id.campDos);
		TextView campDont = (TextView) convertView.findViewById(R.id.campDont);
		TextView campBacklink = (TextView) convertView.findViewById(R.id.campBacklink);
		TextView campTag = (TextView) convertView.findViewById(R.id.campTag);
		TextView campid = (TextView) convertView.findViewById(R.id.campid);
		TextView campApplyTill = (TextView) convertView.findViewById(R.id.campApplyTill);
		TextView campRewards = (TextView) convertView.findViewById(R.id.campRewards);
		TextView campRewardType = (TextView) convertView.findViewById(R.id.campRewardType);
		TextView fixedamount = (TextView) convertView.findViewById(R.id.fixedamount);

		// getting campaign data for the row
		CampValues cv = campValuesItem.get(position);
		// thumbnail image
		campImg.setImageUrl(cv.getCampImg(), imageLoader);
		//normal values
		campName.setText(cv.getCampName());
		campShortNote.setText(cv.getCampShortNote());
		campCat.setText(cv.getCampCat());
		campLongNote.setText(cv.getCampLongNote());
		campGoal.setText(cv.getCampGoal());
		campDos.setText(cv.getCampDos());
		campDont.setText(cv.getCampDont());
		campBacklink.setText(cv.getCampBacklink());
		campTag.setText(cv.getCampTag());
		campid.setText(cv.getCampid());
		campApplyTill.setText(cv.getCampApplyTill());
		campRewards.setText(cv.getCampRewards());
		campRewardType.setText(cv.getCampRewardType());
		fixedamount.setText(cv.getFixedamount());
		return convertView;
	}

}