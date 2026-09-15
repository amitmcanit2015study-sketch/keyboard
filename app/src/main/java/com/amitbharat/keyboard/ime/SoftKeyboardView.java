package com.amitbharat.keyboard.ime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.amitbharat.keyboard.R;
import com.amitbharat.keyboard.engine.KeyboardPreferences;
import java.util.ArrayList;
import java.util.List;

public class SoftKeyboardView extends View {

    public interface OnKeyboardActionListener {
        void onKey(int primaryCode, KeyboardKey key);
        void onText(CharSequence text);
    }

    private OnKeyboardActionListener actionListener;
    private KeyboardPreferences preferences;
    private AudioManager audioManager;

    private int currentMode = KeyboardLayoutHelper.MODE_ALPHA;
    private boolean isShifted = false;
    private boolean isCapsLock = false;
    private long lastShiftTime = 0;

    private List<List<KeyboardKey>> keyRows = new ArrayList<>();

    // Drawing paints
    private Paint paintKeyText;
    private Paint paintKeyHint;
    private Paint paintKeyBg;
    private Paint paintKeyBgAction;
    private Paint paintKeyBgAccent;
    private Paint paintKeyBorder;

    private int colorKeyBgNormal;
    private int colorKeyBgAction;
    private int colorKeyBgAccent;
    private int colorKeyTextAccent;
    private int colorKeyText;
    private int colorKeyHint;
    private int colorKeyBorder;

    private KeyboardKey activeKey = null;
    private final Handler repeatHandler = new Handler(Looper.getMainLooper());
    private boolean isRepeating = false;

    // Key popup preview
    private PopupWindow popupWindow;
    private TextView popupTextView;

    public SoftKeyboardView(Context context) {
        super(context);
        init(context);
    }

    public SoftKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SoftKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        preferences = new KeyboardPreferences(context);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // Load colors
        resolveThemeColors();

        paintKeyText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintKeyText.setTextAlign(Paint.Align.CENTER);
        paintKeyText.setColor(colorKeyText);

        paintKeyHint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintKeyHint.setTextAlign(Paint.Align.RIGHT);
        paintKeyHint.setColor(colorKeyHint);

        paintKeyBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintKeyBg.setStyle(Paint.Style.FILL);
        paintKeyBg.setColor(colorKeyBgNormal);

        paintKeyBgAction = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintKeyBgAction.setStyle(Paint.Style.FILL);
        paintKeyBgAction.setColor(colorKeyBgAction);

        paintKeyBgAccent = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintKeyBgAccent.setStyle(Paint.Style.FILL);
        paintKeyBgAccent.setColor(colorKeyBgAccent);

        paintKeyBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintKeyBorder.setStyle(Paint.Style.STROKE);
        paintKeyBorder.setStrokeWidth(1.5f);
        paintKeyBorder.setColor(colorKeyBorder);

        // Init popup preview
        popupTextView = new TextView(context);
        popupTextView.setBackgroundResource(R.drawable.bg_key_popup);
        popupTextView.setTextColor(colorKeyText);
        popupTextView.setTextSize(26);
        popupTextView.setGravity(android.view.Gravity.CENTER);
        popupWindow = new PopupWindow(popupTextView, dpToPx(56), dpToPx(64));
        popupWindow.setTouchable(false);

