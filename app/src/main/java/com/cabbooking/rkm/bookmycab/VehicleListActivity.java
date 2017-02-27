package com.cabbooking.rkm.bookmycab;

import android.content.Intent;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VehicleListActivity extends AppCompatActivity
{

    private DBHelper db;
    private ExpandableListAdapter VehilceListAdapter;
    private ExpandableListView expListView;
    List<Vehicle> listDataHeader;
    //HashMap<String, ArrayList<VehicleBooking>> listDataChild;
    HashMap<String, ArrayList<VehicleBooking>> listDataChild;
    private  Intent intent;
    private boolean IsSearchCab;
    private Booking booking;
    private String BookingTrackingId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try
        {
            setContentView(R.layout.activity_vehicle_list);
            Integer TotalHours;
            Date PickUpDateTime;
            Date BookingToDate;
            ArrayList<Vehicle> allVehicle  = new ArrayList<Vehicle>();

            db =  new DBHelper(this);
            db.createDataBase();
            db.openDataBase();

            InitViews();
            intent = getIntent();
            booking = (Booking) intent.getSerializableExtra("SearChCab");

            BookingTrackingId = intent.getStringExtra("ModifyBookingRecord");

            IsSearchCab = booking == null ? false : true;

            if(IsSearchCab)
            {
                TotalHours = (Integer) booking.getRequiredHours();
                PickUpDateTime = (Date) booking.getPickUpDateTime();

                String DateFormat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

                Calendar cal  =  Calendar.getInstance();
                cal.setTime(PickUpDateTime);
                cal.add(Calendar.HOUR,TotalHours);
                BookingToDate =  cal.getTime();

                allVehicle = db.GetAllVehicles(PickUpDateTime,BookingToDate);
            }
            else
            {
                allVehicle = db.GetAllVehicles(null,null);
            }

            PopulateData(allVehicle);
            //VehilceListAdapter = new CompleteVehicleListAdapter(this,allVehicle);
            VehilceListAdapter = new ExpandableListAdapter(this,listDataHeader,listDataChild);

            expListView.setAdapter(VehilceListAdapter);
        }
        catch (IOException ioe)
        {

            throw new Error("Unable to create database");

        }
        catch(SQLException sqle)
        {

            throw sqle;

        }
        catch(RuntimeException re)
        {
            String s = re.getMessage();
            String g= s;

        }
        catch(Throwable ex)
        {
            String s = ex.getMessage();
            String g= s;
        }
        finally
        {
            db.close();
        }
    }

    private void InitViews()
    {
        expListView = (ExpandableListView) findViewById(R.id.lvExp) ;
        Button btnAddVehicle =  (Button) findViewById(R.id.ButtonAddVehicle);

        Button mHome = (Button) findViewById(R.id.ButtonHomeV);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View view,
                                        int groupPosition, long id)
            {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                setListViewHeight(parent, groupPosition);

               /* //ExpandableListView elistView = (ExpandableListView) view.findViewById(R.id.lvExp);
                //ExpandableListView Eview = VehilceListAdapter.getGroupView(0,true, parent, view.get);
                int desiredWidth = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.UNSPECIFIED);
                int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                //int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
                int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.EXACTLY);
                view.measure(widthMeasureSpec, heightMeasureSpec);
                return false;
                http://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
                */
                return false;
            }
        });


        expListView.setOnTouchListener(new View.OnTouchListener()
        {
               @Override
               public boolean onTouch(View view, MotionEvent motionEvent)
               {
                   int action = motionEvent.getAction();
                   switch (action) {
                       case MotionEvent.ACTION_DOWN:
                           // Disallow ScrollView to intercept touch events.
                           view.getParent().requestDisallowInterceptTouchEvent(true);
                           break;

                       case MotionEvent.ACTION_UP:
                           // Allow ScrollView to intercept touch events.
                           view.getParent().requestDisallowInterceptTouchEvent(false);
                           break;
                   }

                   // Handle ListView touch events.
                   view.onTouchEvent(motionEvent);
                   return true;
               }

           }
        );
                // Listview Group expanded listener
                expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        Toast.makeText(getApplicationContext(),
                                listDataHeader.get(groupPosition) + " Expanded",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
             /*   Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                        */
                Vehicle vehicle;
                vehicle = (Vehicle) listDataHeader.get(groupPosition);

                /*if (booking.getPickUpDateTime() != null && booking.getRequiredHours() != null)
                {
                    vehicle = (Vehicle) listDataHeader.get(groupPosition);
                }
                else
                {

                }
               */

                Intent mainIntent = new Intent(VehicleListActivity.this, AddBookingActivity.class);

                booking.setVehicleId(vehicle.getVehicleId());
                booking.setVehicle(vehicle);

                mainIntent.putExtra("RetriveBooking",booking);
                mainIntent.putExtra("ModifyBookingRecord",BookingTrackingId);

                startActivity(mainIntent);
                return false;
            }

        });

        btnAddVehicle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(VehicleListActivity.this,AddVehicleActivity.class);
                intent.putExtra("ModifyVehicleRecord", v.findViewById(R.id.lblListHeaderId)!= null?((TextView) v.findViewById(R.id.lblListHeaderId)).getText():null);
                startActivity(intent);
            }
        });

        mHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent mainIntent = new Intent(VehicleListActivity.this, HomeActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    private void setListViewHeight(ExpandableListView listView,int group)
    {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++)
        {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group)))
            {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++)
                {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private void PopulateData(ArrayList<Vehicle> vehiclelist)
    {
        listDataHeader = new ArrayList<Vehicle>();
        listDataChild = new HashMap<String, ArrayList<VehicleBooking>>();
        try
        {

                for (int i=0 ;i< vehiclelist.size();i++)
                {
                    Vehicle vehicle  = vehiclelist.get(i);
                    listDataHeader.add(vehiclelist.get(i));

                    ArrayList<VehicleBooking> vehicleBookingList = vehiclelist.get(i).getVehicleBookingList();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                    Calendar  cal =  Calendar.getInstance();
                    Date from  =  df.parse(df.format(cal.getTime()));
                    cal.add(Calendar.HOUR,5);
                    Date To  = df.parse(df.format(cal.getTime()));
                    Date d = new Date(2016,12,20);

                    listDataChild.put(vehiclelist.get(i).getVehicleNumber().toString(),vehicleBookingList);

                }

        }
        catch (Throwable ex)
        {
            String s = ex.getMessage();
            String g= s;
        }
    }
}

