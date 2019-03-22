package xerus.mpris

import org.freedesktop.dbus.Variant
import java.util.*

class PropertyMap private constructor(initial: PropertyMap.() -> Unit, private val map: HashMap<String, Variant<*>>): Map<String, Variant<*>> by map {
	
	@JvmOverloads
	constructor(initial: PropertyMap.() -> Unit = {}): this(initial, HashMap())
	
	override val entries: Set<Map.Entry<String, Variant<*>>>
		get() = refresh().entries
	override val values: Collection<Variant<*>>
		get() = refresh().values
	
	override fun containsValue(value: Variant<*>) = refresh().containsValue(value)
	
	private val mapCallable = HashMap<String, () -> Any?>()
	
	init {
		initial(this)
	}
	
	fun refresh(): Map<String, Variant<*>> {
		mapCallable.forEach { key, cal -> refresh(key, cal) }
		return map
	}
	
	private fun refresh(key: String, cal: (() -> Any?)? = mapCallable[key]): Any? {
		val value = cal?.invoke()
		value?.variant()?.let { map[key] = it } ?: map.remove(key)
		return value
	}
	
	override operator fun get(key: String) =
		refresh(key)?.variant() ?: map[key]
	
	fun getValue(key: String): Any = get(key)!!.value
	
	operator fun set(key: String, value: Any) = put(key, value)
	
	fun put(key: String, value: Any) {
		map[key] = value.variant()
	}
	
	fun put(key: String, cal: () -> Any?) {
		mapCallable[key] = cal
		refresh(key)
	}
	
}
