# Module dependencies

Add jitpack to your root `build.gradle`:
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

### Jetpack Compose support

All artifacts should be available both with and without a `-compose` suffix, e.g.:

```groovy
// non-compose variant"
implementation 'com.github.badoo.RIBs:rib-{###}:{latest-version}'
// OR
// compose variant:
implementation 'com.github.badoo.RIBs:rib-{###}-compose:{latest-version}' 
```

You only need one of them though, based on whether you need compose support or not. 
Make sure to refer to all dependencies consistently.

### Base
```groovy
implementation 'com.github.badoo.RIBs:rib-base:{latest-version}'
```

### Test helpers
Contains utilities to test your components in isolation:
```groovy
implementation 'com.github.badoo.RIBs:rib-base-test:{latest-version}'
implementation 'com.github.badoo.RIBs:rib-base-test-activity:{latest-version}'
```

### Rx
Contains Rx implementations of the minimal reactive APIs and Rx based workflow helper classes:
```groovy
implementation 'com.github.badoo.RIBs:rib-rx2:{latest-version}'
```

### Debug utils
Handy utils: tree printer, logger, debug drawer, memory leak detector:
```groovy
implementation 'com.github.badoo.RIBs:rib-debug-utils:{latest-version}'
```

### Portals (experimental)
Base deps:
```groovy
implementation 'com.github.badoo.RIBs:rib-portal:{latest-version}'
```
Rx (optional):
```groovy
implementation 'com.github.badoo.RIBs:rib-portal-rx:{latest-version}'
```

### Recyclerview integration (experimental)
```groovy
implementation 'com.github.badoo.RIBs:rib-recyclerview:{latest-version}'
```


