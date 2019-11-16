@file:Suppress("unused")

package com.sorrowblue.koup.annotation

//@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
//@Retention(AnnotationRetention.SOURCE)
//@MustBeDocumented
//annotation class KoupSharedPreference(val prefix: String = "")

//@Retention(AnnotationRetention.SOURCE)
//@MustBeDocumented
//annotation class KoupKey(val key: String = "")
//
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class KotPref(val prefix: String = "", val nameSuffix: String = "")


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class KotPrefKeyString(val key: String = "")


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class KotPrefKeyResId(val key: Int = -1)


//@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
//@Retention(AnnotationRetention.SOURCE)
//@MustBeDocumented
//annotation class KotSharedPref(val prefix: String = "")
