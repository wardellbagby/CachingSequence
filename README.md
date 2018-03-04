# CachingSequence

A Kotlin library that provides a CachingSequence.

## CachingSequence?

A CachingSequence is a subscriptable sequence that caches the values it has emitted. You can retrieve previous values by calling `.get` or using `[index]` on the caching sequence.

### Uh.....why?

So.... Sequences are great! A lazy way to calculate and emit items? Yes, please! However, sometimes it's useful to use a sequence like an array without being forced to reiterate over the source.

## Getting Started

Simply check out this project and import it into Android Studio! Gradle and Android Studio should take care of the rest!

### As A Dependency

In your root `build.gradle`:

```
allprojects {
        repositories {
                ...
                maven { url 'https://jitpack.io' }
        }
}
```

In your app `build.gradle`:

```
dependencies {
        ...
        implementation 'com.github.wardellbagby:CachingSequence:0.1.1'
}
```

### Using

```kotlin
    //Creates a CachingSequence from an IntRange.
    val cachingSequence = (0..1000).asCachingSequence()
    //Iterates to the 100th value and prints that out, caching as it goes.
    print(cachingSequence[100]) //prints 100
    print(cachingSequence[10]) //prints 10 from its internal cache.
```

### Building

You can build this lib using:

```
./gradlew build
```

## Running the tests

Unit tests can be run with:

```
./gradlew check
```

### Code Style

This app uses [ktlint](https://ktlint.github.io/) for enforcing style constraints. Most ktlint errors can be fixed by running

```
./gradlew ktlintFormat
```

but not all. ktlint will output to console any errors it encounters.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for the process for submitting pull requests to this project.

## License

This project is licensed under the LGPL-3.0 License - see the [LICENSE](LICENSE) file for details
