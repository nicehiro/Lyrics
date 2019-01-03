package nicehiro.org.lyrics.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SpotifyBroadcastReceiver extends BroadcastReceiver {

  private static final String TAG = "SPOTIFY-BROADCAST";

  private String artistName;

  private String albumName;

  private String trackName;

  public String getArtistName() {
    return artistName;
  }

  public void setArtistName(String artistName) {
    this.artistName = artistName;
  }

  public String getAlbumName() {
    return albumName;
  }

  public void setAlbumName(String albumName) {
    this.albumName = albumName;
  }

  public String getTrackName() {
    return trackName;
  }

  public void setTrackName(String trackName) {
    this.trackName = trackName;
  }

  static final class BroadcastTypes {
    static final String SPOTIFY_PACKAGE = "com.spotify.music";
    static final String PLAYBACK_STATE_CHANGED = SPOTIFY_PACKAGE + ".playbackstatechanged";
    static final String QUEUE_CHANGED = SPOTIFY_PACKAGE + ".queuechanged";
    static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    long timeSentInMs = intent.getLongExtra("timeSent", 0L);

    String action = intent.getAction();

    if (action.equals(BroadcastTypes.METADATA_CHANGED)) {
      String trackId = intent.getStringExtra("id");
      artistName = intent.getStringExtra("artist");
      albumName = intent.getStringExtra("album");
      trackName = intent.getStringExtra("track");
      int trackLengthInSec = intent.getIntExtra("length", 0);
      // Do something with extracted information...
      Log.d(TAG, trackId + "  " + artistName + "  " + albumName + " " + trackName + "  " + trackLengthInSec);
      // Search song's lyric
    } else if (action.equals(BroadcastTypes.PLAYBACK_STATE_CHANGED)) {
      boolean playing = intent.getBooleanExtra("playing", false);
      int positionInMs = intent.getIntExtra("playbackPosition", 0);
      // Do something with extracted information
      Log.d(TAG, "Playback Position: " + positionInMs);
    } else if (action.equals(BroadcastTypes.QUEUE_CHANGED)) {
      // Sent only as a notification, your app may want to respond accordingly.
      Log.d(TAG, "receive a queue changed message from spotify");
    }
  }

}
