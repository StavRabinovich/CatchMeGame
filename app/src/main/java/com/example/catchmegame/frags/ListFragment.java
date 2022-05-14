package com.example.catchmegame.frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.catchmegame.CallBack_MapFocus;
import com.example.catchmegame.R;
import com.example.catchmegame.winning.Winn;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ListFragment extends Fragment {

    private ListView list_LST_list;
    ArrayAdapter<String> arrayAdapter;
    private CallBack_MapFocus callBack_mapFocus;
    private ArrayList<Winn> win = new ArrayList<>();
    private ArrayList<String> wins = new ArrayList<>();

    public void setCallBack_mapFocus(CallBack_MapFocus callBack_mapFocus) {
        this.callBack_mapFocus = callBack_mapFocus;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, wins);;
        list_LST_list.setAdapter(arrayAdapter);
        list_LST_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list_LST_list.getItemAtPosition(position);
                if (callBack_mapFocus != null) {
                    callBack_mapFocus.focusMap(win.get(position).getLatitude(), win.get(position).getLongitude());
                }
            }
        });
        return view;
    }

    private void findViews(View view) {
        list_LST_list = view.findViewById(R.id.list_LST_list);
    }

    public void setList(ArrayList<Winn> list){
        win = list;
        for (Winn i : list) {
            this.wins.add(String.valueOf(i.getResult()));
        }
    }

}