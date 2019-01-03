package nicehiro.org.lyrics.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nicehiro.org.lyrics.model.LyricTime;

public class RegexParser {

  private static String TAG = "REGEX-PARSER";

  private static String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]";

  private static Pattern pattern = Pattern.compile(regex);

  /**
   * unsorted lyrics but search faster.
   *
   * @param lyrics
   * @return
   * @throws IOException
   */
  private static Map<Long, String> parseLyricsToMap(String lyrics) {
    Map<Long, String> lyricsMap = new TreeMap<>();
    try {
      BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(
          new ByteArrayInputStream(lyrics.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
      String line;


      while ((line = bufferedReader.readLine()) != null) {
        if (!line.trim().equals("")) {
          Matcher matcher = pattern.matcher(line);
          List<LyricTime> lyricTimeList = new ArrayList<>();
          int i = 0;
          int end = 0;
          while (matcher.find()) {
            LyricTime lyricTime = new LyricTime();
            lyricTime.setMinute(matcher.group(1));
            lyricTime.setSecond(matcher.group(2));
            lyricTime.setMillSecond(matcher.group(3));
            lyricTimeList.add(lyricTime);
            end = matcher.end();
            i++;
          }
          String text = line.substring(end);
          if (text.equals("")) {
            text = "\n";
          }
          for (LyricTime lyricTime : lyricTimeList) {
            Long time = getLongTime(lyricTime.getMinute(), lyricTime.getSecond(), lyricTime.getMillSecond());
            lyricsMap.put(time, text);
          }
        }
      }
    } catch (IOException e) {
      Log.d(TAG, "Lyric parse wrong!");
    }
    return lyricsMap;
  }

  private static long getLongTime(String minute, String second, String millSecond) {
    int m = Integer.parseInt(minute);
    int s = Integer.parseInt(second);
    int ms = Integer.parseInt(millSecond);

    if (s >= 60) {
      Log.d(TAG, "Time's not right!");
    }

    long time = m * 60 * 1000 + s * 1000 + ms;
    return time;
  }

  public static String concatLyric(Map<Long, String> lyricsMap) {
    StringBuilder lyrics = new StringBuilder();
    for (Map.Entry<Long, String> entry : lyricsMap.entrySet()) {
      lyrics.append(entry.getValue()).append("\n");
    }
    return lyrics.toString();
  }

  public static String concatTranslateLyrics(String lyricsOriginal, String lyricsTranslate) {
    String lyrics = "";
    if (lyricsTranslate == null || lyricsTranslate.equals("")) {
      return parseLyricToString(lyricsOriginal);
    }
    Map<Long, String> lyricsOriMap =parseLyricsToMap(lyricsOriginal);
    Map<Long, String> lyricTransMap = lyricTransMap = parseLyricsToMap(lyricsTranslate);
    lyrics = concatTranslateLyrics(lyricsOriMap, lyricTransMap);
    return lyrics;
  }

  public static String concatTranslateLyrics(Map<Long, String> lyricsOriMap, Map<Long, String> lyricsTransMap) {
    String lyrics = "";
    for (Map.Entry<Long, String> entry : lyricsOriMap.entrySet()) {
      lyrics += entry.getValue() + "\n" + (lyricsTransMap.containsKey(entry.getKey()) ? lyricsTransMap.get(entry.getKey()) : "") + "\n";
    }
    return lyrics;
  }

  public static String beautifyLyric(String lyrics) {
    return lyrics.replaceAll("\n{2,}", "\n\n");
  }

  public static String parseLyricToString(String lyricsOrignal) {
    String lyrics = "";
    lyrics = concatLyric(parseLyricsToMap(lyricsOrignal));

    return lyrics;
  }
}
