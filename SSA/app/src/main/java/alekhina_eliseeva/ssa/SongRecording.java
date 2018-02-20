package alekhina_eliseeva.ssa;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
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
    private static final int PERMISSION_REQUEST_RECORD = 1;
    private static final int PERMISSION_REQUEST_READ = 2;
    private static final int PERMISSION_REQUEST_WRITE = 3;
    private AudioRecording audioRecording;
    protected String absolutePathSong;
    protected boolean type;

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




    protected void next() {
        Intent intent = new Intent(SongRecording.this, PlaySong.class);
        intent.putExtra("SongFile", absolutePathSong);
        startActivity(intent);
        finish();
    }

    protected void setType() {
        type = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_recording);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setType();
        getPermissionRecorder();
        getPermissionReadWrite();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final ImageButton startRecord = (ImageButton) findViewById(R.id.startRecord);
        final ImageButton stopRecord = (ImageButton) findViewById(R.id.stopRecord);
        final Button nextButton = (Button) findViewById(R.id.next);
        audioRecording = new AudioRecording();
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
                audioRecording.initAudioRecord(MediaRecorder.AudioSource.MIC, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                audioRecording.setIsRecording(true);
                try {
                    audioRecording.startRecording();
                } catch (Exception e) {
                    Log.d("SongRecording", e.getMessage());
                }
                audioRecording.readStart();
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
                audioRecording.stop();
                audioRecording.setIsRecording(false);
                absolutePathSong = audioRecording.save(type);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButton.setVisibility(View.GONE);
                nextButton.setEnabled(false);
                audioRecording.stopRecording();
                next();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioRecording.stopRecording();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SongRecording.this);
        alert.setMessage("Вы уверены, что хотите выйти? ");
        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                audioRecording.stopRecording();
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
