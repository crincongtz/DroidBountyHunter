package edu.training.droidbountyhunter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.util.Log;
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

public class DetailActivity extends AppCompatActivity implements LocationListener {

    private Fugitive fugitive;
    private Button buttonCapture;

    private int mode;
    private Button buttonDelete;

    private static final int REQUEST_CODE_PHOTO_IMAGE = 1787;
    private Uri pathImage;
    private String photo;

    private static final int REQUEST_CODE_GPS = 1234;
    private LocationManager locationManager;

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
            turnOnGPS();
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

    public void OnMapClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("fugitive", fugitive);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PictureTools.REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d("RequestPermissions", "Camera - Granted");
                dispatchPicture();
            } else {
                Log.d("RequestPermissions", "Camera - Not Granted");
            }
        } else if (requestCode == REQUEST_CODE_GPS){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                turnOnGPS();
            } else {
                Log.d("RequestPermissions", "GPS - Not Granted");
            }
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

    @Override
    public void onLocationChanged(Location location) {
        fugitive.setLatitude(location.getLatitude());
        fugitive.setLongitude(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    private void turnOnGPS(){
        if (isGPSActivated()){
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            Toast.makeText(this,"Activando GPS...",Toast.LENGTH_LONG).show();

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);

            // BestProvider
            String provider = locationManager.getBestProvider(criteria, true);
            // Getting last location available
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null){
                fugitive.setLatitude(location.getLatitude());
                fugitive.setLongitude(location.getLongitude());
            }
        }
    }

    private void turnOffGPS(){
        Log.e("CALIS", "turnOffGPS()");
        if (locationManager != null){
            try {
                locationManager.removeUpdates(this);
                Toast.makeText(this,"Desactivando GPS...",Toast.LENGTH_LONG).show();
            }catch (SecurityException e){
                Toast.makeText(this,"Error desactivando GPS " + e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isGPSActivated() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)){
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_GPS);
                    return false;
                }else {
                    //No explanation needed, we can request the permissions.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_GPS);
                    return false;
                }
            }else {
                return true;
            }
        }else {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        turnOffGPS();
        super.onDestroy();
    }
}

