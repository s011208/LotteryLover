package yhh.bj4.lotterylover.firebase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by yenhsunhuang on 2016/6/30.
 */
public class StorageHelper {
    private static final String BUCKET_ID = "gs://project-4595708104798093054.appspot.com";

    public static StorageReference getRootStorageRef() {
        return FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_ID);
    }
}
