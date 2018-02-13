package xerus.mpris

import org.freedesktop.DBus
import org.freedesktop.dbus.Variant

interface DefaultDBus: DBus.Properties {
    override fun isRemote() = false
    @Suppress("UNCHECKED_CAST")
    override fun <A: Any?> Get(interface_name: String, property_name: String): A {
        return GetAll(interface_name)[property_name]?.value as A
    }
    override fun <A: Any?> Set(interface_name: String, property_name: String, value: A) {
        GetAll(interface_name).put(property_name, Variant(value))
    }
}