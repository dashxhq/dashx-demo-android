fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android buildDebug

```sh
[bundle exec] fastlane android buildDebug
```

Builds the debug code

### android increment_build_number

```sh
[bundle exec] fastlane android increment_build_number
```

Increment Build number

### android buildRelease

```sh
[bundle exec] fastlane android buildRelease
```

Builds the release code

### android test

```sh
[bundle exec] fastlane android test
```

Runs all the tests

### android internal

```sh
[bundle exec] fastlane android internal
```

Submit a new Internal Build to Play Store

### android internal_push

```sh
[bundle exec] fastlane android internal_push
```

Publish to Internal Testing

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
