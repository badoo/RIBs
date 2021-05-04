# Minimal reactive API

We find that some APIs (like reacting to Android's permission request result, or Activity result) are easier to use in a reactive way if we wanted to give the freedom to catch those events in any class you like (compared to forcing you to extend some specific class and override a method).

On the other hand, we wanted to remain unopinionated about what reactive libraries approaches you want to use, and not couple the library with any specific one.

For these reasons, RIBs comes only with a minimal provided implementation that you can find in the `com.badoo.ribs.minimal.reactive` package, which is part of our `rib-base` dependency.

This package provides for example a simple observable source:

```kotlin
interface Source<out T> {
    fun observe(callback: (T) -> Unit): Cancellable
}
```

An emitter:

```kotlin
interface Emitter<in T> {
    fun emit(value: T)
}
```

A relay:
```kotlin
class Relay<T> : Source<T>, Emitter<T>
```

Cancellable (~Disposable):

```kotlin
interface Cancellable {
    fun cancel()
}
```

And some other basic tools and operators that should look familiar if you've ever used other dedicated libraries.

Our goal was not to provide a full blown reactive implementation - you can do that better with the tool of your choice - just to have a bare minimum starting point that you can convert further if you wish.


## Adapters

You can find an `.rx2()` adapter method in:

```groovy
implementation 'com.github.badoo.RIBs:rib-rx2:{latest-version}'
```

It converts `Source` to `Observable`:

```kotlin
fun <T> Source<T>.rx2(): Observable<T>
```
