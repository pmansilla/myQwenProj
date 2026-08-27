package com.luck.picture.lib;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.luck.picture.lib.tools.DateUtils;

/* loaded from: classes.dex */
public class PicturePlayAudioActivity extends PictureBaseActivity implements View.OnClickListener {
    private String audio_path;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;
    private TextView tv_PlayPause;
    private TextView tv_Quit;
    private TextView tv_Stop;
    private TextView tv_musicStatus;
    private TextView tv_musicTime;
    private TextView tv_musicTotal;
    private boolean isPlayAudio = false;
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() { // from class: com.luck.picture.lib.PicturePlayAudioActivity.3
        @Override // java.lang.Runnable
        public void run() {
            try {
                if (PicturePlayAudioActivity.this.mediaPlayer != null) {
                    PicturePlayAudioActivity.this.tv_musicTime.setText(DateUtils.timeParse(PicturePlayAudioActivity.this.mediaPlayer.getCurrentPosition()));
                    PicturePlayAudioActivity.this.musicSeekBar.setProgress(PicturePlayAudioActivity.this.mediaPlayer.getCurrentPosition());
                    PicturePlayAudioActivity.this.musicSeekBar.setMax(PicturePlayAudioActivity.this.mediaPlayer.getDuration());
                    PicturePlayAudioActivity.this.tv_musicTotal.setText(DateUtils.timeParse(PicturePlayAudioActivity.this.mediaPlayer.getDuration()));
                    PicturePlayAudioActivity.this.handler.postDelayed(PicturePlayAudioActivity.this.runnable, 200L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void initPlayer(String str) {
        this.mediaPlayer = new MediaPlayer();
        try {
            this.mediaPlayer.setDataSource(str);
            this.mediaPlayer.prepare();
            this.mediaPlayer.setLooping(true);
            playAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playAudio() {
        if (this.mediaPlayer != null) {
            this.musicSeekBar.setProgress(this.mediaPlayer.getCurrentPosition());
            this.musicSeekBar.setMax(this.mediaPlayer.getDuration());
        }
        if (this.tv_PlayPause.getText().toString().equals(getString(R.string.picture_play_audio))) {
            this.tv_PlayPause.setText(getString(R.string.picture_pause_audio));
            this.tv_musicStatus.setText(getString(R.string.picture_play_audio));
            playOrPause();
        } else {
            this.tv_PlayPause.setText(getString(R.string.picture_play_audio));
            this.tv_musicStatus.setText(getString(R.string.picture_pause_audio));
            playOrPause();
        }
        if (this.isPlayAudio) {
            return;
        }
        this.handler.post(this.runnable);
        this.isPlayAudio = true;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_PlayPause) {
            playAudio();
        }
        if (id == R.id.tv_Stop) {
            this.tv_musicStatus.setText(getString(R.string.picture_stop_audio));
            this.tv_PlayPause.setText(getString(R.string.picture_play_audio));
            stop(this.audio_path);
        }
        if (id == R.id.tv_Quit) {
            this.handler.removeCallbacks(this.runnable);
            new Handler().postDelayed(new Runnable() { // from class: com.luck.picture.lib.PicturePlayAudioActivity.4
                @Override // java.lang.Runnable
                public void run() {
                    PicturePlayAudioActivity.this.stop(PicturePlayAudioActivity.this.audio_path);
                }
            }, 30L);
            try {
                closeActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        getWindow().setFlags(1024, 1024);
        super.onCreate(bundle);
        setContentView(R.layout.activity_picture_play_audio);
        this.audio_path = getIntent().getStringExtra("audio_path");
        this.tv_musicStatus = (TextView) findViewById(R.id.tv_musicStatus);
        this.tv_musicTime = (TextView) findViewById(R.id.tv_musicTime);
        this.musicSeekBar = (SeekBar) findViewById(R.id.musicSeekBar);
        this.tv_musicTotal = (TextView) findViewById(R.id.tv_musicTotal);
        this.tv_PlayPause = (TextView) findViewById(R.id.tv_PlayPause);
        this.tv_Stop = (TextView) findViewById(R.id.tv_Stop);
        this.tv_Quit = (TextView) findViewById(R.id.tv_Quit);
        this.handler.postDelayed(new Runnable() { // from class: com.luck.picture.lib.PicturePlayAudioActivity.1
            @Override // java.lang.Runnable
            public void run() {
                PicturePlayAudioActivity.this.initPlayer(PicturePlayAudioActivity.this.audio_path);
            }
        }, 30L);
        this.tv_PlayPause.setOnClickListener(this);
        this.tv_Stop.setOnClickListener(this);
        this.tv_Quit.setOnClickListener(this);
        this.musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.luck.picture.lib.PicturePlayAudioActivity.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    PicturePlayAudioActivity.this.mediaPlayer.seekTo(i);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.mediaPlayer == null || this.handler == null) {
            return;
        }
        this.handler.removeCallbacks(this.runnable);
        this.mediaPlayer.release();
        this.mediaPlayer = null;
    }

    public void playOrPause() {
        try {
            if (this.mediaPlayer != null) {
                if (this.mediaPlayer.isPlaying()) {
                    this.mediaPlayer.pause();
                } else {
                    this.mediaPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(String str) {
        if (this.mediaPlayer != null) {
            try {
                this.mediaPlayer.stop();
                this.mediaPlayer.reset();
                this.mediaPlayer.setDataSource(str);
                this.mediaPlayer.prepare();
                this.mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
