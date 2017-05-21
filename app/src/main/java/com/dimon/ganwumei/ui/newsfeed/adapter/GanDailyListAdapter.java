package com.dimon.ganwumei.ui.newsfeed.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dimon.ganwumei.R;
import com.dimon.ganwumei.database.entities.News;
import com.dimon.ganwumei.ui.newsfeed.activity.WebActivity;
import com.dimon.ganwumei.util.Preconditions;
import com.socks.library.KLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * Created by Dimon on 2016/5/3.
 */
public class GanDailyListAdapter extends AnimRecyclerViewAdapter<GanDailyListAdapter.ViewHolder> {

    private List<News> mGanList;

    public GanDailyListAdapter(List<News> ganList) {
        mGanList = ganList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mGanList.get(position);
        if (position == 0) {
            showCategory(holder);
        } else {
            boolean theCategoryOfLastEqualsToThis = mGanList.get(
                    position - 1).getType().equals(mGanList.get(position).getType());
            if (!theCategoryOfLastEqualsToThis) {
                showCategory(holder);
            } else {
                hideCategory(holder);
            }
        }
        holder.category.setText(news.getType());
        SpannableStringBuilder builder = new SpannableStringBuilder(news.getDesc()).append(
                Preconditions.format(holder.ganwu.getContext(), " (via. " +
                        news.getWho() +
                        ")", R.style.ViaTextAppearance));
        CharSequence ganwuText = builder.subSequence(0, builder.length());

        holder.ganwu.setText(ganwuText);
        showItemAnim(holder.ganwu, position);
    }


    @Override
    public int getItemCount() {
        return mGanList.size();
    }


    private void showCategory(ViewHolder holder) {
        if (!isVisibleOf(holder.category)) holder.category.setVisibility(View.VISIBLE);
    }


    private void hideCategory(ViewHolder holder) {
        if (isVisibleOf(holder.category)) holder.category.setVisibility(View.GONE);
    }


    /**
     * view.isShown() is a kidding...
     */
    private boolean isVisibleOf(View view) {
        return view.getVisibility() == View.VISIBLE;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.news_who)
        TextView category;
        @Bind(R.id.news_desc)
        TextView ganwu;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        @OnClick(R.id.card_view)
        void onGank(View v) {
            News news = mGanList.get(getLayoutPosition());
            Intent intent = WebActivity.newIntent(v.getContext(), news.getUrl(), news.getDesc());
            KLog.d(intent);
            v.getContext().startActivity(intent);
        }
    }
}