package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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
        nameUserWhoWantPlay.add("Vasya");
        nameUserWhoWantPlay.add("Olya");
        nameUserWhoWantPlay.add("Masha");
        nameUserWhoWantPlay.add("Vadik");
        arrayAdapter.notifyDataSetChanged();
        applicationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* TODO передаем контроллеру с кем хотим играть nameUserWhoWantPlay.get(i);*/
                
            }
        });
    }
}
