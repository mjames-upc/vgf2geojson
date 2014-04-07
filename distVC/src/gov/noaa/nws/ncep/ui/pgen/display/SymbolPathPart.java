/*    */ package gov.noaa.nws.ncep.ui.pgen.display;
/*    */ 
/*    */ import com.vividsolutions.jts.geom.Coordinate;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.NONE)
/*    */ public class SymbolPathPart extends SymbolPart
/*    */ {
/*    */ 
/*    */   @XmlElement(name="path")
/*    */   @XmlJavaTypeAdapter(CoordinateArrayAdapter.class)
/*    */   private Coordinate[] path;
/*    */ 
/*    */   @XmlAttribute(name="isFilled")
/*    */   private boolean filled;
/*    */ 
/*    */   public SymbolPathPart()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SymbolPathPart(Coordinate[] path, boolean filled)
/*    */   {
/* 55 */     this.path = path;
/* 56 */     this.filled = filled;
/*    */   }
/*    */ 
/*    */   public Coordinate[] getPath()
/*    */   {
/* 64 */     return this.path;
/*    */   }
/*    */ 
/*    */   public void setPath(Coordinate[] path)
/*    */   {
/* 72 */     this.path = path;
/*    */   }
/*    */ 
/*    */   public boolean isFilled()
/*    */   {
/* 80 */     return this.filled;
/*    */   }
/*    */ 
/*    */   public void setFilled(boolean filled)
/*    */   {
/* 88 */     this.filled = filled;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.display.SymbolPathPart
 * JD-Core Version:    0.6.2
 */