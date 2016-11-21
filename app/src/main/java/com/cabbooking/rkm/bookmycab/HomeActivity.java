package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends Activity {

    Button imagebutton;
    Button imagebuttonBookingList;
    Button imagebuttonGetAllBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addListneronImageButton();
    }

    public void addListneronImageButton()
    {
        imagebutton  = (Button)findViewById(R.id.imageButtonAddUser);
        imagebutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(HomeActivity.this,CompleteListActivity.class);
                startActivity(intent);
            }
        });

        imagebuttonBookingList  = (Button)findViewById(R.id.imagebuttonBookingList);
        imagebuttonBookingList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(HomeActivity.this,BookingListActivity.class);
                    startActivity(intent);
                }
                catch(Exception ex)
                {
                    String s = ex.getMessage();
                    String g= s;
                }

            }
        });
/*
        imagebuttonGetAllBooking  = (Button)findViewById(R.id.imagebuttonbookingHistroy);
        imagebuttonGetAllBooking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(HomeActivity.this,BookingListActivity.class);
                    startActivity(intent);
                }
                catch(Exception ex)
                {
                    String s = ex.getMessage();
                    String g= s;
                }

            }
        });
        */
    }

}
