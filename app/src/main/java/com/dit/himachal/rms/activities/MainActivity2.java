package com.dit.himachal.rms.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dit.himachal.rms.R;
import com.dit.himachal.rms.adapter.HomeGridViewAdapter;
import com.dit.himachal.rms.adapter.SliderAdapter;
import com.dit.himachal.rms.databases.DatabaseHandler;
import com.dit.himachal.rms.enums.TaskType;
import com.dit.himachal.rms.generic.Generic_Async_Get;
import com.dit.himachal.rms.interfaces.AsyncTaskListenerObjectGet;
import com.dit.himachal.rms.interfaces.LengthAgenda;
import com.dit.himachal.rms.modal.GetDataPojo;
import com.dit.himachal.rms.modal.ModulesPojo;
import com.dit.himachal.rms.modal.OfflineDataModel;
import com.dit.himachal.rms.presentation.CustomDialog;
import com.dit.himachal.rms.utilities.AppStatus;
import com.dit.himachal.rms.utilities.CommonUtils;
import com.dit.himachal.rms.utilities.Econstants;
import com.dit.himachal.rms.utilities.Preferences;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity2 extends AppCompatActivity implements AsyncTaskListenerObjectGet, LengthAgenda, NavigationView.OnNavigationItemSelectedListener {

    public SliderView sliderView;

    private File actualImage;
    private File compressedImage;

    private String[] list;

    GridView home_gv;
    HomeGridViewAdapter adapter_modules;
    ActionBarDrawerToggle actionBarDrawerToggle = null;


    CustomDialog CD = new CustomDialog();
    List<ModulesPojo> modules = null;




    //SwipeRefreshLayout pullToRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


        // actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.color_red_maroon));


        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_home_actionbar_layout, null);
        getSupportActionBar().setCustomView(v);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top lev


        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView name = hView.findViewById(R.id.name);
       // TextView mobile_number = hView.findViewById(R.id.mobilenumber);
       // mobile_number.setText(Preferences.getInstance().phone_number);
       // designation.setText(Preferences.getInstance().role_name);

        if (Preferences.getInstance().isLoggedIn) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.logout);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.login);
        }

        //  PreventScreenshot.on(MainActivity2.this);

        // pullToRefresh = findViewById(R.id.pullToRefresh);
        sliderView = findViewById(R.id.imageSlider);
        home_gv = findViewById(R.id.gv);










        SliderAdapter adapters = new SliderAdapter(this);
        sliderView.setSliderAdapter(adapters);
        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.startAutoCycle();
        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);

            }
        });

        modules = new ArrayList<>();
        ModulesPojo modulesPojo = new ModulesPojo();
        modulesPojo.setId("1");
        modulesPojo.setLogo("cameracrop");
        modulesPojo.setName("Click Crop Image");


        modules.add(modulesPojo);


        adapter_modules = new HomeGridViewAdapter(this, (ArrayList<ModulesPojo>) modules);
        home_gv.setAdapter(adapter_modules);



    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onStop() {
         super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
         super.onPause();

    }


    @Override
    public void onTaskCompleted(OfflineDataModel result, TaskType taskType) throws JSONException {


    }

    @Override
    public void onLengthChanged(View v, int progress) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.log_in) {
            Intent i = new Intent(MainActivity2.this, Login.class);
            startActivity(i);

        }
        //else
        else if (id == R.id.log_out) {
            Preferences.getInstance().loadPreferences(MainActivity2.this);

            Preferences.getInstance().user_name = "";
            Preferences.getInstance().isLoggedIn = false;
            Preferences.getInstance().loadTutorial = false;
            Preferences.getInstance().savePreferences(MainActivity2.this);
            Toast.makeText(MainActivity2.this, "Logout Successful", Toast.LENGTH_LONG).show();
            Intent mainIntent = new Intent(MainActivity2.this, Login.class);
            (MainActivity2.this).startActivity(mainIntent);
            MainActivity2.this.finish();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        //956
        if (resultCode == Activity.RESULT_OK
                && requestCode == 956
                && data != null) {
            assert data != null;
            list = data.getStringArrayExtra("resultData");
            System.out.println(list[0]);
            Log.e("Path", "" + new File(list[0]).getPath());
            Log.e("Absolute Path", "" + new File(list[0]).getAbsolutePath());
            Log.e("Name", "" + new File(list[0]).getName());

            File imgFile = new File(new File(list[0]).getPath());
            Log.e("File", String.valueOf(imgFile.length()));
            actualImage = new File(imgFile.getPath());


            Compressor.getDefault(this)
                    .compressToFileAsObservable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            compressedImage = file;

                            if (compressedImage != null) {
                                System.out.println(compressedImage.getPath());
                                Log.d("Compressed Image", compressedImage.getPath());
                               // surveyObject.setConsentImagePath(compressedImage.getPath());
                                //ModulesPojo moduleToUpdate = modules.get(12 - 1);
                                //moduleToUpdate.setClicked(true);
                                //moduleToUpdate.setLogo("click");
                                //adapter_modules.notifyDataSetChanged();
                            //    System.out.println(surveyObject.toString());
                            }


                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            showError(throwable.getMessage());
                        }
                    });


            // Bitmap myBitmap = BitmapFactory.decodeFile(media.getPath());
            Toast.makeText(getApplicationContext(), "Media One captured.", Toast.LENGTH_SHORT).show();

        }


    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

}
