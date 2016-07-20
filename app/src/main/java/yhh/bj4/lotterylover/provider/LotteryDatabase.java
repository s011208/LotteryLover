package yhh.bj4.lotterylover.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.LtoList3.LtoList3;
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
import yhh.bj4.lotterylover.parser.ltoem.LtoEm;
import yhh.bj4.lotterylover.parser.ltolist4.LtoList4;
import yhh.bj4.lotterylover.parser.ltopow.LtoPow;
import yhh.bj4.lotterylover.services.UpdateLogger;
import yhh.bj4.lotterylover.settings.calendar.ShowDrawingTip;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class LotteryDatabase extends SQLiteOpenHelper {
    // adb exec-out run-as yhh.bj4.lotterylover cat databases/ltd.db > file.db
    private static final String DATABASE_NAME = "ltd.db";
    private static final int VERSION = 14;

    private static final String TAG = "LotteryDatabase";
    private static final boolean DEBUG = Utilities.DEBUG;

    public LotteryDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
//        Utilities.clearAllLtoTables(context); // clearAllLtoTables
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

        // v7
        createTableLtoAuPow(db);

        // v8
        createTableLtoEM(db);

        // v9
        createTableLtoList4(db);
        createTableLtoList3(db);

        // v10
        createTableShowDrawingTip(db);

        // v12
        createTableUpdateLogger(db);
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
        if (oldVersion == 7) {
            createTableLtoEM(db);
            ++oldVersion;
        }
        if (oldVersion == 8) {
            createTableLtoList4(db);
            createTableLtoList3(db);
            ++oldVersion;
        }
        if (oldVersion == 9) {
            createTableShowDrawingTip(db);
            ++oldVersion;
        }
        if (oldVersion == 10) {
            clearAllLtoTables(db);
            ++oldVersion;
        }
        if (oldVersion == 11) {
            createTableUpdateLogger(db);
            ++oldVersion;
        }
        if (oldVersion == 12) {
            db.execSQL("ALTER TABLE " + UpdateLogger.TABLE_NAME + " ADD " + UpdateLogger.COLUMN_TYPE + " INTEGER DEFAULT " + UpdateLogger.TYPE_NORMAL);
            ++oldVersion;
        }
        if (oldVersion == 13) {
            // update all tables' primary key
            dropAllLtoTables(db);
            createAllLtoTables(db);
            ++oldVersion;
        }
    }

    private void dropAllLtoTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE " + Lto.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoBig.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoHK.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoDof.TABLE_NAME);
        db.execSQL("DROP TABLE " + Lto2C.TABLE_NAME);
        db.execSQL("DROP TABLE " + Lto7C.TABLE_NAME);
        db.execSQL("DROP TABLE " + Lto539.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoPow.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoMM.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoJ6.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoToTo.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoAuPow.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoEm.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoList4.TABLE_NAME);
        db.execSQL("DROP TABLE " + LtoList3.TABLE_NAME);
    }

    private void clearAllLtoTables(SQLiteDatabase db) {
        db.delete(Lto.TABLE_NAME, null, null);
        db.delete(LtoBig.TABLE_NAME, null, null);
        db.delete(LtoHK.TABLE_NAME, null, null);
        db.delete(LtoDof.TABLE_NAME, null, null);
        db.delete(Lto2C.TABLE_NAME, null, null);
        db.delete(Lto7C.TABLE_NAME, null, null);
        db.delete(Lto539.TABLE_NAME, null, null);
        db.delete(LtoPow.TABLE_NAME, null, null);
        db.delete(LtoMM.TABLE_NAME, null, null);
        db.delete(LtoJ6.TABLE_NAME, null, null);
        db.delete(LtoToTo.TABLE_NAME, null, null);
        db.delete(LtoAuPow.TABLE_NAME, null, null);
        db.delete(LtoEm.TABLE_NAME, null, null);
        db.delete(LtoList4.TABLE_NAME, null, null);
        db.delete(LtoList3.TABLE_NAME, null, null);
    }

    private void createAllLtoTables(SQLiteDatabase db) {
        createTableLto(db);
        createTableLtoBig(db);
        createTableLtoHK(db);
        createTableLtoDof(db);
        createTableLto2C(db);
        createTableLto7C(db);
        createTableLto539(db);
        createTableLtoPow(db);
        createTableLtoMM(db);
        createTableLtoJ6(db);
        createTableLtoToTo(db);
        createTableLtoAuPow(db);
        createTableLtoEM(db);
        createTableLtoList4(db);
        createTableLtoList3(db);
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
        db.execSQL(LtoHK.COMMAND_CREATE_TABLE(LtoHK.TABLE_NAME));
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

    private void createTableLtoEM(SQLiteDatabase db) {
        db.execSQL(LtoEm.COMMAND_CREATE_TABLE(LtoEm.TABLE_NAME));
    }

    private void createTableLtoList4(SQLiteDatabase db) {
        db.execSQL(LtoList4.COMMAND_CREATE_TABLE(LtoList4.TABLE_NAME));
    }

    private void createTableLtoList3(SQLiteDatabase db) {
        db.execSQL(LtoList3.COMMAND_CREATE_TABLE(LtoList3.TABLE_NAME));
    }

    private void createTableShowDrawingTip(SQLiteDatabase db) {
        db.execSQL(ShowDrawingTip.COMMAND_CREATE_TABLE);
    }

    private void createTableUpdateLogger(SQLiteDatabase db) {
        db.execSQL(UpdateLogger.COMMAND_CREATE_TABLE);
    }
}
