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
            int countReadBytes = fis.read(header);
            if (countReadBytes < header.length) {
                Log.e("ReverseSongRecording", "header reading error");
            }
            countReadBytes = fis.read(song);
            if (countReadBytes < song.length - 45) {
                Log.e("ReverseSongRecording", "song reading error");
            }
        } catch (IOException e) {
            Log.e("PlaySong", e.getMessage());
        }
    }

    @Override
    protected void next() {
        //TODO разрезать песню на куски
        Intent intent = new Intent(ReverseSongRecording.this, PlayResultSong.class);
        songFile = absolutePathSong;
        getSong();
        song = Controller.reverse(song);
        String absolutePathForReverseSong = SaveWavFile.saveMusic(song.length - 45, song);
        intent.putExtra("SongFile", absolutePathForReverseSong);
        startActivity(intent);
    }
}
