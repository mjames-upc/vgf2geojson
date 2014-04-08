package gov.noaa.nws.ncep.standalone.vgfConverter;

import com.sun.jna.Library;
import com.sun.jna.Structure;

public class WrapperC
{
  public static abstract interface VgfXml extends Library
  {
    public abstract int vgfToXml(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);

    public abstract int compareVgf(String paramString1, String paramString2);

    public abstract void printf(String paramString, Object[] paramArrayOfObject);

    public static class AshType extends Structure
    {
    }

    public static class CCFType extends Structure
    {
    }

    public static class CircType extends Structure
    {
    }

    public static class Elem extends Structure
    {
      public WrapperC.VgfXml.FileHeadType fhed;
      public WrapperC.VgfXml.FrontType frt;
      public WrapperC.VgfXml.LineType lin;
      public WrapperC.VgfXml.SpLineType spl;
      public WrapperC.VgfXml.WatchBoxType wbx;
      public WrapperC.VgfXml.WatchSMType wsm;
      public WrapperC.VgfXml.SymType sym;
      public WrapperC.VgfXml.WindType wnd;
      public WrapperC.VgfXml.TextType txt;
      public WrapperC.VgfXml.SptxType spt;
      public WrapperC.VgfXml.CircType cir;
      public WrapperC.VgfXml.TrackType trk;
      public WrapperC.VgfXml.SigmetType sig;
      public WrapperC.VgfXml.CCFType ccf;
      public WrapperC.VgfXml.ListType lst;
      public WrapperC.VgfXml.AshType ash;
      public WrapperC.VgfXml.VolType vol;
      public WrapperC.VgfXml.JetType jet;
      public WrapperC.VgfXml.GfaType gfa;
      public WrapperC.VgfXml.TcaType tca;
      public WrapperC.VgfXml.TcerrType tce;
      public WrapperC.VgfXml.TctrkType tct;
      public WrapperC.VgfXml.TcbklType tcb;
    }

    public static class FileHeadType extends Structure
    {
    }

    public static class FrontType extends Structure
    {
    }

    public static class GfaType extends Structure
    {
    }

    public static class JetType extends Structure
    {
    }

    public static class LineInfo extends Structure
    {
      public int numpts;
      public int lintyp;
      public int lthw;
      public int width;
      public int lwhw;
    }

    public static class LineType extends Structure
    {
      WrapperC.VgfXml.LineInfo info;
      public float[] latlon;
    }

    public static class ListType extends Structure
    {
    }

    public static class SigmetType extends Structure
    {
    }

    public static class SpLineInfo extends Structure
    {
      public int numpts;
      public int spltyp;
      public int splstr;
      public int spldir;
      public int splsiz;
      public int splwid;
    }

    public static class SpLineType extends Structure
    {
      WrapperC.VgfXml.SpLineInfo info;
      public float[] latlon;
    }

    public static class SptxType extends Structure
    {
    }

    public static class SymType extends Structure
    {
    }

    public static class TcaType extends Structure
    {
    }

    public static class TcbklType extends Structure
    {
    }

    public static class TcerrType extends Structure
    {
    }

    public static class TctrkType extends Structure
    {
    }

    public static class TextType extends Structure
    {
    }

    public static class TrackType extends Structure
    {
    }

    public static class VG_DBStruct extends Structure
    {
      public WrapperC.VgfXml.VG_HdrStruct hdr;
      public WrapperC.VgfXml.Elem elem;
    }

    public static class VG_HdrStruct extends Structure
    {
      public char delete;
      public char vg_type;
      public char vg_class;
      public char filled;
      public char closed;
      public char smooth;
      public char version;
      public char grptyp;
      public int grpnum;
      public int maj_col;
      public int min_col;
      public int recsz;
      public float range_min_lat;
      public float range_min_lon;
      public float range_max_lat;
      public float range_max_lon;
    }

    public static class VolType extends Structure
    {
    }

    public static class WatchBoxType extends Structure
    {
    }

    public static class WatchSMType extends Structure
    {
    }

    public static class WindType extends Structure
    {
    }
  }
}

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.standalone.vgfConverter.WrapperC
 * JD-Core Version:    0.6.2
 */