package com.example.rabu21mei;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    GridView gridViewData;
    ImageButton buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridViewData = findViewById(R.id.gridViewData);
        buttonAdd = findViewById(R.id.imageButtonMainActivity);
        showPermission();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageDataActivity.class);
                startActivity(intent);
            }
        });
    }

    void showPermissionAgain(String title, String message, String kindPermission, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission(kindPermission, requestCode);
                    }
                });
        builder.create().show();
    }

    void showPermission() {
        String kindPermission = "";
        if (Build.VERSION.SDK_INT >= 33) {
            kindPermission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            kindPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        
        int permissionCheck = ContextCompat.checkSelfPermission(this, kindPermission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, kindPermission)) {
                showPermissionAgain("Allow Confirmation", "Please Allow this access", kindPermission, 200);
            } else {
                requestPermission(kindPermission, 200);
            }
        } else {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    void requestPermission(String kindPermission, int permissionCode) {
        ActivityCompat.requestPermissions(this, new String[]{kindPermission}, permissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Akses Image Disetujui", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Akses Image Tidak Disetujui", Toast.LENGTH_SHORT).show();
            }
        }
    }
}