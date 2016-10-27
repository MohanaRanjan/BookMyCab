package com.cabbooking.rkm.bookmycab;


import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
//import com.cabbooking.rkm.R;


public class CompleteListAdapter extends BaseAdapter
{
    private Activity mContext;
    private List<Users> mList;
    private LayoutInflater mLayoutInflater = null;

    public CompleteListAdapter(Activity context, List<Users> list)
    {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        CompleteListViewHolder viewHolder;

       if (convertView == null)
        {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        }
        else
        {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.mUserItem.setText(mList.get(position).getName());

        switch(mList.get(position).getUserRoleId())
        {
            case "A":
                viewHolder.mImageItem.setImageResource(R.drawable.ic_bluecircle);
                viewHolder.mRoleId.setText(mList.get(position).getId());
                break;
            case "H":
                viewHolder.mImageItem.setImageResource(R.drawable.ic_orangecircle);
                viewHolder.mRoleId.setText(mList.get(position).getId());
                break;
            case "B":
                viewHolder.mImageItem.setImageResource(R.drawable.ic_greencircle);
                viewHolder.mRoleId.setText(mList.get(position).getId());
                break;
            case "D":
                viewHolder.mImageItem.setImageResource(R.drawable.ic_redcircle);
                viewHolder.mRoleId.setText(mList.get(position).getId());
                break;
        }

        return v;
    }
}

class CompleteListViewHolder
{
    public TextView mUserItem;
    public TextView mRoleId;
    public ImageView mImageItem;

    public CompleteListViewHolder(View base)
    {
        mUserItem = (TextView) base.findViewById(R.id.username);
        mImageItem =  (ImageView) base.findViewById(R.id.imageViewCircle);
        mRoleId = (TextView) base.findViewById(R.id.roleId);
    }
}