        buildLayout();
    }

    public void resolveThemeColors() {
        Context ctx = getContext();
        String theme = preferences.getTheme();

        colorKeyBgNormal = ContextCompat.getColor(ctx, R.color.key_bg_normal);
        colorKeyBgAction = ContextCompat.getColor(ctx, R.color.key_bg_action);
        colorKeyText = ContextCompat.getColor(ctx, R.color.key_text_color);
        colorKeyHint = ContextCompat.getColor(ctx, R.color.key_hint_color);
        colorKeyBorder = ContextCompat.getColor(ctx, R.color.key_border);

        // Resolve accent colors dynamically to match host device
        android.util.TypedValue tv = new android.util.TypedValue();
        if (ctx.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, tv, true)) {
            colorKeyBgAccent = tv.data;
        } else if (ctx.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, tv, true)) {
            colorKeyBgAccent = tv.data;
        } else {
            colorKeyBgAccent = ContextCompat.getColor(ctx, R.color.md_theme_primaryContainer);
        }

        // Ensure accent key text/icon is 100% visible and keyboard-friendly
        double bgLum = androidx.core.graphics.ColorUtils.calculateLuminance(colorKeyBgAccent);
        colorKeyTextAccent = (bgLum < 0.55) ? 0xFFFFFFFF : 0xFF111827;

        if (paintKeyText != null) {
            paintKeyText.setColor(colorKeyText);
            paintKeyHint.setColor(colorKeyHint);
            paintKeyBg.setColor(colorKeyBgNormal);
            paintKeyBgAction.setColor(colorKeyBgAction);
            paintKeyBgAccent.setColor(colorKeyBgAccent);
            paintKeyBorder.setColor(colorKeyBorder);
            if (popupTextView != null) popupTextView.setTextColor(colorKeyText);
            invalidate();
        }
    }

    public void setOnKeyboardActionListener(OnKeyboardActionListener listener) {
        this.actionListener = listener;
    }

    public void setMode(int mode) {
        this.currentMode = mode;
        buildLayout();
        requestLayout();
        invalidate();
    }

    public int getMode() {
        return currentMode;
    }

    public void setShifted(boolean shifted) {
        this.isShifted = shifted;
        if (!shifted) isCapsLock = false;
        if (currentMode == KeyboardLayoutHelper.MODE_ALPHA) {
            buildLayout();
            invalidate();
        }
    }

    public boolean isShifted() {
        return isShifted || isCapsLock;
    }

    private void buildLayout() {
        if (currentMode == KeyboardLayoutHelper.MODE_NUMPAD) {
            keyRows = KeyboardLayoutHelper.createNumpadLayout();
        } else if (currentMode == KeyboardLayoutHelper.MODE_NUMERIC) {
            keyRows = KeyboardLayoutHelper.createNumericLayout();
        } else if (currentMode == KeyboardLayoutHelper.MODE_SYMBOLS) {
            keyRows = KeyboardLayoutHelper.createSymbolsLayout();
        } else {
            keyRows = KeyboardLayoutHelper.createQwertyLayout(isShifted, isCapsLock, preferences);
        }
        computeKeyPositions();
    }

    public void rebuildLayout() {
        buildLayout();
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // Height of the soft keyboard keys ~ 280dp (increased for larger, more comfortable keys)
        int desiredHeight = dpToPx(280);
        setMeasuredDimension(width, desiredHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        computeKeyPositions();
    }

    private void computeKeyPositions() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0 || keyRows.isEmpty()) return;

        int rowCount = keyRows.size();
        float keyGapHorizontal = dpToPx(4);
        float keyGapVertical = dpToPx(6);
        float paddingHorizontal = dpToPx(4);
        float paddingTop = dpToPx(6);
        float paddingBottom = dpToPx(6);

        float availableHeight = height - paddingTop - paddingBottom - (keyGapVertical * (rowCount - 1));
        float rowHeight = availableHeight / rowCount;

        for (int r = 0; r < rowCount; r++) {
            List<KeyboardKey> row = keyRows.get(r);
            float totalWeight = 0;
            for (KeyboardKey key : row) {
                totalWeight += key.weight;
            }

            float availableWidth = width - (paddingHorizontal * 2) - (keyGapHorizontal * (row.size() - 1));
            float unitWidth = availableWidth / totalWeight;

            float currentLeft = paddingHorizontal;
            float top = paddingTop + r * (rowHeight + keyGapVertical);
            float bottom = top + rowHeight;

            for (KeyboardKey key : row) {
                float keyWidth = unitWidth * key.weight;
                key.bounds.set(currentLeft, top, currentLeft + keyWidth, bottom);
                currentLeft += keyWidth + keyGapHorizontal;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cornerRadius = dpToPx(10);

        paintKeyText.setTextSize(dpToPx(22));
        paintKeyHint.setTextSize(dpToPx(11));

        for (List<KeyboardKey> row : keyRows) {
            for (KeyboardKey key : row) {
                RectF b = key.bounds;

                // Pick background paint
                Paint bgPaint = paintKeyBg;
                if (key.isAccent) {
                    bgPaint = paintKeyBgAccent;
                } else if (key.isAction) {
                    bgPaint = paintKeyBgAction;
                }

                // Pressed effect
                if (key.isPressed) {
                    canvas.drawRoundRect(b, cornerRadius, cornerRadius, paintKeyBgAction);
                } else {
                    canvas.drawRoundRect(b, cornerRadius, cornerRadius, bgPaint);
                }

                // Border
                canvas.drawRoundRect(b, cornerRadius, cornerRadius, paintKeyBorder);

                // Draw icon if present
                if (key.iconResId != 0) {
                    Drawable icon = ContextCompat.getDrawable(getContext(), key.iconResId);
                    if (icon != null) {
                        int iconColor = key.isAccent ? colorKeyTextAccent : colorKeyText;
                        icon.setTint(iconColor);
                        int iconSize = dpToPx(22);
                        int cx = (int) b.centerX();
                        int cy = (int) b.centerY();
                        icon.setBounds(cx - iconSize / 2, cy - iconSize / 2, cx + iconSize / 2, cy + iconSize / 2);
                        icon.draw(canvas);
                    }
                } else {
                    // Draw label text
                    int textColor = key.isAccent ? colorKeyTextAccent : colorKeyText;
                    paintKeyText.setColor(textColor);
                    if (key.code == KeyboardKey.CODE_SPACE) {
                        paintKeyText.setTextSize(dpToPx(14));
                    } else if (key.label != null && key.label.length() > 1) {
                        paintKeyText.setTextSize(dpToPx(15));
                    } else {
                        paintKeyText.setTextSize(dpToPx(22));
                    }

                    Paint.FontMetrics fm = paintKeyText.getFontMetrics();
                    float baseline = b.centerY() - (fm.ascent + fm.descent) / 2;
                    canvas.drawText(key.label, b.centerX(), baseline, paintKeyText);
                }

                // Draw secondary hint if present
                if (key.hint != null) {
                    canvas.drawText(key.hint, b.right - dpToPx(6), b.top + dpToPx(13), paintKeyHint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                KeyboardKey key = findKey(x, y);
                if (key != null) {
                    activeKey = key;
                    key.isPressed = true;
                    handleFeedback();
                    showPopupPreview(key);
                    invalidate();

                    if (key.code == KeyboardKey.CODE_BACKSPACE) {
                        startBackspaceRepeat();
                    }
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (activeKey != null) {
                    float slop = dpToPx(10);
                    if (x >= activeKey.bounds.left - slop && x <= activeKey.bounds.right + slop &&
                        y >= activeKey.bounds.top - slop && y <= activeKey.bounds.bottom + slop) {
                        return true;
                    }
                }
                KeyboardKey currentKey = findKey(x, y);
                if (currentKey != activeKey) {
                    if (activeKey != null) {
                        activeKey.isPressed = false;
                    }
                    activeKey = currentKey;
                    if (activeKey != null) {
                        activeKey.isPressed = true;
                        showPopupPreview(activeKey);
                    } else {
                        dismissPopupPreview();
                    }
                    invalidate();
                }
                return true;

            case MotionEvent.ACTION_UP:
                stopBackspaceRepeat();
                dismissPopupPreview();
                if (activeKey != null) {
                    activeKey.isPressed = false;
                    onKeyReleased(activeKey);
                    activeKey = null;
                    invalidate();
                }
                return true;

            case MotionEvent.ACTION_CANCEL:
                stopBackspaceRepeat();
                dismissPopupPreview();
                if (activeKey != null) {
                    activeKey.isPressed = false;
                    activeKey = null;
                    invalidate();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void onKeyReleased(KeyboardKey key) {
        if (key.code == KeyboardKey.CODE_SHIFT) {
            long now = System.currentTimeMillis();
            if (now - lastShiftTime < 300) {
                // Double tap shift = caps lock
                isCapsLock = !isCapsLock;
                isShifted = isCapsLock;
            } else {
                isShifted = !isShifted;
                if (!isShifted) isCapsLock = false;
            }
            lastShiftTime = now;
            buildLayout();
            invalidate();
            return;
        }

        if (key.code == KeyboardKey.CODE_MODE_NUM) {
            setMode(KeyboardLayoutHelper.MODE_NUMERIC);
            return;
        }

        if (key.code == KeyboardKey.CODE_MODE_ALPHA) {
            setMode(KeyboardLayoutHelper.MODE_ALPHA);
            return;
        }

        if (key.code == KeyboardKey.CODE_MODE_SYM) {
            setMode(KeyboardLayoutHelper.MODE_SYMBOLS);
            return;
        }

        if (actionListener != null) {
            actionListener.onKey(key.code, key);
        }

        // If not caps locked, auto revert shift after typing a character
        if (isShifted && !isCapsLock && key.code > 0) {
            isShifted = false;
            buildLayout();
            invalidate();
        }
    }

    private void handleFeedback() {
        if (preferences.isSoundEnabled() && audioManager != null) {
            audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, 0.6f);
        }
        if (preferences.isVibrateEnabled()) {
            performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
        }
    }

    private void showPopupPreview(KeyboardKey key) {
        if (!preferences.isPopupEnabled()) return;
        if (key.code <= 0 && key.label != null && key.label.length() > 1) return; // don't show preview for special action keys

        popupTextView.setText(key.label);

        int[] location = new int[2];
        getLocationInWindow(location);

        int posX = (int) (location[0] + key.bounds.centerX() - popupWindow.getWidth() / 2);
        int posY = (int) (location[1] + key.bounds.top - popupWindow.getHeight() - dpToPx(8));

        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(this, 0, posX, posY);
        } else {
            popupWindow.update(posX, posY, popupWindow.getWidth(), popupWindow.getHeight());
        }
    }

    private void dismissPopupPreview() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private void startBackspaceRepeat() {
        isRepeating = true;
        repeatHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRepeating && activeKey != null && activeKey.code == KeyboardKey.CODE_BACKSPACE) {
                    if (actionListener != null) {
                        actionListener.onKey(KeyboardKey.CODE_BACKSPACE, activeKey);
                    }
                    handleFeedback();
                    repeatHandler.postDelayed(this, 50); // repeat every 50ms
                }
            }
        }, 400); // 400ms initial delay
    }

    private void stopBackspaceRepeat() {
        isRepeating = false;
        repeatHandler.removeCallbacksAndMessages(null);
    }

    private KeyboardKey findKey(float x, float y) {
        for (List<KeyboardKey> row : keyRows) {
            for (KeyboardKey key : row) {
                if (key.bounds.contains(x, y)) {
                    return key;
                }
            }
        }
        // If finger touched within the gap between keys, snap to closest key within 14dp
        float closestDist = Float.MAX_VALUE;
        KeyboardKey closestKey = null;
        float maxTolerance = dpToPx(14);
        for (List<KeyboardKey> row : keyRows) {
            for (KeyboardKey key : row) {
                float dx = Math.max(0, Math.max(key.bounds.left - x, x - key.bounds.right));
                float dy = Math.max(0, Math.max(key.bounds.top - y, y - key.bounds.bottom));
                float dist = (float) Math.hypot(dx, dy);
                if (dist < closestDist && dist <= maxTolerance) {
                    closestDist = dist;
                    closestKey = key;
                }
            }
        }
        return closestKey;
    }

    private int dpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
}
