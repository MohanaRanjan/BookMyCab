package com.cabbooking.rkm.bookmycab;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;

public class UserSettingsActivity extends TabActivity {

    private DBHelper db;
    private Users user;
    private EditText oldPassword;
    private EditText NewPassword;
    private EditText NewRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings_tab_layout);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try
        {
            db = new DBHelper(this);

            Intent intent = getIntent();
            user = (Users) intent.getSerializableExtra("LoginUser");

            LoadTabs();
        }
        catch(Throwable ex)
        {
            String s  = ex.getMessage();
        }

    }

    private void LoadTabs()
    {
        Resources ressources = getResources();
        TabHost tabHost = getTabHost();

        // Android tab
        Intent intentProfile = new Intent().setClass(this, ProfileActivity.class);
        TabHost.TabSpec tabSpecProfile = tabHost
                .newTabSpec("Profile")
                .setIndicator("", ressources.getDrawable(R.drawable.icon_profile_config))
                .setContent(intentProfile);

        // Apple tab
        Intent intentBooking = new Intent().setClass(this, BookingSettingsActivity.class);
        TabHost.TabSpec tabSpecBooking = tabHost
                .newTabSpec("Booking")
                .setIndicator("", ressources.getDrawable(R.drawable.icon_bookingsettings_config))
                .setContent(intentBooking);

        tabHost.addTab(tabSpecProfile);
        tabHost.addTab(tabSpecBooking);

        tabHost.setCurrentTab(1);
    }

    private void SaveProfileSettings()
    {
        oldPassword = (EditText)findViewById(R.id.SettingsOldPassword);
        NewPassword = (EditText)findViewById(R.id.SettingsNewPassword);
        NewRePassword = (EditText)findViewById(R.id.SettingsNewPasswordRepeat);

        if(IsValidPasswordChange())
        {
          user.setPassword(NewPassword.toString());
          db.EditUser(user);
        }

    }

    private boolean IsValidPasswordChange()
    {
        return user.getPassword().equals(oldPassword.getText().toString()) &&
                NewPassword.getText().toString().equals(NewRePassword.getText().toString()) ?true:false;
    }
}
