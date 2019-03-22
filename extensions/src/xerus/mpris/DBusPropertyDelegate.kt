@file:Suppress("UNCHECKED_CAST")

package xerus.mpris

import javafx.beans.value.ObservableValue
import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

val logger = LoggerFactory.getLogger("xerus.mpris.properties")

private fun findInterface(clazz: Class<*>, name: String): Class<*>? {
	for(c in (clazz.interfaces + clazz.superclass)) {
		if(c == null)
			continue
		return findInterface(c, name) ?: continue
	}
	if(clazz.declaredMethods.any { it.name.contains(name) }) {
		logger.debug("Found $clazz for property $name")
		return clazz
	}
	return null
}

private val Class<*>.dbusInterfaceName: String
	get() = annotations.filterIsInstance<DBusInterfaceName>().firstOrNull()?.value ?: name

class DBusProperty<T: Any>(private val initial: T, private val observable: ObservableValue<T>? = null, private val onSet: ((T) -> Unit)? = null) {
	
	constructor(observable: ObservableValue<T>, onSet: ((T) -> Unit)? = null): this(observable.value, observable, onSet)
	
	operator fun provideDelegate(
		thisRef: AbstractMPRISPlayer,
		prop: KProperty<*>
	): ReadWriteProperty<AbstractMPRISPlayer, T> {
		val name = prop.name.capitalize()
		val clazz = findInterface(thisRef::class.java, name)
			?: throw RuntimeException("No interface found for Property $name")
		val interfaceName = clazz.dbusInterfaceName
		logger.debug("Registered Property $name for $interfaceName")
		thisRef.properties.getOrPut(interfaceName) { HashMap() }[name] = initial.variant()
		val property = Property<T>(interfaceName, name)
		if(onSet != null)
			thisRef.propertyListeners[name] = onSet as ((Any) -> Unit)
		observable?.addListener { _, _, new -> property.setValue(thisRef, prop, new) }
		return property
	}
	
}

class DBusMapProperty<K: Any, V: Any>(
	private val keyClass: KClass<K>,
	private val valueClass: KClass<V>,
	private val initial: Map<K, V> = HashMap(),
	private val observable: ObservableValue<Map<K, V>>? = null,
	private val onSet: ((Map<K, V>) -> Unit)? = null) {
	
	constructor(keyClass: KClass<K>, valueClass: KClass<V>, observable: ObservableValue<Map<K, V>>, onSet: ((Map<K, V>) -> Unit)? = null):
		this(keyClass, valueClass, observable.value, observable, onSet)
	
	operator fun provideDelegate(
		thisRef: AbstractMPRISPlayer,
		prop: KProperty<*>
	): ReadWriteProperty<AbstractMPRISPlayer, Map<K, V>> {
		val name = prop.name.capitalize()
		val clazz = findInterface(thisRef::class.java, name)
			?: throw RuntimeException("No interface found for Property $name")
		val interfaceName = clazz.dbusInterfaceName
		logger.debug("Registered Map Property $name for $interfaceName")
		thisRef.properties.getOrPut(interfaceName) { HashMap() }[name] = initial.variant(keyClass, valueClass)
		val property = Property<Map<K, V>>(interfaceName, name)
		if(onSet != null)
			thisRef.propertyListeners[name] = onSet as ((Any) -> Unit)
		observable?.addListener { _, _, new -> property.setValue(thisRef, prop, new) }
		return property
	}
	
}

class DBusConstant<out T: Any>(private val value: T) {
	
	operator fun provideDelegate(
		thisRef: AbstractMPRISPlayer,
		prop: KProperty<*>
	): ReadOnlyProperty<AbstractMPRISPlayer, T> {
		val name = prop.name.capitalize()
		val clazz = findInterface(thisRef::class.java, name)
			?: throw RuntimeException("No interface found for Property $name")
		val interfaceName = clazz.dbusInterfaceName
		logger.debug("Registered Constant $name for $interfaceName")
		thisRef.properties.getOrPut(interfaceName) { HashMap() }.put(name, value.variant())
		return Constant(value)
	}
}

private class Property<T: Any>(private val interfaceName: String, private val name: String): ReadWriteProperty<AbstractMPRISPlayer, T> {
	
	override fun setValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>, value: T) {
		if(thisRef.properties.getValue(interfaceName).put(name, value.variant())?.value != value)
			thisRef.connection.sendMessage(thisRef.propertyChanged(interfaceName, name))
	}
	
	override fun getValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>) =
		thisRef.properties.getValue(interfaceName).getValue(name).value as T
	
}

private class Constant<out T: Any>(private val value: T): ReadOnlyProperty<AbstractMPRISPlayer, T> {
	override fun getValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>) = value
}