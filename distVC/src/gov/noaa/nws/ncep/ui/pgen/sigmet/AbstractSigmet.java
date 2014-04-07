/*     */ package gov.noaa.nws.ncep.ui.pgen.sigmet;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class AbstractSigmet extends Line
/*     */   implements ISigmet
/*     */ {
/*     */   public static final String AREA = "Area";
/*     */   public static final String LINE = "Line";
/*     */   public static final String ISOLATED = "Isolated";
/*     */   private String type;
/*     */   private double width;
/*     */   private String editableAttrArea;
/*     */   private String editableAttrIssueOffice;
/*     */   private String editableAttrId;
/*     */   private String editableAttrSeqNum;
/*     */   private String editableAttrFromLine;
/*     */ 
/*     */   public AbstractSigmet()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AbstractSigmet(Coordinate[] range, Color[] colors, float lineWidth, double sizeScale, boolean closed, boolean filled, ArrayList<Coordinate> linePoints, int smoothFactor, FillPatternList.FillPattern fillPattern, String pgenCategory, String pgenType, String type, double width, String editableAttrArea, String editableAttrIssueOffice, String editableAttrFromLine, String editableAttrId, String editableAttrSeqNum)
/*     */   {
/*  76 */     super(range, colors, lineWidth, sizeScale, closed, filled, 
/*  76 */       linePoints, smoothFactor, fillPattern, pgenCategory, pgenType);
/*     */ 
/*  78 */     this.type = type;
/*  79 */     this.width = width;
/*     */ 
/*  81 */     this.editableAttrArea = editableAttrArea;
/*  82 */     this.editableAttrIssueOffice = editableAttrIssueOffice;
/*  83 */     this.editableAttrFromLine = editableAttrFromLine;
/*  84 */     this.editableAttrId = editableAttrId;
/*  85 */     this.editableAttrSeqNum = editableAttrSeqNum;
/*     */   }
/*     */ 
/*     */   public String getPatternName()
/*     */   {
/*  90 */     if ("CCFP_SIGMET".equalsIgnoreCase(getPgenType())) {
/*  91 */       if ("Line".equalsIgnoreCase(getType()))
/*  92 */         return "LINE_SOLID";
/*  93 */       if ("LineMed".equalsIgnoreCase(getType())) {
/*  94 */         return "LINE_DASHED_3";
/*     */       }
/*  96 */       return getPgenType();
/*     */     }
/*     */ 
/*  99 */     return getPgenType();
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 104 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getLineType() {
/* 108 */     return getType();
/*     */   }
/*     */ 
/*     */   public void setType(String type) {
/* 112 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public double getWidth() {
/* 116 */     return this.width;
/*     */   }
/*     */ 
/*     */   public void setWidth(double width)
/*     */   {
/* 121 */     this.width = width;
/*     */   }
/*     */ 
/*     */   public void setWidth(String aWidth) {
/*     */     try {
/* 126 */       setWidth(1852.0D * Double.parseDouble(aWidth));
/*     */     } catch (NumberFormatException e) {
/* 128 */       setWidth(18520.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getEditableAttrArea() {
/* 133 */     return this.editableAttrArea;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrArea(String editableAttrArea) {
/* 137 */     this.editableAttrArea = editableAttrArea;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrIssueOffice() {
/* 141 */     return this.editableAttrIssueOffice;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrIssueOffice(String editableAttrIssueOffice) {
/* 145 */     this.editableAttrIssueOffice = editableAttrIssueOffice;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrId() {
/* 149 */     return this.editableAttrId;
/*     */   }
/*     */   public void setEditableAttrId(String editableAttrId) {
/* 152 */     this.editableAttrId = editableAttrId;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrSeqNum() {
/* 156 */     return this.editableAttrSeqNum;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrSeqNum(String editableAttrSeqNum) {
/* 160 */     this.editableAttrSeqNum = editableAttrSeqNum;
/*     */   }
/*     */ 
/*     */   public String getEditableAttrFromLine() {
/* 164 */     return this.editableAttrFromLine;
/*     */   }
/*     */ 
/*     */   public void setEditableAttrFromLine(String editableAttrFromLine) {
/* 168 */     this.editableAttrFromLine = editableAttrFromLine;
/*     */   }
/*     */ 
/*     */   public boolean isWithTopText() {
/* 172 */     return (getPgenType().equalsIgnoreCase("CONV_SIGMET")) || (getPgenType().equalsIgnoreCase("OUTL_SIGMET"));
/*     */   }
/*     */ 
/*     */   public String getTopText()
/*     */   {
/* 177 */     if (getPgenType().equalsIgnoreCase("CONV_SIGMET")) {
/* 178 */       if ((getEditableAttrSeqNum() == null) || (getEditableAttrSeqNum().length() == 0)) {
/* 179 */         return "0E";
/*     */       }
/* 181 */       StringBuilder sb = new StringBuilder();
/* 182 */       sb.append(getEditableAttrSeqNum());
/* 183 */       if (getEditableAttrId() == null)
/* 184 */         sb.append("E");
/*     */       else {
/* 186 */         sb.append(getEditableAttrId().charAt(0));
/*     */       }
/* 188 */       return sb.toString();
/* 189 */     }if (getPgenType().equalsIgnoreCase("OUTL_SIGMET")) {
/* 190 */       if ((getEditableAttrSeqNum() == null) || (getEditableAttrSeqNum().length() == 0))
/* 191 */         return "0";
/* 192 */       return getEditableAttrSeqNum();
/*     */     }
/*     */ 
/* 195 */     return "";
/*     */   }
/*     */ 
/*     */   public String[] getDisplayTxt()
/*     */   {
/* 216 */     String[] ss = getType().split(":::");
/*     */ 
/* 218 */     return 
/* 220 */       new String[] { ss.length > 1 ? new String[] { ss[1] } : ss.length > 2 ? 
/* 219 */       new String[] { ss[1], ss[2] } : 
/* 220 */       ss[0] };
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.sigmet.AbstractSigmet
 * JD-Core Version:    0.6.2
 */