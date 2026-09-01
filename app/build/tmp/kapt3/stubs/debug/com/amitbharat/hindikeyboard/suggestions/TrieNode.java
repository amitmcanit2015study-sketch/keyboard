package com.amitbharat.hindikeyboard.suggestions;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001d\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00000\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018\u00a8\u0006\u0019"}, d2 = {"Lcom/amitbharat/hindikeyboard/suggestions/TrieNode;", "", "()V", "children", "", "", "getChildren", "()Ljava/util/Map;", "frequency", "", "getFrequency", "()I", "setFrequency", "(I)V", "isEndOfWord", "", "()Z", "setEndOfWord", "(Z)V", "word", "", "getWord", "()Ljava/lang/String;", "setWord", "(Ljava/lang/String;)V", "app_debug"})
public final class TrieNode {
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.Character, com.amitbharat.hindikeyboard.suggestions.TrieNode> children = null;
    private boolean isEndOfWord = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String word;
    private int frequency = 0;
    
    public TrieNode() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Character, com.amitbharat.hindikeyboard.suggestions.TrieNode> getChildren() {
        return null;
    }
    
    public final boolean isEndOfWord() {
        return false;
    }
    
    public final void setEndOfWord(boolean p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getWord() {
        return null;
    }
    
    public final void setWord(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final int getFrequency() {
        return 0;
    }
    
    public final void setFrequency(int p0) {
    }
}