# Bharat Indic Keyboard (भारत कीबोर्ड) 🇮🇳

[![Android](https://img.shields.io/badge/Platform-Android%207.0%2B%20(API%2024%2B)-green.svg)](https://developer.android.com)
[![Privacy First](https://img.shields.io/badge/Privacy-100%25%20Offline%20(Zero%20Internet)-blue.svg)](file:///d:/_test/keyboard/README.md)
[![Google Play Ready](https://img.shields.io/badge/Google%20Play-Security%20Audited-brightgreen.svg)](file:///d:/_test/keyboard/README.md)
[![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)](file:///d:/_test/keyboard/LICENSE)

A modern, fast, private, and feature-rich soft keyboard built natively for Indian languages and English. Designed from the ground up to empower users across India to type, speak, and express themselves seamlessly in Devanagari (Hindi, Marathi), Bengali, Telugu, Tamil, Gujarati, Kannada, Malayalam, Punjabi, Urdu, and English.

---

## 🌟 Key Highlights

- **100% Offline & Private**: Zero `android.permission.INTERNET` requested. Keystrokes, passwords, and voice inputs never leave the device.
- **Phonetic Transliteration Engine**: Intuitive Hinglish-to-Indic typing (e.g., type `namaste` → get `नमस्ते`). Supports 11 Indian scripts.
- **Smart English Autocorrect & Suggestions**: Built-in 20,000+ word dictionary with intelligent prefix matching and frequency weighting.
- **Bi-Lingual Voice Typing**: One-tap voice recognition configured specifically for Indian accents (`en-IN` for English and `hi-IN` for Hindi).
- **Expressive Media Keyboard**:
  - 12 comprehensive emoji categories (Smileys, Gestures, Hearts, Festivals, Animals, Food, etc.).
  - 8 Visual GIF Category Cards (`Trending`, `Reactions`, `Greetings`, `Perfect`, `Welcome`, `Congratulations`, `Thank you`, `Excited`).
  - Viral Bollywood & Desi Video Clips tab (`[▶ CLIPS]`) with one-touch shareable captions.
  - Japanese Kaomoji / Text Emoticons (`[^_^]`).
- **Standard Keyboard Height & Ergonomics**: Perfectly standardized 320dp height with zero layout shifting or jitter when switching between letters and emojis.
- **Proud Indian Tricolor Branding**: Sleek adaptive launcher icon featuring Kesari Saffron, Pure White with the 24-spoke Ashoka Chakra, and India Green.

---

## 🏗️ Technical Architecture

### 1. High-Level Component Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Android OS System Window                        │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │
                                   ▼
┌────────────────────────────────────────────────────────────────────────┐
│               IndicKeyboardService (InputMethodService)                │
├────────────────────────┬─────────────────────────┬─────────────────────┤
│   CandidateStripView   │     SoftKeyboardView    │  EmojiKeyboardView  │
│  - Suggestion Chips    │  - QWERTY / Indic Alpha │  - 12 Emoji Cats    │
│  - Quick EN/HN Pill    │  - Symbols & Numbers    │  - 8 Visual GIF Cats│
│  - Mic / Voice Button  │  - Action / Enter Keys  │  - Video Clips Tab  │
│  - Settings Shortcut   │  - Haptic & Audio       │  - Kaomoji Tab      │
└───────────┬────────────┴────────────┬────────────┴──────────┬──────────┘
            │                         │                       │
            ▼                         ▼                       ▼
┌────────────────────────┐┌────────────────────────┐┌────────────────────┐
│   Transliteration &    ││  Native Voice Typing   ││ Media Collections  │
│   Suggestion Engine    ││     Subsystem          ││     & Recents      │
├────────────────────────┤├────────────────────────┤├────────────────────┤
│ • IndicTransliterator  ││ • SpeechRecognizer     ││ • EmojiData        │
│ • HinglishEngine       ││ • en-IN / hi-IN Locale ││ • SharedPreferences│
│ • EnglishDictionary    ││ • Strict Password Guard││ • Gradient Drawables│
└────────────────────────┘└────────────────────────┘└────────────────────┘
```

---

### 2. Core Subsystems

#### A. Input Method Lifecycle (`IndicKeyboardService`)
- Extends standard Android `android.inputmethodservice.InputMethodService`.
- Coordinates `InputConnection` transactions (`commitText`, `setComposingText`, `deleteSurroundingText`, `performEditorAction`).
- Implements dynamic height management: maintains an exact standard keyboard height of **320dp** across both soft keyboard typing and rich media browsing.
- Strict password field detection prevents logging, dictionary lookups, or voice activation in password and numeric PIN fields (`TYPE_TEXT_VARIATION_PASSWORD`, `TYPE_NUMBER_VARIATION_PASSWORD`).

#### B. Indic Transliteration & Phonetic Soundex Engine (`IndicTransliterator`)
- Efficient mapping matrices translating Romanized syllables to Unicode codepoints for 11 Indic scripts:
  - Hindi (Devanagari)
  - Marathi (Devanagari)
  - Bengali (Bangla)
  - Telugu
  - Tamil
  - Gujarati
  - Kannada
  - Malayalam
  - Punjabi (Gurmukhi)
  - Urdu (Nastaliq mapping)
  - Odia
- Supports matras, conjuncts, halant elimination, and nasal anusvara (`ं`) / visarga (`ः`) rules.

#### C. English Dictionary & Autocorrect (`EnglishDictionary`)
- High-performance prefix trie storing frequently typed English vocabulary.
- Word casing preservation: if user starts typing with uppercase (`Hello`), candidate suggestions dynamically reflect the casing (`Hello`, `Helpful`).
- Single-letter guard: space commits only typed letters without forcing premature suggestions unless the word is complete.

#### D. Voice Typing Integration
- Directly utilizes Android's built-in `SpeechRecognizer` API with zero external cloud dependencies.
- Automatically selects the recognition locale based on the active keyboard language:
  - **EN** mode → `en-IN` (Indian English)
  - **HN** mode → `hi-IN` (Hindi)
  - Also supports `mr-IN`, `bn-IN`, `te-IN`, `ta-IN`, `gu-IN`, `kn-IN`, `ml-IN`, `pa-IN`, `ur-IN`.
- Commits transcribed sentences with intelligent punctuation spacing.

#### E. Media & GIF Category Engine (`EmojiKeyboardView`)
- 2-column visual category cards matching Tenor / Gboard UI:
  1. `🔥 Trending`
  2. `🤩 Reactions`
  3. `👋 Greetings`
  4. `👌 Perfect`
  5. `🚪 Welcome`
  6. `🎉 Congratulations`
  7. `🙏 Thank you`
  8. `⚡ Excited`
- Direct category navigation with header back buttons.
- Bottom safe insets prevent clipping against Android gesture navigation bars.

---

## 🔒 Security & Google Play Compliance

| Audit Check | Status | Verification Detail |
|---|---|---|
| **Internet Access** | ✅ None | `INTERNET` permission is NOT declared in `AndroidManifest.xml`. The app cannot connect to external servers. |
| **Password Protection** | ✅ Compliant | Keyboard disables candidate suggestions, word learning, and voice typing inside password/PIN fields. |
| **Keystroke Logging** | ✅ Safe | No logging of user keystrokes, personal communications, or sensitive credentials. |
| **App Backup** | ✅ Disabled | `android:allowBackup="false"` prevents extraction of keyboard state via ADB or cloud backups. |
| **File Provider** | ✅ Hardened | Scoped exclusively to cache directory for APK sharing; external storage root exposure removed. |
| **Target SDK** | ✅ Modern | Targets Android 14+ (API 34) with compatibility up to Android 16 (API 35). |

---

## 🛠️ Build & Development

### Prerequisites
- Android Studio Ladybug / Koala or newer.
- Android SDK Platform 34 or 35.
- JDK 17.
- Gradle 8.2+.

### Build Commands

```bash
# Clean project
./gradlew clean

# Run unit tests
./gradlew testDebugUnitTest

# Assemble Debug APK
./gradlew assembleDebug

# Assemble Release Bundle (AAB) for Google Play
./gradlew bundleRelease
```

### Install onto Device via ADB

```bash
adb install -r ./app/build/outputs/apk/debug/indic-keyboard-amit-bharat.apk
```

---

## 📁 Project Structure

```
keyboard/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/amitbharat/keyboard/
│   │   │   │   ├── engine/                # Transliteration, Dictionary, Soundex
│   │   │   │   │   ├── IndicTransliterator.java
│   │   │   │   │   ├── EnglishDictionary.java
│   │   │   │   │   └── KeyboardPreferences.java
│   │   │   │   ├── ime/                   # InputMethodService and View Controllers
│   │   │   │   │   ├── IndicKeyboardService.java
│   │   │   │   │   ├── SoftKeyboardView.java
│   │   │   │   │   ├── CandidateStripView.java
│   │   │   │   │   ├── EmojiKeyboardView.java
│   │   │   │   │   └── EmojiData.java
│   │   │   │   └── ui/                    # Setup Wizard, Settings & About Activities
│   │   │   ├── res/
│   │   │   │   ├── drawable/              # Tricolor Adaptive Icons & Keycap Drawables
│   │   │   │   ├── layout/                # Keyboard Root & Dialog Layouts
│   │   │   │   ├── values/                # Themes, Dimensions, Colors, Strings
│   │   │   │   └── xml/                   # IME Method & FileProvider config
│   │   │   └── AndroidManifest.xml
│   │   └── test/                          # Unit Tests
│   └── build.gradle
├── README.md                              # Technical Documentation
├── USER_MANUAL.md                         # End-User Manual & Feature Guide
└── build.gradle
```

---

## 🇮🇳 Credits & License

Developed with ❤️ for Bharat. Distributed under the Apache License 2.0.
