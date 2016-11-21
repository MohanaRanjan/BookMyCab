package com.cabbooking.rkm.bookmycab;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ramakrishna Mission on 11-11-2016.
 */
public class CompleteBookingListAdapter  extends BaseAdapter
{
    private Activity mContext;
    private List<Booking> mList;
    private LayoutInflater mLayoutInflater = null;
    private DBHelper db;

    public CompleteBookingListAdapter(Activity context, List<Booking> list)
    {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        db = new DBHelper(context);
        db.getReadableDatabase();
    }

    @Override
    public int getCount()
    {
        return mList.size();
    }

    @Override
    public Object getItem(int pos)
    {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        CompleteBookingListViewHolder viewHolder;


        if (convertView == null)
        {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.booking_list_layout, null);
            viewHolder = new CompleteBookingListViewHolder(v);
            v.setTag(viewHolder);
        }
        else
        {
            viewHolder = (CompleteBookingListViewHolder) v.getTag();
        }
        //viewHolder.mUserBookingItem.setText(db.GetUser( mList.get(position).getBookingRequesterId()).getName());

        if (position % 2 == 1)
        {
            v.setBackgroundColor(Color.parseColor("#DBE9F4"));

        }
        else
        {
            v.setBackgroundColor(Color.parseColor("#F5F5DC"));
        }


        String DateFormat =  "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        try
        {
            switch( mList.get(position).getIsTravelComplete() == true ? "TravelComplete" : "TraveNotStarted")
            {
                case "TravelComplete":
                    viewHolder.mUserBookingItem.setText(mList.get(position).getBookingRequesterName());
                    viewHolder.mBookingTransactionId.setText(mList.get(position).getGlobalBookingTransactionId());
                    viewHolder.mTravelDate.setText(sdf.format( mList.get(position).getTravelCompleteDate()));
                    break;
                case "TraveNotStarted":
                    viewHolder.mUserBookingItem.setText(mList.get(position).getBookingRequesterName());
                    viewHolder.mBookingTransactionId.setText(mList.get(position).getGlobalBookingTransactionId());
                    viewHolder.mTravelDate.setText(sdf.format( mList.get(position).getPickUpDateTime()));
                    break;
            }
        }
        catch (Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
        return v;
    }


}

class CompleteBookingListViewHolder
{
    public TextView mUserBookingItem;
    public TextView mBookingTransactionId;
    public ImageView mImageItem;
    public TextView mTravelDate;

    public CompleteBookingListViewHolder(View base)
    {
        mUserBookingItem = (TextView) base.findViewById(R.id.BookingListUserName);
        //mImageItem =  (ImageView) base.findViewById(R.id.BookingListimageViewCircle);
        mBookingTransactionId = (TextView) base.findViewById(R.id.BookingListBookingTransactionId);
        mTravelDate = (TextView) base.findViewById(R.id.BookingListTravelDate);
    }
}

