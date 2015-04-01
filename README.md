# Android MG Bootstrap
An opinionated bootstrap library for android projects that includes commonly used dependancies and some custom wrapper libraries.  Included utilities and dependencies are documented below.

## MG Anim

A animation helper library that provides common animation patterns in a simple API (fades, springs, movements, etc).

#### Configuration

- No configuration needed!

#### Usage

- Fade in/out views with a simple wrapper call - supports end states of View.VISIBLE,INVISIBLE and GONE.  Returns an observable that emits an event when the animation completes.

```java

// Basic fade in call.
MGAnimFade.setVisibility(someView, View.VISIBLE);

// Observe the fade completion callback.
MGAnimFade.setVisibility(fadeTestView, View.GONE)
        .takeUntil(getPaused())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(o -> {
            
            // Do some action.
        });
```

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

## MG Websocket

A wrapper library on top of an android websocket client library (http://java-websocket.org/) that layers in RxJava observables for events and supports more fined tuned configuration options.  The underlying websocket library is no longer maintained so future released may swap this out for a newer implementation.

#### Configuration

```java

// Initialize a web socket.
websocket =  MGWebsocket.create();

// Provide it a url - required.
websocket.getConfig().setUrl("ws://10.0.1.11:3001");

// Should auto reconnect - optional.
websocket.getConfig().setReconnect(true);

// Should messages buffer - optional.
websocket.getConfig().setBuffered(true);

// Time to reconnect - optional.
websocket.getConfig().setReconnectDelay(1000);
```

#### Usage

- Connecting and disconnecting: 

```java
// Open socket.
websocket.connect();

// Close socket.
websocket.close();
```

- Sending messages to the socket:

```java
// You can send a complex object to be serialized.
websocket.message(testComplexObject);

// Or just a string.
websocket.message("test");
```

- Getting websocket state:

```java
// Fetch the state.
MGWebsocket.STATE state = websocket.getState();

// Supported states.
public enum STATE {
        NOT_YET_CONNECTED, CONNECTING, OPEN, CLOSING, CLOSED
}
```

- Listening for websocket events (also available - getOnOpen, getOnClose and getOnError):

```java
// Listen for messages.
websocket.getOnMessage()
        .subscribe(message -> {
        
            MGLog.e("Message: " + message.getMessageJson());
        });
```
