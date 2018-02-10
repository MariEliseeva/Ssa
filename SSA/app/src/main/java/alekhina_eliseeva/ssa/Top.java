package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import alekhina_eliseeva.ssa.controller.Controller;

public class Top extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> songNames = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_song);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songNames);
        Controller.getRating(arrayAdapter, songNames);

        ListView songList = (ListView) findViewById(R.id.ListSong);
        songList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Top.this, Menu.class);
        startActivity(intent);
        finish();
    }
}
