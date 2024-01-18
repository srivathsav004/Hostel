package com.example.hostel;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends IntentService {

    public static final String DOWNLOAD_URL_EXTRA = "downloadUrl";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String downloadUrl = intent.getStringExtra(DOWNLOAD_URL_EXTRA);
            if (downloadUrl != null && !downloadUrl.isEmpty()) {
                downloadFile(downloadUrl);
            }
        }
    }

    private void downloadFile(String downloadUrl) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "food_menu.pdf");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadId = downloadManager.enqueue(request);

            // Save the download ID for later retrieval
            saveDownloadId(downloadId);
        } catch (Exception e) {
            showToast("Failed to start download");
        }
    }

    private void saveDownloadId(long downloadId) {
        // Save the download ID to SharedPreferences or any other storage mechanism
        // You can retrieve this ID later to check the download status
    }

    public static String getLatestDownloadedFilePath() {
        // Return the file path of the latest downloaded file
        // Retrieve the file path from SharedPreferences or any other storage mechanism
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "food_menu.pdf";
    }

    private void showToast(final String message) {
        // Show a toast message on the UI thread
        if (getApplicationContext() != null) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
