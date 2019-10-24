package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;



public class CustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private FirebaseAuth firebaseAuth;

    public CustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    public void addItem(String item)
    {
        list.add(item);
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.food_list_item, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.food_item);
        listItemText.setText(list.get(position));

        TextView deleteButton = (TextView)view.findViewById(R.id.deleteButton);
        TextView detailsButton = (TextView) view.findViewById(R.id.detailsButton);
        Button editButton = (Button)view.findViewById(R.id.editButton);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
              //  final DatabaseHelper db = new DatabaseHelper(context);
                firebaseAuth = FirebaseAuth.getInstance();
                final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("food");

                database.orderByChild("username_food").equalTo(firebaseAuth.getCurrentUser().getEmail() + "_" + getItem(position).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            Log.d("PrintLog",data.getValue().toString());
                            data.getRef().removeValue();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
               // db.delete(db.getWritableDatabase(), getItem(position).toString());
                list.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Food Deleted", Toast.LENGTH_LONG).show();
            }
        });


        detailsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            //    notifyDataSetChanged();

                Intent intent = new Intent(context,FoodDetails.class);
                intent.putExtra("Food Name",getItem(position).toString());
                context.startActivity(intent);
            }
        });


        return view;
    }
}