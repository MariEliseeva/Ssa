package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import alekhina_eliseeva.ssa.controller.Controller;

public class Applications extends AppCompatActivity {
    private ArrayList<Byte> list = new ArrayList<>();

    public void next() {
        byte[] bytes = new byte[list.size() + 45];
        for (int i = 0; i < list.size(); i++) {
            bytes[i] = list.get(i);
        }
        String abdolutePathSong = SaveWavFile.saveMusic(list.size(), bytes);
        Intent intent = new Intent(Applications.this, PlayReverseSong.class);
        intent.putExtra("SongFile", abdolutePathSong);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);
        ListView applicationsList = (ListView) findViewById(R.id.ListApplications);
        final ArrayList<String> nameUserWhoWantPlay = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameUserWhoWantPlay);
        applicationsList.setAdapter(arrayAdapter);
        Controller.getSuggestList(arrayAdapter, nameUserWhoWantPlay);
        // выводит список предложений
        // если сделаешь кнопочку "заигнорить", то можно вызывать метод ignore(email)
        // не проверила работает ли, т.к. кнопочки нет, но там легко исрпавлять, если не работает -- пиши
        applicationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter arrayAdapterMary = new ArrayAdapter(Applications.this, android.R.layout.simple_list_item_1, list);
                Controller.getSong(Applications.this, arrayAdapterMary, list, nameUserWhoWantPlay.get(i), "0");
            }
        });
    }
}
