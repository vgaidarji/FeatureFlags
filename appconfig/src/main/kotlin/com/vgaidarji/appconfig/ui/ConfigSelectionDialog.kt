package com.vgaidarji.appconfig.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.vgaidarji.appconfig.R
import com.vgaidarji.appconfig.config.Config1
import com.vgaidarji.appconfig.config.Config2
import com.vgaidarji.appconfig.config.FeaturesConfig
import com.vgaidarji.appconfig.config.persistence.ConfigPersister
import com.vgaidarji.appconfig.extension.bindView

class ConfigSelectionDialog : DialogFragment() {
    private val configsListView: ListView by bindView(R.id.configs_list_view)
    private val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
    }
    private val configs: List<FeaturesConfig> = listOf(
            Config1(),
            Config2()
    )
    private var configSelectedListener: ConfigSelectedListener? = null

    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.config_selection, container)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ConfigSelectedListener) {
            configSelectedListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configsListView.adapter = adapter
        configsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedConfig = configs[position]
            ConfigPersister(requireContext()).save(selectedConfig)
            configSelectedListener?.onConfigSelected(selectedConfig)
        }

        configs.forEach { adapter.add(it::class.java.simpleName) }

        adapter.addAll()
        adapter.notifyDataSetChanged()
    }
}
