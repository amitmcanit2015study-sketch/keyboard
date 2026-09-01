package com.amitbharat.hindikeyboard.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J$\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\b0\u000e2\u0006\u0010\u000b\u001a\u00020\nH\'J(\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0014"}, d2 = {"Lcom/amitbharat/hindikeyboard/database/UserWordDao;", "", "delete", "", "word", "Lcom/amitbharat/hindikeyboard/database/UserWordEntity;", "(Lcom/amitbharat/hindikeyboard/database/UserWordEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMatchingWords", "", "prefix", "", "lang", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTopWords", "Lkotlinx/coroutines/flow/Flow;", "incrementFrequency", "timestamp", "", "(Ljava/lang/String;Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertOrUpdate", "app_debug"})
@androidx.room.Dao()
public abstract interface UserWordDao {
    
    @androidx.room.Query(value = "SELECT * FROM user_words WHERE language = :lang ORDER BY frequency DESC, lastUsed DESC LIMIT 100")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.amitbharat.hindikeyboard.database.UserWordEntity>> getTopWords(@org.jetbrains.annotations.NotNull()
    java.lang.String lang);
    
    @androidx.room.Query(value = "SELECT * FROM user_words WHERE word LIKE :prefix || \'%\' AND language = :lang ORDER BY frequency DESC LIMIT 10")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMatchingWords(@org.jetbrains.annotations.NotNull()
    java.lang.String prefix, @org.jetbrains.annotations.NotNull()
    java.lang.String lang, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.amitbharat.hindikeyboard.database.UserWordEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertOrUpdate(@org.jetbrains.annotations.NotNull()
    com.amitbharat.hindikeyboard.database.UserWordEntity word, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE user_words SET frequency = frequency + 1, lastUsed = :timestamp WHERE word = :word AND language = :lang")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object incrementFrequency(@org.jetbrains.annotations.NotNull()
    java.lang.String word, @org.jetbrains.annotations.NotNull()
    java.lang.String lang, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.amitbharat.hindikeyboard.database.UserWordEntity word, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}