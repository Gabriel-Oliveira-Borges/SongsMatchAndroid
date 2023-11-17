# SongMatch

## Overview

Welcome to SongMatch, the Android app that takes playlist creation to the next level by blending your music with that of your friends on Spotify. SongMatch offers a collaborative music experience, allowing you to curate playlists that reflect the diverse tastes of your social circle.

## How to Use

To use SongMatch, make sure you have the following:

1. **Spotify Account**: Sign up for a Spotify account if you don't have one already.

2. **Mobile Device/Emulator with the Spotify App Installed**: Ensure you have a mobile device or emulator with the **Spotify app** installed. Please note that during development, there were issues with the Spotify app facing difficulties connecting to the internet. If encountered, run the app on a physical device.

3. **API Access (Important!)**: Since we're in beta, we access the Spotify API in development mode. To enable us to access your music, please provide your full name and the email associated with your Spotify account by filling out [this form](https://docs.google.com/forms/d/1gHrcepIcMCdDaNfZZV1M8Ks-GS2CtLMKf5qBVdKeTQE/edit). We'll add your data to allowlist as quickly as possible.

## Architecture

The codebase follows the Clean Architecture principles for Android, organizing the app into:

- **Presentation Classes (Fragments and ViewModels)**
- **Use Cases**
- **Repositories**
- **Data Sources**

## Technologies Used

- **Room**
- **Dagger Hilt**
- **Retrofit**
- **Moshi**
- **Firebase**
- **Navigation Component**
- **Spotify Android SDK**

## Notes

The app is currently in beta, and as such, may have some bugs:

- After creating or joining a room, there's no option to leave. To do so, clear the app's storage and cache in the device settings.
- When reopening the app, it will navigate to the correct screen based on your last action (e.g., returning to a room). However, this navigation is not optimized and may lead to unexpected situations.

Your feedback is crucial as we refine and enhance the SongMatch experience! 🎵