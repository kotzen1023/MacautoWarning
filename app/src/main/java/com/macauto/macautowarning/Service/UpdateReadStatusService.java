package com.macauto.macautowarning.Service;

import android.app.IntentService;

import android.content.Intent;

import android.util.Log;


import com.macauto.macautowarning.Data.Constants;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.macauto.macautowarning.HistoryFragment.historyItemArrayList;


public class UpdateReadStatusService extends IntentService {

    public static final String TAG = "UpdateReadStatusService";

    public static final String SERVICE_IP = "service_ip_address";

    public static final String SERVICE_PORT = "service_port";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Message_Update_Read_Status"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Message_Update_Read_Status"; // SOAP_ACTION

    //private static final String URL = "http://60.249.239.47:8920/service.asmx"; // 網址

    //private Context context;

    public UpdateReadStatusService() {
        super("UpdateReadStatusService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");



        /*pref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        name = pref.getString("NAME", "");
        account = pref.getString("ACCOUNT", "");
        alarm_interval = pref.getInt("ALARM_INTERVAL", 30);
        sync_option = pref.getInt("SYNC_SETTING", 0);*/

        //context = getApplicationContext();

        // = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.TAIWAN);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Handle");



        String account = intent.getStringExtra("ACCOUNT");
        String device_id = intent.getStringExtra("DEVICE_ID");
        String message_id = intent.getStringExtra("MESSAGE_ID");
        String service_ip = intent.getStringExtra(SERVICE_IP);
        String service_port = intent.getStringExtra(SERVICE_PORT);

        Log.d(TAG, "account = "+account+" device id = "+device_id+" message_id = "+message_id);
        String combine_url = "http://"+service_ip+":"+service_port+"/service.asmx";

        if (intent.getAction().equals(Constants.ACTION.UPDATE_MESSAGE_READ_SP_ACTION)) {
            Log.i(TAG, "UPDATE_MESSAGE_READ_SP_ACTION");
        }

        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)


            //request.addProperty("start_date", "");
            //request.addProperty("end_date", "");
            //request.addProperty("emp_no", account);
            //request.addProperty("room_no", "");
            //request.addProperty("doc_type", "PO");
            request.addProperty("message_id", message_id);
            request.addProperty("user_no", account);
            request.addProperty("ime_code", account); //device_id -> account
            //request.addProperty("meeting_room_name", "");
            //request.addProperty("subject_or_content", "");
            //request.addProperty("meeting_type_id", "");
            //request.addProperty("passWord", "sunnyhitest");

            // 擴充 SOAP 序列化功能為第11版

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true; // 設定為 .net 預設編碼

            envelope.setOutputSoapObject(request); // 設定輸出的 SOAP 物件


            // 建立一個 HTTP 傳輸層

            HttpTransportSE httpTransport = new HttpTransportSE(combine_url);
            httpTransport.debug = true; // 測試模式使用

            httpTransport.call(SOAP_ACTION1, envelope); // 設定 SoapAction 所需的標題欄位


            // 將 WebService 資訊轉為 DataTable
            if (envelope.bodyIn instanceof SoapFault) {
                String str= ((SoapFault) envelope.bodyIn).faultstring;
                Log.e(TAG, str);
            } else {
                //Intent loginResultIntent;
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.d(TAG, String.valueOf(resultsRequestSOAP));

                if (String.valueOf(resultsRequestSOAP).indexOf("OK") > 0) {
                    Log.e(TAG, "ret = true");
                    //loginResultIntent = new Intent(Constants.ACTION.CHECK_MANUFACTURER_LOGIN_COMPLETE);
                    //sendBroadcast(loginResultIntent);
                    int badgeCount = 0;
                    for (int i=0; i<historyItemArrayList.size(); i++) {
                        if (!historyItemArrayList.get(i).isRead_sp()) {
                            badgeCount++;
                        }
                    }

                    ShortcutBadger.applyCount(this, badgeCount);
                } else {
                    Log.e(TAG, "ret = false");
                    //loginResultIntent = new Intent(Constants.ACTION.CHECK_MANUFACTURER_LOGIN_FAIL);
                    //sendBroadcast(loginResultIntent);
                }
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(StandardCharsets.UTF_8));
                    LoadAndParseXML(stream);
                } else {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(Charset.forName("UTF-8")));
                    LoadAndParseXML(stream);
                }*/

            }

            //meetingArrayAdapter = new MeetingArrayAdapter(MainActivity.this, R.layout.list_item, meetingList);
            //listView.setAdapter(meetingArrayAdapter);


            //Intent meetingAddintent = new Intent(Constants.ACTION.MEETING_NEW_BROCAST);
            //context.sendBroadcast(meetingAddintent);
            //SoapObject bodyIn = (SoapObject) envelope.bodyIn; // KDOM 節點文字編碼

            //Log.e(TAG, bodyIn.toString());

            //DataTable dt = soapToDataTable(bodyIn);

        } catch (Exception e) {
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent decryptDoneIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
            sendBroadcast(decryptDoneIntent);
        }

        //MeetingAlarm.last_sync_setting = sync_option;
        //Intent decryptDoneIntent = new Intent(Constants.ACTION.GET_PERSONAL_MEETING_LIST_COMPLETE);
        //sendBroadcast(decryptDoneIntent);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

    }


}
