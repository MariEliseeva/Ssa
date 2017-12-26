package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AnswerOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_option);

        final ListView answers = (ListView) findViewById(R.id.Answers);
        final ArrayList<String> answersList = new ArrayList<>();
        final ArrayAdapter arrayAdapter =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, answersList);
        final EditText addAnswer = (EditText) findViewById(R.id.addAnswer);
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
                String whatToDo = answersList.get(i);
                //TODO отправляю Маше правильный ответ
                Intent intent = new Intent(AnswerOption.this, Menu.class);
                startActivity(intent);
            }
        });
    }
}
