package com.example.rabu21mei;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class ManageDataActivity extends AppCompatActivity {
    private static final String TAG = "ManageDataActivity";
    ImageView imageViewManageDataActivity;
    ImageButton imageButtonManageDataActivity;
    EditText editTextNim, editTextNama, editTextUmur;
    Button buttonInputData, buttonUpdateData, buttonDeleteData, buttonView;

    Mahasiswa data = null;
    String mode, path;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

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
                // /storage/emulated/0/Pictures/
                String path = Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator;
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(uri, "image/*");
                activityResultLauncher.launch(intent);
            }
        });

        buttonInputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAlert("insert");
            }
        });

        buttonUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAlert("update");
            }
        });

        buttonDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAlert("delete");
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        refreshActivity();
        Intent intent = getIntent();
        mode = intent.getStringExtra("Mode");

        if (mode.equalsIgnoreCase("Insert")) {
            buttonInputData.setEnabled(true);
            buttonUpdateData.setEnabled(false);
            buttonDeleteData.setEnabled(false);
            editTextNama.setEnabled(true);
        } else {
            buttonInputData.setEnabled(false);
            buttonUpdateData.setEnabled(true);
            buttonDeleteData.setEnabled(true);

            try {
                data = intent.getParcelableExtra("data");
                editTextNim.setEnabled(false);
                Bitmap bitmap = BitmapFactory.decodeFile(data.getPath());
                imageViewManageDataActivity.setImageBitmap(bitmap);
                path = data.getPath();
                editTextNama.setText(data.getNama());
                editTextNim.setText(data.getNim());
                editTextUmur.setText(String.valueOf(data.getUmur()));
            } catch (Exception e) {
                Log.wtf(TAG, e.getMessage());
            }
        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            Uri data = o.getData().getData();
            imageViewManageDataActivity.setImageURI(data);
            path = getRealPath(getApplicationContext(), data);
        }
    });

    public String getRealPath(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] data_media_uri = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, data_media_uri, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshActivity() {
        imageViewManageDataActivity.setImageResource(R.drawable.ic_launcher_background);
        editTextNama.setText("");
        editTextNim.setText("");
        editTextUmur.setText("");
        path = "";
    }

    private void showDialogAlert(String Mode) {
        int buttonpic = -1;
        String title = "", message = "";

        switch (Mode) {
            case "insert":
                title = "Do You Sure Save Data";
                message = "Click Yes to Save Data";
                buttonpic = R.drawable.ic_launcher_background; // save icon
                break;
            case "update":
                title = "Do You Sure Update Data";
                message = "Click Yes to Update Data";
                buttonpic = R.drawable.ic_launcher_background; // update icon
                break;
            case "delete":
                title = "Do You Sure Delete Data";
                message = "Click Yes to Delete Data";
                buttonpic = R.drawable.ic_launcher_background; // delete icon
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(buttonpic)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (Mode) {
                            case "insert":
                                Mahasiswa mhs = databaseHelper.getExistData(getApplicationContext(), editTextNim.getText().toString());
                                if (mhs == null) {
                                    Mahasiswa myMhs = new Mahasiswa(editTextNim.getText().toString(), editTextNama.getText().toString(), path, Integer.parseInt(editTextUmur.getText().toString()));
                                    boolean isInserted = databaseHelper.insertData(getApplicationContext(), myMhs);
                                    
                                    if (isInserted) {
                                        Toast.makeText(ManageDataActivity.this, "Insert Successfuly", Toast.LENGTH_SHORT).show();
                                        refreshActivity();
                                    } else {
                                        Toast.makeText(ManageDataActivity.this, "Insert Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ManageDataActivity.this, "Nim Owned by " + mhs.getNama(), Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case "update":
                                Mahasiswa myMhs = new Mahasiswa(editTextNim.getText().toString(), editTextNama.getText().toString(), path, Integer.parseInt(editTextUmur.getText().toString()));
                                boolean isUpdated = databaseHelper.updateData(getApplicationContext(), myMhs);

                                if (isUpdated) {
                                    Toast.makeText(ManageDataActivity.this, "Update Successfuly", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ManageDataActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                }
                                break;
                                
                            case "delete":
                                int isDeleted = databaseHelper.deleteData(getApplicationContext(), editTextNim.getText().toString());
                                
                                if (isDeleted > 0) {
                                    Toast.makeText(ManageDataActivity.this, "Delete Successfuly", Toast.LENGTH_SHORT).show();
                                    refreshActivity();
                                } else {
                                    Toast.makeText(ManageDataActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ManageDataActivity.this, "Operasi dibatalkan", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

        builder.show();
    }
}