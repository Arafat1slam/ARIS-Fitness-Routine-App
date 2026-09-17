# ARIS Fitness Routine App ⚡

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android%20Native-brightgreen.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3%2B-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-purple.svg)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-26-orange.svg)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-36-red.svg)](https://developer.android.com)

A data-driven, daily-routine and nutrition-tracking native Android application built around **5 selectable Character Profiles**. Each character provides a pre-defined **3-year (1,095-day) day-indexed routine** covering wake/sleep schedules, tailored meals with scaled calorie/macro targets, workouts, and recovery habits.

The app personalizes each character's reference routine to the actual user via their biometrics (height, weight, age, gender, activity level) using the **Mifflin-St Jeor** formula, then drives their day through **exact alarms and notifications** even in Android Doze mode.

---

## 🌟 Key Features

### 1. 5 Character Profiles
- 🛡️ **Kaelen Vance ("The Titan")**: Advanced • Hypertrophy & Raw Strength (Ref: 2,800 kcal)
- ⚡ **Zephyr Sterling ("Aero")**: Intermediate • Agility & High-Velocity Conditioning (Ref: 2,500 kcal)
- 🔨 **Marcus Drake ("Ironclad")**: Advanced • Powerlifting & Heavy Mass (Ref: 3,000 kcal)
- 🥷 **Aria Vance ("Shadow")**: Intermediate • Gymnastic Calisthenics & Joint Mobility (Ref: 2,200 kcal)
- 🌱 **Leo Hayes ("The Catalyst")**: Beginner • Habit Transformation & Longevity (Ref: 2,100 kcal)

### 2. 3-Year (1,095-Day) Progressive Overload Engine
- **Phase I (Days 1–90)**: Structural Foundation & Neuromuscular Adaptation
- **Phase II (Days 91–365)**: Progressive Hypertrophy & Mechanical Tension
- **Phase III (Days 366–730)**: High-Density Intensification & Peak Force
- **Phase IV (Days 731–1,095)**: Elite Mastery, Joint Longevity & Lifelong Vitality
- Periodic deload weeks (every 6th week) and active recovery sessions.

### 3. Calorie Personalization Engine
- **Mifflin-St Jeor BMR Formula**:
  - Male: `10 × weight_kg + 6.25 × height_cm - 5 × age + 5`
  - Female: `10 × weight_kg + 6.25 × height_cm - 5 × age - 161`
- **TDEE Scaling**: Multipliers from Sedentary (1.2) to Very Active (1.9).
- **Proportional Macro Scaling**: `scale_factor = user_tdee / character.reference_tdee` automatically scales calories, protein, carbohydrates, and fats for every meal across all 1,095 days.

### 4. Exact-Time Alarms & Background Scheduling
- **`AlarmManager.setExactAndAllowWhileIdle()`**: Wake-up alarms and meal notifications fire reliably even when device is locked or in deep Doze mode.
- **`DailyRescheduleWorker`**: Automatic WorkManager job scheduled at 00:05 midnight every day to queue the next day's alarms.
- **`BootReceiver`**: Automatically restores all scheduled alarms whenever the phone restarts.
- **4 Dedicated Notification Channels**: Wake Alarms, Meal Alerts, Workout Reminders, and Recovery Protocols.

### 5. Missed-Day & Pause/Resume Protocol
- Handles days when the app wasn't opened without silently skipping routines.
- Prompts the user to either:
  - **Resume from where they left off** (repeat missed day; log skipped calendar days in `paused_days`).
  - **Catch up to today's date**.
- Accurately tracks adherence rate over total elapsed active days.

### 6. Animated Cyber-Athletic Dark Theme
- **Animated Calorie & Macro Rings**: Multi-ring Canvas drawing with fluid sweep animations.
- **Breathing Streak Flame**: Infinite pulsing glow effect representing continuous discipline.
- **Spring-Physics Checklists**: Interactive cards with bounce micro-interactions and smooth strikethrough animations.
- **Live Canvas Weight Trend Chart**: Interactive historical weight tracking graph.

---

## 🛠️ Tech Stack & Architecture

| Layer | Technology |
|---|---|
| **Language** | Kotlin 2.3+ / Java 21 |
| **UI Framework** | Jetpack Compose + Material 3 |
| **Local Database** | Offline-First SQLite (`ArisDatabaseHelper`) with O(1) indexed lookups on `(character_id, day_index)` |
| **Architecture** | MVVM + Repository Pattern + StateFlow |
| **Background Scheduling** | `AlarmManager` + `WorkManager` + `BroadcastReceiver` |
| **Data Serialization** | `kotlinx.serialization` (JSON) |
| **Navigation** | `androidx.navigation3` |

---

## 📱 Installation Guide

### Option 1: Direct APK Install on Phone (Recommended)
1. Copy **`ARIS-Fitness-v1.0-debug.apk`** from the project root to your Android phone via USB cable, Google Drive, or messaging apps.
2. Tap on the APK file in your phone's file manager.
3. Allow "Install unknown apps" if prompted.
4. Tap **Install** and launch **ARIS Fitness**!

### Option 2: Build & Install via USB Debugging
Connect your Android device with USB debugging enabled and run:
```bash
# On Windows (PowerShell):
.\gradlew.bat installDebug

# On macOS/Linux:
./gradlew installDebug
```

---

## 🧪 Testing

Run automated unit tests for the Calorie Personalization Engine, Date Resolution, and 1,095-Day Generator:
```bash
# Windows
.\gradlew.bat testDebugUnitTest

# macOS/Linux
./gradlew testDebugUnitTest
```

---

## 📄 License

This project is licensed under the [MIT License](LICENSE) - see the [LICENSE](LICENSE) file for details.

Copyright (c) 2026 **Arafat Islam**.
