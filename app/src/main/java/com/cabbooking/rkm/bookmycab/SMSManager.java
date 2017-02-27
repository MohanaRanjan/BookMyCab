package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.telephony.SmsManager;
import android.widget.Toast;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ramakrishna Mission on 28-12-2016.
 */
public class SMSManager
{
    private ArrayList<String> bookingMessages;
    private static final int  SMSLENGTH = 140;
    private static final String SMSIdentifier = "RKMBKG";
    public SMSManager()
    {

    }

    public ArrayList<String> BreakSMSIntoSegments(String OriginalMessage )
    {
        ArrayList<String> MessageArray = new ArrayList<String>();

        for (int start = 0; start < OriginalMessage.length(); start += 160)
        {
            MessageArray.add(OriginalMessage.substring(start, Math.min(OriginalMessage.length(), start + 160)));
        }

        return MessageArray;
    }

    public void GetReceivedBookingMessage(ArrayList<String>  BookingsmsReceived)
    {
        bookingMessages =  BookingsmsReceived;
        ParseBookingMessage(bookingMessages);
    }

    private ArrayList<String>  AppendSMSMessageIdentifier(ArrayList<String> messageArray ,String BookerId)
    {
        ArrayList<String> msgArray =  new ArrayList<String>();
        UUID idOne = UUID.randomUUID();

        for(int count=0; count < messageArray.size() ; count++)
        {
            String MessageIdentifier = SMSIdentifier + BookerId + idOne.toString().substring(0,3)+ "-" + messageArray.size()+ "-" + Integer.toString(count + 1);

            msgArray.add(MessageIdentifier + "-" + messageArray.get(count));
        }

        return msgArray;
    }
    public ArrayList<String> DivideSMStoIntoParts(String WholeMessage,String BookerId)
    {
        ArrayList<String> messageParts = new ArrayList<String>();

        //String WholeMessage = "RKMBooking#@##@@A#@1##@@B#@Mon Jan 23 15:07:00 GMT+05:30 2017##@@C#@ooty##@@D#@any##@@E#@10##@@F#@YR##@@H#@1##@@I#@##@@J#@IE##@@K#@A##@@L#@8220725733";

        if(WholeMessage.length() > SMSLENGTH)
        {
            char [] chs = WholeMessage.toCharArray();
            //159,165,200
            //158 + 7
            int rightindex = 0;
            int StartIndex = 0;
            for(int index = 0; index < WholeMessage.length(); index += rightindex)
            {
                String rightMessagepart = "";
               //0 - 160 =
                if(index + SMSLENGTH < WholeMessage.length())
                {
                    rightMessagepart =  WholeMessage.substring(index, index + SMSLENGTH);
                    rightindex = rightMessagepart.lastIndexOf("##@@");
                    messageParts.add(rightMessagepart.substring(0,rightindex));
                }
                else
                {
                    messageParts.add(WholeMessage.substring(index,  WholeMessage.length()));
                }

                rightindex = rightMessagepart.lastIndexOf("##@@");

                if(rightindex <= 0)
                {
                    break;
                }
            }

        }

        return AppendSMSMessageIdentifier(messageParts,BookerId);
    }

    public HashMap<String,String> ParseBookingMessage(ArrayList<String>  BookingsmsArray)
    {
        HashMap<String,String> hashBookingParams = new HashMap<String, String>();

        try
        {
            StringBuilder ConcatSMS = new StringBuilder();
            for (String sms:BookingsmsArray)
            {
                ConcatSMS.append(sms);
            }

            //String booking = "RKMBooking#@2##@@A#@2##@@B#@2017-11-11 11:11:11##@@C#@Tambaram##@@D#@Meet devotees##@@E#@5##@@F#@YF##@@G#@##@@H#@1##@@I#@##@@J#@CH##@@K#@A##@@L#@9874563210";

            String [] messageParts = ConcatSMS.toString().split("##@@?");

            for (int i=0; i < messageParts.length;i++)
            {
                String [] infopart = messageParts[i].split("#@");

                hashBookingParams.put(infopart.length > 0 ? infopart[0] : "",infopart.length > 1 ? infopart[1]: "");
            }
        }
        catch(Throwable ex)
        {
            String s = ex.getMessage();
        }

        return hashBookingParams;
    }
}
