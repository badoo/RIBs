# Jetpack Compose support

RIBs support Jetpack Compose!

## Dependencies

All artifacts should be available from jitpack with a `-compose` suffix too.

See [Module dependencies](../setup/deps.md)


## Building the project locally

If you'd like to play around in this repo, add this flag in your `local.properties`, then hit gradle sync:

```
useCompose=true
```

What you get:
- Some new modules enabled (you can check `settings.gradle` to see which)
- Compose-based implementation of `RibView`: `ComposeRibView`
- Demo in `sandbox` app
