package com.sorrowblue.koup.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sorrowblue.koup.example.preferences.Koup_UserPreference
import com.sorrowblue.koup.example.ui.main.MainFragment
import com.sorrowblue.koup.koup

class MainActivity : AppCompatActivity() {

	private var koup by koup("preferenceName", Koup_UserPreference.ID, true)
	private var koaup by koup( Koup_UserPreference.ID2, true)

	private var userId by koup(Koup_UserPreference.ID, true)

	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
		userId = 123456789
		koup = 1006
		koaup = 123456789
		if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
