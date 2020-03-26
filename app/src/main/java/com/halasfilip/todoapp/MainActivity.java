package com.halasfilip.todoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //list storing our data
    final List<String> toDoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //elements from our layout
        final ListView listView = findViewById(R.id.lvList);
        final Button button = findViewById(R.id.btnAdd);


        final TextAdapter textAdapter = new TextAdapter();

        //read informations saved from file
        readInformations();
        //
        //setting data into textAdapter
        textAdapter.setData(toDoList);
        //
        listView.setAdapter(textAdapter);

        //method to delete the item from list


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toDoList.remove(position);
                                textAdapter.setData(toDoList);
                                saveInformations();
                            }
                        })
                        .setNegativeButton("Nope", null)
                        .create();
                dialog.show();
                return true;
            }
        });

        //method to strike thru already existing task

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               TextView textView;
                textView = view.findViewById(R.id.task);
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }
        });


        //method adding new todoitem to the list
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText newTaskInput = new EditText(MainActivity.this);
                newTaskInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("New Task")
                        .setView(newTaskInput)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toDoList.add(newTaskInput.getText().toString());
                                textAdapter.setData(toDoList);
                                saveInformations();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

    }

        //method to save informations from todolist
    private void saveInformations() {
        try {
            File file = new File(this.getFilesDir(), "filesToDo");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (int i = 0; i < toDoList.size(); i++) {
                bufferedWriter.write(toDoList.get(i));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            fileOutputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        //method to read informations from todolist
    private void readInformations() {
        File file = new File(this.getFilesDir(), "filesToDo");
        if (!file.exists()) {
            return;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = bufferedReader.readLine();
            while (line != null){
                toDoList.add(line);
                line = bufferedReader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //class responsible for putting elements in list
    class TextAdapter extends BaseAdapter {
        List<String> list = new ArrayList<>();
        void setData(List<String> newList) {
            list.clear();
            list.addAll(newList);
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater)
                        MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.todoitem, parent, false);
            }

            final TextView textView = convertView.findViewById(R.id.task);
            textView.setText(list.get(position));
            return convertView;
        }
    }
}
