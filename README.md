# Android Shoppable Application

This is a small app that lets the users browse items, visualize item details, add them to a persisted cart, visualize the cart, remove items and lastly to send orders.

Tha app is build with the help of the following libraries and technologies:
* Kotlin
* Dagger2 for DI
* ViewModels and Repositories
* Room for persistence
* Android Jetpack Nnavigation
* RxJava (Kotlin) - Reactive


## Installation
Clone this repository and optionally import into **Android Studio**

```bash
git clone https://github.com/kalmanbencze/Shoppable.git
```
Conext an android device then:
Run the app with
```bash
./gradlew installDebug
```

Run the UI tests with
```bash
./gradlew connectedDebugAndroidTest
```


## Generating signed APK
From Android Studio:
1. ***Build*** menu
2. ***Generate Signed APK...***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*

## Owner
* [Kalman Bencze](https://github.com/kalmanbencze/Shoppable)

