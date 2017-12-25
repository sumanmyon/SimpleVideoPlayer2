package com.example.suman.videoplayerdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity
{
    private VideoView mVideoView;

    private MediaController mController;

    MediaMetadataRetriever mMetadataRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = (VideoView) findViewById(R.id.videoView);

        mMetadataRetriever = new MediaMetadataRetriever();

        Intent intent = new Intent();

        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Video File to Play"), 0);
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

