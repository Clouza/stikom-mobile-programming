package com.example.rabu21mei;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gridViewData;
    ImageButton buttonAdd;
    ArrayList<Mahasiswa> data_mhs = new ArrayList<Mahasiswa>();
    Mahasiswa tempData;
    private final int REQUEST_PERMISSION_READIMAGE = 1001;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

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
                intent.putExtra("Mode", "Insert");
                startActivity(intent);
            }
        });

        if (databaseHelper.getCountData() > 0) {
            data_mhs = databaseHelper.transferToArrayList(getApplicationContext());
            if (data_mhs.size() > 0) {
                setAdapterGrid();
            }
        }
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

    class myListAdapter extends ArrayAdapter<Mahasiswa> {
        public myListAdapter() {
            super(MainActivity.this, R.layout.data_item, data_mhs);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.data_item, parent, false);
            }
            Mahasiswa myMhs = data_mhs.get(position);
            ImageView imv = (ImageView) convertView.findViewById(R.id.imageViewFotoGrid);
            TextView tvNim = (TextView) convertView.findViewById(R.id.textViewNimGrid);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;

            Bitmap bitmap = BitmapFactory.decodeFile(myMhs.getPath(), options);
            imv.setImageBitmap(bitmap);
            tvNim.setText(myMhs.getNim());

            return convertView;
//            return super.getView(position, convertView, parent);
        }
    }

    private void setAdapterGrid() {
        gridViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempData = data_mhs.get(position);
                Intent intent = new Intent(MainActivity.this, ManageDataActivity.class);
                intent.putExtra("data", tempData);
                intent.putExtra("Mode", "updatedelete");
                startActivity(intent);
            }
        });

        ArrayAdapter<Mahasiswa> adapter = new myListAdapter();
        gridViewData.setAdapter(adapter);
    }
}