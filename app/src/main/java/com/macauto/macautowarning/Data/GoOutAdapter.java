package com.macauto.macautowarning.Data;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import com.macauto.macautowarning.R;

import java.util.ArrayList;


public class GoOutAdapter extends ArrayAdapter<GoOutData> {
    public static final String TAG = GoOutAdapter.class.getName();
    private Context context;
    private LayoutInflater inflater = null;

    private int layoutResourceId;
    private ArrayList<GoOutData> items = new ArrayList<>();
    //public static SparseBooleanArray mSparseBooleanArray;
    //private static int contact_count = 0;
    //private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s", Locale.getDefault());
    //private SimpleDateFormat AMBIENT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    //private String current_date;

    public GoOutAdapter(Context context, int textViewResourceId,
                          ArrayList<GoOutData> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.layoutResourceId = textViewResourceId;
        this.items = objects;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mSparseBooleanArray = new SparseBooleanArray();


        //current_date = DATE_FORMAT.format(new Date());

        //Calendar calendar = Calendar.getInstance();
        //calendar.get(Calendar.MONTH);
        //Log.e(TAG, "month = ", calendar.get(Calendar.MONTH));

        //Log.e(TAG, "date ===> "+DATE_FORMAT.format(new Date()));
        /*Calendar calander = Calendar.getInstance();
        int cDay = calander.get(Calendar.DAY_OF_MONTH);
        int week = calander.get(Calendar.DAY_OF_WEEK);
        cMonth = calander.get(Calendar.MONTH) + 1;
        cYear = calander.get(Calendar.YEAR);
        selectedMonth = "" + cMonth;
        selectedYear = "" + cYear;
        cHour = calander.get(Calendar.HOUR);
        cMinute = calander.get(Calendar.MINUTE);
        cSecond = calander.get(Calendar.SECOND);*/
    }

    public int getCount() {

        return items.size();

    }

    public GoOutData getItem(int position)
    {
        return items.get(position);
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        //Log.e(TAG, "getView = " + position);
        View view;
        ViewHolder holder;


        if (convertView == null || convertView.getTag() == null) {
            /*LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.jid = (TextView) convertView.findViewById(R.id.contact_jid);
            holder.avatar = (ImageView) convertView.findViewById(R.id.contact_icon);
            convertView.setTag(holder);*/
            view = inflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(view);
            view.setTag(holder);


        } else {
            view = convertView;
            //Log.e(TAG, "here!");
            holder = (ViewHolder) view.getTag();
        }


        if (position < getCount()) {

            GoOutData item = items.get(position);

            //2017-09-29T16:28:59+08:00


            if (item != null) {

                //String date = item.getStart_date().substring(0, 10);





                /*String splitter[] = item.getStart_date().split(" ");


                //holder.date.setText(end_date);
                if (current_date.substring(0, 10).equals(splitter[0])) {
                    holder.date.setText(context.getResources().getString(R.string.date_today));

                } else {
                    holder.date.setText(splitter[0]);
                }*/


                String indexWithName = String.format("%4d  "+item.getEmp_name(), position+1);
                holder.name.setText(indexWithName);

                holder.reason.setText(item.getReason());
                holder.location.setText(item.getLocation());
                //holder.date.setText(date);
            }
        }


        return view;
    }

    private class ViewHolder {
        //ImageView icon;
        //TextView jid;
        //ImageView action;
        //TextView day;
        TextView name;
        TextView reason;
        TextView location;
        //TextView date;
        //CheckBox ckbox;

        private ViewHolder(View view) {

            //this.action = (ImageView) view.findViewById(R.id.goout_show_header);
            this.name = view.findViewById(R.id.goout_show_name);
            this.reason = view.findViewById(R.id.goout_show_msg);
            this.location = view.findViewById(R.id.goout_show_location);
            //this.msg = (TextView) view.findViewById(R.id.history_msg);
            //this.date = (TextView) view.findViewById(R.id.history_time);
        }


    }
}
