# CatchIt

![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)

CatchIt is an Android Library to monitor exceptions and sync with dedicated node-js server.
<br><br>


  - Supports caught and uncaught exceptions
  - Easy integration
  - Dedicated node-js code to handle exceptions
  - Unit & Instrumented tests
  <br><Br>

## Requirements
- Android 28+
<br><br>

## Installation

CatchIt library is distributed as Android library so first clone repository to your local machine, then add **catchItLib** module into your Android Studio project, lastly:
<br>
### Using Gradle

```xml
dependencies {
    implementation project (':catchItLib')
}
```
<br>

## Initializing CatchIt

1. Add the internet permission to the AndroidManifest.xml file:

    ```xml
    <uses-permission android:name="android.permission.INTERNET" />
    ```

3. Import the CatchIt into your **Application** class:

    
    ```java
    import com.catchit.lib.CatchIt;
    import com.catchit.lib.CatchItOptions; // optional
    ```

4. Start CatchIt with your **Application** context by adding this to your `Application.onCreate` method:

    
    
    ```java
    import com.catchit.lib.CatchIt;

    public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CatchIt.start(this);
    }
    ```
    
    You can start CatchIt with custom options (setting server address and changing backend sync interval):

    ```java
    import com.catchit.lib.CatchIt;
    import com.catchit.lib.CatchItOptions;

    public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CatchIt.start(this,
            new CatchItOptions.Builder()
                .setServerAddress("SERVER_ADDRESS")
                .setSyncInterval(15, TimeUnit.SECONDS).build());
    }
    ```


5. To log caught exception from anywhere in your app use:
    ```java
    CatchIt.logException(new Exception("hello caught exception"));
    ```

And that's it! You're good to go and start monitor caught and uncaught exceptions!

*For a working implementation of CatchIt project see the `app/` folder.*
<br><br>

## Todos (future features)
  
  - Add CatchIt web dashboard (to view exceptions and generating API tokens)
  - Add Activity lifecycle logs as well so we can track users UI trail before exception was thrown
  - Let developer add custom attributes on runtime to identify end-user (by using third_party_id or email address)
  - Adding "handshake" API call every app launch (or just on first exceptions sync)
  - In this "handshake" we'll send  App&Device info as for now the data being sent on every exceptions sync
  - Create "Interruptible" Executor and "Worker" so I can shut down it safely on uncaught exception scenario
  - Protecting internal library classes from container app access (this is a common issue in SDK development) as we don't want any unintended usage of library
  - Provide my own custom Executor for Retrofit (Networking) as I want full control on network tasks pooling
  - Adding Proguard/R8 support
  - Adding more tests
  <br><br>
  
 ### Notes

 - You'll see Builder pattern for constructing new objects, this pattern let us test objects dependenceis on ease and is well used by Google in Android API
  <br><br>
 
### Dependencies
* [Retrofit] - A type-safe HTTP client for Android and Java.
* [Room] - Persistence library that provides an abstraction layer over SQLite.
* [JUnit] -  Unit testing framework for Java.
* [Espresso] - UI testing framework for Android powered devices.
  <br><br><br>
# NodeJS server
CatchIt server responsible for receiving exceptions and saving them into SQLite database

<br>

## Installation

CatchIt server requires [Node.js](https://nodejs.org/) v12.19.0+ to run.

### Install dependencies

```sh
$ cd catchit-server
$ npm install express
$ npm install sqlite3
```



## Initializing CatchIt Server
### Start Node.js server

```sh
$ node run start
```

## API Endpoints

Exceptions REST API endpoints (require no authentication).

* **Syncing Exceptions** `POST /api/exceptions/`
* **Viewing Exceptions** `GET /api/exceptions/`


## Exceptions scheme (JSON style)

```json
      "id": 6,
      "message": "Attempt to invoke virtual method 'java.lang.String java.lang.String.toString()' on a null object reference",
      "class": "com.catchit.app.MainActivity",
      "method": "lambda$onCreate$1",
      "line": 20,
      "timestamp": "1603570643720",
      "stackTrace": "long stack trace goes here...",
      "app_info": {
                    "version":1.0,
                    "versionCode":1
                  },
      "device_info": {
                    "brand":"OnePlus",
                    "densityDpi":420,
                    "manufacturer":"OnePlus",
                    "model":"ONEPLUS A6013",
                    "screenHeight":2261,
                    "screenWidth":1080,
                    "sdkVersion":28
                    }
```

<br>

### Dependencies
* [ExpressJS] - Node.js web  framework to create REST style web app in ease (v4.17.1).
* [Sqlite3] - SQLite3 persistence library (v5.0.0).

<br>

License
----
MIT

**Free Software, Hell Yeah!**

[//]: #

   [Retrofit]: <https://square.github.io/retrofit/>
   [Room]: <https://developer.android.com/topic/libraries/architecture/room>
   [JUnit]: <https://developer.android.com/training/testing/unit-testing/local-unit-tests>
   [Espresso]: <https://developer.android.com/training/testing/espresso>
   [SQLite3]: <https://www.npmjs.com/package/sqlite3>
   [ExpressJS]: <https://expressjs.com/>
