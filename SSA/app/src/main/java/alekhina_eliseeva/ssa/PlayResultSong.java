package alekhina_eliseeva.ssa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import alekhina_eliseeva.ssa.controller.Controller;

public class PlayResultSong extends PlaySong {
    @Override
    public void next() {
        Intent intent = new Intent(PlayResultSong.this, SelectTitle.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PlayResultSong.this);
        alert.setMessage("Вы уверены, что хотите выйти? выход = проигрыш");
        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                Controller.fixResult(false);
                Intent intent = new Intent(PlayResultSong.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }
}
