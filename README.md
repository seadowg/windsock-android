# Deckard (for Gradle)
[![Build Status](https://secure.travis-ci.org/robolectric/deckard-gradle.png?branch=master)](http://travis-ci.org/robolectric/deckard-gradle)

Deckard is the simplest possible Android project that uses Robolectric for testing and Gradle to build. It has one Activity, a single Robolectric test of that Activity, and an Espresso test of that Activity.

Deckard also imports seamlessly into IntelliJ and Android Studio, due to their support for gradle. See below for instructions.

## Setup

*Note: These instructions assume you have a Java 1.6 [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) installed.*

To start a new Android project:

1. Install the [Android SDK](http://developer.android.com/sdk/index.html). On Mac OS X with [Homebrew](http://brew.sh/) just run `brew install android-sdk`

2. Set your `ANDROID_HOME` environment variable to `/usr/local/opt/android-sdk`.

3. Run the Android SDK GUI and install API 18 and any other APIs you might need. You can start the GUI by invoking `android`

4. Download Deckard from GitHub:
    ```bash
    wget https://github.com/robolectric/deckard-gradle/archive/master.zip
    unzip master.zip
    mv deckard-master my-new-project
    ```

5. In the project directory you should be able to run the Robolectric tests:
    ```bash
    cd my-new-project
    ./gradlew clean test
    ```
6. You should also be able to run the Espresso tests: `./gradlew clean connectedInstrumentTest`
7. Change the names of things from 'Deckard' to whatever is appropriate for your project. Package name, classes, and the AndroidManifest are good places to start.

8. Build an app. Win.

## IntelliJ / Android Studio Support

1. Import the project into IntelliJ or Android Studio by selecting 'Import Project' and selecting the project's `build.gradle`. When prompted, you can just pick the default gradle wrapper.

2. You will also need to change the classpath order for you dependencies. Otherwise you will see the dreaded `Stub!` exception:
	```
	!!! JUnit version 3.8 or later expected:

	java.lang.RuntimeException: Stub!
	at junit.runner.BaseTestRunner.<init>(BaseTestRunner.java:5)
	at junit.textui.TestRunner.<init>(TestRunner.java:54)
	at junit.textui.TestRunner.<init>(TestRunner.java:48)
	at junit.textui.TestRunner.<init>(TestRunner.java:41)
	```
    * For Intellij, go to Project Structure -> Modules -> deckard-gradle pane. In the Dependencies tab, move the Module SDK dependency (i.e. Android API 19 Platform) to be the last item in the list.
    * For Android Studio, dependency ordering is currently not modifiable via any GUI. Therefore, you must modify the project iml file directly as such and reload the project:

    ```html
    	    	<orderEntry type="library" exported="" scope="TEST" name="wagon-provider-api-1.0-beta-6" level="project" />
    	    	<orderEntry type="library" exported="" scope="TEST" name="xercesMinimal-1.9.6.2" level="project" />
    	    	<orderEntry type="jdk" jdkName="Android API 19 Platform" jdkType="Android SDK" />			    		<---make sure this is the last orderEntry
    		</component>
    	</module>
    ```
3. You should now be able to `DeckardActivityRobolectricTest`. Run it as a normal JUnit test - make sure to choose the JUnit test runner and not the Android one.
 
4. To run the Espresso test, you need to set up a Run Configuration. Go to `Edit Configurations -> Defaults -> Android Tests` and, after choosing  the correct module (deckard-gradle), fill in the `Specific instrumentation test runner` field. The easiest way is to click the elipsis button on the right and type in `GITR`. This will find `GoogleInstrumentationTestRunner`, which is what you want. The fully-qualified class name will appear. Now you can right click on the test method in `DeckardEspressoTest` and choose the Android test runner.





