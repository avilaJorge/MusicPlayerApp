package com.example.maxvoskr.musicplayer;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import java.io.File;

import static android.app.DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR;
import static android.app.DownloadManager.COLUMN_STATUS;
import static android.app.DownloadManager.COLUMN_TOTAL_SIZE_BYTES;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;

/**
 * Created by tevor on 3/7/2018.
 */



// TODO: add to loading or somewhere: context.getDir("MusicFolder", 0);


public class Downloader {

    private DownloadManager manager;
    private DownloadManager.Request request;
    private long lastID;
    private File localDest;

    @TargetApi(25)
    Downloader(Context context, File localDest) {
        manager = context.getSystemService(DownloadManager.class);
        this.localDest = localDest;
    }

    public long download(String url) {
        request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationUri(Uri.fromFile(localDest));

        lastID = manager.enqueue(request);
        return lastID;
    }

    public boolean downloadSuccessful() {
        return Integer.parseInt(COLUMN_STATUS) == STATUS_SUCCESSFUL;
    }

    public boolean downloadRunning() {
        return Integer.parseInt(COLUMN_STATUS) == STATUS_RUNNING;
    }

    public float downloadPercent() {
        return Integer.parseInt(COLUMN_BYTES_DOWNLOADED_SO_FAR)  / Integer.parseInt(COLUMN_TOTAL_SIZE_BYTES);
    }
}
