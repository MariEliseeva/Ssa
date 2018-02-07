package alekhina_eliseeva.ssa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import alekhina_eliseeva.ssa.controller.Controller;

import static java.lang.Math.min;

public class ReverseSongRecording extends SongRecording {
    private String songFile;
    private byte[] song;

    private void getSong() {
        File file = new File(songFile);
        byte[] header = new byte[44];
        song = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            int countReadBytes = fis.read(header);
            if (countReadBytes < header.length) {
                Log.e("ReverseSongRecording", "header reading error");
            }
            countReadBytes = fis.read(song);
            if (countReadBytes < song.length - 45) {
                Log.e("ReverseSongRecording", "song reading error");
            }
        } catch (IOException e) {
            Log.e("ReverseSongRecording", e.getMessage());
        }
    }

    @Override
    protected void next() {
        //TODO разрезать песню на куски
        Intent intent = new Intent(ReverseSongRecording.this, PlayResultSong.class);
        songFile = absolutePathSong;
        getSong();
        song = Controller.reverse(song);
        //TODO записать в файл байт
        //TODO если все куски закончились, то сохраняем все байты в wav файл и перейти, а если нет, то записать очередной кусок и перейти в playreverseSOng
        String absolutePathForReverseSong = SaveFile.saveMusic(song.length - 45, song, "music");
        intent.putExtra("SongFile", absolutePathForReverseSong);
        startActivity(intent);
        finish();

        /** TODO
         * countByteSong, bufferForSong
         * если есть кусок, то сохраняем для прослушивания следующий кусок, считанне байты передаем между intent, переходим в playreverse song
         * если куска нет, то добавляем записанный кусок, переворачиваем, сохраням, передаем в playreverse song
         */
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ReverseSongRecording.this);
        alert.setMessage("Вы уверены, что хотите выйти? выход = проигрыш");
        alert.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO сказать контроллеру, что автоматически проигрывает
                Intent intent = new Intent(ReverseSongRecording.this, Menu.class);
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
