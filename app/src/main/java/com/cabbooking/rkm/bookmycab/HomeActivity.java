package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends Activity
{

    Button imagebutton;
    Button imagebuttonBookingList;
    Button imagebuttonGetAllBooking;
    Button imagebuttontaxi;
    Button imageButtonSettings;
    Users user;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        user = (Users) intent.getSerializableExtra("LoginUser");
        ctx = this.getApplicationContext();

        addListneronImageButton();
    }

    public void addListneronImageButton()
    {

       Button imageButtonTesting  = (Button)findViewById(R.id.imageButtonTesting);

        imageButtonTesting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SmsListener listner  = new SmsListener();
                SMSManager mgr = new SMSManager();
               // ArrayList<String> messages = mgr.DivideSMStoIntoParts("");

                //HashMap<String,String> hashmsg = mgr.ParseBookingMessage(new ArrayList<String>());
                //listner.SaveSMSBookingInfoToDb(hashmsg,ctx);
                String s = "";
            }
        });
        imageButtonSettings  = (Button)findViewById(R.id.imageButtonSettings);

        imageButtonSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(HomeActivity.this,UserSettingsActivity.class);
                intent.putExtra("LoginUser",user);
                startActivity(intent);
            }
        });

        imagebutton  = (Button)findViewById(R.id.imageButtonAddUser);


        /*if(user != null  && user.getUserRoleId().equals("A"))
        {
            //imagebutton.setVisibility(View.GONE);
        }
*/
        imagebutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(HomeActivity.this,CompleteListActivity.class);
                intent.putExtra("LoginUser",user);
                startActivity(intent);
            }
        });

        imagebuttonBookingList  = (Button)findViewById(R.id.imagebuttonBookingList);

       /* if(user != null  && user.getUserRoleId() != "A")
        {
            //imagebuttonBookingList.setVisibility(View.GONE);
        }
        */

        imagebuttonBookingList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(HomeActivity.this,BookingListActivity.class);

                    intent.putExtra("LoginUser",user);
                    startActivity(intent);
                }
                catch(Exception ex)
                {
                    String s = ex.getMessage();
                    String g= s;
                }

            }
        });



        imagebuttontaxi  = (Button)findViewById(R.id.imagebuttontaxi);

      /*  if(user != null  && user.getUserRoleId() != "A")
        {
            //imagebuttontaxi.setVisibility(View.GONE);
        }
*/
        imagebuttontaxi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(HomeActivity.this,VehicleListActivity.class);
                    startActivity(intent);
                }
                catch(Exception ex)
                {
                    String s = ex.getMessage();
                    String g= s;
                }

            }
        });

    }

}
