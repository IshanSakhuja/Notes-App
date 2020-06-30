package com.example.mynotesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import static com.example.mynotesapp.MainActivity.arrayAdapter;
import static com.example.mynotesapp.MainActivity.descriptions;
import static com.example.mynotesapp.MainActivity.sharedPreferences;
import static com.example.mynotesapp.MainActivity.titles;

public class getNotes extends AppCompatActivity {
    EditText textViewtitle;
    EditText textViewDes;
    Intent intent;
    String title;
    String description;
    String editTitle;
    String editDes;
    int postition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_notes);
        textViewtitle = (EditText) findViewById(R.id.tvTitle);
        textViewDes = (EditText) findViewById(R.id.tvDes);
        textViewtitle.setPaintFlags(textViewtitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        intent = getIntent();
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        postition = intent.getIntExtra("pos",-1);
        if(title != null && description != null) {
            textViewtitle.setText(title);
            textViewDes.setText(description);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(title == null && description == null) {
            titles.add(textViewtitle.getText().toString());
            descriptions.add(textViewDes.getText().toString());
            arrayAdapter.notifyDataSetChanged();
            try {
                sharedPreferences.edit().putString("titles", ObjectSerializer.serialize(titles)).apply();
                sharedPreferences.edit().putString("des",ObjectSerializer.serialize(descriptions)).apply();
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finish();
        }
        else
        {
            editTitle = textViewtitle.getText().toString();
            editDes = textViewDes.getText().toString();
            if(title != editTitle || editDes != description && postition != -1)
            {
                titles.set(postition,editTitle);
                descriptions.set(postition,editDes);
                arrayAdapter.notifyDataSetChanged();
            }
            try {
                sharedPreferences.edit().putString("titles", ObjectSerializer.serialize(titles)).apply();
                sharedPreferences.edit().putString("des",ObjectSerializer.serialize(descriptions)).apply();
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finish();
        }
    }
}
