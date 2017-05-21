package com.dimon.ganwumei.ui.newsfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.database.entities.Item;
import com.dimon.ganwumei.ui.UiHelper;
import com.dimon.ganwumei.ui.newsfeed.activity.WebActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * Created by Dimon on 2016/4/20.
 */
public class GanWuListAdapter extends RecyclerView.Adapter<GanWuListAdapter.NewsViewHolder>{
    private List<Item> mItems;
    private Context mContext;
    private boolean animateItems = false;
    private int lastAnimatedPosition = -1;
    public GanWuListAdapter(List<Item> items, Context context) {
        this.mItems = items;
        this.mContext = context;
    }

    //自定义ViewHolder类
     class NewsViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.news_who)
        TextView news_who;
        @Bind(R.id.news_desc)
        TextView news_desc;

        public NewsViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        void onGanWu(View v) {
            Item item = mItems.get(getLayoutPosition());
            Intent intent = WebActivity.newIntent(v.getContext(), item.getUrl(), item.getDescription());
            v.getContext().startActivity(intent);
        }

    }
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.item_list,viewGroup,false);
        NewsViewHolder nvh=new NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder personViewHolder, int i) {
        runEnterAnimation(personViewHolder.itemView,i);
        personViewHolder.news_who.setText(mItems.get(i).getWho() + "：");
        personViewHolder.news_desc.setText(mItems.get(i).getDescription());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<Item> items, boolean animated) {
        animateItems = animated;
        lastAnimatedPosition = -1;
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    private void runEnterAnimation(View view, int position) {
        if (!animateItems || position >= 5) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(UiHelper.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setStartDelay(100 * position)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }
}
