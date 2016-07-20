package yhh.bj4.lotterylover.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class FirebaseDatabaseHelper {
    private static FirebaseDatabase sFirebaseDatabase;

    public synchronized static FirebaseDatabase getFirebaseDatabase() {
        if (sFirebaseDatabase == null) sFirebaseDatabase = FirebaseDatabase.getInstance();
        return sFirebaseDatabase;
    }

    public static final String CHILD_LOTTERY_DATA = "lottery_data_v20";

    private FirebaseDatabaseHelper() {
    }

    public static void setLtoValues(List<LotteryItem> items) {
        DatabaseReference db = FirebaseDatabaseHelper.getFirebaseDatabase().getReference();
        for (final LotteryItem item : items) {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(String.valueOf(item.getSequence()), item.toMap());
            db.child(FirebaseDatabaseHelper.CHILD_LOTTERY_DATA).child(item.getClass().getSimpleName()).updateChildren(childUpdates);
        }
    }
}
