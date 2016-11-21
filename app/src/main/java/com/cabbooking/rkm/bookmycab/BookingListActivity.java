package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingListActivity extends Activity implements View.OnClickListener{

    private ListView mCompleteListView;
    private Button mAddItemToList;
    private ArrayList<Booking> mItems;
    private CompleteBookingListAdapter mListAdapter;
    private static final int MIN = 0, MAX = 10000;
    private static  ArrayList<Booking> cloneBookinglist;
    private Button mAddBooking;
    private Switch mSwitch;
    private DBHelper db;
    private RadioButton mRadioButtonNextWeek;
    private RadioButton mRadioButtonNextMonth;
    private RadioButton mRadioButtonAllCurrentBooking;
    private RadioButton mRadioButtonAllExpiredBooking;
    private static final String NextMonth =  "NextMonth";
    private static final String NextWeek =  "NextWeek";
    private static final String AllBooking =  "All";
    private boolean IsCurrentBooking = false;
    private Calendar cal;
    private ListView mBookingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        try
        {
            setContentView(R.layout.activity_booking_list);
            initViews();

            db = new DBHelper(this);

            mItems =  db.GetAllBooking();
            mListAdapter = new CompleteBookingListAdapter(this, mItems);
            mCompleteListView.setAdapter(mListAdapter);
        }
        catch(Exception ex )
        {
            String s  =  ex.getMessage();
            String d = s;
        }
    }
    private void fillAdapter(ArrayList<Booking> mItems )
    {
        mListAdapter = new CompleteBookingListAdapter(this, mItems);

        mCompleteListView.setAdapter(mListAdapter);
    }

    private void initViews()
    {
        mCompleteListView = (ListView) findViewById(android.R.id.list);

        mAddBooking = (Button) findViewById(R.id.ButtonAddBooking);

        mRadioButtonNextWeek = (RadioButton) findViewById(R.id.radioButtonNextWeek);
        mRadioButtonNextMonth = (RadioButton) findViewById(R.id.radioButtonNextMonth);
        mRadioButtonAllCurrentBooking = (RadioButton) findViewById(R.id.radioButtonAllBooking);
        mRadioButtonAllExpiredBooking = (RadioButton) findViewById(R.id.radioButtonAllExpiredBooking);

        mBookingListView = (ListView) findViewById(android.R.id.list);

        cal = Calendar.getInstance();

        mCompleteListView.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l)
            {

                Intent mainIntent = new Intent(BookingListActivity.this, AddBookingActivity.class);
                mainIntent.putExtra("ModifyBookingRecord",((TextView) v.findViewById(R.id.BookingListBookingTransactionId)).getText());
                startActivity(mainIntent);
            }
        });

        mAddBooking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent mainIntent = new Intent(BookingListActivity.this, AddBookingActivity.class);
                startActivity(mainIntent);
            }
        });

        mRadioButtonNextWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked)
            {
                if(mRadioButtonNextWeek.isChecked())
                {
                    mCompleteListView.setAdapter(null);
                    IsCurrentBooking = true;
                    fillAdapter(mItems);

                    FilterBookingByDateRage(7);
                    mItems = (ArrayList<Booking>) cloneBookinglist.clone();
                }
            }
        });

        mRadioButtonNextMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked)
            {
                if(mRadioButtonNextMonth.isChecked())
                {
                    mCompleteListView.setAdapter(null);
                    IsCurrentBooking = true;
                    fillAdapter(mItems);

                    FilterBookingByDateRage(30);
                    mItems = (ArrayList<Booking>) cloneBookinglist.clone();
                }
            }
        });

        mRadioButtonAllCurrentBooking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked)
            {
                if(mRadioButtonAllCurrentBooking.isChecked())
                {
                    fillAdapter(mItems);
                    IsCurrentBooking = true;

                    FilterBookingByDateRage(365);

                    mItems = (ArrayList<Booking>) cloneBookinglist.clone();

                }
            }
        });

        mRadioButtonAllExpiredBooking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked)
            {


                if(mRadioButtonAllExpiredBooking.isChecked())
                {
                    mCompleteListView.setAdapter(null);
                    fillAdapter(mItems);

                    IsCurrentBooking = false;
                    FilterByBookingStatus(false);
                    mItems = (ArrayList<Booking>) cloneBookinglist.clone();
                }
            }
        });
    }


    private void FilterBookingByDateRage(Integer FilterDays)
    {
        try
        {
            cloneBookinglist = (ArrayList<Booking>) mItems.clone();
            mItems.clear();

            String StartDateString = String.format("%s-%s-%s",cal.get(Calendar.YEAR),cal.get(Calendar.MONTH) + 1,cal.get(Calendar.DAY_OF_MONTH));
            String EndDateString = String.format("%s-%s-%s",cal.get(Calendar.YEAR),cal.get(Calendar.MONTH) + 1,cal.get(Calendar.DAY_OF_MONTH) + FilterDays);

            String DateFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

            Date CurrentDate =  sdf.parse(StartDateString);
            Date FilterDate =  sdf.parse(EndDateString);

            for (Integer i=0 ;i < cloneBookinglist.size();i++)
            {
                if( IsCurrentBooking &&
                        !cloneBookinglist.get(i).getIsTravelComplete() &&
                        cloneBookinglist.get(i).getPickUpDateTime().after(CurrentDate) &&
                        cloneBookinglist.get(i).getPickUpDateTime().before(FilterDate))
                    mItems.add(cloneBookinglist.get(i));
            }

            mListAdapter.notifyDataSetChanged();
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{%s}",S));
        }

    }

    private void FilterByBookingStatus(Boolean IsCurrent)
    {
        try
        {
            cloneBookinglist = (ArrayList<Booking>) mItems.clone();
            mItems.clear();

            for (Integer i=0 ;i < cloneBookinglist.size();i++)
            {
                if(!IsCurrentBooking && cloneBookinglist.get(i).getIsTravelComplete() != IsCurrent)
                {
                    mItems.add(cloneBookinglist.get(i));
                }
            }

            mListAdapter.notifyDataSetChanged();
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }

    }



    @Override
    public void onClick(View v)
    {
        mCompleteListView.setAdapter(null);

        fillAdapter(mItems);

        switch (v.getId())
        {
            case R.id.imageViewBlueCircle:
                FilterByBookingStatus(true);
                break;
            case R.id.imageViewOrangeCircle:
                FilterByBookingStatus(false);
                break;
        }
        mItems = (ArrayList<Booking>) cloneBookinglist.clone();
    }
}
