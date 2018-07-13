package com.vgaidarji.appconfig.ui

import com.vgaidarji.appconfig.config.FeaturesConfig

interface ConfigSelectedListener {
    fun onConfigSelected(config: FeaturesConfig)
}
