package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import alekhina_eliseeva.ssa.controller.Controller;

public class ReverseSongRecording extends SongRecording {
    private String songFile;
    private byte[] song;

    private void getSong() {
        File file = new File(songFile);
        byte[] header = new byte[44];
        song = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(header, 0, header.length);
            fis.read(song, 44, song.length - 45);
        }
        catch (IOException e) {
            Log.d("PlaySong", e.getMessage());
        }
    }
    @Override
    protected void next() {
        //Если есть непрослушанные куски, то сохраняем
        Intent intent = new Intent(ReverseSongRecording.this, PlayResultSong.class);
        songFile = saveWavFile.getPath();
        Log.d("MyLog", songFile);
        getSong();
        Log.e("Mylog1", ((Integer) song.length).toString());
        song = Controller.reverse(song);
        Log.e("Mylog2", ((Integer) song.length).toString());
        SaveWavFile saveWavFile1 = new SaveWavFile();
        Log.e("Mylog3", ((Integer) song.length).toString());
        saveWavFile1.saveMusic(song.length - 45, song);
        intent.putExtra("SongFile", saveWavFile1.getPath());
        startActivity(intent);
    }
}
