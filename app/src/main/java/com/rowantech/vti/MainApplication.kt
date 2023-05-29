package com.rowantech.vti

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.databinding.library.BuildConfig
import com.rowantech.vti.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class MainApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector
    fun setPreference(context: Context?, key: String, value: String) {
        if (context != null) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun getStringPref(context: Context?, key: String): String? {
        var preferences: SharedPreferences? = null
        if (context != null)
            preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences!!.getString(key, "")
    }

    fun removePref(context: Context?, key: String) {
        val preferences = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        if (preferences != null) {
            preferences.edit().remove(key).apply()
        }
    }
}