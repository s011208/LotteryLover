package yhh.bj4.lotterylover.firebase;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class FirebaseDatabaseHelper {
    private static FirebaseDatabase sFirebaseDatabase;

    public synchronized static FirebaseDatabase getFirebaseDatabase() {
        if (sFirebaseDatabase == null) sFirebaseDatabase = FirebaseDatabase.getInstance();
        return sFirebaseDatabase;
    }

    public static final String CHILD_LOTTERY_DATA = "lottery_data";

    private FirebaseDatabaseHelper() {
    }
}
