package nicehiro.org.lyrics.model;

public class LyricTime {

  private String minute;

  private String second;

  private String millSecond;

  private String lyric;

  public String getMinute() {
    return minute;
  }

  public void setMinute(String minute) {
    this.minute = minute;
  }

  public String getSecond() {
    return second;
  }

  public void setSecond(String second) {
    this.second = second;
  }

  public String getMillSecond() {
    return millSecond;
  }

  public void setMillSecond(String millSecond) {
    this.millSecond = millSecond;
  }

  public String getLyric() {
    return lyric;
  }

  public void setLyric(String lyric) {
    this.lyric = lyric;
  }
}
