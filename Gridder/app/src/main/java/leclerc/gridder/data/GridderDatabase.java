package leclerc.gridder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Antoine on 2015-04-23.
 */
public class GridderDatabase extends SQLiteOpenHelper {
    private Context dbContext;

    private final static String DB_NAME = "gridder_dev.db";
    private static String DB_PATH;

    public SQLiteDatabase Database;


    public GridderDatabase(Context context) throws IOException {
        super(context, DB_NAME, null, 1);
        dbContext = context;

        DB_PATH = "/data/data/" + context.getApplicationContext().getPackageName() + "/databases/" + DB_NAME;


        boolean dbExists = checkDatabase();
        if(!dbExists) {
            createDatabase();
         }

        openDatabase();
    }

    public void createDatabase() throws IOException {
        boolean dbExists = checkDatabase();
        if(!dbExists) {
            getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database.");
            }
        }
    }

    private boolean checkDatabase() {
        boolean checkDb = false;
        try {
            String path = DB_PATH + DB_NAME;
            File dbFile = new File(path);
            checkDb = dbFile.exists();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return checkDb;
    }

    private void copyDatabase() throws IOException {
        InputStream input = dbContext.getAssets().open("databases/" + DB_NAME);

        String outputName = DB_PATH + DB_NAME;

        OutputStream output = new FileOutputStream(outputName);

        byte[] buffer = new byte[1024];
        int length = 0;
        while((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }

    public void openDatabase() throws IOException {
        String path = DB_PATH + DB_NAME;
        Database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        if(Database != null) {
            Database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
