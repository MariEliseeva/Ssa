package alekhina_eliseeva.ssa;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alekhina_eliseeva.ssa.controller.Controller;

public class Menu extends AppCompatActivity {
    protected static final int PERMISSION_REQUEST_RECORD = 1;
    protected static final int PERMISSION_REQUEST_READ = 2;
    protected static final int PERMISSION_REQUEST_WRITE = 3;

    protected void getPermissionRecorder() {
        if (ContextCompat.checkSelfPermission(Menu.this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Menu.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_RECORD);
        }
    }

    protected void getPermissionReadWrite() {
        if (ContextCompat.checkSelfPermission(Menu.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Menu.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ);
        }

        if (ContextCompat.checkSelfPermission(Menu.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Menu.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE);
        }
    }

    private static void deleteUnnecessaryFiles() {
        Pattern p = Pattern.compile("music.*\\.wav");
        File dirForSSA = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA");
        if (dirForSSA.exists()) {
            File[] files = dirForSSA.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                Matcher m = p.matcher(fileName);
                if (m.matches()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        Log.e("Menu", "can't delete " + file.getAbsolutePath());
                    }
                }
            }
        }

        Pattern p1 = Pattern.compile("text.*\\.bin");
        if (dirForSSA.exists()) {
            File[] files = dirForSSA.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                Matcher m = p1.matcher(fileName);
                if (m.matches()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        Log.e("Menu", "can't delete " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public void next() {
        Intent intent = new Intent(Menu.this, SongRecording.class);
        startActivity(intent);
        finish();
    }

    public void notNext() {
        Toast.makeText(Menu.this, "Неправильный логин друга", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getPermissionRecorder();
        getPermissionReadWrite();
        deleteUnnecessaryFiles();
        ListView menuList = (ListView) findViewById(R.id.ListMenu);
        final ArrayList<String> menuString = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuString);
        menuList.setAdapter(arrayAdapter);
        menuString.add("Играть с другом");
        menuString.add("Заявки на игру");
        menuString.add("Завершенные игры");
        menuString.add("Рейтинг");
        menuString.add("Выйти");
        arrayAdapter.notifyDataSetChanged();

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String whatToDo = menuString.get(i);

                if (whatToDo.equals("Играть с другом")) {
                    //играем с другом
                    AlertDialog.Builder alert = new AlertDialog.Builder(Menu.this);
                    alert.setMessage("Введите email того, с кем хотите играть");
                    final EditText input = new EditText(Menu.this);
                    alert.setView(input);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String emailFriend = input.getText().toString();
                            //отправляем контроллеру email пользователя
                            Controller.suggest(Menu.this, emailFriend);

                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.show();

                }
                if (whatToDo.equals("Заявки на игру")) {
                    Intent intent = new Intent(Menu.this, Applications.class);
                    startActivity(intent);
                    finish();
                }
                if (whatToDo.equals("Рейтинг")) {
                    Intent intent = new Intent(Menu.this, Top.class);
                    startActivity(intent);
                    finish();
                }
                if (whatToDo.equals("Завершенные игры")) {
                    Intent intent = new Intent(Menu.this, EndedGames.class);
                    startActivity(intent);
                }
                if (whatToDo.equals("Выйти")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Menu.this);
                    alert.setMessage("Сохранить логин и пароль для следующего входа?");
                    alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Menu.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    alert.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = getSharedPreferences("StoreData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", "");
                            editor.putString("password", "");
                            editor.apply();
                            Intent intent = new Intent(Menu.this, MainActivity.class);
                            startActivity(intent);
                            Controller.signOut();
                            finish();
                        }
                    });
                    alert.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Menu.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
