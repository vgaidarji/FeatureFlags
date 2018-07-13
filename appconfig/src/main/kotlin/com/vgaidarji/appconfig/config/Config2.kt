package com.vgaidarji.appconfig.config

import com.vgaidarji.appconfig.feature.ConfigurableFeatures.SupportedFeatures
import com.vgaidarji.appconfig.feature.Feature

class Config2: FeaturesConfig() {
    override fun features(): List<Feature> {
        return listOf(
            Feature(SupportedFeatures.BOTTOM_NAVIGATION.featureName, false)
        )
    }
}
