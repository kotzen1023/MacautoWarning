package com.macauto.macautowarning;


import android.app.SearchManager;
import android.content.Context;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;


import com.macauto.macautowarning.Data.Constants;

import static com.macauto.macautowarning.HistoryFragment.historyItemArrayList;
import static com.macauto.macautowarning.HistoryFragment.sortedNotifyList;

public class MainMenu extends AppCompatActivity {
    private static final String TAG = MainMenu.class.getName();

    private static final String TAB_1_TAG = "tab_1";
    private static final String TAB_2_TAG = "tab_2";

    public static MenuItem item_search;
    //public static Locale default_locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);




        Window window;
        //init folder
        //FileOperation.mqtt_init_folder();

        /*if (isMyServiceRunning(MqttMainService.class) && InitData.mqttServiceIntent != null) {
            Log.d(TAG, "service is running!");
            //stopService(InitData.mqttServiceIntent);
            //InitData.mqttServiceIntent = null;
        } else {
            Log.d(TAG, "start service!");
            InitData.mqttServiceIntent = new Intent(ConnectionDetails.this, MqttMainService.class);
            startService(InitData.mqttServiceIntent);
        }*/



        ActionBar actionBar = getSupportActionBar();
        //int color = 0xff3964f4;

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            actionBar.setTitle(getResources().getString(R.string.app_name));
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

        InitView();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");



        super.onDestroy();

    }

    private void InitView() {
        FragmentTabHost mTabHost;

        mTabHost =  findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_1_TAG),
        //        R.drawable.tab_indicator_gen, getResources().getString(R.string.scm_history_tab), R.drawable.ic_history_white_48dp), HistoryFragment.class, null);
        mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_1_TAG),
                R.drawable.tab_indicator_gen, "", R.drawable.mail), HistoryFragment.class, null);




        //mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_2_TAG),
        //        R.drawable.tab_indicator_gen, getResources().getString(R.string.scm_setting), R.drawable.ic_settings_white_48dp), SettingsFragment.class, null);
        mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_2_TAG),
                R.drawable.tab_indicator_gen, "", R.drawable.gear), SettingsFragment.class, null);






        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {


                switch (tabId) {
                    case "tab_1":
                        //if (item_clear != null)
                        //    item_clear.setVisible(true);
                        if (item_search != null)
                            item_search.setVisible(true);
                        break;
                    case "tab_2":
                        //if (item_clear != null)
                        //    item_clear.setVisible(false);
                        if (item_search != null)
                            item_search.setVisible(false);
                        break;

                    default:
                        break;

                }
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        item_search = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        if (searchManager != null) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


            //item_clear = menu.findItem(R.id.action_clear);

            //item_clear.setVisible(false);

            try {
                //SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_keeper));
                searchView.setOnQueryTextListener(queryListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Intent intent;
        /*switch (item.getItemId()) {
            case R.id.action_clear:

                Log.i(TAG, "item_clear");
                AlertDialog.Builder confirmdialog = new AlertDialog.Builder(MainMenu.this);
                confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                confirmdialog.setTitle(getResources().getString(R.string.scm_warning));
                confirmdialog.setMessage(getResources().getString(R.string.scm_clear_msg));
                confirmdialog.setPositiveButton(getResources().getString(R.string.scm_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        InitData.notifyList.clear();
                        clear_record();

                        historyAdapter.notifyDataSetChanged();

                    }
                });
                confirmdialog.setNegativeButton(getResources().getString(R.string.scm_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                confirmdialog.show();


                //Intent deleteIntent = new Intent(Constants.ACTION.MQTT_CLEAR_HISTORY);
                //sendBroadcast(deleteIntent);
                break;
        }*/
        return true;
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec,
                                         int resid, String string, int genresIcon) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
        v.setBackgroundResource(resid);
        //TextView tv = (TextView)v.findViewById(R.id.txt_tabtxt);
        ImageView img = v.findViewById(R.id.img_tabtxt);

        //tv.setText(string);
        img.setBackgroundResource(genresIcon);
        return spec.setIndicator(v);
    }




    final private android.support.v7.widget.SearchView.OnQueryTextListener queryListener = new android.support.v7.widget.SearchView.OnQueryTextListener() {
        //searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Intent intent;

            //ArrayList<MeetingListItem> list = new ArrayList<>();
            sortedNotifyList.clear();
            if (!newText.equals("")) {



                //ArrayList<PasswordKeeperItem> list = new ArrayList<PasswordKeeperItem>();
                for (int i = 0; i < historyItemArrayList.size(); i++) {
                    if (historyItemArrayList.get(i).getMsg_title() != null && historyItemArrayList.get(i).getMsg_title().contains(newText)) {
                        sortedNotifyList.add(historyItemArrayList.get(i));
                    } else if (historyItemArrayList.get(i).getMsg_content() != null && historyItemArrayList.get(i).getMsg_content().contains(newText)) {
                        sortedNotifyList.add(historyItemArrayList.get(i));
                    } else if (historyItemArrayList.get(i).getAnnounce_date() != null && historyItemArrayList.get(i).getAnnounce_date().contains(newText)) {
                        sortedNotifyList.add(historyItemArrayList.get(i));
                    }
                }

                //passwordKeeperArrayAdapter = new PasswordKeeperArrayAdapter(Password_Keeper.this, R.layout.passwd_keeper_browsw_item, list);
                //listView.setAdapter(passwordKeeperArrayAdapter);

            } else {
                //ArrayList<PasswordKeeperItem> list = new ArrayList<PasswordKeeperItem>();

                for (int i = 0; i < historyItemArrayList.size(); i++) {
                    //sortedNotifyList.add(historyItemArrayList.get(i));
                    sortedNotifyList.add(historyItemArrayList.get(i));

                }


                //passwordKeeperArrayAdapter = new PasswordKeeperArrayAdapter(Password_Keeper.this, R.layout.passwd_keeper_browsw_item, list);
                //listView.setAdapter(passwordKeeperArrayAdapter);
            }

            //meetingArrayAdapter = new MeetingArrayAdapter(context, R.layout.meeting_list_item, list);
            //AllFragment.resetAdapter(list);
            //AllFragment.listView.setAdapter(AllFragment.meetingArrayAdapter);
            intent = new Intent(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE);
            sendBroadcast(intent);


            return false;
        }
    };
}
