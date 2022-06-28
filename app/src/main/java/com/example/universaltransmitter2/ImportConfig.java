package com.example.universaltransmitter2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ImportConfig extends AppCompatActivity {

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_config);
        Intent FileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        FileIntent.setType("*/*");
        startActivityForResult(FileIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                uri = data.getData();
                Context context = ImportConfig.this;
                String filePath = FileUtils.getPath(context, uri);
                StringBuilder builder = new StringBuilder();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
                    String curLine;
                    while ((curLine = bufferedReader.readLine()) != null) {
                        builder.append(curLine);
                        builder.append("\n");
                    }
                    SaveFile(builder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void SaveFile(StringBuilder builder) {
        String filenameExternal = "setting.conf";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/UniTransmitter/");
        File fileT = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/UniTransmitter/SaveConfig/History");
        try {
            if (!file.exists()) file.mkdirs();
            File fileCon = new File(file, filenameExternal);
            fileCon.createNewFile();
            FileOutputStream fileSave = new FileOutputStream(fileCon);
            OutputStreamWriter outputStream = new OutputStreamWriter(fileSave);
            outputStream.write(builder.toString());
            outputStream.close();
            if (!fileT.exists()) fileT.mkdirs();
            File fileConT = new File(fileT, String.valueOf(System.currentTimeMillis()));
            fileConT.createNewFile();
            FileOutputStream fileSaveT = new FileOutputStream(fileConT);
            OutputStreamWriter outputStreamT = new OutputStreamWriter(fileSaveT);
            outputStreamT.write(builder.toString());
            outputStreamT.close();
            //Toast.makeText(ImportConfig.this, "Конфигурационный файл - записан", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ImportConfig.this, MainActivity.class);
            startActivity(intent);
        } catch (Throwable t) {
            Toast.makeText(ImportConfig.this, "Ошибка: " + t, Toast.LENGTH_LONG).show();
        }
    }
}

