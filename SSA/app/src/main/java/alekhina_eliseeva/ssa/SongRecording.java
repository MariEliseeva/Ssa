package alekhina_eliseeva.ssa;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.support.v7.app.AlertDialog;

import alekhina_eliseeva.ssa.controller.Controller;

public class SongRecording extends AppCompatActivity {
    protected static final int PERMISSION_REQUEST_RECORD = 1;
    protected static final int PERMISSION_REQUEST_READ = 2;
    protected static final int PERMISSION_REQUEST_WRITE = 3;
    protected static int bufferSizeForRecord = 0;
    protected AudioRecord recorder;
    protected volatile boolean isRecording = false;
    protected volatile byte[] bufferForSong;
    protected final int bufferSizeForMusic = 6859776 * 8;
    protected volatile int countByteSong = 0;
    protected String absolutePathSong;


    protected void getPermissionRecorder() {
        if (ContextCompat.checkSelfPermission(SongRecording.this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SongRecording.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_RECORD);
        }
    }

    protected void getPermissionReadWrite() {
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

    protected void initAudioRecord(int audioSourse, int channelConfig, int audioFormat) {
        int sampleRatelnHz = 44100;
        bufferSizeForRecord = AudioRecord.getMinBufferSize(sampleRatelnHz, channelConfig, audioFormat);
        recorder = new AudioRecord(audioSourse, sampleRatelnHz, channelConfig, audioFormat, bufferSizeForRecord * 10);
    }


    protected void next() {
        Intent intent = new Intent(SongRecording.this, PlaySong.class);
        intent.putExtra("SongFile", absolutePathSong);
        startActivity(intent);
        finish();
    }

    protected void save() {
        absolutePathSong = SaveFile.saveMusic(countByteSong, bufferForSong);
    }

    private void stopRecording() {
        bufferForSong = null;
        if (recorder != null) {
            recorder.stop();
            isRecording = false;
        }
    }

    public void readStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (recorder == null)
                    return;
                countByteSong = 0;
                bufferForSong = new byte[bufferSizeForMusic];
                int readCount;
                while (isRecording && (recorder != null)) {
                    readCount = recorder.read(bufferForSong, 0, bufferSizeForMusic);
                    countByteSong += readCount;
                }

            }
        }).start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_recording);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getPermissionRecorder();
        getPermissionReadWrite();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                initAudioRecord(MediaRecorder.AudioSource.MIC, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                isRecording = true;
                try {
                    recorder.startRecording();
                } catch (Exception e) {
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
                isRecording = false;
                save();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButton.setVisibility(View.GONE);
                nextButton.setEnabled(false);
                stopRecording();
                next();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SongRecording.this);
        alert.setMessage("Вы уверены, что хотите выйти? ");
        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopRecording();
                Controller.cancel();
                Intent intent = new Intent(SongRecording.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }
}
