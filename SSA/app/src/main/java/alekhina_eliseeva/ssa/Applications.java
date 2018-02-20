package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import alekhina_eliseeva.ssa.controller.Controller;

import static java.lang.Math.min;

public class Applications extends AppCompatActivity {
    public void next(byte[] bytes) {

    }

    private final AtomicBoolean isEnd = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);
        ListView applicationsList = (ListView) findViewById(R.id.ListApplications);
        final ArrayList<String> nameUserWhoWantPlay = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameUserWhoWantPlay);
        applicationsList.setAdapter(arrayAdapter);
        Controller.getSuggestList(arrayAdapter, nameUserWhoWantPlay);
        applicationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Task<byte[]>> tasks = new ArrayList<>();
                Controller.getSong(tasks, isEnd, nameUserWhoWantPlay.get(i));
                synchronized (isEnd) {
                    while (!isEnd.get()) {
                        try {
                            isEnd.wait();
                        } catch (Exception e) {
                        }
                    }
                }
                tasks.get(0).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
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
                });
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
