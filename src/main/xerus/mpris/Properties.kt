package xerus.mpris

import org.freedesktop.dbus.Variant
import java.util.HashMap

class PropertyMap(initial: PropertyMap.() -> Unit): HashMap<String, Variant<*>>() {

    init {
        initial(this)
    }

    fun put(key: String, value: Any) {
        var v = value
        if(value is Enum<*>)
            v = value.name
        super.put(key, Variant(v))
    }

}
