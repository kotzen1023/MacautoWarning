package com.macauto.macautowarning;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.macauto.macautowarning.data.Constants;
import com.macauto.macautowarning.data.GoOutAdapter;
import com.macauto.macautowarning.data.GoOutData;
import com.macauto.macautowarning.data.HistoryAdapter;
import com.macauto.macautowarning.data.HistoryItem;

import com.macauto.macautowarning.service.GetMessageService;
import com.macauto.macautowarning.service.GetWhoGoesOutService;
import com.macauto.macautowarning.service.UpdateReadStatusService;


import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.MODE_PRIVATE;
import static com.macauto.macautowarning.MainMenu.item_lines;
import static com.macauto.macautowarning.MainMenu.message_type_select;
import static com.macauto.macautowarning.MainMenu.message_type_string;
import static com.macauto.macautowarning.MainMenu.multi_lines;

public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getName();

    private Context context;
    private ListView listView;
    //ProgressDialog loadDialog = null;
    ProgressBar progressBar = null;

    //public ArrayAdapter<Spanned> arrayAdapter = null;
    public static ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();
    public static ArrayList<HistoryItem> typeSortedList = new ArrayList<>();
    public static ArrayList<HistoryItem> sortedNotifyList = new ArrayList<>();
    public HistoryAdapter historyAdapter;
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
    //private static int select_item_index = 0;

    private static String service_ip_address;
    private static String service_port;
    private static String service_port_no2;

    //private Spinner typeSpinner;
    public ArrayAdapter<String> typeAdapter;

    private static ArrayList<String> typeList = new ArrayList<>();

    public static ArrayList<GoOutData> goOutList = new ArrayList<>();
    public GoOutAdapter goOutAdapter;
    RelativeLayout relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.history_fragment, container, false);

        relativeLayout = view.findViewById(R.id.list_container);

        context = getContext();

        if (context != null) {
            pref = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
            account = pref.getString("ACCOUNT", "");
            device_id = pref.getString("WIFIMAC", "");
            service_ip_address = pref.getString("DEFAULT_SERVICE_ADDRESS", "60.249.239.47");
            service_port = pref.getString("DEFAULT_SERVICE_PORT", "9571");
            service_port_no2 = pref.getString("DEFAULT_SERVICE_PORT_NO2", "9572");
        } else {
            account = "";
            device_id = "";
            service_ip_address = "60.249.239.47";
            service_port = "9571";
            service_port_no2 = "9572";
        }



        IntentFilter filter;

        listView =  view.findViewById(R.id.listViewHistory);

        Spinner typeSpinner = view.findViewById(R.id.spinnerType);

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

                            progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleLarge);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
                            params.addRule(RelativeLayout.CENTER_IN_PARENT);
                            relativeLayout.addView(progressBar,params);
                            progressBar.setVisibility(View.VISIBLE);  //To show ProgressBar
                            //progressBar.setVisibility(View.GONE);

                            /*loadDialog = new ProgressDialog(context);
                            loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            loadDialog.setTitle(getResources().getString(R.string.loding));
                            loadDialog.setIndeterminate(false);
                            loadDialog.setCancelable(false);
                            Log.e(TAG, "loadDialog.show 1");
                            loadDialog.show();*/
                        }

                        if (item_lines != null) {
                            item_lines.setVisible(false);
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

                        if (item_lines != null) {
                            item_lines.setVisible(true);
                        }
                    }
                } else {
                    goOutList.clear();
                    if (goOutAdapter != null) {
                        goOutAdapter.notifyDataSetChanged();
                        goOutAdapter = null;
                    }

                    Intent intent = new Intent(Constants.ACTION.GET_ORIGINAL_LIST_ACTION);
                    context.sendBroadcast(intent);

                    if (item_lines != null) {
                        item_lines.setVisible(true);
                    }
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

                        if (historyAdapter != null)
                            historyAdapter.notifyDataSetChanged();


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

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.SOAP_CONNECTION_FAIL)) {
                        Log.d(TAG, "receive SOAP_CONNECTION_FAIL");

                        progressBar.setVisibility(View.GONE);

                        toast(getResources().getString(R.string.soap_connection_failed));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SOCKET_TIMEOUT)) {
                        Log.d(TAG, "receive ACTION_SOCKET_TIMEOUT");

                        progressBar.setVisibility(View.GONE);

                        toast(getResources().getString(R.string.connection_timeout));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION)) {
                        Log.d(TAG, "receive brocast !");

                        if (message_type_select == typeList.size() - 1) {
                            Intent gointent = new Intent(context, GetWhoGoesOutService.class);
                            gointent.setAction(Constants.ACTION.GET_WHOGOESOUT_LIST_ACTION);
                            gointent.putExtra("DATE_SELECT", "0");
                            gointent.putExtra("ACCOUNT", account);
                            gointent.putExtra("DEVICE_ID", device_id);
                            gointent.putExtra("SERVICE_IP", service_ip_address);
                            gointent.putExtra("SERVICE_PORT_NO2", service_port_no2);
                            context.startService(gointent);
                        } else {


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

                            if (historyAdapter != null)
                                historyAdapter.notifyDataSetChanged();
                            Log.d(TAG, "size = "+historyItemArrayList.size());
                        }


                        //loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);

                        int badgeCount = 0;
                        for (int i = 0; i < historyItemArrayList.size(); i++) {
                            if (!historyItemArrayList.get(i).isRead_sp()) {
                                badgeCount++;
                            }
                        }

                        ShortcutBadger.applyCount(context, badgeCount);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE)) {
                        if (multi_lines) {
                            historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
                            listView.setAdapter(historyAdapter);
                        } else {
                            historyAdapter = new HistoryAdapter(context, R.layout.history_item_single, sortedNotifyList);
                            listView.setAdapter(historyAdapter);
                        }



                        typeSortedList.clear();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_ORIGINAL_LIST_ACTION)) {
                        if (multi_lines) {
                            historyAdapter = new HistoryAdapter(context, R.layout.history_item, historyItemArrayList);
                            listView.setAdapter(historyAdapter);
                        } else {
                            historyAdapter = new HistoryAdapter(context, R.layout.history_item_single, historyItemArrayList);
                            listView.setAdapter(historyAdapter);
                        }


                        sortedNotifyList.clear();
                        typeSortedList.clear();

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_TYPE_LIST_ACTION)) {

                        if (multi_lines) {
                            historyAdapter = new HistoryAdapter(context, R.layout.history_item, typeSortedList);
                            listView.setAdapter(historyAdapter);
                        } else {
                            historyAdapter = new HistoryAdapter(context, R.layout.history_item_single, typeSortedList);
                            listView.setAdapter(historyAdapter);
                        }



                        sortedNotifyList.clear();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_WHOGOESOUT_LIST_COMPLETE)) {
                        Log.d(TAG, "receive brocast GET_WHOGOESOUT_LIST_COMPLETE!");

                        goOutAdapter = new GoOutAdapter(context, R.layout.goout_list_item, goOutList);
                        listView.setAdapter(goOutAdapter);

                        //loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);

                        if (goOutList.size() == 0) {
                            toast(getResources().getString(R.string.whogoesout_list_empty));
                        }
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_MESSAGE_LIST_CLEAR)) {
                        Log.d(TAG, "receive brocast GET_MESSAGE_LIST_CLEAR!");
                        historyItemArrayList.clear();
                        if (historyAdapter != null) {
                            historyAdapter.notifyDataSetChanged();
                        }


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_MESSAGE_DATA)) {
                        Log.d(TAG, "receive brocast GET_MESSAGE_DATA!");

                        if (intent.getExtras() != null) {
                            HistoryItem item = new HistoryItem();
                            item.setMsg_id(intent.getExtras().getString("message_id"));
                            item.setMsg_code(intent.getExtras().getString("message_code"));
                            item.setMsg_title(intent.getExtras().getString("message_title"));
                            item.setMsg_content(intent.getExtras().getString("message_content"));
                            item.setAnnounce_date(intent.getExtras().getString("announce_date"));
                            item.setInternal_doc_no(intent.getExtras().getString("internal_doc_no"));
                            item.setInternal_part_no(intent.getExtras().getString("internal_part_no"));
                            item.setInternal_model_no(intent.getExtras().getString("internal_model_no"));
                            item.setInternal_machine_no(intent.getExtras().getString("internal_machine_no"));
                            item.setInternal_plant_no(intent.getExtras().getString("internal_plant_no"));
                            item.setAnnouncer(intent.getExtras().getString("announcer"));
                            item.setIme_code(intent.getExtras().getString("ime_code"));
                            item.setRead_sp(intent.getExtras().getBoolean("read_sp"));


                            historyItemArrayList.add(item);
                        }


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_LINES_CHANGE)) {
                        Log.d(TAG, "receive brocast ACTION_LINES_CHANGE!");

                        if (multi_lines) {
                            if (message_type_select != 6) { //not go out
                                if (message_type_select == 0) {
                                    historyAdapter = new HistoryAdapter(context, R.layout.history_item, historyItemArrayList);
                                    listView.setAdapter(historyAdapter);
                                } else {
                                    historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
                                    listView.setAdapter(historyAdapter);
                                }
                            }
                        } else {
                            if (message_type_select != 6) { //not go out
                                if (message_type_select == 0) {
                                    historyAdapter = new HistoryAdapter(context, R.layout.history_item_single, historyItemArrayList);
                                    listView.setAdapter(historyAdapter);
                                } else {
                                    historyAdapter = new HistoryAdapter(context, R.layout.history_item_single, sortedNotifyList);
                                    listView.setAdapter(historyAdapter);
                                }
                            }
                        }



                    }
                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.SOAP_CONNECTION_FAIL);
            filter.addAction(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            filter.addAction(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION);
            filter.addAction(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE);
            filter.addAction(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE);
            filter.addAction(Constants.ACTION.GET_ORIGINAL_LIST_ACTION);
            filter.addAction(Constants.ACTION.GET_TYPE_LIST_ACTION);
            filter.addAction(Constants.ACTION.GET_WHOGOESOUT_LIST_COMPLETE);
            filter.addAction(Constants.ACTION.GET_MESSAGE_LIST_CLEAR);
            filter.addAction(Constants.ACTION.GET_MESSAGE_DATA);
            filter.addAction(Constants.ACTION.ACTION_LINES_CHANGE);
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

            progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            relativeLayout.addView(progressBar,params);
            progressBar.setVisibility(View.VISIBLE);  //To show ProgressBar

            /*loadDialog = new ProgressDialog(context);
            loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadDialog.setTitle(getResources().getString(R.string.loding));
            loadDialog.setIndeterminate(false);
            loadDialog.setCancelable(false);
            Log.e(TAG, "loadDialog.show 2");
            loadDialog.show();*/
        }



        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");



        if (message_type_select == 0) {
            historyItemArrayList.clear();
            historyAdapter.notifyDataSetChanged();
        }




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
