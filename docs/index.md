![testawdaw](image/logo.svg)

*Koup* is the `Kotlin` extension of `Android SharedPreference`.

* Compatibility: Supports types that can be used in Shared Preferences. All types can be used by serializing.
* Modern: Koup is Kotlin-first and uses modern libraries including AndroidX Preferences.

Koup is an acronym for: **Ko**tlin **U**pper **P**references.

# Download

Koup is not available on `jcenter()`

```kotlin
repositories {
    maven(url = "http://sorrowblue.com/Koup/repository")
}

dependencies {
    implementation("com.sorrowblue.koup:koup:0.1.3")
    kapt("com.sorrowblue.koup:compiler:0.1.3")
}
```

# Quick Start

```kotlin
sealed class UserInfo<T : Any>(def: T, key: String? = null) : KoupInfo<T>(def,key) {
    override val prefix = "USER"

    // Key is "USER_ID"
    object USER_ID :UserInfo<Int>(123456789)
    // Key is "USER_NAME"
    object USER_NAME :UserInfo<Striing>("Koup user")
}

class MainActivity : AppCompatActivity() {
    private var useId by koup(UserInfo.USER_ID, commit = true)
    private var userName by koup(UserInfo.USER_NAME, commit = false)
}
```

Or the annotation `@SharedPreferenceKoup` is easy

```kotlin
@SharedPreferenceKoup
object UserInfo : IKoupInfo {
    override val prefix = "USER"

    const val USER_ID = 123456789
}
```

↓↓↓↓↓**Generate**↓↓↓↓

```kotlin
sealed class Koup_UserInfo<T : Any>(def: T, key: String? = null) : KoupInfo<T>(def,key) {
    override val prefix = "USER"

    // Key is "USER_ID"
    object USER_ID :Koup_UserInfo<Int>(123456789)
    // Key is "USER_NAME"
    object USER_NAME :Koup_UserInfo<Striing>("Koup user")
}
```
