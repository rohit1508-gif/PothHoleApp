package com.example.pothhole;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocateAdapter extends RecyclerView.Adapter<LocateAdapter.ViewHolder> {
    private List<locate> list;
    private Context ctx;
    private String id;
    LocateAdapter(List<locate> list, Context ctx){
        this.list = list;
        this.ctx =ctx;
    }

    @NonNull
    @Override
    public LocateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new LocateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocateAdapter.ViewHolder holder, final int position) {
        SharedPreferences preferences = ctx.getSharedPreferences("Pothhole", Context.MODE_PRIVATE);
        final String Token  = preferences.getString("TOKEN",null);
        locate l = list.get(position);
        Glide.with(ctx).load(l.getImage()).into(holder.imageView);
        holder.t.setText(l.getLocation());
        holder.t1.setText(l.getSender());
        id = l.getId();
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(ctx);
                String url = "https://pothholeapp.herokuapp.com/holes/"+ id;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        list.remove(position);
                        notifyItemRemoved(position);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Delete",error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headerMap = new HashMap<String, String>();
                        headerMap.put("Content-Type", "application/json");
                        headerMap.put("Authorization", "Bearer " + Token);
                        return headerMap;
                    }
                };
                requestQueue.add(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView t,t1;
        Button b;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.locationtext);
            t1 = itemView.findViewById(R.id.sender);
            b = itemView.findViewById(R.id.button);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
