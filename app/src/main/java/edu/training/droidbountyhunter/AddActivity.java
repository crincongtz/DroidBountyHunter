package edu.training.droidbountyhunter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("New Fugitive");
        setContentView(R.layout.activity_add);
    }

}

