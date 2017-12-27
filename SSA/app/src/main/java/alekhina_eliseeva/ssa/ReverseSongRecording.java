package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReverseSongRecording extends SongRecording {

    @Override
    protected void next() {
        //Если есть непрослушанные куски, то сохраняем
        Intent intent = new Intent(ReverseSongRecording.this, PlayReverseSong.class);
        intent.putExtra("SongFile", /*savefile.getpath*/ "/storage/emulated/0/SSA/music.wav");
        startActivity(intent);
    }
}
