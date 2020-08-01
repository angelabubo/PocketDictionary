package com.example.pocketdictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavorites extends Fragment {

    ListView faveList;

    //Implement a custom adapter to handle display of
    //list row with text and delete icon
    CustomAdapter customAdapter;

    public FragmentFavorites() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Get list of favorite words from database
        List<DictionaryItem> favoriteWords = new ArrayList<DictionaryItem>();
        DBHelper db = new DBHelper(getContext());
        Cursor res = db.getAllFavorites();
        if (res.getCount() == 0)
        {
            Log.d("PocketDictionary", "Empty favorites table");
        }
        else
        {
            while (res.moveToNext())
            {
                favoriteWords.add(new DictionaryItem(res.getInt(DBHelper.COL_ID), res.getString(DBHelper.COL_WORD)));
            }
        }

        // Inflate the layout for this fragment
        View thisView =  inflater.inflate(R.layout.fragment_favorites, container, false);
        faveList = (ListView) thisView.findViewById(R.id.favoritesList);

        customAdapter = new CustomAdapter(getContext(), favoriteWords);
        faveList.setAdapter(customAdapter);

        faveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DictionaryItem itemSelected = (DictionaryItem)parent.getItemAtPosition(position);

                Intent intent = new Intent(getContext(), DefinitionActivity.class);
                intent.putExtra("word", itemSelected.getItemName());
                startActivity(intent);
            }
        });

        return thisView;
    }

    @Override
    public void onResume() {
        getActivity().setTitle("Favorites");
        super.onResume();
    }

    private class CustomAdapter  extends BaseAdapter {
        private Context context;
        private List<DictionaryItem> items;

        TextView faveWord;
        ImageView deleteIcon;

        public CustomAdapter(Context context, List<DictionaryItem> items)
        {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public Object getItem(int position) {
            //Return DictionaryItem item
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).getItemId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.fave_single_row, null);

            TextView faveWord = view.findViewById(R.id.txtFaveWord);
            ImageView deleteIcon = view.findViewById(R.id.imgDelete);

            faveWord.setText(((DictionaryItem)getItem(position)).getItemName());

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Show Dialog to confirm delete
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(((DictionaryItem)getItem(position)).getItemName());
                    builder.setMessage("Are you sure you want to remove this word from your" +
                            " favorites list?");

                    builder.setCancelable(true);

                    builder.setNegativeButton(
                            "CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder.setPositiveButton(
                            "DELETE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DBHelper db = new DBHelper(getContext());
                                    db.deleteFavoriteWord(((DictionaryItem)getItem(position)).getItemId());
                                    items.remove(position);
                                    notifyDataSetChanged();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });

            return view;
        }
    }

}
