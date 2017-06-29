package com.aiqiyi.ediswit;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aiqiyi.ediswit.utils.LogUtils;
import com.qiyi.video.playcore.ErrorCode;
import com.qiyi.video.playcore.IQYPlayerHandlerCallBack;
import com.qiyi.video.playcore.QiyiVideoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

public class LLVideoView extends FrameLayout implements View.OnTouchListener, View.OnClickListener, Animator
        .AnimatorListener {
    private static final String TAG = "joshua";

    private static final int HANDLER_MSG_UPDATE_PROGRESS = 1;
    private static final int HANDLER_DEPLAY_UPDATE_PROGRESS = 1000; // 1s

    private Context context;
    private FrameLayout viewBox;
    private QiyiVideoView mVideoView;
    private LinearLayout videoPauseBtn;
    private LinearLayout screenSwitchBtn;
    private LinearLayout touchStatusView;
    private LinearLayout videoControllerLayout;
    private LinearLayout videoTopControllerLayout;
    private ImageView touchStatusImg;
    private ImageView videoPlayImg;
    private ImageView videoPauseImg;
    private TextView touchStatusTime;
    private TextView videoCurTimeText;
    private TextView videoTotalTimeText;
    private SeekBar mSeekBar;
    private TextView videotitle;
    private ImageView videocrop;
    private ProgressBar progressBar;

    private float touchLastX;
    //定义用seekBar当前的位置，触摸快进的时候显示时间
    private int position;
    private int touchStep = 1000;//快进的时间，1秒
    private int touchPosition = -1;

    private boolean videoControllerShow = true;//底部状态栏的显示状态
    private boolean videoTopControllerShow = true; //顶部状态栏显示状态
    private boolean animation = false;
    private boolean animation2 = false;
    private int timeTotal;
    private int timeCurrent;
    private String tv_id;


    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MSG_UPDATE_PROGRESS:
                    int duration = mVideoView.getDuration();
                    int progress = mVideoView.getCurrentPosition();
                    Log.w("joshua", "handleMessage: duration: " + duration + "  progress " + progress);
                    if (duration > 0) {
                        mSeekBar.setMax(duration);
                        mSeekBar.setProgress(progress);

                        timeTotal = duration;
                        timeCurrent = progress;
                        videoCurTimeText.setText(ms2hms(timeCurrent));
                        videoTotalTimeText.setText(ms2hms(timeTotal));
                    }
                    mMainHandler.sendEmptyMessageDelayed(HANDLER_MSG_UPDATE_PROGRESS, HANDLER_DEPLAY_UPDATE_PROGRESS);
                    break;
                default:
                    break;
            }
        }
    };

    public LLVideoView(Context context) {
        this(context, null);
    }

    public LLVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LLVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void start() {
        if (mVideoView != null)
            mVideoView.start();
        mMainHandler.sendEmptyMessageDelayed(HANDLER_MSG_UPDATE_PROGRESS, HANDLER_DEPLAY_UPDATE_PROGRESS);
    }

    public void destroy() {
        mMainHandler.removeCallbacksAndMessages(null);
        mVideoView.release();
        mVideoView = null;
    }

    public void pause() {
        if (null != mVideoView) {
            mVideoView.pause();
        }
        mMainHandler.removeMessages(HANDLER_MSG_UPDATE_PROGRESS);
    }

    public void setPlayData(String var1) {
        videoPauseBtn.setEnabled(true);
        mVideoView.setPlayData(var1);
        //设置回调，监听播放器状态
        tv_id = var1;
        mVideoView.setPlayerCallBack(mCallBack);
    }

    public void release() {
        mVideoView.release();
    }

    public void setFullScreen() {
        touchStatusImg.setImageResource(R.mipmap.iconfont_exit);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mVideoView.setVideoViewSize(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);
        mVideoView.requestLayout();
    }

    public void setNormalScreen() {
        touchStatusImg.setImageResource(R.mipmap.iconfont_enter_32);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mVideoView.requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.common_video_view, null);
        viewBox = (FrameLayout) view.findViewById(R.id.viewBox);
        mVideoView = (QiyiVideoView) view.findViewById(R.id.videoView);
        videoPauseBtn = (LinearLayout) view.findViewById(R.id.videoPauseBtn);
        screenSwitchBtn = (LinearLayout) view.findViewById(R.id.screen_status_btn);
        videoControllerLayout = (LinearLayout) view.findViewById(R.id.videoControllerLayout);
        videoTopControllerLayout = (LinearLayout)view.findViewById(R.id.videoTopController);
        videotitle = (TextView)view.findViewById(R.id.video_title);
        videocrop = (ImageView)view.findViewById(R.id.video_crop);

        touchStatusView = (LinearLayout) view.findViewById(R.id.touch_view);
        touchStatusImg = (ImageView) view.findViewById(R.id.touchStatusImg);
        touchStatusTime = (TextView) view.findViewById(R.id.touch_time);
        videoCurTimeText = (TextView) view.findViewById(R.id.videoCurTime);
        videoTotalTimeText = (TextView) view.findViewById(R.id.videoTotalTime);
        videoPlayImg = (ImageView) view.findViewById(R.id.videoPlayImg);
        videoPauseImg = (ImageView) view.findViewById(R.id.videoPauseImg);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        videoPlayImg.setVisibility(GONE);
        videocrop.setOnClickListener(this);
        videoPauseBtn.setOnClickListener(this);
        videoPauseBtn.setOnClickListener(this);
        screenSwitchBtn.setOnClickListener(this);
        videoPlayImg.setOnClickListener(this);
        //注册在设置或播放过程中发生错误时调用的回调函数。如果未指定回调函数，或回调函数返回false，mVideoView 会通知用户发生了错误。
        viewBox.setOnClickListener(this);
        viewBox.setOnTouchListener(this);
        addView(view);

        initSeekBar(view);
    }

    private void initSeekBar(View view) {
        mSeekBar = (SeekBar) view.findViewById(R.id.videoSeekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("TXT", "move");
                LogUtils.d(TAG, "onProgressChanged, progress = " + progress + ", fromUser = " + fromUser);
                if (fromUser) {
                    this.progress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.w("joshua", "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekBar.setProgress(progress);
                Log.w("joshua", "onStopTrackingTouch: seekTo " + progress);
                mVideoView.seekTo(progress);
                progressBar.setVisibility(View.VISIBLE);
                videoPlayImg.setVisibility(View.INVISIBLE);
                videoPauseImg.setImageResource(R.mipmap.icon_video_pause);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mVideoView.isPlaying()) {
                    return false;
                }
                float downX = event.getRawX();
                touchLastX = downX;
                Log.d("FilmDetailActivity", "downX" + downX);
                this.position = mVideoView.getCurrentPosition();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mVideoView.isPlaying()) {
                    return false;
                }
                float currentX = event.getRawX();
                float deltaX = currentX - touchLastX;
                float deltaXAbs = Math.abs(deltaX);
                if (deltaXAbs > 1) {
                    if (touchStatusView.getVisibility() != View.VISIBLE) {
                        touchStatusView.setVisibility(View.VISIBLE);
                    }
                    touchLastX = currentX;
                    Log.d("FilmDetailActivity", "deltaX" + deltaX);
                    if (deltaX > 1) {
                        position += touchStep;
                        if (position > timeTotal) {
                            position = timeTotal;
                        }
                        touchPosition = position;
                        touchStatusImg.setImageResource(R.mipmap.ic_fast_forward_white_24dp);
                        touchStatusTime.setText(ms2hms(timeCurrent) + "/" + ms2hms(timeTotal));
                    } else if (deltaX < -1) {
                        position -= touchStep;
                        if (position < 0) {
                            position = 0;
                        }
                        touchPosition = position;
                        touchStatusImg.setImageResource(R.mipmap.ic_fast_rewind_white_24dp);
                        touchStatusTime.setText(ms2hms(timeCurrent) + "/" + ms2hms(timeTotal));
                        mVideoView.seekTo(position);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchPosition != -1) {
                    mVideoView.seekTo(touchPosition);
                    touchStatusView.setVisibility(View.GONE);
                    touchPosition = -1;
                    if (videoControllerShow) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoPlayImg:
                mVideoView.start();
                videoPlayImg.setVisibility(View.INVISIBLE);
                videoPauseImg.setImageResource(R.mipmap.icon_video_pause);
                break;
            case R.id.videoPauseBtn:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    videoPauseImg.setImageResource(R.mipmap.icon_video_play);
                    videoPlayImg.setVisibility(View.VISIBLE);
                } else {
                    mVideoView.start();
                    videoPauseImg.setImageResource(R.mipmap.icon_video_pause);
                    videoPlayImg.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.video_crop:
                Log.d("TXT","crop");
                cropBeautiful();
                break;
            case R.id.viewBox:
                float curY = videoControllerLayout.getY();
                if (!animation && videoControllerShow) {
                    Log.d("screen","bottom");
                    animation = true;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout, "y",
                            curY, curY + videoControllerLayout.getHeight());
                    animator.setDuration(200);
                    animator.start();
                    animator.addListener(this);
                } else if (!animation) {
                    animation = true;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout, "y",
                            curY, curY - videoControllerLayout.getHeight());
                    animator.setDuration(200);
                    animator.start();
                    animator.addListener(this);
                }

                float curY2 = videoTopControllerLayout.getY();
                if (!animation2 && videoTopControllerShow) {
                    Log.d("screen","top");
                    animation2 = true;
                    ObjectAnimator animator2 = ObjectAnimator.ofFloat(videoTopControllerLayout, "y",
                            curY2, curY2 - videoTopControllerLayout.getHeight());
                    animator2.setDuration(200);
                    animator2.start();
                    animator2.addListener(this);
                } else if (!animation2) {
                    animation2 = true;
                    ObjectAnimator animator2 = ObjectAnimator.ofFloat(videoTopControllerLayout, "y",
                            curY2, curY2 + videoTopControllerLayout.getHeight());
                    animator2.setDuration(200);
                    animator2.start();
                    animator2.addListener(this);
                }
                break;
            case R.id.screen_status_btn:
                if (onFullScreenListener != null)
                    onFullScreenListener.onFullScreen(mVideoView.getCurrentPosition(),tv_id);
                break;
        }
    }

    private void cropBeautiful() {
        int crop_progress = mVideoView.getCurrentPosition();
        String id = this.tv_id;
        Toast.makeText(context,"已上传"+id+" "+crop_progress,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Log.d("screen","end");
       if (this.animation ==true){
           this.animation = false;
           this.videoControllerShow = !this.videoControllerShow;
       }

       if (this.animation2==true){
           this.animation2 = false;
           this.videoTopControllerShow = !this.videoTopControllerShow;
       }

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    /**
     * Convert ms to hh:mm:ss
     *
     * @param millis
     * @return
     */
    private String ms2hms(int millis) {
        String result = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        return result;
    }

    IQYPlayerHandlerCallBack mCallBack = new IQYPlayerHandlerCallBack() {
        /**
         * SeekTo 成功，可以通过该回调获取当前准确时间点。
         */
        @Override
        public void OnSeekSuccess(long l) {
            LogUtils.i(TAG, "OnSeekSuccess: " + l);
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        /**
         * 是否因数据加载而暂停播放
         */
        @Override
        public void OnWaiting(boolean b) {
            LogUtils.i(TAG, "OnWaiting: " + b);
        }

        /**
         * 播放内核发生错误
         */
        @Override
        public void OnError(ErrorCode errorCode) {
            LogUtils.i(TAG, "OnError: " + errorCode);
            mMainHandler.removeMessages(HANDLER_MSG_UPDATE_PROGRESS);
        }

        /**
         * 播放器状态码 {@link com.iqiyi.player.nativemediaplayer.MediaPlayerState}
         * 0	空闲状态
         * 1	已经初始化
         * 2	调用PrepareMovie，但还没有进入播放
         * 4    可以获取视频信息（比如时长等）
         * 8    广告播放中
         * 16   正片播放中
         * 32	一个影片播放结束
         * 64	错误
         * 128	播放结束（没有连播）
         */
        @Override
        public void OnPlayerStateChanged(int i) {
            LogUtils.i(TAG, "OnPlayerStateChanged: " + i);
        }
    };

    private OnFullScreenListener onFullScreenListener;

    public void setOnFullScreenListener(OnFullScreenListener listener) {
        this.onFullScreenListener = listener;
    }

    public void seekTo(int currentPos) {
        mVideoView.seekTo(currentPos);
    }

    public interface OnFullScreenListener {
        void onFullScreen(int currentPosition,String id);
    }



}
