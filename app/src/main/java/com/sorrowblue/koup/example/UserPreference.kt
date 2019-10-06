package com.sorrowblue.koup.example

import com.sorrowblue.koup.annotation.KoupKey
import com.sorrowblue.koup.annotation.KoupSharedPreference

@KoupSharedPreference("User")
object UserPreference {

	@KoupKey(key = "TEST_ID")
	const val ID: Int = -1
	const val ID_D: Double = 35.584
	const val ID_F = 3.0f
	const val IS_AUTH = true
	@KoupKey(key = "USER_NAME")
	const val NAME = "NO_NAME"
	val CODES = setOf("adw", "adw", "Qawdwdwa")
}
