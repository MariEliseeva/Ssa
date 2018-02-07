package alekhina_eliseeva.ssa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

public class PlayReverseSong extends PlaySong {
    @Override
    public void next() {
        Intent prevIntent = getIntent();
        String songBytesFileName  = prevIntent.getStringExtra("SongBytes");
        int songSize = prevIntent.getIntExtra("SongSize", 0);
        int startByteNumber = prevIntent.getIntExtra("StartByteNumber", 0);
        String recordBytesFileName = prevIntent.getStringExtra("RecordBytes");

        Intent intent = new Intent(PlayReverseSong.this, ReverseSongRecording.class);
        //TODO передать файл для записи и файл для чтения и позицию

        intent.putExtra("SongBytes", songBytesFileName);
        intent.putExtra("SongSize", songSize);
        intent.putExtra("StartByteNumber", startByteNumber);
        intent.putExtra("RecordBytes", recordBytesFileName);
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
