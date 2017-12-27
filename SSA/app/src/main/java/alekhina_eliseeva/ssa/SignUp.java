package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import alekhina_eliseeva.ssa.controller.Controller;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button buttonSignUp = (Button) findViewById(R.id.ButtonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameTextView = (EditText) findViewById(R.id.TextEnterName);
                String name = nameTextView.getText().toString();
                EditText passwordTextView = (EditText) findViewById(R.id.TextEnterPassword);
                String password = passwordTextView.getText().toString();
                EditText confirmPasswordTextView = (EditText) findViewById(R.id.TextConfirmPassword);
                String confirmPassword = confirmPasswordTextView.getText().toString();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUp.this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
                }
                else {

                    /* Вызов корректности имени */
                    /* TODO*/  boolean flag = true;

                    /* TODO Вызов корректности имени*/  //boolean flag = (name.charAt(0) == 'o');

                    if (flag) {
                        Controller.signUp(name, password, "MARI");
                        //Нужно окошечко для имени, т.к. регистрация по email
                        //И окошечки для email -- обязательно должн быть textEmailAddress
                        Intent i = new Intent(SignUp.this, Menu.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(SignUp.this, "Данное имя уже существует", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
