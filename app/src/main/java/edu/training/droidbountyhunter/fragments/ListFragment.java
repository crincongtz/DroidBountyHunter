package edu.training.droidbountyhunter.fragments;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import edu.training.droidbountyhunter.DetailActivity;
import edu.training.droidbountyhunter.R;

public class ListFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Se hace referencia al Fragment generado por XML en los Layouts y
        // se instancia en una View...
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Bundle arguments = this.getArguments();
        final int mode = arguments.getInt(ListFragment.ARG_SECTION_NUMBER);

        ListView lista = (ListView) view.findViewById(R.id.listTrappedFugitives);
        String[] dummyData = new String[6];
        // Datos en HardCode...
        dummyData[0] = "Sergio Anguiano";
        dummyData[1] = "Arturo Ceballos";
        dummyData[2] = "Jonatan Juarez";
        dummyData[3] = "Fabian Olguin";
        dummyData[4] = "Karen Muñoz";
        dummyData[5] = "Roque Rueda";

        ArrayAdapter<String> aList = new ArrayAdapter<String>(
                getActivity(), R.layout.item_fugitive_list, dummyData);
        lista.setAdapter(aList);
        // Se genera el Listener para el detalle de cada elemento...
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("title", ((TextView) view).getText());
                intent.putExtra("mode", mode);
                startActivity(intent);
            }
        });
        return view;
    }
}