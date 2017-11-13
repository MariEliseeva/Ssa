package alekhina_eliseeva.ssa.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

/**
 * Created by mari on 06.11.17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static String name = "";
    private static String path = "";

    private SQLiteDatabase mDataBase;
    private final Context context;
    private boolean mNeedUpdate = false;

    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
        this.name = name;
        if (android.os.Build.VERSION.SDK_INT >= 17)
            this.path = context.getApplicationInfo().dataDir + "/databases/";
        else
           this.path = "/data/data/" + context.getPackageName() + "/databases/";
        this.context = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    private boolean checkDataBase() {
        File dbFile = new File(path + name);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = context.getAssets().open(name);
        OutputStream mOutput = new FileOutputStream(path + name);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }


    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private File downloadFile(String urlName) {
        URL url;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;

        byte[] buffer;
        int bufferLength;

        File file;
        OutputStream outputStream;

        try {
            url = new URL(urlName);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            file = new File(context.getFilesDir(), name);
            outputStream = new FileOutputStream(file);
            inputStream = httpURLConnection.getInputStream();

            httpURLConnection.getContentLength();

            buffer = new byte[1024];

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bufferLength);
            }
            outputStream.close();
            inputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}