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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser {

  private static String TAG = "REGEX-PARSER";

  private static String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,3})\\]";

  private static Pattern pattern = Pattern.compile(regex);

  public static List<Map> parseLyricsToList(String lyrics) {
    List<Map> lyricsList = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(
      new InputStreamReader(
        new ByteArrayInputStream(lyrics.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")))) {
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        if (!line.trim().equals("")) {
          Matcher matcher = pattern.matcher(line);
          Map<Long, String> lyricsMap = new HashMap<>();
          while (matcher.find()) {
            String minute = matcher.group(1);
            String second = matcher.group(2);
            String millSecond = matcher.group(3);
            Long time = getLongTime(minute, second, millSecond);
            String text = line.substring(matcher.end());
            lyricsMap.put(time, text);
            lyricsList.add(lyricsMap);
          }
        }
      }
    } catch (IOException e) {
      Log.d(TAG, "No Lyrics found!");
    }

    return lyricsList;
  }

  /**
   * unsorted lyrics but search faster.
   * @param lyrics
   * @return
   * @throws IOException
   */
  private static Map<Long, String> parseLyricsToMap(String lyrics) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(
      new InputStreamReader(
        new ByteArrayInputStream(lyrics.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
    String line;

    Map<Long, String> lyricsMap = new HashMap();

    while ((line = bufferedReader.readLine()) != null) {
      if (!line.trim().equals("")) {
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
          String minute = matcher.group(1);
          String second = matcher.group(2);
          String millSecond = matcher.group(3);
          Long time = getLongTime(minute, second, millSecond);
          String text = line.substring(matcher.end());
          lyricsMap.put(time, text);
        }
      }
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

  public static String concatLyric(List<Map> lyricsList) {
    String lyrics = "";
    for (Map<Long, String> lyricMap : lyricsList) {
      for (Map.Entry<Long, String> entry : lyricMap.entrySet()) {
        lyrics += entry.getValue() + "\n";
      }
    }
    return lyrics;
  }

  public static String concatTranslateLyrics(String lyricsOriginal, String lyricsTranslate) {
    String lyrics = "";
    if (lyricsTranslate == null || lyricsTranslate.equals("")) {
      return parseLyricToString(lyricsOriginal);
    }
    try {
      List<Map> lyricsOriList = parseLyricsToList(lyricsOriginal);
         Map<Long, String> lyricTransMap = lyricTransMap = parseLyricsToMap(lyricsTranslate);
      lyrics = concatTranslateLyrics(lyricsOriList, lyricTransMap);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lyrics;
  }

  public static String concatTranslateLyrics(List<Map> lyricsOriList, Map<Long, String> lyricsTransMap) {
    String lyrics = "";
    for (Map<Long, String> lyricMap : lyricsOriList) {
      for (Map.Entry<Long, String> entry : lyricMap.entrySet()) {
        lyrics += entry.getValue() + "\n" + lyricsTransMap.get(entry.getKey()) + "\n";
      }
    }
    return lyrics;
  }

  public static String parseLyricToString(String lyricsOrignal) {
    String lyrics = "";
    lyrics = concatLyric(parseLyricsToList(lyricsOrignal));

    return lyrics;
  }
}
