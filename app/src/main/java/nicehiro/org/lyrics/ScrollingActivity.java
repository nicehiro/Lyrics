package nicehiro.org.lyrics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import nicehiro.org.lyrics.model.Album;
import nicehiro.org.lyrics.model.Lyric;
import nicehiro.org.lyrics.model.Song;
import nicehiro.org.lyrics.request.NetRequest;
import nicehiro.org.lyrics.utils.NotificationUtils;
import nicehiro.org.lyrics.utils.RegexParser;
import nicehiro.org.lyrics.utils.RetrofitUtil;
import okhttp3.ResponseBody;

public class ScrollingActivity extends AppCompatActivity {

  private static final String TAG = "MAIN_ACTIVITY";

  private String songId;

  private String albumId;

  private String imageUrl;

  private ImageView album;

  private SpotifyBroadcastReceiver spotifyBroadcastReceiver;

  NetRequest netRequest = RetrofitUtil.getInstance().create(NetRequest.class);

  TextView tvLyric;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scrolling);
    Toolbar toolbar = findViewById(R.id.toolbar);
    album = findViewById(R.id.album);
    setSupportActionBar(toolbar);

    songId = "";
    tvLyric = this.findViewById(R.id.lyric_content);

    if (!NotificationUtils.isNotificationListenerEnable(this)) {
      NotificationUtils.toggleNotificationListenerService(this);
      NotificationUtils.gotoNotificationSetting(this);
    }

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//          .setAction("Action", null).show();
        if (spotifyBroadcastReceiver.getAlbumName() == null ||
            spotifyBroadcastReceiver.getArtistName() == null ||
            spotifyBroadcastReceiver.getTrackName() == null) {
          Log.d(TAG, "Song's Information Not Complete!");
        } else {
          // search song
          getLyrics();
        }
      }
    });

    // 透明状态栏
    View decorView = getWindow().getDecorView();
    int option = View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    decorView.setSystemUiVisibility(option);
    getWindow().setStatusBarColor(Color.TRANSPARENT);

    subcribeSpotifyBroadcast(this);
  }

  private void getLyrics() {
//    searchLyric(String.format("%s %s %s", spotifyBroadcastReceiver.getAlbumName(),
//        spotifyBroadcastReceiver.getTrackName(),
//        spotifyBroadcastReceiver.getArtistName()));
    getLyricsOneStep(String.format("%s %s %s", spotifyBroadcastReceiver.getAlbumName(),
        spotifyBroadcastReceiver.getTrackName(),
        spotifyBroadcastReceiver.getArtistName()));
  }

  @Override
  protected void onResume() {
    super.onResume();
//    getLyrics();
  }

  private void subcribeSpotifyBroadcast(Context context) {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(BroadcastTypes.PLAYBACK_STATE_CHANGED);
    intentFilter.addAction(BroadcastTypes.METADATA_CHANGED);
    intentFilter.addAction(BroadcastTypes.QUEUE_CHANGED);
    spotifyBroadcastReceiver = new SpotifyBroadcastReceiver();

    context.registerReceiver(spotifyBroadcastReceiver, intentFilter);
  }

  /**
   * 查找当前播放的歌曲并获取歌词和专辑图片
   * @param keywords
   */
  private void getLyricsOneStep(String keywords) {
    netRequest.searchSong(keywords)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(new Consumer<Song>() {
          @Override
          public void accept(Song song) throws Exception {
            // update lyric content
            if (song.getResult().getSongCount() <= 0) {
              // Not found any results.
              songId = "";
              albumId = "";
              tvLyric.setText(R.string.not_found_lyric);
            } else {
              // Find the first result.
              songId = song.getResult().getSongs().get(0).getId();
              albumId = song.getResult().getSongs().get(0).getAlbum().getId();
              tvLyric.setText("");
              if (songId.equals("")) {
                Log.d(TAG, "Don't find any result!");
              }
            }
          }
        })
        .observeOn(Schedulers.io())
        .flatMap(new Function<Song, ObservableSource<Lyric>>() {
          @Override
          public ObservableSource<Lyric> apply(Song song) throws Exception {
            return netRequest.getLyrics(songId);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(new Consumer<Lyric>() {
          @Override
          public void accept(Lyric lyric) throws Exception {
            tvLyric.setText(R.string.not_found_lyric);
            if (lyric.getLrc() != null) {
              String lyricsOrignal = lyric.getLrc().getLyric();
              String lyricsTranslate = lyric.getTlyric().getLyric();
              String lyrics = RegexParser.concatTranslateLyrics(lyricsOrignal, lyricsTranslate);
              lyrics = RegexParser.beautifyLyric(lyrics);
              tvLyric.setText(lyrics);
              Log.d(TAG, lyrics);
            }
          }
        })
        .observeOn(Schedulers.io())
        .flatMap(new Function<Lyric, ObservableSource<Album>>() {
          @Override
          public ObservableSource<Album> apply(Lyric lyric) throws Exception {
            return netRequest.getAlbum(albumId);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(new Consumer<Album>() {
          @Override
          public void accept(Album album) throws Exception {
            // 获取图片url
            imageUrl = album.getAlbum().getPicUrl();
          }
        })
        .observeOn(Schedulers.io())
        .flatMap(new Function<Album, ObservableSource<ResponseBody>>() {
          @Override
          public ObservableSource<ResponseBody> apply(Album album) throws Exception {
            return netRequest.downloadImage(imageUrl);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<ResponseBody>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(ResponseBody value) {
            // 绘制图片
            Bitmap bitmap = BitmapFactory.decodeStream(value.byteStream());
            album.setImageBitmap(bitmap);
          }

          @Override
          public void onError(Throwable e) {
          }

          @Override
          public void onComplete() {
          }
        });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.menu_scrolling, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(spotifyBroadcastReceiver);
  }

  static final class BroadcastTypes {
    static final String SPOTIFY_PACKAGE = "com.spotify.music";
    static final String PLAYBACK_STATE_CHANGED = SPOTIFY_PACKAGE + ".playbackstatechanged";
    static final String QUEUE_CHANGED = SPOTIFY_PACKAGE + ".queuechanged";
    static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
  }

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
        getLyricsOneStep(String.format("%s %s %s", artistName, albumName, trackName));
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
}
