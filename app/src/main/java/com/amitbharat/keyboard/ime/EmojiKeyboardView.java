package com.amitbharat.keyboard.ime;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amitbharat.keyboard.R;
import com.amitbharat.keyboard.engine.KeyboardPreferences;
import java.util.List;

public class EmojiKeyboardView extends FrameLayout {

    public interface OnEmojiSelectedListener {
        void onEmojiSelected(String emoji);
        void onBackToAlpha();
        void onBackspace();
    }

    private RecyclerView recyclerEmojis;
    private View btnBackToAlpha;
    private ImageButton btnBackspace;
    private android.widget.LinearLayout bottomBarLayout;
    private OnEmojiSelectedListener listener;

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

        // Create container layout
        android.widget.LinearLayout root = new android.widget.LinearLayout(context);
        root.setOrientation(android.widget.LinearLayout.VERTICAL);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // Emoji Recycler
        recyclerEmojis = new RecyclerView(context);
        android.widget.LinearLayout.LayoutParams recyclerLp = new android.widget.LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 1.0f
        );
        recyclerEmojis.setLayoutParams(recyclerLp);
        recyclerEmojis.setLayoutManager(new GridLayoutManager(context, 8));
        recyclerEmojis.setClipToPadding(false);
        recyclerEmojis.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        recyclerEmojis.setAdapter(new EmojiAdapter(KeyboardLayoutHelper.EMOJIS));
        root.addView(recyclerEmojis);

        // Bottom Bar (ABC, Space, Backspace)
        bottomBarLayout = new android.widget.LinearLayout(context);
        bottomBarLayout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        bottomBarLayout.setGravity(Gravity.CENTER_VERTICAL);
        bottomBarLayout.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));
        bottomBarLayout.setBackgroundColor(ContextCompatColor(context, R.color.candidate_bar_bg));
        android.widget.LinearLayout.LayoutParams bottomBarLp = new android.widget.LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, dpToPx(46)
        );
        bottomBarLayout.setLayoutParams(bottomBarLp);

        TextView tvAbc = new TextView(context);
        tvAbc.setText("ABC");
        tvAbc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        tvAbc.setTypeface(null, android.graphics.Typeface.BOLD);
        tvAbc.setTextColor(ContextCompatColor(context, R.color.key_text_action));
        tvAbc.setBackgroundResource(R.drawable.bg_key_action);
        tvAbc.setGravity(Gravity.CENTER);
        android.widget.LinearLayout.LayoutParams abcLp = new android.widget.LinearLayout.LayoutParams(
                dpToPx(76), dpToPx(38)
        );
        tvAbc.setLayoutParams(abcLp);
        tvAbc.setOnClickListener(v -> {
            if (listener != null) listener.onBackToAlpha();
        });
        bottomBarLayout.addView(tvAbc);

        View spacer = new View(context);
        android.widget.LinearLayout.LayoutParams spacerLp = new android.widget.LinearLayout.LayoutParams(
                0, 1, 1.0f
        );
        spacer.setLayoutParams(spacerLp);
        bottomBarLayout.addView(spacer);

        ImageButton ibBackspace = new ImageButton(context);
        ibBackspace.setImageResource(R.drawable.ic_backspace);
        ibBackspace.setBackgroundResource(R.drawable.bg_key_action);
        ibBackspace.setColorFilter(ContextCompatColor(context, R.color.key_text_action));
        android.widget.LinearLayout.LayoutParams bsLp = new android.widget.LinearLayout.LayoutParams(
                dpToPx(76), dpToPx(38)
        );
        ibBackspace.setLayoutParams(bsLp);
        ibBackspace.setOnClickListener(v -> {
            if (listener != null) listener.onBackspace();
        });
        bottomBarLayout.addView(ibBackspace);

        root.addView(bottomBarLayout);
        addView(root);
        updateTheme();
    }

    public void updateTheme() {
        int bgColor = ContextCompatColor(getContext(), R.color.keyboard_bg);
        int barBgColor = ContextCompatColor(getContext(), R.color.candidate_bar_bg);
        setBackgroundColor(bgColor);
        if (bottomBarLayout != null) {
            bottomBarLayout.setBackgroundColor(barBgColor);
        }
    }

    public void setOnEmojiSelectedListener(OnEmojiSelectedListener listener) {
        this.listener = listener;
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

    private int dpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private int ContextCompatColor(Context context, int resId) {
        return androidx.core.content.ContextCompat.getColor(context, resId);
    }
}
