package com.cabbooking.rkm.bookmycab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddBookingActivity extends AppCompatActivity
{
    private DatePicker datePicker;
    private DatePickerCustom datePickerCustom;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private TextView editTextPersonName;
    private EditText editTextPickUpfrom;
    private EditText editTextPlaceofvisit;
    private EditText editTextReasonTravel;
    private EditText editTextTotalTimeNeeded;
    private EditText editTextDateofTravel;
    private EditText editTextNumberOfPersons;
    private DatePicker dpDatePicker;
    private Booking booking;
    //private  AutoCompleteTextView actv;
    //private UserAutoCompleteAdapter Useradapter;
    private DBHelper db;
    private Spinner spinnerUser;
    private String BookingRequesterId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setTheme(R.style.Theme_Light_NoTitleBar_Workaround);
            setContentView(R.layout.activity_add_booking);
            db = new DBHelper(this);
            booking = new Booking();

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year,month,day);

            spinnerUser = (Spinner) findViewById(R.id.SpinnerUserName);

            AddListener();

            List<StringWithTag>  UserIdNameList =  CreateUsersList(db.GetAllUsers());
            ArrayAdapter<StringWithTag> userAdapter = new ArrayAdapter<StringWithTag>(this,R.layout.support_simple_spinner_dropdown_item,UserIdNameList);

            userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinnerUser.setAdapter(userAdapter);

        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            String g= s;
        }
    }

    private List<StringWithTag> CreateUsersList(ArrayList<Users> users)
    {
        List<StringWithTag> userlist = new ArrayList<StringWithTag>();
            try
            {
                if(users.size() > 0)
                {
                    for (int i = 0; i < users.size(); i++)
                    {
                        userlist.add(new StringWithTag(users.get(i).getName(),users.get(i)));
                    }
                }
            }
            catch(Exception ex)
            {
                String s = ex.getMessage();
                String g= s;
            }

        return userlist;
    }

    public void AddListener()
    {
        Button btn = (Button)findViewById(R.id.ButtonBookNow);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SaveBooking();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.SpinnerUserName);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                BookingRequesterId = ((Users) ((StringWithTag) parent.getItemAtPosition(pos)).tag).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void SaveBooking()
    {
       // editTextPersonName  = (TextView)findViewById(R.id.PersonName);
        editTextPickUpfrom = (EditText)findViewById(R.id.PickUpfrom);
        editTextPlaceofvisit= (EditText)findViewById(R.id.Placeofvisit);
        editTextReasonTravel = (EditText)findViewById(R.id.ReasonForTravel);
        editTextTotalTimeNeeded = (EditText)findViewById(R.id.TotalTimeNeeded);
        editTextNumberOfPersons = (EditText)findViewById(R.id.NumberOfPersons);
        dpDatePicker  =  (DatePicker) findViewById(R.id.datePicker);


        try
        {
            String DateString =  String.format("%s-%s-%s",dpDatePicker.getYear(),dpDatePicker.getMonth() + 1 ,dpDatePicker.getDayOfMonth());

            String DateFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
            Date PickUpDateTime = sdf.parse(DateString);

            int NumberOfPersons = Integer.parseInt(editTextNumberOfPersons.getText().toString());
            int TotalHoursNeeded = Integer.parseInt(editTextTotalTimeNeeded.getText().toString());

            Booking booking  = new Booking("",
                    "",
                    BookingRequesterId,
                    NumberOfPersons,
                    PickUpDateTime,
                    editTextPickUpfrom.getText().toString(),
                    editTextPlaceofvisit.getText().toString(),
                    editTextReasonTravel.getText().toString(),
                    TotalHoursNeeded,null,false,false,false
                    );

            db.AddBooking(booking);

            Toast.makeText(getApplicationContext(), "Saved Successfully",
                    Toast.LENGTH_LONG).show();
        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view)
    {
        showDialog(999);
        /*Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();*/
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        // TODO Auto-generated method stub
        if (id == 999)
        {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private EditText.OnClickListener myEditText = new EditText.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            TextView tx1 = (TextView)findViewById(R.id.LablePersonName);
           tx1.setText("");
        }
    };

    private DatePicker.OnClickListener myDateClick = new DatePicker.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
           /* Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                    .show();*/
        }
    };

    private DatePickerCustom.OnClickListener myCustomDateClick = new DatePickerCustom.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
           /* Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                    .show();*/
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day

            showDate(arg1,arg2,arg3);
        }
    };

    private void showDate(int year, int month, int day)
    {
        DatePicker dp =  (DatePicker) findViewById(R.id.datePicker);

        dp.updateDate(year,month,day);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

class StringWithTag
{
    public String string;
    public Object tag;

    public StringWithTag(String stringPart, Object tagPart)
    {
        string = stringPart;
        tag = tagPart;
    }

    @Override
    public String toString()
    {
        return string;
    }
}