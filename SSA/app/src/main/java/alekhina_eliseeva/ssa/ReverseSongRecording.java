package alekhina_eliseeva.ssa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import alekhina_eliseeva.ssa.controller.Controller;

import static java.lang.Math.min;

public class ReverseSongRecording extends SongRecording {
    private String recordBytesFileName;
    private String songBytesFileName;
    private int songSize;
    private int startByteNumber;
    private int count = 0;

    @Override
    protected void setType() {
        type = false;
    }

    private String getResultSong() {
        byte[] resultSong = ReadBytes.getBytes(recordBytesFileName);
        resultSong = Controller.reverse(resultSong);
        return(SaveFile.saveMusic(resultSong.length, resultSong));
    }

    private String getNextPiece() {
        count = min(132300, songSize - startByteNumber);
        byte[] songBytes = ReadBytes.getBytes(songBytesFileName, startByteNumber, count);
        return SaveFile.saveMusic(songBytes.length, songBytes);
    }

    @Override
    protected void next() {

//         если есть кусок, то сохраняем для прослушивания следующий кусок, считанные байты передаем между intent, переходим в playreverse song
//         если куска нет, то добавляем записанный кусок, переворачиваем, сохраням, передаем в playreverse song
        Intent prevIntent = getIntent();
        songBytesFileName = prevIntent.getStringExtra("SongBytes");
        songSize = prevIntent.getIntExtra("SongSize", 0);
        startByteNumber = prevIntent.getIntExtra("StartByteNumber", 0);
        recordBytesFileName = prevIntent.getStringExtra("RecordBytes");

        byte[] song = ReadBytes.getBytes(absolutePathSong);
        SaveFile.addSong(recordBytesFileName, song);
        if (startByteNumber >= songSize) {
            Intent intent = new Intent(ReverseSongRecording.this, PlayResultSong.class);
            intent.putExtra("SongFile", getResultSong());
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(ReverseSongRecording.this, PlayReverseSong.class);
            intent.putExtra("SongFile", getNextPiece());
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
                Controller.fixResult(false);
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
