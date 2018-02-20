package ru.mit.spbau.alekhina_eliseeva.ssa;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class ReadBytes {
    static byte[] getBytes(String fileName, int skip, int count) {
        File file = new File(fileName);
        byte[] result = new byte[count];
        try (FileInputStream fis = new FileInputStream(file)) {
            long countSkipBytes = fis.skip(skip);
            if (countSkipBytes != skip) {
                Log.e("ReadBytes", "can't skip in" + fileName);
            }
            long countReadBytes = fis.read(result);
            if (countReadBytes != count) {
                Log.e("ReadBytes", "can't read in " + fileName);
            }
        } catch (IOException e) {
            Log.e("ReadBytes", e.getMessage());
        }
        return result;
    }

    static byte[] getBytes(String fileName) {
        File file = new File(fileName);
        return getBytes(fileName, 0, (int) file.length());
    }

    static byte[] getSong(String songFile) {
        File file = new File(songFile);
        return getBytes(songFile, 44, (int) file.length() - 44);
    }
}
