package com.example.pocketdictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.fragment.app.Fragment;


public class FragmentFeedback extends Fragment {

    public FragmentFeedback() {
        // Required empty public constructor
    }

    private EditText email, comment;
    private RatingBar ratingBar;
    private Button submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View thisView = inflater.inflate(R.layout.fragment_feedback, container, false);

        //Map the widgets
        email = thisView.findViewById(R.id.etEmail);
        comment = thisView.findViewById(R.id.etComment);
        ratingBar = thisView.findViewById(R.id.ratingBar);
        submit = thisView.findViewById(R.id.btnSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Store Feedback Information to Database
                DBHelper db = new DBHelper(getContext());

                long id = db.saveFeedback(ratingBar.getRating(),
                        email.getText().toString(),
                        comment.getText().toString());

                //Display information to user
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Thank you for your feedback!");

                //TestCode START
                //Cursor res = db.getAllFeedback();
                Cursor res = db.getAFeedback(id);
                StringBuilder sb = new StringBuilder();
                if (res.getCount() > 0)
                {
                    Log.d("PocketDictionary", "getFeedback: " );
                    while (res.moveToNext())
                    {
                        sb.append("Thank you for your feedback!\n");
                        sb.append(" \nID      - " + res.getString(0));
                        sb.append(" \nDATE    - " + res.getString(1));
                        sb.append(" \nRATING  - " + res.getString(2));
                        sb.append(" \nEMAIL   - " + res.getString(3));
                        sb.append(" \nCOMMENT - " + res.getString(4));
                    }
                }
                builder.setMessage(sb.toString());
                //TestCode END

                builder.setCancelable(true);

                builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                //Return to last fragment
                                getActivity().onBackPressed();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return thisView;
    }

    @Override
    public void onResume() {
        getActivity().setTitle("Feedback");
        super.onResume();
    }
}
