package edu.training.droidbountyhunter;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.training.droidbountyhunter.data.DatabaseBountyHunter;
import edu.training.droidbountyhunter.models.Fugitive;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("New Fugitive");
        setContentView(R.layout.activity_add);
    }

    public void OnSaveClick(View view) {
        TextView name = (TextView)findViewById(R.id.nameFugitiveEditText);
        if (name.getText().toString().length() > 0){
            DatabaseBountyHunter database = new DatabaseBountyHunter(this);
            database.InsertFugitive(new Fugitive(0, name.getText().toString(),"0"));
            setResult(0);
            finish();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Please enter the name of the fugitive.")
                    .show();
        }
    }

}

