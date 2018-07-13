package com.vgaidarji.appconfig.config

import com.vgaidarji.appconfig.feature.Feature

abstract class FeaturesConfig {
    abstract fun features(): List<Feature>
}
