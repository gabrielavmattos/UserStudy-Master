package com.example.jessica.masterproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.text.TextUtils;

public class FileSaver {

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    public FileSaver() {

    }

    public static boolean save(String filename, String[] data, boolean append, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        File folder = new File(String.valueOf(Environment.getExternalStorageDirectory())+"/annoyme");
        String state = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(state)) {
            System.err.println("Media not mounted");
            return false;
        }

        if(!folder.exists()) {
            if(!(folder.mkdir() || folder.isDirectory())) {
                System.err.println("Can't mkdir or doesn't exist");
                return false;
            }
        }

        File file = new File(folder.getPath()+"/"+filename);

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Couldn't create file "+file.getName());
                return false;
            }
        }

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), append);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if(append)
                bufferedWriter.write(","+TextUtils.join(",", data));
            else
                bufferedWriter.write(TextUtils.join(",", data));
            bufferedWriter.close();

            editor.putBoolean(activity.getString(R.string.upload_pending)+filename.substring(0, filename.length()-4), true);
            editor.commit();
            //FileUploader.upload(file, activity);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
