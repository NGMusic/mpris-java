@file: Suppress("UNUSED")
package org.mpris.MediaPlayer2

import org.freedesktop.dbus.DBusInterface
import org.freedesktop.dbus.DBusInterfaceName
import org.freedesktop.dbus.DBusSignal
import org.freedesktop.dbus.Position
import org.freedesktop.dbus.Struct
import org.freedesktop.dbus.UInt32

/** [https://specifications.freedesktop.org/mpris-spec/latest/Playlists_Interface.html]
 *
 * ## Properties
 * - PlaylistCount		u	                        Read only
 * - Orderings		    as (Playlist_Ordering_List)	Read only
 * - ActivePlaylist		(b(oss)) (Maybe_Playlist)	Read only
 * */
@DBusInterfaceName("org.mpris.MediaPlayer2.Playlists")
interface Playlists: DBusInterface {
    class PlaylistChanged(path: DBusPath, playlist: Playlist): DBusSignal(path, playlist)

    fun ActivatePlaylist(playlist_id: DBusInterface)
    fun GetPlaylists(index: Int, max_count: Int, order: String, reverse_order: Boolean): List<Playlist>
}

class Playlist @JvmOverloads
constructor(@field:Position(0)
            val id: String,
            @field:Position(1)
            val name: String,
            @field:Position(2)
            val icon: String? = null): Struct()

class MaybePlaylist(@field:Position(0)
                    val valid: Boolean,
                    @field:Position(1)
                    val playlist: Playlist): Struct() {
    /** Creates an empty MaybePlaylist */
    constructor() : this(false, Playlist("/", ""))
    /** Creates a valid MaybePlaylist containing the Playlist */
    constructor(playlist: Playlist) : this(true, playlist)
}

enum class PlaylistOrdering: CharSequence by this.toString() {
    /**Alphabetical ordering by name, ascending.*/
    Alphabetical,
    /**Ordering by creation date, oldest first.*/
    Created,
    /**Ordering by last modified date, oldest first.*/
    Modified,
    /**Ordering by date of last playback, oldest first.*/
    Played,
    /**A user-defined ordering.*/
    User;
}