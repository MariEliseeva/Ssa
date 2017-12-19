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
        applicationsList.setAdapter(arrayAdapter);
        Controller.getSuggestList(arrayAdapter, nameUserWhoWantPlay);
        // выводит список предложений
        // если сделаешь кнопочку "заигнорить", то можно вызывать метод ignore(email)
        // не проверила работает ли, т.к. кнопочки нет, но там легко исрпавлять, если не работает -- пиши
        applicationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* TODO передаем контроллеру с кем хотим играть nameUserWhoWantPlay.get(i);*/
                Intent intent = new Intent(Applications.this, PlaySong.class);
                startActivity(intent);
            }
        });
    }
}
