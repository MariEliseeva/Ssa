package alekhina_eliseeva.ssa;

import android.content.Intent;

/**
 * Created by olya on 27.12.17.
 */

public class PlayResultSong extends PlaySong {
    @Override
    public void next() {
        Intent intent = new Intent(PlayResultSong.this, SelectTitle.class);
        startActivity(intent);
    }
}
