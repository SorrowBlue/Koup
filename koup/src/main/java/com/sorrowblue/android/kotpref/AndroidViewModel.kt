@file:Suppress("unused")

package com.sorrowblue.android.kotpref

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlin.reflect.KProperty

class AndroidViewModelKotPrefKeyDelegate<T : Any> internal constructor(
	kotPrefKey: KotPrefKey<T>,
	isCommit: Boolean,
	name: String?
) : KotPrefKeyDelegate<AndroidViewModel, T>(kotPrefKey, isCommit, name) {
	override fun getValue(thisRef: AndroidViewModel, property: KProperty<*>): T =
		thisRef.getSharedPreference(kotPrefKey, name)

	override fun setValue(thisRef: AndroidViewModel, property: KProperty<*>, value: T) =
		thisRef.putSharedPreference(kotPrefKey, value, isCommit, name)
}

fun <T : Any> AndroidViewModel.sharedPreference(
	kotPref: KotPrefKey<T>,
	isCommit: Boolean = false,
	name: String? = null
): AndroidViewModelKotPrefKeyDelegate<T> = AndroidViewModelKotPrefKeyDelegate(kotPref, isCommit, name)

fun <T : Any> AndroidViewModel.getSharedPreference(
	kotPref: KotPrefKey<T>,
	name: String? = null
): T = getApplication<Application>().run {
	sharedPreferences(name).getValue(kotPref.getName(this), kotPref.default)
}

fun <T : Any> AndroidViewModel.putSharedPreference(
	kotPref: KotPrefKey<T>,
	value: T,
	isCommit: Boolean = false,
	name: String? = null
): Unit = getApplication<Application>().run {
	sharedPreferences(name).putValue(kotPref.getName(this), value, isCommit)
}
