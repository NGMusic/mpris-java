@file: Suppress("UNUSED")
package org.mpris.MediaPlayer2

import org.freedesktop.dbus.DBusInterface
import org.freedesktop.dbus.DBusSignal
import org.freedesktop.dbus.Variant

/** [https://specifications.freedesktop.org/mpris-spec/latest/Track_List_Interface.html]
 *
 * ## Properties
 * - Tracks		    ao  Read only
 * - CanEditTracks	b   Read only
 * */
interface TrackList: DBusInterface {

    /**Indicates that the entire tracklist has been replaced.
     *
     * It is left up to the implementation to decide when a change to the track list is invasive enough that this signal should be emitted instead of a series of TrackAdded and TrackRemoved signals.
     *
     * @param path The path to the object this is emitted from.
     * @param Tracks new content of the tracklist
     * @param CurrentTrack The identifier of the track to be considered as current.
     * `/org/mpris/MediaPlayer2/TrackList/NoTrack` indicates that there is no current track. */
    class TrackListReplaced(path: String,
                            Tracks: List<String>,
                            CurrentTrack: String): DBusSignal(path, Tracks, CurrentTrack)

    /** Indicates that a track has been added to the track list.
     *
     * @param path The path to the object this is emitted from.
     * @param Metadata The metadata of the newly added item. This must include a mpris:trackid entry.
     * @param AfterTrack The identifier of the track after which the new track was inserted.
     * The path `/org/mpris/MediaPlayer2/TrackList/NoTrack` indicates that the track was inserted at the start of the track list. */
    class TrackAdded(path: String,
                     Metadata: Map<String, Variant<*>>,
                     AfterTrack: String): DBusSignal(path, Metadata, AfterTrack)

    /** Indicates that a track has been removed from the track list.
     *
     * @param path The path to the object this is emitted from.
     * @param TrackId The identifier of the track being removed. */
    class TrackRemoved(path: String,
                       TrackId: String): DBusSignal(path, TrackId)

    /** Indicates that the metadata of a track in the tracklist has changed.
     *
     * This may indicate that a track has been replaced, in which case the mpris:trackid metadata entry is different from the TrackId argument.
     *
     * @param path The path to the object this is emitted from.
     * @param TrackId The id of the track which metadata has changed. If the track id has changed, this will be the old value.
     * @param Metadata metadata of the new track. This must include a mpris:trackid entry. */
    class TrackMetadataChanged(path: String,
                               TrackId: String,
                               Metadata: Map<String, Variant<*>>): DBusSignal(path, TrackId, Metadata)

    fun GetTracksMetadata(TrackIds: List<String>): List<Map<String, Variant<*>>>

    fun AddTrack(Uri: String, AfterTrack: String, SetAsCurrent: Boolean)

    fun RemoveTrack(TrackId: String)

    fun GoTo(TrackId: String)

}