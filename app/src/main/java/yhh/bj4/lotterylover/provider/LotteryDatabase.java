package yhh.bj4.lotterylover.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto539.Lto539;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltoJ6.LtoJ6;
import yhh.bj4.lotterylover.parser.ltoMM.LtoMM;
import yhh.bj4.lotterylover.parser.ltoToTo.LtoToTo;
import yhh.bj4.lotterylover.parser.ltoapow.LtoAuPow;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;
import yhh.bj4.lotterylover.parser.ltopow.LtoPow;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class LotteryDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ltd.db";
    private static final int VERSION = 7;

    private static final String TAG = "LotteryDatabase";
    private static final boolean DEBUG = Utilities.DEBUG;

    public LotteryDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // v1
        createTableLto(db);
        createTableLtoBig(db);
        createTableLtoHK(db);
        createTableLtoDof(db);
        createTableLto2C(db);
        createTableLto7C(db);
        createTableAppSettings(db);

        // v2
        createTableLto539(db);

        // v3
        createTableLtoPow(db);

        // v4
        createTableLtoMM(db);

        // v5
        createTableLtoJ6(db);

        // v6
        createTableLtoToTo(db);

        // v6
        createTableLtoAuPow(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            createTableLto539(db);
            ++oldVersion;
        }
        if (oldVersion == 2) {
            createTableLtoPow(db);
            ++oldVersion;
        }
        if (oldVersion == 3) {
            createTableLtoMM(db);
            ++oldVersion;
        }
        if (oldVersion == 4) {
            createTableLtoJ6(db);
            ++oldVersion;
        }
        if (oldVersion == 5) {
            createTableLtoToTo(db);
            ++oldVersion;
        }
        if (oldVersion == 6) {
            createTableLtoAuPow(db);
            ++oldVersion;
        }
    }

    private void createTableAppSettings(SQLiteDatabase db) {
        db.execSQL(AppSettings.COMMAND_CREATE_TABLE);
    }

    private void createTableLto(SQLiteDatabase db) {
        db.execSQL(Lto.COMMAND_CREATE_TABLE(Lto.TABLE_NAME));
    }

    private void createTableLtoBig(SQLiteDatabase db) {
        db.execSQL(LtoBig.COMMAND_CREATE_TABLE(LtoBig.TABLE_NAME));
    }

    private void createTableLtoHK(SQLiteDatabase db) {
        db.execSQL(LtoBig.COMMAND_CREATE_TABLE(LtoHK.TABLE_NAME));
    }

    private void createTableLtoDof(SQLiteDatabase db) {
        db.execSQL(LtoDof.COMMAND_CREATE_TABLE(LtoDof.TABLE_NAME));
    }

    private void createTableLto2C(SQLiteDatabase db) {
        db.execSQL(Lto2C.COMMAND_CREATE_TABLE(Lto2C.TABLE_NAME));
    }

    private void createTableLto7C(SQLiteDatabase db) {
        db.execSQL(Lto7C.COMMAND_CREATE_TABLE(Lto7C.TABLE_NAME));
    }

    private void createTableLto539(SQLiteDatabase db) {
        db.execSQL(Lto539.COMMAND_CREATE_TABLE(Lto539.TABLE_NAME));
    }

    private void createTableLtoPow(SQLiteDatabase db) {
        db.execSQL(LtoPow.COMMAND_CREATE_TABLE(LtoPow.TABLE_NAME));
    }

    private void createTableLtoMM(SQLiteDatabase db) {
        db.execSQL(LtoMM.COMMAND_CREATE_TABLE(LtoMM.TABLE_NAME));
    }

    private void createTableLtoJ6(SQLiteDatabase db) {
        db.execSQL(LtoJ6.COMMAND_CREATE_TABLE(LtoJ6.TABLE_NAME));
    }

    private void createTableLtoToTo(SQLiteDatabase db) {
        db.execSQL(LtoToTo.COMMAND_CREATE_TABLE(LtoToTo.TABLE_NAME));
    }

    private void createTableLtoAuPow(SQLiteDatabase db) {
        db.execSQL(LtoAuPow.COMMAND_CREATE_TABLE(LtoAuPow.TABLE_NAME));
    }
}
