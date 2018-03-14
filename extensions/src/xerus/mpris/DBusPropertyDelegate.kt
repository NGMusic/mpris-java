@file:Suppress("UNCHECKED_CAST")

package xerus.mpris

import org.freedesktop.dbus.DBusInterfaceName
import org.mpris.MediaPlayer2.PlaylistOrdering
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun main(args: Array<String>) {
	println(arrayOf(PlaylistOrdering.Alphabetical).variant())
}

private fun findInterface(clazz: Class<*>, name: String): Class<*>? {
	if (clazz.declaredMethods.any { it.name.contains(name) })
		return clazz.interfaces.first()
	return (clazz.interfaces + clazz.superclass).firstOrNull { findInterface(it, name) != null }
}

fun

class DBusProperty<T : Any>(private val initial: T, private val onSet: ((T) -> Unit)? = null) {
	
	operator fun provideDelegate(
			thisRef: AbstractMPRISPlayer,
			prop: KProperty<*>
	): ReadWriteProperty<AbstractMPRISPlayer, T> {
		val name = prop.name.capitalize()
		val clazz = findInterface(thisRef::class.java, name)
				?: throw RuntimeException("No interface found for Property $name")
		val interfaceName = (clazz.annotations.find { it is DBusInterfaceName } as? DBusInterfaceName)?.value
				?: clazz.name
		println("${prop.name} - $clazz - $interfaceName")
		thisRef.properties.getOrPut(interfaceName) { HashMap() }.put(name, initial.variant())
		if (onSet != null)
			thisRef.propertyListeners[name] = onSet as ((Any) -> Unit)
		return Property(interfaceName, name)
	}
	
}

class DBusConstant<T : Any>(private val initial: T) {
	
	operator fun provideDelegate(
			thisRef: AbstractMPRISPlayer,
			prop: KProperty<*>
	): ReadOnlyProperty<AbstractMPRISPlayer, T> {
		val name = prop.name.capitalize()
		val clazz = findInterface(thisRef::class.java, name)
				?: throw RuntimeException("No interface found for Property $name")
		val interfaceName = (clazz.annotations.find { it is DBusInterfaceName } as? DBusInterfaceName)?.value
				?: clazz.name
		println("${prop.name} - $clazz - $interfaceName")
		thisRef.properties.getOrPut(interfaceName) { HashMap() }.put(name, initial.variant())
		return Constant(initial)
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