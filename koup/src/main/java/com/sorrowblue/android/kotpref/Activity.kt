@file:Suppress("unused")

package com.sorrowblue.android.kotpref

import android.app.Activity
import kotlin.reflect.KProperty

class ActivityKotPrefKeyDelegate<T : Any> internal constructor(
	kotPrefKey: KotPrefKey<T>,
	isCommit: Boolean,
	name: String?
) : KotPrefKeyDelegate<Activity, T>(kotPrefKey, isCommit, name) {
	override fun getValue(thisRef: Activity, property: KProperty<*>): T =
		thisRef.getSharedPreference(kotPrefKey, name)

	override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) =
		thisRef.putSharedPreference(kotPrefKey, value, isCommit, name)
}

fun <T : Any> Activity.sharedPreference(
	kotPref: KotPrefKey<T>,
	isCommit: Boolean = false,
	name: String? = null
) = ActivityKotPrefKeyDelegate(kotPref, isCommit, name)

fun <T : Any> Activity.getSharedPreference(kotPref: KotPrefKey<T>, name: String? = null) =
	sharedPreferences(name).getValue(kotPref.getName(this), kotPref.default)

fun <T : Any> Activity.putSharedPreference(
	kotPref: KotPrefKey<T>,
	value: T,
	isCommit: Boolean = false,
	name: String? = null
) = sharedPreferences(name).putValue(kotPref.getName(this), value, isCommit)
