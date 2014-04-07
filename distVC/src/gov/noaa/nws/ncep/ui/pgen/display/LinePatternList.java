/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ @XmlRootElement
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class LinePatternList
/*    */   implements ISerializableObject
/*    */ {
/*    */ 
/*    */   @XmlElement(name="patternEntry")
/*    */   private ArrayList<LinePatternMapEntry> patternList;
/*    */ 
/*    */   public LinePatternList()
/*    */   {
/*    */   }
/*    */ 
/*    */   public LinePatternList(HashMap<String, LinePattern> patternMap)
/*    */   {
/* 52 */     this.patternList = new ArrayList();
/* 53 */     for (String key : patternMap.keySet())
/* 54 */       this.patternList.add(new LinePatternMapEntry(key, (LinePattern)patternMap.get(key)));
/*    */   }
/*    */ 
/*    */   public ArrayList<LinePatternMapEntry> getPatternList()
/*    */   {
/* 62 */     return this.patternList;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.LinePatternList
 * JD-Core Version:    0.6.2
 */