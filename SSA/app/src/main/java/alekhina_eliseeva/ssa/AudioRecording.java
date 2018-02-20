package alekhina_eliseeva.ssa;

import android.media.AudioRecord;

class AudioRecording {
    private AudioRecord recorder;
    private volatile int countByteSong = 0;
    private volatile byte[] bufferForSong;
    private final int bufferSizeForMusic = 6859776 * 8;
    private volatile boolean isRecording = false;

    void initAudioRecord(int audioSourse, int channelConfig, int audioFormat) {
        int sampleRatelnHz = 44100;
        int bufferSizeForRecord = AudioRecord.getMinBufferSize(sampleRatelnHz, channelConfig, audioFormat);
        recorder = new AudioRecord(audioSourse, sampleRatelnHz, channelConfig, audioFormat, bufferSizeForRecord * 10);
    }

    void readStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (recorder == null)
                    return;
                countByteSong = 0;
                bufferForSong = new byte[bufferSizeForMusic];
                int readCount;
                while (isRecording && (recorder != null)) {
                    readCount = recorder.read(bufferForSong, 0, bufferSizeForMusic);
                    countByteSong += readCount;
                }

            }
        }).start();
    }

    void stopRecording() {
        bufferForSong = null;
        if (recorder != null) {
            recorder.stop();
            isRecording = false;
        }
    }

    String save(boolean type) {
        return type ? (SaveFile.saveMusic(countByteSong, bufferForSong)) : (SaveFile.saveBytes(countByteSong, bufferForSong, "text"));
    }


    void setIsRecording(boolean value) {
        isRecording = value;
    }

    void startRecording() {
        recorder.startRecording();
    }
    void stop() {
        recorder.stop();
    }
}
