@file: Suppress("UNUSED")
package org.mpris.MediaPlayer2

import org.freedesktop.dbus.DBusInterface
import org.freedesktop.dbus.DBusInterfaceName
import org.freedesktop.dbus.DBusSignal
import org.freedesktop.dbus.Position
import org.freedesktop.dbus.Struct
import org.freedesktop.dbus.UInt32

@DBusInterfaceName("org.mpris.MediaPlayer2.Playlists")
interface Playlists: DBusInterface {
    class PlaylistChanged(path: String, val playlist: Struct2): DBusSignal(path, playlist)
    fun ActivatePlaylist(playlist_id: DBusInterface)
    fun GetPlaylists(index: UInt32, max_count: UInt32, order: String, reverse_order: Boolean): List<Playlist>
}

class Playlist(@field:Position(0)
               val a: DBusInterface,
               @field:Position(1)
               val b: String,
               @field:Position(2)
               val c: String): Struct()

class Struct2(@field:Position(0)
              val a: DBusInterface,
              @field:Position(1)
              val b: String,
              @field:Position(2)
              val c: String): Struct()
