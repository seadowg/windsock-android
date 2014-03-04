# Deckard

[![Build Status](https://secure.travis-ci.org/robolectric/deckard-gradle.png?branch=master)](http://travis-ci.org/robolectric/deckard-gradle)

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

6. Change the names of things from 'Deckard' to whatever is appropriate for your project. Package name, classes, and the AndroidManifest are good places to start.

7. Build an app. Win.

## Optional
1. Import the project into IntelliJ, Android Studio, or Eclipse by selecting 'Import Project' and selecting the project's `build.gradle`. When prompted, you can just pick the default gradle wrapper.

2. You may also need to change the classpath order for you dependencies:
    - For Intellij, go to Project Structure -> Modules -> RobolectricGradleExample pane. In the Dependencies tab, move the Module SDK dependency (i.e. Android API 19 Platform) to be the last item in the list.
    - For Android Studio, dependency ordering is currently not modifiable via any GUI. Therefore, you must modify the project iml file directly as such and reload the project:

```html
	    	<orderEntry type="library" exported="" scope="TEST" name="wagon-provider-api-1.0-beta-6" level="project" />
	    	<orderEntry type="library" exported="" scope="TEST" name="xercesMinimal-1.9.6.2" level="project" />
	    	<orderEntry type="jdk" jdkName="Android API 19 Platform" jdkType="Android SDK" />					<---make sure this is the last orderEntry
		</component>
	</module>
```






