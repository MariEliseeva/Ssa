package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import alekhina_eliseeva.ssa.controller.Controller;

public class Applications extends AppCompatActivity {
    private ArrayList<Byte> list = new ArrayList<>();

    public void next() {
        Log.e("NEXT", "ASaa");
        SaveWavFile saveWavFile = new SaveWavFile();
        byte[] bytes = new byte[list.size() + 45];
        for (int i = 0; i < list.size(); i++) {
            bytes[i] = list.get(i);
        }
        Log.e("MyLog", ((Integer)list.size()).toString());
        saveWavFile.saveMusic(list.size(), bytes);
        Intent intent = new Intent(Applications.this, PlayReverseSong.class);
        intent.putExtra("SongFile", saveWavFile.getPath());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);

        ListView applicationsList = (ListView) findViewById(R.id.ListApplications);
        final ArrayList<String> nameUserWhoWantPlay = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameUserWhoWantPlay);
        /* TODO Запрос к контроллеру выдать список заявок
            songNames = controller.getListApplications();
         */
        Log.e("AAA", "asdhcjasdc");
        applicationsList.setAdapter(arrayAdapter);
        Controller.getSuggestList(arrayAdapter, nameUserWhoWantPlay);
        // выводит список предложений
        // если сделаешь кнопочку "заигнорить", то можно вызывать метод ignore(email)
        // не проверила работает ли, т.к. кнопочки нет, но там легко исрпавлять, если не работает -- пиши
        applicationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* TODO передаем контроллеру с кем хотим играть nameUserWhoWantPlay.get(i);*/
                /*TODO получаем байты*/
                /*SaveWavFile saveWavFile = new SaveWavFile();
                saveWavFile.saveMusic(len, "1.wav", bytes);
                */
                ArrayAdapter arrayAdapterMary = new ArrayAdapter(Applications.this, android.R.layout.simple_list_item_1, list);
                Controller.getSong(Applications.this, arrayAdapterMary, list, nameUserWhoWantPlay.get(i), "0");

            }
        });
    }
}
