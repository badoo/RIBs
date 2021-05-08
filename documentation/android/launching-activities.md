# Launching Activities

Even though RIBs is built around a single-Activity philosophy, sometimes it's unavoidable to interface with other Activities: 3rd party or legacy code.



## ActivityStarter

We suggest to not pass `Activity` or `Context` objects around as dependencies, but smaller classes for dedicated purposes.

`ActivityStarter` is an interface provided by the framework:

```kotlin
interface ActivityStarter : RequestCodeBasedEventStream<ActivityResultEvent> {

    fun startActivity(createIntent: Context.() -> Intent)

    fun startActivityForResult(
        client: RequestCodeClient,
        requestCode: Int,
        createIntent: Context.() -> Intent
    )
    
    // ...
}
```

It also has a default implementation provided by `IntegrationPoint`, so you get it right from the start, e.g. when creating your root:

```kotlin
class SomeActivity : RibActivity() {
    // ...
    
    override fun createRib(savedInstanceState: Bundle?) {
        // IntegrationPoint is available in RibActivity
        // You can use ActivityStarter to provide as a dependency in the tree
        val activityStarter = integrationPoint.activityStarter
    }
}
```

Alternatively, you can access it directly through the `node` in any plugin that implements the `NodeAware` interface. (See [Plugins](../basics/plugins.md) for details):

```kotlin
val activityStarter = node.integrationPoint.activityStarter
```



## Launch an Activity


```kotlin
private fun launchOtherActivity() {
    activityStarter.startActivity() {
        // implicit »this« from lambda is Context, let's return an Intent
        Intent(this, OtherActivity::class.java)
            .putExtra(OtherActivity.KEY_INCOMING, "Some data")
    }
}
```



## Launch an Activity for result

**1. Define your request code(s)**


```kotlin
companion object {
    private const val REQUEST_CODE_OTHER_ACTIVITY = 1
}
```

**Don't**: You don't need to come up with a unique number!

**Do**: You can freely start from 1 in every `Rib`. All `Rib` has its own identifier, which will be automatically part of the actual request code forged under the hood – uniqueness is taken care of for you.

The point is that wherever you are in the application's tree structure, the framework guarantees that you get the results delivered to the same Rib that fired the request.


**2. Set up reacting on result**

`ActivityStarter` uses our [Minimal reactive API](../extras/minimal-reactive-api.md) that you can subscribe to:

We'll talk about what `client` is in a second.

```kotlin
val client = TODO()

val activityResult: Source<ActivityResultEvent> = activityStarter.events(client)

// You can subscribe to it directly:
val cancellable = activityResult.observe { /*...*/ }
// Or you can map it to other reactive types:
val disposable = activityResult.rx2().subscribe { /*...*/ }
```

**3. Launch the Activity for result**

Once listening to results is in place, it's time to fire the request.

```kotlin
val client = TODO() // It should be the same instance as above

private fun launchOtherActivityForResult() {
    activityStarter.startActivityForResult(client, REQUEST_CODE_OTHER_ACTIVITY) {
        // implicit »this« from lambda is Context, let's return an Intent
        Intent(this, OtherActivity::class.java)
            .putExtra(OtherActivity.KEY_INCOMING, "Some data")
    }
}
```


## Client

In the above example we referred to `client`. It should be any object implementing this simple interface to ensure delivering the results:

```kotlin
interface RequestCodeClient {

    val requestCodeClientId: String
}
```

The value of `requestCodeClientId` has to be a globally unique id. 

Some carefree options how to provide it:

1. When extending the `Interactor` class (provided by the framework) to implement business logic, it already implements this interface, so you can just call use `this` in place of the `client`
2. Every `Rib` gets an instance of `BuildParam` upon creation in the `Builder`. `BuildParam` contains an `identifier` field, which is guaranteed to be unique by the framework. You can call `.toString()` on it and use its value. (Note: `Interactor` uses this same exact mechanics)




