package com.macauto.macautowarning.Service;

import android.app.IntentService;
import android.content.Intent;

import android.util.Log;


import com.macauto.macautowarning.Data.Constants;
import com.macauto.macautowarning.Data.GoOutData;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.macauto.macautowarning.HistoryFragment.goOutList;


public class GetWhoGoesOutService extends IntentService {

    public static final String TAG = "GetWhoGoesOutService";

    //public static final String SERVICE_IP = "172.17.8.146";

    //public static final String SERVICE_PORT = "8080";

    //public static final String SERVICE_IP = "service_ip_address";

    //public static final String SERVICE_PORT = "service_port";

    private static final String NAMESPACE = "http://it.macauto.com"; // 命名空間

    private static final String METHOD_NAME = "getDataBySelect"; // 方法名稱

    //private static final String SOAP_ACTION1 = "http://172.17.8.146:8080/WhoisoutSOAP/services/getDataBySelect"; // SOAP_ACTION
    private static String SOAP_ACTION1 = ""; // SOAP_ACTION

    private static int date_select = 0;

    public GetWhoGoesOutService() {
        super("GetWhoGoesOutService");
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

        //String account;
        //String device_id;

        //account = intent.getStringExtra("ACCOUNT");
        //device_id = intent.getStringExtra("DEVICE_ID");
        String service_ip = intent.getStringExtra("SERVICE_IP");
        //String service_port = intent.getStringExtra("SERVICE_PORT");
        String service_port_no2 = intent.getStringExtra("SERVICE_PORT_NO2");
        String dateSelect = intent.getStringExtra("DATE_SELECT");
        Log.e(TAG, "Get date : "+dateSelect);
        if (dateSelect != null) {
            date_select = Integer.valueOf(dateSelect);
        } else {
            date_select = 0;
        }

        String combine_url = "http://"+service_ip+":"+service_port_no2+"/WhoisoutSOAP/services/GetWhoGoesOutServiceImpl";

        if (intent.getAction().equals(Constants.ACTION.GET_WHOGOESOUT_LIST_ACTION)) {
            Log.i(TAG, "GET_WHOGOESOUT_LIST_ACTION");
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
            //request.addProperty("user_no", account);
            //request.addProperty("ime_code", device_id);
            request.addProperty("select", date_select);
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
                Log.e(TAG, "SoapFault = "+str);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, "result = "+String.valueOf(resultsRequestSOAP));
                //result.setText(String.valueOf(resultsRequestSOAP));

                goOutList.clear();

                for(int i=0;i<resultsRequestSOAP.getPropertyCount();i++){

                    //arraylist.add((String) primitive.getProperty(i));
                    //arraylist.add(primitive.getPropertyCount());

                    GoOutData goOutDatadata = new GoOutData();

                    System.out.println("ForLoop--------------"+resultsRequestSOAP.getProperty(i));
                    SoapObject data = (SoapObject) resultsRequestSOAP.getProperty(i);
                    Log.d(TAG, "id = "+data.getProperty("id").toString());
                    Log.d(TAG, "emp_name = "+data.getProperty("emp_name").toString());
                    Log.d(TAG, "emp_no = "+data.getProperty("emp_no").toString());
                    Log.d(TAG, "start_date = "+data.getProperty("start_date").toString());
                    Log.d(TAG, "end_date = "+data.getProperty("end_date").toString());
                    Log.d(TAG, "back_date = "+data.getProperty("end_date").toString());
                    Log.d(TAG, "reason = "+data.getProperty("reason").toString());
                    Log.d(TAG, "location = "+data.getProperty("location").toString());
                    Log.d(TAG, "car_type = "+data.getProperty("car_type").toString());
                    Log.d(TAG, "car_no = "+data.getProperty("car_no").toString());
                    Log.d(TAG, "car_or_moto = "+data.getProperty("car_or_moto").toString());


                    //Log.d(TAG, "app_sent_status = "+data.getProperty("app_sent_status").toString());

                    if (data.getProperty("id") == null || data.getProperty("id").equals("anyType{}")) {
                        Log.e(TAG, "set id = 0");
                        goOutDatadata.setId(0);
                    } else {
                        goOutDatadata.setId(Integer.valueOf(data.getProperty("id").toString()));
                    }

                    if (data.getProperty("emp_name") == null || data.getProperty("emp_name").toString().equals("anyType{}")) {
                        Log.e(TAG, "set emp_name = \"\"");
                        goOutDatadata.setEmp_name("");
                    } else {
                        goOutDatadata.setEmp_name(data.getProperty("emp_name").toString());
                    }

                    if (data.getProperty("emp_no") == null || data.getProperty("emp_no").toString().equals("anyType{}")) {
                        Log.e(TAG, "set emp_no = \"\"");
                        goOutDatadata.setEmp_no("");
                    } else {
                        goOutDatadata.setEmp_no(data.getProperty("emp_no").toString());
                    }

                    if (data.getProperty("start_date") == null || data.getProperty("start_date").toString().equals("anyType{}")) {
                        Log.e(TAG, "set start_date = \"\"");
                        goOutDatadata.setStart_date("");
                    } else {
                        goOutDatadata.setStart_date(data.getProperty("start_date").toString());
                    }

                    if (data.getProperty("end_date") == null || data.getProperty("end_date").toString().equals("anyType{}")) {
                        Log.e(TAG, "set end_date = \"\"");
                        goOutDatadata.setEnd_date("");
                    } else {
                        goOutDatadata.setEnd_date(data.getProperty("end_date").toString());
                    }

                    if (data.getProperty("back_date") == null || data.getProperty("back_date").toString().equals("anyType{}")) {
                        Log.e(TAG, "set back_date = \"\"");
                        goOutDatadata.setBack_date("");
                    } else {
                        goOutDatadata.setBack_date(data.getProperty("back_date").toString());
                    }

                    if (data.getProperty("reason") == null || data.getProperty("reason").toString().equals("anyType{}")) {
                        Log.e(TAG, "set reason = \"\"");
                        goOutDatadata.setReason("");
                    } else {
                        goOutDatadata.setReason(data.getProperty("reason").toString());
                    }

                    if (data.getProperty("location") == null || data.getProperty("location").toString().equals("anyType{}")) {
                        Log.e(TAG, "set location = \"\"");
                        goOutDatadata.setLocation("");
                    } else {
                        goOutDatadata.setLocation(data.getProperty("location").toString());
                    }

                    if (data.getProperty("car_type") == null || data.getProperty("car_type").toString().equals("anyType{}")) {
                        Log.e(TAG, "set car_type = \"\"");
                        goOutDatadata.setCar_type("");
                    } else {
                        goOutDatadata.setCar_type(data.getProperty("car_type").toString());
                    }

                    if (data.getProperty("car_no") == null || data.getProperty("car_no").toString().equals("anyType{}")) {
                        Log.e(TAG, "set car_no = \"\"");
                        goOutDatadata.setCar_no("");
                    } else {
                        goOutDatadata.setCar_no(data.getProperty("car_no").toString());
                    }

                    if (data.getProperty("car_or_moto") == null || data.getProperty("car_or_moto").toString().equals("anyType{}")) {
                        Log.e(TAG, "set car_or_moto = \"\"");
                        goOutDatadata.setCar_or_moto("");
                    } else {
                        goOutDatadata.setCar_or_moto(data.getProperty("car_or_moto").toString());
                    }

                    if (data.getProperty("app_sent_datetime") == null || data.getProperty("app_sent_datetime").toString().equals("anyType{}")) {
                        Log.e(TAG, "set app_sent_datetime = \"\"");
                        goOutDatadata.setApp_sent_datetime("");
                    } else {
                        goOutDatadata.setApp_sent_datetime(data.getProperty("app_sent_datetime").toString());
                    }

                    if (data.getProperty("app_sent_status") == null || data.getProperty("app_sent_status").toString().equals("anyType{}")) {
                        Log.e(TAG, "set app_sent_status = \"\"");
                        goOutDatadata.setApp_sent_status("");
                    } else {
                        goOutDatadata.setApp_sent_status(data.getProperty("app_sent_status").toString());
                    }

                    // System.out.println("is result null????????????"+arrayList.listIterator());
                    goOutList.add(goOutDatadata);

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
        Intent intent = new Intent(Constants.ACTION.GET_WHOGOESOUT_LIST_COMPLETE);
        sendBroadcast(intent);
    }


}
