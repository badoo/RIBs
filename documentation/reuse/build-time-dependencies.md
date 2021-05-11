# Build-time dependencies

You can face situations where not all dependencies can be provided compile-time, e.g. the value to pass for the dependency is a result of a network request. 

RIBs support build-time dependencies (where build refers to calling ```.build()``` on your ```Builder```).

## Example

Main interface:

```kotlin
interface Profile : Rib {

    interface Dependency

    data class Params(
        val profileId: Int // <--
    )
}
```

Builder:

```kotlin
class ProfileBuilder(
    private val dependency: Profile.Dependency
) : Builder<Profile.Params, Profile>() { // <-- 
    
    override fun build(buildParams: BuildParams<Profile.Params>): Profile { // <--
        val profileId: Int = buildParams.payload.profileId // TODO use it
        
        return TODO()
    }
}
```

Parent defines a parameterised routing for it:

```kotlin
sealed class Configuration : Parcelable {
    @Parcelize
    data class ShowProfile(
        val profileId: Int
    ) : Configuration()
}
```

Parent resolving it:

```kotlin
internal class ParentRouter(
    // ...remainder omitted...
): Router<Configuration>(
    // ...remainder omitted...
) {
    
    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (val configuration = routing.configuration) {
            is ShowProfile -> child { profile.build(it, Profile.Params(configuration.profileId)) }
        }
}
```


Parent business logic triggering a routing change:

```kotlin
internal class ParentInteractor() {

    fun someMethod {
        val profile = TODO()
        backStack.push(ShowProfile(profile.id))
    }
}
```

The full chain of events:

1. Business logic grabs a piece of data required for building the child
2. Business logic triggers a routing change, including this data in the routing configuration
3. -- state restoration point --
4. Router resolves the routing configuration and grabs the packed piece of data
5. The child's Builder is invoked with the piece of data as payload
6. The child's Builder grabs the data from the payload and uses it as it needs


## State restoration

Since routing configurations are ```Parcelable```, passed in data are also persisted and restored for free.


## Simple, immutable data only

However tempting it might seem, don't (ab)use configurations to store complex or mutable data. Store only the minimal amount of information required to represent routing.

For example: don't store the whole profile object, store only its id; use a local repo / cache to restore the full object based on the id when needed.










