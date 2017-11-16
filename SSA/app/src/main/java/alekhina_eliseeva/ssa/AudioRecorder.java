package alekhina_eliseeva.ssa;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;


public class AudioRecorder extends AppCompatActivity {
    Uri savedUri;
    MediaPlayer mPlayer;
    Button startRecordButton, playRecordButton, pausePlayButton, stopPlayButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);
        startRecordButton = (Button) findViewById(R.id.startRecord);
        playRecordButton = (Button) findViewById(R.id.playRecord);
        pausePlayButton = (Button) findViewById(R.id.PausePlay);
        stopPlayButton = (Button) findViewById(R.id.StopPlay);
        nextButton = (Button) findViewById(R.id.next);

        pausePlayButton.setEnabled(false);
        playRecordButton.setEnabled(false);
        stopPlayButton.setEnabled(false);

        startRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savedUri != null) {
                    //TODO ошибка
                    // deleteFile(savedUri.Path())
                }
                Intent intent = new Intent(
                        MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, 1);
            }
        });



        playRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer != null) {
                    mPlayer.start();
                }
            }
        });

        pausePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer!=null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                }
            }
        });


        stopPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer != null) {
                    mPlayer.stop();
                    try {
                        mPlayer.prepare();
                    } catch (IOException e) {
                        Toast.makeText(AudioRecorder.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    mPlayer.seekTo(0);
                }

            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* TODO передаем контроллеру Uri. После ненадобности файл должен быть удален */
                Toast.makeText(AudioRecorder.this,"Передано контроллеру", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            savedUri = data.getData();
            Toast.makeText(AudioRecorder.this,
                    "Saved: " + savedUri.getPath(), Toast.LENGTH_LONG).show();
            mPlayer = null;
            mPlayer=MediaPlayer.create(this, savedUri);
            mPlayer.setLooping(true);
            pausePlayButton.setEnabled(true);
            playRecordButton.setEnabled(true);
            stopPlayButton.setEnabled(true);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayer.stop();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }


}