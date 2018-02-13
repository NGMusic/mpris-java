@file: Suppress("UNUSED")
package org.mpris.MediaPlayer2

import org.freedesktop.DBus
import org.freedesktop.dbus.DBusInterface
import org.freedesktop.dbus.DBusInterfaceName
import org.freedesktop.dbus.DBusSignal
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

@DBusInterfaceName("org.mpris.MediaPlayer2")
interface MediaPlayer2: DBusInterface {
    fun Raise()
    fun Quit()
}

@DBusInterfaceName("org.mpris.MediaPlayer2.Player")
interface Player: DBusInterface {
    class Seeked(path: String, val position: Long): DBusSignal(path, position)

    fun Next()
    fun Previous()
    fun Pause()
    fun PlayPause()
    fun Play()
    fun Stop()
    fun Seek(x: Long)
    fun OpenUri(uri: String)
}

enum class PlaybackStatus {
    Playing, Paused, Stopped
}