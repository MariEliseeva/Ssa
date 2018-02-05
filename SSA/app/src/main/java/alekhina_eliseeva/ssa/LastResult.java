package alekhina_eliseeva.ssa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import alekhina_eliseeva.ssa.controller.Controller;

public class LastResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TextView tw = (TextView) findViewById(R.id.textView);
        //tw.setText(Controller.getEmail());

        ArrayList<String> result = new ArrayList<>();
        ArrayAdapter arrayAdapter;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_result);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);
        Controller.getResult(arrayAdapter, result);

        ListView list = (ListView) findViewById(R.id.List);
        /* TODO Запрос к контроллеру список возможных песен
            songNames = controller.getListSong();
         */
        list.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }
}