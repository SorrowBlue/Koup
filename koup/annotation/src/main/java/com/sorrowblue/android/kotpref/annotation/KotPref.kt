package com.sorrowblue.android.kotpref.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class KotPref(val prefix: String = "", val nameSuffix: String = "")
