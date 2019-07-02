package edu.training.droidbountyhunter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.training.droidbountyhunter.DetailActivity;
import edu.training.droidbountyhunter.R;
import edu.training.droidbountyhunter.data.DatabaseBountyHunter;
import edu.training.droidbountyhunter.models.Fugitive;

public class ListFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Se hace referencia al Fragment generado por XML en los Layouts y
        // se instancia en una View...
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        Bundle arguments = this.getArguments();
        final int mode = arguments.getInt(ListFragment.ARG_SECTION_NUMBER);

        final ListView listView = view.findViewById(R.id.listTrappedFugitives);
        // Se actualiza los valores de la lista con la base de datos
        UpdateList(listView, mode);
        // Se genera el Listener para el detalle de cada elemento...
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ArrayList<Fugitive> fugitives = (ArrayList<Fugitive>) listView.getTag();
                Fugitive fugitive = fugitives.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("fugitive", fugitive);
                intent.putExtra("mode", mode);
                startActivityForResult(intent, mode);
            }
        });
        return view;
    }

    private void UpdateList(ListView list, int mode){
        DatabaseBountyHunter database = new DatabaseBountyHunter(getContext());
        ArrayList<Fugitive> fugitives = database.GetFugitives(mode == 1);
        if (fugitives.size() > 0){
            String[] data = new String[fugitives.size()];
            for (int i = 0 ; i < fugitives.size() ; i++){
                data[i] = fugitives.get(i).getName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, data);
            list.setAdapter(adapter);
            list.setTag(fugitives);
        }
    }
}