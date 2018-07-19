package com.vgaidarji.featureflags

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.jakewharton.processphoenix.ProcessPhoenix
import com.vgaidarji.appconfig.config.Config1
import com.vgaidarji.appconfig.config.Config2
import com.vgaidarji.appconfig.config.FeaturesConfig
import com.vgaidarji.appconfig.config.persistence.ConfigPersister
import com.vgaidarji.appconfig.extension.bindView
import com.vgaidarji.appconfig.feature.ConfigurableFeatures
import com.vgaidarji.appconfig.ui.ConfigSelectedListener
import com.vgaidarji.appconfig.ui.ConfigSelectionDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConfigSelectedListener {

    private val buttonSwitchConfig: Button by bindView(R.id.button_switch_config)

    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        message.setText(R.string.title_home)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_dashboard -> {
                        message.setText(R.string.title_dashboard)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_notifications -> {
                        message.setText(R.string.title_notifications)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
        updateUiAccordingToConfig()
    }

    override fun onConfigSelected(config: FeaturesConfig) {
        ProcessPhoenix.triggerRebirth(this)
    }

    private fun initUi() {
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        buttonSwitchConfig.setOnClickListener {
            ConfigSelectionDialog
                    .newInstance(listOf(Config1(), Config2()))
                    .show(supportFragmentManager, "config_selection_dialog")
        }
    }

    private fun updateUiAccordingToConfig() {
        ConfigPersister(this).load().also {
            Log.d(MainActivity::class.java.name, "Loaded ${it::class.java.simpleName}")
            val features = ConfigurableFeatures(it)
            if (!features.hasBottomNavigation()) {
                navigation.visibility = View.GONE
            }
        }
    }
}
