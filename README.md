# CatchIt

> CatchIt is a Android Library to monitor exceptions and sync with dedicated node-js server.

  - Supports caught and uncaught exceptions
  - Easy integration
  - Dedicated node-js code to handle exceptions
  - Unit & Instrumented tests
  <br><br>

### How to use
If you want use this library, you need to clone this project and import *catchItLib* module into your android studio project.
<br>
<br>
```xml
dependencies {
    implementation project (':catchItLib')
}
```
<br>
Then you need to subclass your Application class and start CatchIt <br> <br>

```xml
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CatchIt.start(this);
    }
}
```
You can start CatchIt with custom options (setting server address and changing backend sync interval)
```xml
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CatchIt.start(this,
            new CatchItOptions.Builder()
                .setServerAddress("SERVER_ADDRESS")
                .setSyncInterval(15, TimeUnit.SECONDS).build());
    }
}

```
To log caught exception
```xml
CatchIt.logException(new Exception("hello caught exception"));
``` 
<br>

### Todos (future features)
  
  - Add CatchIt web dashboard (to view exceptions and generating API tokens)
  - Add Activity lifecycle logs as well so we can track users trail before exceptions was thrown (to understand user footprint)
  - Let developer add custom attributes to users using his app so he can identifies user (by using third_party_id or email address)
  - Add CatchItOptions.Builder so developers can customize CatchIt behvaiour such as chaging backend URL and sync interval.
  - Differentiating between caught and uncaught exceptions
  <br><br>
 ### Notes
 - CatchIt uses less 3rd parties libraries more than usual because we want the library to be little as possible
 - You'll see Builder pattern for constructing new objects, this pattern let us test objects dependenceis on ease and is well used by Google in Android API
  <br><br>
### Future dev improvements
  
  - Adding "handshake" API call every app launch (or just on first exceptions sync) to send App&Device info as for now the data being sent on every exceptions sync
  - Create "Interruptable" Executor and Worker (more info on AppExecutors.java)
  - Protecting internal library classes from container app access (this is a common issue in SDK development) as we don't want any unintended usage of library (we can achieve this by few solutions, making all our classes private and then organizing flat package structure or by using Reflection to use private classes)
  - Add Repository interface and Implementation
  - Provide my own custom Executor for Retrofit (Networking) as I want full control on network tasks pooling
  - Adding more tests
  <br><br>

### Dependencies
* [Retrofit] - A type-safe HTTP client for Android and Java.
* [Room] - Persistence library that provides an abstraction layer over SQLite.
* [JUnit] -  Unit testing framework for Java.
* [Espresso] - UI testing framework for Android powered devices.
  <br><br><br>
# NodeJS server
> CatchIt server responsible for receiving exceptions and saving them into SQLite database

<br>

### Installation

CatchIt server requires [Node.js](https://nodejs.org/) v12.19.0+ to run.

Install the dependencies

```sh
$ cd catchit-server
$ npm install express
$ npm install sqlite3
```
Start the server

```sh
$ node run start
```
<br>

## API Endpoints


Exceptions REST API endpoints (require no authentication).

* **Syncing Exceptions** `POST /api/exceptions/`
* **Viewing Exceptions** `GET /api/exceptions/`

<br>

### Dependencies
* [ExpressJS] - Node.js web  framework to create REST style web app in ease (v4.17.1).
* [Sqlite3] - SQLite3 persistence library (v5.0.0).


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
