package com.sorrowblue.koup

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager


internal fun sharedPreferences(thisRef: Context, name: String = ""): SharedPreferences =
	if (name.isNotBlank()) thisRef.getSharedPreferences(name, Context.MODE_PRIVATE)
	else PreferenceManager.getDefaultSharedPreferences(thisRef)


@Suppress("UNCHECKED_CAST")
internal fun <T : Any> SharedPreferences.putValue(
	key: String,
	value: T,
	isCommit: Boolean = false
) {
	edit(commit = isCommit) {
		when (value) {
			is String -> putString(key, value)
			is Int -> putInt(key, value)
			is Boolean -> putBoolean(key, value)
			is Float -> putFloat(key, value)
			is Long -> putLong(key, value)
			else -> if (value is Set<*> && value.all { it is String }) putStringSet(
				key,
				value as Set<String>
			)
		}
	}
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> SharedPreferences.getValue(name: String, default: T) = when (default) {
	is String -> getString(name, default) ?: default
	is Int -> getInt(name, default)
	is Boolean -> getBoolean(name, default)
	is Float -> getFloat(name, default)
	is Long -> getLong(name, default)
	else -> if (default is Set<*> && default.all { it is String }) {
		getStringSet(name, default as Set<String>)
	} else default
} as T
