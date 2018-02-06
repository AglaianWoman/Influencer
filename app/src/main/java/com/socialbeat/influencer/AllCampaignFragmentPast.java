package com.socialbeat.influencer;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AllCampaignFragmentPast extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = AllCampaignFragmentPast.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pDialog;
    private List<CampValues> campValuesList = new ArrayList<CampValues>();
    private ListView listView;
    private CustomListAdapter adapter;
    String cid,url,valueofcid;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.alllivecamp, container, false);
        context = v.getContext();
        cd = new ConnectionDetector(getActivity());
        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        SharedPreferences prfs = this.getActivity().getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        Log.v("Cid Value : ",cid);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (isInternetPresent) {

            listView = (ListView) v.findViewById(R.id.overalllist);
            adapter = new CustomListAdapter(getActivity(), campValuesList);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                    String campImg = campValuesList.get(position).getCampImg();
                    String campName = campValuesList.get(position).getCampName();
                    String campShortNote = campValuesList.get(position).getCampShortNote();
                    String campCat = campValuesList.get(position).getCampCat();
                    String campLongNote = campValuesList.get(position).getCampLongNote();
                    String campGoal = campValuesList.get(position).getCampGoal();
                    String campDos = campValuesList.get(position).getCampDos();
                    String campDont = campValuesList.get(position).getCampDont();
                    String campBacklink = campValuesList.get(position).getCampBacklink();
                    String campTag= campValuesList.get(position).getCampTag();
                    String campid = campValuesList.get(position).getCampid();
                    String campApplyTill = campValuesList.get(position).getCampApplyTill();
                    String campRewards = campValuesList.get(position).getCampRewards();
                    String campRewardType = campValuesList.get(position).getCampRewardType();
                    String fixedamount= campValuesList.get(position).getFixedamount();

                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(), AllCampDetailsPast.class);
                    Bundle bund = new Bundle();
                    //Inserts a String value into the mapping of this Bundle
                    bund.putString("campImg", campImg);
                    bund.putString("campName", campName);
                    bund.putString("campShortNote", campShortNote);
                    bund.putString("campCat", campCat);
                    bund.putString("campLongNote", campLongNote);
                    bund.putString("campGoal", campGoal);
                    bund.putString("campDos", campDos);
                    bund.putString("campDont", campDont);
                    bund.putString("campBacklink", campBacklink);
                    bund.putString("campTag", campTag);
                    bund.putString("campid", campid);
                    bund.putString("campApplyTill", campApplyTill);
                    bund.putString("campRewards", campRewards);
                    bund.putString("campRewardType", campRewardType);
                    bund.putString("fixedamount", fixedamount);
                    //Add the bundle to the intent.
                    intent.putExtras(bund);
                    //start the DisplayActivity
                    startActivity(intent);
                    // Toast.makeText(getActivity(),campImg+"_____________"+campBacklink, Toast.LENGTH_LONG).show();
                }
            });

            CampaignsFunction();

        }else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(Color.RED);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
        return v;
    }

    private void CampaignsFunction() {

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String LIVE_CAMP_URL = getResources().getString(R.string.base_url)+getResources().getString(R.string.past_campaign_url);
        JsonArrayRequest campobj = new JsonArrayRequest(LIVE_CAMP_URL,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                hidePDialog();

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        CampValues campvalue = new CampValues();
                        campvalue.setCampImg(obj.getString("campImg"));
                        campvalue.setCampName(obj.getString("campName"));
                        campvalue.setCampShortNote(obj.getString("campShortNote"));
                        campvalue.setCampCat(obj.getString("campCat"));
                        campvalue.setCampLongNote(obj.getString("campLongNote"));
                        campvalue.setCampGoal(obj.getString("campGoal"));
                        campvalue.setCampDos(obj.getString("campDos"));
                        campvalue.setCampDont(obj.getString("campDont"));
                        campvalue.setCampBacklink(obj.getString("campBacklink"));
                        campvalue.setCampTag(obj.getString("campTag"));
                        campvalue.setCampid(obj.getString("campid"));
                        campvalue.setCampApplyTill(obj.getString("campApplyTill"));
                        campvalue.setCampRewards(obj.getString("campRewards"));
                        campvalue.setCampRewardType(obj.getString("campRewardType"));
                        campvalue.setFixedamount(obj.getString("fixedamount"));
                        // adding contentofCampaigns to movies array
                        campValuesList.add(campvalue);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        MyApplication.getInstance().trackException(e);
                        Log.e(TAG, "Exception: " + e.getMessage());
                    }
                }
                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(campobj);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Past Campaigns List Screen");
    }
    public static AllCampaignFragmentPast newInstance() {
        return (new AllCampaignFragmentPast());
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        AllCampaignFragmentPast fragment = new AllCampaignFragmentPast();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        swipeRefreshLayout.setRefreshing(false);
    }
}