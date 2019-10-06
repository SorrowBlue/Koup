package com.sorrowblue.koup.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sorrowblue.koup.example.ui.main.MainFragment
import com.sorrowblue.koup.getKoup
import com.sorrowblue.koup.koup
import com.sorrowblue.koup.putKoup

class MainActivity : AppCompatActivity() {

	private var userID by koup(Koup_UserPreference.ID, true,"preferenceName")
	private var userIDd by koup( Koup_UserPreference.ID_D, true)

	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
		userID = 1006
		userIDd = 123456789.0
		putKoup(Koup_UserPreference.ID, 114514)
		if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
