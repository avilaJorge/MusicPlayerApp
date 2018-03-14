package com.example.maxvoskr.musicplayer;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    private SongFactory factory;
    private Song lastSong;
    private static String lastPath;
    private Context context;

    @TargetApi(25)
    Downloader(Context context, Resources res) {
        manager = context.getSystemService(DownloadManager.class);
        this.context = context;
        factory = new SongFactory(res);
    }

    public String download(String url) {
        //String name = url.substring(url.lastIndexOf("/") + 1);
        String name = "testAlbum.zip";

    /*    File musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File[] songFiles = musicDir.listFiles();

        for(File file: songFiles) {
            if(file.getName() == name)
                return "";
        }
      */
        File localDest = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), name);
        request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationUri(Uri.fromFile(localDest));

        manager.enqueue(request);

        Toast.makeText(context, "Download Started", Toast.LENGTH_SHORT).show();

        return localDest.getPath();
    }

    public String download(SongFile song) {

        String url = song.getUrl();
        String name = url.substring(url.lastIndexOf("/") + 1);
/*
        File musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File[] songFiles = musicDir.listFiles();

        for(File file: songFiles) {
            if(file.getName() == name)
                return "";
        }
*/

        File localDest = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), name);
        request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationUri(Uri.fromFile(localDest));
        //request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_MUSIC, "");

        manager.enqueue(request);

        Toast.makeText(context, "Download Started", Toast.LENGTH_SHORT).show();

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
    public Song getLastDownloadSong() {
        return lastSong;
    }




    @Override
    public void onReceive(Context context, Intent intent) {
        String title = "This DID NOT WORK!!!!!!~!!!!!!!!";
        //Get path of music downloads
        String path = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath() + '/';

        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE) ){
            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = manager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    path = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                }
                else{
                    Toast.makeText(context, "Download Unsuccessful", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            c.close();


            if(title.contains(".zip"))
            {
                Toast.makeText(context, "Downloaded a Zip file complete", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Title: " + title, Toast.LENGTH_LONG).show();

                //String name = path.substring(path.lastIndexOf("/") + 1, path.length());
                //String folderPath = path.substring(0, path.lastIndexOf("/"));

                Toast.makeText(context, "Path: " + path, Toast.LENGTH_LONG).show();
                Log.d("Zip Path", path);
                //Toast.makeText(context, "name: " + name, Toast.LENGTH_LONG).show();


                Boolean worked = unpackZip(path);

                if(worked)
                    Toast.makeText(context, "Zip file extracted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context, "Zip file extraction failed....", Toast.LENGTH_LONG).show();
            }
            else {

                Song newSong = factory.makeSongFromPath(path);
                SongFile oldSong = (SongFile) MusicArrayList.allMusicTable.get(newSong.getName() + newSong.getAlbum() + newSong.getArtist());

                if (oldSong != null) {
                    oldSong.setSong(path);
                    lastSong = oldSong;
                    MusicArrayList.insertLocalSong(oldSong);
                } else {
                    lastSong = newSong;
                    MusicArrayList.insertLocalSong(newSong);
                }

                Toast.makeText(context, "Downloading " + newSong.getName() + " complete", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean unpackZip(String zipPath)
    {
        InputStream fileStream;
        ZipInputStream zis;

        SongFactory songFactory = new SongFactory(context.getResources());

        try
        {
            String filename;
            String destPath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath();
            fileStream = new FileInputStream(zipPath);
            zis = new ZipInputStream(new BufferedInputStream(fileStream));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();
                if (ze.isDirectory()) {
                    File fmd = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), filename);
                    fmd.mkdirs();
                    continue;
                }


                String filePath = destPath + "/" + filename;
                FileOutputStream fout = new FileOutputStream(filePath);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();

                Song song = songFactory.makeSongFromPath(filePath);
                MusicArrayList.insertLocalSong(song);
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}