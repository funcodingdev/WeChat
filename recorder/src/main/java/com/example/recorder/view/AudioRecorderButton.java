package com.example.recorder.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.recorder.AudioManager;
import com.example.recorder.R;

@SuppressLint("AppCompatCustomView")
public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {
    private static final String TAG = "AudioRecorderButton";
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDERING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private static final int DISTANCE_Y_CANCEL = 50;

    private static final int MSG_AUDIO_PREPARED = 0x110;
    private static final int MSG_AUDIO_CHANGED = 0x111;
    private static final int MSG_AUDIO_DISMISS = 0x112;


    private int mCurState = STATE_NORMAL;//当前的状态
    private boolean isRecordering = false;//判断是否正在录音

    private float mTime = 0;
    private boolean mReady;//是否出发OnLongClick

    private DialogManager mDialogManager;
    private AudioManager mAudioManager;
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecordering) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_AUDIO_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public interface AudioFinshRecorderListener{
        void onFinsh(float seconds,String filePath);
    }

    private AudioFinshRecorderListener mListener;

    public void setAudioFinshRecorderListener(AudioFinshRecorderListener mListener) {
        this.mListener = mListener;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_CHANGED: {
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    break;
                }
                case MSG_AUDIO_DISMISS: {
                    mDialogManager.dismissDialog();
                    break;
                }
                case MSG_AUDIO_PREPARED: {
                    mDialogManager.showRecordingDialog();
                    isRecordering = true;
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                }
            }
        }
    };


    public AudioRecorderButton(Context context) {
        this(context, null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDialogManager = new DialogManager(getContext());

        String dir = Environment.getExternalStorageDirectory() + "/imooc_recorder";
        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setAudioStateListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mAudioManager.prepareAudio();
                mReady = true;
                return false;
            }
        });
    }

    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();// 获取点击事件距离控件左边的距离，即视图坐标
        int y = (int) event.getY(); //获取点击事件距离控件顶边的距离，即视图坐标
        switch (action) {
            case MotionEvent.ACTION_DOWN: {//按下操作
                changeState(STATE_RECORDERING);
                break;
            }
            case MotionEvent.ACTION_MOVE: {//移动操作
                if (isRecordering) {
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDERING);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {//弹起操作
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecordering || mTime < 0.6f) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_AUDIO_DISMISS, 1300);
                } else if (mCurState == STATE_RECORDERING) {
                    //正常录制结束
                    mDialogManager.dismissDialog();
                    mAudioManager.release();
                    if(mListener != null){
                        mListener.onFinsh(mTime,mAudioManager.getmCurrentFilePath());
                    }
                } else if (mCurState == STATE_WANT_TO_CANCEL) {
                    //取消录音
                    mDialogManager.dismissDialog();
                    mAudioManager.cancel();
                }
                reset();
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态即标志位
     */
    private void reset() {
        mTime = 0;
        isRecordering = false;
        mReady = false;
        changeState(STATE_NORMAL);
    }

    /**
     * 判断是否想要取消
     *
     * @param x
     * @param y
     * @return
     */
    private boolean wantToCancel(int x, int y) {
//        Log.d(TAG, "wantToCancel: x="+x+",y="+y);
        if (x < 0 || x > getWidth()) {
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    /**
     * 改变当前状态
     *
     * @param state
     */
    private void changeState(int state) {
        if (state != mCurState) {
            mCurState = state;
            switch (state) {
                case STATE_NORMAL: {
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                }
                case STATE_RECORDERING: {
                    setBackgroundResource(R.drawable.btn_recorder_recordering);
                    setText(R.string.str_recorder_recordering);
                    if (isRecordering) {
                        mDialogManager.recording();
                    }
                    break;
                }
                case STATE_WANT_TO_CANCEL: {
                    setBackgroundResource(R.drawable.btn_recorder_recordering);
                    setText(R.string.str_recorder_want_cancel);
                    if (isRecordering) {
                        mDialogManager.wantToCancel();
                    }
                    break;
                }
            }
        }
    }
}
