package xerus.mpris

import org.freedesktop.dbus.Variant
import org.mpris.MediaPlayer2.DBusPlayer
import org.mpris.MediaPlayer2.MediaPlayer2

abstract class AbstractMPRISPlayer: MediaPlayer2, DBusPlayer {

    override fun isRemote() = false

    val properties = HashMap<String, MutableMap<String, Variant<*>>>()

    /*override fun <A> Get(interface_name: String, property_name: String): A {
        return super.Get(interface_name, property_name)
    }

    override fun <A> Set(interface_name: String, property_name: String, value: A) {
        super.Set(interface_name, property_name, value)
    }

    override fun GetAll(interface_name: String): Map<String, Variant<*>> {

    }*/


    abstract val PlaybackStatus: String
    abstract var LoopStatus: String?
    abstract var Rate: Double
    abstract var Shuffle: Boolean?
    abstract val Metadata: Map<String, Variant<*>>
    abstract var Volume: Double
    abstract val Position: Long
    abstract val MinimumRate: Double
    abstract val MaximumRate: Double
    abstract val CanGoNext: Boolean
    abstract val CanGoPrevious: Boolean
    abstract val CanPlay: Boolean
    abstract val CanPause: Boolean
    abstract val CanSeek: Boolean
    abstract val CanControl: Boolean

    abstract val CanQuit: Boolean
    abstract var Fullscreen: Boolean?
    val CanSetFullscreen
        get() = Fullscreen != null
    abstract val CanRaise: Boolean
    abstract val HasTrackList: Boolean
    abstract val Identity: String
    abstract val DesktopEntry: String?
    abstract val SupportedUriSchemes: Array<String>
    abstract val SupportedMimeTypes: Array<String>

}