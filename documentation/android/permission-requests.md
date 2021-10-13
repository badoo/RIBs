# Permission requests

The mechanics of permission requesting are almost exactly the same as [launching Activities for result](launching-activities.md).


## PermissionRequester

`PermissionRequester` is an interface provided by the framework:

```kotlin
interface PermissionRequester :
    RequestCodeBasedEventStream<RequestPermissionsEvent> {

    fun checkPermissions(client: RequestCodeClient, permissions: Array<String>) : CheckPermissionsResult

    fun requestPermissions(client: RequestCodeClient, requestCode: Int, permissions: Array<String>)
    
    // ...
}
```

## Similarities with ActivityStarter

- Default implementation for `PermissionRequester` is also provided by `IntegrationPoint` and is available in `RibActivity` just the same, or through `node.integrationPoint`
- Request codes are also suggested to start from 1 
- `client: RequestCodeClient` is the same as discussed there
- Getting results also uses our [Minimal reactive API](../extras/minimal-reactive-api.md)
- Results are delivered back to the `Rib` that initiated the request, regardless of where it is in the tree


## Sample code

```kotlin
companion object {
    private const val REQUEST_CODE_CAMERA = 1
}

val client = TODO() // See description in Launching Activities

val permissionResult: Source<RequestPermissionsEvent> = permissionRequester.events(client)

// You can subscribe to it directly:
val cancellable = permissionResult.observe { /*...*/ }
// Or you can map it to other reactive types:
val disposable = permissionResult.rx2().subscribe { /*...*/ }

// Once you set up listening to results, fire the request:
permissionRequester.requestPermissions(
    client = client,
    requestCode = REQUEST_CODE_CAMERA,
    permissions = arrayOf(
        Manifest.permission.CAMERA
    )
)
```
Below you can check differences between the two flows where in the first one, the user grants the permissions and in the second denies them.

![](https://i.imgur.com/ykukrXP.gif)
![](https://i.imgur.com/yvppKEf.gif)
