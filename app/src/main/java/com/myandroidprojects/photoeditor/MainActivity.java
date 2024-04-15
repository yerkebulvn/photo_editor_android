package com.myandroidprojects.photoeditor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

import com.myandroidprojects.photoeditor.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int IMAGE_REQUEST_CODE = 45;    //Әртүрлі событиелерге арналған код айнымалылары: галереяға руқсат сұрау
    int CAMERA_REQUEST_CODE = 14;   ////Камераға рұқсат сұрау айнымалысы
    int RESULT_CODE = 200;  ////Сәтті аяқталу коды
    ImageView editBtn,imageView;    ////Басты менюдағы батырмалар айнымалысы, галерея және камера батырмалары

    @Override
    protected void onCreate(Bundle savedInstanceState) {    ///Активити құрылу кезінде қосылатын событие
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editBtn.setOnClickListener(new View.OnClickListener() { //Галерея батырмасы басылғанда іске қосылуын тыңдайтын событие
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_REQUEST_CODE);
            }
        });


        binding.cameraBtn.setOnClickListener(new View.OnClickListener() {   //Камера батырмасы басылғанда іске қосылуын тыңдайтын событие
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this,    //Қолданушының камераға рұқсат бергенін тексеру
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.CAMERA}, 32);
                } else{
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //Рұқсат болса камераны ашу
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //Активити жұмысын аяқтағанда қосылатын событие
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_CODE){  //Редакторды аяқтау коды келсе ResultActivity ді іске қосу
            if(data.getData() != null){
                Uri filePath = data.getData();
                Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
                dsPhotoEditorIntent.setData(filePath);
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Just photo editor");
                 startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);  //Суретті редакторға жіберу
            }
        }

        if(requestCode == RESULT_CODE){
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.image_saved_in_gallery), Toast.LENGTH_SHORT).show();
        }

        if(requestCode == CAMERA_REQUEST_CODE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = getImageUri(photo);
            Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
            dsPhotoEditorIntent.setData(uri);
            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Just photo editor");
            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);
        }
    }

    public Uri getImageUri(Bitmap bitmap) { //Суретті алу және сақтау функциясы
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, arrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"Title", null);
        return Uri.parse(path);
    }

}