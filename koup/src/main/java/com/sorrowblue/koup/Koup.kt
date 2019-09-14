package com.sorrowblue.preflin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty



@Suppress("UNCHECKED_CAST")
class ActivityPreflin<T : Any>(
    override val enumValue: PreflinInfo<T>,
    override val isCommit: Boolean = false
) : ReadWriteProperty<FragmentActivity, T>, PreflinProperty<T> {

    override fun setValue(thisRef: FragmentActivity, property: KProperty<*>, value: T) =
        putValue(PreferenceManager.getDefaultSharedPreferences(thisRef), value)

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T =
        getValue(PreferenceManager.getDefaultSharedPreferences(thisRef))
}

@Suppress("UNCHECKED_CAST")
class FragmentPreflin<T : Any>(
    override val enumValue: PreflinInfo<T>,
    override val isCommit: Boolean = false
) : ReadWriteProperty<Fragment, T>, PreflinProperty<T> {

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        putValue(PreferenceManager.getDefaultSharedPreferences(thisRef.requireContext()), value)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        getValue(PreferenceManager.getDefaultSharedPreferences(thisRef.requireContext()))
}
