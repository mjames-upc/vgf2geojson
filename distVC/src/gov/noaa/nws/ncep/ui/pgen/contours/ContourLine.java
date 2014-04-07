/*     */ package gov.noaa.nws.ncep.ui.pgen.contours;
/*     */ 
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.FillPatternList.FillPattern;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.FontStyle;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextJustification;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.TextRotation;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ContourLine extends DECollection
/*     */ {
/*     */   private String[] labelString;
/*     */   private int numOfLabels;
/*     */ 
/*     */   public ContourLine()
/*     */   {
/*  53 */     super("ContourLine");
/*  54 */     this.numOfLabels = 0;
/*  55 */     this.labelString = null;
/*     */   }
/*     */ 
/*     */   public ContourLine(ArrayList<Coordinate> linePoints, boolean closed, String[] text)
/*     */   {
/*  63 */     super("ContourLine");
/*     */ 
/*  65 */     Line cline = new Line(null, new Color[] { Color.red }, 2.0F, 1.0D, closed, 
/*  66 */       false, linePoints, 2, FillPatternList.FillPattern.SOLID, "Lines", "LINE_SOLID");
/*     */ 
/*  68 */     cline.setParent(this);
/*     */ 
/*  70 */     add(cline);
/*     */ 
/*  72 */     for (String str : text)
/*     */     {
/*  74 */       Text lbl = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/*  75 */         null, 0.0D, IText.TextRotation.SCREEN_RELATIVE, new String[] { str }, 
/*  76 */         IText.FontStyle.REGULAR, Color.GREEN, 0, 0, true, IText.DisplayType.NORMAL, 
/*  77 */         "Text", "General Text");
/*  78 */       lbl.setLocation((Coordinate)linePoints.get(linePoints.size() / 2));
/*  79 */       lbl.setAuto(Boolean.valueOf(true));
/*  80 */       lbl.setParent(this);
/*     */ 
/*  82 */       add(lbl);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ContourLine(ArrayList<Coordinate> linePoints, boolean closed, String[] text, int nlabels)
/*     */   {
/*  93 */     super("ContourLine");
/*     */ 
/*  95 */     this.numOfLabels = nlabels;
/*     */ 
/*  97 */     this.labelString = new String[text.length];
/*  98 */     for (int ii = 0; ii < text.length; ii++) {
/*  99 */       this.labelString[ii] = new String(text[ii]);
/*     */     }
/*     */ 
/* 102 */     Line cline = new Line(null, new Color[] { Color.red }, 2.0F, 1.0D, closed, 
/* 103 */       false, linePoints, 2, FillPatternList.FillPattern.SOLID, "Lines", "LINE_SOLID");
/*     */ 
/* 105 */     cline.setParent(this);
/*     */ 
/* 107 */     add(cline);
/*     */ 
/* 109 */     for (int ii = 0; ii < nlabels; ii++)
/*     */     {
/* 111 */       Text lbl = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/* 112 */         null, 0.0D, IText.TextRotation.SCREEN_RELATIVE, this.labelString, 
/* 113 */         IText.FontStyle.REGULAR, Color.GREEN, 0, 0, true, IText.DisplayType.NORMAL, 
/* 114 */         "Text", "General Text");
/*     */ 
/* 116 */       lbl.setLocation((Coordinate)linePoints.get(linePoints.size() / 2));
/* 117 */       lbl.setAuto(Boolean.valueOf(true));
/* 118 */       lbl.setParent(this);
/*     */ 
/* 120 */       add(lbl);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ContourLine(Line line, Text text, int nlabels)
/*     */   {
/* 131 */     super("ContourLine");
/*     */ 
/* 133 */     this.numOfLabels = nlabels;
/*     */ 
/* 135 */     if (line != null) {
/* 136 */       Line cline = (Line)line.copy();
/*     */ 
/* 138 */       cline.setParent(this);
/*     */ 
/* 140 */       add(cline);
/*     */ 
/* 142 */       if ((text != null) && (nlabels > 0))
/*     */       {
/* 144 */         this.labelString = new String[text.getText().length];
/* 145 */         for (int ii = 0; ii < text.getText().length; ii++) {
/* 146 */           this.labelString[ii] = new String(text.getText()[ii]);
/*     */         }
/*     */ 
/* 149 */         for (int ii = 0; ii < nlabels; ii++)
/*     */         {
/* 151 */           Text lbl = (Text)text.copy();
/*     */ 
/* 153 */           ArrayList pts = cline.getPoints();
/*     */ 
/* 155 */           lbl.setLocation((Coordinate)pts.get(pts.size() / 2));
/* 156 */           lbl.setAuto(Boolean.valueOf(true));
/* 157 */           lbl.setParent(this);
/*     */ 
/* 159 */           add(lbl);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public ContourLine copy()
/*     */   {
/* 173 */     ContourLine cline = new ContourLine();
/*     */ 
/* 175 */     cline.numOfLabels = this.numOfLabels;
/* 176 */     cline.labelString = new String[this.labelString.length];
/* 177 */     for (int ii = 0; ii < this.labelString.length; ii++) {
/* 178 */       cline.labelString[ii] = new String(this.labelString[ii]);
/*     */     }
/*     */ 
/* 182 */     Iterator iterator = createDEIterator();
/*     */ 
/* 184 */     while (iterator.hasNext()) {
/* 185 */       DrawableElement de = (DrawableElement)((DrawableElement)iterator.next()).copy();
/* 186 */       de.setParent(cline);
/* 187 */       cline.add(de);
/*     */     }
/*     */ 
/* 190 */     return cline;
/*     */   }
/*     */ 
/*     */   public ArrayList<Text> getLabels()
/*     */   {
/* 198 */     Iterator iterator = createDEIterator();
/*     */ 
/* 200 */     ArrayList labels = new ArrayList();
/* 201 */     while (iterator.hasNext()) {
/* 202 */       DrawableElement de = (DrawableElement)iterator.next();
/* 203 */       if ((de instanceof Text)) {
/* 204 */         labels.add((Text)de);
/*     */       }
/*     */     }
/*     */ 
/* 208 */     return labels;
/*     */   }
/*     */ 
/*     */   public String[] getLabelString()
/*     */   {
/* 216 */     return this.labelString;
/*     */   }
/*     */ 
/*     */   public int getNumOfLabels()
/*     */   {
/* 223 */     return this.numOfLabels;
/*     */   }
/*     */ 
/*     */   public void updateLabelString(String[] label)
/*     */   {
/* 232 */     setLabelString(label);
/*     */ 
/* 234 */     for (Text lbl : getLabels())
/* 235 */       lbl.setText(this.labelString);
/*     */   }
/*     */ 
/*     */   public void updateNumOfLabels(int nlabels)
/*     */   {
/* 245 */     if (nlabels == getNumOfLabels()) {
/* 246 */       return;
/*     */     }
/*     */ 
/* 249 */     if (getNumOfLabels() > 0)
/*     */     {
/* 251 */       Text oldLabel = (Text)((Text)getLabels().get(0)).copy();
/*     */ 
/* 253 */       for (Text lbl : getLabels()) {
/* 254 */         removeElement(lbl);
/*     */       }
/*     */ 
/* 257 */       for (int ii = 0; ii < nlabels; ii++)
/*     */       {
/* 259 */         Text lbl = (Text)oldLabel.copy();
/*     */ 
/* 261 */         lbl.setAuto(Boolean.valueOf(true));
/*     */ 
/* 263 */         lbl.setParent(this);
/*     */ 
/* 265 */         add(lbl);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 272 */       for (int ii = 0; ii < nlabels; ii++)
/*     */       {
/* 274 */         Text lbl = new Text(null, "Courier", 14.0F, IText.TextJustification.CENTER, 
/* 275 */           null, 0.0D, IText.TextRotation.SCREEN_RELATIVE, this.labelString, 
/* 276 */           IText.FontStyle.REGULAR, Color.GREEN, 0, 0, true, IText.DisplayType.NORMAL, 
/* 277 */           "Text", "General Text");
/*     */ 
/* 279 */         lbl.setLocation((Coordinate)getLine().getPoints().get(0));
/*     */ 
/* 281 */         lbl.setAuto(Boolean.valueOf(true));
/*     */ 
/* 283 */         lbl.setParent(this);
/*     */ 
/* 285 */         add(lbl);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 291 */     setNumOfLabels(nlabels);
/*     */   }
/*     */ 
/*     */   public Line getLine()
/*     */   {
/* 300 */     Iterator iterator = createDEIterator();
/*     */ 
/* 302 */     Line cline = null;
/* 303 */     while (iterator.hasNext()) {
/* 304 */       DrawableElement de = (DrawableElement)iterator.next();
/* 305 */       if ((de instanceof Line)) {
/* 306 */         cline = (Line)de;
/* 307 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 311 */     return cline;
/*     */   }
/*     */ 
/*     */   public ArrayList<ContourLine> split(int start, int end)
/*     */   {
/* 320 */     ArrayList newContourlines = new ArrayList();
/*     */ 
/* 322 */     ArrayList newLines = splitLine(getLine(), start, end);
/*     */ 
/* 324 */     for (Line ln : newLines) {
/* 325 */       ContourLine ncline = copy();
/* 326 */       ncline.getLine().setClosed(ln.isClosedLine());
/* 327 */       ncline.getLine().setLinePoints(ln.getPoints());
/* 328 */       newContourlines.add(ncline);
/*     */     }
/*     */ 
/* 331 */     return newContourlines;
/*     */   }
/*     */ 
/*     */   public ArrayList<ContourLine> split(Coordinate start, Coordinate end)
/*     */   {
/* 340 */     ArrayList newContourlines = new ArrayList();
/*     */ 
/* 342 */     Line ln = getLine();
/* 343 */     ArrayList pts = ln.getPoints();
/* 344 */     boolean closed = ln.isClosedLine().booleanValue();
/* 345 */     List newLines = PgenUtil.deleteLinePart(pts, closed, start, end);
/*     */ 
/* 347 */     for (ArrayList newLn : newLines) {
/* 348 */       ContourLine ncline = copy();
/* 349 */       ncline.getLine().setClosed(Boolean.valueOf(false));
/* 350 */       ncline.getLine().setLinePoints(newLn);
/* 351 */       newContourlines.add(ncline);
/*     */     }
/*     */ 
/* 354 */     return newContourlines;
/*     */   }
/*     */ 
/*     */   private ArrayList<Line> splitLine(Line element, int start, int end)
/*     */   {
/*     */     ArrayList newlns;
/*     */     ArrayList newlns;
/* 366 */     if (element.isClosedLine().booleanValue()) {
/* 367 */       newlns = removePartFromClosedLine(element, start, end);
/*     */     }
/*     */     else {
/* 370 */       newlns = removePartFromOpenLine(element, start, end);
/*     */     }
/*     */ 
/* 373 */     return newlns;
/*     */   }
/*     */ 
/*     */   private ArrayList<Line> removePartFromOpenLine(Line line, int pt1Index, int pt2Index)
/*     */   {
/* 382 */     ArrayList newLines = new ArrayList();
/* 383 */     Line element1 = null; Line element2 = null;
/*     */ 
/* 385 */     List points = line.getPoints();
/*     */ 
/* 387 */     if (Math.abs(pt2Index - pt1Index) + 1 != points.size())
/*     */     {
/* 390 */       if ((pt1Index == 0) || (pt2Index == points.size() - 1))
/*     */       {
/* 393 */         element1 = (Line)line.copy();
/* 394 */         ArrayList newPts = new ArrayList(points);
/*     */ 
/* 396 */         if (pt1Index == 0) {
/* 397 */           newPts.subList(pt1Index, pt2Index).clear();
/*     */         }
/* 399 */         else if (pt2Index == points.size() - 1) {
/* 400 */           newPts.subList(pt1Index + 1, pt2Index + 1).clear();
/*     */         }
/*     */ 
/* 403 */         element1.setPoints(newPts);
/* 404 */         newLines.add(element1);
/*     */       }
/*     */       else
/*     */       {
/* 410 */         element1 = (Line)line.copy();
/* 411 */         element1.setPoints(new ArrayList(points.subList(0, pt1Index + 1)));
/*     */ 
/* 413 */         element2 = (Line)line.copy();
/* 414 */         element2.setPoints(new ArrayList(points.subList(pt2Index, points.size())));
/*     */ 
/* 416 */         newLines.add(element1);
/* 417 */         newLines.add(element2);
/*     */       }
/*     */     }
/* 420 */     return newLines;
/*     */   }
/*     */ 
/*     */   private ArrayList<Line> removePartFromClosedLine(Line element, int pt1Index, int pt2Index)
/*     */   {
/* 428 */     ArrayList newLines = new ArrayList();
/* 429 */     Line element1 = null;
/*     */ 
/* 431 */     List points = element.getPoints();
/*     */ 
/* 433 */     element1 = (Line)element.copy();
/* 434 */     element1.setClosed(Boolean.valueOf(false));
/* 435 */     element1.getPoints().clear();
/*     */ 
/* 437 */     if (pt2Index - pt1Index + 1 > points.size() - (pt2Index - pt1Index + 1) + 2)
/*     */     {
/* 439 */       element1.getPoints().addAll(points.subList(pt1Index, pt2Index + 1));
/*     */     }
/*     */     else {
/* 442 */       element1.getPoints().addAll(points.subList(pt2Index, points.size()));
/* 443 */       element1.getPoints().addAll(points.subList(0, pt1Index + 1));
/*     */     }
/*     */ 
/* 446 */     newLines.add(element1);
/*     */ 
/* 448 */     return newLines;
/*     */   }
/*     */ 
/*     */   public void setNumOfLabels(int nlabels)
/*     */   {
/* 457 */     this.numOfLabels = nlabels;
/*     */   }
/*     */ 
/*     */   public void setLabelString(String[] label)
/*     */   {
/* 466 */     if (label != null)
/*     */     {
/* 468 */       this.labelString = new String[label.length];
/* 469 */       for (int ii = 0; ii < label.length; ii++)
/* 470 */         this.labelString[ii] = new String(label[ii]);
/*     */     }
/*     */     else
/*     */     {
/* 474 */       this.labelString = new String[1];
/* 475 */       this.labelString[0] = "";
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.contours.ContourLine
 * JD-Core Version:    0.6.2
 */