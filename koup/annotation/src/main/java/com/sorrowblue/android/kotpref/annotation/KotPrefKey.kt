package com.sorrowblue.android.kotpref.annotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class KotPrefKey(val key: String = "", val resId: Int = 0)
