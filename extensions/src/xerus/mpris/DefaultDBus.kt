package xerus.mpris

import org.freedesktop.DBus
import org.freedesktop.dbus.Variant
import java.util.*

interface DefaultDBus: DBus.Properties {

    override fun isRemote() = false

    @Suppress("UNCHECKED_CAST")
    override fun <A: Any?> Get(interface_name: String, property_name: String) =
        GetAll(interface_name)[property_name]?.value as A

    override fun <A: Any?> Set(interface_name: String, property_name: String, value: A) {
        GetAll(interface_name).put(property_name, Variant(value))
        propertyChanged(interface_name, property_name)
    }

    fun propertyChanged(interface_name: String, property_name: String) {
        DBus.Properties.PropertiesChanged(objectPath, interface_name, Collections.singletonMap(property_name, Variant(Get<Any>(interface_name, property_name))) as Map<String, Variant<*>>, null)
    }

}
