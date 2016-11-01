package info.androidhive.cardview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by amine on 01/11/16.
 */

public class CoverAdapter extends RelativeLayout{
    View root_view;
    ImageView img_cover;
    TextView cover_title;
    TextView cover_title2;

    public CoverAdapter(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context){
        root_view = inflate(context, R.layout.cover_layout, this);
        cover_title = (TextView) root_view.findViewById(R.id.text_category);
        cover_title2 = (TextView) root_view.findViewById(R.id.text_category1);
        img_cover = (ImageView) root_view.findViewById(R.id.cover_category);
    }

    public void setImage(int drawable){
        img_cover.setImageResource(drawable);
    }

    public void setText(String text){
        cover_title.setText(text);
    }

}
