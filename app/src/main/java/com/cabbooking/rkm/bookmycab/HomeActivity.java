package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends Activity {

    ImageButton imagebutton;
    ImageButton imagebuttonAddBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addListneronImageButton();
    }

    public void addListneronImageButton()
    {
        imagebutton  = (ImageButton)findViewById(R.id.imageButtonAddUser);
        imagebutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(HomeActivity.this,CompleteListActivity.class);
                startActivity(intent);
            }
        });

        imagebuttonAddBooking  = (ImageButton)findViewById(R.id.imagebuttonAddBooking);
        imagebuttonAddBooking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(HomeActivity.this, "test", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(HomeActivity.this,AddBooking.class);
                startActivity(intent);
            }
        });
    }

}
