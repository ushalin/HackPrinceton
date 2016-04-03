package com.example.sadman.whichwolf;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "SHARE-DEBUG";
    public NotificationManager mNotifyManager;
    public NotificationCompat.Builder mBuilder;
    TextView tv1;
    private static final int startCameraCode = 0; //this is an identifier when you request the camera app
    private ImageView mPhotoCapturedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // for hidings

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        findViewById(R.id.loadingPanel1).setVisibility(View.GONE);
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

            findViewById(R.id.text1).setVisibility(View.GONE);
            findViewById(R.id.mainLogo).setVisibility(View.GONE);
            findViewById(R.id.photoButton).setVisibility(View.GONE);
            findViewById(R.id.loadingPanel1).setVisibility(View.VISIBLE);

            Bundle extras = data.getExtras(); //formatting the DATA
            Bitmap photoBitmap = (Bitmap) extras.get("data");  //THIS GIVES US THE THUMBNAIL RETURNED BY THE CAMERA ACTIVITY!
            mPhotoCapturedImageView.setImageBitmap(photoBitmap);
            Toast.makeText(MainActivity.this, "Photo taken!", Toast.LENGTH_SHORT).show();

            //DECODE BITMAP
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                boolean compress = photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            }
            catch(NullPointerException e){
                e.getCause();
            }
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);


            //Do the thing where we send it to the server
            /*
            try{
                upload(encodedImage);
            }
            catch(IOException e){
                e.getCause();
            }*/
            //Intent intent = new Intent(this, Loading.class);

        }
    }

    private void upload(String filepath) throws IOException {
        RequestParams params = new RequestParams();
        //params.put("key", "dr3ti912h2130h39js12"); // Need to  get the api key
        final int NOTIF_ID = (int)(Math.random()*1000);
        params.put("Image", filepath);

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        client.post("https://www.googleapis.com/upload/drive/v2/files ", params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(DEBUG_TAG, "UPLOAD FAIL " + error.toString());
                /*if(error.getClass().getSimpleName().equals("IOException")){
                    //mBuilder.setContentText("Could not resolve host");
                }
                else if(error.getClass().getSimpleName().equals("HttpResponseException")){
                    //mBuilder.setContentText("Invalid API Key. Check settings and try again");
                }
                else {
                    //mBuilder.setContentText("Upload failed. Check settings and try again");
                }
                */
                //mNotifyManager.notify(NOTIF_ID, mBuilder.build());
                Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody) {
                Log.d(DEBUG_TAG, "Upload completed");
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    Log.d(DEBUG_TAG, "JSON: " + json.toString());
                    Uri result = Uri.parse(json.getString("url"));
                    /*Intent launchBrowser = new Intent(Intent.ACTION_VIEW, result);
                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    getApplicationContext(),
                                    0,
                                    launchBrowser,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

                    mBuilder.setContentIntent(resultPendingIntent);
                    mNotifyManager.notify(NOTIF_ID, mBuilder.build());
                    */
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("sr.ht URL", result.toString());
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getApplicationContext(), "File uploaded, URL copied to clipboard.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d(DEBUG_TAG, "Upload complete with exception " + e.toString());
                    mBuilder.setContentText("Upload failed. Check URL and API key")
                            .setProgress(0, 0, false)
                            .setContentInfo("");
                    mNotifyManager.notify(NOTIF_ID, mBuilder.build());
                    Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });



    }

}
