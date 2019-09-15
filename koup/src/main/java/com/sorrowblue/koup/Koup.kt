package com.sorrowblue.koup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ActivityKoup<T : Any> internal constructor(
	override val name: String,
	override val enumValue: KoupInfo<T>,
	override val isCommit: Boolean
) : ReadWriteProperty<FragmentActivity, T>, KoupProperty<T> {

	override fun setValue(thisRef: FragmentActivity, property: KProperty<*>, value: T) =
		putValue(sharedPreferences(thisRef, name), value)

	override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T =
		getValue(sharedPreferences(thisRef, name))
}

class FragmentKoup<T : Any> internal constructor(
	override val name: String,
	override val enumValue: KoupInfo<T>,
	override val isCommit: Boolean
) : ReadWriteProperty<Fragment, T>, KoupProperty<T> {

	override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
		putValue(sharedPreferences(thisRef.requireContext(), name), value)
	}

	override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
		getValue(sharedPreferences(thisRef.requireContext(), name))
}
