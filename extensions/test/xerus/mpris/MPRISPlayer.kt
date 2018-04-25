package xerus.mpris

import org.freedesktop.DBus.Properties.PropertiesChanged
import org.freedesktop.dbus.Variant
import org.mpris.MediaPlayer2.LoopStatus
import org.mpris.MediaPlayer2.PlaybackStatus
import java.util.*

operator fun <K, V> HashMap<K, V>.set(k: K, value: V) = put(k, value)

fun main(args: Array<String>) {
	println("Connecting to DBus")
	MPRISPlayer().exportAs("TestPlayer")
}

class MPRISPlayer : AbstractMPRISPlayer() {
	
	override var playbackStatus by DBusProperty(PlaybackStatus.Stopped)
	override var loopStatus: LoopStatus = LoopStatus.None
	override var rate by DBusProperty(1.0)
	override var shuffle: Boolean = false
	override val metadata by DBusMapProperty(PropertyMap {
		put("mpris:trackid", "/playerfx/songs/untiltheend")
		put("mpris:length", 10_000000)
		put("mpris:artUrl", "file:///home/janek/daten/musik/Monstercat/Aero Chord - Love & Hate EP/cover.jpeg")
		put("xesam:artist", arrayOf("Aero Chord", "Fractal"))
		put("xesam:title", "Until The End (feat. Q'AILA)")
	})
	override var volume: Double
		get() = TODO("not implemented")
		set(value) {}
	override val position: Long
		get() = TODO("not implemented")
	override val minimumRate: Double
		get() = TODO("not implemented")
	override val maximumRate: Double
		get() = TODO("not implemented")
	override val canGoNext: Boolean
		get() = TODO("not implemented")
	override val canGoPrevious: Boolean
		get() = TODO("not implemented")
	override val canPlay: Boolean
		get() = TODO("not implemented")
	override val canPause: Boolean
		get() = TODO("not implemented")
	override val canSeek: Boolean
		get() = TODO("not implemented")
	override val canControl: Boolean
		get() = TODO("not implemented")
	override val canQuit: Boolean
		get() = TODO("not implemented")
	override var fullscreen: Boolean
		get() = TODO("not implemented")
		set(value) {}
	override val canSetFullscreen: Boolean
		get() = TODO("not implemented")
	override val canRaise: Boolean
		get() = TODO("not implemented")
	override val hasTrackList: Boolean
		get() = TODO("not implemented")
	override val identity: String
		get() = TODO("not implemented")
	override val desktopEntry: String
		get() = TODO("not implemented")
	override val supportedUriSchemes: Array<String>
		get() = TODO("not implemented")
	override val supportedMimeTypes: Array<String>
		get() = TODO("not implemented")
	
	var currentTrack = SimpleTrack(metadata)
	
	init {
		println("Constructing MPRISPlayer")
		/*
		properties["org.mpris.MediaPlayer2"] = PropertyMap {
			put("CanSeek", true)
			put("CanQuit", true)
			put("CanRaise", false)
			put("HasTrackList", true)
			put("Identity", "PlayerFX")
			put("SupportedUriSchemes", arrayOf("file"))
			put("SupportedMimeTypes", arrayOf("audio/mpeg", "audio/mp4"))
			//put("DesktopEntry")
		}

		properties["org.mpris.MediaPlayer2.Player"] = PropertyMap {
			put("PlaybackStatus", PlaybackStatus.Stopped)
			put("LoopStatus", LoopStatus.None)
			put("Rate", 1.0)
			put("Shuffle", false)
			put("Metadata", Variant(currentTrack.metadata, DBusMapType(String::class.java, Variant::class.java)))
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
		*/
		println("MPRISPlayer constructed")
		
	}
	
	override fun GetAll(interface_name: String) = properties[interface_name]
	
	fun <A : Any> updateProperty(interface_name: String, name: String, value: A) {
		println("Updating $name of $interface_name to $value")
		val new = Variant(value)
		properties[interface_name]!![name] = new
		try {
			connection.sendSignal(PropertiesChanged("/org/mpris/MediaPlayer2", interface_name, Collections.singletonMap(name, new) as Map<String, Variant<*>>, Collections.emptyList()))
		} catch (e: Throwable) {
			e.printStackTrace()
		}
	}
	
	fun updateStatus(status: PlaybackStatus) {
		playbackStatus = status
		updateProperty("org.mpris.MediaPlayer2.Player", "PlaybackStatus", status.name)
	}
	
	override fun PlayPause() {
		println("PlayPause called")
		updateStatus(playbackStatus.playPause())
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
		connection.disconnect()
	}
	
	override fun OpenUri(uri: String) {
		println("OpenUri called")
	}
	
	override fun getObjectPath() = "/org/mpris/MediaPlayer2"
	
}
