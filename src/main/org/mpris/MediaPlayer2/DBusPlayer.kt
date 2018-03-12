@file: Suppress("UNUSED")
package org.mpris.MediaPlayer2

import org.freedesktop.dbus.DBusInterface
import org.freedesktop.dbus.DBusInterfaceName
import org.freedesktop.dbus.DBusSignal

/** [https://specifications.freedesktop.org/mpris-spec/latest/Player_Interface.html]
 *
 * ## Properties
 * - PlaybackStatus	s (Playback_Status) 	Read only
 * - LoopStatus		s (Loop_Status)	        Read/Write	(optional)
 * - Rate		    d (Playback_Rate)	    Read/Write
 * - Shuffle		b	                    Read/Write	(optional)
 * - Metadata		a{sv} (Metadata_Map)	Read only
 * - Volume		    d (Volume)	            Read/Write
 * - Position		x (Time_In_Us)	        Read only
 * - MinimumRate	d (Playback_Rate)	    Read only
 * - MaximumRate	d (Playback_Rate)	    Read only
 * - CanGoNext		b	                    Read only
 * - CanGoPrevious	b	                    Read only
 * - CanPlay		b	                    Read only
 * - CanPause		b	                    Read only
 * - CanSeek		b	                    Read only
 * - CanControl		b	                    Read only
 * */
@DBusInterfaceName("org.mpris.MediaPlayer2.Player")
interface DBusPlayer: DBusInterface {
    class Seeked(path: String, position: Long): DBusSignal(path, position)

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

enum class LoopStatus {
    None, Track, Playlist
}