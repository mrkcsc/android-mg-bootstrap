# Android MG Bootstrap
An opinionated bootstrap library for android projects that includes commonly used dependancies and some custom wrapper libraries.  Included utilities and dependencies are documented below.

## MG Anim

A animation helper library that provides common animation patterns in a simple API.

#### Configuration

- No configuration needed!

#### Usage

- TODO: Fade

## MG Log

A simple logging wrapper over Timber (https://github.com/JakeWharton/timber).  Use this to more easily log messages with a standard tag across projects.  By default it also filters out any log statement in production, or you can configure custom behavior in this enviornment.

#### Configuration

- Initialize the logger in your application class.

```java
class App extends Application {
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

## MG Preference

A powerful wrapper on top of androids native preferences http://developer.android.com/guide/topics/ui/settings.html which by default can only store primative types.  Allows for persisting of arbitrary complex objects and adds a level of memory caching to them.

#### Configuration

- Initialize the preferences utility in your application class.

```java
class App extends Application {
    public void onCreate() {
        super.onCreate();
        
        MGPreference.getConfig().init(this);
    }
}
```

#### Usage

- Create an MGPreference object typed to an object of your choosing - provide a unique string identifier for lookups. Note:  You can make the variable final or/or static if you choose because the lookup is done by key.

```java
    // You can back primitive class wrappers.
    MGPreference<Integer> preference0 = MGPreference.create("INTEGER_PREFERENCE");
    
    // You can back classes that use generics.
    MGPreference<Map<String, List<String>>> preference1 = MGPreference.create("MAP_PREFERENCE");

    // You can back complex objects.
    MGPreference<MGRestClientErrorModel> preference2 = MGPreference.create("OBJECT_PREFERENCE");
```

- Once you variable is delcared, get and/or set its value as needed.  Get will return null if no value has been previously set.

```java
    // Fetch with get.
    int value = preference0.get();
    
    // Set with set.
    preference0.set(100);
    
    // Clear value.
    preference0.clear();
```
