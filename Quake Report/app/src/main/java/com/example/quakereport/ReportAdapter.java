package com.example.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportAdapter extends ArrayAdapter {

    private static final String seperator = "of";


    public ReportAdapter(@NonNull Context context, int resource, @NonNull List objects) {
//        super(context, R.layout.support_simple_spinner_dropdown_item, objects);   (Resource id is not valid and so we can give any value to it)
        super(context, 0, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null) {
            Log.v("itemView-------------->", "itemView is null");
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        } else {
            Log.v("itemView------(NOT)--->", "itemView is not null");
        }

        Report report = (Report) getItem(position);

        TextView magnitudeView = (TextView) itemView.findViewById(R.id.magnitudeView);
        DecimalFormat decimalFormatter = new DecimalFormat("0.0");
        magnitudeView.setText(decimalFormatter.format(report.getMagnitude()));

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magColor = getMagColor(report.getMagnitude());
        magnitudeCircle.setColor(magColor);

        String place = report.getPlace();
        String location1;
        String location2;
        String[] parts = place.split(seperator);

        if (place.contains(seperator)) {
            location1 = parts[0] + seperator;
            location2 = parts[1];
        } else {
            location1 = "Near the ";
            location2 = place;
        }

        TextView place1 = (TextView) itemView.findViewById(R.id.placeView1);
        place1.setText(location1);

        TextView place2 = (TextView) itemView.findViewById(R.id.placeView2);
        place2.setText(location2);

        TextView dateView = (TextView) itemView.findViewById(R.id.dateView);
        //Date object to be used for date and time.
        Date dateObj = new Date(report.getDate());

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd , yyyy");
        String date = dateFormatter.format(dateObj);
        dateView.setText(date);

        TextView timeView = (TextView) itemView.findViewById(R.id.textView);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:m a");
        String time = timeFormatter.format(dateObj);
        timeView.setText(time);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(report.getUrl());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                if (i.resolveActivity(getContext().getPackageManager()) != null)
                    getContext().startActivity(i);
            }
        });

        return itemView;
    }

    private int getMagColor(Double magnitude) {

        int mag = (int) Math.floor(magnitude);
        int magnitudeColor;
        switch (mag) {
            case 0:
            case 1:
                magnitudeColor = R.color.magnitude1;
                break;
            case 2:
                magnitudeColor = R.color.magnitude2;
                break;
            case 3:
                magnitudeColor = R.color.magnitude3;
                break;
            case 4:
                magnitudeColor = R.color.magnitude4;
                break;
            case 5:
                magnitudeColor = R.color.magnitude5;
                break;
            case 6:
                if (magnitude > 6.49)
                    magnitudeColor = R.color.magnitude6_1;
                else
                    magnitudeColor = R.color.magnitude6;
                break;
            case 7:
                if (magnitude > 7.49)
                    magnitudeColor = R.color.magnitude7_1;
                else
                    magnitudeColor = R.color.magnitude7;
                break;
            case 8:
                if (magnitude > 8.49)
                    magnitudeColor = R.color.magnitude8_1;
                else
                    magnitudeColor = R.color.magnitude8;
                break;
            case 9:
                if (magnitude > 9.49)
                    magnitudeColor = R.color.magnitude9_1;
                else
                    magnitudeColor = R.color.magnitude9;
                break;
            default:
                magnitudeColor = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColor);
    }
}
