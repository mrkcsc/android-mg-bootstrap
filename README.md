# Android MG Bootstrap
An opinionated bootstrap library for android projects that includes commonly used dependancies and some custom wrapper libraries.  Included utilities and dependencies are documented below.

## MG Log

A simple logging wrapper over Timber (https://github.com/JakeWharton/timber).  Use this to more easily log messages with a standard tag across projects.  By default it also filters out any log statement in production, or you can configure custom behavior in this enviornment.

#### Configuration

- Initialize the logger in your application class.

```java
class App extends MGLifecycleApplication {
    public void onCreate() {
        super.onCreate();
        
        MGLog.getConfig().init(this);
    }
}
```
- Optionally set production error and info actions (in Debug enviornment, messages are logged to console).

```java
MGLog.getConfig().setError((throwable, s, objects) -> { });
MGLog.getConfig().setInfo ((throwable, s, objects) -> { });
```

#### Usage

- Log anything and everything to your liking!

```java
MGLog.e("Here is a message");
MGLog.e("Here is a message with parameters: %d, %d", param1, param2);
MGLog.e("Here is a message" + " with more string.");

MGLog.e("Here is a message that logs at info level.");
```
