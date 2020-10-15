package edu.training.droidbountyhunter.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import edu.training.droidbountyhunter.R;

public class AboutFragment extends Fragment {
    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se hace referencia al Fragment generado por XML en los Layouts y
        // se instanc’a en una View...
        View view = inflater.inflate(R.layout.fragment_about, container,
                false);
        // Se accede a los elementos ajustables del Fragment...
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingApp);

        String sRating = "0.0"; // Variable para lectura del Rating guardado
        // en el property.
        try {
            if (System.getProperty("rating") != null) {
                sRating = System.getProperty("rating");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (sRating.isEmpty()) {
            sRating = "0.0";
        }
        ratingBar.setRating(Float.parseFloat(sRating));
        // Listener al Raiting para la actualizacion de la property...
        ratingBar.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float rating,boolean fromUser) {
                        System.setProperty("rating", String.valueOf(rating));
                        ratingBar.setRating(rating);
                    }
                });
        return view;
    }
}

