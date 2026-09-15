package com.amitbharat.keyboard.ime;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.amitbharat.keyboard.R;
import com.amitbharat.keyboard.engine.KeyboardPreferences;
import java.util.List;

public class CandidateStripView extends FrameLayout {

    public interface OnCandidateClickListener {
        void onCandidateClicked(String candidate);
    }

    private View btnLanguageToggle;
    private TextView tvActiveLang;
    private TextView tvAltLang;
    private LinearLayout layoutSuggestionsContainer;
    private ImageButton btnQuickSettings;

    private OnCandidateClickListener candidateListener;
    private Runnable languageToggleListener;
    private Runnable settingsClickListener;

    public CandidateStripView(Context context) {
        super(context);
        init(context);
    }

    public CandidateStripView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CandidateStripView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_candidate_strip, this, true);
        btnLanguageToggle = findViewById(R.id.btnLanguageToggle);
        tvActiveLang = findViewById(R.id.tvActiveLang);
        tvAltLang = findViewById(R.id.tvAltLang);
        layoutSuggestionsContainer = findViewById(R.id.layoutSuggestionsContainer);
        btnQuickSettings = findViewById(R.id.btnQuickSettings);

        btnLanguageToggle.setOnClickListener(v -> {
            if (languageToggleListener != null) {
                languageToggleListener.run();
            }
        });

        btnQuickSettings.setOnClickListener(v -> {
            if (settingsClickListener != null) {
                settingsClickListener.run();
            }
        });
    }

    public void setLanguages(String activeLang, String altLang) {
        if (tvActiveLang != null) {
            tvActiveLang.setText(activeLang != null ? activeLang.toUpperCase(java.util.Locale.ROOT) : "");
        }
        TextView tvDivider = findViewById(R.id.tvLangDivider);
        if (altLang != null && !altLang.isEmpty()) {
            if (tvAltLang != null) {
                tvAltLang.setText(altLang.toUpperCase(java.util.Locale.ROOT));
                tvAltLang.setVisibility(View.VISIBLE);
            }
            if (tvDivider != null) {
                tvDivider.setVisibility(View.VISIBLE);
            }
        } else {
            if (tvAltLang != null) {
                tvAltLang.setVisibility(View.GONE);
            }
            if (tvDivider != null) {
                tvDivider.setVisibility(View.GONE);
            }
        }
    }

    public void setLanguage(String lang) {
        setLanguages(lang, null);
    }

    public void setOnLanguageToggleListener(Runnable listener) {
        this.languageToggleListener = listener;
    }

    public void setOnCandidateClickListener(OnCandidateClickListener listener) {
        this.candidateListener = listener;
    }

    public void setOnSettingsClickListener(Runnable listener) {
        this.settingsClickListener = listener;
    }

    private int currentAccentColor = 0xFF283C5A;
    private int currentAccentTextColor = 0xFFD6E4FF;

    public void updateTheme(String theme) {
        Context ctx = getContext();
        TypedValue tv = new TypedValue();

        int accentBg;
        if (ctx.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, tv, true)) {
            accentBg = tv.data;
        } else if (ctx.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, tv, true)) {
            accentBg = tv.data;
        } else {
            accentBg = ContextCompat.getColor(ctx, R.color.md_theme_primaryContainer);
        }

        int accentText;
        if (ctx.getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnPrimaryContainer, tv, true)) {
            accentText = tv.data;
        } else if (ctx.getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, tv, true)) {
            accentText = tv.data;
        } else {
            accentText = ContextCompat.getColor(ctx, R.color.md_theme_onPrimaryContainer);
        }

        currentAccentColor = accentBg;
        
        // Guarantee high-contrast text and icons on the language pill
        double pillLum = androidx.core.graphics.ColorUtils.calculateLuminance(accentBg);
        currentAccentTextColor = (pillLum < 0.55) ? 0xFFFFFFFF : 0xFF111827;

        android.graphics.drawable.GradientDrawable pillBg = new android.graphics.drawable.GradientDrawable();
        pillBg.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        pillBg.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, ctx.getResources().getDisplayMetrics()));
        pillBg.setColor(accentBg);
        btnLanguageToggle.setBackground(pillBg);

        tvActiveLang.setTextColor(currentAccentTextColor);
        tvAltLang.setTextColor(currentAccentTextColor);
        tvAltLang.setAlpha(0.85f);
        android.widget.ImageView ivGlobe = findViewById(R.id.ivGlobeIcon);
        if (ivGlobe != null) {
            ivGlobe.setColorFilter(currentAccentTextColor);
        }
        TextView tvDivider = findViewById(R.id.tvLangDivider);
        if (tvDivider != null) {
            tvDivider.setTextColor(currentAccentTextColor);
            tvDivider.setAlpha(0.75f);
        }

        android.widget.ImageButton ibSettings = findViewById(R.id.ibSettings);
        if (ibSettings != null) {
            boolean isNight = (ctx.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
            ibSettings.setColorFilter(isNight ? 0xFFCBD5E1 : 0xFF4B5563);
        }
    }

    public void setSuggestions(List<String> suggestions) {
        layoutSuggestionsContainer.removeAllViews();
        if (suggestions == null || suggestions.isEmpty()) {
            return;
        }

        Context ctx = getContext();
        boolean isNight = (ctx.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        int primaryColor = isNight ? 0xFFFFFFFF : 0xFF111827;
        int secondaryColor = isNight ? 0xFFD1D5DB : 0xFF4B5563;

        for (int i = 0; i < suggestions.size(); i++) {
            final String word = suggestions.get(i);
            TextView chip = new TextView(ctx);
            chip.setText(word);
            chip.setGravity(Gravity.CENTER);
            chip.setSingleLine(true);
            chip.setBackgroundResource(R.drawable.bg_suggestion_chip);
            chip.setClickable(true);
            chip.setFocusable(true);

            // Highlight primary suggestion with pure, crisp contrast
            if (i == 0) {
                chip.setTextColor(primaryColor);
                chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                chip.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                chip.setTextColor(secondaryColor);
                chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            }

            int padH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, ctx.getResources().getDisplayMetrics());
            int padV = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, ctx.getResources().getDisplayMetrics());
            chip.setPadding(padH, padV, padH, padV);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, ctx.getResources().getDisplayMetrics())
            );
            int marginH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, ctx.getResources().getDisplayMetrics());
            lp.setMargins(marginH, 0, marginH, 0);
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                android.util.Log.d("IndicKeyboard", "Candidate chip clicked: " + word);
                if (candidateListener != null) {
                    candidateListener.onCandidateClicked(word);
                }
            });

            layoutSuggestionsContainer.addView(chip);
        }
    }
}
