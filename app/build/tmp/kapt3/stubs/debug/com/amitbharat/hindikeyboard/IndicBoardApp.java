package com.amitbharat.hindikeyboard;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016\u00a8\u0006\u0006"}, d2 = {"Lcom/amitbharat/hindikeyboard/IndicBoardApp;", "Landroid/app/Application;", "()V", "onCreate", "", "Companion", "app_debug"})
public final class IndicBoardApp extends android.app.Application {
    private static com.amitbharat.hindikeyboard.IndicBoardApp instance;
    private static com.amitbharat.hindikeyboard.database.KeyboardDatabase database;
    @org.jetbrains.annotations.NotNull()
    public static final com.amitbharat.hindikeyboard.IndicBoardApp.Companion Companion = null;
    
    public IndicBoardApp() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/amitbharat/hindikeyboard/IndicBoardApp$Companion;", "", "()V", "<set-?>", "Lcom/amitbharat/hindikeyboard/database/KeyboardDatabase;", "database", "getDatabase", "()Lcom/amitbharat/hindikeyboard/database/KeyboardDatabase;", "Lcom/amitbharat/hindikeyboard/IndicBoardApp;", "instance", "getInstance", "()Lcom/amitbharat/hindikeyboard/IndicBoardApp;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.amitbharat.hindikeyboard.IndicBoardApp getInstance() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.amitbharat.hindikeyboard.database.KeyboardDatabase getDatabase() {
            return null;
        }
    }
}