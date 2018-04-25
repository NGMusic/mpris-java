package xerus.mpris

import org.freedesktop.DBus.Properties
import org.freedesktop.dbus.Variant
import org.freedesktop.dbus.types.DBusMapType
import java.security.InvalidParameterException
import java.util.*

fun Any.variant(): Variant<*> {
	if (this is Variant<*>)
		return this
	if (this is Map<*, *>)
		throw InvalidParameterException("Use Map.variant for Maps!")
	return Variant(this)
}

inline fun <reified K, reified V> Map<K, V>.variant() =
	Variant(this, DBusMapType(K::class.java, V::class.java))

interface DefaultDBus : Properties {
	
	override fun isRemote() = false
	
	@Suppress("UNCHECKED_CAST")
	override fun <A : Any> Get(interface_name: String, property_name: String) =
			GetAll(interface_name)[property_name]?.value as A
	
	override fun <A : Any> Set(interface_name: String, property_name: String, value: A) {
		GetAll(interface_name).put(property_name, value.variant())
		propertyChanged(interface_name, property_name)
	}
	
	/** returns a new [Properties.PropertiesChanged] signal */
	fun propertyChanged(interface_name: String, property_name: String) =
			Properties.PropertiesChanged(objectPath, interface_name, Collections.singletonMap(property_name, Get<Any>(interface_name, property_name).variant()) as Map<String, Variant<*>>, null)
	
}
