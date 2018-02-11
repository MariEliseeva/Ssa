package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import alekhina_eliseeva.ssa.controller.Controller;

public class SignUp extends AppCompatActivity {
    public void next() {
        Intent intent = new Intent(SignUp.this, Menu.class);
        startActivity(intent);
        finish();
    }

    public void notNext() {
        Toast.makeText(SignUp.this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
    }

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
                } else {
                    Controller.signUp(SignUp.this, name, password);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
