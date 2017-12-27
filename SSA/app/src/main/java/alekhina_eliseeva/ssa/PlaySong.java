package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import alekhina_eliseeva.ssa.controller.Controller;


public class PlaySong extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private SeekBar seekBar;
    private String songFile;
    private byte[] song;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekBar.setMax(mediaPlayer.getDuration());
            int currentPosition = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(currentPosition);
            handler.postDelayed(this, 100);
        }
    };

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

    public void next() {
        //TODO отдать Маше байты
        getSong();
        Intent intent = new Intent(PlaySong.this, AnswerOption.class);
        startActivity(intent);
    }
    private void getSong() {
        File file = new File(songFile);
        byte[] header = new byte[44];
        song = new byte[(int) file.length() - 44];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(header, 0, header.length);
            fis.read(song, 44, song.length - 45);
        }
        catch (IOException e) {
            Log.d("PlaySong", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        //mPlayer=MediaPlayer.create(this, R.raw.sound);

//        ArrayList<Byte> songNames = new ArrayList<>();
 //       ArrayAdapter arrayAdapter;
  //      arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, songNames);
   //     Controller.getSong(arrayAdapter, songNames, "Eliseevamary17@gmail.com", "");
        //это email игрока, с которым играешь. В songNames записываются байты с песенкой.
        // ArrayAdapter оповещает, когда они записались. Т.к. я не шарю, они
        // пока просто выводятся на экран. Последний параметр -- какой кусочек ты хочешь,
        // но пока не доделано с ними
        // 4 варианта ответа -- тоже можно просить у контроллера,
        // также передавать адаптер, arraylist и mail. В возвращаемом списке первый вариант правильный,
        //так что нужно как-то перемешивать..
        // Или скажи куда -- несложно передавать правильный вариант отдельно
        // когда выбрал вариант -- вызывай fixResult(booleanValue, email) true -- если правильно ответил и тд
        // оно отправится первому игроку, а этому баллы
       // ListView songList = (ListView) findViewById(R.id.list);
        /* TODO Запрос к контроллеру список возможных песен
            songNames = controller.getListSong();
         */
        //songList.setAdapter(arrayAdapter);
        //arrayAdapter.notifyDataSetChanged();

        Intent intent = getIntent();
        handler = new Handler();
        songFile = intent.getStringExtra("SongFile");
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
                next();
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
