package com.example.universaltransmitter2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.universaltransmitter2.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;

import static com.example.universaltransmitter2.R.id.*;

public class MainActivity extends AppCompatActivity {

    String scanMAIN = "";

    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.universaltransmitter2.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_nfc, nav_camera, R.id.nav_file, R.id.nav_zipfile, R.id.nav_download, R.id.nav_geolocation).setOpenableLayout(drawer).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermissions(this)) {
            requestPermissions(MainActivity.this,1);
        } else {
            SaveConfiguration();
            ScannerConfiguration();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public boolean hasPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void SaveConfiguration() {
        String textToWrite="[General]\n" + "main=NFC\n" + "protocol=HTTP\n" +
                "[NFC]\n" + "url=\n" + "user=\n" + "pass=\n" + "method=\n" + "variable_tag=\n" +
                "[Camera]\n" + "url=\n" + "user=\n" + "pass=\n" + "variable_name=\n" + "variable_image=\n" +
                "[File]\n" + "url=\n" + "user=\n" + "pass=\n" + "variable_name=\n" + "variable_file=\n" +
                "[Download]\n" + "url=\n" + "user=\n" + "pass=\n"  +
                "[ZipFile]\n" + "url=\n" + "user=\n" + "pass=\n" + "variable_name=\n" + "variable_file=\n" +
                "[Geolocation]\n" + "url=\n" + "user=\n" + "pass=\n" + "variable_map=";
        String filenameExternal = "setting.conf";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/UniTransmitter/");
        try {
            if (!file.exists()) file.mkdirs();
            File fileCon = new File(file, filenameExternal);
            if (!fileCon.exists()) {
                fileCon.createNewFile();
                FileOutputStream fileSave = new FileOutputStream(fileCon);
                OutputStreamWriter outputStream = new OutputStreamWriter(fileSave);
                outputStream.write(textToWrite);
                outputStream.close();
                Toast.makeText(MainActivity.this, "Конфигурационный файл создан!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void ScannerConfiguration() {
        String fileNew = "/storage/emulated/0/Documents/UniTransmitter/setting.conf";
        Path path = Paths.get(fileNew);
        try {
            Scanner scanner = new Scanner(path);
            scanner.useDelimiter("\n");
            System.out.println(scanner.next());
            scanMAIN = scanner.next();
            StringTokenizer tokenMAIN = new StringTokenizer(scanMAIN, "=");
            tokenMAIN.nextToken();
            scanMAIN = tokenMAIN.nextToken();
            System.out.println(scanMAIN);
            if (Objects.equals(scanMAIN, "NFC")) {
                navController.navigate(nav_nfc);
            }
            if (Objects.equals(scanMAIN, "Camera")) {
                navController.navigate(nav_camera);
            }
            if (Objects.equals(scanMAIN, "File")) {
                navController.navigate(nav_file);
            }
            if (Objects.equals(scanMAIN, "Download")) {
                navController.navigate(nav_download);
            }
            if (Objects.equals(scanMAIN, "ZipFile")) {
                navController.navigate(nav_zipfile);
            }
            if (Objects.equals(scanMAIN, "Geolocation")) {
                navController.navigate(nav_geolocation);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void requestPermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", activity.getPackageName())));
                activity.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activity.startActivityForResult(intent, requestCode);
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, requestCode);
        }
    }
}