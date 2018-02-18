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

import static java.lang.Math.min;

public class Applications extends AppCompatActivity {
    public void next(byte[] bytes) {
        int count = min(132300, bytes.length);
        String absolutePathPieceSong = SaveFile.saveMusic(count, bytes);
        Intent intent = new Intent(Applications.this, PlayReverseSong.class);
        intent.putExtra("SongFile", absolutePathPieceSong);
        intent.putExtra("SongBytes", SaveFile.saveBytes(bytes));
        intent.putExtra("SongSize", bytes.length);
        intent.putExtra("StartByteNumber", count);
        intent.putExtra("RecordBytes", SaveFile.saveBytes(new byte[0]));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);
        ListView applicationsList = (ListView) findViewById(R.id.ListApplications);
        final ArrayList<String> nameUserWhoWantPlay = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameUserWhoWantPlay);
        applicationsList.setAdapter(arrayAdapter);
        Controller.getSuggestList(arrayAdapter, nameUserWhoWantPlay);
        // если сделаешь кнопочку "заигнорить", то можно вызывать метод ignore(email)
        applicationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Controller.getSong(Applications.this, nameUserWhoWantPlay.get(i));
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Applications.this, Menu.class);
        startActivity(intent);
        finish();
    }
}
