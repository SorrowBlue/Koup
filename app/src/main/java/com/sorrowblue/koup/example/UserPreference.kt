package com.sorrowblue.koup.example

import com.sorrowblue.android.kotpref.annotation.KotPref
import com.sorrowblue.android.kotpref.annotation.KotPrefKey

@KotPref("User")
object UserPreference {

	const val STRING = "NO_NAME"

	@KotPrefKey(key = "ID_INT")
	const val INT: Int = 2147483647

	@KotPrefKey(resId = R.string.preference_key_long)
	const val LONG: Long = 9223372036854775807

	const val FLOAT = 1.4E-45

	@KotPrefKey(key = "ID_DOUBLE")
	const val DOUBLE: Double = 4.9E-324

	@KotPrefKey(resId = R.string.preference_key_is_auth)
	const val IS_AUTH = false


	val SET = setOf("adw", "adw", "Qawdwdwa")
}
