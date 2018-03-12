package xerus.mpris

import org.freedesktop.dbus.Variant

interface Track {
    /** the TrackID, must be a valid DBus-Path */
    val id: String
        get() = metadata["mpris:trackid"]!!.value as String
    /** Metadata of the Track */
    val metadata: Map<String, Variant<*>>
}

class SimpleTrack(override val metadata: Map<String, Variant<*>>): Track {
    constructor(createMetadata: PropertyMap.() -> Unit): this(PropertyMap(createMetadata))
}
