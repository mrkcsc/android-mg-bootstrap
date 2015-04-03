## 1.4.1 - 2015-04-02

- Added an RXJava extnsion class to MGPreference.

## 1.3.7 - 2015-03-31

- Bug fixes.

## 1.3.5 - 2015-03-31

- Initial working implementation of MGWebsocket client.

## 1.3.4 - 2015-03-31

- Bug fix for MGKeyboard.

## 1.3.3 - 2015-03-30

- Paused observable now retrieved with getPaused().  Transition delay observable retrieved with getTransitions.getDelay()

## 1.3.2 - 2015-03-30

- Allow setting null values in MGPreference.

## 1.3.0 - 2015-03-29

- Added MGKeyboard utility (see documentation).

## 1.2.8 - 2015-03-28

- Fixed a bug in MGPreference.

## 1.2.7 - 2015-03-28

- Libary users are now required to initialize utilities themselves (see documentation).  Renamed MGLifecycleApplicationCallbacks to MGLifecycleCallbacks.

## 1.2.6 - 2015-03-28

- Improved MGPreference utility to support persisting any and all objects.

## 1.2.5 - 2015-03-26

- Added a convenience class for Application lifecycle callbacks: MGLifecycleApplicationCallbacks.

## 1.2.4 - 2015-03-24

- Added some additional error serialization to MGRestClientError.

## 1.2.3 - 2015-03-24

- Added a Anim Fade utility for setting view visibility with animation (see docs).

## 1.2.2 - 2015-03-24

- Added support for all activity transitions to be played in reverse, eg:

```java
getTransitions.setReversed(true);
```

## 1.2.1 - 2015-03-23

- Updated horizontal/vertical transitions for pop.

## 1.2.0 - 2015-03-15

- Added logger library.

## 1.1.0 - 2015-03-15

- Bumped library support to target Lollipop SDK.  Includes partial implementation of websocket library.

## 1.0.0 - 2015-03-06

- Initial commit of skeleton project.
