package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
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
    private Button mHome;
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
    private Users user;
    private ListView mBookingListView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        try
        {
            setContentView(R.layout.activity_booking_list);
            Intent intent = getIntent();
            user = (Users) intent.getSerializableExtra("LoginUser");

            initViews();

            db = new DBHelper(this);
            db.createDataBase();
            db.openDataBase();
            //http://stackoverflow.com/questions/4208886/using-the-android-application-class-to-persist-data
            //https://web.archive.org/web/20130818035631/http://www.bryandenny.com/index.php/2010/05/25/what-i-learned-from-writing-my-first-android-application

            if(user == null)
            {
                SharedPreferences prefs = getSharedPreferences("LoginDetails", 0);
                String Mobile = prefs.getString("key_Mobile", "0000000000");
                String Password = prefs.getString("key_Password", "password");
                user = db.GetUserLogin(Mobile, Password);
            }

            if(user.getUserRoleId().equals("H"))
            {
                mItems =  db.GetAllBooking(true,null,"",user.getUserRoleId());
            }
            else if(user.getUserRoleId().equals("B") || user.getUserRoleId().equals("D"))
            {
                mItems =  db.GetAllBooking(false,null,user.getId(),user.getUserRoleId());
            }
            else if(user.getUserRoleId().equals("A"))
            {
                mItems =  db.GetAllBooking(false,null,"",user.getUserRoleId());
            }

            mListAdapter = new CompleteBookingListAdapter(this, mItems);
            mCompleteListView.setAdapter(mListAdapter);

        }
        catch(SQLException sqle)
        {

            throw sqle;

        }
        catch(Exception ex )
        {
            String s  =  ex.getMessage();
            String d = s;
        }
    }

    private void GetBookingInfo()
    {
        SmsListener smslistener = new SmsListener();

       // smslistener.BookingRawinfo()
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
        mHome = (Button) findViewById(R.id.ButtonHomeB);

      /*  Button buttonSentHOIApproval = (Button) findViewById(R.id.imagebuttonStatusSentHOIApproval);
        Button buttonSentHOIApproved = (Button) findViewById(R.id.imagebuttonStatusHOIApproved);
        Button buttonSentHOIRejected = (Button) findViewById(R.id.imagebuttonStatusSentHOIRejected);



        mRadioButtonNextWeek = (RadioButton) findViewById(R.id.radioButtonNextWeek);
        mRadioButtonNextMonth = (RadioButton) findViewById(R.id.radioButtonNextMonth);
        mRadioButtonAllCurrentBooking = (RadioButton) findViewById(R.id.radioButtonAllBooking);
        mRadioButtonAllExpiredBooking = (RadioButton) findViewById(R.id.radioButtonAllExpiredBooking);
        */

        mBookingListView = (ListView) findViewById(android.R.id.list);

        Spinner SpinnerBookingStatus = (Spinner)findViewById(R.id.SpinnerBookingStatus);
        Spinner SpinnerFilterByDate = (Spinner)findViewById(R.id.SpinnerFilterByDate);


        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Sent for HOI Approval");
        spinnerArray.add("HOI Approved");
        spinnerArray.add("HOI Rejected");

        List<String> spinnerArrayFilter =  new ArrayList<String>();
        spinnerArrayFilter.add("Next One Week");
        spinnerArrayFilter.add("Next 30 Days");
        spinnerArrayFilter.add("All Current Booking");
        spinnerArrayFilter.add("All Expired Booking");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerBookingStatus.setAdapter(adapter);

        ArrayAdapter<String> adapterFilterByDate = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapterFilterByDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerFilterByDate.setAdapter(adapterFilterByDate);


        cal = Calendar.getInstance();

        mCompleteListView.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l)
            {

                Intent mainIntent = new Intent(BookingListActivity.this, AddBookingActivity.class);
                mainIntent.putExtra("ModifyBookingRecord",((TextView) v.findViewById(R.id.BookingListBookingId)).getText());
                startActivity(mainIntent);
            }
        });


   /*     buttonSentHOIApproval.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FilterBasedOnStatus(ApplicationConstants.BOOKINGSTATUS.HOI_APPROVAL_REQUEST,false);
            }
        });

        buttonSentHOIApproved.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FilterBasedOnStatus(ApplicationConstants.BOOKINGSTATUS.HOI_APPROVAL_REQUEST_ACCEPTED,false);
            }
        });

        buttonSentHOIRejected.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FilterBasedOnStatus(ApplicationConstants.BOOKINGSTATUS.HOI_APPROVAL_REQUEST_REJECTED,false);
            }
        });
*/
        mAddBooking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent mainIntent = new Intent(BookingListActivity.this, AddBookingActivity.class);
                mainIntent.putExtra("LoginUser",user);
                startActivity(mainIntent);
            }
        });

        mHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent mainIntent = new Intent(BookingListActivity.this, HomeActivity.class);
                startActivity(mainIntent);
            }
        });
/*
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
        */
    }

    private void FilterBasedOnStatus(ApplicationConstants.BOOKINGSTATUS BookingStatus,boolean ShowAll)
    {
        try
        {
            Booking b;
            cloneBookinglist = (ArrayList<Booking>) mItems.clone();
            mItems.clear();

            String bookingStatusCode = "";

            switch (BookingStatus)
            {
                case BOOKING_REQUEST:
                    bookingStatusCode = "A";
                    break;
                case APPROVED:
                    bookingStatusCode = "B";
                    break;
                case HOI_APPROVAL_REQUEST:
                    bookingStatusCode = "C";
                    break;
                case COMPLETED:
                    bookingStatusCode = "D";
                    break;
                case REJECTED:
                    bookingStatusCode = "E";
                    break;
                case CANCELED:
                    bookingStatusCode = "F";
                    break;
                case HOI_APPROVAL_REQUEST_ACCEPTED:
                    bookingStatusCode = "G";
                    break;
                case HOI_APPROVAL_REQUEST_REJECTED:
                    bookingStatusCode = "H";
                    break;
            }
            for (Integer i=0 ;i < cloneBookinglist.size();i++)
            {
                b = cloneBookinglist.get(i);
                if(!ShowAll &&  bookingStatusCode.equals(b.getBookingStatus().get_BookingStatus()) )
                {
                    mItems.add(cloneBookinglist.get(i));
                }
                else if(ShowAll)
                {
                    mItems.add(cloneBookinglist.get(i));
                }
            }

            mListAdapter.notifyDataSetChanged();
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{%s}",S));
        }
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
