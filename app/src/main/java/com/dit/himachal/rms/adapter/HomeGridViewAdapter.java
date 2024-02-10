package com.dit.himachal.rms.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dit.himachal.rms.R;
import com.dit.himachal.rms.activities.AdvocateList;
import com.dit.himachal.rms.activities.ArchiveCaseActivity;
import com.dit.himachal.rms.activities.CauseList;
import com.dit.himachal.rms.activities.NoticeAdvocates;
import com.dit.himachal.rms.activities.SubscribedCases;
import com.dit.himachal.rms.activities.ZimniOrder;
import com.dit.himachal.rms.lazyloader.ImageLoader;
import com.dit.himachal.rms.modal.ModulesPojo;
import com.dit.himachal.rms.presentation.CustomDialog;

import java.util.ArrayList;

import in.balakrishnan.easycam.CameraBundleBuilder;
import in.balakrishnan.easycam.CameraControllerActivity;

/**
 * @author Kush.Dhawan
 * @project HPePass
 * @Time 01, 05 , 2020
 */
public class HomeGridViewAdapter extends BaseAdapter {
    Context c;
    ArrayList<ModulesPojo> gridHome;


    ImageLoader il = new ImageLoader(c);
    CustomDialog CD = new CustomDialog();
    String dept_id_ = null;


    public HomeGridViewAdapter(Context c, ArrayList<ModulesPojo> spacecrafts) {
        this.c = c;
        this.gridHome = spacecrafts;
    }

    @Override
    public int getCount() {
        return gridHome.size();
    }

    @Override
    public Object getItem(int i) {
        return gridHome.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.home_gridview_model, viewGroup, false);
        }

        final ModulesPojo s = (ModulesPojo) this.getItem(i);

        ImageView img = view.findViewById(R.id.spacecraftImg);
        TextView nameTxt = view.findViewById(R.id.nameTxt);


        nameTxt.setText(s.getName());


        if (s.getLogo().equalsIgnoreCase("") || s.getLogo() == null) {
            String fnm = "icar_logo";
            String PACKAGE_NAME = c.getApplicationContext().getPackageName();
            int imgId = this.c.getApplicationContext().getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + fnm, null, null);
            System.out.println("IMG ID :: " + imgId);
            System.out.println("PACKAGE_NAME :: " + PACKAGE_NAME);
            img.setImageBitmap(BitmapFactory.decodeResource(c.getApplicationContext().getResources(), imgId));
        } else {
            String fnm = s.getLogo();
            String PACKAGE_NAME = c.getApplicationContext().getPackageName();
            int imgId = this.c.getApplicationContext().getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + fnm, null, null);
            System.out.println("IMG ID :: " + imgId);
            System.out.println("PACKAGE_NAME :: " + PACKAGE_NAME);
            img.setImageBitmap(BitmapFactory.decodeResource(c.getApplicationContext().getResources(), imgId));
           // il.DisplaySquareImage(fnm, img, null, null, false);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ID", s.getId());


                //Advocate List
                if (s.getId().equalsIgnoreCase("1")) {


                    /**
                     * Capture Image
                     */

                    Intent intent = new Intent();
                    intent.setClass(c.getApplicationContext(), CameraControllerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("inputData", new CameraBundleBuilder()
                            .setFullscreenMode(true)
                            .setDoneButtonString("Save")
                            .setSinglePhotoMode(false)
                            .setMax_photo(1)
                            .setManualFocus(false)
                            .setBucketName(getClass().getName())
                            .setPreviewEnableCount(true)
                            .setPreviewIconVisiblity(true)
                            .setPreviewPageRedirection(true)
                            .setEnableDone(true)
                            .setClearBucket(true)
                            .createCameraBundle());
                    ((Activity) c).startActivityForResult(intent, 956);


                }


            }
        });

        return view;
    }


}