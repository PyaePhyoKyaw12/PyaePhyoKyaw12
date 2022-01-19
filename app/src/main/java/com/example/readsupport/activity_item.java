package com.example.readsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class activity_item extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<setget> yourtext;


    private void openDatailActivity(String[] data) {

        Intent intent=new Intent(this,activity_detail.class);
        intent.putExtra("Name_KEY",data[0]);

        intent.putExtra("Image_KEY",data[1]);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);


        mRecyclerView =findViewById(R.id.mRecyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar =findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        yourtext =new ArrayList<>();
        mAdapter =new RecyclerAdapter(activity_item.this,yourtext);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(activity_item.this);

        mStorage =FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("all_categories");

        mDBListener =mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                yourtext.clear();

                for (DataSnapshot yourtextSnapshot : snapshot.getChildren() )
                {

                    setget upload=yourtextSnapshot.getValue(setget.class);
                    upload.setKey(yourtextSnapshot.getKey());
                    yourtext.add(upload);
                }

                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(activity_item.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onItemClick(int position) {

        setget clickText=yourtext.get(position);
        String[] Data={clickText.getName(),               clickText.getImageUrl()};

        openDatailActivity(Data);

    }



    @Override
    public void onShowItemClick(int position) {

        setget clickText=yourtext.get(position);
        String[] Data={clickText.getName(),               clickText.getImageUrl()};
        openDatailActivity(Data);



    }

    @Override
    public void onDeleteItemClick(int position) {

        setget selecteditem=yourtext.get(position);

       final  String selectedKey=selecteditem.getKey();

        StorageReference imgRef =mStorage.getReferenceFromUrl(selecteditem.getImageUrl());
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void aVoid) {

                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(activity_item.this,"Item deleted",Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(activity_item.this,"Error On Delete",Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}