package com.cabbooking.rkm.bookmycab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.io.IOException;
import java.sql.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ramakrishan Mission Chennai on 10-01-2017.
 */
//ref http://stackoverflow.com/questions/7089313/android-listen-for-incoming-sms-messages
public class SmsListener extends BroadcastReceiver
{
    public SmsListener()
    {
        super();
        smsManager = new SMSManager();
    }

    private  ArrayList<String> smsRecevedArray;
    private SMSManager smsManager;
    DBHelper db;
    HashMap<String,String> bookinginfoKeyValue = new HashMap<String,String>();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ArrayList<String> smsRecevedArray = new ArrayList<>();

        db = new DBHelper(context);


        try
        {
            db.createDataBase();
            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction()))
            {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent))
                {
                    smsRecevedArray.add(smsMessage.getMessageBody());
                }

                if(smsRecevedArray.size() > 0)
                {
                    bookinginfoKeyValue =  smsManager.ParseBookingMessage(smsRecevedArray);
                }
            }

            SaveSMSBookingInfoToDb(bookinginfoKeyValue,null);

        }
        catch (IOException ioe)
        {

            throw new Error("Unable to create database");

        }
        catch(SQLException sqle)
        {

            throw sqle;

        }
        catch (Throwable ex)
        {
            String e = ex.getMessage();
        }
    }

    public void SaveSMSBookingInfoToDb(HashMap<String,String> bookinginfoKeyValue, Context ctx)
    {
        String DateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        DBHelper db1 =  new  DBHelper(ctx);
        db1.getWritableDatabase();

        try
        {
            String GlobalBookingTransactionId = bookinginfoKeyValue.get("RKMBooking");
            String BookingTrackingId = "";
            String BookingRequesterId =   bookinginfoKeyValue.get("F");
            Integer NumberOfPersons  =  Integer.parseInt(bookinginfoKeyValue.get("A"));
            Date PickUpDateTime  = sdf.parse(bookinginfoKeyValue.get("B"));
            String PlaceOfPickup =   bookinginfoKeyValue.get("RKMBooking");
            String PlaceOfVisit =   bookinginfoKeyValue.get("C");
            String ReasonForTravel =   bookinginfoKeyValue.get("D");
            Integer RequiredHours =   Integer.parseInt(bookinginfoKeyValue.get("E"));
            Date TravelCompleteDate = null;
            boolean IsApproved = true;
            boolean IsTravelComplete = false;
            boolean IsTravelAborted = false;
            String BookingRequesterName = bookinginfoKeyValue.get("F");
            String OldBookingTrackingId =   bookinginfoKeyValue.get("G");
            Boolean IsEdit =  false;
            String DriverId =   bookinginfoKeyValue.get("I");
            String CabId =   bookinginfoKeyValue.get("J");
            String ApprovedById =   bookinginfoKeyValue.get("F");
            String BookingStatusId =   bookinginfoKeyValue.get("K");
            ApplicationConstants.BOOKINGSTATUS bookingStatusEnum  = ApplicationConstants.BOOKINGSTATUS.BOOKING_REQUEST;

            switch(BookingStatusId)
            {
                case "A":
                    bookingStatusEnum =  ApplicationConstants.BOOKINGSTATUS.BOOKING_REQUEST;
                    break;
                case "B":
                    bookingStatusEnum =  ApplicationConstants.BOOKINGSTATUS.APPROVED;
                    break;
                case "C":
                    bookingStatusEnum =  ApplicationConstants.BOOKINGSTATUS.HOI_APPROVAL_REQUEST;
                    break;
                case "D":
                    bookingStatusEnum =  ApplicationConstants.BOOKINGSTATUS.COMPLETED;
                    break;
                case "E":
                    bookingStatusEnum =  ApplicationConstants.BOOKINGSTATUS.REJECTED;
                    break;
                case "F":
                    bookingStatusEnum =  ApplicationConstants.BOOKINGSTATUS.CANCELED;
                    break;
            }


            db1.AddEditBooking(GlobalBookingTransactionId,
                    BookingTrackingId,
                    BookingRequesterId,
                    NumberOfPersons,
                    PickUpDateTime,
                    PlaceOfPickup,
                    PlaceOfVisit,
                    ReasonForTravel,
                    RequiredHours,
                    TravelCompleteDate,
                    IsApproved,
                    IsTravelComplete,
                    IsApproved,
                    BookingRequesterName,
                    OldBookingTrackingId,
                    IsEdit,
                    DriverId,
                    CabId,
                    ApprovedById,
                    bookingStatusEnum,"");

        }
        catch (Throwable ex)
        {
            String e = ex.getMessage();
        }
    }
}