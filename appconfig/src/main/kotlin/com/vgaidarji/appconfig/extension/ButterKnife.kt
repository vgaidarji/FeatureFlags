package com.vgaidarji.appconfig.extension

import android.app.Activity
import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import java.util.Collections
import java.util.WeakHashMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <V : View> View.bindView(id: Int): ReadOnlyProperty<View, V> =
        required(id, viewFinder)

fun <V : View> Activity.bindView(id: Int): ReadOnlyProperty<Activity, V> =
        required(id, viewFinder)

fun <V : View> Dialog.bindView(id: Int): ReadOnlyProperty<Dialog, V> =
        required(id, viewFinder)

fun <V : View> DialogFragment.bindView(id: Int): ReadOnlyProperty<DialogFragment, V> =
        required(id, viewFinder)

fun <V : View> Fragment.bindView(id: Int): ReadOnlyProperty<Fragment, V> =
        required(id, viewFinder)

fun <V : View> ViewHolder.bindView(id: Int): ReadOnlyProperty<ViewHolder, V> =
        required(id, viewFinder)

fun <V : View> View.bindOptionalView(id: Int): ReadOnlyProperty<View, V?> =
        optional(id, viewFinder)

fun <V : View> Activity.bindOptionalView(id: Int): ReadOnlyProperty<Activity, V?> =
        optional(id, viewFinder)

fun <V : View> Dialog.bindOptionalView(id: Int): ReadOnlyProperty<Dialog, V?> =
        optional(id, viewFinder)

fun <V : View> DialogFragment.bindOptionalView(id: Int): ReadOnlyProperty<DialogFragment, V?> =
        optional(id, viewFinder)

fun <V : View> Fragment.bindOptionalView(id: Int): ReadOnlyProperty<Fragment, V?> =
        optional(id, viewFinder)

fun <V : View> ViewHolder.bindOptionalView(id: Int): ReadOnlyProperty<ViewHolder, V?> =
        optional(id, viewFinder)

fun <V : View> View.bindViews(vararg ids: Int): ReadOnlyProperty<View, List<V>> =
        required(ids, viewFinder)

fun <V : View> Activity.bindViews(vararg ids: Int): ReadOnlyProperty<Activity, List<V>> =
        required(ids, viewFinder)

fun <V : View> Dialog.bindViews(vararg ids: Int): ReadOnlyProperty<Dialog, List<V>> =
        required(ids, viewFinder)

fun <V : View> DialogFragment.bindViews(vararg ids: Int):
        ReadOnlyProperty<DialogFragment, List<V>> = required(ids, viewFinder)

fun <V : View> Fragment.bindViews(vararg ids: Int): ReadOnlyProperty<Fragment, List<V>> =
        required(ids, viewFinder)

fun <V : View> ViewHolder.bindViews(vararg ids: Int): ReadOnlyProperty<ViewHolder, List<V>> =
        required(ids, viewFinder)

fun <V : View> View.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<View, List<V>> =
        optional(ids, viewFinder)

fun <V : View> Activity.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<Activity, List<V>> =
        optional(ids, viewFinder)

fun <V : View> Dialog.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<Dialog, List<V>> =
        optional(ids, viewFinder)

fun <V : View> DialogFragment.bindOptionalViews(vararg ids: Int):
        ReadOnlyProperty<DialogFragment, List<V>> = optional(ids, viewFinder)

fun <V : View> Fragment.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<Fragment, List<V>> =
        optional(ids, viewFinder)

fun <V : View> ViewHolder.bindOptionalViews(vararg ids: Int):
        ReadOnlyProperty<ViewHolder, List<V>> = optional(ids, viewFinder)

private val View.viewFinder: View.(Int) -> View? get() = { findViewById(it) }
private val Activity.viewFinder: Activity.(Int) -> View? get() = { findViewById(it) }
private val Dialog.viewFinder: Dialog.(Int) -> View? get() = { findViewById(it) }
private val DialogFragment.viewFinder: DialogFragment.(Int) -> View?
    get() = { dialog?.findViewById(it) ?: view?.findViewById(it) }
private val Fragment.viewFinder: Fragment.(Int) -> View? get() = { view?.findViewById(it) }
private val ViewHolder.viewFinder: ViewHolder.(Int) -> View? get() = { itemView.findViewById(it) }

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing =
        throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?) = Lazy { t: T, desc ->
    t.finder(id) as V? ?: viewNotFound(id, desc)
}

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(id: Int, finder: T.(Int) -> View?) = Lazy { t: T, _ ->
    t.finder(id) as V?
}

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(ids: IntArray, finder: T.(Int) -> View?) = Lazy { t: T, desc ->
    ids.map {
        t.finder(it) as V? ?: viewNotFound(it, desc)
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(ids: IntArray, finder: T.(Int) -> View?) = Lazy { t: T, _ ->
    ids.map {
        t.finder(it) as V?
    }.filterNotNull()
}

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
            LazyRegistry.register(thisRef, this)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }

    fun reset() {
        value = EMPTY
    }
}

object ButterKnife {
    @JvmStatic
    fun reset(target: Any) {
        LazyRegistry.reset(target)
    }
}

private object LazyRegistry {
    private val lazyMap = WeakHashMap<Any, MutableCollection<Lazy<*, *>>>()

    fun register(target: Any?, lazy: Lazy<*, *>) {
        lazyMap.getOrPut(target, { Collections.newSetFromMap(WeakHashMap()) }).add(lazy)
    }

    fun reset(target: Any) {
        lazyMap[target]?.forEach { it.reset() }
    }
}
