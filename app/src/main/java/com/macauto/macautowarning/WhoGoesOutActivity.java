package com.macauto.macautowarning;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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


public class WhoGoesOutActivity extends AppCompatActivity {
    private static final String TAG = WhoGoesOutActivity.class.getName();

    private Context context;
    private ListView listView;
    ProgressDialog loadDialog = null;

    //private Spinner typeSpinner;
    //public ArrayAdapter<String> typeAdapter;

    //private static ArrayList<String> typeList = new ArrayList<>();

    private static int searchChoose = 0;

    public static ArrayList<GoOutData> goOutList = new ArrayList<>();
    public static GoOutAdapter goOutAdapter;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goout_list);

        context = getBaseContext();

        Window window;

        listView = findViewById(R.id.listViewGoOutList);



        ActionBar actionBar = getSupportActionBar();
        //int color = 0xff3964f4;

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            actionBar.setTitle(getResources().getString(R.string.whogoesout_title));
        }

        window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background)));
        } else
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_background, getTheme())));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color_menu_classic));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color_menu_classic, getTheme()));
        }

        /*typeSpinner = findViewById(R.id.spinnerGoOutType);

        typeList.clear();
        typeList.add("今天");
        typeList.add("最近7日");
        typeList.add("最近15日");
        typeList.add("最近30日");

        typeAdapter = new ArrayAdapter<>(context, R.layout.myspinner, typeList);
        typeSpinner.setAdapter(typeAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchChoose = position;
                Log.i(TAG, "select "+searchChoose);

                Intent intent = new Intent(context, GetWhoGoesOutService.class);
                intent.putExtra("DATE_SELECT", String.valueOf(position));
                intent.setAction(Constants.ACTION.GET_WHOGOESOUT_LIST_ACTION);
                context.startService(intent);

                loadDialog = new ProgressDialog(WhoGoesOutActivity.this);
                loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loadDialog.setTitle("Loading...");
                loadDialog.setIndeterminate(false);
                loadDialog.setCancelable(false);

                loadDialog.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoOutData item = goOutAdapter.getItem(position);

                /*if (item.isRead_sp()) {
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
                }*/


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
            }
        });


        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_WHOGOESOUT_LIST_COMPLETE)) {
                    Log.d(TAG, "receive brocast GET_MESSAGE_LIST_COMPLETE!");

                    goOutAdapter = new GoOutAdapter(context, R.layout.goout_list_item, goOutList);
                    listView.setAdapter(goOutAdapter);
                    loadDialog.dismiss();

                    if (goOutList.size() == 0) {
                        toast(getResources().getString(R.string.whogoesout_list_empty));
                    }
                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.GET_WHOGOESOUT_LIST_COMPLETE);
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }

        Intent intent = new Intent(context, GetWhoGoesOutService.class);
        intent.putExtra("DATE_SELECT", String.valueOf(searchChoose));
        intent.setAction(Constants.ACTION.GET_WHOGOESOUT_LIST_ACTION);
        context.startService(intent);

        loadDialog = new ProgressDialog(WhoGoesOutActivity.this);
        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadDialog.setTitle(getResources().getString(R.string.loding));
        loadDialog.setIndeterminate(false);
        loadDialog.setCancelable(false);

        loadDialog.show();

        /*Intent intent = new Intent(context, GetWhoGoesOutService.class);
        intent.setAction(Constants.ACTION.GET_WHOGOESOUT_LIST_ACTION);
        context.startService(intent);

        loadDialog = new ProgressDialog(context);
        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadDialog.setTitle("Loading...");
        loadDialog.setIndeterminate(false);
        loadDialog.setCancelable(false);

        loadDialog.show();*/
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        goOutList.clear();
        goOutAdapter.notifyDataSetChanged();

        if (isRegister && mReceiver != null) {
            try {
                context.unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            isRegister = false;
            mReceiver = null;
        }

        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }
    @Override
    public void onResume() {

        Log.i(TAG, "onResume");

        if (goOutAdapter != null)
            goOutAdapter.notifyDataSetChanged();

        super.onResume();
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(Login.this, TopMenu.class);
        //startActivity(intent);
        finish();
    }
}
