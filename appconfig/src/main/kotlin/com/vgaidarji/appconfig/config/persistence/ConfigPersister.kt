package com.vgaidarji.appconfig.config.persistence

import android.content.Context
import android.content.SharedPreferences
import com.vgaidarji.appconfig.config.FeaturesConfig

private const val KEY_CONFIG = "config"

class ConfigPersister(val context: Context) {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(ConfigPersister::class.java.simpleName, Context.MODE_PRIVATE)
    }

    fun save(config: FeaturesConfig) {
        preferences
                .edit()
                .putString(KEY_CONFIG, config::class.java.name)
                .commit()
    }

    fun load(): FeaturesConfig {
        return Class.forName(
                preferences.getString(KEY_CONFIG, "")
        ).newInstance() as FeaturesConfig
    }
}
