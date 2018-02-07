package alekhina_eliseeva.ssa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import alekhina_eliseeva.ssa.controller.Controller;

import static java.lang.Math.min;

public class ReverseSongRecording extends SongRecording {
    private String songFile;
    private byte[] song;

    /*private void getSong() {
        File file = new File(songFile);
        byte[] header = new byte[44];
        song = new byte[(int) file.length() - 44];
        try {
            FileInputStream fis = new FileInputStream(file);
            int countReadBytes = fis.read(header);
            if (countReadBytes < header.length) {
                Log.e("ReverseSongRecording", "header reading error");
            }
            countReadBytes = fis.read(song);
            if (countReadBytes < song.length) {
                Log.e("ReverseSongRecording", "song reading error");
            }
        } catch (IOException e) {
            Log.e("ReverseSongRecording", e.getMessage());
        }
    }*/

    @Override
    protected void save(){
        absolutePathSong = SaveFile.saveBytes(countByteSong, bufferForSong, "text");
    }

    private byte[] getSong(String fileName) {
        File file = new File(fileName);
        byte[] result = new byte[(int) file.length()];
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(result);
        }
        catch (IOException e) {
            Log.e("ReverseSongRecording", e.getMessage());
        }
        return result;
    }



    private void addSong(String fileName) {
        File file = new File(fileName);
        try(FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(song);
        }
        catch (IOException e) {
            Log.e("ReverseSongRecording", e.getMessage());
        }
    }

    private byte[] getSong(String fileName, int skip, int count) {
        File file = new File(fileName);
        byte[] result = new byte[count];
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.skip(skip);
            fis.read(result, 0, count);
        }
        catch (IOException e) {
            Log.e("ReverseSongRecording", e.getMessage());
        }
        return result;
    }


    @Override
    protected void next() {
        /*//TODO разрезать песню на куски
        Intent intent = new Intent(ReverseSongRecording.this, PlayResultSong.class);
        songFile = absolutePathSong;
        getSong();
        song = Controller.reverse(song);
        //TODO записать в файл байт
        //TODO если все куски закончились, то сохраняем все байты в wav файл и перейти, а если нет, то записать очередной кусок и перейти в playreverseSOng
        String absolutePathForReverseSong = SaveFile.saveMusic(song.length - 45, song, "music");
        intent.putExtra("SongFile", absolutePathForReverseSong);
        startActivity(intent);
        finish();*/

        /** TODO
         * countByteSong, bufferForSong
         * если есть кусок, то сохраняем для прослушивания следующий кусок, считанне байты передаем между intent, переходим в playreverse song
         * если куска нет, то добавляем записанный кусок, переворачиваем, сохраням, передаем в playreverse song
         */
        Intent prevIntent = getIntent();
        String songBytesFileName  = prevIntent.getStringExtra("SongBytes");
        int songSize = prevIntent.getIntExtra("SongSize", 0);
        int startByteNumber = prevIntent.getIntExtra("StartByteNumber", 0);
        String recordBytesFileName = prevIntent.getStringExtra("RecordBytes");
        song = getSong(absolutePathSong);
        addSong(recordBytesFileName);
        if (startByteNumber >= songSize) {
            byte[] resultSong = getSong(recordBytesFileName);
            resultSong = Controller.reverse(resultSong);
            String absolutePathForResultSong = SaveFile.saveMusic(resultSong.length, resultSong, "music");
            Intent intent = new Intent(ReverseSongRecording.this, PlayResultSong.class);
            intent.putExtra("SongFile", absolutePathForResultSong);
            startActivity(intent);
            finish();
        }
        else {
            int count = min(132300, songSize - startByteNumber);
            byte[] songBytes = getSong(songBytesFileName, startByteNumber, count);
            String absolutePathForPieceSong = SaveFile.saveMusic(songBytes.length, songBytes, "music");
            Intent intent = new Intent(ReverseSongRecording.this, PlayReverseSong.class);


            intent.putExtra("SongFile", absolutePathForPieceSong);
            intent.putExtra("SongBytes", songBytesFileName);
            intent.putExtra("SongSize", songSize);
            intent.putExtra("StartByteNumber", startByteNumber + count);
            intent.putExtra("RecordBytes", recordBytesFileName);
            startActivity(intent);
            finish();
        }


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
