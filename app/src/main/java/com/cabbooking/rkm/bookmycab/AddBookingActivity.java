package com.cabbooking.rkm.bookmycab;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
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
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    private TimePicker dpTimePicker;
    private Booking booking;
    //private  AutoCompleteTextView actv;
    //private UserAutoCompleteAdapter Useradapter;
    private DBHelper db;
    private Spinner spinnerUser;
    private String BookingRequesterId ="";
    private String BookingRequesterName = "";
    //private String BookingRequesterId = "";
    private boolean IsEdit = false;
    private  Intent intent;
    private String GlobalBookingTransactionId;
    private Spinner spinner;
    private String OldPlaceOfVisit;
    private Date OldPickupdateTime;
    private String OldBookingTrackingId;
    private Boolean IsVehicleDetailAdd;
    private Booking bookingWithVehicle;
    private  String BookingTrackingId;
    private String bookingReqPhone;
    private TextView textviewVehicleAssigned;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private String mPhoneNumber="";
    private String Message = "";
    private SMSManager smsManagerUtil;
    final int version = android.os.Build.VERSION.SDK_INT;
    private Users user;
    ArrayAdapter<StringWithTag> userAdapter;
    private boolean IsApprovedByHOI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try
        {
            setTheme(R.style.Theme_Light_NoTitleBar_Workaround);
            setContentView(R.layout.activity_add_booking);
            db = new DBHelper(this);

            db.createDataBase();
            db.openDataBase();

            booking = new Booking();
            dpDatePicker  =  (DatePicker) findViewById(R.id.datePicker);
            dpTimePicker =  (TimePicker) findViewById(R.id.timePicker);
            smsManagerUtil = new SMSManager();
            spinnerUser = (Spinner) findViewById(R.id.SpinnerUserName);

            intent = getIntent();

            BookingTrackingId = intent.getStringExtra("ModifyBookingRecord");
            bookingWithVehicle = (Booking)intent.getSerializableExtra("RetriveBooking");

            IsVehicleDetailAdd =  bookingWithVehicle == null ? false: true;

            IsEdit = BookingTrackingId == null ? false : true;

            AddListener();

            List<StringWithTag>  UserIdNameList =  CreateUsersList(db.GetAllUsers());
            userAdapter = new ArrayAdapter<StringWithTag>(this,R.layout.support_simple_spinner_dropdown_item,UserIdNameList);


            userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinnerUser.setAdapter(userAdapter);

            Intent intent = getIntent();
            user = (Users) intent.getSerializableExtra("LoginUser");

            if (IsVehicleDetailAdd)
            {
                FillControls(bookingWithVehicle);
                booking = bookingWithVehicle;
                OldPlaceOfVisit =  booking.getPlaceOfVisit();
                OldPickupdateTime = booking.getPickUpDateTime();
                OldBookingTrackingId = booking.getBookingTrackingId();
            }
            else if(IsEdit)
            {
                if(BookingTrackingId != null && !BookingTrackingId.isEmpty())
                {
                    booking = GetBooking(BookingTrackingId);
                    OldPlaceOfVisit =  booking.getPlaceOfVisit();
                    OldPickupdateTime = booking.getPickUpDateTime();
                    OldBookingTrackingId = booking.getBookingTrackingId();

                    FillControls(booking);
                }
            }
            else
            {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                showDate(year,month,day);
            }
        }
        catch (IOException ioe)
        {

            throw new Error("Unable to create database");

        }
        catch(SQLException sqle)
        {

            throw sqle;

        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            String g= s;
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (intent != null)
        {
            if(GlobalBookingTransactionId != null && !GlobalBookingTransactionId.isEmpty())
            {
               // booking = GetBooking(GlobalBookingTransactionId);
                //FillControls(booking);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(mPhoneNumber, null, "sample mesage today", null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }


    // —sends a SMS message to another device—
    private void sendSMSNew(String phoneNumber, String message)
    {

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

      /*  PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        // —when the SMS has been sent—
        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // —when the SMS has been delivered—
        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        //SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        */
        ArrayList<String> messageparts = new ArrayList<>();
        SmsManager sms = SmsManager.getDefault();
        if(message.toCharArray().length > 160)
        {
            messageparts =   sms.divideMessage(message);
            for (int i=0;i<messageparts.size() - 1 ;i++)
            {
                sms.sendMultipartTextMessage(phoneNumber, null, messageparts, null, null);
            }
        }
        else
        {
            sms.sendTextMessage(phoneNumber, null, message, null, null);
        }

        Toast.makeText(getBaseContext(), "SMS sent to  " +  phoneNumber, Toast.LENGTH_SHORT).show();
        //http://stackoverflow.com/questions/12384344/how-to-send-message-send-and-receive-between-two-emulator-in-android-but-is-no
        //http://www.vineetdhanawat.com/blog/2012/04/how-to-use-broadcast-receiver-in-android-send-and-receive-sms/
    }


    protected  void SendSMS(String MessageBody, String MobileNumber)
    {
        String s= "test";

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("5556", null, "sample mesage today", null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS))
                {

                }
                else
                {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
    }

    private void  FillControls(Booking booking)
    {
        editTextPickUpfrom = (EditText)findViewById(R.id.PickUpfrom);
        editTextPlaceofvisit= (EditText)findViewById(R.id.Placeofvisit);
        editTextReasonTravel = (EditText)findViewById(R.id.ReasonForTravel);
        editTextTotalTimeNeeded = (EditText)findViewById(R.id.TotalTimeNeeded);
        editTextNumberOfPersons = (EditText)findViewById(R.id.NumberOfPersons);
        textviewVehicleAssigned = (TextView) findViewById(R.id.VehicleAssigned);

        dpTimePicker  =  (TimePicker) findViewById(R.id.timePicker);

        try
        {
            editTextPickUpfrom.setText(booking.getPlaceOfPickup());
            editTextPlaceofvisit.setText(booking.getPlaceOfVisit());
            editTextReasonTravel.setText(booking.getReasonForTravel());
            editTextTotalTimeNeeded.setText(String.valueOf(booking.getRequiredHours()));
            editTextNumberOfPersons.setText(String.valueOf(booking.getNumberOfPersons()));

            if(booking.getVehicle() != null || IsEdit)
            {
                textviewVehicleAssigned.setText(
                        String.format("%s - %s",
                                booking.getVehicle().getVehicleNumber(),
                                booking.getVehicle().getVehicleModel())
                );
            }

            SetSpinnerValue(spinnerUser,booking);

            dpTimePicker.setCurrentHour(booking.getPickUpDateTime().getHours());
            dpTimePicker.setCurrentMinute(booking.getPickUpDateTime().getMinutes());

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

            String PickupDateString = dateformat.format(booking.getPickUpDateTime());

            Calendar cal =  Calendar.getInstance();

            cal.setTime(booking.getPickUpDateTime());

            int Year = cal.get(Calendar.YEAR);
            int Month =  cal.get(Calendar.MONTH);
            int Day  =  cal.get(Calendar.DAY_OF_MONTH);

            showDate(Year,Month,Day);

        }
        catch(Throwable ex)
        {
            String s = ex.getMessage();
            String g= s;
        }

    }

    private void SetSpinnerValue(Spinner spinner, Booking value)
    {
        for (int i = 0; i < spinner.getCount(); i++)
        {
            if (Objects.equals(spinner.getItemAtPosition(i).toString(),value.getBookingRequesterName().toString()))
            {
                spinner.setSelection(i);
                break;
            }
        }
    }
    private Booking GetBooking(String BookingTrackingId)
    {
        return db.GetBookingById(BookingTrackingId);
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

        Button ButtonSearchCab = (Button) findViewById(R.id.ButtonSearchCab);

        Button mHome = (Button) findViewById(R.id.ButtonHomeBA);

        Button mTransformToHOI = (Button) findViewById(R.id.ButtonTransformToHOI);

        Button ButtonApproveByHOI = (Button) findViewById(R.id.ButtonApproveByHOI);


        ButtonApproveByHOI.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IsApprovedByHOI  = true;
                SaveBooking(true,false,true);
                Toast.makeText(getApplicationContext(), "Approved the Request",
                        Toast.LENGTH_LONG).show();
            }
        });

        mTransformToHOI.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SaveBooking(true,false,true);
                Toast.makeText(getApplicationContext(), "Approval transformed to HOI",
                        Toast.LENGTH_LONG).show();
            }
        });


        mHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent mainIntent = new Intent(AddBookingActivity.this, HomeActivity.class);
                startActivity(mainIntent);
            }
        });


        ButtonSearchCab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    String DateString =  String.format("%s-%s-%s %s:%s:%s",dpDatePicker.getYear(),dpDatePicker.getMonth() + 1 ,dpDatePicker.getDayOfMonth(),dpTimePicker.getCurrentHour(),dpTimePicker.getCurrentMinute(),"00");

                    String DateFormat = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
                    Date PickUpDateTime = sdf.parse(DateString);

                    SaveBooking(false,true,false);
                    /*HashMap<String,Object> hash = new HashMap<String, Object>();

                    hash.put("TotalTimeNeeded",Integer.parseInt(((TextView) view.findViewById(R.id.TotalTimeNeeded)).getText().toString()));
                    hash.put("PickUpDateTime",PickUpDateTime);*/
                    Intent mainIntent = new Intent(AddBookingActivity.this, VehicleListActivity.class);
                    mainIntent.putExtra("SearChCab",booking);
                    mainIntent.putExtra("ModifyBookingRecord",BookingTrackingId);
                    startActivity(mainIntent);

                }
                catch(Throwable ex)
                {
                    String S = ex.getMessage();
                    System.out.println(String.format("Cab Booking Exception details :{%s}",S));
                }
            }
        });


        btn.setBackgroundResource(R.drawable.green_bg_1);

        if (IsEdit)
        {
            btn.setBackgroundResource(R.drawable.blue_bg_1);
            btn.setText("Update Booking");
        } else
        {
            btn.setBackgroundResource(R.drawable.green_bg_1);
            btn.setText("Create New Booking");
        }

        /*
        if(version >= 21)
        {
            btn.setBackground(getDrawable(R.drawable.green_bg_1));
            if (IsEdit) {
                btn.setBackground(getDrawable(R.drawable.blue_bg_1));
                btn.setText("Update Booking");
            } else {
                btn.setBackground(getDrawable(R.drawable.green_bg_1));
                btn.setText("Create New Booking");
            }
        }
        else
        {
            btn.setBackgroundResource(R.drawable.green_bg_1);

            if (IsEdit)
            {
                btn.setBackgroundResource(R.drawable.blue_bg_1);
                btn.setText("Update Booking");
            } else
            {
                btn.setBackgroundResource(R.drawable.green_bg_1);
                btn.setText("Create New Booking");
            }

        }
        */
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(IsEdit)
                {
                    SaveBooking(true,false,false);
                }
                else
                {
                    SaveBooking(false,false,false);
                }

                Intent mainIntent = new Intent(AddBookingActivity.this, BookingListActivity.class);
                startActivity(mainIntent);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.SpinnerUserName);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                BookingRequesterId = ((Users)((StringWithTag) parent.getItemAtPosition(pos)).tag).getId();

                BookingRequesterName = ((Users)((StringWithTag) parent.getItemAtPosition(pos)).tag).getName();

                bookingReqPhone =  ((Users)((StringWithTag) parent.getItemAtPosition(pos)).tag).getMobileNumber();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

    }

    private void SaveBooking(Boolean IsUpdate,Boolean IsSearchCab,boolean TransformRequestToHOI)
    {
        editTextPickUpfrom = (EditText)findViewById(R.id.PickUpfrom);
        editTextPlaceofvisit= (EditText)findViewById(R.id.Placeofvisit);
        editTextReasonTravel = (EditText)findViewById(R.id.ReasonForTravel);
        editTextTotalTimeNeeded = (EditText)findViewById(R.id.TotalTimeNeeded);
        editTextNumberOfPersons = (EditText)findViewById(R.id.NumberOfPersons);
        dpDatePicker  =  (DatePicker) findViewById(R.id.datePicker);
        dpTimePicker  =  (TimePicker) findViewById(R.id.timePicker);
        textviewVehicleAssigned = (TextView) findViewById(R.id.VehicleAssigned);

        try
        {

            String DateString =  String.format("%s-%s-%s %s:%s:%s",dpDatePicker.getYear(),dpDatePicker.getMonth() + 1 ,dpDatePicker.getDayOfMonth(),dpTimePicker.getCurrentHour(),dpTimePicker.getCurrentMinute(),"00");

            String DateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
            Date PickUpDateTime = sdf.parse(DateString);

            int NumberOfPersons = Integer.parseInt(editTextNumberOfPersons.getText().toString());
            int TotalHoursNeeded = Integer.parseInt(editTextTotalTimeNeeded.getText().toString());
            String VehicleId = booking.getVehicleId();
            String DriverId = booking.getDriverId();

            StringWithTag selectedUser = (StringWithTag) spinnerUser.getSelectedItem();
            Users user =  (Users) selectedUser.tag;
            if (user != null)
            {
                mPhoneNumber =  user.getMobileNumber().toString();
            }


            if(!IsSearchCab)
            {
                if (IsUpdate)
                {
                    String OldBookingTrackingIdToSend =  (
                            OldPlaceOfVisit.equalsIgnoreCase(editTextPlaceofvisit.getText().toString()) &&
                                    OldPickupdateTime.compareTo(PickUpDateTime)== 0) ? "" : OldBookingTrackingId;

                    StringBuilder MessageBody = new StringBuilder();
                    // MessageBody.append("RKMBooking#@" + editTextNumberOfPersons.getText().toString());
                    //MessageBody.append("##@@");
                    MessageBody.append("A#@" + editTextNumberOfPersons.getText().toString());
                    MessageBody.append("##@@");
                    MessageBody.append("B#@" + PickUpDateTime.toString());
                    MessageBody.append("##@@");
                    MessageBody.append("C#@" + editTextPlaceofvisit.getText().toString());
                    MessageBody.append("##@@");
                    MessageBody.append("D#@" + editTextReasonTravel.getText().toString() );
                    MessageBody.append("##@@");
                    MessageBody.append("E#@" + editTextTotalTimeNeeded.getText().toString());
                    MessageBody.append("##@@");
                    MessageBody.append("F#@" + BookingRequesterId);
                    MessageBody.append("##@@");
                    MessageBody.append("G#@" + OldBookingTrackingIdToSend);
                    MessageBody.append("##@@");
                    MessageBody.append("H#@" + "1");
                    MessageBody.append("##@@");
                    MessageBody.append("I#@" + booking.getDriverId().toString());
                    MessageBody.append("##@@");
                    MessageBody.append("J#@" + booking.getVehicleId());
                    MessageBody.append("##@@");

                    BookingStatus bookingStatus = new BookingStatus();
                    if (TransformRequestToHOI)
                    {
                        bookingStatus =  booking.getBookingStatus();
                    }

                    MessageBody.append("K#@" + bookingStatus.get_BookingStatus());
                    MessageBody.append("##@@");
                    MessageBody.append("L#@" + bookingReqPhone);

                    if(TransformRequestToHOI)
                    {
                        db.AddEditBooking("","",BookingRequesterId,
                                NumberOfPersons,PickUpDateTime,editTextPickUpfrom.getText().toString(),
                                editTextPlaceofvisit.getText().toString(),editTextReasonTravel.getText().toString(),
                                TotalHoursNeeded,null,false,false,false,
                                BookingRequesterName,OldBookingTrackingIdToSend,true,
                                booking.getDriverId(),booking.getVehicleId(),"", ApplicationConstants.BOOKINGSTATUS.HOI_APPROVAL_REQUEST,user.getUserRoleId().equals("A") ?  user.getId() : "");
                    }
                    else if (IsApprovedByHOI)
                    {
                        db.AddEditBooking("","",BookingRequesterId,
                                NumberOfPersons,PickUpDateTime,editTextPickUpfrom.getText().toString(),
                                editTextPlaceofvisit.getText().toString(),editTextReasonTravel.getText().toString(),
                                TotalHoursNeeded,null,true,false,false,
                                BookingRequesterName,OldBookingTrackingIdToSend,true,
                                booking.getDriverId(),booking.getVehicleId(),user.getId(), ApplicationConstants.BOOKINGSTATUS.HOI_APPROVAL_REQUEST,user.getUserRoleId().equals("A") ?  user.getId() : "");
                    }
                    else
                    {
                        db.AddEditBooking("","",BookingRequesterId,
                                NumberOfPersons,PickUpDateTime,editTextPickUpfrom.getText().toString(),
                                editTextPlaceofvisit.getText().toString(),editTextReasonTravel.getText().toString(),
                                TotalHoursNeeded,null,false,false,false,
                                BookingRequesterName,OldBookingTrackingIdToSend,true,
                                booking.getDriverId(),booking.getVehicleId(),"", ApplicationConstants.BOOKINGSTATUS.BOOKING_REQUEST,user.getUserRoleId().equals("A") ?  user.getId() : "");
                    }

                    ArrayList<String> MessageArray =  smsManagerUtil.DivideSMStoIntoParts(MessageBody.toString(),BookingRequesterId);

//#RKMBooking# 3#A# 3 #B# Fri Jan 13 04:08:00 EST 2017 #C# Coimbatore #D# Diksha #E# 48 #F# SW Gautamanada #G#  #H# 1 #I#  #J# NP #K# A #L# 9874563210
                    for (int messagecount = 0;messagecount <MessageArray.size();messagecount++)
                    {
                        if(mPhoneNumber != null)
                        {
                            sendSMSNew(mPhoneNumber, MessageArray.get(messagecount));
                        }
                    }
                }
                else
                {

                    db.AddEditBooking("","",BookingRequesterId,
                            NumberOfPersons,PickUpDateTime,editTextPickUpfrom.getText().toString(),
                            editTextPlaceofvisit.getText().toString(),editTextReasonTravel.getText().toString(),
                            TotalHoursNeeded,null,false,false,false,
                            BookingRequesterName,"",false,
                            bookingWithVehicle.getDriverId(),bookingWithVehicle.getVehicleId(),"",ApplicationConstants.BOOKINGSTATUS.BOOKING_REQUEST,user.getUserRoleId().equals("A") ?  user.getId() : "");

                    StringBuilder MessageBody = new StringBuilder();
                    //MessageBody.append("RKMBooking#@" + booking.getGlobalBookingTransactionId() );
                    //MessageBody.append("##@@");
                    MessageBody.append("A#@" + editTextNumberOfPersons.getText().toString() );
                    MessageBody.append("##@@");
                    MessageBody.append("B#@" + PickUpDateTime.toString() );
                    MessageBody.append("##@@");
                    MessageBody.append("C#@" + editTextPlaceofvisit.getText().toString() );
                    MessageBody.append("##@@");
                    MessageBody.append("D#@" + editTextReasonTravel.getText().toString() );
                    MessageBody.append("##@@");
                    MessageBody.append("E#@" + editTextTotalTimeNeeded.getText().toString());
                    MessageBody.append("##@@");
                    MessageBody.append("F#@" + BookingRequesterId);
                    MessageBody.append("##@@");
                    MessageBody.append("H#@" + "1");
                    MessageBody.append("##@@");
                    MessageBody.append("I#@" + booking.getDriverId().toString());
                    MessageBody.append("##@@");
                    MessageBody.append("J#@" + booking.getVehicleId());
                    MessageBody.append("##@@");
                    MessageBody.append("K#@" + "A");
                    MessageBody.append("##@@");
                    MessageBody.append("L#@" + bookingReqPhone);

                    ArrayList<String> MessageArray =  smsManagerUtil.DivideSMStoIntoParts(MessageBody.toString(),BookingRequesterId);


                    for (int messagecount = 0;messagecount < MessageArray.size(); messagecount++)
                    {
                        if(mPhoneNumber != null)
                        {
                            sendSMSNew(mPhoneNumber, MessageArray.get(messagecount));
                        }
                    }
                }
            }

            booking  = new Booking(-1,"",
                    "",
                    BookingRequesterId,
                    NumberOfPersons,
                    PickUpDateTime,
                    editTextPickUpfrom.getText().toString(),
                    editTextPlaceofvisit.getText().toString(),
                    editTextReasonTravel.getText().toString(),
                    TotalHoursNeeded,null,false,false,false,BookingRequesterName,"","",new Vehicle(),
            new BookingStatus());

            Toast.makeText(getApplicationContext(), "Saved Successfully",
                    Toast.LENGTH_LONG).show();
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{%s}",S));
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view)
    {
        showDialog(999);
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

        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
        {
            showDate(arg1,arg2,arg3);
        }
    };

    private DatePicker.OnDateChangedListener dateSetListener = new DatePicker.OnDateChangedListener()
    {
        public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth)
        {
            showDate(year,monthOfYear,dayOfMonth);
        }
    };

    private void showDate(int year, int month, int day)
    {
        DatePicker dp =  (DatePicker) findViewById(R.id.datePicker);

        dp.updateDate(year,month,day);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}


class ReceiveMessage extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "\n";
            }
            //---display the new SMS message---
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
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