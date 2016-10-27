package com.cabbooking.rkm.bookmycab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddBooking extends AppCompatActivity
{
    private DatePicker datePicker;
    private DatePickerCustom datePickerCustom;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_add_booking);
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year,month,day);

        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            String g= s;
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
