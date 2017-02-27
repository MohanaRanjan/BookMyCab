package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class CompleteListActivity extends Activity implements View.OnClickListener
{
    private ListView mCompleteListView;
    private Button mAddItemToList;
    private Button mAddUser;
    private ArrayList<Users> mItems;
    private CompleteListAdapter mListAdapter;
    private static final int MIN = 0, MAX = 10000;
    private static  ArrayList<Users> cloneUserlist;

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        try
        {
            setContentView(R.layout.activity_complete_list);
            initViews();

            db = new DBHelper(this);
            db.createDataBase();
            db.openDataBase();

            mItems =  db.GetAllUsers();
            mListAdapter = new CompleteListAdapter(this, mItems);
            mCompleteListView.setAdapter(mListAdapter);

            ListView list = (ListView) findViewById(android.R.id.list);

            setListViewHeightBasedOnChildren(list);

        }
        catch (IOException ioe)
        {

            throw new Error("Unable to create database");

        }
        catch(SQLException sqle)
        {

            throw sqle;

        }
        catch(Exception ex)
        {
            String s  =  ex.getMessage();
            String d = s;
        }

        //fillAdapter(mItems);
    }
    private void fillAdapter(ArrayList<Users> mItems )
    {
        mListAdapter = new CompleteListAdapter(this, mItems);

        mCompleteListView.setAdapter(mListAdapter);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        //http://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
    }

    private void initViews()
    {
        mCompleteListView = (ListView) findViewById(android.R.id.list);
       // mAddItemToList = (Button) findViewById(R.id.showOnlyAdmin);
        mAddUser = (Button) findViewById(R.id.ButtonAddUser);

         Button mHome = (Button) findViewById(R.id.ButtonHomeUL);
       // mAddItemToList.setOnClickListener(this);


        mHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent mainIntent = new Intent(CompleteListActivity.this, HomeActivity.class);
                startActivity(mainIntent);
            }
        });


        mAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent mainIntent = new Intent(CompleteListActivity.this, AddUserActivity.class);
               // mainIntent.putExtra("NewRecord",((TextView) view.findViewById(R.id.roleId)).getText());
                startActivity(mainIntent);
            }
        });

        mCompleteListView.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l)
            {

                Intent mainIntent = new Intent(CompleteListActivity.this, AddUserActivity.class);
                mainIntent.putExtra("ModifyRecord",((TextView) v.findViewById(R.id.roleId)).getText());
                startActivity(mainIntent);
            }
        });

    }

    private void FilterByRole(String roleId)
    {
        // int randomVal = MIN + (int) (Math.random() * ((MAX - MIN) + 1));
        cloneUserlist = (ArrayList<Users>) mItems.clone();
        mItems.clear();
       //mListAdapter.notifyDataSetInvalidated();
        for (Integer i=0 ;i < cloneUserlist.size();i++)
        {
            if(cloneUserlist.get(i).getUserRoleId().contains(roleId))
            {
                mItems.add(cloneUserlist.get(i));
            }
        }
        // mItems.add(String.valueOf(randomVal));
        mListAdapter.notifyDataSetChanged();
    }



    @Override
    public void onClick(View v)
    {
        mCompleteListView.setAdapter(null);

        fillAdapter(mItems);

        switch (v.getId())
        {
            case R.id.imageViewBlueCircle:
                FilterByRole("A");
                break;
            case R.id.imageViewOrangeCircle:
                FilterByRole("H");
                break;
            case R.id.imageViewGreenCircle:
                FilterByRole("B");
                break;
            case R.id.imageViewRedCircle:
                FilterByRole("D");
                break;
        }
        mItems = (ArrayList<Users>) cloneUserlist.clone();
    }
}
