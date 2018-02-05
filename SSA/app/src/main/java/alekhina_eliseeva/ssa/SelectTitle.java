package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import alekhina_eliseeva.ssa.controller.Controller;

public class SelectTitle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_title);

        ListView selectListView = (ListView) findViewById(R.id.SelectRightAnswer);
        final ArrayList<String> variantiesList = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, variantiesList);
        selectListView.setAdapter(arrayAdapter);
        Controller.getVariants(arrayAdapter, variantiesList);
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
            }
        });
    }
}
