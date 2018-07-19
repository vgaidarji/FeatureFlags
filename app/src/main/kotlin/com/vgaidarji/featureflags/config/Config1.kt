package com.vgaidarji.featureflags.config

import com.vgaidarji.appconfig.config.FeaturesConfig
import com.vgaidarji.appconfig.feature.Feature
import com.vgaidarji.featureflags.config.ConfigurableFeatures.SupportedFeatures

class Config1: FeaturesConfig() {
    override fun features(): List<Feature> {
        return listOf(
            Feature(SupportedFeatures.BOTTOM_NAVIGATION.featureName, true)
        )
    }
}
