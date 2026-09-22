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
    private ImageButton btnVoiceInput;
    private ImageButton btnQuickSettings;

    private OnCandidateClickListener candidateListener;
    private Runnable languageToggleListener;
    private Runnable settingsClickListener;
    private Runnable voiceClickListener;
    private boolean isVoiceListening = false;

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
        btnVoiceInput = findViewById(R.id.btnVoiceInput);
        btnQuickSettings = findViewById(R.id.btnQuickSettings);

        btnLanguageToggle.setOnClickListener(v -> {
            if (languageToggleListener != null) {
                languageToggleListener.run();
            }
        });

        if (btnVoiceInput != null) {
            btnVoiceInput.setOnClickListener(v -> {
                if (voiceClickListener != null) {
                    voiceClickListener.run();
                }
            });
        }

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

    public void setOnVoiceClickListener(Runnable listener) {
        this.voiceClickListener = listener;
    }

    public void setVoiceListening(boolean listening) {
        this.isVoiceListening = listening;
        if (btnVoiceInput != null) {
            if (listening) {
                btnVoiceInput.setColorFilter(0xFFE53935);
            } else {
                Context ctx = getContext();
                boolean isNight = (ctx.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
                btnVoiceInput.setColorFilter(isNight ? 0xFFCBD5E1 : 0xFF4B5563);
            }
        }
    }

    public void showVoiceStatus(String statusText) {
        layoutSuggestionsContainer.removeAllViews();
        if (statusText == null || statusText.isEmpty()) return;
        Context ctx = getContext();
        TextView statusView = new TextView(ctx);
        statusView.setText(statusText);
        statusView.setGravity(Gravity.CENTER_VERTICAL);
        statusView.setSingleLine(true);
        boolean isNight = (ctx.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        statusView.setTextColor(isNight ? 0xFF93C5FD : 0xFF1D4ED8);
        statusView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.5f);
        int padH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, ctx.getResources().getDisplayMetrics());
        statusView.setPadding(padH, 0, padH, 0);
        layoutSuggestionsContainer.addView(statusView);
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

        boolean isNight = (ctx.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        if (btnVoiceInput != null) {
            btnVoiceInput.setColorFilter(isVoiceListening ? 0xFFE53935 : (isNight ? 0xFFCBD5E1 : 0xFF4B5563));
        }

        if (btnQuickSettings != null) {
            btnQuickSettings.setColorFilter(isNight ? 0xFFCBD5E1 : 0xFF4B5563);
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
        boolean useWeight = suggestions.size() <= 3;

        for (int i = 0; i < suggestions.size(); i++) {
            final String word = suggestions.get(i);
            TextView chip = new TextView(ctx);
            chip.setText(word);
            chip.setGravity(Gravity.CENTER);
            chip.setSingleLine(true);
            chip.setClickable(true);
            chip.setFocusable(true);

            // Highlight primary suggestion with crisp contrast and sleek pill
            if (i == 0) {
                chip.setTextColor(primaryColor);
                chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.5f);
                chip.setTypeface(null, android.graphics.Typeface.BOLD);
                android.graphics.drawable.GradientDrawable primBg = new android.graphics.drawable.GradientDrawable();
                primBg.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                primBg.setCornerRadius(dpToPx(16));
                primBg.setColor(isNight ? 0xFF2A3441 : 0xFFE2E8F0);
                primBg.setStroke(dpToPx(1), 0xFF3B82F6);
                chip.setBackground(primBg);
            } else {
                chip.setTextColor(secondaryColor);
                chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.5f);
                chip.setBackgroundResource(R.drawable.bg_suggestion_chip);
            }

            int padH = dpToPx(10);
            int padV = dpToPx(4);
            chip.setPadding(padH, padV, padH, padV);

            LinearLayout.LayoutParams lp;
            if (useWeight) {
                lp = new LinearLayout.LayoutParams(0, dpToPx(32), 1.0f);
            } else {
                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, dpToPx(32));
            }
            int marginH = dpToPx(3);
            lp.setMargins(marginH, 0, marginH, 0);
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                if (candidateListener != null) {
                    candidateListener.onCandidateClicked(word);
                }
            });

            layoutSuggestionsContainer.addView(chip);
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
