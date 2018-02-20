package ru.mit.spbau.alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

import ru.mit.spbau.alekhina_eliseeva.ssa.controller.Controller;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                    Controller.signUp(name, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent intent = new Intent(SignUp.this, Menu.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUp.this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
                        }
                    });
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
