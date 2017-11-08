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


public class LoginCheckService extends IntentService {
    private static final String TAG = LoginCheckService.class.getName();

    //public static final String MESSAGE_TYPE = "message_type";

    public static final String USER_NO = "user_no";

    public static final String PASSWORD = "password";

    public static final String DEVICE_ID = "device_id";

    public static final String SERVICE_IP = "service_ip_address";

    public static final String SERVICE_PORT = "service_port";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Message_login"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Message_login"; // SOAP_ACTION

    //private static final String URL = "http://60.249.239.47:8920/service.asmx"; // 網址

    public LoginCheckService() {
        super("LoginCheckService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Handle");

        String user_no = intent.getStringExtra(USER_NO);
        String password = intent.getStringExtra(PASSWORD);
        String device_id = intent.getStringExtra(DEVICE_ID);
        String service_ip = intent.getStringExtra(SERVICE_IP);
        String service_port = intent.getStringExtra(SERVICE_PORT);

        String combine_url = "http://"+service_ip+":"+service_port+"/service.asmx";

        Log.d(TAG, "device_id = "+device_id);

        //Log.e(TAG, "combine_url = "+combine_url);

        /*if (intent.getAction().equals(Constants.ACTION.CHECK_EMPLOYEE_EXIST_ACTION)) {
            Log.i(TAG, "CHECK_EMPLOYEE_EXIST_ACTION");
        }*/

        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)
            /*

            DT053      ZD53215
            QT102     ZQ102503
            QT093     ZQ93224
            BT132      ZB132C38
            AT014      ZAT01412
            ET087      ZJ489111
            NT058      ZN589386
            NT014      ZA23663509
            ET006      ZET00611
            BT002      ZBT00207


             */


            //request.addProperty("message_type", "PO");
            request.addProperty("user_no", user_no);
            request.addProperty("password", password);
            request.addProperty("ime_code", device_id);


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
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                Log.e(TAG, str);
                Intent decryptDoneIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
                sendBroadcast(decryptDoneIntent);
            } else {
                Intent loginResultIntent;
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.d(TAG, String.valueOf(resultsRequestSOAP));

                if (String.valueOf(resultsRequestSOAP).indexOf("OK") > 0) {
                    Log.e(TAG, "ret = true");
                    loginResultIntent = new Intent(Constants.ACTION.CHECK_USER_LOGIN_COMPLETE);
                    sendBroadcast(loginResultIntent);
                } else {
                    Log.e(TAG, "ret = false");
                    loginResultIntent = new Intent(Constants.ACTION.CHECK_USER_LOGIN_FAIL);
                    sendBroadcast(loginResultIntent);
                }
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(StandardCharsets.UTF_8));
                    LoadAndParseXML(stream);
                } else {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(Charset.forName("UTF-8")));
                    LoadAndParseXML(stream);
                }*/

            }


        } catch (Exception e) {
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent decryptDoneIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
            sendBroadcast(decryptDoneIntent);
        }



    }
}
