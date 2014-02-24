# Deckard

[![Build Status](https://secure.travis-ci.org/robolectric/deckard.png?branch=master)](http://travis-ci.org/robolectric/deckard)

Deckard is the simplest possible Android project that uses Robolectric for testing and Gradle to build. It has one Activity (with an empty layout), and a Robolectric test that creates that Activity. 

Deckard also imports seamlessly into IntelliJ, due to IntelliJ's support for gradle. Just import the build.gradle.

## Setup

*Note: These instructions assume you have a Java 1.6 [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) installed.*

To start a new Android project:

1. Install the [Android SDK](http://developer.android.com/sdk/index.html). On Mac OS X with [Homebrew](http://brew.sh/) just run:
    ```bash
    brew install android-sdk
    ```

2. Set your `ANDROID_HOME` environment variable to `/usr/local/opt/android-sdk`.

3. Run the Android SDK GUI and install API 18 and any other APIs you might need. You can start the GUI like so:
    ```bash
    android
    ```

4. Download Deckard from GitHub:
    ```bash
    wget https://github.com/robolectric/deckard/archive/master.zip
    unzip master.zip
    mv deckard-master my-new-project
    ```

5. In the project directory you should be able to run the tests:
    ```bash
    cd my-new-project
    ./gradlew clean test
    ```
        
6. Optionally, import the project into IntelliJ (or Eclipse) by selecting 'Import Project' in IntelliJ and selecting the project's `build.gradle`. When prompted to pick an SDK you just need to select the Android SDK home and your JDK.

7. Change the names of things from 'Deckard' to whatever is appropriate for your project. Package name, classes, and the AndroidManifest are good places to start.

8. Build an app. Win.
