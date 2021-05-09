package com.example.infantcareaidingmobileapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infantcareaidingmobileapplication.Feeding;
import com.example.infantcareaidingmobileapplication.R;
import com.example.infantcareaidingmobileapplication.RecordActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {


    private Context context;
    private ArrayList<Feeding> feedings;
    FirebaseDatabase database;
    DatabaseReference reference;


    public DataAdapter(Context context, ArrayList<Feeding> feedings) {
        this.context = context;
        this.feedings = feedings;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Feedings").child(FirebaseAuth.getInstance().getUid());
    }

    @NonNull
    @Override
    public  DataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.table_row,parent,false);
        return new DataAdapter.DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataViewHolder holder, final int i) {

        holder.date.setText(feedings.get(i).getDate());
        holder.time.setText(feedings.get(i).getTime());
        DecimalFormat df = new DecimalFormat("#.#");
        holder.period.setText(String.valueOf(df.format(feedings.get(i).getPeriod()/60.0)));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(feedings.get(i).getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Record removed", Toast.LENGTH_SHORT).show();
                        feedings.remove(i);
                        notifyItemRemoved(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {

        return feedings.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder{

        ImageView delete;
        TextView date,time,period;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            period = itemView.findViewById(R.id.value);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
