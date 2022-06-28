package com.example.universaltransmitter2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Geolocation extends AppCompatActivity {

    private LocationManager locationManager;
    Context context;

    String scanMAIN = "";
    String scanProtocol = "";
    String scanURL = "";
    String scanUSER = "";
    String scanPASS = "";
    String httpname = "";
    String httpGET = "";
    String variable_name = "";
    String variable_file = "";
    String encodedFile = "";
    String cord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geolocation_activity);
        String fileNew = "/storage/emulated/0/Documents/UniTransmitter/setting.conf";
        Path path = Paths.get(fileNew);
        Charset charset = StandardCharsets.UTF_8;
        try {
            Scanner scanner = new Scanner(path);
            scanner.useDelimiter("\n");
            System.out.println(scanner.next());
            scanMAIN = scanner.next();

            scanProtocol = scanner.next();
            StringTokenizer tokenProtocol = new StringTokenizer(scanProtocol, "=");
            tokenProtocol.nextToken();
            scanProtocol = tokenProtocol.nextToken();
            System.out.println(scanProtocol);

            for(int i=29; i>0; i--) scanner.next();

            scanURL = scanner.next();
            scanUSER = scanner.next();
            scanPASS = scanner.next();
            variable_name = scanner.next();

            StringTokenizer tokenURL = new StringTokenizer(scanURL, "=");
            if (tokenURL.countTokens()==2) {
                tokenURL.nextToken();
                scanURL = tokenURL.nextToken();
                System.out.println(scanURL);
            }
            StringTokenizer tokenUSER = new StringTokenizer(scanUSER, "=");
            if (tokenUSER.countTokens()==2) {
                tokenUSER.nextToken();
                scanUSER = tokenUSER.nextToken();
                System.out.println(scanUSER);
            }
            StringTokenizer tokenPASS = new StringTokenizer(scanPASS, "=");
            if (tokenPASS.countTokens()==2) {
                tokenPASS.nextToken();
                scanPASS = tokenPASS.nextToken();
                System.out.println(scanPASS);
            }
            StringTokenizer tokenName = new StringTokenizer(variable_name, "=");
            if (tokenName.countTokens()==2) {
                tokenName.nextToken();
                variable_name = tokenName.nextToken();
                System.out.println(variable_name);
            }
            Files.write(path, new String(Files.readAllBytes(path), charset).replace(scanMAIN, "main=Geolocation").getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }

        context = Geolocation.this;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        while (true) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                break;
            }
        }
    }

    public Activity getActivity(Context context)
    {
        if (context instanceof Activity) return (Activity) context;
        else return getActivity(((ContextWrapper) context).getBaseContext());
    }

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            locationManager.removeUpdates(locationListener);
            if (location == null) return;
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                cord = location.getLatitude() + "," + location.getLongitude();
            }
            else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                cord = location.getLatitude() + "," + location.getLongitude();
            }
            if (cord != null && scanURL != null) {
                System.out.println(cord);
                httpname = variable_name + "=" + cord;
                encodedFile = variable_file + "=" + encodedFile;
                if (Objects.equals(scanProtocol, "HTTPS")) {
                    Https anotherRun = new Https(scanURL, scanUSER, scanPASS, httpname, encodedFile, httpGET, getActivity(context));
                    Thread childTread = new Thread(anotherRun);
                    childTread.start();
                } else {
                    Http anotherRun = new Http(scanURL, scanUSER, scanPASS, httpname, encodedFile, httpGET, getActivity(context));
                    Thread childTread = new Thread(anotherRun);
                    childTread.start();
                }
                Intent intent = new Intent(Geolocation.this, MainActivity.class);
                startActivity(intent);
            }
        }
    };

}