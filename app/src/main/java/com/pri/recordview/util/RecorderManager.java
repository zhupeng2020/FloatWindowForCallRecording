package com.pri.recordview.util;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecorderManager {
    private static final String TAG = "RecorderManager";
    private MediaRecorder mMediaRecorder;
    private long mStartTimeMillis;

    public long startRecording(String phoneNumber) {
        LogUtils.i(TAG, "startRecording: ");
        try {
            mMediaRecorder = new MediaRecorder();
            // Set the audio source to VOICE_CALL
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(getOutputFile(phoneNumber));
            mMediaRecorder.prepare();
            // Start recording
            mMediaRecorder.start();
            // Record the start time of the recording
            mStartTimeMillis = SystemClock.elapsedRealtime();
        } catch (IOException e) {
            // Handling exception
            LogUtils.i(TAG, "startRecording: catch  = " + e.getMessage());
            mMediaRecorder = null;
            return 0;
        }

        return mStartTimeMillis;
    }

    public void stopRecording() {
        LogUtils.i(TAG, "stopRecording. ");
        if (mMediaRecorder != null) {
            // Stop recording
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Calculate recording duration
            //long durationMillis = SystemClock.elapsedRealtime() - mStartTimeMillis;
            // Save the recording file to local storage
            //saveAudioFile(durationMillis);
        }
    }

    private File getOutputFile(String phoneNumber) {
        LogUtils.i(TAG, "getOutputFile: ");
        File dir = new File(Environment.getExternalStorageDirectory(), "Music");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String dateTime = sdf.format(date);
        String filename = phoneNumber + "_" + dateTime + ".aac";
        LogUtils.i(TAG, "getOutputFile: filename = " + filename);
        return new File(dir, filename);
    }

    private void saveAudioFile(long durationMillis) {
        // Handles the logic for saving the recording file
    }

}
