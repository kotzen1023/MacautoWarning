package com.macauto.macautowarning;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.macauto.macautowarning.Data.Constants;
import com.macauto.macautowarning.Data.GoOutAdapter;
import com.macauto.macautowarning.Data.GoOutData;
import com.macauto.macautowarning.Data.HistoryAdapter;
import com.macauto.macautowarning.Data.HistoryItem;

import com.macauto.macautowarning.Service.GetMessageService;
import com.macauto.macautowarning.Service.GetWhoGoesOutService;
import com.macauto.macautowarning.Service.UpdateReadStatusService;


import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.MODE_PRIVATE;
import static com.macauto.macautowarning.MainMenu.message_type_select;
import static com.macauto.macautowarning.MainMenu.message_type_string;

public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getName();

    private Context context;
    private ListView listView;
    ProgressDialog loadDialog = null;

    //public ArrayAdapter<Spanned> arrayAdapter = null;
    public static ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();
    public static ArrayList<HistoryItem> typeSortedList = new ArrayList<>();
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
    private static String service_port_no2;

    private Spinner typeSpinner;
    public ArrayAdapter<String> typeAdapter;

    private static ArrayList<String> typeList = new ArrayList<>();

    public static ArrayList<GoOutData> goOutList = new ArrayList<>();
    public static GoOutAdapter goOutAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
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
        service_port_no2 = pref.getString("DEFAULT_SERVICE_PORT_NO2", "8080");

        IntentFilter filter;

        listView =  view.findViewById(R.id.listViewHistory);

        typeSpinner = view.findViewById(R.id.spinnerType);

        typeList.clear();
        typeList.add("全部");
        typeList.add("設備異常");
        typeList.add("品質異常");
        typeList.add("檢具異常");
        typeList.add("治具異常");
        typeList.add("缺料異常");
        typeList.add("外出人員");

        typeAdapter = new ArrayAdapter<>(context, R.layout.myspinner, typeList);
        typeSpinner.setAdapter(typeAdapter);

        //typeSpinner.setSelection(0);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "select "+position+" text = "+typeList.get(position));

                message_type_select = position;
                message_type_string = typeList.get(position);

                //sortedNotifyList.clear();
                typeSortedList.clear();

                if (position != 0) {

                    if (position == typeList.size() - 1) {
                        if (goOutList.size() > 0) {

                            goOutAdapter = new GoOutAdapter(context, R.layout.goout_list_item, goOutList);
                            listView.setAdapter(goOutAdapter);
                        } else {
                            Intent intent = new Intent(context, GetWhoGoesOutService.class);
                            intent.setAction(Constants.ACTION.GET_WHOGOESOUT_LIST_ACTION);
                            intent.putExtra("DATE_SELECT", "0");
                            intent.putExtra("ACCOUNT", account);
                            intent.putExtra("DEVICE_ID", device_id);
                            intent.putExtra("SERVICE_IP", service_ip_address);
                            //intent.putExtra("SERVICE_PORT", service_port);
                            intent.putExtra("SERVICE_PORT_NO2", service_port_no2);
                            context.startService(intent);

                            loadDialog = new ProgressDialog(context);
                            loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            loadDialog.setTitle(getResources().getString(R.string.loding));
                            loadDialog.setIndeterminate(false);
                            loadDialog.setCancelable(false);
                            Log.e(TAG, "loadDialog.show 1");
                            loadDialog.show();
                        }



                    } else {
                        goOutList.clear();
                        if (goOutAdapter != null) {
                            goOutAdapter.notifyDataSetChanged();
                            goOutAdapter = null;
                        }


                        for (int i = 0; i < historyItemArrayList.size(); i++) {
                            if (historyItemArrayList.get(i).getMsg_title() != null && historyItemArrayList.get(i).getMsg_title().contains(typeList.get(position))) {
                                typeSortedList.add(historyItemArrayList.get(i));
                            }
                        }

                        Intent intent = new Intent(Constants.ACTION.GET_TYPE_LIST_ACTION);
                        context.sendBroadcast(intent);
                    }
                } else {
                    goOutList.clear();
                    if (goOutAdapter != null) {
                        goOutAdapter.notifyDataSetChanged();
                        goOutAdapter = null;
                    }

                    Intent intent = new Intent(Constants.ACTION.GET_ORIGINAL_LIST_ACTION);
                    context.sendBroadcast(intent);
                }

                //meetingArrayAdapter = new MeetingArrayAdapter(context, R.layout.meeting_list_item, list);
                //AllFragment.resetAdapter(list);
                //AllFragment.listView.setAdapter(AllFragment.meetingArrayAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (message_type_select == typeList.size() - 1) {
                    GoOutData item = goOutAdapter.getItem(position);

                    if (item != null) {
                        Intent intent = new Intent(context, GoOutDataShow.class);
                        //intent.putExtra("ID", item.getId());
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
                        startActivity(intent);
                    }
                } else {
                    HistoryItem item = historyAdapter.getItem(position);

                    if (item != null) {

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


            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION)) {
                        Log.d(TAG, "receive brocast !");

                        if (message_type_select == 6) {
                            Intent gointent = new Intent(context, GetWhoGoesOutService.class);
                            gointent.setAction(Constants.ACTION.GET_WHOGOESOUT_LIST_ACTION);
                            gointent.putExtra("DATE_SELECT", "0");
                            gointent.putExtra("ACCOUNT", account);
                            gointent.putExtra("DEVICE_ID", device_id);
                            gointent.putExtra("service_ip_address", service_ip_address);
                            gointent.putExtra("service_port", service_port);
                            context.startService(gointent);
                        } else {

                            //historyAdapter.notifyDataSetChanged();
                            Intent getintent = new Intent(context, GetMessageService.class);
                            getintent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
                            getintent.putExtra("ACCOUNT", account);
                            getintent.putExtra("DEVICE_ID", device_id);
                            getintent.putExtra("service_ip_address", service_ip_address);
                            getintent.putExtra("service_port", service_port);
                            context.startService(getintent);
                        }


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE)) {
                        Log.d(TAG, "receive brocast GET_MESSAGE_LIST_COMPLETE!");

                        if (message_type_select == 0) {
                            historyAdapter = new HistoryAdapter(context, R.layout.history_item, historyItemArrayList);
                            listView.setAdapter(historyAdapter);
                        }


                        loadDialog.dismiss();

                        int badgeCount = 0;
                        for (int i = 0; i < historyItemArrayList.size(); i++) {
                            if (!historyItemArrayList.get(i).isRead_sp()) {
                                badgeCount++;
                            }
                        }

                        ShortcutBadger.applyCount(context, badgeCount);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE)) {
                        historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
                        listView.setAdapter(historyAdapter);

                        typeSortedList.clear();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_ORIGINAL_LIST_ACTION)) {
                        historyAdapter = new HistoryAdapter(context, R.layout.history_item, historyItemArrayList);
                        listView.setAdapter(historyAdapter);

                        sortedNotifyList.clear();
                        typeSortedList.clear();

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_TYPE_LIST_ACTION)) {
                        historyAdapter = new HistoryAdapter(context, R.layout.history_item, typeSortedList);
                        listView.setAdapter(historyAdapter);

                        sortedNotifyList.clear();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_WHOGOESOUT_LIST_COMPLETE)) {
                        Log.d(TAG, "receive brocast GET_WHOGOESOUT_LIST_COMPLETE!");

                        goOutAdapter = new GoOutAdapter(context, R.layout.goout_list_item, goOutList);
                        listView.setAdapter(goOutAdapter);

                        loadDialog.dismiss();

                        if (goOutList.size() == 0) {
                            toast(getResources().getString(R.string.whogoesout_list_empty));
                        }
                    }
                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION);
            filter.addAction(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE);
            filter.addAction(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE);
            filter.addAction(Constants.ACTION.GET_ORIGINAL_LIST_ACTION);
            filter.addAction(Constants.ACTION.GET_TYPE_LIST_ACTION);
            filter.addAction(Constants.ACTION.GET_WHOGOESOUT_LIST_COMPLETE);
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }

        //run on create

        if (message_type_select == 0) {

            Intent intent = new Intent(context, GetMessageService.class);
            intent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
            intent.putExtra("ACCOUNT", account);
            intent.putExtra("DEVICE_ID", device_id);
            intent.putExtra("service_ip_address", service_ip_address);
            intent.putExtra("service_port", service_port);
            context.startService(intent);

            loadDialog = new ProgressDialog(context);
            loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadDialog.setTitle(getResources().getString(R.string.loding));
            loadDialog.setIndeterminate(false);
            loadDialog.setCancelable(false);
            Log.e(TAG, "loadDialog.show 2");
            loadDialog.show();
        }



        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");

        typeList.clear();
        typeAdapter.notifyDataSetChanged();


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

        //if (typeAdapter != null)
        //    typeSpinner.setSelection(0);

        /*if (sortedNotifyList.size() > 0) {
            historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
            listView.setAdapter(historyAdapter);
        } else {
            historyAdapter = new HistoryAdapter(context, R.layout.history_item, InitData.notifyList);
            listView.setAdapter(historyAdapter);
        }*/
        //if (historyAdapter != null)
        //    historyAdapter.notifyDataSetChanged();





        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    public void toast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
