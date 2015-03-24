# Android MG Bootstrap
An opinionated bootstrap library for android projects that includes commonly used dependancies and some custom wrapper libraries.

## MG Log
A simple logging wrapper over Timber (https://github.com/JakeWharton/timber)

#### Configuration
Initialize the logger in your application class.
```java
class App extends MGLifecycleApplication {
    public void onCreate() {
        super.onCreate();
        
        MGLog.getConfig().init(this);
    }
}
```
