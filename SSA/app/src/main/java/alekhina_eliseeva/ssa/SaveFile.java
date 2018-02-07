package alekhina_eliseeva.ssa;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by olya on 27.12.17.
 */

public class SaveFile {
    private static byte[] header = new byte[]{82, 73, 70, 70, -124, 69, 3, 0, 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0,
            0, 1, 0, 1, 0, 68, -84, 0, 0, -128, 62, 0, 0, 2, 0, 16, 0, 100, 97, 116, 97};

    private static File createFileForSave(String fileType, String extension) {
        File fileForSave;
        File dirForSSA = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA");
        String filename = fileType;
        for (Integer i = 0; ; i++) {
            fileForSave = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA" + File.separator + filename + i.toString() + extension);
            if (!fileForSave.exists()) {
                break;
            }
        }
        try {
            dirForSSA.mkdirs();
            fileForSave.createNewFile();
        } catch (Exception e) {
            Log.e("SaveFile", e.getMessage());
        }
        return fileForSave;
    }


    public static String saveMusic(int countByteSong, byte[] bufferForSong, String fileType) {
        File fileForSave = createFileForSave(fileType, ".wav");
        try (FileOutputStream fos = new FileOutputStream(fileForSave)) {
            fos.write(header);
            fos.write(new byte[]{(byte) (countByteSong & 0xFF), (byte) ((countByteSong >> 8) & 0xFF),
                    (byte) ((countByteSong >> 16) & 0xFF), (byte) ((countByteSong >> 24) & 0xFF)});
            fos.write(bufferForSong, 44, countByteSong);

            fos.flush();
        } catch (Exception e) {
            Log.e("SaveFile", e.getMessage());
        }
        return fileForSave.getAbsolutePath();
    }

    public static String saveBytes (byte[] buffer, String fileType) {
        File fileForSave = createFileForSave(fileType, ".bin");
        try (FileOutputStream fos = new FileOutputStream(fileForSave)) {
            fos.write(buffer);
        }
        catch (IOException e) {
            Log.e("SaveFile", e.getMessage());
        }
        return fileForSave.getAbsolutePath();
    }

}
