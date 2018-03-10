package com.example.maxvoskr.musicplayer;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

import static android.app.DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR;
import static android.app.DownloadManager.COLUMN_STATUS;
import static android.app.DownloadManager.COLUMN_TOTAL_SIZE_BYTES;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;

/**
 * Created by tevor on 3/7/2018.
 */



// TODO: Does not auto add .mp3 to music files
/*
    Context context = getApplicationContext();
    Downloader downloader = new Downloader(context);
    path = downloader.download("https://freemusicarchive.org/music/download/24daacec73f9279086fbb714a8da8a84f2a16f1f");
 */


public class Downloader extends BroadcastReceiver {

    private DownloadManager manager;
    private DownloadManager.Request request;
    private long lastID;
    private String lastPath;
    private Context context;

    @TargetApi(25)
    Downloader(Context context) {
        manager = context.getSystemService(DownloadManager.class);
        this.context = context;
    }

    public String download(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1);
        File localDest = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), name);
        request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationUri(Uri.fromFile(localDest));

        lastID = manager.enqueue(request);

        return localDest.getPath();
    }

    public boolean downloadSuccessful() {
        return Integer.parseInt(COLUMN_STATUS) == STATUS_SUCCESSFUL;
    }

    public boolean downloadRunning() {
        if(COLUMN_STATUS == "status")
            return false;
        else
            return Integer.parseInt(COLUMN_STATUS) == STATUS_RUNNING;
    }

    public float downloadPercent() {
        return Integer.parseInt(COLUMN_BYTES_DOWNLOADED_SO_FAR)  / Integer.parseInt(COLUMN_TOTAL_SIZE_BYTES);
    }

    public String getLastDownloadPath() {
        return lastPath;
    }





    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Download complete", Toast.LENGTH_LONG).show();
    }

}
