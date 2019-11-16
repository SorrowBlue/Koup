@file:Suppress("unused")

package com.sorrowblue.android.kotpref

import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

class FragmentKotPrefKeyDelegate<T : Any> internal constructor(
	kotPrefKey: KotPrefKey<T>,
	isCommit: Boolean,
	name: String?
) : KotPrefKeyDelegate<Fragment, T>(kotPrefKey, isCommit, name) {
	override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
		thisRef.getSharedPreference(kotPrefKey, name)

	override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) =
		thisRef.putSharedPreference(kotPrefKey, value, isCommit, name)
}

fun <T : Any> Fragment.sharedPreference(
	kotPref: KotPrefKey<T>,
	isCommit: Boolean = false,
	name: String? = null
) = FragmentKotPrefKeyDelegate(kotPref, isCommit, name)

fun <T : Any> Fragment.getSharedPreference(kotPref: KotPrefKey<T>, name: String? = null) =
	sharedPreferences(name).getValue(kotPref.getName(requireContext()), kotPref.default)

fun <T : Any> Fragment.putSharedPreference(
	kotPref: KotPrefKey<T>,
	value: T,
	isCommit: Boolean = false,
	name: String? = null
) = sharedPreferences(name).putValue(kotPref.getName(requireContext()), value, isCommit)
