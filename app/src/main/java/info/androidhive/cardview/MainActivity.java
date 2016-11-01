package info.androidhive.cardview;

/**
 * Created by Amine Liazidi on 31/10/16.
 */
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.LinkagePager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PageItemClickListener;
import me.crosswall.lib.coverflow.core.PagerContainer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TshirtsAdapter adapter;
    List<Tshirt> tshirtList;
    int[] covers = new int[]{
            R.drawable.tshirt1,
            R.drawable.tshirt2,
            R.drawable.tshirt3,
            R.drawable.tshirt4,
            R.drawable.tshirt5,
            R.drawable.tshirt6,
            R.drawable.tshirt7,
            R.drawable.tshirt8,
            R.drawable.tshirt9,
            R.drawable.tshirt10,
            R.drawable.tshirt11
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
        Tshirt a = new Tshirt("Tshirt 8", covers[2]);
        tshirtList.add(a);
        a = new Tshirt("Tshirt 7", covers[3]);
        tshirtList.add(a);
        a = new Tshirt("Tshirt 6", covers[4]);
        tshirtList.add(a);
        a = new Tshirt("Tshirt 5", covers[5]);
        tshirtList.add(a);
        a = new Tshirt("Tshirt 10", covers[0]);
        tshirtList.add(a);
        a = new Tshirt("Tshirt 9", covers[1]);
        tshirtList.add(a);
        a = new Tshirt("Tshirt 4", covers[6]);
        tshirtList.add(a);
        a = new Tshirt("Tshirt 3", covers[7]);
        tshirtList.add(a);
        a = new Tshirt("Tshirt 2", covers[8]);
        tshirtList.add(a);
        a = new Tshirt("Tshirt 1", covers[9]);
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
            ImageView view = new ImageView(MainActivity.this);
            view.setBackgroundResource(R.drawable.tshirt1);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(p == 0){
                        tshirtList.clear();
                        Tshirt a = new Tshirt("Tshirt 9", covers[1]);
                        tshirtList.add(a);
                        a = new Tshirt("Tshirt 8", covers[2]);
                        tshirtList.add(a);
                        adapter.notifyDataSetChanged();
                    } else if (p == 1) {
                        tshirtList.clear();
                        Tshirt a = new Tshirt("Tshirt 7", covers[3]);
                        tshirtList.add(a);
                        a = new Tshirt("Tshirt 6", covers[4]);
                        tshirtList.add(a);
                        a = new Tshirt("Tshirt 5", covers[5]);
                        tshirtList.add(a);
                        adapter.notifyDataSetChanged();
                    } else if (p == 2) {
                        tshirtList.clear();
                        Tshirt a = new Tshirt("Tshirt 10", covers[0]);
                        tshirtList.add(a);
                        a = new Tshirt("Tshirt 4", covers[6]);
                        tshirtList.add(a);
                        adapter.notifyDataSetChanged();
                    } else if (p == 3) {
                        tshirtList.clear();
                        Tshirt a = new Tshirt("Tshirt 3", covers[7]);
                        tshirtList.add(a);
                        a = new Tshirt("Tshirt 2", covers[8]);
                        tshirtList.add(a);
                        a = new Tshirt("Tshirt 1", covers[9]);
                        tshirtList.add(a);
                        adapter.notifyDataSetChanged();
                    }

                }
            });
            container.addView(view);
            return view;
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
