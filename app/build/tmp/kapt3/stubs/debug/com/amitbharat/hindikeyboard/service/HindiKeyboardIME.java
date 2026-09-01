package com.amitbharat.hindikeyboard.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004B\u0005\u00a2\u0006\u0002\u0010\u0005J\b\u0010%\u001a\u00020&H\u0002J\u0010\u0010\'\u001a\u00020&2\u0006\u0010(\u001a\u00020)H\u0002J\b\u0010*\u001a\u00020&H\u0002J\b\u0010+\u001a\u00020&H\u0002J\b\u0010,\u001a\u00020&H\u0002J\b\u0010-\u001a\u00020&H\u0002J\u0010\u0010.\u001a\u00020&2\u0006\u0010/\u001a\u000200H\u0002J\u0010\u00101\u001a\u00020&2\u0006\u00102\u001a\u000200H\u0002J\b\u00103\u001a\u00020&H\u0002J\b\u00104\u001a\u00020&H\u0002J\b\u00105\u001a\u00020&H\u0016J\b\u00106\u001a\u000207H\u0016J\b\u00108\u001a\u00020&H\u0016J\b\u00109\u001a\u00020:H\u0016J\b\u0010;\u001a\u00020:H\u0016J\u0010\u0010<\u001a\u00020&2\u0006\u0010=\u001a\u00020:H\u0016J\u001a\u0010>\u001a\u00020&2\b\u0010?\u001a\u0004\u0018\u00010@2\u0006\u0010A\u001a\u00020:H\u0016J\b\u0010B\u001a\u00020&H\u0002R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u00060\tj\u0002`\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R+\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8B@BX\u0082\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0014\u001a\u00020\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\u00020\u001b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\"\u001a\u00020!8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b#\u0010$\u00a8\u0006C"}, d2 = {"Lcom/amitbharat/hindikeyboard/service/HindiKeyboardIME;", "Landroid/inputmethodservice/InputMethodService;", "Landroidx/lifecycle/LifecycleOwner;", "Landroidx/lifecycle/ViewModelStoreOwner;", "Landroidx/savedstate/SavedStateRegistryOwner;", "()V", "composeView", "Landroidx/compose/ui/platform/ComposeView;", "composingWord", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "<set-?>", "Lcom/amitbharat/hindikeyboard/keyboard/KeyboardUiState;", "keyboardState", "getKeyboardState", "()Lcom/amitbharat/hindikeyboard/keyboard/KeyboardUiState;", "setKeyboardState", "(Lcom/amitbharat/hindikeyboard/keyboard/KeyboardUiState;)V", "keyboardState$delegate", "Landroidx/compose/runtime/MutableState;", "lifecycle", "Landroidx/lifecycle/Lifecycle;", "getLifecycle", "()Landroidx/lifecycle/Lifecycle;", "lifecycleRegistry", "Landroidx/lifecycle/LifecycleRegistry;", "savedStateRegistry", "Landroidx/savedstate/SavedStateRegistry;", "getSavedStateRegistry", "()Landroidx/savedstate/SavedStateRegistry;", "savedStateRegistryController", "Landroidx/savedstate/SavedStateRegistryController;", "store", "Landroidx/lifecycle/ViewModelStore;", "viewModelStore", "getViewModelStore", "()Landroidx/lifecycle/ViewModelStore;", "handleBackspace", "", "handleCursorMove", "offset", "", "handleEnter", "handleLanguageToggle", "handleShiftToggle", "handleSpace", "handleSuggestionClick", "selectedText", "", "handleText", "text", "launchSettings", "launchVoiceTyping", "onCreate", "onCreateInputView", "Landroid/view/View;", "onDestroy", "onEvaluateFullscreenMode", "", "onEvaluateInputViewShown", "onFinishInputView", "finishingInput", "onStartInputView", "info", "Landroid/view/inputmethod/EditorInfo;", "restarting", "updateSuggestions", "app_debug"})
public final class HindiKeyboardIME extends android.inputmethodservice.InputMethodService implements androidx.lifecycle.LifecycleOwner, androidx.lifecycle.ViewModelStoreOwner, androidx.savedstate.SavedStateRegistryOwner {
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LifecycleRegistry lifecycleRegistry = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.ViewModelStore store = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.savedstate.SavedStateRegistryController savedStateRegistryController = null;
    @org.jetbrains.annotations.Nullable()
    private androidx.compose.ui.platform.ComposeView composeView;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState keyboardState$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.StringBuilder composingWord;
    
    public HindiKeyboardIME() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.lifecycle.Lifecycle getLifecycle() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.lifecycle.ViewModelStore getViewModelStore() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.savedstate.SavedStateRegistry getSavedStateRegistry() {
        return null;
    }
    
    private final com.amitbharat.hindikeyboard.keyboard.KeyboardUiState getKeyboardState() {
        return null;
    }
    
    private final void setKeyboardState(com.amitbharat.hindikeyboard.keyboard.KeyboardUiState p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public boolean onEvaluateFullscreenMode() {
        return false;
    }
    
    @java.lang.Override()
    public boolean onEvaluateInputViewShown() {
        return false;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateInputView() {
        return null;
    }
    
    @java.lang.Override()
    public void onStartInputView(@org.jetbrains.annotations.Nullable()
    android.view.inputmethod.EditorInfo info, boolean restarting) {
    }
    
    @java.lang.Override()
    public void onFinishInputView(boolean finishingInput) {
    }
    
    private final void handleText(java.lang.String text) {
    }
    
    private final void handleBackspace() {
    }
    
    private final void handleSpace() {
    }
    
    private final void handleEnter() {
    }
    
    private final void handleLanguageToggle() {
    }
    
    private final void handleShiftToggle() {
    }
    
    private final void handleSuggestionClick(java.lang.String selectedText) {
    }
    
    private final void handleCursorMove(int offset) {
    }
    
    private final void updateSuggestions() {
    }
    
    private final void launchVoiceTyping() {
    }
    
    private final void launchSettings() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
}