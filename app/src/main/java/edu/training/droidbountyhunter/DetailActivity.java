package edu.training.droidbountyhunter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.training.droidbountyhunter.data.DatabaseBountyHunter;
import edu.training.droidbountyhunter.interfaces.OnTaskListener;
import edu.training.droidbountyhunter.models.Fugitive;
import edu.training.droidbountyhunter.network.NetServices;
import edu.training.droidbountyhunter.utilities.PictureTools;

import static edu.training.droidbountyhunter.utilities.PictureTools.MEDIA_TYPE_IMAGE;

public class DetailActivity extends AppCompatActivity {

    private Fugitive fugitive;
    private Button buttonCapture;

    private int mode;
    private Button buttonDelete;

    private static final int REQUEST_CODE_PHOTO_IMAGE = 1787;
    private Uri pathImage;
    private String photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se obtiene la información del Intent...
        Bundle bundleExtras = this.getIntent().getExtras();
        // Se pone el nombre del Fugitivo como título...
        fugitive = bundleExtras.getParcelable("fugitive");
        mode = bundleExtras.getInt("mode");
        this.setTitle(fugitive.getName() + " - Id: " + fugitive.getId());
        setContentView(R.layout.activity_detail);
        TextView label = findViewById(R.id.labelMessage);
        buttonCapture = findViewById(R.id.buttonCapture);
        buttonDelete = (Button)findViewById(R.id.buttonDelete);
        // Se identifica si es Fugitivo o Capturado para el mensaje...
        if(bundleExtras.getInt("mode") == 0){
            label.setText("The fugitive is still free...");
        }else{
            buttonCapture.setVisibility(View.GONE);
            label.setText("Caught!!!");
            ImageView photoImageView = (ImageView)findViewById(R.id.pictureFugitive);
            String pathPhoto = fugitive.getPhoto();
            if (pathPhoto != null && pathPhoto.length() > 0){
                Bitmap bitmap = PictureTools.decodeSampledBitmapFromUri(pathPhoto, 200, 200);
                photoImageView.setImageBitmap(bitmap);
                photo = pathPhoto;
            }

        }
    }

    public void OnCaptureClick(View view) {
        DatabaseBountyHunter database = new DatabaseBountyHunter(this);
        fugitive.setStatus("1");
        String pathPhoto = fugitive.getPhoto();
        if (pathPhoto == null || pathPhoto.length() == 0){
            Toast.makeText(this,
                    "Es necesario tomar la foto antes de capturar al fugitivo",
                    Toast.LENGTH_LONG).show();
            return;
        }
        database.UpdateFugitive(fugitive);
        NetServices netServices = new NetServices(new OnTaskListener() {
            @Override
            public void OnTaskCompleted(String response) {
                // despues de traer los datos del web service se actualiza la interfaz...
                String message = "";
                try {
                    JSONObject object = new JSONObject(response);
                    message = object.optString("mensaje","");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MessageClose(message);
            }

            @Override
            public void OnTaskError(int errorCode, String message) {
                Toast.makeText(getApplicationContext(),
                        "Ocurrio un problema en la comunicación con el WebService!!!",
                        Toast.LENGTH_LONG).show();
            }
        });
        netServices.execute("Capture", HomeActivity.UDID);
        buttonCapture.setVisibility(View.GONE);
        buttonDelete.setVisibility(View.GONE);
        setResult(0);
    }

    public void OnDeleteClick(View view) {
        DatabaseBountyHunter database = new DatabaseBountyHunter(this);
        database.DeleteFugitive(fugitive.getId());
        setResult(0);
        finish();
    }

    public void OnPhotoClick(View view) {
        if(PictureTools.permissionReadMemmory(this)) {
            dispatchPicture();
        }
    }

    private void dispatchPicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pathImage = PictureTools.with(this).getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pathImage);
        startActivityForResult(intent, REQUEST_CODE_PHOTO_IMAGE);
    }

    public void MessageClose(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle("Alert!!!");
        builder.setMessage(message);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setResult(mode);
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PHOTO_IMAGE){
            if (resultCode == RESULT_OK){
                fugitive.setPhoto(PictureTools.currentPhotoPath);
                ImageView imageFugitive = (ImageView) findViewById(R.id.pictureFugitive);
                Bitmap bitmap = PictureTools.decodeSampledBitmapFromUri(PictureTools.currentPhotoPath, 200, 200);
                imageFugitive.setImageBitmap(bitmap);
            }
        }
    }

}

