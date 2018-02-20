package ru.mit.spbau.alekhina_eliseeva.ssa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.mit.spbau.alekhina_eliseeva.ssa.controller.Controller;

public class SelectTitle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_title);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ListView selectListView = (ListView) findViewById(R.id.SelectRightAnswer);
        final ArrayList<String> variantsList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, variantsList);
        selectListView.setAdapter(arrayAdapter);
        Controller.getVariants(arrayAdapter, variantsList);
        selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == Controller.getRightAnswer()) {
                    Controller.fixResult(true);
                    Toast.makeText(SelectTitle.this, "ЭТО ПОБЕДА!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Controller.fixResult(false);
                    Toast.makeText(SelectTitle.this, "=( Вы проиграли", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(SelectTitle.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SelectTitle.this);
        alert.setMessage("Вы уверены, что хотите выйти? выход = проигрыш");
        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Controller.fixResult(false);
                Intent intent = new Intent(SelectTitle.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }
}
