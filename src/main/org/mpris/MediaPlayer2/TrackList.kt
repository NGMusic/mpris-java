@file: Suppress("UNUSED")
package org.mpris.MediaPlayer2

import org.freedesktop.dbus.DBusInterface
import org.freedesktop.dbus.DBusSignal
import org.freedesktop.dbus.Variant

interface TrackList: DBusInterface {

    class TrackListReplacedr(path: String, val Tracks: List<DBusInterface>,
                val CurrentTrack: DBusInterface): DBusSignal(path, Tracks, CurrentTrack)

    class TrackAdded(path: String, val Metadata: Map<String, Variant<*>>,
                val AfterTrack: DBusInterface): DBusSignal(path, Metadata, AfterTrack)

    class TrackRemoved(path: String, val TrackId: DBusInterface): DBusSignal(path, TrackId)

    class TrackMetadataChanged(path: String, val TrackId: DBusInterface,
                val Metadata: Map<String, Variant<*>>): DBusSignal(path, TrackId, Metadata)

    fun GetTracksMetadata(
            TrackIds: List<DBusInterface>): List<Map<String, Variant<*>>>

    fun AddTrack(Uri: String, AfterTrack: DBusInterface,
                 SetAsCurrent: Boolean)

    fun RemoveTrack(TrackId: DBusInterface)

    fun GoTo(TrackId: DBusInterface)

}