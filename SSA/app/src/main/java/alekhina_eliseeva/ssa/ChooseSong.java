package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import alekhina_eliseeva.ssa.controller.Controller;

public class ChooseSong extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_song);

        ListView songList = (ListView) findViewById(R.id.ListSong);
        final ArrayList<String> songNames = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, songNames);
        /* TODO Запрос к контроллеру список возможных песен
            songNames = controller.getListSong();
         */
        songList.setAdapter(arrayAdapter);
        songNames.add("Ольга Бузова - Мало половин");
        songNames.add("Ольга Бузова - Хит Парад");
        songNames.add("Ольга Бузова - Привыкаю");
        songNames.add("Ольга Бузова - Мои Люди");
        songNames.add("Ольга Бузова - Равновесие");
        songNames.add("Ольга Бузова - Под звуки поцелуев");
        songNames.add("Ольга Бузова - А люди не верили");
        arrayAdapter.notifyDataSetChanged();
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* TODO Отвпраляем контроллеру выбранную песню
                 */
                byte[] bytes = new byte[3];
                bytes[0] = 1;
                bytes[1] = 2;
                bytes[2] = 3;
                //Controller.addSong(bytes, "name1", "name2", "name3", "name4", "Eliseevamary@mail.ru");
                // кажется сейчас у нас все по-другому и возможно этого файла нет...
                // Но вот так пока записывать байты с песней.
                // и 4 варианта ответа, первый -- правильный
                // email -- того, с кем хотим играть
                // Тут нужно как-то запомнить, что других игр начинать нельзя, т.е.
                // ничего не менять в активити, пока не придет результат игры.
                //getResult(arrayadapter, arraylist, email) -- пока так например,
                // список все пустой пустой, а потом в него внезапно что-то запишется (win или lose там),
                // баллы прибавятся и можно будет закончить

                Toast.makeText(ChooseSong.this, "Ваш выбор отправлен контроллеру", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChooseSong.this, Menu.class);
                startActivity(intent);
            }
        });
    }
}
