package edu.training.droidbountyhunter;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.training.droidbountyhunter.data.DatabaseBountyHunter;
import edu.training.droidbountyhunter.models.Fugitive;

public class DetailActivity extends AppCompatActivity {

    private Fugitive fugitive;
    private Button buttonCapture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se obtiene la información del Intent...
        Bundle bundleExtras = this.getIntent().getExtras();
        // Se pone el nombre del Fugitivo como título...
        fugitive = bundleExtras.getParcelable("fugitive");
        this.setTitle(fugitive.getName() + " - Id: " + fugitive.getId());
        setContentView(R.layout.activity_detail);
        TextView label = findViewById(R.id.labelMessage);
        buttonCapture = findViewById(R.id.buttonCapture);
        // Se identifica si es Fugitivo o Capturado para el mensaje...
        if(bundleExtras.getInt("mode") == 0){
            label.setText("The fugitive is still free...");
        }else{
            buttonCapture.setVisibility(View.GONE);
            label.setText("Caught!!!");
        }
    }

    public void OnCaptureClick(View view) {
        DatabaseBountyHunter database = new DatabaseBountyHunter(this);
        fugitive.setStatus("1");
        database.UpdateFugitive(fugitive);
        setResult(0);
        finish();
    }

    public void OnDeleteClick(View view) {
        DatabaseBountyHunter database = new DatabaseBountyHunter(this);
        database.DeleteFugitive(fugitive.getId());
        setResult(0);
        finish();
    }

}

