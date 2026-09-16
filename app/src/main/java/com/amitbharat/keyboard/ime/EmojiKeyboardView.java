package com.amitbharat.keyboard.ime;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amitbharat.keyboard.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EmojiKeyboardView extends FrameLayout {

    public interface OnEmojiSelectedListener {
        void onEmojiSelected(String emoji);
        void onGifSelected(String title, String content);
        void onBackToAlpha();
        void onBackspace();
    }

    private static final String PREF_RECENT_EMOJIS = "pref_recent_emojis_list";
    private static final int TAB_EMOJI = 0;
    private static final int TAB_CLIPS = 1;
    private static final int TAB_GIF = 2;
    private static final int TAB_KAOMOJI = 3;

    private int currentTab = TAB_EMOJI;
    private String currentCategory = EmojiData.CAT_SMILEYS;
    private String currentMediaFilter = "All";

    private LinearLayout topBarContainer;
    private HorizontalScrollView categoryScrollView;
    private LinearLayout categoryStripLayout;
    private HorizontalScrollView filterScrollView;
    private LinearLayout filterChipsLayout;
    private EditText etSearch;
    private FrameLayout contentContainer;

    private RecyclerView recyclerEmojis;
    private RecyclerView recyclerClips;
    private RecyclerView recyclerGifs;
    private RecyclerView recyclerKaomoji;

    private LinearLayout bottomBarLayout;
    private TextView tvAbc;
    private TextView tvEmojiTab;
    private TextView tvClipsTab;
    private TextView tvGifTab;
    private TextView tvKaomojiTab;
    private ImageButton ibBackspace;

    private EmojiAdapter emojiAdapter;
    private VideoClipAdapter clipsAdapter;
    private GifCardAdapter gifAdapter;
    private KaomojiAdapter kaomojiAdapter;

    private OnEmojiSelectedListener listener;
    private SharedPreferences sharedPreferences;

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
        sharedPreferences = context.getSharedPreferences("indic_keyboard_media", Context.MODE_PRIVATE);
        setBackgroundColor(ContextCompatColor(context, R.color.keyboard_bg));

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // 1. Top Bar: Search Bar + Categories / Filters
        topBarContainer = new LinearLayout(context);
        topBarContainer.setOrientation(LinearLayout.VERTICAL);
        topBarContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        topBarContainer.setBackgroundColor(ContextCompatColor(context, R.color.candidate_bar_bg));

        // Search Bar
        LinearLayout searchLayout = new LinearLayout(context);
        searchLayout.setOrientation(LinearLayout.HORIZONTAL);
        searchLayout.setGravity(Gravity.CENTER_VERTICAL);
        searchLayout.setPadding(dpToPx(12), dpToPx(4), dpToPx(12), dpToPx(2));

        TextView tvSearchIcon = new TextView(context);
        tvSearchIcon.setText("🔍");
        tvSearchIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        searchLayout.addView(tvSearchIcon);

        etSearch = new EditText(context);
        etSearch.setHint("Search emojis, clips & GIFs...");
        etSearch.setHintTextColor(0xFF94A3B8);
        etSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.5f);
        etSearch.setTextColor(ContextCompatColor(context, R.color.key_text_color));
        etSearch.setBackground(null);
        etSearch.setSingleLine(true);
        LinearLayout.LayoutParams searchLp = new LinearLayout.LayoutParams(0, dpToPx(34), 1.0f);
        searchLp.setMarginStart(dpToPx(8));
        etSearch.setLayoutParams(searchLp);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applySearch(s != null ? s.toString() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        searchLayout.addView(etSearch);
        topBarContainer.addView(searchLayout);

        // Emoji Categories Strip
        categoryScrollView = new HorizontalScrollView(context);
        categoryScrollView.setHorizontalScrollBarEnabled(false);
        categoryScrollView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(36)));
        categoryStripLayout = new LinearLayout(context);
        categoryStripLayout.setOrientation(LinearLayout.HORIZONTAL);
        categoryStripLayout.setGravity(Gravity.CENTER_VERTICAL);
        categoryStripLayout.setPadding(dpToPx(6), 0, dpToPx(6), dpToPx(2));
        buildCategoryTabs(context);
        categoryScrollView.addView(categoryStripLayout);
        topBarContainer.addView(categoryScrollView);

        // Media Filter Chips (For Clips and GIFs: All, Trending, Memes, Bollywood, Desi, etc.)
        filterScrollView = new HorizontalScrollView(context);
        filterScrollView.setHorizontalScrollBarEnabled(false);
        filterScrollView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(36)));
        filterChipsLayout = new LinearLayout(context);
        filterChipsLayout.setOrientation(LinearLayout.HORIZONTAL);
        filterChipsLayout.setGravity(Gravity.CENTER_VERTICAL);
        filterChipsLayout.setPadding(dpToPx(6), 0, dpToPx(6), dpToPx(2));
        buildMediaFilterChips(context);
        filterScrollView.addView(filterChipsLayout);
        filterScrollView.setVisibility(View.GONE);
        topBarContainer.addView(filterScrollView);

        root.addView(topBarContainer);

        // 2. Middle Content Container
        contentContainer = new FrameLayout(context);
        LinearLayout.LayoutParams contentLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 1.0f
        );
        contentContainer.setLayoutParams(contentLp);

        // Emoji Recycler
        recyclerEmojis = new RecyclerView(context);
        recyclerEmojis.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerEmojis.setLayoutManager(new GridLayoutManager(context, 8));
        recyclerEmojis.setClipToPadding(false);
        recyclerEmojis.setPadding(dpToPx(4), dpToPx(2), dpToPx(4), dpToPx(4));
        emojiAdapter = new EmojiAdapter(getEmojisForCategory(currentCategory));
        recyclerEmojis.setAdapter(emojiAdapter);
        contentContainer.addView(recyclerEmojis);

        // Video Clips Recycler
        recyclerClips = new RecyclerView(context);
        recyclerClips.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerClips.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerClips.setClipToPadding(false);
        recyclerClips.setPadding(dpToPx(6), dpToPx(4), dpToPx(6), dpToPx(6));
        clipsAdapter = new VideoClipAdapter(EmojiData.getAllVideoClips());
        recyclerClips.setAdapter(clipsAdapter);
        recyclerClips.setVisibility(View.GONE);
        contentContainer.addView(recyclerClips);

        // GIF Recycler
        recyclerGifs = new RecyclerView(context);
        recyclerGifs.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerGifs.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerGifs.setClipToPadding(false);
        recyclerGifs.setPadding(dpToPx(6), dpToPx(4), dpToPx(6), dpToPx(6));
        gifAdapter = new GifCardAdapter(EmojiData.getAllGifCards());
        recyclerGifs.setAdapter(gifAdapter);
        recyclerGifs.setVisibility(View.GONE);
        contentContainer.addView(recyclerGifs);

        // Kaomoji Recycler
        recyclerKaomoji = new RecyclerView(context);
        recyclerKaomoji.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerKaomoji.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerKaomoji.setClipToPadding(false);
        recyclerKaomoji.setPadding(dpToPx(6), dpToPx(4), dpToPx(6), dpToPx(6));
        kaomojiAdapter = new KaomojiAdapter(getAllKaomojis());
        recyclerKaomoji.setAdapter(kaomojiAdapter);
        recyclerKaomoji.setVisibility(View.GONE);
        contentContainer.addView(recyclerKaomoji);

        root.addView(contentContainer);

        // 3. Bottom Control Bar (ABC Large, Emoji, Clips, GIF, Kaomoji, Spacer, Backspace)
        bottomBarLayout = new LinearLayout(context);
        bottomBarLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomBarLayout.setGravity(Gravity.CENTER_VERTICAL);
        bottomBarLayout.setPadding(dpToPx(8), dpToPx(3), dpToPx(8), dpToPx(3));
        bottomBarLayout.setBackgroundColor(ContextCompatColor(context, R.color.candidate_bar_bg));
        LinearLayout.LayoutParams bottomBarLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, dpToPx(48)
        );
        bottomBarLayout.setLayoutParams(bottomBarLp);

        // Large ABC Button as requested
        tvAbc = new TextView(context);
        tvAbc.setText("ABC");
        tvAbc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        tvAbc.setTypeface(null, android.graphics.Typeface.BOLD);
        tvAbc.setTextColor(0xFFFFFFFF);
        tvAbc.setBackgroundResource(R.drawable.bg_key_action);
        tvAbc.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams abcLp = new LinearLayout.LayoutParams(dpToPx(76), dpToPx(38));
        tvAbc.setLayoutParams(abcLp);
        tvAbc.setOnClickListener(v -> {
            performFeedback();
            if (listener != null) listener.onBackToAlpha();
        });
        bottomBarLayout.addView(tvAbc);

        // Emoji Tab
        tvEmojiTab = createBottomTab("😊", dpToPx(42));
        tvEmojiTab.setOnClickListener(v -> switchMainTab(TAB_EMOJI));
        bottomBarLayout.addView(tvEmojiTab);

        // Video Clips Tab (NEW)
        tvClipsTab = createBottomTab("▶ CLIPS", dpToPx(68));
        tvClipsTab.setOnClickListener(v -> switchMainTab(TAB_CLIPS));
        bottomBarLayout.addView(tvClipsTab);

        // GIF Tab
        tvGifTab = createBottomTab("GIF", dpToPx(46));
        tvGifTab.setOnClickListener(v -> switchMainTab(TAB_GIF));
        bottomBarLayout.addView(tvGifTab);

        // Kaomoji Tab
        tvKaomojiTab = createBottomTab("^_^", dpToPx(44));
        tvKaomojiTab.setOnClickListener(v -> switchMainTab(TAB_KAOMOJI));
        bottomBarLayout.addView(tvKaomojiTab);

        // Spacer
        View spacer = new View(context);
        LinearLayout.LayoutParams spacerLp = new LinearLayout.LayoutParams(0, 1, 1.0f);
        spacer.setLayoutParams(spacerLp);
        bottomBarLayout.addView(spacer);

        // Backspace
        ibBackspace = new ImageButton(context);
        ibBackspace.setImageResource(R.drawable.ic_backspace);
        ibBackspace.setBackgroundResource(R.drawable.bg_key_action);
        ibBackspace.setColorFilter(ContextCompatColor(context, R.color.key_text_action));
        LinearLayout.LayoutParams bsLp = new LinearLayout.LayoutParams(dpToPx(54), dpToPx(38));
        ibBackspace.setLayoutParams(bsLp);
        ibBackspace.setOnClickListener(v -> {
            performFeedback();
            if (listener != null) listener.onBackspace();
        });
        bottomBarLayout.addView(ibBackspace);

        root.addView(bottomBarLayout);
        addView(root);

        switchMainTab(TAB_EMOJI);
        updateTheme();
    }

    private TextView createBottomTab(String label, int width) {
        TextView tv = new TextView(getContext());
        tv.setText(label);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, label.equals("😊") ? 17f : 12f);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, dpToPx(38));
        lp.setMarginStart(dpToPx(4));
        tv.setLayoutParams(lp);
        return tv;
    }

    private void buildCategoryTabs(Context context) {
        categoryStripLayout.removeAllViews();
        for (final String catIcon : EmojiData.CATEGORY_ICONS) {
            TextView catView = new TextView(context);
            catView.setText(catIcon);
            catView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f);
            catView.setGravity(Gravity.CENTER);

            boolean isSelected = catIcon.equals(currentCategory);
            catView.setAlpha(isSelected ? 1.0f : 0.5f);
            if (isSelected) {
                catView.setBackgroundResource(R.drawable.bg_suggestion_chip);
            } else {
                catView.setBackground(null);
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpToPx(38), dpToPx(32));
            lp.setMargins(dpToPx(2), 0, dpToPx(2), 0);
            catView.setLayoutParams(lp);

            catView.setOnClickListener(v -> {
                performFeedback();
                currentCategory = catIcon;
                etSearch.setText("");
                buildCategoryTabs(getContext());
                emojiAdapter.updateList(getEmojisForCategory(currentCategory));
                recyclerEmojis.scrollToPosition(0);
            });

            categoryStripLayout.addView(catView);
        }
    }

    private void buildMediaFilterChips(Context context) {
        filterChipsLayout.removeAllViews();
        String[] filters = {"All", "Trending", "Memes", "Bollywood", "Celebration", "Desi", "Love"};
        for (final String filter : filters) {
            TextView chip = new TextView(context);
            chip.setText(filter);
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            chip.setTypeface(null, android.graphics.Typeface.BOLD);
            chip.setGravity(Gravity.CENTER);

            boolean isSelected = filter.equals(currentMediaFilter);
            chip.setTextColor(isSelected ? 0xFFFFFFFF : ContextCompatColor(context, R.color.key_text_color));
            chip.setBackgroundResource(isSelected ? R.drawable.bg_suggestion_chip : R.drawable.bg_key_action);
            chip.setPadding(dpToPx(10), dpToPx(4), dpToPx(10), dpToPx(4));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(30));
            lp.setMargins(dpToPx(3), 0, dpToPx(3), 0);
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                performFeedback();
                currentMediaFilter = filter;
                buildMediaFilterChips(getContext());
                applyMediaFilter();
            });

            filterChipsLayout.addView(chip);
        }
    }

    private void switchMainTab(int tab) {
        this.currentTab = tab;
        performFeedback();

        recyclerEmojis.setVisibility(tab == TAB_EMOJI ? View.VISIBLE : View.GONE);
        recyclerClips.setVisibility(tab == TAB_CLIPS ? View.VISIBLE : View.GONE);
        recyclerGifs.setVisibility(tab == TAB_GIF ? View.VISIBLE : View.GONE);
        recyclerKaomoji.setVisibility(tab == TAB_KAOMOJI ? View.VISIBLE : View.GONE);

        // Header controls visibility
        categoryScrollView.setVisibility(tab == TAB_EMOJI ? View.VISIBLE : View.GONE);
        filterScrollView.setVisibility((tab == TAB_CLIPS || tab == TAB_GIF) ? View.VISIBLE : View.GONE);
        topBarContainer.setVisibility(tab == TAB_KAOMOJI ? View.GONE : View.VISIBLE);

        highlightTab(tvEmojiTab, tab == TAB_EMOJI);
        highlightTab(tvClipsTab, tab == TAB_CLIPS);
        highlightTab(tvGifTab, tab == TAB_GIF);
        highlightTab(tvKaomojiTab, tab == TAB_KAOMOJI);

        if (tab == TAB_EMOJI) {
            buildCategoryTabs(getContext());
            emojiAdapter.updateList(getEmojisForCategory(currentCategory));
        } else if (tab == TAB_CLIPS || tab == TAB_GIF) {
            buildMediaFilterChips(getContext());
            applyMediaFilter();
        }
    }

    private void highlightTab(TextView tabView, boolean active) {
        if (active) {
            tabView.setBackgroundResource(R.drawable.bg_key_action);
            tabView.setTextColor(ContextCompatColor(getContext(), R.color.key_text_color));
            tabView.setAlpha(1.0f);
        } else {
            tabView.setBackground(null);
            tabView.setTextColor(ContextCompatColor(getContext(), R.color.key_text_action));
            tabView.setAlpha(0.65f);
        }
    }

    private void applyMediaFilter() {
        String query = etSearch.getText() != null ? etSearch.getText().toString().trim().toLowerCase() : "";
        if (currentTab == TAB_CLIPS) {
            List<EmojiData.VideoClip> all = EmojiData.getAllVideoClips();
            List<EmojiData.VideoClip> filtered = new ArrayList<>();
            for (EmojiData.VideoClip c : all) {
                boolean matchesFilter = "All".equals(currentMediaFilter) || c.category.equalsIgnoreCase(currentMediaFilter);
                boolean matchesQuery = query.isEmpty() || c.title.toLowerCase().contains(query) || c.caption.toLowerCase().contains(query);
                if (matchesFilter && matchesQuery) {
                    filtered.add(c);
                }
            }
            clipsAdapter.updateList(filtered);
            recyclerClips.scrollToPosition(0);
        } else if (currentTab == TAB_GIF) {
            List<EmojiData.GifCard> all = EmojiData.getAllGifCards();
            List<EmojiData.GifCard> filtered = new ArrayList<>();
            for (EmojiData.GifCard g : all) {
                boolean matchesFilter = "All".equals(currentMediaFilter) || g.category.equalsIgnoreCase(currentMediaFilter);
                boolean matchesQuery = query.isEmpty() || g.title.toLowerCase().contains(query) || g.message.toLowerCase().contains(query);
                if (matchesFilter && matchesQuery) {
                    filtered.add(g);
                }
            }
            gifAdapter.updateList(filtered);
            recyclerGifs.scrollToPosition(0);
        }
    }

    private void applySearch(String query) {
        if (currentTab == TAB_EMOJI) {
            if (query.trim().isEmpty()) {
                emojiAdapter.updateList(getEmojisForCategory(currentCategory));
            } else {
                emojiAdapter.updateList(EmojiData.searchEmojis(query));
            }
            recyclerEmojis.scrollToPosition(0);
        } else if (currentTab == TAB_CLIPS || currentTab == TAB_GIF) {
            applyMediaFilter();
        }
    }

    private List<String> getEmojisForCategory(String category) {
        if (EmojiData.CAT_RECENT.equals(category)) {
            List<String> recents = getRecentEmojis();
            if (recents.isEmpty()) {
                return EmojiData.CATEGORY_EMOJIS.get(EmojiData.CAT_SMILEYS).subList(0, 32);
            }
            return recents;
        }
        List<String> list = EmojiData.CATEGORY_EMOJIS.get(category);
        return list != null ? list : Collections.emptyList();
    }

    private List<String> getRecentEmojis() {
        String saved = sharedPreferences.getString(PREF_RECENT_EMOJIS, "");
        if (saved == null || saved.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(saved.split(",")));
    }

    private void saveRecentEmoji(String emoji) {
        List<String> recents = getRecentEmojis();
        recents.remove(emoji);
        recents.add(0, emoji);
        if (recents.size() > 48) recents = recents.subList(0, 48);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recents.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(recents.get(i));
        }
        sharedPreferences.edit().putString(PREF_RECENT_EMOJIS, sb.toString()).apply();
    }

    private List<String> getAllKaomojis() {
        List<String> list = new ArrayList<>();
        for (List<String> group : EmojiData.KAOMOJI_CATEGORIES.values()) {
            list.addAll(group);
        }
        return list;
    }

    public void updateTheme() {
        int bgColor = ContextCompatColor(getContext(), R.color.keyboard_bg);
        int barBgColor = ContextCompatColor(getContext(), R.color.candidate_bar_bg);
        setBackgroundColor(bgColor);
        if (bottomBarLayout != null) bottomBarLayout.setBackgroundColor(barBgColor);
        if (topBarContainer != null) topBarContainer.setBackgroundColor(barBgColor);
        switchMainTab(currentTab);
    }

    public void setOnEmojiSelectedListener(OnEmojiSelectedListener listener) {
        this.listener = listener;
    }

    private void performFeedback() {
        try {
            performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
        } catch (Exception ignored) {}
    }

    // Emoji Adapter
    private class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {
        private List<String> list;

        public EmojiAdapter(List<String> list) {
            this.list = new ArrayList<>(list);
        }

        public void updateList(List<String> newList) {
            this.list = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f);
            tv.setGravity(Gravity.CENTER);
            tv.setIncludeFontPadding(false);
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
                performFeedback();
                saveRecentEmoji(emoji);
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

    // Video Clips Adapter (NEW)
    private class VideoClipAdapter extends RecyclerView.Adapter<VideoClipAdapter.ClipViewHolder> {
        private List<EmojiData.VideoClip> list;

        public VideoClipAdapter(List<EmojiData.VideoClip> list) {
            this.list = new ArrayList<>(list);
        }

        public void updateList(List<EmojiData.VideoClip> newList) {
            this.list = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ClipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout card = new LinearLayout(parent.getContext());
            card.setOrientation(LinearLayout.VERTICAL);
            card.setBackgroundResource(R.drawable.bg_key_action);
            card.setPadding(dpToPx(8), dpToPx(6), dpToPx(8), dpToPx(6));

            GridLayoutManager.LayoutParams lp = new GridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(76)
            );
            int m = dpToPx(4);
            lp.setMargins(m, m, m, m);
            card.setLayoutParams(lp);

            // Header row: Emoji + Tag + Duration
            LinearLayout header = new LinearLayout(parent.getContext());
            header.setOrientation(LinearLayout.HORIZONTAL);
            header.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvEmoji = new TextView(parent.getContext());
            tvEmoji.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            header.addView(tvEmoji);

            View headerSpacer = new View(parent.getContext());
            header.addView(headerSpacer, new LinearLayout.LayoutParams(0, 1, 1.0f));

            TextView tvDuration = new TextView(parent.getContext());
            tvDuration.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
            tvDuration.setTypeface(null, android.graphics.Typeface.BOLD);
            tvDuration.setTextColor(0xFF38BDF8);
            tvDuration.setBackgroundResource(R.drawable.bg_suggestion_chip);
            tvDuration.setPadding(dpToPx(6), dpToPx(2), dpToPx(6), dpToPx(2));
            header.addView(tvDuration);

            card.addView(header);

            // Title
            TextView tvTitle = new TextView(parent.getContext());
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.5f);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setTextColor(ContextCompatColor(parent.getContext(), R.color.key_text_color));
            tvTitle.setSingleLine(true);
            tvTitle.setPadding(0, dpToPx(3), 0, 0);
            card.addView(tvTitle);

            // Category subtitle
            TextView tvCat = new TextView(parent.getContext());
            tvCat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
            tvCat.setTextColor(0xFF94A3B8);
            tvCat.setSingleLine(true);
            card.addView(tvCat);

            return new ClipViewHolder(card, tvEmoji, tvDuration, tvTitle, tvCat);
        }

        @Override
        public void onBindViewHolder(@NonNull ClipViewHolder holder, int position) {
            EmojiData.VideoClip clip = list.get(position);
            holder.tvEmoji.setText(clip.emoji);
            holder.tvDuration.setText("▶ " + clip.duration);
            holder.tvTitle.setText(clip.title);
            holder.tvCat.setText("🎬 " + clip.category);
            holder.itemView.setOnClickListener(v -> {
                performFeedback();
                if (listener != null) {
                    listener.onGifSelected(clip.title, clip.caption);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ClipViewHolder extends RecyclerView.ViewHolder {
            TextView tvEmoji;
            TextView tvDuration;
            TextView tvTitle;
            TextView tvCat;
            ClipViewHolder(View itemView, TextView tvEmoji, TextView tvDuration, TextView tvTitle, TextView tvCat) {
                super(itemView);
                this.tvEmoji = tvEmoji;
                this.tvDuration = tvDuration;
                this.tvTitle = tvTitle;
                this.tvCat = tvCat;
            }
        }
    }

    // GIF and Greeting Cards Adapter
    private class GifCardAdapter extends RecyclerView.Adapter<GifCardAdapter.GifViewHolder> {
        private List<EmojiData.GifCard> list;

        public GifCardAdapter(List<EmojiData.GifCard> list) {
            this.list = new ArrayList<>(list);
        }

        public void updateList(List<EmojiData.GifCard> newList) {
            this.list = new ArrayList<>(newList);
            notifyDataSetChanged();
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
                    dpToPx(74)
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
            EmojiData.GifCard item = list.get(position);
            holder.tvEmoji.setText(item.emoji);
            holder.tvTitle.setText(item.title);
            holder.itemView.setOnClickListener(v -> {
                performFeedback();
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

    // Kaomoji / Text Emoticons Adapter
    private class KaomojiAdapter extends RecyclerView.Adapter<KaomojiAdapter.KaomojiViewHolder> {
        private final List<String> list;

        public KaomojiAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public KaomojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
            tv.setTextColor(ContextCompatColor(parent.getContext(), R.color.key_text_color));
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.drawable.bg_key_action);
            int p = dpToPx(8);
            tv.setPadding(p, p, p, p);

            GridLayoutManager.LayoutParams lp = new GridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(44)
            );
            int m = dpToPx(3);
            lp.setMargins(m, m, m, m);
            tv.setLayoutParams(lp);

            return new KaomojiViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(@NonNull KaomojiViewHolder holder, int position) {
            String face = list.get(position);
            holder.textView.setText(face);
            holder.itemView.setOnClickListener(v -> {
                performFeedback();
                if (listener != null) {
                    listener.onEmojiSelected(face + " ");
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class KaomojiViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            KaomojiViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }

    private int dpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private int ContextCompatColor(Context context, int resId) {
        return ContextCompat.getColor(context, resId);
    }
}
