package nicehiro.org.lyrics.request;

import io.reactivex.Observable;
import nicehiro.org.lyrics.model.Album;
import nicehiro.org.lyrics.model.Lyric;
import nicehiro.org.lyrics.model.Song;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface NetRequest {

  @GET("lyric")
  Observable<Lyric> getLyrics(@Query("id") String songId);

  @GET("search")
  Observable<Song> searchSong(@Query("keywords") String keyWords);

  @GET("album")
  Observable<Album> getAlbum(@Query("id") String albumId);

  /**
   * 下载图片
   * @param imageUrl 图片url
   * @return Observable
   */
  @Streaming
  @GET
  Observable<ResponseBody> downloadImage(@Url String imageUrl);
}
