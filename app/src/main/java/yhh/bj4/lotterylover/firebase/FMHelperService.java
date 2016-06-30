package yhh.bj4.lotterylover.firebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Iterator;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/6/30.
 */
public class FMHelperService extends FirebaseMessagingService {
    private static final String KEY_UPDATE_TASK = "update_task";
    private static final String KEY_UPDATE_VERSION = "update_version";
    private static final String KEY_UPDATE_MESSAGE = "update_message";

    public static final String TOPIC_ALL = "All";

    private static final String TAG = "FMHelperService";
    private static final boolean DEBUG = Utilities.DEBUG;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (DEBUG) {
            Log.e(TAG, "getCollapseKey: " + remoteMessage.getCollapseKey());
            Log.e(TAG, "getFrom: " + remoteMessage.getFrom());
            Log.e(TAG, "getMessageId: " + remoteMessage.getMessageId());
            Log.e(TAG, "getMessageType: " + remoteMessage.getMessageType());
            Log.e(TAG, "getTo: " + remoteMessage.getTo());
            Log.e(TAG, "getNotification.getBody: " + remoteMessage.getNotification().getBody());
            Log.e(TAG, "getSentTime: " + remoteMessage.getSentTime());
            Log.e(TAG, "getTtl: " + remoteMessage.getTtl());

            if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {
                Iterator<String> keyIterator = remoteMessage.getData().keySet().iterator();
                while (keyIterator.hasNext()) {
                    final String key = keyIterator.next();
                    Log.e(TAG, "key: " + key + ", value: " + remoteMessage.getData().get(key));
                }
            }
        }
        if (remoteMessage.getData().isEmpty()) return;

        Looper.prepare();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                boolean hasShownDialog = false;
                if (remoteMessage.getData().containsKey(KEY_UPDATE_TASK)) {
                    if (remoteMessage.getData().containsKey(KEY_UPDATE_VERSION)) {
                        showUpdateTaskDialog(remoteMessage.getData().get(KEY_UPDATE_VERSION),
                                remoteMessage.getData().get(KEY_UPDATE_MESSAGE));
                        hasShownDialog = true;
                    }
                }
                if (!hasShownDialog) {
                    showDefaultDialog(remoteMessage);
                }
            }
        });
        Looper.loop();
    }

    private void showDefaultDialog(final RemoteMessage remoteMessage) {
        AlertDialog dialog = new AlertDialog.Builder(FMHelperService.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle("NEW MESSAGE")
                .setMessage(remoteMessage.getNotification().getBody())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.show();
    }

    private void showUpdateTaskDialog(String updateVersion, String updateMessage) {
        AlertDialog dialog = new AlertDialog.Builder(FMHelperService.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(getResources().getString(R.string.update_task_dialog_title, updateVersion))
                .setMessage(updateMessage)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.show();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        if (DEBUG)
            Log.e(TAG, "onDeletedMessages");
    }
}
