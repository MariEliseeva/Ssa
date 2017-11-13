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
                Toast.makeText(ChooseSong.this, "Ваш выбор отправлен контроллеру", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChooseSong.this, Menu.class);
                startActivity(intent);
            }
        });
    }
}
