package alekhina_eliseeva.ssa.controller;

import java.io.*;
import java.util.Deque;
import java.util.LinkedList;

public class Songs {
    private static int size;

    public static void reverseAudio(String fileName) {
        openSong(fileName);
        writeSong(fileName);
    }

    private static byte[] header;
    private static byte[] data;

    private static void openSong(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("!!");
        }
        Deque<Byte> deque = new LinkedList<>();
        header = new byte[44];
        try {
            inputStream.read(header);
        } catch (IOException e) {
        }
        byte[] fragment = new byte[4];
        int readed = -1;
        do {
            try {
                readed = inputStream.read(fragment);
                if (readed != 0) {
                    deque.addFirst(fragment[3]);
                    deque.addFirst(fragment[2]);
                    deque.addFirst(fragment[1]);
                    deque.addFirst(fragment[0]);
                }
            } catch (IOException e) {
            }
        } while (readed != -1);
        size = deque.size();
        data = new byte[size];
        int i = 0;
        for (Byte b : deque) {
            data[i] = b;
            i++;
        }
    }

    private static void writeSong(String fileName) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(fileName));
        } catch (FileNotFoundException e) {
        }
        try {
            outputStream.write(header);
            outputStream.write(data);
        } catch (IOException e) {
        }
    }
}