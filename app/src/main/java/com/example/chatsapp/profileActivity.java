package com.example.chatsapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {

    EditText name;
    Button save;
    CircleImageView profilePic;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name=findViewById(R.id.name);
        save= findViewById(R.id.save);
        profilePic=findViewById(R.id.profilePicture);

        database= FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
    }
    ActivityResultLauncher<String> mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result!=null)
                    {
                        imageUri=result;
                        profilePic.setImageURI(result);
                        uploadImage();
                    }
                }
            }
    );

    private void uploadImage(){
        if(imageUri != null)
        {
            StorageReference reference=storage.getReference().child("image/"+ UUID.randomUUID().toString());
            reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Image Uploded Successfully",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Image is not Uploded Successfully",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


}