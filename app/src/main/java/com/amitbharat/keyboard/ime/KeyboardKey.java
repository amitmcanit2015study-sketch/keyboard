package com.amitbharat.keyboard.ime;

import android.graphics.RectF;

public class KeyboardKey {

    public static final int CODE_SHIFT = -1;
    public static final int CODE_BACKSPACE = -2;
    public static final int CODE_ENTER = -3;
    public static final int CODE_MODE_NUM = -4;
    public static final int CODE_MODE_ALPHA = -5;
    public static final int CODE_MODE_SYM = -6;
    public static final int CODE_EMOJI = -7;
    public static final int CODE_SPACE = 32;
    public static final int CODE_LANG_SWITCH = -8;

    public int code;
    public String label;
    public String hint;
    public int iconResId;
    public float weight; // Relative width (default 1.0f)
    public boolean isAction;
    public boolean isAccent;

    // Computed bounding box during onLayout
    public final RectF bounds = new RectF();
    public boolean isPressed = false;

    public KeyboardKey(int code, String label, String hint, float weight) {
        this.code = code;
        this.label = label;
        this.hint = hint;
        this.weight = weight;
        this.isAction = false;
        this.isAccent = false;
        this.iconResId = 0;
    }

    public KeyboardKey(int code, String label, int iconResId, float weight, boolean isAction, boolean isAccent) {
        this.code = code;
        this.label = label;
        this.hint = null;
        this.weight = weight;
        this.iconResId = iconResId;
        this.isAction = isAction;
        this.isAccent = isAccent;
    }
}
