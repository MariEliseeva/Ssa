package alekhina_eliseeva.ssa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SongRecording extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_RECORD = 1;
    private static final int PERMISSION_REQUEST_READ = 2;
    private static final int PERMISSION_REQUEST_WRITE = 3;
    private static int bufferSizeForRecord = 0;
    private AudioRecord recorder;
    private boolean isRecording = false;
    private byte[] bufferForSong;
    private final int bufferSizeForMusic = 6859776 * 16;
    private int countByteSong = 0;
    private File fileForSave;
    final SaveWavFile saveWavFile = new SaveWavFile();


    private void getPermissionRecorder() {
        if (ContextCompat.checkSelfPermission(SongRecording.this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SongRecording.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_RECORD);
        }
    }

    private void getPermissionReadWrite() {
        if (ContextCompat.checkSelfPermission(SongRecording.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SongRecording.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ);
        }

        if (ContextCompat.checkSelfPermission(SongRecording.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SongRecording.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE);
        }
    }

    private void initAudioRecord(int audioSourse, int sampleRatelnHz, int channelConfig, int audioFormat) {
        bufferSizeForRecord = AudioRecord.getMinBufferSize(sampleRatelnHz, channelConfig, audioFormat);
        recorder = new AudioRecord(audioSourse, sampleRatelnHz, channelConfig, audioFormat, bufferSizeForRecord * 10);
    }


    public void next() {
        Intent intent = new Intent(SongRecording.this, PlaySong.class);
        intent.putExtra("SongFile", saveWavFile.getPath());
        startActivity(intent);
    }

    public void readStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (recorder == null)
                    return;
                countByteSong = 0;
                bufferForSong = new byte[bufferSizeForMusic];
                int readCount = 0;

                while (isRecording && (recorder != null)) {
                    readCount = recorder.read(bufferForSong, 0, bufferSizeForMusic);
                    countByteSong += readCount;
                }

            }
        }).start();
    }

    private void createFileForSave() {
        File dirForSSA = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA");
        fileForSave = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA" + File.separator + "music.wav");
        try {
            dirForSSA.mkdirs();
            fileForSave.createNewFile();
        } catch (Exception e) {
            Log.d("SongRecording", e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_recording);
        getPermissionRecorder();
        getPermissionReadWrite();
        final ImageButton startRecord = (ImageButton) findViewById(R.id.startRecord);
        final ImageButton stopRecord = (ImageButton) findViewById(R.id.stopRecord);
        final Button nextButton = (Button) findViewById(R.id.next);
        stopRecord.setVisibility(View.GONE);
        stopRecord.setEnabled(false);
        nextButton.setVisibility(View.GONE);
        nextButton.setEnabled(false);
        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecord.setVisibility(View.GONE);
                startRecord.setEnabled(false);
                stopRecord.setVisibility(View.VISIBLE);
                stopRecord.setEnabled(true);
                initAudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                isRecording = true;
                try {
                    recorder.startRecording();
                }
                catch (Exception e) {
                    Log.d("SongRecording", e.getMessage());
                }
                readStart();
            }
        });

        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecord.setVisibility(View.GONE);
                stopRecord.setEnabled(false);
                startRecord.setVisibility(View.VISIBLE);
                startRecord.setEnabled(true);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setEnabled(true);
                recorder.stop();
                recorder = null;
                isRecording = false;
                saveWavFile.saveMusic(countByteSong, "music.wav", bufferForSong);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bufferForSong = null;
                if (recorder!= null) {
                    recorder.stop();
                    isRecording = false;
                }
                next();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bufferForSong = null;
        if (recorder!= null) {
            recorder.stop();
            isRecording = false;
        }
    }
}
