package com.amitbharat.keyboard.ime;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amitbharat.keyboard.R;
import java.util.ArrayList;
import java.util.List;

public class EmojiKeyboardView extends FrameLayout {

    public interface OnEmojiSelectedListener {
        void onEmojiSelected(String emoji);
        void onGifSelected(String title, String content);
        void onBackToAlpha();
        void onBackspace();
    }

    public static class GifItem {
        public final String title;
        public final String emoji;
        public final String message;

        public GifItem(String emoji, String title, String message) {
            this.emoji = emoji;
            this.title = title;
            this.message = message;
        }
    }

    private RecyclerView recyclerEmojis;
    private RecyclerView recyclerGifs;
    private TextView tvAbc;
    private TextView tvEmojiTab;
    private TextView tvGifTab;
    private ImageButton ibBackspace;
    private LinearLayout bottomBarLayout;
    private OnEmojiSelectedListener listener;

    private boolean isGifTabActive = false;

    public EmojiKeyboardView(Context context) {
        super(context);
        init(context);
    }

    public EmojiKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmojiKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(ContextCompatColor(context, R.color.keyboard_bg));

        // Create root container layout
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // Container for Emoji / GIF views
        FrameLayout contentContainer = new FrameLayout(context);
        LinearLayout.LayoutParams contentLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 1.0f
        );
        contentContainer.setLayoutParams(contentLp);

        // 1. Emoji Recycler
        recyclerEmojis = new RecyclerView(context);
        recyclerEmojis.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerEmojis.setLayoutManager(new GridLayoutManager(context, 8));
        recyclerEmojis.setClipToPadding(false);
        recyclerEmojis.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        recyclerEmojis.setAdapter(new EmojiAdapter(KeyboardLayoutHelper.EMOJIS));
        contentContainer.addView(recyclerEmojis);

        // 2. GIF Recycler
        recyclerGifs = new RecyclerView(context);
        recyclerGifs.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerGifs.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerGifs.setClipToPadding(false);
        recyclerGifs.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        recyclerGifs.setAdapter(new GifAdapter(getPopularGifs()));
        recyclerGifs.setVisibility(View.GONE);
        contentContainer.addView(recyclerGifs);

        root.addView(contentContainer);

        // Bottom Bar (ABC, Emoji Tab, GIF Tab, Spacer, Backspace)
        bottomBarLayout = new LinearLayout(context);
        bottomBarLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomBarLayout.setGravity(Gravity.CENTER_VERTICAL);
        bottomBarLayout.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));
        bottomBarLayout.setBackgroundColor(ContextCompatColor(context, R.color.candidate_bar_bg));
        LinearLayout.LayoutParams bottomBarLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, dpToPx(46)
        );
        bottomBarLayout.setLayoutParams(bottomBarLp);

        // ABC Button (Back to standard QWERTY)
        tvAbc = new TextView(context);
        tvAbc.setText("ABC");
        tvAbc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        tvAbc.setTypeface(null, android.graphics.Typeface.BOLD);
        tvAbc.setTextColor(ContextCompatColor(context, R.color.key_text_action));
        tvAbc.setBackgroundResource(R.drawable.bg_key_action);
        tvAbc.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams abcLp = new LinearLayout.LayoutParams(
                dpToPx(56), dpToPx(36)
        );
        tvAbc.setLayoutParams(abcLp);
        tvAbc.setOnClickListener(v -> {
            if (listener != null) listener.onBackToAlpha();
        });
        bottomBarLayout.addView(tvAbc);

        // Emoji Tab Button
        tvEmojiTab = new TextView(context);
        tvEmojiTab.setText("😊");
        tvEmojiTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f);
        tvEmojiTab.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams emojiTabLp = new LinearLayout.LayoutParams(
                dpToPx(46), dpToPx(36)
        );
        emojiTabLp.setMarginStart(dpToPx(6));
        tvEmojiTab.setLayoutParams(emojiTabLp);
        tvEmojiTab.setOnClickListener(v -> selectTab(false));
        bottomBarLayout.addView(tvEmojiTab);

        // GIF Tab Button
        tvGifTab = new TextView(context);
        tvGifTab.setText("GIF");
        tvGifTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.5f);
        tvGifTab.setTypeface(null, android.graphics.Typeface.BOLD);
        tvGifTab.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams gifTabLp = new LinearLayout.LayoutParams(
                dpToPx(52), dpToPx(36)
        );
        gifTabLp.setMarginStart(dpToPx(6));
        tvGifTab.setLayoutParams(gifTabLp);
        tvGifTab.setOnClickListener(v -> selectTab(true));
        bottomBarLayout.addView(tvGifTab);

        // Spacer
        View spacer = new View(context);
        LinearLayout.LayoutParams spacerLp = new LinearLayout.LayoutParams(
                0, 1, 1.0f
        );
        spacer.setLayoutParams(spacerLp);
        bottomBarLayout.addView(spacer);

        // Backspace button
        ibBackspace = new ImageButton(context);
        ibBackspace.setImageResource(R.drawable.ic_backspace);
        ibBackspace.setBackgroundResource(R.drawable.bg_key_action);
        ibBackspace.setColorFilter(ContextCompatColor(context, R.color.key_text_action));
        LinearLayout.LayoutParams bsLp = new LinearLayout.LayoutParams(
                dpToPx(56), dpToPx(36)
        );
        ibBackspace.setLayoutParams(bsLp);
        ibBackspace.setOnClickListener(v -> {
            if (listener != null) listener.onBackspace();
        });
        bottomBarLayout.addView(ibBackspace);

        root.addView(bottomBarLayout);
        addView(root);

        selectTab(false);
        updateTheme();
    }

    private void selectTab(boolean isGif) {
        this.isGifTabActive = isGif;
        if (isGif) {
            recyclerEmojis.setVisibility(View.GONE);
            recyclerGifs.setVisibility(View.VISIBLE);

            tvGifTab.setBackgroundResource(R.drawable.bg_key_action);
            tvGifTab.setTextColor(ContextCompatColor(getContext(), R.color.key_text_color));

            tvEmojiTab.setBackground(null);
            tvEmojiTab.setAlpha(0.65f);
        } else {
            recyclerEmojis.setVisibility(View.VISIBLE);
            recyclerGifs.setVisibility(View.GONE);

            tvEmojiTab.setBackgroundResource(R.drawable.bg_key_action);
            tvEmojiTab.setAlpha(1.0f);

            tvGifTab.setBackground(null);
            tvGifTab.setTextColor(ContextCompatColor(getContext(), R.color.key_text_action));
        }
    }

    public void updateTheme() {
        int bgColor = ContextCompatColor(getContext(), R.color.keyboard_bg);
        int barBgColor = ContextCompatColor(getContext(), R.color.candidate_bar_bg);
        setBackgroundColor(bgColor);
        if (bottomBarLayout != null) {
            bottomBarLayout.setBackgroundColor(barBgColor);
        }
        selectTab(isGifTabActive);
    }

    public void setOnEmojiSelectedListener(OnEmojiSelectedListener listener) {
        this.listener = listener;
    }

    private List<GifItem> getPopularGifs() {
        List<GifItem> list = new ArrayList<>();
        list.add(new GifItem("🎉", "Badhai Ho", "🎉 Mubarak! Badhai ho!"));
        list.add(new GifItem("🙏", "Namaste", "🙏 Namaste / Pranam"));
        list.add(new GifItem("😂", "Hahaha", "😂 Hahaha! Very funny!"));
        list.add(new GifItem("👍", "Zabardast", "👍 Zabardast! Super!"));
        list.add(new GifItem("🔥", "Dhamaka", "🔥 Kya baat hai! Dhamaka!"));
        list.add(new GifItem("❤️", "Dil Se", "❤️ Dil se shukriya!"));
        list.add(new GifItem("💃", "Nacho Party", "💃 Nacho nacho! Party time!"));
        list.add(new GifItem("☕", "Chai Peelo", "☕ Chalo chai peete hain!"));
        list.add(new GifItem("🇮🇳", "Jai Hind", "🇮🇳 Jai Hind! Bharat Mata ki Jai!"));
        list.add(new GifItem("🎂", "Happy B'day", "🎂 Janamdin Mubarak! Happy Birthday!"));
        list.add(new GifItem("👏", "Shabaash", "👏 Wah! Shabaash!"));
        list.add(new GifItem("🙌", "Balle Balle", "🙌 Balle Balle! Chak de phatte!"));
        list.add(new GifItem("😎", "Swag", "😎 Apna swag alag hai!"));
        list.add(new GifItem("🤝", "Dosti", "🤝 Dosti zindabad!"));
        list.add(new GifItem("🚀", "Mast", "🚀 Ekdum mast!"));
        list.add(new GifItem("😴", "Good Night", "😴 Shubh Ratri! Good Night!"));
        list.add(new GifItem("✨", "Shubhkamnaye", "✨ Hardik Shubhkamnayein!"));
        list.add(new GifItem("🤩", "Mindblowing", "🤩 Mindblowing!"));
        return list;
    }

    private class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {
        private final List<String> list;

        public EmojiAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
            tv.setGravity(Gravity.CENTER);
            tv.setIncludeFontPadding(false);
            tv.setPadding(0, 0, 0, 0);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(48)));
            TypedValue outValue = new TypedValue();
            parent.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
            tv.setBackgroundResource(outValue.resourceId);
            return new EmojiViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
            String emoji = list.get(position);
            holder.textView.setText(emoji);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onEmojiSelected(emoji);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class EmojiViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            EmojiViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }

    private class GifAdapter extends RecyclerView.Adapter<GifAdapter.GifViewHolder> {
        private final List<GifItem> list;

        public GifAdapter(List<GifItem> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public GifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout card = new LinearLayout(parent.getContext());
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER);
            card.setBackgroundResource(R.drawable.bg_key_action);
            int p = dpToPx(8);
            card.setPadding(p, p, p, p);

            GridLayoutManager.LayoutParams lp = new GridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(72)
            );
            int m = dpToPx(4);
            lp.setMargins(m, m, m, m);
            card.setLayoutParams(lp);

            TextView tvEmoji = new TextView(parent.getContext());
            tvEmoji.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
            tvEmoji.setGravity(Gravity.CENTER);
            card.addView(tvEmoji);

            TextView tvTitle = new TextView(parent.getContext());
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setTextColor(ContextCompatColor(parent.getContext(), R.color.key_text_color));
            tvTitle.setGravity(Gravity.CENTER);
            card.addView(tvTitle);

            return new GifViewHolder(card, tvEmoji, tvTitle);
        }

        @Override
        public void onBindViewHolder(@NonNull GifViewHolder holder, int position) {
            GifItem item = list.get(position);
            holder.tvEmoji.setText(item.emoji);
            holder.tvTitle.setText(item.title);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onGifSelected(item.title, item.message);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class GifViewHolder extends RecyclerView.ViewHolder {
            TextView tvEmoji;
            TextView tvTitle;
            GifViewHolder(View itemView, TextView tvEmoji, TextView tvTitle) {
                super(itemView);
                this.tvEmoji = tvEmoji;
                this.tvTitle = tvTitle;
            }
        }
    }

    private int dpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private int ContextCompatColor(Context context, int resId) {
        return androidx.core.content.ContextCompat.getColor(context, resId);
    }
}
