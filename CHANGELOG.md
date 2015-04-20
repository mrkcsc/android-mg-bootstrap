## 1.7.7 - 2015-04-20

- Support for `MGPreference<Map<String, List<Object>>` where the list of object is sometimes empty (nulls will still likely crash.

## 1.7.6 - 2015-04-20

- Fixed a faulty assumption in MGBackgrounded - that when the utility is initialized we are guaranteed to not be in the background. This can be proven false in the case of GCM broadcast receivers. Now provide a default value of true to backgrounded.

## 1.7.5 - 2015-04-17

- Added a version code cache breaker to `MGPreference`

## 1.7.3 - 2015-04-16

- Bug fixes in `MGPreferenceRx` and `MGWebsocket`

## 1.7.1 - 2015-04-16

- Can provide objects as the heartbeat message now.

## 1.7.0 - 2015-04-16

- Bug fix for reconnecting in `MGWebsocket`

## 1.6.9 - 2015-04-16

- `MGWebsocket` api changes (see docs).

## 1.6.8 - 2015-04-15

- `MGWebsocket` can now (insecurely) connect to WSS if the url provided is of that format.

## 1.6.7 - 2015-04-15

- MGPreference now supports fully nested maps and lists, eg:  `MGPreference<Map<Integer, List<SomeObject>>` - will not deserialize other types of generics such as `SomeObject<T>` only collections and maps.

## 1.6.6 - 2015-04-14

- Api and behavior changes for MGPreferenceRx.  By default all PrefRx object will emit a null as their first initial value if no other value is present in the cache or previously set by the user.

- Additionally a new overload has been added to the create call to allow to user to provide a custom default value.  The cache parameter that used to be the overloaded 2nd parameter of the create call has now been moved to the third parameter overload (requiring a default value to be set for its use (can be set to null as the default)).

- This change essentially eliminates the need for getBlocking as a subscription will be guaranteed to at least immediately return the default value, however for convenience it has been left in place.  However the "defaultValue" overload has been removed as it is now covered in the create call.

## 1.6.4 - 2015-04-11

- Serialization bug fixes on MGPreference.

## 1.6.3 - 2015-04-11

- Added Transition.NONE animation for activity transitions.

## 1.6.0 - 2015-04-11

- Added an updated websocket to the core with better SSL support.

## 1.5.4 - 2015-04-09

- Added a recycler view holder helper class.

## 1.5.3 - 2015-04-07

- Added support for Maps and Collections of complex objects in MGPreference.

## 1.5.1 - 2015-04-06

- Added an activity fade transition, move transitions enum into enclosing class.

## 1.5.0 - 2015-04-06

- Revamped backgrounded to use internal lifecycle callbacks and utilize MGPreferenceRx.  MGPreferenceRx internally uses a BehaviorSubject now.  Lifecycle activity/fragment classes now have access to a onCreateOrResume lifecycle method.

## 1.4.8 - 2015-04-06

- Added some additional method to MGPreferenceRx (see documentation).

## 1.4.5 - 2015-04-03

- Updated MGPreferenceRx with a cached option overload.

## 1.4.3 - 2015-04-03

- Added documentation for MGDelay.

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
