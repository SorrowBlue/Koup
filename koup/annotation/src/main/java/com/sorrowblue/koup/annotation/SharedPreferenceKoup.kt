package com.sorrowblue.koup.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class KoupSharedPreference(val prefix: String = "")

@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class KoupKey(val key: String = "")
