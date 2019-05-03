package com.example.recorder;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AudioManager {
    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioManager mInstance;
    private boolean isPrepared;

    private AudioManager(String dir){
        this.mDir = dir;
    }

    public interface AudioStateListener{
        void wellPrepared();
    }

    public AudioStateListener mListener;

    public void setAudioStateListener(AudioStateListener mListener) {
        this.mListener = mListener;
    }

    public String getmCurrentFilePath() {
        return mCurrentFilePath;
    }

    public static AudioManager getInstance(String dir){
        if(mInstance == null){
            synchronized (AudioManager.class){
                mInstance = new AudioManager(dir);
            }
        }
        return mInstance;
    }

    public void prepareAudio(){
        File dir = new File(mDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        isPrepared = false;
        String fileName = generateFileName();
        File file = new File(dir,fileName);
        mCurrentFilePath = file.getAbsolutePath();
        try {
            mMediaRecorder = new MediaRecorder();
            //设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            //设置mediaRecorder的音频源为我们的麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //设置音频编码
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isPrepared = true;
            if(mListener != null){
                mListener.wellPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String generateFileName() {
        return UUID.randomUUID().toString()+".amr";
    }

    public int getVoiceLevel(int maxLevel){
        if(isPrepared){
            try {
                return maxLevel*mMediaRecorder.getMaxAmplitude()/32768+1;//mMediaRecorder.getMaxAmplitude() 1-32767
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public void release(){
        isPrepared = false;
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    public void cancel(){
        release();
        if(mCurrentFilePath != null){
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }
}
