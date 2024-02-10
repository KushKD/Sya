package com.dit.himachal.rms.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dit.himachal.rms.R;
import com.dit.himachal.rms.enums.TaskType;
import com.dit.himachal.rms.generic.Generic_Async_Get;
import com.dit.himachal.rms.interfaces.AsyncTaskListenerObjectGet;
import com.dit.himachal.rms.modal.GetDataPojo;
import com.dit.himachal.rms.modal.OfflineDataModel;
import com.dit.himachal.rms.modal.UserAdvocate;
import com.dit.himachal.rms.presentation.CustomDialog;
import com.dit.himachal.rms.utilities.AppStatus;
import com.dit.himachal.rms.utilities.CommonUtils;
import com.dit.himachal.rms.utilities.Econstants;
import com.dit.himachal.rms.utilities.Preferences;
import com.doi.spinnersearchable.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import cmreliefdund.kushkumardhawan.com.instructions.MaterialTutorialActivity;
import cmreliefdund.kushkumardhawan.com.instructions.TutorialItem;

public class Login extends AppCompatActivity implements AsyncTaskListenerObjectGet {

    CustomDialog CD = new CustomDialog();
    EditText otp, mobile;
    SearchableSpinner role;
    Button login, reset, get_otp;
    private static final int REQUEST_CODE = 1234;


    private String  Global_roleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        LinearLayout login_form = findViewById(R.id.login_form);

        otp = (EditText) login_form.findViewById(R.id.otp);
        mobile = (EditText) login_form.findViewById(R.id.mobile);

        login = (Button) login_form.findViewById(R.id.login);
//
//        if(!Preferences.getInstance().loadTutorial){
//            loadTutorial();
//        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Mobile",mobile.getText().toString());
                if (mobile.getText().toString().equalsIgnoreCase("admin") && !mobile.getText().toString().isEmpty()) {
                    if (otp.getText().toString().equalsIgnoreCase("password")  && !otp.getText().toString().isEmpty()) {

                        UserAdvocate advocate = new UserAdvocate();
                        advocate.setUsername(mobile.getText().toString());
                        saveDataSharedPrefrences(advocate);

                        Intent mainIntent = new Intent(Login.this, MainActivity2.class);
                        Login.this.startActivity(mainIntent);
                        Login.this.finish();

                    } else {

                        CD.showDialog(Login.this, "Please Enter Password.");
                    }
                } else {
                    CD.showDialog(Login.this, "Please Enter a valid Username.");
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // PreventScreenshot.on(Login.this);

    }




    @Override
    protected void onStop() {
        // PreventScreenshot.on(Login.this);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        // PreventScreenshot.on(Login.this);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
        // PreventScreenshot.on(Login.this);
        super.onPause();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void loadTutorial() {

        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);
    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("Instant Updates", "Get Instant Updates of Cases, Notices, Orders ",
                R.color.colr_maroon, R.drawable.settings);

        TutorialItem tutorialItem2 = new TutorialItem("Advocates", "Get Registered Advocates Lists",
                R.color.color_red_maroon,  R.drawable.judge);

//        TutorialItem tutorialItem3 = new TutorialItem("Get Your Map Virtually", "Know your guide map virtually. In case you lost your way turn on Live locator.",
//                R.color.white, R.drawable.slider3);
//
//        TutorialItem tutorialItem4 = new TutorialItem("Find Nearby Locations", "Know all important nearby locations such as taxi stands, hospitals or any other places",
//                R.color.white, R.drawable.slide_four);



        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
//        tutorialItems.add(tutorialItem3);
//        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    @Override
    public void onTaskCompleted(OfflineDataModel result, TaskType taskType) throws JSONException {

    }

    private boolean saveDataSharedPrefrences(UserAdvocate dataPojo) {
        StringBuilder SB = new StringBuilder();
        try {

            Preferences.getInstance().loadPreferences(Login.this);

            Preferences.getInstance().user_name = dataPojo.getUsername();
            Preferences.getInstance().isLoggedIn = true;
            Preferences.getInstance().loadTutorial = false;
            Preferences.getInstance().Loginuserinfo = dataPojo.getLoginuserinfo();

            Preferences.getInstance().savePreferences(Login.this);
            finish();
            return true;
        } catch (Exception ex) {
            Log.e("Error SP==", ex.getLocalizedMessage());
            return false;
        }


    }
}