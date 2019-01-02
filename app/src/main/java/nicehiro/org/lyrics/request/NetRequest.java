package nicehiro.org.lyrics.request;

import io.reactivex.Observable;
import nicehiro.org.lyrics.model.Lyric;
import nicehiro.org.lyrics.model.Song;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetRequest {

  @GET("lyric")
  Observable<Lyric> getLyrics(@Query("id") String songId);

  @GET("search")
  Observable<Song> searchSong(@Query("keywords") String keyWords);
}
