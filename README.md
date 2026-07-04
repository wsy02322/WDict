# WDict

A lightweight Android dictionary app powered by the [OpenRouter](https://openrouter.ai) API.

## Features

- **Main screen** – type any word or phrase and tap *Look Up* to get a definition.
- **Text-selection toolbar** – select text in *any* app, tap **WDict** in the floating toolbar, and get an instant definition popup without leaving what you're reading.
- **Settings** – enter your personal OpenRouter API key and optionally choose a model. The key is stored locally on the device (SharedPreferences) and never leaves your phone.
- **Dark mode** – follows system theme automatically (Material 3 DayNight).

## Getting started

### Prerequisites

| Tool | Version |
|------|---------|
| Android Studio | Hedgehog 2023.1.1 or later |
| Android SDK | API 26+ (Android 8.0 Oreo) |
| JDK | 17 (bundled with Android Studio) |

### Build & run

1. Clone the repo and open it in **Android Studio**.  
   Android Studio will sync automatically.

2. Build a debug APK from the command line:
   ```sh
   ./gradlew assembleDebug
   ```

3. Install on a device / emulator:
   ```sh
   ./gradlew installDebug
   ```

### Build a debug APK with GitHub Actions

1. Open the repository on GitHub.
2. Click **Actions**.
3. Click **Build Debug APK** in the left sidebar.
4. Click **Run workflow**.
5. Choose the branch and click **Run workflow** again.
6. Open the new workflow run after it starts.
7. Wait for the **build** job to finish successfully.
8. In **Artifacts**, click **debug-apk** to download the generated APK.

### OpenRouter API key

1. Create a free account at <https://openrouter.ai> and generate an API key.
2. Open **WDict → ⋮ → Settings**, paste the key, and tap **Save**.
3. Optionally choose a model (default: `openai/gpt-4o-mini`).  
   Any model available on OpenRouter works, including free ones like `google/gemma-2-9b-it:free`.

The key is stored in private app SharedPreferences and is **never transmitted** outside of direct calls to the OpenRouter API endpoint.

## Project structure

```
app/src/main/java/com/wsy/wdict/
├── ApiKeyStorage.kt          # SharedPreferences wrapper (API key + model)
├── OpenRouterApiClient.kt    # OkHttp + Gson client for OpenRouter chat-completions
├── MainActivity.kt           # Main dictionary look-up screen
├── SettingsActivity.kt       # API key / model configuration
└── ProcessTextActivity.kt    # Handles ACTION_PROCESS_TEXT (text-selection toolbar)
```

## License

MIT
