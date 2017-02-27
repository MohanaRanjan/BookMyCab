package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BookingSettingsActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TextView textview = new TextView(this);
        textview.setText("This is Windows tab");
        setContentView(textview);
    }
}
