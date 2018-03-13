package xerus.mpris

import org.freedesktop.DBus
import org.freedesktop.dbus.Variant
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PropertyProvider<T: Any>(private val interfaceName: String, private val initial: T) {
    operator fun provideDelegate(
            thisRef: AbstractMPRISPlayer,
            prop: KProperty<*>
    ): DBusProperty<T> {
        thisRef.properties.getOrPut(interfaceName) { HashMap() }.put(prop.name, Variant(initial))
        return DBusProperty(interfaceName)
    }
}

class DBusProperty<T: Any>(private val interfaceName: String): ReadWriteProperty<AbstractMPRISPlayer, T> {

    override fun setValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>, value: T) {
        thisRef.properties.getValue(interfaceName).put(property.name, value.variant())
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>): T =
            thisRef.properties.getValue(interfaceName).getValue(property.name).value as T

}
