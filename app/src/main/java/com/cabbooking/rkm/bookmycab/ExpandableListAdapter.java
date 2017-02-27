package com.cabbooking.rkm.bookmycab;

import android.graphics.Color;
import android.widget.BaseExpandableListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * Created by Ramakrishna Mission on 28-11-2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter
{

    private Context _context;
    private List<Vehicle> _listDataHeader; // header titles
    private static int index = 1;
    // child data in format of header title, child title
    private HashMap<String, ArrayList<VehicleBooking>> _listDataChild;

    public ExpandableListAdapter(Context context, List<Vehicle> listDataHeader,
                                 HashMap<String,ArrayList<VehicleBooking>> listChildData)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        if(this._listDataChild != null &&
                this._listDataChild.get(this._listDataHeader.get(groupPosition).getVehicleNumber()).size() > 0)
        {
           /* return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);*/
            return this._listDataChild.get(this._listDataHeader.get(groupPosition).getVehicleNumber()).get(childPosititon);
        }
        else
        {
            return new VehicleBooking();
        }
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent)
    {

        VehicleBooking vehicleBooking = (VehicleBooking) getChild(groupPosition, childPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChildFrom  = (TextView) convertView
            .findViewById(R.id.lblListItemFrom);

        TextView txtListChildTo = (TextView) convertView
                .findViewById(R.id.lblListItemTo);

        TextView txtListChildDesc  = (TextView) convertView
                .findViewById(R.id.lblListItemDescription);

        TextView txtListChildCapacity = (TextView) convertView
                .findViewById(R.id.lblListItemCapacity);

        LinearLayout llBookingdates = (LinearLayout)convertView.findViewById(R.id.LLBookingDates);
        LinearLayout llVehicleDesc = (LinearLayout)convertView.findViewById(R.id.LLVehicleDescriptions);


        if(vehicleBooking.getBookedFrom() != null)
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String from  =  df.format(vehicleBooking.getBookedFrom());
            String To  = df.format(vehicleBooking.getBookedTo());

            //llVehicleDesc.setVisibility(View.INVISIBLE);
            llVehicleDesc.setVisibility(View.GONE);
            llBookingdates.setVisibility(View.VISIBLE);
            int i =  index % 2;
            if(i ==0 )
            {
                llBookingdates.setBackgroundColor(Color.parseColor("#CFD8DC"));
                index = index + 1;
            }
            else
            {
                llBookingdates.setBackgroundColor(Color.parseColor("#F2F0E6"));
                index = index + 1;
            }

            txtListChildFrom.setText(from);
            txtListChildTo.setText(To);


        }
        else
        {
            Vehicle vehicle = (Vehicle) this._listDataHeader.get(groupPosition);

            llVehicleDesc.setVisibility(View.VISIBLE);
            llBookingdates.setVisibility(View.GONE);

            llVehicleDesc.setBackgroundColor(Color.parseColor("#CFD8DC"));
            txtListChildDesc.setText(vehicle.getVehicleDescription());
            txtListChildCapacity.setText("Capacity  "+ vehicle.getCapacity());
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        int count = 0;
        try
        {

            if(this._listDataChild.size() > 0 &&
                    this._listDataChild.get(this._listDataHeader.get(groupPosition).getVehicleNumber()).size() > 0 )
            {
                count = this._listDataChild.get(this._listDataHeader.get(groupPosition).getVehicleNumber()).size();
                return count;
            }
            else
            {
                count = 1 ;
                return count;
            }
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{%s}",S));
        }

        return count;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent)
    {
        Vehicle vehicle = (Vehicle) getGroup(groupPosition);

        String HeaderString ="";

        HeaderString = vehicle.getVehicleNumber();
        HeaderString  =  HeaderString + " " +  vehicle.getVehicleModel().toString();
        HeaderString = HeaderString + "---Available---";

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        if(HeaderString != null)
        {
            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(HeaderString);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}