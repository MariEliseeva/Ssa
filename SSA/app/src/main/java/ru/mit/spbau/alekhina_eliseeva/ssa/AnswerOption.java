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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.mit.spbau.alekhina_eliseeva.ssa.controller.Controller;

public class AnswerOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_option);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final ListView answers = (ListView) findViewById(R.id.Answers);
        final ArrayList<String> answersList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, answersList);
        final EditText addAnswer = (EditText) findViewById(R.id.addAnswer);
        final TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Введите 4 варианта ответа и нажмите на правильный");

        answers.setAdapter(arrayAdapter);
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAnswer = addAnswer.getText().toString();
                answersList.add(newAnswer);
                arrayAdapter.notifyDataSetChanged();
                addAnswer.setText("");
                if (answersList.size() == 4) {
                    addAnswer.setFocusable(false);
                }
            }
        });

        answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tmp = answersList.get(0);
                answersList.set(0, answersList.get(i));
                answersList.set(i, tmp);
                Controller.addNames(answersList.get(0), answersList.get(1), answersList.get(2), answersList.get(3));
                Intent intent = new Intent(AnswerOption.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AnswerOption.this);
        alert.setMessage("Вы уверены, что хотите выйти? ");
        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Controller.cancel();
                Intent intent = new Intent(AnswerOption.this, Menu.class);
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
