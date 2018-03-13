package xerus.mpris

import org.freedesktop.dbus.Variant
import java.util.HashMap

fun Any.variant() = this as? Variant<*> ?: Variant(this)

class PropertyMap private constructor(initial: PropertyMap.() -> Unit, private val map: HashMap<String, Variant<*>?>) : Map<String, Variant<*>?> by map {

    @JvmOverloads constructor(initial: PropertyMap.() -> Unit = {}) : this(initial, HashMap())

    override val entries: Set<Map.Entry<String, Variant<*>?>>
        get() = refresh().entries
    override val values: Collection<Variant<*>?>
        get() = refresh().values

    override fun containsValue(value: Variant<*>?) = refresh().containsValue(value)

    private val mapCallable = HashMap<String, () -> Any?>()

    init {
        initial(this)
    }

    fun refresh(): Map<String, Variant<*>?> {
        mapCallable.forEach { key, v ->
            val calc = v()
            map[key] = calc?.variant()
        }
        return map
    }

    private fun refresh(key: String): Variant<*>? {
        val variant = mapCallable[key]?.invoke()?.variant()
        map[key] = variant
        return variant
    }

    override operator fun get(key: String) = refresh(key)

    fun getValue(key: String) = get(key)?.value

    operator fun set(key: String, value: Any) = put(key, value)

    fun put(key: String, value: Any) {
        var v = value
        if (value is Enum<*>)
            v = value.name
        map[key] = Variant(v)
    }

    fun put(key: String, cal: () -> Any?) {
        mapCallable[key] = cal
        refresh(key)
    }

}
