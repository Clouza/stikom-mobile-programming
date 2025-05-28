package com.example.rabu21mei;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class ManageDataActivity extends AppCompatActivity {
    ImageView imageViewManageDataActivity;
    ImageButton imageButtonManageDataActivity;
    EditText editTextNim, editTextNama, editTextUmur;
    Button buttonInputData, buttonUpdateData, buttonDeleteData, buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewManageDataActivity = findViewById(R.id.imageViewManageDataActivity);
        imageButtonManageDataActivity = findViewById(R.id.imageButtonManageDataActivity);

        editTextNim = findViewById(R.id.editTextNim);
        editTextNama = findViewById(R.id.editTextNama);
        editTextUmur = findViewById(R.id.editTextUmur);

        buttonInputData = findViewById(R.id.buttonInputData);
        buttonUpdateData = findViewById(R.id.buttonUpdateData);
        buttonDeleteData = findViewById(R.id.buttonDeleteData);
        buttonView = findViewById(R.id.buttonView);

        imageButtonManageDataActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator;
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(uri, "image/*");
                activityResultLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            Uri data = o.getData().getData();
            if (data != null) {
                imageViewManageDataActivity.setImageURI(data);
            }
        }
    });
}