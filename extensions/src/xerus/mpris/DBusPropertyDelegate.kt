@file:Suppress("UNCHECKED_CAST")

package xerus.mpris

import javafx.beans.value.ObservableValue
import org.freedesktop.dbus.DBusInterfaceName
import org.mpris.MediaPlayer2.PlaylistOrdering
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun main(args: Array<String>) {
	println(arrayOf(PlaylistOrdering.Alphabetical).variant())
}

val logger = LoggerFactory.getLogger("xerus.mpris.properties")

private fun findInterface(clazz: Class<*>, name: String): Class<*>? =
	(clazz.interfaces + clazz.superclass).firstOrNull {
		if(it == null) return@firstOrNull false
		if (it.declaredMethods.any { it.name.contains(name) }) {
			logger.trace("Found $it for property $name")
			return it.interfaces.first()
		}
		findInterface(it, name) != null
	}

class DBusProperty<T : Any>(private val initial: T, private val observable: ObservableValue<T>? = null, private val onSet: ((T) -> Unit)? = null) {

	constructor(observable: ObservableValue<T>, onSet: ((T) -> Unit)? = null) : this(observable.value, observable, onSet)

	operator fun provideDelegate(
			thisRef: AbstractMPRISPlayer,
			prop: KProperty<*>
	): ReadWriteProperty<AbstractMPRISPlayer, T> {
		val name = prop.name.capitalize()
		val clazz = findInterface(thisRef::class.java, name)
				?: throw RuntimeException("No interface found for Property $name")
		val interfaceName = (clazz.annotations.find { it is DBusInterfaceName } as? DBusInterfaceName)?.value
				?: clazz.name
		logger.trace("Registered Property ${prop.name} for $interfaceName")
		thisRef.properties.getOrPut(interfaceName) { HashMap() }[name] = initial.variant()
		val property = Property<T>(interfaceName, name)
		if (onSet != null)
			thisRef.propertyListeners[name] = onSet as ((Any) -> Unit)
		observable?.addListener { _, _, new -> property.setValue(thisRef, prop, new) }
		return property
	}
	
}
class DBusMapProperty<K, V>(private val initial: Map<K, V> = HashMap<K, V>(), private val observable: ObservableValue<Map<K, V>>? = null, private val onSet: ((Map<K, V>) -> Unit)? = null) {
	
	constructor(observable: ObservableValue<Map<K, V>>, onSet: ((Map<K, V>) -> Unit)? = null) : this(observable.value, observable, onSet)
	
	operator fun provideDelegate(
			thisRef: AbstractMPRISPlayer,
			prop: KProperty<*>
	): ReadWriteProperty<AbstractMPRISPlayer, Map<K, V>> {
		val name = prop.name.capitalize()
		val clazz = findInterface(thisRef::class.java, name)
				?: throw RuntimeException("No interface found for Property $name")
		val interfaceName = (clazz.annotations.find { it is DBusInterfaceName } as? DBusInterfaceName)?.value
				?: clazz.name
		logger.trace("Registered Property ${prop.name} for $interfaceName")
		//thisRef.properties.getOrPut(interfaceName) { HashMap() }[name] = initial.variant()
		val property = Property<Map<K, V>>(interfaceName, name)
		if (onSet != null)
			thisRef.propertyListeners[name] = onSet as ((Any) -> Unit)
		observable?.addListener { _, _, new -> property.setValue(thisRef, prop, new) }
		return property
	}
	
}

class DBusConstant<out T : Any>(private val value: T) {
	
	operator fun provideDelegate(
			thisRef: AbstractMPRISPlayer,
			prop: KProperty<*>
	): ReadOnlyProperty<AbstractMPRISPlayer, T> {
		val name = prop.name.capitalize()
		val clazz = findInterface(thisRef::class.java, name)
				?: throw RuntimeException("No interface found for Property $name")
		val interfaceName = (clazz.annotations.find { it is DBusInterfaceName } as? DBusInterfaceName)?.value
				?: clazz.name
		logger.trace("Registered Constant ${prop.name} for $interfaceName")
		thisRef.properties.getOrPut(interfaceName) { HashMap() }.put(name, value.variant())
		return Constant(value)
	}
}

private class Property<T : Any>(private val interfaceName: String, private val name: String) : ReadWriteProperty<AbstractMPRISPlayer, T> {
	
	override fun setValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>, value: T) {
		if (thisRef.properties.getValue(interfaceName).put(name, value.variant())?.value != value)
			thisRef.connection.sendSignal(thisRef.propertyChanged(interfaceName, name))
	}
	
	override fun getValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>) =
			thisRef.properties.getValue(interfaceName).getValue(name).value as T
	
}

private class Constant<out T : Any>(private val value: T) : ReadOnlyProperty<AbstractMPRISPlayer, T> {
	override fun getValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>) = value
}