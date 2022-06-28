package com.example.universaltransmitter2;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class ExportConfig extends AppCompatActivity {

    private final static String FILENAME = "/storage/emulated/0/Documents/UniTransmitter/setting.conf";
    String filenameExternal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = ExportConfig.this;
        StringBuilder builder = new StringBuilder();
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.activity_import_config, null);
        EditText userInput = promptsView.findViewById(R.id.input_text);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);
        mDialogBuilder.setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
            filenameExternal = userInput.getText() + ".conf";
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/UniTransmitter/SaveConfig");
            try {
                if (!file.exists()) file.mkdirs();
                if (userInput.getText() != null) {
                    File fileCon = new File(file, filenameExternal);
                    fileCon.createNewFile();
                    FileOutputStream fileSave = new FileOutputStream(fileCon);
                    OutputStreamWriter outputStream = new OutputStreamWriter(fileSave);
                    outputStream.write(builder.toString());
                    outputStream.close();
                    //Toast.makeText(ExportConfig.this, "Конфигурационный файл - создан", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ExportConfig.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ExportConfig.this, "Ошибка: поле не заполнено", Toast.LENGTH_LONG).show();
                }
            } catch (Throwable t) {
                Toast.makeText(ExportConfig.this, "Ошибка: " + t, Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(FILENAME), StandardCharsets.UTF_8));
            String curLine;
            while ((curLine = bufferedReader.readLine()) != null) {
                builder.append(curLine);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}