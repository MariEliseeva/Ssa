package alekhina_eliseeva.ssa;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;

import alekhina_eliseeva.ssa.controller.Controller;

public class PlaySong extends AppCompatActivity {
    MediaPlayer mPlayer;
    Button startButton, pauseButton, stopButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        mPlayer=MediaPlayer.create(this, R.raw.sound);

        ArrayList<Byte> songNames = new ArrayList<>();
        ArrayAdapter arrayAdapter;
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, songNames);
        Controller.getSong(arrayAdapter, songNames, "Eliseevamary17@gmail.com", "");
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
        ListView songList = (ListView) findViewById(R.id.list);
        /* TODO Запрос к контроллеру список возможных песен
            songNames = controller.getListSong();
         */
        songList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        startButton = (Button) findViewById(R.id.start);
        pauseButton = (Button) findViewById(R.id.pause);
        stopButton = (Button) findViewById(R.id.stop);
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

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }
}
