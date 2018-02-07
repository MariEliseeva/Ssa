package alekhina_eliseeva.ssa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

public class PlayReverseSong extends PlaySong {
    @Override
    public void next() {
        //TODO возможно, после того, как мы воспроизвели песню, ее надо удалить, но тогда перестанет работать кнопка назад
        Intent intent = new Intent(PlayReverseSong.this, ReverseSongRecording.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PlayReverseSong.this);
        alert.setMessage("Вы уверены, что хотите выйти? Выход = проигрыш ");
        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                //TODO сказать контроллеру, что автоматически проигрывает
                Intent intent = new Intent(PlayReverseSong.this, Menu.class);
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
