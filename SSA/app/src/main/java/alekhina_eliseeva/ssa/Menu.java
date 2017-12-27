package alekhina_eliseeva.ssa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import alekhina_eliseeva.ssa.controller.Controller;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ListView menuList = (ListView) findViewById(R.id.ListMenu);
        final ArrayList<String> menuString = new ArrayList<>();
        ArrayAdapter arrayAdapter =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, menuString);
        menuList.setAdapter(arrayAdapter);
        menuString.add("Играть с другом");
        menuString.add("Заявки на игру");
        menuString.add("Активные игры");
        menuString.add("Текущая игра");
        menuString.add("Рейтинг");
        menuString.add("Выйти");
        arrayAdapter.notifyDataSetChanged();

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String whatToDo = menuString.get(i);

                if (whatToDo.equals("Играть с другом")) {
                    /* TODO отправляем информацию о том, что будем играть с другом */
                    AlertDialog.Builder alert = new AlertDialog.Builder(Menu.this);
                    alert.setMessage("Введите имя того, с кем хотите играть");
                    final EditText input = new EditText(Menu.this);
                    alert.setView(input);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String emailFriend = input.getText().toString();
                            Controller.suggest(emailFriend);
                            /* TODO отправляем контроллеру имя пользователя*/
                            Intent intent = new Intent(Menu.this, SongRecording.class);
                            startActivity(intent);
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
                }
                if (whatToDo.equals("Рейтинг")) {
                    Intent intent = new Intent(Menu.this, Top.class);
                    startActivity(intent);
                }
                if (whatToDo.equals("Текущая игра")) {
                    //Intent intent = new Intent(Menu.this, LastResult.class);
                    //startActivity(intent);
                }
                if (whatToDo.equals("Выйти")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Menu.this);
                    alert.setMessage("Сохранить логин и пароль для следующего входа?");
                    alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Menu.this, MainActivity.class);
                            startActivity(intent);
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
                            editor.commit();
                            Intent intent = new Intent(Menu.this, MainActivity.class);
                            startActivity(intent);
                            Controller.signOut();
                            //возможно хранить логин-пароль не нужно и оно хранится само
                        }
                    });
                    alert.show();
                }
            }
        });
    }
}
