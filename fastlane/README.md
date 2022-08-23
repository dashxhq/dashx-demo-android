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

### android internal

```sh
[bundle exec] fastlane android internal
```

Deploy a internal version to the Google Play

### android increment_version_code

```sh
[bundle exec] fastlane android increment_version_code
```

Responsible for fetching version code from play console and incrementing version code.

### android internal_push

```sh
[bundle exec] fastlane android internal_push
```

Deploy a internal version with auto increment of version code

### android production

```sh
[bundle exec] fastlane android production
```

Deploy a production version to the Google Play

### android production_push

```sh
[bundle exec] fastlane android production_push
```

Deploy a production version with auto increment of version code

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
