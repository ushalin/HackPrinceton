package com.example.sadman.whichwolf;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;
import java.lang.Object;


import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tv1;
    private static final int startCameraCode = 0; //this is an identifier when you request the camera app
    private ImageView mPhotoCapturedImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // for hidings


        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mPhotoCapturedImageView = (ImageView) findViewById(R.id.capturePhotoImageView);

        tv1=(TextView)findViewById(R.id.text1);

        Typeface face= Typeface.createFromAsset(getAssets(), "lobster.ttf");
        tv1.setTypeface(face);



       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });*/
    }



    public void takePhoto(View view){
        Intent callCameraApp = new Intent();
        callCameraApp.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(callCameraApp, startCameraCode);//first param is the intent, second is the identifier
    }
    //the following function intercepts the photo captured by the camera
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == startCameraCode && resultCode == RESULT_OK){
            Bundle extras = data.getExtras(); //formatting the DATA
            Bitmap photoBitmap = (Bitmap) extras.get("data");  //THIS GIVES US THE THUMBNAIL RETURNED BY THE CAMERA ACTIVITY!
            mPhotoCapturedImageView.setImageBitmap(photoBitmap);
            Toast.makeTe    xt(MainActivity.this, "Photo taken!", Toast.LENGTH_SHORT).show();
        }
    }

}
