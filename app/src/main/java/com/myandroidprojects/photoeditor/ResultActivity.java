package com.myandroidprojects.photoeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.myandroidprojects.photoeditor.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    ImageView homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    //Редактор жұмысын аяқтағаннан кейін қосылатын активити
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.image.setImageURI(getIntent().getData());
        homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() { //Қайта оралу батырмасын басқанын тыңдаушы
            @Override
            public void onClick(View v) {
                homeActivity();
            }
        });
    }
        private void homeActivity() {   //Басты мәзірге қайта оралу функциясы
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
}
