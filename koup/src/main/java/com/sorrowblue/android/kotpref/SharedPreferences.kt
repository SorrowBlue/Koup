package com.sorrowblue.android.kotpref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

fun Context.sharedPreferences(name: String? = null): SharedPreferences =
	name?.let { getSharedPreferences(it, Context.MODE_PRIVATE) }
		?: PreferenceManager.getDefaultSharedPreferences(this)

fun Fragment.sharedPreferences(name: String? = null): SharedPreferences =
	requireContext().sharedPreferences(name)

@Suppress("UNCHECKED_CAST")
internal fun <T : Any> SharedPreferences.putValue(
	key: String,
	value: T,
	isCommit: Boolean = false
): Unit = edit(commit = isCommit) {
	when (value) {
		is String -> putString(key, value)
		is Int -> putInt(key, value)
		is Boolean -> putBoolean(key, value)
		is Float -> putFloat(key, value)
		is Long -> putLong(key, value)
		else -> if (value is Set<*>) {
			if (value.all { it is String }) putStringSet(key, value as Set<String>)
			else throw RuntimeException("${value::class.java.simpleName} type is not supported.")
		} else {
			throw RuntimeException("${value::class.java.simpleName} type is not supported.")
		}
	}
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> SharedPreferences.getValue(name: String, default: T): T = when (default) {
	is String -> getString(name, default) ?: default
	is Int -> getInt(name, default)
	is Boolean -> getBoolean(name, default)
	is Float -> getFloat(name, default)
	is Long -> getLong(name, default)
	else -> if (default is Set<*>) {
		if (default.all { it is String }) getStringSet(name, default as Set<String>)
		else throw RuntimeException("${default::class.java.simpleName} type is not supported.")
	} else {
		throw RuntimeException("${default::class.java.simpleName} type is not supported.")
	}
} as T
