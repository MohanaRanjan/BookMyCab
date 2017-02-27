package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends Activity
{
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private RadioButton rdoButton;
    private RadioButton rdoButtonHOI;
    private RadioButton rdoButtonAdmin;
    private RadioButton rdoButtonBookingRequester;
    private RadioButton rdoButtonDriver;
    private RadioGroup radioGroupRoles;
    private  DBHelper db;

    private boolean IsEdit = false;

    private Button btnSave;
    private Button btnCancel;

    private Users user = new Users();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_add_user);

            db = new DBHelper(this);
            db.createDataBase();
            db.openDataBase();
            addListeronButton();
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
            System.out.println(String.format("Exception Details {0}",ex.getMessage()));
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Intent intent =  getIntent();

        String userId = intent.getStringExtra("ModifyRecord");

        IsEdit = userId == null ? false : true;

        if(userId != null && !userId.isEmpty())
        {
            user = fillUser(userId);
            fillControls(user);
        }
    }

    private Users fillUser(String UserId)
    {
        return db.GetUser(UserId);
    }

    private void fillControls(Users user)
    {
        editTextName = (EditText) findViewById(R.id.Name);
        editTextEmail = (EditText) findViewById(R.id.Email);
        editTextPhone = (EditText) findViewById(R.id.phone);
        editTextPassword = (EditText) findViewById(R.id.userpassword);

        try
        {
            radioGroupRoles =  (RadioGroup) findViewById(R.id.radioGroupRoles);

            rdoButtonHOI  = (RadioButton)findViewById(R.id.radioButtonHOI);
            rdoButtonAdmin = (RadioButton)findViewById(R.id.radioButtonAdmin);
            rdoButtonBookingRequester = (RadioButton)findViewById(R.id.radioButtonBookingRequester);
            rdoButtonDriver = (RadioButton)findViewById(R.id.radioButtonDriver);

            switch(user.getUserRoleId())
            {
                case  "A":
                    rdoButtonAdmin.setChecked(true);
                    break;
                case "H":
                    rdoButtonHOI.setChecked(true);
                    break;
                case "D":
                    rdoButtonDriver.setChecked(true);
                    break;
                case "B":
                    rdoButtonBookingRequester.setChecked(true);
                    break;
            }

            editTextName.setText(user.getName());
            editTextEmail.setText(user.getEmail());
            editTextPhone.setText(user.getMobileNumber());
            editTextPassword.setText(user.getPassword());


        }catch(Exception ex)
        {
            String S  = ex.getMessage();
        }
    }

    public void addListeronButton()
    {
        btnSave = (Button) findViewById(R.id.buttonSave);
        Button mHome =  (Button) findViewById(R.id.ButtonHomeUA);

        mHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent mainIntent = new Intent(AddUserActivity.this, HomeActivity.class);
                startActivity(mainIntent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                editTextName = (EditText) findViewById(R.id.Name);
                editTextEmail = (EditText) findViewById(R.id.Email);
                editTextPhone = (EditText) findViewById(R.id.phone);
                editTextPassword = (EditText) findViewById(R.id.userpassword);
                radioGroupRoles =  (RadioGroup) findViewById(R.id.radioGroupRoles);

                if(editTextName.getText().toString().equals(""))
                {
                    editTextName.setError("Name is required");
                    return;
                }
                else if(editTextEmail.getText().toString().equals(""))
                {
                    editTextEmail.setError("Email is required");
                    return;
                }
                else if(editTextPhone.getText().toString().equals(""))
                {
                    editTextPhone.setError("Phone is required");
                    return;
                }
                else if(editTextPassword.getText().toString().equals(""))
                {
                    editTextPassword.setError("Password is required");
                    return;
                }
                else if(!db.IsUniquePhone(editTextPhone.getText().toString()))
                {
                    editTextPhone.setError("Phone number already registered");
                    return;
                }
                else if (radioGroupRoles.getCheckedRadioButtonId() == -1)
                {
                    RadioButton rdn = (RadioButton)findViewById(R.id.radioButtonAdmin);
                    Toast.makeText(getApplicationContext(), "Select any one option", Toast.LENGTH_LONG).show();
                    return;
                }

                int selectedId =  radioGroupRoles.getCheckedRadioButtonId();

                rdoButton = (RadioButton) findViewById(selectedId);

                String RoleId = "";
                switch(rdoButton.getId())
                {
                    case R.id.radioButtonAdmin:
                        RoleId = "A";
                        break;
                    case R.id.radioButtonHOI:
                        RoleId = "H";
                        break;
                    case R.id.radioButtonDriver:
                        RoleId = "D";
                        break;
                    case R.id.radioButtonBookingRequester:
                        RoleId = "B";
                        break;
                }

                if(IsEdit)
                {
                    user = new Users(
                            user.getId(),
                            editTextName.getText().toString(),
                            editTextEmail.getText().toString(),
                            user.getPassword(),
                            editTextPhone.getText().toString(),
                            RoleId,
                            user.getIsAvailable());

                    db.EditUser(user);
                }
                else
                {
                    user = new Users("",
                            editTextName.getText().toString(),
                            editTextEmail.getText().toString(),
                            editTextPassword.getText().toString(),
                            editTextPhone.getText().toString(),
                            RoleId,
                            Boolean.TRUE);

                    db.AddUser(user);
                }

                List<Users> userArray = new ArrayList<Users>();
                userArray =  db.GetAllUsers();

                new AlertDialog.Builder(AddUserActivity.this)
                        .setTitle("SuccessFul save")
                        .setMessage("Record is saved Successfully")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(editTextName.getText().toString().equals(""))
                                {
                                    editTextName.setError("Name is required");
                                }
                                else if(editTextEmail.getText().toString().equals(""))
                                {
                                    editTextEmail.setError("Email is required");
                                }
                                else if(editTextPhone.getText().toString().equals(""))
                                {
                                    editTextPhone.setError("Phone is required");
                                }
                                else if(editTextPassword.getText().toString().equals(""))
                                {
                                    editTextPassword.setError("Password is required");
                                }
                                else
                                {
                                    Intent intent = new Intent(AddUserActivity.this, CompleteListActivity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }

        });
    }
}
