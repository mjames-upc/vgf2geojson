/*     */ package gov.noaa.nws.ncep.ui.pgen.elements.tcm;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.SinglePointElement;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElements;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public class TcmFcst extends SinglePointElement
/*     */   implements ITcmFcst
/*     */ {
/*     */ 
/*     */   @XmlAttribute
/*     */   private int fcstHr;
/*     */ 
/*     */   @XmlAttribute
/*     */   private int gust;
/*     */ 
/*     */   @XmlAttribute
/*     */   private int windMax;
/*     */ 
/*     */   @XmlAttribute
/*     */   private int direction;
/*     */ 
/*     */   @XmlAttribute
/*     */   private int speed;
/*     */ 
/*     */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="winds", type=TcmWindQuarters.class)})
/*     */   private TcmWindQuarters[] winds;
/*     */ 
/*     */   public TcmFcst()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TcmFcst(Coordinate loc, int fcstHr, double[][] quatros)
/*     */   {
/*  60 */     this.fcstHr = fcstHr;
/*  61 */     setLocation(loc);
/*  62 */     this.winds = new TcmWindQuarters[3];
/*  63 */     this.winds[0] = new TcmWindQuarters(loc, 32, quatros[0][0], quatros[1][0], quatros[2][0], quatros[3][0]);
/*  64 */     this.winds[1] = new TcmWindQuarters(loc, 50, quatros[0][1], quatros[1][1], quatros[2][1], quatros[3][1]);
/*  65 */     this.winds[2] = new TcmWindQuarters(loc, 64, quatros[0][2], quatros[1][2], quatros[2][2], quatros[3][2]);
/*     */   }
/*     */ 
/*     */   private TcmFcst(TcmWindQuarters[] quatros)
/*     */   {
/*  70 */     this.winds = quatros;
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent copy()
/*     */   {
/*  75 */     TcmWindQuarters[] newWinds = new TcmWindQuarters[3];
/*  76 */     newWinds[0] = ((TcmWindQuarters)this.winds[0].copy());
/*  77 */     newWinds[1] = ((TcmWindQuarters)this.winds[1].copy());
/*  78 */     newWinds[2] = ((TcmWindQuarters)this.winds[2].copy());
/*     */ 
/*  80 */     TcmFcst newFcst = new TcmFcst(newWinds);
/*  81 */     newFcst.setLocation(getLocation());
/*  82 */     newFcst.direction = this.direction;
/*  83 */     newFcst.gust = this.gust;
/*  84 */     newFcst.speed = this.speed;
/*  85 */     newFcst.windMax = this.windMax;
/*  86 */     newFcst.fcstHr = this.fcstHr;
/*  87 */     return newFcst;
/*     */   }
/*     */ 
/*     */   public ITcmWindQuarter[] getQuarters()
/*     */   {
/*  92 */     return this.winds;
/*     */   }
/*     */ 
/*     */   public void setFcstHr(int fcstHr) {
/*  96 */     this.fcstHr = fcstHr;
/*     */   }
/*     */ 
/*     */   public int getFcstHr() {
/* 100 */     return this.fcstHr;
/*     */   }
/*     */ 
/*     */   public void setGust(int gust) {
/* 104 */     this.gust = gust;
/*     */   }
/*     */ 
/*     */   public int getGust() {
/* 108 */     return this.gust;
/*     */   }
/*     */ 
/*     */   public void setWindMax(int windMax) {
/* 112 */     this.windMax = windMax;
/*     */   }
/*     */ 
/*     */   public int getWindMax() {
/* 116 */     return this.windMax;
/*     */   }
/*     */ 
/*     */   public void setDirection(int direction) {
/* 120 */     this.direction = direction;
/*     */   }
/*     */ 
/*     */   public int getDirection() {
/* 124 */     return this.direction;
/*     */   }
/*     */ 
/*     */   public void setSpeed(int speed) {
/* 128 */     this.speed = speed;
/*     */   }
/*     */ 
/*     */   public int getSpeed() {
/* 132 */     return this.speed;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.tcm.TcmFcst
 * JD-Core Version:    0.6.2
 */