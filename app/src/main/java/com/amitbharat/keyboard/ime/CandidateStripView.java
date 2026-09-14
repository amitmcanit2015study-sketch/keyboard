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

    public void setLanguage(String lang) {
        if (KeyboardPreferences.LANG_HINDI.equals(lang)) {
            tvActiveLang.setText("HN");
            tvAltLang.setText("EN");
        } else {
            tvActiveLang.setText("EN");
            tvAltLang.setText("HN");
        }
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

    public void setSuggestions(List<String> suggestions) {
        layoutSuggestionsContainer.removeAllViews();
        if (suggestions == null || suggestions.isEmpty()) {
            return;
        }

        Context ctx = getContext();
        for (int i = 0; i < suggestions.size(); i++) {
            final String word = suggestions.get(i);
            TextView chip = new TextView(ctx);
            chip.setText(word);
            chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
            chip.setGravity(Gravity.CENTER);
            chip.setSingleLine(true);
            chip.setBackgroundResource(R.drawable.bg_suggestion_chip);
            chip.setClickable(true);
            chip.setFocusable(true);

            // Highlight the primary/first suggestion
            if (i == 0) {
                chip.setTextColor(ContextCompat.getColor(ctx, R.color.suggestion_chip_active_text));
                chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                chip.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                chip.setTextColor(ContextCompat.getColor(ctx, R.color.suggestion_chip_text));
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
