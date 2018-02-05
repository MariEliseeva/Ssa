package alekhina_eliseeva.ssa;

import android.content.Intent;

public class PlayReverseSong extends PlaySong {
    @Override
    public void next() {
        //TODO возможно, после того, как мы воспроизвели песню, ее надо удалить, но тогда перестанет работать кнопка назад
        Intent intent = new Intent(PlayReverseSong.this, ReverseSongRecording.class);
        startActivity(intent);
    }
}
