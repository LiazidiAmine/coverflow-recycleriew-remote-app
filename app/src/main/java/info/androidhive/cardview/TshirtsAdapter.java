package info.androidhive.cardview;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amine Liazidi on 31/10/16.
 */
public class TshirtsAdapter extends RecyclerView.Adapter<TshirtsAdapter.MyViewHolder> {

    /* POST REQUEST URL */
    private final static String URL = "http://192.168.1.85:3000/shirt";
    private Context mContext;
    private List<Tshirt> tshirtList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView thumbnail;
        public View v;

        public MyViewHolder(View view) {
            super(view);
            v = view;
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            /*overflow = (ImageView) view.findViewById(R.id.overflow);*/

        }

    }


    public TshirtsAdapter(Context mContext, List<Tshirt> tshirtList) {
        this.mContext = mContext;
        this.tshirtList = tshirtList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tshirt_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Tshirt tshirt = tshirtList.get(position);
        holder.title.setText(tshirt.getName());
        // loading tshirt covers using Glide library
        Glide.with(mContext).load(tshirt.getThumbnail()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                ImageView imgview = new ImageView(mContext);
                imgview.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                imgview.setAlpha(0.8f);
                imgview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                imgview.setImageDrawable(holder.thumbnail.getDrawable());
                dialog.setView(imgview).show();

                /*SENDING HTTP POST REQUEST*/
                postRequest(URL,tshirt.getName());

            }
        });

    }
    private void postRequest(String URL, String name){
        final String value = name;
    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext,"Veuillez choisir une nouvelle fois votre produit",Toast.LENGTH_LONG).show();
                }
            }){
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("name",value);
            return params;
        }

    };

    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
    requestQueue.add(stringRequest);
}


    @Override
    public int getItemCount() {
        return tshirtList.size();
    }

}
