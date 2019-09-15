package com.sorrowblue.koup

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

interface KoupProperty<T : Any> {
	val name: String
	val enumValue: KoupInfo<T>
	val isCommit: Boolean

	fun sharedPreferences(thisRef: Context, name: String): SharedPreferences =
		if (name.isNotBlank()) thisRef.getSharedPreferences(name, Context.MODE_PRIVATE)
		else PreferenceManager.getDefaultSharedPreferences(thisRef)

	@Suppress("UNCHECKED_CAST")
	fun putValue(preferences: SharedPreferences, value: T) {
		preferences.edit(commit = isCommit) {
			when (value) {
				is String -> putString(enumValue.name, value)
				is Int -> putInt(enumValue.name, value)
				is Boolean -> putBoolean(enumValue.name, value)
				is Float -> putFloat(enumValue.name, value)
				is Long -> putLong(enumValue.name, value)
				else -> if (value is Set<*> && value.all { it is String }) {
					putStringSet(enumValue.name, value as Set<String>)
				}

			}
		}
	}

	@Suppress("UNCHECKED_CAST")
	fun getValue(preferences: SharedPreferences): T = preferences.run {
		when (val def = enumValue.def) {
			is String -> getString(enumValue.name, def) ?: def
			is Int -> getInt(enumValue.name, def)
			is Boolean -> getBoolean(enumValue.name, def)
			is Float -> getFloat(enumValue.name, def)
			is Long -> getLong(enumValue.name, def)
			else -> if (def is Set<*> && def.all { it is String }) {
				getStringSet(enumValue.name, def as Set<String>)
			} else def
		} as T
	}
}
