package com.example.sadman.whichwolf;;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int startCameraCode = 0; //this is an identifier when you request the camera app
    private ImageView mPhotoCapturedImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhotoCapturedImageView = (ImageView) findViewById(R.id.capturePhotoImageView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void takePhoto(View view){
        Intent callCameraApp = new Intent();
        callCameraApp.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(callCameraApp, startCameraCode);//first param is the intent, second is the identifier
    }
    //the following function intercepts the photo captured by the camera
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == startCameraCode && resultCode == RESULT_OK){
            Bundle extras = data.getExtras(); //formatting the DATA
            Bitmap photoBitmap = (Bitmap) extras.get("data");  //THIS GIVES US THE THUMBNAIL RETURNED BY THE CAMERA ACTIVITY!
            mPhotoCapturedImageView.setImageBitmap(photoBitmap);
            Toast.makeText(MainActivity.this, "Photo taken!", Toast.LENGTH_SHORT).show();
        }
        //else if (requestCode == )
    }
    //the following function was made to store the actual full size image (maybe the thumbnail is sufficient) HOW TO IMPORT FILE CLASS
    /*void createImageFile (){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //allows files to have unique names
        String fileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

       // .... Do we even need to do this!??!?!?

    }*/
/*
    //ACCESSING THE GALLERY NOT SURE IF THIS REALLY WORKS OR NAH

    public void useGallery(View view){
        Intent callGallery = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(callGallery, startCameraCode);//first param is the intent, second is the identifier
    }
*/
    public void useGallery(View view) {
        Intent callGallery = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(callGallery, );
    }
}
