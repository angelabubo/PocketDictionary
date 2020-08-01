package com.example.pocketdictionary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class FragmentDictionary extends Fragment {

    private ListView wordList;
    public ArrayAdapter adapter;

    public FragmentDictionary() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Enable the display of the search icon
        setHasOptionsMenu(true);

        List<String>  words = new ArrayList<String>();
        DBHelper db = new DBHelper(getContext());
        Cursor res = db.getAllWords();
        if (res.getCount() == 0)
        {
            Log.d("PocketDictionary", "Empty data");
        }
        else
        {
            while (res.moveToNext())
            {
                words.add(res.getString(DBHelper.COL_WORD));
            }
        }

        // Inflate the layout for this fragment
        View thisView =  inflater.inflate(R.layout.fragment_dictionary, container, false);
        wordList = thisView.findViewById(R.id.wordList);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, words);
        wordList.setAdapter(adapter);

        wordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();
                //Start the Word Definition Activity passing the current word selected
                Intent intent = new Intent(getActivity(), DefinitionActivity.class);
                intent.putExtra("word", item);
                startActivity(intent);
            }
        });
        return thisView;
    }

    @Override
    public void onResume() {
        //Set the title of the Fragment
        getActivity().setTitle("Dictionary");
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getActivity().getMenuInflater().inflate(R.menu.board_menu, menu);

            //Setup the search functionality
            MenuItem searchItem = menu.findItem(R.id.action_Search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Search...");
            searchView.setIconifiedByDefault(false);

            //Implement the search functionality by filtering the adapter data source
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
            });

    }
}
