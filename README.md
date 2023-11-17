# SongMatch

## Overview

Welcome to SongMatch, an Android app designed to turn playlist creation into a collaborative and fun experience with your friends on Spotify. With SongMatch, you can seamlessly blend your favorite tunes and create playlists that capture the essence of shared musical tastes.

## How to Use

To use SongMatch, ensure you have the following:

1. **Spotify Account**: Sign up for a Spotify account if you don't have one already.

2. **Mobile Device/Emulator with the Spotify App Installed**: Ensure you have a mobile device or emulator with the **Spotify app** installed. Please note that during development, there were issues with the Spotify app facing difficulties connecting to the internet. If encountered, run the app on a physical device.

3. **API Access (Important!)**: Since we're in beta, we access the Spotify API in development mode. To enable us to access your music, please provide your full name and the email associated with your Spotify account by filling out [this form](https://docs.google.com/forms/d/1gHrcepIcMCdDaNfZZV1M8Ks-GS2CtLMKf5qBVdKeTQE/edit). We'll add your details as quickly as possible.

## Architecture

The codebase follows the principles of Clean Architecture for Android, organized in:

- **Presentation Classes (Fragments and ViewModels)**
- **Use Cases**
- **Repositories**
- **Data Sources**

Implemented in MVVM pattern.

## Technologies Used

- **Room**
- **Dagger Hilt**
- **Retrofit**
- **Moshi**
- **Firebase**
- **Navigation Component**
- **Spotify Android SDK**
- **Kotlin Flow**
- **LiveData**
- **ViewBinding**
- **Coroutines**

## Next Steps
- Replace MVP UI
- Refine objects returned by `FirebaseDataSource` for reduced coupling
- Change data layer dispatchers to `Dispatchers.IO`
- Implement logic for music matching. Currently, the app only combines the songs of all users in the room because I haven't had time to implement it.
- Enhance error context for user understanding
- Reauthenticate users if necessary for playlist creation
- Implement unit tests using jUnit and mockk following the given -> when -> then methodology

## Notes

The app is currently in beta, and as such, may have some bugs:

- After creating or joining a room, there's no option to leave. To do so, clear the app's storage and cache in the device settings.
- When reopening the app, it will navigate to the correct screen based on your last action, but the navigation is not optimized and may lead to unexpected situations.

Your feedback is valuable as we work towards refining and enhancing the SongMatch experience! 🎵
