package com.lztek.hdmiin.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import com.lixiao.build.mybase.LG;
import com.lztek.hdmiin.Amix;
import java.util.List;




public class HdmiPlayerView extends TextureView implements
        Camera.ErrorCallback,
        SoundPool.OnLoadCompleteListener {
    private final String tag=getClass().getSimpleName()+">>>>";
    private static final int STYLE_SCALE = 0;
    private static final int STYLE_CENTER = 1;
    private static final int STYLE_FULL = 2;
    private static final int SOUND_NOT_LOADED = -1;
    protected int mVideoWidth;
    protected int mVideoHeight;
    protected SurfaceTexture surfaceTexture;
    private Camera mCamera;
    private final int mScaleStyle = STYLE_FULL;
    private OnPlayEventListener mOnPlayEventListener;
    private AudioThread mAudioThread = null;
    private SoundPool mSoundPool;
    private int mSoundId = SOUND_NOT_LOADED;
    private final Handler mHandler = new Handler();
    private final Runnable mRunnableHdmiDetect = new Runnable() {
        @Override
        public void run() {
            if (hdmiInStatus()) {
                mHandler.postDelayed(mRunnableHdmiDetect, 4000);
            } else {
                mHandler.post(mRunnableCameraError);
            }
        }
    };
    private final Runnable mRunnableHdmiInStart = new Runnable() {
        @Override
        public void run() {
            onHdmiInStartRunnable();
        }
    };
    private AudioManager mAudioManager = null;
    private final Runnable mRunnableCameraError = new Runnable() {
        @Override
        public void run() {
            onHdmiInErrorRunnable();
        }
    };

    public HdmiPlayerView(Context context) {
        this(context, null);
    }

    public HdmiPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HdmiPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private static String systemProperties(String key) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Class<?>[] clzParams = {String.class};
            java.lang.reflect.Method method = clazz.getDeclaredMethod("get", clzParams);
            Object obj = null == method ? "" : method.invoke(null, key);
            return obj instanceof CharSequence ? obj.toString().trim() : "";
        } catch (Exception e) {
            return "";
        }
    }

    @SuppressWarnings("deprecation")
    private void initView() {

        mVideoWidth = 0;
        mVideoHeight = 0;
        surfaceTexture = null;
        setSurfaceTextureListener(new SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                surfaceTexture = surface;

                if (null != mOnPlayEventListener) {
                    mOnPlayEventListener.onSurfacePrepared(HdmiPlayerView.this);
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                LG.i(tag,"----------------onSurfaceTextureSizeChanged:"+width+"--"+height);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(this);
        mSoundId = mSoundPool.load("/system/media/audio/ui/camera_click.ogg", 1);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cameraStop();
        if (null != mSoundPool) {
            mSoundPool.release();
        }
        mSoundPool = null;
        mSoundId = SOUND_NOT_LOADED;
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            if (null != mAudioManager) {
                mAudioManager.setParameters("HDMIin_enable=false");
                mAudioManager = null;
            }
        } else {
            Amix.lineInOff();
        }
        synchronized (HdmiPlayerView.class) {
            if (null != mAudioThread) {
                mAudioThread.stopRun();
                mAudioThread = null;
            }
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0 && soundPool == mSoundPool && sampleId == mSoundId) {
            mSoundPool.play(mSoundId, 0.0f, 0.0f, 0, -1, 1.0f);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecSize > 0 && heightSpecSize > 0) {
            if (mScaleStyle == STYLE_SCALE) {
                //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                if (mVideoWidth <= 0 || mVideoHeight <= 0) {
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                } else if (mVideoWidth * heightSpecSize < widthSpecSize * mVideoHeight) {
                    setMeasuredDimension(heightSpecSize * mVideoWidth / mVideoHeight, heightSpecSize);
                } else if (mVideoWidth * heightSpecSize > widthSpecSize * mVideoHeight) {
                    setMeasuredDimension(widthSpecSize, widthSpecSize * mVideoHeight / mVideoWidth);
                } else {
                    setMeasuredDimension(widthSpecSize, heightSpecSize);
                }
            } else if (mScaleStyle == STYLE_CENTER) {
                if (0 < mVideoWidth && mVideoWidth <= widthSpecSize &&
                        0 < mVideoHeight && mVideoHeight <= heightSpecSize)
                    setMeasuredDimension(mVideoWidth, mVideoHeight);
                else
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else if (mScaleStyle == STYLE_FULL) {
                setMeasuredDimension(widthSpecSize, heightSpecSize);
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setOnPlayEventListener(OnPlayEventListener l) {
        mOnPlayEventListener = l;
    }

    public void startPlay() {
        cameraOpen();
        cameraStart();
    }

    public void stopPlay() {
        cameraStop();
    }

    private void cameraOpen() {

        Log.d("#HDMI#", "camera open() is called");
        if (surfaceTexture == null) {
            return;
        }
        cameraStop();
        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int cameraId = 0;
            for (cameraId = 0; cameraId < numberOfCameras; ++cameraId) {
                Camera.getCameraInfo(cameraId, cameraInfo);
                if (android.os.Build.VERSION.SDK_INT >= 24) {
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        break;
                    }
                } else {
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        break;
                    }
                }
            }
            if (cameraId < numberOfCameras) {
                // Open the Camera in preview mode
                mCamera = Camera.open(cameraId);
                if (null != mCamera) {
                    mCamera.setPreviewTexture(surfaceTexture);
                    mCamera.setErrorCallback(this);
                    mCamera.setDisplayOrientation(cameraInfo.orientation);
                    Camera.Parameters param = mCamera.getParameters();
                    List<Camera.Size> sizeLise = param.getSupportedPreviewSizes();
                    if (sizeLise != null) {
                        int width = -1;
                        int height = -1;
                        for (Camera.Size sz : sizeLise) {
                            //Log.d("#HDMI#", "PreviewSize Support[" + sz.width + ", " + sz.height + "]");
                            if (width < sz.width || (width == sz.width && height < sz.height)) {
                                width = sz.width;
                                height = sz.height;
                            }
                        }
                        if (width > 0 && height > 0) {
                            int inWidth = -1;
                            int inHeight = -1;
                            // 1920x1080P60/1920x1080I60/1280x720P60/720x576P50/720x480P60/720x576I50/720x480I60/640x480P60
                            String text = systemProperties("sys.hdmiin.resolution");
                            text = null == text ? "" : text.trim();
                            if (text.equals("1") || text.equals("2") ||
                                    text.matches("^\\d{3,4}(x|X)\\d{3,4}(p|P|i|I)\\d{2}$")) { // 720P
                                if (text.equals("1")) {
                                    //inWidth = 1920;
                                    //inHeight = 1080;
                                } else if (text.equals("2")) {
                                    inWidth = 1280;
                                    inHeight = 720;
                                } else { //if (text.matches("^\\d{3,4}(x|X)\\d{3,4}(p|P|i|I)\\d{2}$")) {
                                    String[] itemArray = text.split("x|X|p|P|i|I");
                                    inWidth = Integer.parseInt(itemArray[0]);
                                    inHeight = Integer.parseInt(itemArray[1]);
                                }
                            }
                            if (inWidth > 0 && inHeight > 0) { // 720P
                                width = inWidth;
                                height = inHeight;
                            }
                            param.setPreviewSize(width, height);
                            mCamera.setParameters(param);
                        }
                    }
                    Camera.Size size = param != null ? param.getPreviewSize() : null;

                    if (size != null) {
                        mVideoWidth = size.width;
                        mVideoHeight = size.height;
                    } else {
                        mVideoWidth = 0;
                        mVideoHeight = 0;
                    }
                    if (mVideoWidth != 0 && mVideoHeight != 0) {
//                        getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                        requestLayout();
                    }
                    Log.d("#HDMI#", "PreviewSize[" + size.width + ", " + size.height + "]");
                    if (mSoundPool != null) {
                        mSoundPool.play(mSoundId, 0.0f, 0.0f, 0, -1, 1.0f);
                    }
                } else {
                    Log.e("#HDMI#", "Camera.open() failed");
                }
            } else {
                Log.e("#HDMI#", "Cannot find camera");
            }
        } catch (Exception e) {
            Log.e("#HDMI#", "Camera.open exception: " + e.getMessage());
            mCamera = null;
        }
        if (null == mCamera) {
            mHandler.post(mRunnableCameraError);
        }
    }

    private void cameraStart() {
        if (null != mCamera) {
            Log.d("#HDMI#", "camera start() is called");
            try {
                mCamera.startPreview();

                if (android.os.Build.VERSION.SDK_INT >= 24) {
                    if (null == mAudioManager) {
                        mAudioManager = (AudioManager) getContext().getSystemService(
                                Context.AUDIO_SERVICE);
                        Log.d("#HDMI#", "getSystemService(AUDIO_SERVICE) " +
                                (null != mAudioManager ? "OK" : "FAILED"));
                    }
                    if (null != mAudioManager) {
                        mAudioManager.setParameters("HDMIin_enable=true");
                    }
                } else {
                    Amix.lineInOn();
                }

                synchronized (HdmiPlayerView.class) {
                    if (null == mAudioThread) {
                        mAudioThread = new AudioThread();
                        mAudioThread.start();
                    }
                }
//		        if (hdmiInStatus()) { 
//		        	mHandler.postDelayed(mRunnableHdmiDetect, 4000);
//		        } else {
//					if (null != mOnPlayEventListener) { 
//						mOnPlayEventListener.onHdmiError(this); 
//					}
//					return;
//		        }
                if (null != mOnPlayEventListener) {
                    mOnPlayEventListener.onHdmiStart(this);
                }
//                mHandler.postDelayed(mRunnableHdmiInStart, 5000);
            } catch (Exception e) {
                Log.d("#HDMI#", "Error starting camera preview: " + e.getMessage());
                mHandler.post(mRunnableCameraError);
            }
        }
    }

    private void cameraStop() {
        if (null != mCamera) {
            Log.d("#HDMI#", "camera stop() is called");
            try {
                mCamera.stopPreview();
                mCamera.setPreviewDisplay(null);
                mCamera.release();
            } catch (Exception e) {
                Log.e("#HDMI#", "Camera relase failed: " + e.getMessage(), e);
            }
            mCamera = null;
            if (android.os.Build.VERSION.SDK_INT >= 24) {
                if (null != mAudioManager) {
                    mAudioManager.setParameters("HDMIin_enable=false");
                    mAudioManager = null;
                }
            } else {
                Amix.lineInOff();
            }
            synchronized (HdmiPlayerView.class) {
                if (null != mAudioThread) {
                    mAudioThread.stopRun();
                    mAudioThread = null;
                }
            }
            if (null != mSoundPool && SOUND_NOT_LOADED != mSoundId) {
                mSoundPool.stop(mSoundId);
            }
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    @Override
    public void onError(int error, Camera camera) {
        mHandler.post(mRunnableCameraError);
    }

    private void onHdmiInStartRunnable() {
        if (hdmiInStatus()) {
            int inWidth = -1;
            int inHeight = -1;
            // 1920x1080P60/1920x1080I60/1280x720P60/720x576P50/720x480P60/720x576I50/720x480I60/640x480P60
            String text = systemProperties("sys.hdmiin.resolution");
            text = null == text ? "" : text.trim();
            if (text.equals("1") || text.equals("2") ||
                    text.matches("^\\d{3,4}(x|X)\\d{3,4}(p|P|i|I)\\d{2}$")) { // 720P
                if (text.equals("1")) {
                    //inWidth = 1920;
                    //inHeight = 1080;
                } else if (text.equals("2")) {
                    inWidth = 1280;
                    inHeight = 720;
                } else { //if (text.matches("^\\d{3,4}(x|X)\\d{3,4}(p|P|i|I)\\d{2}$")) {
                    String[] itemArray = text.split("x|X|p|P|i|I");
                    inWidth = Integer.parseInt(itemArray[0]);
                    inHeight = Integer.parseInt(itemArray[1]);
                }
            }
            if (inWidth > 0 && inHeight > 0) {
                Camera.Parameters param = mCamera.getParameters();
                Camera.Size sz = param.getPreviewSize();
                if (sz.width != inWidth || sz.height != inHeight) {
                    try {
                        mCamera.stopPreview();
                        param.setPreviewSize(inWidth, inHeight);
                        mCamera.setParameters(param);
                        mCamera.startPreview();
                    } catch (Exception e) {
                        Log.e("#HDMI#", "Camera reset preview size failed: " + e.getMessage(), e);
                        if (null != mOnPlayEventListener) {
                            mOnPlayEventListener.onHdmiError(HdmiPlayerView.this);
                        }
                        return;
                    }
                }
            }
            if (null != mOnPlayEventListener) {
                mOnPlayEventListener.onHdmiStart(HdmiPlayerView.this);
            }
//            mHandler.postDelayed(mRunnableHdmiDetect, 4000);
        } else {
            if (null != mOnPlayEventListener) {
                mOnPlayEventListener.onHdmiError(HdmiPlayerView.this);
            }
        }
    }

    private void onHdmiInErrorRunnable() {
        mHandler.removeCallbacksAndMessages(null);
        if (null != mOnPlayEventListener) {
            mOnPlayEventListener.onHdmiError(HdmiPlayerView.this);
        }
        cameraStop();
    }

    private boolean hdmiInStatus() {
        return systemProperties("sys.hdmiin.status").equals("1") &&
                !systemProperties("sys.hdmiin.resolution").equals("0");
    }

    public interface OnPlayEventListener {
        void onSurfacePrepared(HdmiPlayerView playerView);

        void onHdmiStart(HdmiPlayerView playerView);

        void onHdmiError(HdmiPlayerView playerView);
    }

    class AudioThread extends Thread {
        static final int mFrequency = 44100;
        static final int mFormat = AudioFormat.ENCODING_PCM_16BIT;

        static final int mChannelIn = AudioFormat.CHANNEL_IN_MONO;
        static final int mChannelOut = AudioFormat.CHANNEL_OUT_MONO;

        private volatile boolean mThreadRun = false;

        public void stopRun() {
            mThreadRun = false;
        }


        @Override
        public void run() {
            mThreadRun = true;
            //this.setPriority(Thread.MAX_PRIORITY);
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

            AudioRecord record = null;
            AudioTrack track = null;
            try {
                int recBufSize = AudioRecord.getMinBufferSize(mFrequency, mChannelIn, mFormat) * 2;
                int plyBufSize = AudioTrack.getMinBufferSize(mFrequency, mChannelOut, mFormat) * 2;
                record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        mFrequency, mChannelIn, mFormat, recBufSize);
                track = new AudioTrack(AudioManager.STREAM_MUSIC,
                        mFrequency, mChannelOut, mFormat, plyBufSize, AudioTrack.MODE_STREAM);
                byte[] recBuf = new byte[recBufSize];
                record.startRecording();
                track.play();
                while (mThreadRun) {
                    int readLen = record.read(recBuf, 0, recBufSize);
                    track.write(recBuf, 0, readLen);
                }
            } catch (Exception e) {
                Log.e("#ERROR#", "HdmiAudio exception: " + e.getMessage(), e);
            } finally {
                if (null != track) {
                    try {
                        track.stop();
                    } catch (Exception e) {
                    }
                }
                if (null != record) {
                    try {
                        record.stop();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

}
