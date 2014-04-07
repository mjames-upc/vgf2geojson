/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class SymbolPatternMapEntry
/*    */ {
/*    */ 
/*    */   @XmlElement
/*    */   private String patternId;
/*    */ 
/*    */   @XmlElement(name="symbolPattern")
/*    */   private SymbolPattern pattern;
/*    */ 
/*    */   public SymbolPatternMapEntry()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SymbolPatternMapEntry(String patternId, SymbolPattern pattern)
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
/*    */   public SymbolPattern getPattern()
/*    */   {
/* 70 */     return this.pattern;
/*    */   }
/*    */ 
/*    */   public void setPattern(SymbolPattern pattern)
/*    */   {
/* 76 */     this.pattern = pattern;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.SymbolPatternMapEntry
 * JD-Core Version:    0.6.2
 */