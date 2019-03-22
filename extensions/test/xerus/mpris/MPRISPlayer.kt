package xerus.mpris

import org.freedesktop.dbus.interfaces.Properties
import org.freedesktop.dbus.types.Variant
import org.mpris.MediaPlayer2.LoopStatus
import org.mpris.MediaPlayer2.PlaybackStatus
import xerus.ktutil.javafx.properties.TimedObservable
import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.collections.HashMap

val playerName = "TestPlayer"
val cover = "file://" + File("extensions/test/cover.png").absoluteFile.toString()
val randomDesktopEntry = "file://" + Paths.get(System.getProperty("user.home"), ".local", "share", "applications").toFile().listFiles().first()

var startTime = System.currentTimeMillis()
var lastPosition = 20_000L

fun main() {
	System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info")
	println("Connecting to DBus")
	println("Desktop entry: $randomDesktopEntry")
	val player = MPRISPlayer()
	println("MPRISPlayer constructed")
	player.exportAs(playerName)
}

class MPRISPlayer: AbstractMPRISPlayer(), MediaPlayerX, PlayerX, Properties {
	
	override var playbackStatus by DBusProperty(PlaybackStatus.Playing)
	override var loopStatus: LoopStatus by DBusProperty(LoopStatus.None)
	override var rate by DBusProperty(1.0)
	override var shuffle: Boolean by DBusProperty(false)
	override val metadata by DBusMapProperty(String::class, Variant::class, HashMap(PropertyMap {
		put("mpris:trackid", "/playerfx/songs/monstercat")
		put("mpris:length", 100_000_000)
		put("mpris:artUrl", cover)
		put("xesam:artist", if(System.getenv("XDG_CURRENT_DESKTOP") == "KDE") "Feint x Varien" else arrayOf("Feint", "Varien"))
		put("xesam:album", "Uncaged")
		put("xesam:title", "Monstercat")
	}))
	override var volume: Double by DBusProperty(0.8)
	override val position: Long by DBusProperty(20_000_000, TimedObservable(1000) {
		val elapsed = System.currentTimeMillis() - startTime - lastPosition
		if(playbackStatus as CharSequence == PlaybackStatus.Playing)
			lastPosition += elapsed
		else
			startTime += elapsed
		lastPosition * 1000
	})
	override val minimumRate: Double by DBusConstant(1.0)
	override val maximumRate: Double by DBusConstant(1.0)
	override val canGoNext: Boolean by DBusConstant(true)
	override val canGoPrevious: Boolean by DBusConstant(true)
	override val canPlay: Boolean by DBusConstant(true)
	override val canPause: Boolean by DBusConstant(true)
	override val canSeek: Boolean by DBusConstant(true)
	override val canControl: Boolean by DBusConstant(true)
	override val canQuit: Boolean by DBusConstant(true)
	override var fullscreen: Boolean by DBusProperty(false)
	override val canSetFullscreen: Boolean by DBusConstant(false)
	override val canRaise: Boolean by DBusConstant(false)
	override val identity: String by DBusConstant("PlayerFX")
	override val desktopEntry: String by DBusConstant(randomDesktopEntry)
	override val supportedUriSchemes: Array<String> by DBusConstant(arrayOf("file"))
	override val supportedMimeTypes: Array<String> by DBusConstant(arrayOf("audio/mpeg", "audio/mp4"))
	
	override fun GetAll(interface_name: String) = properties[interface_name]
	
	fun <A: Any> updateProperty(interface_name: String, name: String, value: A) {
		println("Updating $name of $interface_name to $value")
		try {
			val new = value.variant()
			properties[interface_name]!![name] = new
			connection.sendMessage(Properties.PropertiesChanged("/org/mpris/MediaPlayer2", interface_name, Collections.singletonMap(name, new) as Map<String, Variant<*>>, Collections.emptyList()))
		} catch(e: Throwable) {
			e.printStackTrace()
		}
	}
	
	fun updateStatus(status: PlaybackStatus) {
		playbackStatus = status
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
