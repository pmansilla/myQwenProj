package com.luck.picture.lib;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

/* loaded from: classes.dex */
public class PictureVideoPlayActivity extends PictureBaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnClickListener {
    private ImageView iv_play;
    private MediaController mMediaController;
    private VideoView mVideoView;
    private ImageView picture_left_back;
    private String video_path = "";
    private int mPositionWhenPaused = -1;

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(new ContextWrapper(context) { // from class: com.luck.picture.lib.PictureVideoPlayActivity.1
            @Override // android.content.ContextWrapper, android.content.Context
            public Object getSystemService(String str) {
                return "audio".equals(str) ? getApplicationContext().getSystemService(str) : super.getSystemService(str);
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.picture_left_back) {
            finish();
        } else if (id == R.id.iv_play) {
            this.mVideoView.start();
            this.iv_play.setVisibility(4);
        }
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (this.iv_play != null) {
            this.iv_play.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        getWindow().addFlags(67108864);
        super.onCreate(bundle);
        setContentView(R.layout.picture_activity_video_play);
        this.video_path = getIntent().getStringExtra("video_path");
        this.picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        this.mVideoView = (VideoView) findViewById(R.id.video_view);
        this.mVideoView.setBackgroundColor(-16777216);
        this.iv_play = (ImageView) findViewById(R.id.iv_play);
        this.mMediaController = new MediaController(this);
        this.mVideoView.setOnCompletionListener(this);
        this.mVideoView.setOnPreparedListener(this);
        this.mVideoView.setMediaController(this.mMediaController);
        this.picture_left_back.setOnClickListener(this);
        this.iv_play.setOnClickListener(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.mMediaController = null;
        this.mVideoView = null;
        this.iv_play = null;
        super.onDestroy();
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.mPositionWhenPaused = this.mVideoView.getCurrentPosition();
        this.mVideoView.stopPlayback();
        super.onPause();
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() { // from class: com.luck.picture.lib.PictureVideoPlayActivity.2
            @Override // android.media.MediaPlayer.OnInfoListener
            public boolean onInfo(MediaPlayer mediaPlayer2, int i, int i2) {
                if (i != 3) {
                    return false;
                }
                PictureVideoPlayActivity.this.mVideoView.setBackgroundColor(0);
                return true;
            }
        });
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        if (this.mPositionWhenPaused >= 0) {
            this.mVideoView.seekTo(this.mPositionWhenPaused);
            this.mPositionWhenPaused = -1;
        }
        super.onResume();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onStart() {
        this.mVideoView.setVideoPath(this.video_path);
        this.mVideoView.start();
        super.onStart();
    }
}
