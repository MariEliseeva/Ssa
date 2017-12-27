package alekhina_eliseeva.ssa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayReverseSong extends PlaySong {
    @Override
    public void next() {
        Intent intent = new Intent(PlayReverseSong.this, ReverseSongRecording.class);
        startActivity(intent);
    }
}
