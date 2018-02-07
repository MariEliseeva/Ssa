package alekhina_eliseeva.ssa;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by olya on 27.12.17.
 */

public class SaveWavFile {
    private static byte[] header = new byte[]{82, 73, 70, 70, -124, 69, 3, 0, 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0,
            0, 1, 0, 1, 0, 68, -84, 0, 0, -128, 62, 0, 0, 2, 0, 16, 0, 100, 97, 116, 97};

    private static File createFileForSave() {
        File fileForSave;
        File dirForSSA = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA");
        String filename = "music";
        for (Integer i = 0; ; i++) {
            fileForSave = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA" + File.separator + filename + i.toString() + ".wav");
            if (!fileForSave.exists()) {
                break;
            }
        }
        try {
            dirForSSA.mkdirs();
            fileForSave.createNewFile();
        } catch (Exception e) {
            Log.e("SaveWavFile", e.getMessage());
        }
        return fileForSave;
    }


    public static String saveMusic(int countByteSong, byte[] bufferForSong) {
        createFileForSave();
        File fileForSave = createFileForSave();
        try (FileOutputStream fos = new FileOutputStream(fileForSave)) {
            fos.write(header);
            fos.write(new byte[]{(byte) (countByteSong & 0xFF), (byte) ((countByteSong >> 8) & 0xFF),
                    (byte) ((countByteSong >> 16) & 0xFF), (byte) ((countByteSong >> 24) & 0xFF)});
            Log.d("SaveWavFile", ((Integer) countByteSong).toString() + " " + ((Integer) bufferForSong.length));
            try {
                fos.write(bufferForSong, 44, countByteSong);
            } catch (Exception e) {
                Log.e("SaveWavFile", e.getMessage());
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileForSave.getAbsolutePath();
    }

}
