/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class LinePatternMapEntry
/*    */ {
/*    */ 
/*    */   @XmlElement
/*    */   private String patternId;
/*    */ 
/*    */   @XmlElement(name="linePattern")
/*    */   private LinePattern pattern;
/*    */ 
/*    */   public LinePatternMapEntry()
/*    */   {
/*    */   }
/*    */ 
/*    */   public LinePatternMapEntry(String patternId, LinePattern pattern)
/*    */   {
/* 50 */     this.patternId = patternId;
/* 51 */     this.pattern = pattern;
/*    */   }
/*    */ 
/*    */   public String getPatternId()
/*    */   {
/* 58 */     return this.patternId;
/*    */   }
/*    */ 
/*    */   public void setPatternId(String patternId)
/*    */   {
/* 64 */     this.patternId = patternId;
/*    */   }
/*    */ 
/*    */   public LinePattern getPattern()
/*    */   {
/* 70 */     return this.pattern;
/*    */   }
/*    */ 
/*    */   public void setPattern(LinePattern pattern)
/*    */   {
/* 76 */     this.pattern = pattern;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.LinePatternMapEntry
 * JD-Core Version:    0.6.2
 */