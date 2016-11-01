package info.androidhive.cardview;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Amine Liazidi on 31/10/16.
 */
public class TshirtsAdapter extends RecyclerView.Adapter<TshirtsAdapter.MyViewHolder> {

    private static String URL = "http://10.0.2.2:9997/tshirt";
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
        // loading tshirt cover using Glide library
        Glide.with(mContext).load(tshirt.getThumbnail()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                ImageView imgview = new ImageView(mContext);
                imgview.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                imgview.setAlpha(0.8f);
                imgview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imgview.setAdjustViewBounds(true);

                imgview.setImageDrawable(holder.thumbnail.getDrawable());
                dialog.setView(imgview).show();

                /*SENDING HTTP POST REQUEST*/
                AsyncT asyncT = new AsyncT();
                asyncT.execute(URL,"name",tshirt.getName());




            }
        });

    }


    @Override
    public int getItemCount() {
        return tshirtList.size();
    }

}
