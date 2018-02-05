package alekhina_eliseeva.ssa;

import android.content.Intent;

public class PlayReverseSong extends PlaySong {
    @Override
    public void next() {
        Intent intent = new Intent(PlayReverseSong.this, ReverseSongRecording.class);
        startActivity(intent);
    }
}
