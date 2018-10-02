package com.macauto.macautowarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import java.util.Map;



public class GoOutDataShow extends Activity {
    private static final String TAG = GoOutDataShow.class.getName();

    //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ssZ", Locale.TAIWAN);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.gooutdata_show);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color_menu_classic));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color_menu_classic, getTheme()));
        }

        ListView listView =  findViewById(R.id.listViewGoOutDataShow);

        Intent getintent = getIntent();

        /*
        intent.putExtra("EMP_NO", item.getEmp_no());
                    intent.putExtra("EMP_NAME", item.getEmp_name());
                    intent.putExtra("START_DATE", item.getStart_date());
                    intent.putExtra("END_DATE", item.getEnd_date());
                    intent.putExtra("BACK_DATE", item.getBack_date());
                    intent.putExtra("REASON", item.getReason());
                    intent.putExtra("LOCATION", item.getLocation());
                    intent.putExtra("CAR_TYPE", item.getCar_type());
                    intent.putExtra("CAR_NO", item.getCar_no());
                    intent.putExtra("CAR_OR_MOTO", item.getCar_or_moto());
                    intent.putExtra("APP_SENT_DATETIME", item.getApp_sent_datetime());
                    intent.putExtra("APP_SENT_STATUS", item.getApp_sent_status());
         */
        String emp_no = getintent.getStringExtra("EMP_NO");
        String emp_name = getintent.getStringExtra("EMP_NAME");
        String start_date = getintent.getStringExtra("START_DATE");
        String end_date = getintent.getStringExtra("END_DATE");
        String back_date = getintent.getStringExtra("BACK_DATE");
        String reason = getintent.getStringExtra("REASON");
        String location = getintent.getStringExtra("LOCATION");
        String car_type = getintent.getStringExtra("CAR_TYPE");
        String car_no = getintent.getStringExtra("CAR_NO");
        String car_or_moto = getintent.getStringExtra("CAR_OR_MOTO");
        String app_sent_datetime = getintent.getStringExtra("APP_SENT_DATETIME");
        String app_sent_status = getintent.getStringExtra("APP_SENT_STATUS");



        Log.i(TAG, "emp_no = "+emp_no);
        Log.i(TAG, "emp_name = "+emp_name);
        Log.i(TAG, "start_date = "+start_date);
        Log.i(TAG, "end_date = "+end_date);
        Log.i(TAG, "back_date = "+back_date);
        Log.i(TAG, "reason = "+reason);
        Log.i(TAG, "location = "+location);
        Log.i(TAG, "car_type = "+car_type);
        Log.i(TAG, "car_no = "+car_no);
        Log.i(TAG, "car_or_moto = "+car_or_moto);
        Log.i(TAG, "app_sent_datetime = "+app_sent_datetime);
        Log.i(TAG, "app_sent_status = "+app_sent_status);







        //message = message.replace("#", "\n");

        /*Calendar c = Calendar.getInstance();

        Date start_date_compare = null;
        Date end_date_compare = null;

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.TAIWAN);
        try {
            start_date_compare = formatter.parse(start_date);
            end_date_compare = formatter.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/



        List<Map<String, String>> items = new ArrayList<>();

        /*Map<String, String> item1 = new HashMap<>();
        item1.put("show_header", getResources().getString(R.string.msg_id));
        item1.put("show_msg", msg_id);
        items.add(item1);

        Map<String, String> item2 = new HashMap<>();
        item2.put("show_header", getResources().getString(R.string.msg_code));
        item2.put("show_msg", msg_code);
        items.add(item2);*/



        Map<String, String> item1 = new HashMap<>();
        item1.put("show_header", getResources().getString(R.string.emp_no));
        item1.put("show_msg", emp_no);
        items.add(item1);

        Map<String, String> item2 = new HashMap<>();
        item2.put("show_header", getResources().getString(R.string.emp_name));
        item2.put("show_msg", emp_name);
        items.add(item2);

        String new_start_date = start_date.substring(0, 16);

        Map<String, String> item3 = new HashMap<>();
        item3.put("show_header", getResources().getString(R.string.start_date));
        item3.put("show_msg", new_start_date);
        items.add(item3);

        String new_end_date = end_date.substring(0, 16);

        Map<String, String> item4 = new HashMap<>();
        item4.put("show_header", getResources().getString(R.string.end_date));
        item4.put("show_msg", new_end_date);
        items.add(item4);

        if (back_date != null && !back_date.equals("")) {

            String new_back_date = back_date.substring(0, 19);

            Map<String, String> item5 = new HashMap<>();
            item5.put("show_header", getResources().getString(R.string.back_date));
            item5.put("show_msg", new_back_date);
            items.add(item5);
        }

        if (reason != null && !reason.equals("")) {
            Map<String, String> item6 = new HashMap<>();
            item6.put("show_header", getResources().getString(R.string.reason));
            item6.put("show_msg", reason);
            items.add(item6);
        }

        if (location != null && !location.equals("")) {
            Map<String, String> item7 = new HashMap<>();
            item7.put("show_header", getResources().getString(R.string.location));
            item7.put("show_msg", location);
            items.add(item7);
        }

        if (car_type != null) {

            Map<String, String> item7 = new HashMap<>();
            item7.put("show_header", getResources().getString(R.string.car_type));
            if (car_type.equals("C")) {
                item7.put("show_msg", getResources().getString(R.string.car_type_company));
            } else if (car_type.equals("P")) {
                item7.put("show_msg", getResources().getString(R.string.car_type_private));
            } else {
                item7.put("show_msg", car_type);
            }

            items.add(item7);
        }

        if (car_no != null) {
            Map<String, String> item8 = new HashMap<>();
            item8.put("show_header", getResources().getString(R.string.car_no));
            item8.put("show_msg", car_no);
            items.add(item8);
        }

        if (car_or_moto != null) {
            Map<String, String> item9 = new HashMap<>();
            item9.put("show_header", getResources().getString(R.string.car_or_moto));

            if (car_or_moto.equals("C")) {
                item9.put("show_msg", getResources().getString(R.string.car_or_moto_car));
            } else if (car_or_moto.equals("M")) {
                item9.put("show_msg", getResources().getString(R.string.car_or_moto_moto));
            } else {
                item9.put("show_msg", car_or_moto);
            }

            items.add(item9);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                items, R.layout.gooutdate_show_item, new String[]{"show_header", "show_msg"},
                new int[]{R.id.gooutdata_show_header, R.id.gooutdata_show_msg});
        listView.setAdapter(simpleAdapter);

        //Log.i(TAG, "item[1] = "+listView.getAdapter().);

        /*if (read_sp.equals("true")) {
            Log.d(TAG, "This message had been read.");
        } else {

            Intent intent = new Intent(HistoryShow.this, UpdateReadStatusService.class);
            intent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
            intent.putExtra("ACCOUNT", account);
            intent.putExtra("DEVICE_ID", device_id);
            intent.putExtra("DOC_NO", message);
            startService(intent);
        }*/

    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
