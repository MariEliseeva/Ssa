package alekhina_eliseeva.ssa;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by olya on 27.12.17.
 */

public class SaveWavFile {
    private File fileForSave;

    private void createFileForSave() {
        File dirForSSA = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA");
        String filename = "music";
        for (Integer i = 0;; i++) {
            fileForSave = new File(Environment.getExternalStorageDirectory() + File.separator + "SSA" + File.separator + filename + i.toString() + ".wav");
            if (!fileForSave.exists()) {
                break;
            }
        }
        try {
            dirForSSA.mkdirs();
            fileForSave.createNewFile();
        } catch (Exception e) {
            Log.d("SongRecording", e.getMessage());
        }
    }

    public void saveMusic(int countByteSong, byte[] bufferForSong) {
        createFileForSave();
        byte[] header = new byte[44];
        header[0] = 82;
        header[1] = 73;
        header[2] = 70;
        header[3] = 70;
        header[4] = -124;
        header[5] = 69;
        header[6] = 3;
        header[7] = 0;
        header[8] = 87;
        header[9] = 65;
        header[10] = 86;
        header[11] = 69;
        header[12] = 102;
        header[13] = 109;
        header[14] = 116;
        header[15] = 32;
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;
        header[21] = 0;
        header[22] = 1;
        header[23] = 0;
        header[24] = 68;
        header[25] = -84;
        header[26] = 0;
        header[27] = 0;
        header[28] = -128;
        header[29] = 62;
        header[30] = 0;
        header[31] = 0;
        header[32] = 2;
        header[33] = 0;
        header[34] = 16;
        header[35] = 0;
        header[36] = 100;
        header[37] = 97;
        header[38] = 116;
        header[39] = 97;
        int value = countByteSong;
        header[40] = (byte)(value & 0xFF);
        value >>>= 8;
        header[41] = (byte)(value & 0xFF);
        value >>>= 8;
        header[42] = (byte)(value & 0xFF);
        value >>>= 8;
        header[43] = (byte)(value & 0xFF);
        value >>>= 8;
        try {
            FileOutputStream fos = new FileOutputStream(fileForSave);
            fos.write(header, 0, header.length);
            Log.d("MyLog", ((Integer)countByteSong).toString() + " " + ((Integer)bufferForSong.length));
            try {
                fos.write(bufferForSong, 44, countByteSong);
            }
            catch (Exception e) {
                Log.e("MyLog", e.getMessage());
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return fileForSave.getAbsolutePath();
    }

}
