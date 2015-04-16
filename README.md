# Android MG Bootstrap
An opinionated bootstrap library for android projects that includes commonly used dependancies and some custom wrapper libraries.  Included utilities and dependencies are documented below.

## Installation

Installation

## Usage

See wiki

## MG Delay

A simple helper wrapper class for RxJava to create simple delay and looping observables.

#### Usage

```java

// Simple five second delay,
MGDelay.delay(5000).subscribe(aVoid -> { });

// Loop on an interval.
MGDelay.delay(1000, true).subscribe(aVoid -> {});

// Start looping immediatly.
MGDelay.delay(1000, true, true).subscribe(aVoid -> {});
```

## MG Anim

A animation helper library that provides common animation patterns in a simple API (fades, springs, movements, etc).

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
