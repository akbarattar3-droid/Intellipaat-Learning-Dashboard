# Intellipaat Learning Dashboard

Android learning app: login, course list, course details, and lesson completion. Demo credentials: `user@intellipaat.com` / `password123`.

## 1. Architecture

**Clean Architecture** (data → domain → presentation) with **MVVM**, **Hilt**, **Kotlin Flow**, and **Jetpack Compose**.

- **Presentation:** Compose screens + `ViewModel` expose `StateFlow` UI state. Screens never talk to APIs or Room.
- **Domain:** Use cases call repository interfaces and return `Flow<AppResult<T>>`. Business rules (e.g. progress) stay here.
- **Data:** Repository implementations fetch from a mock API, persist to Room, and emit cached updates.

This split keeps UI testable (mock the repository Flow), makes offline a data-layer concern, and lets Hilt swap implementations without changing screens.
Clean Architecture keeps business logic separate from frameworks, databases, and user interfaces. That makes software easier to change, test, and maintain.

## 2. Offline support

Courses and lessons are stored in **Room** (`courses`, `lessons`).

1. On load, the repository reads the local DB.
2. If the cache is empty and the device is **online**, it downloads JSON (mock API), writes it in a **transaction**, then observes Room.
3. If the cache is empty and **offline**, it emits an error (nothing to show yet).
4. If the cache exists, the UI is driven by **Room `Flow`** — no network required. Marking a lesson complete updates Room; progress is recalculated locally and the dashboard/details UI refresh automatically.

First launch needs a network (or seeded DB). After that, browse and complete lessons offline.

## 3. Security (production tokens)

This demo only stores a **logged-in flag in SharedPreferences**. That is not acceptable for real tokens.

In production, store access/refresh tokens in the **Android Keystore** via **EncryptedSharedPreferences** or a small encrypted DataStore. Keep tokens out of logs, backups, and the UI process where possible. Prefer **short-lived access tokens**, a **refresh token** with rotation, HTTPS/certificate pinning, and biometric/lock-screen gating before reading the vault. Never put tokens in Room, plain prefs, or BuildConfig.

## 4. Scale (1M users, hundreds of courses)

1. **Real backend** with pagination, search, and ETags/`updatedAt` so clients sync deltas instead of full catalogs.
2. **Paging 3 + indexed Room**; lazy-load lessons per course instead of caching everything.
3. **CDN + image/video pipeline** for thumbnails and lesson media; don’t bundle content in assets.
4. **Auth at scale:** OAuth/OIDC, token refresh, device session revoke, and rate-limited login.
5. **Observability:** crash/ANR reporting, API tracing, and feature flags for staged rollouts.

## 5. Second platform (iOS / macOS)

Reuse the **same layers**, not the Android UI.

**Preferred:** **Kotlin Multiplatform** — share domain (use cases, models, `AppResult`) and a repository contract. Android keeps Compose + Room; iOS/macOS uses **SwiftUI**. Networking and local DB are expect/actual (Ktor + SQLDelight, or SwiftData on Apple).

**Native-only:** mirror the app in Swift: SwiftUI + Observable ViewModels, protocol-based repositories, async `AsyncSequence`/Combine instead of Flow, **SwiftData/Core Data** for offline, **Keychain** for tokens, and a REST client. Navigation and Hilt become NavigationStack and a DI container (or `environment`).

Either way, keep UI framework-specific and put caching, auth, and progress rules behind the same domain API.
