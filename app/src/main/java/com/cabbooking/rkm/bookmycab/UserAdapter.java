package com.cabbooking.rkm.bookmycab;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Ramakrishna math on 28-09-2016.
 */
public class UserAdapter extends ArrayAdapter<Users>
{
    //private Context context;
    //private ArrayList<Users> users = new ArrayList<Users>();
    private SQLiteDatabase db;
    public ArrayList<Users> items;
    public ArrayList<Users> filtered;
    private Context context;
    private HashMap<String, Integer> alphaIndexer;
    private String[] sections = new String[0];
    private Filter filter;
    private boolean enableSections;

    public UserAdapter(Context context,int resourceId,ArrayList<Users> items,boolean enableSections)
    {
        super(context,resourceId, items);
        this.filtered = items;
        this.items = (ArrayList<Users> )items.clone();
        this.context = context;
        //this.users = usersArrayList;


        this.context = context;
        this.filter = new RoleFilter();
        this.enableSections = enableSections;

        if(enableSections)
        {
            alphaIndexer = new HashMap<String, Integer>();
            for (int i = items.size() - 1; i >= 0; i--)
            {
                Users element = items.get(i);
                String firstChar = element.getUserRoleId();
               /* if(firstChar.charAt(0) > 'Z' || firstChar.charAt(0) < 'A')
                    firstChar = "@";*/
                alphaIndexer.put(firstChar, i);
            }

            Set<String> keys = alphaIndexer.keySet();
            Iterator<String> it = keys.iterator();
            ArrayList<String> keyList = new ArrayList<String>();
            while (it.hasNext())
            {
                keyList.add(it.next());
            }

            Collections.sort(keyList);
            sections = new String[keyList.size()];
            keyList.toArray(sections);
        }
    }
    /*
        public UserAdapter(Context context, ArrayList<Users> usersArrayList)
        {
            this.context = context;
            this.users = usersArrayList;
        }
    */
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null)
        {
            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.mobile, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(items.get(position).getName());

            TextView textViewId = (TextView) gridView
                    .findViewById(R.id.grid_item_idlabel);
            textViewId.setText(items.get(position).getId());

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String mobile = items.get(position).getName();

            if (mobile.equals("Admin"))
            {
                imageView.setImageResource(R.drawable.usermale);
            } else if (mobile.equals("HOI"))
            {
                imageView.setImageResource(R.drawable.usermale);
            } else if (mobile.equals("BookingRequester"))
            {
                imageView.setImageResource(R.drawable.usermale);
            } else
            {
                imageView.setImageResource(R.drawable.usermale);
            }

        } else
        {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public void notifyDataSetInvalidated()
    {
        //super.notifyDataSetInvalidated();

        if(enableSections)
        {
            for (int i = items.size() - 1; i >= 0; i--)
            {
                Users element = items.get(i);
                String firstChar = element.getUserRoleId();
               /*if(firstChar.charAt(0) > 'Z' || firstChar.charAt(0) < 'A')
                    firstChar = "@";*/

                alphaIndexer.put(firstChar, i);
            }

            Set<String> keys = alphaIndexer.keySet();
            Iterator<String> it = keys.iterator();
            ArrayList<String> keyList = new ArrayList<String>();
            while (it.hasNext())
            {
                keyList.add(it.next());
            }

            Collections.sort(keyList);
            sections = new String[keyList.size()];
            keyList.toArray(sections);
            super.notifyDataSetInvalidated();
        }

    }
    public int getPositionForSection(int section)
    {
        if(!enableSections) return 0;
        String letter = sections[section];

        return alphaIndexer.get(letter);
    }

    public int getSectionForPosition(int position)
    {
        if(!enableSections) return 0;
        int prevIndex = 0;
        for(int i = 0; i < sections.length; i++)
        {
            if(getPositionForSection(i) > position && prevIndex <= position)
            {
                prevIndex = i;
                break;
            }
            prevIndex = i;
        }
        return prevIndex;
    }

    public Object[] getSections()
    {
        return sections;
    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public Filter getFilter()
    {
        if(filter == null)
            filter = new RoleFilter();
        return filter;
    }

    private class RoleFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<Users> filt = new ArrayList<Users>();
                ArrayList<Users> lItems = new ArrayList<Users>();

                synchronized (this)
                {
                    lItems.addAll(items);
                }
                for(int i = 0, l = lItems.size(); i < l; i++)
                {
                    Users user = lItems.get(i);
                    if(user.getUserRoleId().toLowerCase().contains(constraint))
                        filt.add(user);
                }
                result.count = filt.size();
                result.values = filt;
            }
            else
            {
                synchronized(this)
                {
                    result.values = items;
                    result.count = items.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results)
        {
            // NOTE: this function is *always* called from the UI thread.
            filtered.clear();
            filtered.addAll((ArrayList<Users>)results.values);
            notifyDataSetChanged();
            //clear();
            /*for(int i = 0, l = filtered.size(); i < l; i++)
                add(filtered.get(i));*/
            //notifyDataSetInvalidated();
        }
    }
}
