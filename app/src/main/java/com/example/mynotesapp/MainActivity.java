package com.example.mynotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static SharedPreferences sharedPreferences;
    ListView listView;
    static ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> titles;
    static ArrayList<String> descriptions;
    Intent intent;
    static boolean active = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        intent = new Intent(this,getNotes.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titles = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        sharedPreferences = this.getSharedPreferences("com.example.mynotesapp", Context.MODE_PRIVATE);
        ArrayList<String> temp = new ArrayList<>();
        try {
            titles = (ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("titles", String.valueOf(temp)));
            descriptions = (ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("des", String.valueOf(temp)));
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        listView = (ListView)findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titles);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeWithData(position);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                showAlert(pos);
                return true;
            }
        });
    }
    public void showAlert(final int pos)
    {
        if(active == true) {
            new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_delete).setTitle("Delete")
                    .setMessage("Are You Sure You want to delete this note?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    titles.remove(pos);
                    descriptions.remove(pos);
                    arrayAdapter.notifyDataSetChanged();
                    try {
                        sharedPreferences.edit().putString("titles", ObjectSerializer.serialize(titles)).apply();
                        sharedPreferences.edit().putString("des",ObjectSerializer.serialize(descriptions)).apply();
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }).setNegativeButton("No", null).create().show();
        }
    }
    public void changeWithData(int position)
    {
        intent = new Intent(this,getNotes.class);
        intent.putExtra("title",titles.get(position));
        intent.putExtra("description",descriptions.get(position));
        intent.putExtra("pos",position);
        startActivity(intent);
    }
}