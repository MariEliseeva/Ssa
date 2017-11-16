package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class PlaySong extends AppCompatActivity {
    MediaPlayer mPlayer;
    Button startButton, pauseButton, stopButton, nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        mPlayer=MediaPlayer.create(this, R.raw.mysound);
        mPlayer.setLooping(true);

        startButton = (Button) findViewById(R.id.start);
        pauseButton = (Button) findViewById(R.id.pause);
        stopButton = (Button) findViewById(R.id.stop);
        nextButton = (Button) findViewById(R.id.next);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayer.stop();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.start();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.pause();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.stop();
                try {
                    mPlayer.prepare();
                }
                catch (IOException e) {
                    Toast.makeText(PlaySong.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                mPlayer.seekTo(0);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                Intent intent = new Intent(PlaySong.this, AudioRecorder.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }
}
