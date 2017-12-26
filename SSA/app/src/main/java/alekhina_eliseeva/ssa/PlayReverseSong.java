package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.FileOutputStream;
import java.io.IOException;


public class PlayReverseSong extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private SeekBar seekBar;
    private String songFile;
    private int sizeSong;
    private byte[] song;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mediaPlayer.getCurrentPosition();
            Integer x = (currentPosition * 100) / mediaPlayer.getDuration();
            seekBar.setProgress(x);
            handler.postDelayed(this, 100);
        }
    };

    private void saveMusic() {
        byte[] header = new byte[44];
        header[0] = 82;
        header[1] = 73;
        header[2] = 70;
        header[3] = 70;
        header[4] = -124;
        header[5] = 69;
        header[6] = 3;
        header[7] = 0;
        header[8] = 87;
        header[9] = 65;
        header[10] = 86;
        header[11] = 69;
        header[12] = 102;
        header[13] = 109;
        header[14] = 116;
        header[15] = 32;
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;
        header[21] = 0;
        header[22] = 1;
        header[23] = 0;
        header[24] = 68;
        header[25] = -84;
        header[26] = 0;
        header[27] = 0;
        header[28] = -128;
        header[29] = 62;
        header[30] = 0;
        header[31] = 0;
        header[32] = 2;
        header[33] = 0;
        header[34] = 16;
        header[35] = 0;
        header[36] = 100;
        header[37] = 97;
        header[38] = 116;
        header[39] = 97;
        int value = sizeSong;
        header[40] = (byte)(value & 0xFF);
        value >>>= 8;
        header[41] = (byte)(value & 0xFF);
        value >>>= 8;
        header[42] = (byte)(value & 0xFF);
        value >>>= 8;
        header[43] = (byte)(value & 0xFF);
        value >>>= 8;
        try {
            FileOutputStream fos = new FileOutputStream(songFile);
            fos.write(header, 0, header.length);
            fos.write(song, 44, sizeSong);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(songFile);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.d("PlaySong", e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        Intent intent = getIntent();
        handler = new Handler();
        ImageView start = (ImageView) findViewById(R.id.startPlay);
        start.setImageResource(R.drawable.start);
        ImageView stop = (ImageView) findViewById(R.id.stopPlay);
        stop.setImageResource(R.drawable.stop);
        ImageView pause = (ImageView) findViewById(R.id.pausePlay);
        pause.setImageResource(R.drawable.pause);
        Button next = (Button) findViewById(R.id.next);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        createMediaPlayer();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.postDelayed(runnable, 100);
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(0);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int result = seekBar.getProgress();
                Integer x = mediaPlayer.getDuration() * result / 100;
                mediaPlayer.seekTo(mediaPlayer.getDuration() * result / 100);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}
