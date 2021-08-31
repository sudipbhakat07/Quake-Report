package com.example.quakereport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static Context mContext;

    static ReportAdapter adapter;

    static Handler mainHandler = new Handler();
    static private TextView emptyTextView;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=150";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this.getApplicationContext();


        emptyTextView = (TextView) findViewById(R.id.emptyTextView);

        ListView listView = (ListView) findViewById(R.id.list);
//          ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_item ,R.id.list_item , reports);
        adapter = new ReportAdapter(this, R.layout.list_item, new ArrayList<Report>());
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyTextView);

        if(isConnected(this)) {
            Log.v("Internet --> ", " is connected");
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(new BackgroundThread());
        }
        else {
            Log.v("Internet --> ", " is not connected");
            emptyTextView.setText("No earthquakes found!");
            showDialogBox();
        }
    }


     static class BackgroundThread implements Runnable {
        public void run() {
//            mainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    showDialogBox();
//                }
//            });

            if (USGS_REQUEST_URL == null) {
                return;
            }
            ArrayList<Report> reports = Utils.fetchEarthquakeData(USGS_REQUEST_URL);

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    emptyTextView.setText("No earthquakes found!");
                    adapter.clear();

                    if (reports != null && !reports.isEmpty()) {
                        adapter.addAll(reports);
                    }
                }
            });
        }
    }
    private static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
    }

    private static void showDialogBox() {
        mContext = mContext.getApplicationContext();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(adapter.getContext());
        alertDialog.setMessage("Please connect to the internet to proceed further!")
                .setCancelable(false)
                .setPositiveButton("Connect",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent i2 = new Intent(android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                        i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i2);
                        ((MainActivity) mContext).finishActivity(1);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(1);
//                            Intent i1 = new Intent(mContext, MainActivity.class);
//                            i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            mContext.startActivity(i1);
//                            ((MainActivity) mContext).finishActivity(1);
                        }

                });
        AlertDialog alert = alertDialog.create();
            alert.show();
    }
}