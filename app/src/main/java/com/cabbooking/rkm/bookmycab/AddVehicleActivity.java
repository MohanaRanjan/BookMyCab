package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.IOException;

public class AddVehicleActivity extends Activity
{
    private DBHelper db;
    private boolean IsEdit = false;
    private Intent intent;
    private  String VehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try
        {

            setContentView(R.layout.activity_add_vehicle);
            init();

            intent = getIntent();
            String VehicleId = intent.getStringExtra("ModifyVehicleRecord");

            IsEdit = VehicleId == null ? false : true;
            db =  new DBHelper(this);
            db.createDataBase();
            db.openDataBase();

            db.getWritableDatabase();
            if(IsEdit)
            {
               Vehicle  vehicle =   db.GetVehicleById(VehicleId);
               PopulateData(vehicle);
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
        catch(Throwable ex)
        {
            String s = ex.getMessage();
            String g= s;
        }
    }

    private void PopulateData(Vehicle vehicle)
    {
        EditText CabNumber =  (EditText) findViewById(R.id.CabNumber);
        EditText CabDescription =  (EditText) findViewById(R.id.CabDescription);
        EditText VehicleModel =  (EditText) findViewById(R.id.VehicleModel);
        Switch CabAvailble = (Switch) findViewById(R.id.switchCabAvailability);

        CabNumber.setText(vehicle.getVehicleNumber());
        CabDescription.setText(vehicle.getVehicleDescription());
        VehicleModel.setText(vehicle.getVehicleModel());
    }

    private void init()
    {
        Button btnSaveVehicle =  (Button) findViewById(R.id.ButtonSaveVehicle);
        Button mHome =  (Button) findViewById(R.id.ButtonHomeVA);

        btnSaveVehicle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(IsEdit)
                {
                    SaveVehicle(true);
                }
                else
                {
                    SaveVehicle(false);
                }

                Intent intent=new Intent(AddVehicleActivity.this,VehicleListActivity.class);
                startActivity(intent);
            }
        });


        mHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent mainIntent = new Intent(AddVehicleActivity.this, HomeActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    private void SaveVehicle(Boolean IsEdit)
    {
        try
        {
            EditText CabNumber =  (EditText) findViewById(R.id.CabNumber);
            EditText CabDescription =  (EditText) findViewById(R.id.CabDescription);
            EditText VehicleModel =  (EditText) findViewById(R.id.VehicleModel);
            Switch CabAvailble = (Switch) findViewById(R.id.switchCabAvailability);

            if(IsEdit)
            {
                db.AddEditVehicle(VehicleId,CabNumber.getText().toString(),CabDescription.getText().toString(),VehicleModel.getText().toString(),4,true);
            }
            else
            {
                db.AddEditVehicle("",CabNumber.getText().toString(),CabDescription.getText().toString(),VehicleModel.getText().toString(),4,false);
            }
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{%s}",S));
        }


    }
}
