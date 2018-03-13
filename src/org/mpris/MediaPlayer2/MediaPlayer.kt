package org.mpris.MediaPlayer2

import org.freedesktop.dbus.DBusInterface
import org.freedesktop.dbus.DBusInterfaceName

/** [https://specifications.freedesktop.org/mpris-spec/latest/Media_Player.html]
 *
 * ## Properties
 * - CanQuit		        b	Read only
 * - Fullscreen		        b	Read/Write	(optional)
 * - CanSetFullscreen		b	Read only	(optional)
 * - CanRaise		        b	Read only
 * - HasTrackList		    b	Read only
 * - Identity		        s	Read only
 * - DesktopEntry		    s	Read only	(optional)
 * - SupportedUriSchemes	as	Read only
 * - SupportedMimeTypes	    as	Read only
 * */
@DBusInterfaceName("org.mpris.MediaPlayer2")
interface MediaPlayer2: DBusInterface {
    fun Raise()
    fun Quit()
}