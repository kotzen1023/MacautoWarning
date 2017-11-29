package com.macauto.macautowarning;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {
    private static final String TAG = SettingsFragment.class.getName();

    private Context context;

    static SharedPreferences pref ;
    static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";

    //private static String account;
    //private static String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.settings_fragment, container, false);

        ImageView imgLogout = view.findViewById(R.id.imageViewLogout);
        ImageView imgGoOut = view.findViewById(R.id.imageViewGoOut);

        TextView txtLogout = view.findViewById(R.id.textLogout);
        TextView txtGoOut = view.findViewById(R.id.textGoOut);

        context = getContext();

        pref = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        final String account = pref.getString("ACCOUNT", "");
        //String password = pref.getString("PASSWORD", "");

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder confirmdialog = new AlertDialog.Builder(view.getContext());
                confirmdialog.setIcon(R.drawable.ic_exit_to_app_black_48dp);
                confirmdialog.setTitle(view.getResources().getString(R.string.scm_more_logout_title));
                confirmdialog.setMessage(view.getResources().getString(R.string.scm_more_logout_descrypt));
                confirmdialog.setPositiveButton(view.getResources().getString(R.string.scm_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //unsubscribe topic
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(account);

                        pref = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("ACCOUNT", "");
                        editor.putString("PASSWORD", "");
                        editor.apply();

                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();

                    }
                });
                confirmdialog.setNegativeButton(view.getResources().getString(R.string.scm_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                confirmdialog.show();
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmdialog = new AlertDialog.Builder(view.getContext());
                confirmdialog.setIcon(R.drawable.ic_exit_to_app_black_48dp);
                confirmdialog.setTitle(view.getResources().getString(R.string.scm_more_logout_title));
                confirmdialog.setMessage(view.getResources().getString(R.string.scm_more_logout_descrypt));
                confirmdialog.setPositiveButton(view.getResources().getString(R.string.scm_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //unsubsvribe
                        FirebaseMessaging.getInstance().subscribeToTopic(account);

                        pref = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("ACCOUNT", "");
                        editor.putString("PASSWORD", "");
                        editor.apply();

                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();

                    }
                });
                confirmdialog.setNegativeButton(view.getResources().getString(R.string.scm_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                confirmdialog.show();
            }
        });

        imgGoOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), WhoGoesOutActivity.class);
                getActivity().startActivity(intent);

            }
        });

        txtGoOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), WhoGoesOutActivity.class);
                getActivity().startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroy");

        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }
}
