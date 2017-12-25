package com.example.suman.videoplayerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;

import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback
{
    private static final int REQUEST_READ_PERMISSION = 786;
    private VideoView mVideoView;

    private MediaController mController;

    MediaMetadataRetriever mMetadataRetriever;
    boolean loadVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mMetadataRetriever = new MediaMetadataRetriever();
        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadVideo = true;
            loadVideo();
        }else {
            Toast.makeText(this,"went wrong",Toast.LENGTH_LONG).show();
        }
    }

    private void loadVideo() {
        if (loadVideo == true){
            Intent intent = new Intent();

            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Video File to Play"), 0);
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
            Toast.makeText(this,"fine",Toast.LENGTH_LONG).show();
        } else {
            loadVideo = true;
            loadVideo();
        }
    }

    public void startPlayback(String videoPath)
    {
        mMetadataRetriever.setDataSource(videoPath);

        Uri uri = Uri.parse(videoPath);
        mVideoView.setVideoURI(uri);

        mController = new MediaController(this, false);
        mVideoView.setMediaController(mController);
        //mVideoView.requestFocus();
        mVideoView.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                Uri sourceUri = data.getData();
                String source = getPath(sourceUri);

                startPlayback(source);
            }
        }
    }

    public String getPath(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null)
        {
            return uri.getPath();
        } else
        {
            cursor.moveToFirst();

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            return cursor.getString(idx);
        }
    }
}

