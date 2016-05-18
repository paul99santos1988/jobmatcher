package com.emergencyapp.fragmentRespondent;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emergencyapp.MainActivity;
import com.emergencyapp.MapsActivity;
import com.emergencyapp.model.Coordinates;
import com.emergencyapp.userWithMap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sourcey.materiallogindemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class RequestFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    ArrayList<String> request;
    ListView listItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    public double lati;
    public double longi;
    TextView tvLong;
    TextView tvLat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_request, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        listItem = (ListView) rootView.findViewById(R.id.listView);
        tvLong =(TextView)rootView.findViewById(R.id.tvLong);
        tvLat=(TextView)rootView.findViewById(R.id.tvLat);
        FetchCordinates fetchCordinates = new FetchCordinates();
        fetchCordinates.execute();
        return rootView;
    }

    public void fetchRequest() {
        request = new ArrayList<String>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    //Log.d("", "Retrieved " + list.size() + " scores"); // Success!
                    for (int i = 0; i < list.size(); i++) {
                        request.add(list.get(i).get("name") + " Requested help.");
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }

                ArrayAdapter<String> allItemsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, request);
                listItem.setAdapter(allItemsAdapter);
                swipeRefreshLayout.setRefreshing(false);


            }
        });

    }

    public class FetchCordinates extends AsyncTask<String, Integer, String> {
        ProgressDialog progDailog = null;


        Coordinates coor = new Coordinates();
        public LocationManager mLocationManager;
        public VeggsterLocationListener mVeggsterLocationListener;

        @Override
        protected void onPreExecute() {
            mVeggsterLocationListener = new VeggsterLocationListener();
            mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    mVeggsterLocationListener);

            progDailog = new ProgressDialog(getActivity());
            progDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    FetchCordinates.this.cancel(true);
                }
            });
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(true);
            progDailog.setCancelable(true);
            progDailog.show();

        }

        @Override
        protected void onCancelled(){
            System.out.println("Cancelled by user!");

            mLocationManager.removeUpdates(mVeggsterLocationListener);
        }

        @Override
        protected void onPostExecute(String result) {
            progDailog.dismiss();

            Toast.makeText(getActivity(),
                    "LATITUDE :" + lati + " LONGITUDE :" + longi,
                    Toast.LENGTH_LONG).show();
            tvLat.setText(String.valueOf(lati) );
            tvLong.setText(String.valueOf(longi));


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (lati == 0.0) {

            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                int lat = (int) location.getLatitude(); // * 1E6);
                int log = (int) location.getLongitude(); // * 1E6);
                int acc = (int) (location.getAccuracy());

                String info = location.getProvider();
                try {

                    // LocatorService.myLatitude=location.getLatitude();

                    // LocatorService.myLongitude=location.getLongitude();

                    lati = location.getLatitude();
                    longi = location.getLongitude();

                    coor.setLongitude(longi);
                    coor.setLatitude(lati);

                } catch (Exception e) {
                    // progDailog.dismiss();
                    // Toast.makeText(getApplicationContext(),"Unable to get Location"
                    // , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("OnProviderDisabled", "OnProviderDisabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("onProviderEnabled", "onProviderEnabled");
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.i("onStatusChanged", "onStatusChanged");

            }

        }

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        fetchRequest();
                                    }
                                }
        );

        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Accept from " + request.get(position) + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(getContext(), MapsActivity.class);
                                startActivity(intent);

                                // FIRE ZE MISSILES!
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.show();
            }
        });

    }


    @Override
    public void onRefresh() {
        request.clear();
        fetchRequest();
    }
}
