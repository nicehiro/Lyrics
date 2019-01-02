package nicehiro.org.lyrics.model;

public class Lyric {

  /**
   * sgc : false
   * sfy : false
   * qfy : false
   * lrc : {"version":5,"lyric":"[00:04.050]\n[00:12.570]难以忘记初次见你\n[00:16.860]一双迷人的眼睛\n[00:21.460]在我脑海里\n[00:23.960]你的身影 挥散不去\n[00:30.160]握你的双手感觉你的温柔\n[00:34.940]真的有点透不过气\n[00:39.680]你的天真 我想珍惜\n[00:43.880]看到你受委屈 我会伤心\n[00:48.180]喔\n[00:50.340]只怕我自己会爱上你\n[00:55.070]不敢让自己靠的太近\n[00:59.550]怕我没什么能够给你\n[01:04.030]爱你也需要很大的勇气\n[01:08.190]只怕我自己会爱上你\n[01:12.910]也许有天会情不自禁\n[01:17.380]想念只让自己苦了自己\n[01:21.840]爱上你是我情非得已\n[01:28.810]难以忘记初次见你\n[01:33.170]一双迷人的眼睛\n[01:37.700]在我脑海里 你的身影 挥散不去\n[01:46.360]握你的双手感觉你的温柔\n[01:51.120]真的有点透不过气\n[01:55.910]你的天真 我想珍惜\n[02:00.150]看到你受委屈 我会伤心\n[02:04.490]喔\n[02:06.540]只怕我自己会爱上你\n[02:11.240]不敢让自己靠的太近\n[02:15.750]怕我没什么能够给你\n[02:20.200]爱你也需要很大的勇气\n[02:24.570]只怕我自己会爱上你\n[02:29.230]也许有天会情不自禁\n[02:33.680]想念只让自己苦了自己\n[02:38.140]爱上你是我情非得已\n[03:04.060]什么原因 耶\n[03:07.730]我竟然又会遇见你\n[03:13.020]我真的真的不愿意\n[03:16.630]就这样陷入爱的陷阱\n[03:20.700]喔\n[03:22.910]只怕我自己会爱上你\n[03:27.570]不敢让自己靠的太近\n[03:32.040]怕我没什么能够给你\n[03:36.560]爱你也需要很大的勇气\n[03:40.740]只怕我自己会爱上你\n[03:45.460]也许有天会情不自禁\n[03:49.990]想念只让自己苦了自己\n[03:54.510]爱上你是我情非得已\n[03:58.970]爱上你是我情非得已\n[04:03.000]\n"}
   * klyric : {"version":1,"lyric":null}
   * tlyric : {"version":0}
   * code : 200
   */

  private boolean sgc;
  private boolean sfy;
  private boolean qfy;
  private LrcBean lrc;
  private KlyricBean klyric;
  private TlyricBean tlyric;
  private int code;

  public boolean isSgc() {
    return sgc;
  }

  public void setSgc(boolean sgc) {
    this.sgc = sgc;
  }

  public boolean isSfy() {
    return sfy;
  }

  public void setSfy(boolean sfy) {
    this.sfy = sfy;
  }

  public boolean isQfy() {
    return qfy;
  }

  public void setQfy(boolean qfy) {
    this.qfy = qfy;
  }

  public LrcBean getLrc() {
    return lrc;
  }

  public void setLrc(LrcBean lrc) {
    this.lrc = lrc;
  }

  public KlyricBean getKlyric() {
    return klyric;
  }

  public void setKlyric(KlyricBean klyric) {
    this.klyric = klyric;
  }

  public TlyricBean getTlyric() {
    return tlyric;
  }

  public void setTlyric(TlyricBean tlyric) {
    this.tlyric = tlyric;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public static class LrcBean {
    /**
     * version : 5
     * lyric : [00:04.050]
     [00:12.570]难以忘记初次见你
     [00:16.860]一双迷人的眼睛
     [00:21.460]在我脑海里
     [00:23.960]你的身影 挥散不去
     [00:30.160]握你的双手感觉你的温柔
     [00:34.940]真的有点透不过气
     [00:39.680]你的天真 我想珍惜
     [00:43.880]看到你受委屈 我会伤心
     [00:48.180]喔
     [00:50.340]只怕我自己会爱上你
     [00:55.070]不敢让自己靠的太近
     [00:59.550]怕我没什么能够给你
     [01:04.030]爱你也需要很大的勇气
     [01:08.190]只怕我自己会爱上你
     [01:12.910]也许有天会情不自禁
     [01:17.380]想念只让自己苦了自己
     [01:21.840]爱上你是我情非得已
     [01:28.810]难以忘记初次见你
     [01:33.170]一双迷人的眼睛
     [01:37.700]在我脑海里 你的身影 挥散不去
     [01:46.360]握你的双手感觉你的温柔
     [01:51.120]真的有点透不过气
     [01:55.910]你的天真 我想珍惜
     [02:00.150]看到你受委屈 我会伤心
     [02:04.490]喔
     [02:06.540]只怕我自己会爱上你
     [02:11.240]不敢让自己靠的太近
     [02:15.750]怕我没什么能够给你
     [02:20.200]爱你也需要很大的勇气
     [02:24.570]只怕我自己会爱上你
     [02:29.230]也许有天会情不自禁
     [02:33.680]想念只让自己苦了自己
     [02:38.140]爱上你是我情非得已
     [03:04.060]什么原因 耶
     [03:07.730]我竟然又会遇见你
     [03:13.020]我真的真的不愿意
     [03:16.630]就这样陷入爱的陷阱
     [03:20.700]喔
     [03:22.910]只怕我自己会爱上你
     [03:27.570]不敢让自己靠的太近
     [03:32.040]怕我没什么能够给你
     [03:36.560]爱你也需要很大的勇气
     [03:40.740]只怕我自己会爱上你
     [03:45.460]也许有天会情不自禁
     [03:49.990]想念只让自己苦了自己
     [03:54.510]爱上你是我情非得已
     [03:58.970]爱上你是我情非得已
     [04:03.000]

     */

    private int version;
    private String lyric;

    public int getVersion() {
      return version;
    }

    public void setVersion(int version) {
      this.version = version;
    }

    public String getLyric() {
      return lyric;
    }

    public void setLyric(String lyric) {
      this.lyric = lyric;
    }
  }

  public static class KlyricBean {
    /**
     * version : 1
     * lyric : null
     */

    private int version;
    private Object lyric;

    public int getVersion() {
      return version;
    }

    public void setVersion(int version) {
      this.version = version;
    }

    public Object getLyric() {
      return lyric;
    }

    public void setLyric(Object lyric) {
      this.lyric = lyric;
    }
  }

  public static class TlyricBean {
    /**
     * version : 0
     */

    private int version;

    public String getLyric() {
      return lyric;
    }

    public void setLyric(String tlyric) {
      this.lyric = tlyric;
    }

    private String lyric;

    public int getVersion() {
      return version;
    }

    public void setVersion(int version) {
      this.version = version;
    }
  }
}
