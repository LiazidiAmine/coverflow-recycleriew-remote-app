package info.androidhive.cardview;

/**
 * Created by Amine Liazidi on 31/10/16.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.progress;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TshirtsAdapter adapter;
    List<Tshirt> tshirtList;
    int[] tshirts = new int[]{
            R.drawable.femme1,
            R.drawable.femme2,
            R.drawable.femme3,
            R.drawable.femme4,
            R.drawable.femme5,
            R.drawable.homme1,
            R.drawable.homme2,
            R.drawable.homme3,
            R.drawable.homme4,
            R.drawable.homme5,
            R.drawable.a,//Image Ben
            R.drawable.b,//Image Ben
            R.drawable.c//Image Ben
    };
    int[] covers = new int[]{
            R.drawable.cover1,
            R.drawable.cover2,
            R.drawable.cover3,
            R.drawable.cover4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        tshirtList = new ArrayList<>();
        adapter = new TshirtsAdapter(this, tshirtList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        PagerContainer mContainer = (PagerContainer) findViewById(R.id.pager_container);

        prepareTshirts();

        final ViewPager pager = mContainer.getViewPager();

        PagerAdapter adapter = new MyPagerAdapter();
        pager.setAdapter(adapter);

        pager.setOffscreenPageLimit(adapter.getCount());

        pager.setClipChildren(false);

        boolean showRotate = getIntent().getBooleanExtra("showRotate",true);

        if(showRotate){
            new CoverFlow.Builder()
                    .with(pager)
                    .scale(0.5f)
                    .pagerMargin(0f)
                    .spaceSize(0f)
                    .rotationY(25f)
                    .build();
        }

    }

    /**
     * Adding few tshirts for testing
     */
    private void prepareTshirts() {
        Tshirt a = new Tshirt("mariejeanne", tshirts[7]);
        tshirtList.add(a);
        a = new Tshirt("Trés Fatiguée", tshirts[1]);
        tshirtList.add(a);
        a = new Tshirt("Ce n'est rien voilà tout", tshirts[2]);
        tshirtList.add(a);
        a = new Tshirt("Just Do Nothing", tshirts[6]);
        tshirtList.add(a);
        a = new Tshirt("Artiste incompris", tshirts[5]);
        tshirtList.add(a);
        a = new Tshirt("BC BG", tshirts[3]);
        tshirtList.add(a);
        a = new Tshirt("Just Do Nothing", tshirts[0]);
        tshirtList.add(a);
        a = new Tshirt("Ananas", tshirts[4]);
        tshirtList.add(a);
        a = new Tshirt("monsieur muscle", tshirts[8]);
        tshirtList.add(a);
        a = new Tshirt("je m'en FISH", tshirts[9]);
        tshirtList.add(a);
        a = new Tshirt("a", tshirts[10]);
        tshirtList.add(a);
        a = new Tshirt("b", tshirts[11]);
        tshirtList.add(a);
        a = new Tshirt("c", tshirts[12]);
        tshirtList.add(a);
        adapter.notifyDataSetChanged();
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }

            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final int p = position;
            CoverAdapter cover = new CoverAdapter(getBaseContext());
            cover.cover_title.setText("<");
            cover.cover_title2.setText(">");
            if(p == 0){
                cover.img_cover.setBackgroundResource(covers[0]);
            }else if(p==1){
                cover.img_cover.setBackgroundResource(covers[1]);
            }else if(p==2){
                cover.img_cover.setBackgroundResource(covers[2]);
            }else if(p==3){
                cover.img_cover.setBackgroundResource(covers[3]);
            }

            cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(p == 0){
                        tshirtList.clear();
                        Tshirt a = new Tshirt("Trés Fatiguée", tshirts[1]);
                        tshirtList.add(a);
                        a = new Tshirt("Ce n'est rien voilà tout", tshirts[2]);
                        tshirtList.add(a);
                        a = new Tshirt("a", tshirts[10]);
                        tshirtList.add(a);
                        a = new Tshirt("b", tshirts[11]);
                        tshirtList.add(a);
                        a = new Tshirt("c", tshirts[12]);
                        tshirtList.add(a);
                        adapter.notifyDataSetChanged();
                    } else if (p == 1) {
                        tshirtList.clear();
                        Tshirt a = new Tshirt("Just Do Nothing", tshirts[6]);
                        tshirtList.add(a);
                        a = new Tshirt("Artiste incompris", tshirts[5]);
                        tshirtList.add(a);
                        adapter.notifyDataSetChanged();
                    } else if (p == 2) {
                        tshirtList.clear();
                        Tshirt a = new Tshirt("BC BG", tshirts[3]);
                        tshirtList.add(a);
                        a = new Tshirt("Just Do Nothing", tshirts[0]);
                        tshirtList.add(a);
                        a = new Tshirt("Ananas", tshirts[4]);
                        tshirtList.add(a);
                        adapter.notifyDataSetChanged();
                    } else if (p == 3) {
                        tshirtList.clear();
                        Tshirt a = new Tshirt("mariejeanne", tshirts[7]);
                        tshirtList.add(a);
                        a = new Tshirt("monsieur muscle", tshirts[8]);
                        tshirtList.add(a);
                        a = new Tshirt("je m'en FISH", tshirts[9]);
                        tshirtList.add(a);
                        adapter.notifyDataSetChanged();
                    }

                }
            });
            container.addView(cover);
            return cover;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }


    }



}
