package edu.training.droidbountyhunter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se obtiene la información del Intent...
        Bundle bundleExtras = this.getIntent().getExtras();
        // Se pone el nombre del Fugitivo como título...
        this.setTitle(bundleExtras.getString("title"));
        setContentView(R.layout.activity_detail);
        TextView label = findViewById(R.id.labelMessage);
        // Se identifica si es Fugitivo o Capturado para el mensaje...
        if(bundleExtras.getInt("mode") == 0){
            label.setText("The fugitive is still free...");
        }else{
            label.setText("Caught!!!");
        }
    }
}

