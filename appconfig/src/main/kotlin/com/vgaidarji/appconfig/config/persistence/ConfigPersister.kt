package com.vgaidarji.appconfig.config.persistence

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.vgaidarji.appconfig.config.FeaturesConfig
import com.vgaidarji.appconfig.feature.Feature

private const val KEY_CONFIG = "config"

class ConfigPersister(private val context: Context) {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(ConfigPersister::class.java.simpleName, Context.MODE_PRIVATE)
    }

    @SuppressLint("ApplySharedPref")
    fun save(config: FeaturesConfig) {
        preferences
                .edit()
                .putString(KEY_CONFIG, config::class.java.name)
                .commit()
    }

    fun load(): FeaturesConfig {
        return try {
            Class.forName(
                preferences.getString(KEY_CONFIG, "")
            ).newInstance() as FeaturesConfig
        } catch (e: ClassNotFoundException) {
            object : FeaturesConfig() {
                override fun features() = emptyList<Feature>()
            }
        }
    }
}
