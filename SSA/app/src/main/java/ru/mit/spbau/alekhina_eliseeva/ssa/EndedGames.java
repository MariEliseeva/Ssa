package ru.mit.spbau.alekhina_eliseeva.ssa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.mit.spbau.alekhina_eliseeva.ssa.controller.Controller;

public class EndedGames extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> songNames = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ended_games);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songNames);
        Controller.getResults(arrayAdapter, songNames);

        ListView songList = (ListView) findViewById(R.id.ListEndedGames);
        songList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EndedGames.this, Menu.class);
        startActivity(intent);
        finish();
    }
}
