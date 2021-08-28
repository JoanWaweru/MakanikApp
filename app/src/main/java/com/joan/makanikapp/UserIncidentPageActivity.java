package com.joan.makanikapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class UserIncidentPageActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private Button image_uploader, submit_incident;
    private RecyclerView incident_images;
    private List<String> filenamelist;
    private List<String> filedonelist;
    private UploadImageListAdapter uploadImageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_incident_page);
        image_uploader = findViewById(R.id.image_breakdown_images);
        submit_incident = findViewById(R.id.button_Find_Mechanic);
        incident_images = findViewById(R.id.images_breakdown_report);
        filenamelist = new ArrayList<>();
        filedonelist = new ArrayList<>();
        uploadImageListAdapter = new UploadImageListAdapter(filenamelist,filedonelist);
        incident_images.setLayoutManager(new LinearLayoutManager(this));
        incident_images.setHasFixedSize(true);
        incident_images.setAdapter(uploadImageListAdapter);
//        if(SaveSharedPreference.getUserName(UserIncidentPageActivity.this).length() == 0)
//        {
//            // call Login Activity
//            startActivity(new Intent(UserIncidentPageActivity.this,LoginActivity.class));
//        }
//        else
//        {
//            // Stay at the current activity.
//            startActivity(new Intent(UserIncidentPageActivity.this,UserIncidentPageActivity.class));
//        }


        image_uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),RESULT_LOAD_IMAGE);

            }
        });

        submit_incident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submitIncidentIntent = new Intent(UserIncidentPageActivity.this, UserMapsActivity.class);
                startActivity(submitIncidentIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && requestCode == RESULT_OK){
            if(data.getClipData() != null){
                //Toast.makeText(UserIncidentPageActivity.this,"Please Select Images",Toast.LENGTH_LONG).show();
                int totalItemsSelected = data.getClipData().getItemCount();
                for(int i = 0; i< totalItemsSelected; i++){
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String filename = getFileName(fileUri);
                    filenamelist.add(filename);
                    uploadImageListAdapter.notifyDataSetChanged();

                }
            }else if (data.getData() != null){

                Toast.makeText(UserIncidentPageActivity.this, "Selected Single File", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}