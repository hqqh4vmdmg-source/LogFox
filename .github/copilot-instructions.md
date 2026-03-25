# GitHub Copilot Instructions for LogFox

This file configures GitHub Copilot to follow the conventions, architecture, and tooling
used in the LogFox project. Read the `AGENTS.md` at the repo root for the full guide.

---

## Androjack Tool Usage — REQUIRED Before Every Android/Kotlin Code Change

Before generating or modifying **any** Android or Kotlin code in this project, you MUST
invoke the relevant [androjack](https://github.com/androjack) MCP tools in this order:

1. **`android_official_search`** — Search developer.android.com / kotlinlang.org for any
   API, Jetpack component, or pattern you are about to use. Never generate code based on
   memorised knowledge alone.

2. **`android_component_status`** — Check that every class from `android.*`, `androidx.*`,
   or `kotlin.*` is **stable** and not deprecated/removed before using it.
   Known failure cases: `AsyncTask`, `IntentService`, `Handler(Looper.main())`,
   `onBackPressed()`, `ProgressDialog`, `TestCoroutineDispatcher`.

3. **`gradle_dependency_checker`** — Before adding or bumping any dependency, verify the
   current latest stable version. The project's `gradle/libs.versions.toml` is the single
   source of truth; always update it there.

4. **`android_code_validator`** — Run on **every generated code block** (Kotlin, XML,
   Gradle) before showing it to the user. If the verdict is **FAIL**, fix all errors and
   re-run before returning the code.

5. **`kotlin_best_practices`** — Consult for patterns involving coroutines, StateFlow,
   Compose state, Room DAOs, Hilt injection, or WorkManager tasks.

6. **`architecture_reference`** — Consult before proposing app architecture changes,
   folder structure changes, or Jetpack component relationships.

7. **`android_api36_compliance`** — Check before any manifest, Activity, or adaptive-layout
   change (mandatory for Play Store by August 2026).

---

## Project Architecture (TEA / MVI)

LogFox uses a **pure-Kotlin TEA (The Elm Architecture)** pattern:

```
State + Command → Reducer → (NewState, SideEffects)
SideEffect → EffectHandler → Commands
State → ViewStateMapper → ViewState → UI
```

**Strict dependency rules** (never violate):
- `presentation` module imports only `api` module — never `impl`
- `impl` imports its own `api`
- Only `:app` aggregates `presentation + impl` modules
- All preference-backed toggle state lives in ViewModel `State`, not in Compose `remember`
- EffectHandlers own all reads/writes to repositories/SharedPreferences

**Naming conventions:**
- `<Feature>ViewModel`, `<Feature>Reducer`, `<Feature>EffectHandler`, `<Feature>ViewStateMapper`
- One top-level type per file; file name matches the type name
- Use cases expose `operator fun invoke()`; failable operations return `Result<T>`
- Hilt `@Binds` methods return interfaces, not implementation types

---

## Kotlin Style Enforced by This Project

- **Expression bodies** for single-expression functions: `fun foo() = bar()`
- **`when` branches without braces** when the body is a single expression
- **Consecutive `-> Unit` branches** merged: `is A, is B -> Unit`
- **Trailing lambdas** preferred over named parameter style when last param is a lambda
- **No `GlobalScope`** — always use `viewModelScope` or a provided `CoroutineScope`
- **No `runBlocking` in UI/ViewModel code**
- `LinkedList` replaced with `ArrayDeque` where only queue/stack operations are needed
- Imports sorted alphabetically; no wildcard imports

---

## Build Commands

```bash
./gradlew :app:assembleDebug --quiet          # Build debug APK
./gradlew testDebugUnitTest --quiet           # Run unit tests
./gradlew verifyRoborazziDebug --quiet        # Run snapshot tests (CI uses this)
```

---

## Dependency Version Checks (androjack-verified, 2026-03)

| Library              | Current in toml | Androjack-verified latest |
|----------------------|-----------------|---------------------------|
| Compose UI           | 1.10.3          | 1.11.0-beta01 (stable: 1.10.3) |
| Compose Material3    | 1.4.0           | 1.4.0 ✅                  |
| Room                 | 2.8.4           | 2.8.4 ✅                  |
| Hilt                 | 2.59.1          | 2.56.2 (project is newer) |
| Coil                 | 2.7.0           | 3.2.0 (group changed to `io.coil-kt.coil3`) |
| Kotlin               | 2.3.10          | check `kotlin_best_practices` |

> ⚠️ Coil 3.x uses a **different Maven group** (`io.coil-kt.coil3`). Do NOT upgrade Coil
> without a full migration — all usage sites change.

---

## Android 16 / API 36 Compliance

- Never add `android:screenOrientation="portrait"` or `android:resizeableActivity="false"`
  to the manifest. This project targets `compileSdk = 36`.
- Use `WindowSizeClass` + `NavigationSuiteScaffold` for adaptive layouts.
- Run `android_api36_compliance` before any manifest or Activity change.

---

## Testing

- Snapshot tests use **Roborazzi** (`verifyRoborazziDebug`). Update golden images with
  `recordRoborazziDebug` whenever Compose UI changes.
- Unit tests use JUnit 4 + Robolectric.
- Never remove or skip existing tests.
