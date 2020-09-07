# Module dependencies

Add jitpack to your root `build.gradle`:
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

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
Contains Rx implementation of workflow node + Rx Disposables plugin:
```groovy
implementation 'com.github.badoo.RIBs:rib-rx:{latest-version}'
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


