package com.sorrowblue.koup

import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FragmentKoup<T : Any> internal constructor(
	override val name: String,
	override val koupInfo: KoupInfo<T>,
	override val isCommit: Boolean
) : ReadWriteProperty<Fragment, T>, KoupProperty<T> {
	override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
		thisRef.getKoup(koupInfo, name)

	override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) =
		thisRef.putKoup(koupInfo, value, isCommit, name)
}

fun <T : Any> Fragment.koup(enumValue: KoupInfo<T>, isCommit: Boolean = false, name: String = "") =
	FragmentKoup(name, enumValue, isCommit)

fun <T : Any> Fragment.getKoup(koupInfo: KoupInfo<T>, name: String = "") =
	sharedPreferences(this.requireContext(), name).getValue(koupInfo.name, koupInfo.def)

fun <T : Any> Fragment.putKoup(
	koupInfo: KoupInfo<T>, value: T,
	isCommit: Boolean = false, name: String = ""
) = sharedPreferences(this.requireContext(), name).putValue(koupInfo.name, value, isCommit)
