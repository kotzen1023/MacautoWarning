package com.macauto.macautowarning;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ShortcutManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;


import com.macauto.macautowarning.Data.Constants;
import com.macauto.macautowarning.Data.HistoryAdapter;
import com.macauto.macautowarning.Data.HistoryItem;

import com.macauto.macautowarning.Service.GetMessageService;
import com.macauto.macautowarning.Service.UpdateReadStatusService;


import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getName();

    private Context context;
    private ListView listView;
    ProgressDialog loadDialog = null;

    //public ArrayAdapter<Spanned> arrayAdapter = null;
    public static ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();
    public static ArrayList<HistoryItem> sortedNotifyList = new ArrayList<>();
    public static HistoryAdapter historyAdapter;
    //private ChangeListener changeListener = null;
    //private Connection connection;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private Spanned[] history;
    //private static boolean isRegisterChangeListener = false;

    static SharedPreferences pref ;
    //static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";

    private static String account;
    private static String device_id;
    private static int select_item_index = 0;

    private static String service_ip_address;
    private static String service_port;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.history_fragment, container, false);

        context = getContext();

        pref = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        account = pref.getString("ACCOUNT", "");
        device_id = pref.getString("WIFIMAC", "");
        service_ip_address = pref.getString("DEFAULT_SERVICE_ADDRESS", "60.249.239.47");
        service_port = pref.getString("DEFAULT_SERVICE_PORT", "9571");


        IntentFilter filter;

        listView = (ListView) view.findViewById(R.id.listViewHistory);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryItem item = historyAdapter.getItem(position);

                if (item.isRead_sp()) {
                    Log.d(TAG, "read sp true");
                } else {
                    item.setRead_sp(true);

                    Intent intent = new Intent(context, UpdateReadStatusService.class);
                    intent.setAction(Constants.ACTION.UPDATE_MESSAGE_READ_SP_ACTION);
                    intent.putExtra("ACCOUNT", account);
                    intent.putExtra("DEVICE_ID", device_id);
                    intent.putExtra("MESSAGE_ID", item.getMsg_id());
                    intent.putExtra("service_ip_address", service_ip_address);
                    intent.putExtra("service_port", service_port);
                    context.startService(intent);
                }


                if (item != null) {
                    Intent intent = new Intent(context, HistoryShow.class);
                    intent.putExtra("HISTORY_MSG_ID", item.getMsg_id());
                    intent.putExtra("HISTORY_MSG_CODE", item.getMsg_code());
                    intent.putExtra("HISTORY_MSG_TITLE", item.getMsg_title());
                    intent.putExtra("HISTORY_MSG_CONTENT", item.getMsg_content());
                    intent.putExtra("HISTORY_ANNOUNCE_DATE", item.getAnnounce_date());
                    intent.putExtra("HISTORY_INTERNAL_DOC_NO", item.getInternal_doc_no());
                    intent.putExtra("HISTORY_INTERNAL_PART_NO", item.getInternal_part_no());
                    intent.putExtra("HISTORY_INTERNAL_MODEL_NO", item.getInternal_model_no());
                    intent.putExtra("HISTORY_INTERNAL_MACHINE_NO", item.getInternal_machine_no());
                    intent.putExtra("HISTORY_INTERNAL_PLANT_NO", item.getInternal_plant_no());
                    intent.putExtra("HISTORY_ANNOUNCER", item.getAnnouncer());
                    intent.putExtra("READ_SP", String.valueOf(item.isRead_sp()));
                    startActivity(intent);
                }
            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION)) {
                    Log.d(TAG, "receive brocast !");

                    //historyAdapter.notifyDataSetChanged();
                    Intent getintent = new Intent(context, GetMessageService.class);
                    getintent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
                    getintent.putExtra("ACCOUNT", account);
                    getintent.putExtra("DEVICE_ID", device_id);
                    getintent.putExtra("service_ip_address", service_ip_address);
                    getintent.putExtra("service_port", service_port);
                    context.startService(getintent);


                } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE)) {
                    Log.d(TAG, "receive brocast GET_MESSAGE_LIST_COMPLETE!");
                    historyAdapter = new HistoryAdapter(context, R.layout.history_item, historyItemArrayList);
                    listView.setAdapter(historyAdapter);
                    loadDialog.dismiss();

                    int badgeCount = 0;
                    for (int i=0; i<historyItemArrayList.size(); i++) {
                        if (!historyItemArrayList.get(i).isRead_sp()) {
                            badgeCount++;
                        }
                    }

                    ShortcutBadger.applyCount(context, badgeCount);
                }

                else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE)) {
                    historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
                    listView.setAdapter(historyAdapter);


                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION);
            filter.addAction(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE);
            filter.addAction(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE);
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }

        //run on create
        Intent intent = new Intent(context, GetMessageService.class);
        intent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
        intent.putExtra("ACCOUNT", account);
        intent.putExtra("DEVICE_ID", device_id);
        intent.putExtra("service_ip_address", service_ip_address);
        intent.putExtra("service_port", service_port);
        context.startService(intent);

        loadDialog = new ProgressDialog(context);
        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadDialog.setTitle("Loading...");
        loadDialog.setIndeterminate(false);
        loadDialog.setCancelable(false);

        loadDialog.show();

        //Intent intent = new Intent(context, GetMessageService.class);
        //intent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
        //intent.putExtra("ACCOUNT", account);
        //intent.putExtra("DEVICE_ID", device_id);
        //context.startService(intent);

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroy");

        if (isRegister && mReceiver != null) {
            try {
                context.unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            isRegister = false;
            mReceiver = null;
        }

        super.onDestroyView();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }
    @Override
    public void onResume() {

        Log.i(TAG, "onResume");

        /*if (sortedNotifyList.size() > 0) {
            historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
            listView.setAdapter(historyAdapter);
        } else {
            historyAdapter = new HistoryAdapter(context, R.layout.history_item, InitData.notifyList);
            listView.setAdapter(historyAdapter);
        }*/
        if (historyAdapter != null)
            historyAdapter.notifyDataSetChanged();





        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    /*public void toast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }*/
}
