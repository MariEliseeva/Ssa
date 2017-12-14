package alekhina_eliseeva.ssa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
    private boolean isReading = false;
    private byte[] bufferForSong;
    private byte[] bufferForSave;
    private final int bufferSizeForMusic = 6859776 * 16;
    private int countByteSong = 0;
    private int countByteForSave = 0;
    private int prevOper = -1;
    private File fileForSave;
    private MediaPlayer mediaPlayer;
    //TODO будет кнопка далее, где я буду передавать данные контроллеру.
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


    public void readStart() {
        isReading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (recorder == null)
                    return;
                bufferForSong = new byte[bufferSizeForMusic];
                int readCount = 0;

                while (isReading && (recorder != null)) {
                    readCount = recorder.read(bufferForSong, 0, bufferSizeForMusic);
                    countByteSong += readCount;
                }

            }
        }).start();
    }

    private void copy() {
        for (int i = 0; i < countByteSong; i++) {
            bufferForSave[i + countByteForSave] = bufferForSong[i];
        }
        countByteForSave += countByteSong;

        Log.d("MyLog", ((Integer) countByteForSave).toString() + "  " + ((Integer) countByteSong).toString());
    }

    private void createFileForSave() {
        File dirForSSA = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA");
        fileForSave = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA" + File.separator + "music.wav");
        try {
            dirForSSA.mkdirs();
            fileForSave.createNewFile();
        } catch (Exception e) {
            Log.d("MyLog", e.getMessage());
        }
    }

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
        int value = countByteForSave;
        header[43] = (byte) (value >>> 24);
        header[42] = (byte) (value >>> 16);
        header[41] = (byte) (value >>> 8);
        header[40] = (byte) value;
        try {
            FileOutputStream fos = new FileOutputStream(fileForSave);
            fos.write(header, 0, header.length);
            fos.write(bufferForSave, 44, countByteForSave);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileForSave.getAbsolutePath());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.d("MyLog", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_recording);
        final ImageButton startRecord = (ImageButton) findViewById(R.id.startRecord);
        final ImageButton stopRecord = (ImageButton) findViewById(R.id.stopRecord);
        final ImageButton pauseRecord = (ImageButton) findViewById(R.id.pauseRecord);
        final ImageButton startPlay = (ImageButton) findViewById(R.id.startPlay);
        final ImageButton stopPlay = (ImageButton) findViewById(R.id.stopPlay);
        final ImageButton pausePlay = (ImageButton) findViewById(R.id.pausePlay);
        stopRecord.setVisibility(View.GONE);
        pauseRecord.setVisibility(View.GONE);
        stopPlay.setVisibility(View.GONE);
        pausePlay.setVisibility(View.GONE);
        startPlay.setVisibility(View.GONE);
        stopRecord.setEnabled(false);
        pauseRecord.setEnabled(false);
        stopPlay.setEnabled(false);
        pausePlay.setEnabled(false);
        startPlay.setEnabled(false);
        bufferForSave = new byte[bufferSizeForMusic];
        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecord.setEnabled(false);
                startRecord.setVisibility(View.GONE);

                pauseRecord.setEnabled(true);
                pauseRecord.setVisibility(View.VISIBLE);

                stopRecord.setEnabled(true);
                stopRecord.setVisibility(View.VISIBLE);
                if (prevOper == 2) {
                    bufferForSave = null;
                    bufferForSave = new byte[bufferSizeForMusic];
                    countByteForSave = 0;
                }
                if (bufferForSong != null) {
                    bufferForSong = null;
                }
                initAudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                isRecording = true;
                recorder.startRecording();
                readStart();
            }
        });

        pauseRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevOper = 1;
                recorder.stop();
                isRecording = false;
                isReading = false;
                recorder.release();

                pauseRecord.setEnabled(false);
                pauseRecord.setVisibility(View.GONE);

                startRecord.setEnabled(true);
                startRecord.setVisibility(View.VISIBLE);

                stopRecord.setEnabled(true);
                stopRecord.setVisibility(View.VISIBLE);
                copy();

            }
        });

        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prevOper != 1) {
                    recorder.stop();
                    isRecording = false;
                    isReading = false;
                    recorder.release();
                }


                stopRecord.setEnabled(false);
                stopRecord.setVisibility(View.GONE);

                startRecord.setEnabled(false);
                startRecord.setVisibility(View.GONE);

                pauseRecord.setEnabled(false);
                pauseRecord.setVisibility(View.GONE);

                startPlay.setEnabled(true);
                startPlay.setVisibility(View.VISIBLE);
                if (prevOper != 1) {
                    copy();
                }
                prevOper = 2;

            }
        });

        startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlay.setEnabled(false);
                startPlay.setVisibility(View.GONE);

                stopPlay.setEnabled(true);
                stopPlay.setVisibility(View.VISIBLE);

                pausePlay.setEnabled(true);
                pausePlay.setVisibility(View.VISIBLE);
                createFileForSave();
                saveMusic();
                createMediaPlayer();
                mediaPlayer.start();
            }
        });

        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlay.setEnabled(false);
                pausePlay.setVisibility(View.GONE);

                startPlay.setEnabled(true);
                startPlay.setVisibility(View.VISIBLE);

                stopPlay.setEnabled(true);
                stopPlay.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
            }
        });

        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlay.setEnabled(false);
                stopPlay.setVisibility(View.GONE);

                pausePlay.setEnabled(false);
                pausePlay.setVisibility(View.GONE);

                startPlay.setEnabled(false);
                startPlay.setVisibility(View.GONE);

                startRecord.setEnabled(true);
                startRecord.setVisibility(View.VISIBLE);
                mediaPlayer.stop();
                mediaPlayer = null;
            }
        });


    }
}
