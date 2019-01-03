package nicehiro.org.lyrics;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nicehiro.org.lyrics.model.Lyric;
import nicehiro.org.lyrics.model.Song;
import nicehiro.org.lyrics.receiver.SpotifyBroadcastReceiver;
import nicehiro.org.lyrics.request.NetRequest;
import nicehiro.org.lyrics.utils.NotificationUtils;
import nicehiro.org.lyrics.utils.RegexParser;
import nicehiro.org.lyrics.utils.RetrofitUtil;

public class ScrollingActivity extends AppCompatActivity {

  private static final String TAG = "MAIN_ACTIVITY";

  private String songId;

  private SpotifyBroadcastReceiver spotifyBroadcastReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scrolling);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    songId = "";

    if (!NotificationUtils.isNotificationListenerEnable(this)) {
      NotificationUtils.toggleNotificationListenerService(this);
      NotificationUtils.gotoNotificationSetting(this);
    }

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
          searchLyric(String.format("%s %s %s", spotifyBroadcastReceiver.getAlbumName(),
            spotifyBroadcastReceiver.getTrackName(),
            spotifyBroadcastReceiver.getArtistName()));
        }
      }
    });

    subcribeSpotifyBroadcast(this);
  }

  private void subcribeSpotifyBroadcast(Context context) {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("com.spotify.music.playbackstatechanged");
    intentFilter.addAction("com.spotify.music.metadatachanged");
    intentFilter.addAction("com.spotify.music.queuechanged");
    spotifyBroadcastReceiver = new SpotifyBroadcastReceiver();

    context.registerReceiver(spotifyBroadcastReceiver, intentFilter);
  }

  private void getLyrics(String songId) {
    final TextView tvLyric = this.findViewById(R.id.lyric_content);
    NetRequest netRequest = RetrofitUtil.getInstance().create(NetRequest.class);
    netRequest.getLyrics(songId)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Observer<Lyric>() {
        @Override
        public void onSubscribe(Disposable d) {
          Log.d(TAG, "Subscribe a request");
        }

        @Override
        public void onNext(Lyric value) {
          if (value.getLrc() != null) {
            String lyricsOrignal = value.getLrc().getLyric();
            String lyricsTranslate = value.getTlyric().getLyric();
            String lyrics = RegexParser.concatTranslateLyrics(lyricsOrignal, lyricsTranslate);
            lyrics = RegexParser.beautifyLyric(lyrics);
            tvLyric.setText(lyrics);
            Log.d(TAG, lyrics);
          }
        }

        @Override
        public void onError(Throwable e) {
          Log.d(TAG, "We got some troubles");
        }

        @Override
        public void onComplete() {
          Log.d(TAG, "Complete get a lyric!");
        }
      });
  }

  private void searchLyric(String keywords) {
    NetRequest netRequest = RetrofitUtil.getInstance().create(NetRequest.class);
    netRequest.searchSong(keywords)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Observer<Song>() {
        @Override
        public void onSubscribe(Disposable d) {
          Log.d(TAG, "Subscribe a request");
        }

        @Override
        public void onNext(Song value) {
          // update lyric content
          if (value.getResult().getSongCount() <= 0) {
            // Not found any results.
            songId = "";
          } else {
            // Find the first result.
            songId = value.getResult().getSongs().get(0).getId();
            if (songId.equals("")) {
              Log.d(TAG, "Don't find any result!");
            } else {
              // search lyric
              getLyrics(songId);
            }
          }
        }

        @Override
        public void onError(Throwable e) {
          Log.d(TAG, "We got some troubles");
        }

        @Override
        public void onComplete() {
          Log.d(TAG, "Complete search a song");
        }
      });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
}
