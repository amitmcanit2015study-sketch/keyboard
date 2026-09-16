package com.amitbharat.keyboard.ime;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewGroup;
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
    public static final int TAB_EMOJI = 0;
    public static final int TAB_CLIPS = 1;
    public static final int TAB_GIF = 2;
    public static final int TAB_KAOMOJI = 3;

    private int currentTab = TAB_EMOJI;
    private String currentCategory = EmojiData.CAT_SMILEYS;
    private String currentClipFilter = "All";
    private String currentGifCategory = null; // null means showing the 8 visual category cards

    private LinearLayout topBarContainer;
    private HorizontalScrollView categoryScrollView;
    private LinearLayout categoryStripLayout;

    private HorizontalScrollView clipFilterScrollView;
    private LinearLayout clipFilterChipsLayout;

    private LinearLayout gifHeaderLayout;
    private TextView tvGifBackBtn;
    private TextView tvGifActiveCategory;
    private HorizontalScrollView gifFilterScrollView;
    private LinearLayout gifFilterChipsLayout;

    private HorizontalScrollView kaomojiFilterScrollView;
    private LinearLayout kaomojiFilterChipsLayout;
    private String currentKaomojiFilter = "All";

    private FrameLayout contentContainer;

    private RecyclerView recyclerEmojis;
    private RecyclerView recyclerClips;
    private RecyclerView recyclerGifCategories;
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
    private GifCategoryAdapter gifCategoryAdapter;
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

        // 1. Top Bar Container (No Search Bar - Streamlined Direct Categories)
        topBarContainer = new LinearLayout(context);
        topBarContainer.setOrientation(LinearLayout.VERTICAL);
        topBarContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        topBarContainer.setBackgroundColor(ContextCompatColor(context, R.color.candidate_bar_bg));

        // 1A. Emoji Categories Strip
        categoryScrollView = new HorizontalScrollView(context);
        categoryScrollView.setHorizontalScrollBarEnabled(false);
        categoryScrollView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(38)));
        categoryStripLayout = new LinearLayout(context);
        categoryStripLayout.setOrientation(LinearLayout.HORIZONTAL);
        categoryStripLayout.setGravity(Gravity.CENTER_VERTICAL);
        categoryStripLayout.setPadding(dpToPx(6), 0, dpToPx(6), 0);
        buildCategoryTabs(context);
        categoryScrollView.addView(categoryStripLayout);
        topBarContainer.addView(categoryScrollView);

        // 1B. Clips Filter Chips (All, Trending, Memes, Bollywood, Celebration, Desi, Love)
        clipFilterScrollView = new HorizontalScrollView(context);
        clipFilterScrollView.setHorizontalScrollBarEnabled(false);
        clipFilterScrollView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(38)));
        clipFilterChipsLayout = new LinearLayout(context);
        clipFilterChipsLayout.setOrientation(LinearLayout.HORIZONTAL);
        clipFilterChipsLayout.setGravity(Gravity.CENTER_VERTICAL);
        clipFilterChipsLayout.setPadding(dpToPx(6), 0, dpToPx(6), 0);
        buildClipFilterChips(context);
        clipFilterScrollView.addView(clipFilterChipsLayout);
        clipFilterScrollView.setVisibility(View.GONE);
        topBarContainer.addView(clipFilterScrollView);

        // 1C. GIF Header & Category Switcher Bar (Back button + Title + Category chips)
        gifHeaderLayout = new LinearLayout(context);
        gifHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
        gifHeaderLayout.setGravity(Gravity.CENTER_VERTICAL);
        gifHeaderLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(38)));
        gifHeaderLayout.setPadding(dpToPx(6), 0, dpToPx(6), 0);
        gifHeaderLayout.setVisibility(View.GONE);

        tvGifBackBtn = new TextView(context);
        tvGifBackBtn.setText("‹ All");
        tvGifBackBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        tvGifBackBtn.setTypeface(null, android.graphics.Typeface.BOLD);
        tvGifBackBtn.setTextColor(0xFF38BDF8);
        tvGifBackBtn.setBackgroundResource(R.drawable.bg_suggestion_chip);
        tvGifBackBtn.setPadding(dpToPx(10), dpToPx(4), dpToPx(10), dpToPx(4));
        tvGifBackBtn.setGravity(Gravity.CENTER);
        tvGifBackBtn.setOnClickListener(v -> {
            performFeedback();
            showGifCategoryOverview();
        });
        gifHeaderLayout.addView(tvGifBackBtn);

        tvGifActiveCategory = new TextView(context);
        tvGifActiveCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        tvGifActiveCategory.setTypeface(null, android.graphics.Typeface.BOLD);
        tvGifActiveCategory.setTextColor(ContextCompatColor(context, R.color.key_text_color));
        tvGifActiveCategory.setPadding(dpToPx(8), 0, dpToPx(8), 0);
        gifHeaderLayout.addView(tvGifActiveCategory);

        gifFilterScrollView = new HorizontalScrollView(context);
        gifFilterScrollView.setHorizontalScrollBarEnabled(false);
        gifFilterScrollView.setLayoutParams(new LinearLayout.LayoutParams(0, dpToPx(38), 1.0f));
        gifFilterChipsLayout = new LinearLayout(context);
        gifFilterChipsLayout.setOrientation(LinearLayout.HORIZONTAL);
        gifFilterChipsLayout.setGravity(Gravity.CENTER_VERTICAL);
        buildGifFilterChips(context);
        gifFilterScrollView.addView(gifFilterChipsLayout);
        gifHeaderLayout.addView(gifFilterScrollView);
        topBarContainer.addView(gifHeaderLayout);

        // 1D. Kaomoji Filter Chips
        kaomojiFilterScrollView = new HorizontalScrollView(context);
        kaomojiFilterScrollView.setHorizontalScrollBarEnabled(false);
        kaomojiFilterScrollView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(38)));
        kaomojiFilterChipsLayout = new LinearLayout(context);
        kaomojiFilterChipsLayout.setOrientation(LinearLayout.HORIZONTAL);
        kaomojiFilterChipsLayout.setGravity(Gravity.CENTER_VERTICAL);
        kaomojiFilterChipsLayout.setPadding(dpToPx(6), 0, dpToPx(6), 0);
        buildKaomojiFilterChips(context);
        kaomojiFilterScrollView.addView(kaomojiFilterChipsLayout);
        kaomojiFilterScrollView.setVisibility(View.GONE);
        topBarContainer.addView(kaomojiFilterScrollView);

        root.addView(topBarContainer);

        // 2. Middle Content Container (Fills standard keyboard height seamlessly)
        contentContainer = new FrameLayout(context);
        LinearLayout.LayoutParams contentLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 1.0f
        );
        contentContainer.setLayoutParams(contentLp);

        // 2A. Emoji Recycler (8 Columns)
        recyclerEmojis = new RecyclerView(context);
        recyclerEmojis.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerEmojis.setLayoutManager(new GridLayoutManager(context, 8));
        recyclerEmojis.setClipToPadding(false);
        recyclerEmojis.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        emojiAdapter = new EmojiAdapter(getEmojisForCategory(currentCategory));
        recyclerEmojis.setAdapter(emojiAdapter);
        contentContainer.addView(recyclerEmojis);

        // 2B. Video Clips Recycler (2 Columns)
        recyclerClips = new RecyclerView(context);
        recyclerClips.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerClips.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerClips.setClipToPadding(false);
        recyclerClips.setPadding(dpToPx(6), dpToPx(4), dpToPx(6), dpToPx(6));
        clipsAdapter = new VideoClipAdapter(EmojiData.getAllVideoClips());
        recyclerClips.setAdapter(clipsAdapter);
        recyclerClips.setVisibility(View.GONE);
        contentContainer.addView(recyclerClips);

        // 2C. Visual GIF Categories Cards Recycler (2 Columns, matching Screenshot 3)
        recyclerGifCategories = new RecyclerView(context);
        recyclerGifCategories.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerGifCategories.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerGifCategories.setClipToPadding(false);
        recyclerGifCategories.setPadding(dpToPx(6), dpToPx(4), dpToPx(6), dpToPx(6));
        gifCategoryAdapter = new GifCategoryAdapter(EmojiData.getGifVisualCategories());
        recyclerGifCategories.setAdapter(gifCategoryAdapter);
        recyclerGifCategories.setVisibility(View.GONE);
        contentContainer.addView(recyclerGifCategories);

        // 2D. GIF Items Recycler (2 Columns, when a category is selected)
        recyclerGifs = new RecyclerView(context);
        recyclerGifs.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        recyclerGifs.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerGifs.setClipToPadding(false);
        recyclerGifs.setPadding(dpToPx(6), dpToPx(4), dpToPx(6), dpToPx(6));
        gifAdapter = new GifCardAdapter(Collections.emptyList());
        recyclerGifs.setAdapter(gifAdapter);
        recyclerGifs.setVisibility(View.GONE);
        contentContainer.addView(recyclerGifs);

        // 2E. Kaomoji Recycler (3 Columns)
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
        // With bottom padding so buttons do not hit system gesture/nav bars
        bottomBarLayout = new LinearLayout(context);
        bottomBarLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomBarLayout.setGravity(Gravity.CENTER_VERTICAL);
        bottomBarLayout.setPadding(dpToPx(8), dpToPx(2), dpToPx(8), dpToPx(6));
        bottomBarLayout.setBackgroundColor(ContextCompatColor(context, R.color.candidate_bar_bg));
        LinearLayout.LayoutParams bottomBarLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, dpToPx(46)
        );
        bottomBarLayout.setLayoutParams(bottomBarLp);

        // Large ABC Button as requested
        tvAbc = new TextView(context);
        tvAbc.setText("ABC");
        tvAbc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
        tvAbc.setTypeface(null, android.graphics.Typeface.BOLD);
        tvAbc.setTextColor(0xFFFFFFFF);
        tvAbc.setBackgroundResource(R.drawable.bg_key_action);
        tvAbc.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams abcLp = new LinearLayout.LayoutParams(dpToPx(76), dpToPx(36));
        tvAbc.setLayoutParams(abcLp);
        tvAbc.setOnClickListener(v -> {
            performFeedback();
            if (listener != null) listener.onBackToAlpha();
        });
        bottomBarLayout.addView(tvAbc);

        // Emoji Tab
        tvEmojiTab = createBottomTab("😊", dpToPx(44));
        tvEmojiTab.setOnClickListener(v -> switchMainTab(TAB_EMOJI));
        bottomBarLayout.addView(tvEmojiTab);

        // Video Clips Tab
        tvClipsTab = createBottomTab("▶ CLIPS", dpToPx(68));
        tvClipsTab.setOnClickListener(v -> switchMainTab(TAB_CLIPS));
        bottomBarLayout.addView(tvClipsTab);

        // GIF Tab
        tvGifTab = createBottomTab("GIF", dpToPx(48));
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
        LinearLayout.LayoutParams bsLp = new LinearLayout.LayoutParams(dpToPx(52), dpToPx(36));
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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, dpToPx(36));
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
                buildCategoryTabs(getContext());
                emojiAdapter.updateList(getEmojisForCategory(currentCategory));
                recyclerEmojis.scrollToPosition(0);
            });

            categoryStripLayout.addView(catView);
        }
    }

    private void buildClipFilterChips(Context context) {
        clipFilterChipsLayout.removeAllViews();
        String[] filters = {"All", "Trending", "Memes", "Bollywood", "Celebration", "Desi", "Love"};
        for (final String filter : filters) {
            TextView chip = new TextView(context);
            chip.setText(filter);
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            chip.setTypeface(null, android.graphics.Typeface.BOLD);
            chip.setGravity(Gravity.CENTER);

            boolean isSelected = filter.equals(currentClipFilter);
            chip.setTextColor(isSelected ? 0xFFFFFFFF : ContextCompatColor(context, R.color.key_text_color));
            chip.setBackgroundResource(isSelected ? R.drawable.bg_suggestion_chip : R.drawable.bg_key_action);
            chip.setPadding(dpToPx(10), dpToPx(4), dpToPx(10), dpToPx(4));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(28));
            lp.setMargins(dpToPx(3), 0, dpToPx(3), 0);
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                performFeedback();
                currentClipFilter = filter;
                buildClipFilterChips(getContext());
                applyClipFilter();
            });

            clipFilterChipsLayout.addView(chip);
        }
    }

    private void buildGifFilterChips(Context context) {
        gifFilterChipsLayout.removeAllViews();
        List<EmojiData.GifCategory> cats = EmojiData.getGifVisualCategories();
        for (final EmojiData.GifCategory cat : cats) {
            TextView chip = new TextView(context);
            chip.setText(cat.icon + " " + cat.title);
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11.5f);
            chip.setTypeface(null, android.graphics.Typeface.BOLD);
            chip.setGravity(Gravity.CENTER);

            boolean isSelected = cat.id.equalsIgnoreCase(currentGifCategory);
            chip.setTextColor(isSelected ? 0xFFFFFFFF : ContextCompatColor(context, R.color.key_text_color));
            chip.setBackgroundResource(isSelected ? R.drawable.bg_suggestion_chip : R.drawable.bg_key_action);
            chip.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(28));
            lp.setMargins(dpToPx(3), 0, dpToPx(3), 0);
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                performFeedback();
                selectGifCategory(cat.id, cat.title);
            });

            gifFilterChipsLayout.addView(chip);
        }
    }

    private void buildKaomojiFilterChips(Context context) {
        kaomojiFilterChipsLayout.removeAllViews();
        List<String> groups = new ArrayList<>();
        groups.add("All");
        groups.addAll(EmojiData.KAOMOJI_CATEGORIES.keySet());

        for (final String group : groups) {
            TextView chip = new TextView(context);
            chip.setText(group);
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            chip.setTypeface(null, android.graphics.Typeface.BOLD);
            chip.setGravity(Gravity.CENTER);

            boolean isSelected = group.equals(currentKaomojiFilter);
            chip.setTextColor(isSelected ? 0xFFFFFFFF : ContextCompatColor(context, R.color.key_text_color));
            chip.setBackgroundResource(isSelected ? R.drawable.bg_suggestion_chip : R.drawable.bg_key_action);
            chip.setPadding(dpToPx(10), dpToPx(4), dpToPx(10), dpToPx(4));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(28));
            lp.setMargins(dpToPx(3), 0, dpToPx(3), 0);
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                performFeedback();
                currentKaomojiFilter = group;
                buildKaomojiFilterChips(getContext());
                applyKaomojiFilter();
            });

            kaomojiFilterChipsLayout.addView(chip);
        }
    }

    private void switchMainTab(int tab) {
        this.currentTab = tab;
        performFeedback();

        recyclerEmojis.setVisibility(tab == TAB_EMOJI ? View.VISIBLE : View.GONE);
        recyclerClips.setVisibility(tab == TAB_CLIPS ? View.VISIBLE : View.GONE);
        recyclerKaomoji.setVisibility(tab == TAB_KAOMOJI ? View.VISIBLE : View.GONE);

        // Header controls visibility
        categoryScrollView.setVisibility(tab == TAB_EMOJI ? View.VISIBLE : View.GONE);
        clipFilterScrollView.setVisibility(tab == TAB_CLIPS ? View.VISIBLE : View.GONE);
        kaomojiFilterScrollView.setVisibility(tab == TAB_KAOMOJI ? View.VISIBLE : View.GONE);

        if (tab == TAB_GIF) {
            if (currentGifCategory == null) {
                showGifCategoryOverview();
            } else {
                showGifCategoryItems(currentGifCategory, currentGifCategory);
            }
        } else {
            gifHeaderLayout.setVisibility(View.GONE);
            recyclerGifCategories.setVisibility(View.GONE);
            recyclerGifs.setVisibility(View.GONE);
        }

        topBarContainer.setVisibility(View.VISIBLE);

        highlightTab(tvEmojiTab, tab == TAB_EMOJI);
        highlightTab(tvClipsTab, tab == TAB_CLIPS);
        highlightTab(tvGifTab, tab == TAB_GIF);
        highlightTab(tvKaomojiTab, tab == TAB_KAOMOJI);

        if (tab == TAB_EMOJI) {
            buildCategoryTabs(getContext());
            emojiAdapter.updateList(getEmojisForCategory(currentCategory));
        } else if (tab == TAB_CLIPS) {
            buildClipFilterChips(getContext());
            applyClipFilter();
        } else if (tab == TAB_KAOMOJI) {
            buildKaomojiFilterChips(getContext());
            applyKaomojiFilter();
        }
    }

    private void showGifCategoryOverview() {
        currentGifCategory = null;
        gifHeaderLayout.setVisibility(View.GONE);
        recyclerGifs.setVisibility(View.GONE);
        recyclerGifCategories.setVisibility(View.VISIBLE);
        recyclerGifCategories.scrollToPosition(0);
    }

    private void selectGifCategory(String categoryId, String categoryTitle) {
        currentGifCategory = categoryId;
        showGifCategoryItems(categoryId, categoryTitle);
    }

    private void showGifCategoryItems(String categoryId, String categoryTitle) {
        recyclerGifCategories.setVisibility(View.GONE);
        gifHeaderLayout.setVisibility(View.VISIBLE);
        tvGifActiveCategory.setText(categoryTitle);
        buildGifFilterChips(getContext());

        List<EmojiData.GifCard> all = EmojiData.getAllGifCards();
        List<EmojiData.GifCard> filtered = new ArrayList<>();
        for (EmojiData.GifCard g : all) {
            if (g.category.equalsIgnoreCase(categoryId)) {
                filtered.add(g);
            }
        }
        if (filtered.isEmpty()) {
            filtered.addAll(all);
        }
        gifAdapter.updateList(filtered);
        recyclerGifs.setVisibility(View.VISIBLE);
        recyclerGifs.scrollToPosition(0);
    }

    private void highlightTab(TextView tabView, boolean active) {
        if (active) {
            tabView.setBackgroundResource(R.drawable.bg_suggestion_chip);
            tabView.setTextColor(0xFFFFFFFF);
            tabView.setAlpha(1.0f);
        } else {
            tabView.setBackground(null);
            tabView.setTextColor(ContextCompatColor(getContext(), R.color.key_text_action));
            tabView.setAlpha(0.65f);
        }
    }

    private void applyClipFilter() {
        List<EmojiData.VideoClip> all = EmojiData.getAllVideoClips();
        List<EmojiData.VideoClip> filtered = new ArrayList<>();
        for (EmojiData.VideoClip c : all) {
            if ("All".equals(currentClipFilter) || c.category.equalsIgnoreCase(currentClipFilter)) {
                filtered.add(c);
            }
        }
        clipsAdapter.updateList(filtered);
        recyclerClips.scrollToPosition(0);
    }

    private void applyKaomojiFilter() {
        List<String> result = new ArrayList<>();
        if ("All".equals(currentKaomojiFilter)) {
            result = getAllKaomojis();
        } else {
            List<String> list = EmojiData.KAOMOJI_CATEGORIES.get(currentKaomojiFilter);
            if (list != null) result.addAll(list);
        }
        kaomojiAdapter.updateList(result);
        recyclerKaomoji.scrollToPosition(0);
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

    // ==========================================
    // ADAPTER 1: Emoji Grid Adapter (8 Columns)
    // ==========================================
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

    // ==========================================
    // ADAPTER 2: Video Clips Adapter (2 Columns)
    // ==========================================
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
                    dpToPx(74)
            );
            int m = dpToPx(4);
            lp.setMargins(m, m, m, m);
            card.setLayoutParams(lp);

            // Header row: Emoji + Duration
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

    // =========================================================================
    // ADAPTER 3: Visual GIF Categories Cards Adapter (2 Columns - Screenshot 3)
    // =========================================================================
    private class GifCategoryAdapter extends RecyclerView.Adapter<GifCategoryAdapter.CategoryViewHolder> {
        private final List<EmojiData.GifCategory> categories;

        public GifCategoryAdapter(List<EmojiData.GifCategory> categories) {
            this.categories = categories;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout card = new LinearLayout(parent.getContext());
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setGravity(Gravity.CENTER);
            card.setPadding(dpToPx(10), dpToPx(8), dpToPx(10), dpToPx(8));

            GridLayoutManager.LayoutParams lp = new GridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(66)
            );
            int m = dpToPx(4);
            lp.setMargins(m, m, m, m);
            card.setLayoutParams(lp);

            TextView tvTitle = new TextView(parent.getContext());
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setTextColor(0xFFFFFFFF);
            tvTitle.setShadowLayer(4f, 1f, 2f, 0x90000000);
            tvTitle.setGravity(Gravity.CENTER);
            card.addView(tvTitle);

            return new CategoryViewHolder(card, tvTitle);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            EmojiData.GifCategory cat = categories.get(position);
            holder.tvTitle.setText(cat.icon + "  " + cat.title);

            // Rich Gradient Background with Rounded Corners
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    cat.gradientColors
            );
            gd.setCornerRadius(dpToPx(12));
            holder.card.setBackground(gd);

            holder.itemView.setOnClickListener(v -> {
                performFeedback();
                selectGifCategory(cat.id, cat.title);
            });
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {
            LinearLayout card;
            TextView tvTitle;
            CategoryViewHolder(View itemView, TextView tvTitle) {
                super(itemView);
                this.card = (LinearLayout) itemView;
                this.tvTitle = tvTitle;
            }
        }
    }

    // ==========================================
    // ADAPTER 4: GIF Cards Adapter (2 Columns)
    // ==========================================
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
                    dpToPx(72)
            );
            int m = dpToPx(4);
            lp.setMargins(m, m, m, m);
            card.setLayoutParams(lp);

            TextView tvEmoji = new TextView(parent.getContext());
            tvEmoji.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);
            tvEmoji.setGravity(Gravity.CENTER);
            card.addView(tvEmoji);

            TextView tvTitle = new TextView(parent.getContext());
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setTextColor(ContextCompatColor(parent.getContext(), R.color.key_text_color));
            tvTitle.setGravity(Gravity.CENTER);
            tvTitle.setSingleLine(true);
            card.addView(tvTitle);

            TextView tvMsg = new TextView(parent.getContext());
            tvMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
            tvMsg.setTextColor(0xFF94A3B8);
            tvMsg.setGravity(Gravity.CENTER);
            tvMsg.setSingleLine(true);
            card.addView(tvMsg);

            return new GifViewHolder(card, tvEmoji, tvTitle, tvMsg);
        }

        @Override
        public void onBindViewHolder(@NonNull GifViewHolder holder, int position) {
            EmojiData.GifCard item = list.get(position);
            holder.tvEmoji.setText(item.emoji);
            holder.tvTitle.setText(item.title);
            holder.tvMsg.setText(item.message);
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
            TextView tvMsg;
            GifViewHolder(View itemView, TextView tvEmoji, TextView tvTitle, TextView tvMsg) {
                super(itemView);
                this.tvEmoji = tvEmoji;
                this.tvTitle = tvTitle;
                this.tvMsg = tvMsg;
            }
        }
    }

    // ==================================================
    // ADAPTER 5: Kaomoji / Text Emoticons (3 Columns)
    // ==================================================
    private class KaomojiAdapter extends RecyclerView.Adapter<KaomojiAdapter.KaomojiViewHolder> {
        private List<String> list;

        public KaomojiAdapter(List<String> list) {
            this.list = new ArrayList<>(list);
        }

        public void updateList(List<String> newList) {
            this.list = new ArrayList<>(newList);
            notifyDataSetChanged();
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
