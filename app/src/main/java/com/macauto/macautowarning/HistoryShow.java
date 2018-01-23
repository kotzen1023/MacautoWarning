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



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class HistoryShow extends Activity {
    private static final String TAG = HistoryShow.class.getName();

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ssZ", Locale.TAIWAN);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.history_show);

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

        ListView listView = findViewById(R.id.listViewHistoryShow);

        Intent getintent = getIntent();


        String msg_id = getintent.getStringExtra("HISTORY_MSG_ID");
        String msg_code = getintent.getStringExtra("HISTORY_MSG_CODE");
        String msg_title = getintent.getStringExtra("HISTORY_MSG_TITLE");
        String msg_content = getintent.getStringExtra("HISTORY_MSG_CONTENT");
        String announce_date = getintent.getStringExtra("HISTORY_ANNOUNCE_DATE");
        String doc_no = getintent.getStringExtra("HISTORY_INTERNAL_DOC_NO");
        String part_no = getintent.getStringExtra("HISTORY_INTERNAL_PART_NO");
        String model_no = getintent.getStringExtra("HISTORY_INTERNAL_MODEL_NO");
        String machine_no = getintent.getStringExtra("HISTORY_INTERNAL_MACHINE_NO");
        String plant_no = getintent.getStringExtra("HISTORY_INTERNAL_PLANT_NO");
        String announcer = getintent.getStringExtra("HISTORY_ANNOUNCER");
        String read_sp = getintent.getStringExtra("READ_SP");



        Log.i(TAG, "msg_id = "+msg_id);
        Log.i(TAG, "msg_code = "+msg_code);
        Log.i(TAG, "msg_title = "+msg_title);
        Log.i(TAG, "msg_content = "+msg_content);
        Log.i(TAG, "announce_date = "+announce_date);
        Log.i(TAG, "doc_no = "+doc_no);
        Log.i(TAG, "part_no = "+part_no);
        Log.i(TAG, "model_no = "+model_no);
        Log.i(TAG, "machine_no = "+machine_no);
        Log.i(TAG, "plant_no = "+plant_no);
        Log.i(TAG, "announcer = "+announcer);
        Log.i(TAG, "read sp = "+read_sp);

        try {
            Date date = formatter.parse(announce_date);
            Log.e(TAG, "date string = "+date.toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getDefault());
            announce_date = sdf.format(date);
            //System.out.println(sdf.format(date)); //prints date in the format sdf
        } catch (ParseException e) {
            e.printStackTrace();
        }




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

        String new_title;
        if (msg_title == null) {
            new_title = msg_code;
        } else {
            new_title = msg_code + " " + msg_title;
        }

        Map<String, String> item3 = new HashMap<>();
        item3.put("show_header", getResources().getString(R.string.msg_title));
        item3.put("show_msg", new_title);
        items.add(item3);

        Map<String, String> item4 = new HashMap<>();
        item4.put("show_header", getResources().getString(R.string.msg_content));
        item4.put("show_msg", msg_content);
        items.add(item4);

        Map<String, String> item5 = new HashMap<>();
        item5.put("show_header", getResources().getString(R.string.msg_date));
        item5.put("show_msg", announce_date);
        items.add(item5);

        if (doc_no != null && !doc_no.equals("")) {
            Map<String, String> item6 = new HashMap<>();
            item6.put("show_header", getResources().getString(R.string.msg_doc_no));
            item6.put("show_msg", doc_no);
            items.add(item6);
        }

        if (part_no != null && !part_no.equals("")) {

            Map<String, String> item7 = new HashMap<>();
            item7.put("show_header", getResources().getString(R.string.msg_part_no));
            item7.put("show_msg", part_no);
            items.add(item7);
        }

        if (model_no != null && !model_no.equals("")) {
            Map<String, String> item8 = new HashMap<>();
            item8.put("show_header", getResources().getString(R.string.msg_model_no));
            item8.put("show_msg", model_no);
            items.add(item8);
        }

        if (machine_no != null && !machine_no.equals("")) {
            Map<String, String> item9 = new HashMap<>();
            item9.put("show_header", getResources().getString(R.string.msg_machine_no));
            item9.put("show_msg", machine_no);
            items.add(item9);
        }

        if (plant_no != null && !plant_no.equals("")) {
            Map<String, String> item10 = new HashMap<>();
            item10.put("show_header", getResources().getString(R.string.msg_plant_no));
            item10.put("show_msg", plant_no);
            items.add(item10);
        }

        if (announcer != null && !announcer.equals("")) {
            Map<String, String> item11 = new HashMap<>();
            item11.put("show_header", getResources().getString(R.string.msg_announcer));
            item11.put("show_msg", announcer);
            items.add(item11);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                items, R.layout.history_show_item, new String[]{"show_header", "show_msg"},
                new int[]{R.id.history_show_header, R.id.history_show_msg});
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
