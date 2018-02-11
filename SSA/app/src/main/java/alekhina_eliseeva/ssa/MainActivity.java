package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Вход и регистрация
 */
public class MainActivity extends AppCompatActivity {
    protected static final int PERMISSION_REQUEST_RECORD = 1;
    protected static final int PERMISSION_REQUEST_READ = 2;
    protected static final int PERMISSION_REQUEST_WRITE = 3;

    protected void getPermissionRecorder() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_RECORD);
        }
    }

    protected void getPermissionReadWrite() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissionRecorder();
        getPermissionReadWrite();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button buttonFromMainToSignUp = (Button) findViewById(R.id.ButtonFromMainToSignUp);
        buttonFromMainToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
                finish();
            }
        });

        Button buttonFromMainToLogIn = (Button) findViewById(R.id.ButtonFromMainToLogIn);
        buttonFromMainToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LogIn.class);
                startActivity(i);
                finish();
            }
        });
    }

    private static long backPressed = 0;

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти",
                    Toast.LENGTH_SHORT).show();
        backPressed = System.currentTimeMillis();
    }

}