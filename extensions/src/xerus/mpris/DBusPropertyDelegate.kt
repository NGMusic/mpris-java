@file:Suppress("UNCHECKED_CAST")

package xerus.mpris

import org.freedesktop.dbus.DBusInterfaceName
import org.mpris.MediaPlayer2.PlaylistOrdering
import java.util.LinkedList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun main(args: Array<String>) {
    println(arrayOf(PlaylistOrdering.Alphabetical).variant())
}

class DBusProperty<T : Any>(private val initial: T, private val onSet: ((T) -> Unit)? = null) {
    operator fun provideDelegate(
            thisRef: AbstractMPRISPlayer,
            prop: KProperty<*>
    ): ReadWriteProperty<AbstractMPRISPlayer, T> {
        val name = prop.name.capitalize()
        val queue = LinkedList<Class<*>>()
        var clazz: Class<*> = thisRef::class.java
        while (true) {
            if(clazz.superclass != null)
                queue.add(clazz.superclass)
            queue.addAll(clazz.interfaces)
            clazz = queue.poll() ?: throw RuntimeException("No interface found for Property $prop")
            if(clazz.declaredMethods.any { it.name.contains(name) }) {
                clazz = clazz.interfaces.first()
                break
            }
        }
        val interfaceName = (clazz.annotations.find { it is DBusInterfaceName } as? DBusInterfaceName)?.value ?: clazz.name
        println("${prop.name} - $clazz - $interfaceName")
        thisRef.properties.getOrPut(interfaceName) { HashMap() }.put(name, initial.variant())
        if(onSet != null)
            thisRef.propertyListeners[name] = onSet as ((Any) -> Unit)
        return Property(interfaceName, name)
    }

}

private class Property<T : Any>(private val interfaceName: String, private val name: String) : ReadWriteProperty<AbstractMPRISPlayer, T> {

    override fun setValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>, value: T) {
        if(thisRef.properties.getValue(interfaceName).put(name, value.variant())?.value != value)
            thisRef.connection.sendSignal(thisRef.propertyChanged(interfaceName, name))
    }

    override fun getValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>) =
        thisRef.properties.getValue(interfaceName).getValue(name).value as T

}
