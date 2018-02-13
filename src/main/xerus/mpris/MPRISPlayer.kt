package xerus.mpris

import org.freedesktop.DBus.Properties.PropertiesChanged
import org.freedesktop.dbus.DBusConnection
import org.freedesktop.dbus.Variant
import org.freedesktop.dbus.types.DBusMapType
import org.mpris.MediaPlayer2.LoopStatus
import org.mpris.MediaPlayer2.MaybePlaylist
import org.mpris.MediaPlayer2.MediaPlayer2
import org.mpris.MediaPlayer2.PlaybackStatus
import org.mpris.MediaPlayer2.Player
import org.mpris.MediaPlayer2.PlaylistOrdering
import java.util.Collections
import java.util.HashMap

val conn = DBusConnection.getConnection(DBusConnection.SESSION)

fun println(obj: String) = System.out.println(obj)
operator fun <K, V> HashMap<K, V>.set(k: K, value: V)  = put(k, value)

fun main(args: Array<String>) {
    println("Connecting to DBus")
    conn.requestBusName("org.mpris.MediaPlayer2.PlayerFX")
    conn.exportObject("/org/mpris/MediaPlayer2", MPRISPlayer())
}

class MPRISPlayer : MediaPlayer2, Player, DefaultDBus {

    val properties = HashMap<String, PropertyMap>()

    var status = PlaybackStatus.Stopped

    init {
        println("Constructing MPRISPlayer")
        // MediaPlayer2
        properties["org.mpris.MediaPlayer2"] = PropertyMap {
            put("CanSeek", true)
            put("CanSeek", true)
            put("CanQuit", true)
            put("CanRaise", false)
            put("HasTrackList", true)
            put("Identity", "PlayerFX")
            put("SupportedUriSchemes", arrayOf("file"))
            put("SupportedMimeTypes", arrayOf("audio/mpeg", "audio/mp4"))
            //put("DesktopEntry")
        }

        // Player
        properties["org.mpris.MediaPlayer2.Player"] = PropertyMap {
            put("PlaybackStatus", status)
            put("LoopStatus", LoopStatus.None)
            put("Rate", 1.0)
            put("Shuffle", false)
            put("Metadata", Variant(
                    PropertyMap {
                        put("mpris:trackid", "/playerfx/songs/untiltheend")
                        put("mpris:length", 10_000000)
                        put("mpris:artUrl", "file:///home/janek/Daten/Musik/Monstercat/Aero Chord - Love & Hate EP/cover.jpeg")
                        put("xesam:artist", arrayOf("Aero Chord", "Fractal"))
                        put("xesam:title", "Until The End (feat. Q'AILA)")
                    },
                    DBusMapType(String::class.java, Variant::class.java)))
            put("Volume", 1.0)
            put("Position", 0)
            put("MinimumRate", 1.0)
            put("MaximumRate", 1.0)
            put("CanGoNext", true)
            put("CanGoPrevious", true)
            put("CanPlay", true)
            put("CanPause", true)
            put("CanSeek", true)
            put("CanControl", true)
        }

        // Playlists
        properties["org.mpris.MediaPlayer2.Playlists"] = PropertyMap {
            put("PlaylistCount", 0)
            put("Orderings", PlaylistOrdering.Alphabetical)
            put("ActivePlaylist", MaybePlaylist())
        }

        // TrackList
        properties["org.mpris.MediaPlayer2.TrackList"] = PropertyMap {
            put("Tracks", arrayOf("/playerfx/songs/untiltheend", "/playerfx/songs/borneo"))
            put("CanEditTracks", false)
        }
        println("MPRISPlayer constructed")
    }

    override fun GetAll(interface_name: String): Map<String, Variant<*>> = properties[interface_name]!!

    fun <A: Any> updateProperty(interface_name: String, name: String, value: A) {
        println("Updating $name of $interface_name to $value")
        val new = Variant(value)
        properties[interface_name]!![name] = new
        try {
            conn.sendSignal(PropertiesChanged("/org/mpris/MediaPlayer2", interface_name, Collections.singletonMap(name, new) as Map<String, Variant<*>>, Collections.emptyList()))
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun updateStatus(status: PlaybackStatus) {
        this.status = status
        updateProperty("org.mpris.MediaPlayer2.Player", "PlaybackStatus", status.name)
    }

    override fun PlayPause() {
        println("PlayPause called")
        if(status == PlaybackStatus.Playing)
            updateStatus(PlaybackStatus.Paused)
        else
            updateStatus(PlaybackStatus.Playing)
    }

    override fun Play() {
        println("Play called")
        updateStatus(PlaybackStatus.Playing)
    }

    override fun Pause() {
        println("Pause called")
        updateStatus(PlaybackStatus.Paused)
    }

    override fun Stop() {
        println("Stop called")
        updateStatus(PlaybackStatus.Stopped)
    }

    override fun Seek(x: Long) {
        println("Seeking by $x")
    }

    override fun Previous() {
        println("Previous called")
    }

    override fun Next() {
        println("Next called")
    }

    override fun Raise() {
        println("open me!")
    }

    override fun Quit() {
        println("I'm quitting!")
        conn.disconnect()
    }

    override fun OpenUri(uri: String) {
        println("OpenUri called")
    }

    override fun getObjectPath() = "/org/mpris/MediaPlayer2"

}
