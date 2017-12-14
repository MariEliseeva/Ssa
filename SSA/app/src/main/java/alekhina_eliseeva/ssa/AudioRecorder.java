package alekhina_eliseeva.ssa;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AudioRecorder extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_RECORD = 1;
    private static final int PERMISSION_REQUEST_READ = 2;
    private static final int PERMISSION_REQUEST_WRITE = 3;
    private AudioRecord mRecord;
    boolean isReading, isRecording;
    private byte[] myBuffer;
    private int bufferSizeForRecord = 0;
    final int bufferSizeForMusic = 6859776 * 32;
    private int totalCount = 0;
    private RandomAccessFile randomAccessWriter;
    private MediaPlayer player;
//TODO Если Вам жалко свои глаза, то не смотрите.

    private void help() {
        File wfile = new File(Environment.getExternalStorageDirectory() + File.separator + "music2.wav");
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
        int value = totalCount;
        header[43] = (byte) (value >>> 24);
        header[42] = (byte) (value >>> 16);
        header[41] = (byte) (value >>> 8);
        header[40] = (byte) value;
        Log.d("MyLog", ((Integer) totalCount).toString());
        String ans = "";
        for (int i = 0; i < header.length; i++) {
            Integer a = (int) ((short) header[i]);
            ans = ans + "\n" + "header[" + ((Integer) i).toString() + "] =  " + a.toString();
        }
        Log.d("MyLog", ans);
        /*int i = 0;
        int j = totalCount - 4;
        while (i < byteInput.length) {
            byteOutput[i] = myBuffer[j];
            byteOutput[i+1] = myBuffer[j+1];
            byteOutput[i+2] = myBuffer[j+2];
            byteOutput[i + 3] = myBuffer[j + 3];
            j -= 4;
            if (j < 0) {
                j = totalCount - 4;
            }
            i+=4;
        }*/
        try {
            FileOutputStream fos = new FileOutputStream(wfile);
            fos.write(header, 0, header.length);
            fos.write(myBuffer, 44, totalCount);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getPermissionRecorder() {
        if (ContextCompat.checkSelfPermission(AudioRecorder.this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AudioRecorder.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_RECORD);
        }
    }

    private void getPermissionReadWrite() {
        if (ContextCompat.checkSelfPermission(AudioRecorder.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AudioRecorder.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ);
        }

        if (ContextCompat.checkSelfPermission(AudioRecorder.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AudioRecorder.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE);
        }
    }

    private void initAudioRecord(int audioSourse, int sampleRatelnHz, int channelConfig, int audioFormat) {
        bufferSizeForRecord = AudioRecord.getMinBufferSize(sampleRatelnHz, channelConfig, audioFormat);
        mRecord = new AudioRecord(audioSourse, sampleRatelnHz, channelConfig, audioFormat, bufferSizeForRecord * 10);
    }

    private void createMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource(Environment.getExternalStorageDirectory() + File.separator + "music2.wav");
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
        } catch (IOException e) {
            Toast.makeText(AudioRecorder.this, "createMediaPlayer() " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void createWavFile(String name) {
        Toast.makeText(AudioRecorder.this, name, Toast.LENGTH_LONG).show();
        File song = new File(name);
        try {
            song.createNewFile();
        } catch (IOException e) {
            Toast.makeText(AudioRecorder.this, "createWavFile " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void readStart() {
        isReading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mRecord == null)
                    return;

                myBuffer = new byte[bufferSizeForMusic];
                int readCount = 0;
                while (isReading) {
                    readCount = mRecord.read(myBuffer, 0, bufferSizeForMusic);
                    totalCount += readCount;
                }
            }
        }).start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        getPermissionRecorder();
        getPermissionReadWrite();
        Button start = (Button) findViewById(R.id.startRecord);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    initAudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                    isRecording = true;
                    mRecord.startRecording();
                    readStart();
                } catch (IllegalStateException e) {
                    Toast.makeText(AudioRecorder.this, "start " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        Button stop = (Button) findViewById(R.id.pauseRecord);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (null != mRecord) {
                        isRecording = false;
                        isReading = false;
                        mRecord.stop();
                        mRecord.release();
                        mRecord = null;
                        createWavFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "1.wav");
                    }
                } catch (IllegalStateException e) {
                    Toast.makeText(AudioRecorder.this, "stop " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });


        final Button play = (Button) findViewById(R.id.playRecord);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help();
                if (player == null) {
                    createMediaPlayer();
                }
                player.start();
            }
        });

        Button stopPlay = (Button) findViewById(R.id.stopPlaySecord);
        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null && player.isPlaying()) {
                    player.pause();
                    player.stop();
                    player = null;
                }
            }
        });


    }


}