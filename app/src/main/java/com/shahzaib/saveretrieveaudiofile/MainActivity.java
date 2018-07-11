package com.shahzaib.saveretrieveaudiofile;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final String LOG_TAG ="123456";
    static final int REQUEST_CODE_RECORD_AUDIO_PERMISSION = 100;

    String outputFilePath ;
    MediaRecorder recorder;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        outputFilePath = getExternalCacheDir().getAbsolutePath() +"/myRecording.3gp";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_CODE_RECORD_AUDIO_PERMISSION);
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_RECORD_AUDIO_PERMISSION)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Log.i(LOG_TAG,"User Granted the permission");
                // now check permission is granted successfull or Denied by some error such as: screen overlay
                if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                {
                    Log.i(LOG_TAG,"permission granted successfully");
                }
                else
                {
                    Log.i(LOG_TAG,"User granted the permission, but it is denied by some error");
                }


            }
            else
            {
                Log.i(LOG_TAG,"User Denied the permission, So request again");
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_CODE_RECORD_AUDIO_PERMISSION);
            }
        }
    }

    public void startRecording(View view) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(outputFilePath);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(LOG_TAG,"Failed To prepare");
        }
        recorder.start();
        Log.i(LOG_TAG,"Recording........");
    }

    public void stopRecording(View view) {
        if(recorder!=null)
        {
            Log.i(LOG_TAG,"Recording Stopped");
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public void startPlaying(View view) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(outputFilePath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(LOG_TAG,"Error While playing");
        }
        mediaPlayer.start();
        Log.i(LOG_TAG,"Start Playing.......");
    }

    public void stopPlaying(View view) {
        if(mediaPlayer !=null)
        {
            Log.i(LOG_TAG,"Playing Stopped");
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

