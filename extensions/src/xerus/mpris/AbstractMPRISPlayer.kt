package xerus.mpris

import org.freedesktop.DBus
import org.freedesktop.dbus.DBusConnection
import org.freedesktop.dbus.DBusInterfaceName
import org.freedesktop.dbus.DBusMemberName
import org.freedesktop.dbus.Variant
import org.mpris.MediaPlayer2.LoopStatus
import org.mpris.MediaPlayer2.MaybePlaylist
import org.mpris.MediaPlayer2.MediaPlayer2
import org.mpris.MediaPlayer2.PlaybackStatus
import org.mpris.MediaPlayer2.Player
import org.mpris.MediaPlayer2.PlaylistOrdering
import org.mpris.MediaPlayer2.Playlists
import org.mpris.MediaPlayer2.TrackId
import org.mpris.MediaPlayer2.TrackList

/** Provides a typesafe foundation for implementing an MPRISPlayer.
 *
 * Every property inherited from an interface must either be null (if it is nullable and you don't want to implement it)
 * or delegated by a [DBusProperty]
 *
 * A val represents a Read-only field as declared by MPRIS, it is perfectly valid to implement it as var */
abstract class AbstractMPRISPlayer: MediaPlayerX, PlayerX, DefaultDBus {

    val connection = DBusConnection.getConnection(DBusConnection.SESSION)
    val properties = HashMap<String, MutableMap<String, Variant<*>>>()
    internal val propertyListeners = HashMap<String, (Any) -> Unit>()

    override fun GetAll(interface_name: String) = properties[interface_name]

    override fun <A : Any> Set(interface_name: String, property_name: String, value: A) {
        super.Set(interface_name, property_name, value)
        propertyListeners[property_name]?.invoke(value)
    }

    fun exportAs(playerName: String) {
        connection.requestBusName("org.mpris.MediaPlayer2.$playerName")
        connection.exportObject("/org/mpris/MediaPlayer2", this)
    }

    /** sends a [DBus.Properties.PropertiesChanged] signal via [connection] */
    override fun propertyChanged(interface_name: String, property_name: String) =
        super.propertyChanged(interface_name, property_name).also { connection.sendSignal(it) }

}

interface PlayerX: Player {
    val playbackStatus: PlaybackStatus
    /** _Optional_ */
    var loopStatus: LoopStatus
    var rate: Double
    /** _Optional_ */
    var shuffle: Boolean
    /** Metadata of the current Track
     * [https://www.freedesktop.org/wiki/Specifications/mpris-spec/metadata/#index2h2] */
    val metadata: Map<String, Variant<*>>
    var volume: Double
    val position: Long
    val minimumRate: Double
    val maximumRate: Double
    val canGoNext: Boolean
    val canGoPrevious: Boolean
    val canPlay: Boolean
    val canPause: Boolean
    val canSeek: Boolean
    val canControl: Boolean
}

interface MediaPlayerX: MediaPlayer2 {
    val canQuit: Boolean
    /** _Optional_ */
    var fullscreen: Boolean
    /** _Optional_ */
    val canSetFullscreen: Boolean
    val canRaise: Boolean
    /** Indicates whether this object implements the org.mpris.MediaPlayer2.TrackList interface. */
    val hasTrackList: Boolean
    /** A friendly name to identify the media player to users.
     * This should usually match the name found in the [desktopEntry]. */
    val identity: String
    /** The basename of an installed .desktop file which complies with the Desktop entry
     * specification, with the ".desktop" extension stripped.
     *
     * _Optional_ */
    val desktopEntry: String
    val supportedUriSchemes: Array<String>
    val supportedMimeTypes: Array<String>
}

interface TrackListX: TrackList {
    val tracks: Array<TrackId>
    val canEditTracks: Boolean
}

interface PlaylistsX: Playlists {
    val playlistCount: Int
    val orderings: Array<PlaylistOrdering>
    val activePlaylist: MaybePlaylist
}