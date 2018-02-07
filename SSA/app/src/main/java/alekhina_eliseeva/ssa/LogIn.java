package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import alekhina_eliseeva.ssa.controller.Controller;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        final SharedPreferences sharedPreferences = getSharedPreferences("StoreData", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String password = sharedPreferences.getString("password", "");

        //TODO возможно хранить логин-пароль не нужно,
        //можешь спрашивать у контроллера -- есть ли юзер,
        //который залогинился в предыдущий раз: Controller.isUser()

        if (name.length() > 0 && password.length() > 0) {
            Intent intent = new Intent(LogIn.this, Menu.class);
            startActivity(intent);
            finish();
        } else {
            Button buttonLogIn = (Button) findViewById(R.id.ButtonLogIn);
            buttonLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    EditText nameTextView = (EditText) findViewById(R.id.TextAddName);
                    EditText passwordTextView = (EditText) findViewById(R.id.TextAddPassword);
                    String newName = nameTextView.getText().toString();
                    String newPassword = passwordTextView.getText().toString();
                    //TODO запрос на корректность
                    Controller.signIn(newName, newPassword);
                    boolean flag = true;
                    if (flag) {
                        editor.putString("name", newName);
                        editor.putString("password", newPassword);
                        editor.commit();
                        Intent intent = new Intent(LogIn.this, Menu.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LogIn.this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LogIn.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
