package com.sorrowblue.android.kotpref

import android.content.Context
import kotlin.properties.ReadWriteProperty

abstract class KotPrefKey<T : Any>(
	val default: T,
	val key: String? = null,
	val resId: Int? = null
) {
	abstract val prefix: String?
	fun getName(context: Context): String {
		val key = resId?.let(context::getString)
			?: key ?: this::class.java.simpleName
		return prefix?.let { "$it-$key" } ?: key
	}
}

abstract class KotPrefKeyDelegate<R : Any, T : Any>(
	val kotPrefKey: KotPrefKey<T>,
	val isCommit: Boolean,
	val name: String?
) : ReadWriteProperty<R, T>
