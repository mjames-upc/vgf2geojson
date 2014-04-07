/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TextAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IText.DisplayType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ComboSymbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.awt.geom.Line2D;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class PgenTextDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   private boolean addLabelToSymbol;
/*     */   private AbstractDrawableComponent prevElem;
/*     */   private boolean usePrevColor;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  74 */     super.activateTool();
/*     */ 
/*  76 */     if ((this.attrDlg != null) && (!isDelObj()))
/*     */     {
/*  78 */       String txt = "";
/*     */       String param;
/*  80 */       if ((param = this.event.getParameter("addLabel")) != null)
/*     */       {
/*  82 */         if (param.equalsIgnoreCase("true"))
/*  83 */           this.addLabelToSymbol = true;
/*  84 */         if ((this.event.getTrigger() instanceof AbstractDrawableComponent)) {
/*  85 */           this.prevElem = ((AbstractDrawableComponent)this.event.getTrigger());
/*  86 */           if (((this.prevElem.getParent() instanceof Outlook)) && 
/*  87 */             (((Outlook)this.prevElem.getParent()).getOutlookType().equalsIgnoreCase("MESO_DSC"))) {
/*  88 */             ((TextAttrDlg)this.attrDlg).setBoxText(true, IText.DisplayType.BOX);
/*     */           }
/*  90 */           else if ((this.prevElem.getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE)) || 
/*  91 */             (this.prevElem.getPgenCategory().equalsIgnoreCase("Front"))) {
/*  92 */             ((TextAttrDlg)this.attrDlg).setBoxText(false, IText.DisplayType.NORMAL);
/*  93 */             ((TextAttrDlg)this.attrDlg).setFontSize(18.0F);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*  98 */         if ((param = this.event.getParameter("usePrevColor")) != null)
/*     */         {
/* 100 */           if (param.equalsIgnoreCase("true")) {
/* 101 */             this.usePrevColor = true;
/*     */           }
/* 103 */           if (this.usePrevColor) {
/* 104 */             ((TextAttrDlg)this.attrDlg).setColor(this.prevElem.getPrimaryDE().getColors()[0]);
/*     */           }
/*     */         }
/*     */ 
/* 108 */         if (((param = this.event.getParameter("defaultTxt")) != null) && (!param.equalsIgnoreCase("Other"))) {
/* 109 */           txt = param;
/* 110 */           if (!txt.isEmpty()) {
/* 111 */             String[] txtArray = { "", "" };
/* 112 */             if (txt.contains("\n")) {
/* 113 */               txtArray[0] = txt.substring(0, txt.indexOf(10));
/* 114 */               txtArray[1] = txt.substring(txt.indexOf(10) + 1, txt.length());
/*     */             }
/*     */             else {
/* 117 */               txtArray[0] = txt;
/*     */             }
/* 119 */             ((TextAttrDlg)this.attrDlg).setText(txtArray);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 130 */         if (this.prevElem.getName().equalsIgnoreCase("labeledFront")) {
/* 131 */           String flbl = getDefaultFrontLabel(this.prevElem);
/* 132 */           ((TextAttrDlg)this.attrDlg).setText(new String[] { flbl });
/*     */         }
/* 134 */         else if (this.prevElem.getName().equalsIgnoreCase("Volcano")) {
/* 135 */           ((TextAttrDlg)this.attrDlg).setFontSize(18.0F);
/* 136 */           ((TextAttrDlg)this.attrDlg).setBoxText(true, IText.DisplayType.BOX);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 152 */     if (this.mouseHandler == null)
/*     */     {
/* 154 */       this.mouseHandler = new PgenTextDrawingHandler();
/*     */     }
/*     */ 
/* 158 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public static int rightOfLine(AbstractEditor mEditor, Coordinate pt, Line ln)
/*     */   {
/* 369 */     double[] screenPt = mEditor.translateInverseClick(pt);
/*     */ 
/* 371 */     Coordinate[] lnPts = ln.getLinePoints();
/*     */ 
/* 373 */     double minDist = 0.0D;
/* 374 */     double[] startPt = new double[2];
/* 375 */     double[] endPt = new double[2];
/*     */ 
/* 377 */     for (int ii = 0; ii < lnPts.length - 1; ii++) {
/* 378 */       double[] pt0 = mEditor.translateInverseClick(lnPts[ii]);
/* 379 */       double[] pt1 = mEditor.translateInverseClick(lnPts[(ii + 1)]);
/*     */ 
/* 381 */       double min = Line2D.ptSegDist(pt0[0], pt0[1], pt1[0], pt1[1], screenPt[0], screenPt[1]);
/*     */ 
/* 383 */       if ((ii == 0) || (min < minDist)) {
/* 384 */         minDist = min;
/* 385 */         startPt[0] = pt0[0];
/* 386 */         startPt[1] = pt0[1];
/* 387 */         endPt[0] = pt1[0];
/* 388 */         endPt[1] = pt1[1];
/*     */       }
/*     */     }
/*     */ 
/* 392 */     if (minDist == 0.0D) return 0;
/*     */ 
/* 394 */     return Line2D.relativeCCW(screenPt[0], screenPt[1], startPt[0], startPt[1], endPt[0], endPt[1]);
/*     */   }
/*     */ 
/*     */   private String getDefaultFrontLabel(AbstractDrawableComponent elem)
/*     */   {
/* 404 */     String frontLabel = "";
/* 405 */     String ptype = elem.getPgenType();
/* 406 */     if (ptype.equalsIgnoreCase("TROF")) {
/* 407 */       frontLabel = new String("OUTFLOW BOUNDARY");
/*     */     }
/* 409 */     else if (ptype.equals("TROPICAL_TROF")) {
/* 410 */       frontLabel = new String("TROPICAL WAVE");
/*     */     }
/* 412 */     else if (ptype.equals("DRY_LINE")) {
/* 413 */       frontLabel = new String("DRYLINE");
/*     */     }
/* 415 */     else if (ptype.equals("INSTABILITY")) {
/* 416 */       frontLabel = new String("SQUALL LINE");
/*     */     }
/* 418 */     else if (ptype.equals("SHEAR_LINE")) {
/* 419 */       frontLabel = new String("SHEARLINE");
/*     */     }
/*     */ 
/* 422 */     return frontLabel;
/*     */   }
/*     */ 
/*     */   public class PgenTextDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/* 173 */     private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/* 178 */     private AbstractDrawableComponent elem = null;
/*     */ 
/*     */     public PgenTextDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 188 */       if (!PgenTextDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 191 */       Coordinate loc = PgenTextDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 192 */       if ((loc == null) || (this.shiftDown)) return false;
/* 193 */       if (button == 1)
/*     */       {
/* 197 */         if (((IText)PgenTextDrawingTool.this.attrDlg).getString().length > 0) {
/* 198 */           this.elem = this.def.create(DrawableType.TEXT, PgenTextDrawingTool.this.attrDlg, 
/* 199 */             PgenTextDrawingTool.this.pgenCategory, PgenTextDrawingTool.this.pgenType, loc, PgenTextDrawingTool.this.drawingLayer.getActiveLayer());
/*     */         }
/*     */ 
/* 203 */         if (this.elem != null)
/*     */         {
/* 205 */           DECollection dec = PgenSinglePointDrawingTool.getCollection();
/* 206 */           if ((PgenTextDrawingTool.this.addLabelToSymbol) && (PgenTextDrawingTool.this.prevElem != null) && ((PgenTextDrawingTool.this.prevElem.getName().equalsIgnoreCase("labeledSymbol")) || 
/* 207 */             (PgenTextDrawingTool.this.prevElem.getName().equalsIgnoreCase("Volcano")))) {
/* 208 */             ((DECollection)PgenTextDrawingTool.this.prevElem).add(this.elem);
/*     */           }
/* 210 */           else if ((PgenTextDrawingTool.this.prevElem != null) && (PgenTextDrawingTool.this.prevElem.getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE))) {
/* 211 */             ((DECollection)PgenTextDrawingTool.this.prevElem).add(this.elem);
/*     */           }
/* 213 */           else if (((PgenTextDrawingTool.this.prevElem instanceof DECollection)) && (PgenTextDrawingTool.this.prevElem.getPgenCategory().equalsIgnoreCase("Front"))) {
/* 214 */             ((DECollection)PgenTextDrawingTool.this.prevElem).add(this.elem);
/*     */           }
/*     */           else {
/* 217 */             PgenTextDrawingTool.this.drawingLayer.addElement(this.elem);
/*     */           }
/* 219 */           AttrSettings.getInstance().setSettings((DrawableElement)this.elem);
/* 220 */           PgenTextDrawingTool.this.mapEditor.refresh();
/*     */ 
/* 222 */           PgenTextDrawingTool.this.attrDlg.getShell().setActive();
/*     */         }
/*     */ 
/* 225 */         if (PgenTextDrawingTool.this.addLabelToSymbol) {
/* 226 */           PgenTextDrawingTool.this.addLabelToSymbol = false;
/* 227 */           PgenTextDrawingTool.this.usePrevColor = false;
/* 228 */           if ((PgenTextDrawingTool.this.prevElem.getName().equalsIgnoreCase("labeledSymbol")) || (PgenTextDrawingTool.this.prevElem.getName().equalsIgnoreCase("Volcano"))) {
/* 229 */             if ((PgenTextDrawingTool.this.prevElem.getPrimaryDE() instanceof Symbol)) {
/* 230 */               PgenUtil.setDrawingSymbolMode(PgenTextDrawingTool.this.prevElem.getPrimaryDE().getPgenCategory(), PgenTextDrawingTool.this.prevElem.getPgenType(), false, null);
/*     */             }
/* 232 */             else if ((PgenTextDrawingTool.this.prevElem.getPrimaryDE() instanceof ComboSymbol)) {
/* 233 */               PgenUtil.setDrawingSymbolMode("Combo", PgenTextDrawingTool.this.prevElem.getPgenType(), false, null);
/*     */             }
/*     */           }
/* 236 */           else if (((PgenTextDrawingTool.this.prevElem instanceof DECollection)) && (PgenTextDrawingTool.this.prevElem.getPgenCategory().equalsIgnoreCase("Front"))) {
/* 237 */             PgenUtil.setDrawingFrontMode((Line)PgenTextDrawingTool.this.prevElem.getPrimaryDE());
/*     */           }
/* 239 */           else if (PgenTextDrawingTool.this.prevElem.getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE)) {
/* 240 */             PgenUtil.loadOutlookDrawingTool();
/*     */           }
/*     */ 
/* 243 */           PgenTextDrawingTool.this.prevElem = null;
/*     */         }
/*     */ 
/* 246 */         return true;
/*     */       }
/*     */ 
/* 249 */       if (button == 3)
/*     */       {
/* 251 */         PgenTextDrawingTool.this.drawingLayer.removeGhostLine();
/* 252 */         PgenTextDrawingTool.this.mapEditor.refresh();
/*     */ 
/* 254 */         if (PgenTextDrawingTool.this.addLabelToSymbol) {
/* 255 */           PgenTextDrawingTool.this.addLabelToSymbol = false;
/* 256 */           PgenTextDrawingTool.this.usePrevColor = false;
/* 257 */           if (PgenTextDrawingTool.this.prevElem.getName().equalsIgnoreCase("labeledSymbol")) {
/* 258 */             if ((PgenTextDrawingTool.this.prevElem.getPrimaryDE() instanceof Symbol)) {
/* 259 */               PgenUtil.setDrawingSymbolMode(PgenTextDrawingTool.this.prevElem.getPrimaryDE().getPgenCategory(), PgenTextDrawingTool.this.prevElem.getPgenType(), false, null);
/*     */             }
/* 261 */             else if ((PgenTextDrawingTool.this.prevElem.getPrimaryDE() instanceof ComboSymbol)) {
/* 262 */               PgenUtil.setDrawingSymbolMode("Combo", PgenTextDrawingTool.this.prevElem.getPgenType(), false, null);
/*     */             }
/*     */           }
/* 265 */           else if (((PgenTextDrawingTool.this.prevElem instanceof DECollection)) && (PgenTextDrawingTool.this.prevElem.getPgenCategory().equalsIgnoreCase("Front"))) {
/* 266 */             PgenUtil.setDrawingFrontMode((Line)PgenTextDrawingTool.this.prevElem.getPrimaryDE());
/*     */           }
/* 268 */           else if (PgenTextDrawingTool.this.prevElem.getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE)) {
/* 269 */             PgenUtil.loadOutlookDrawingTool();
/*     */           }
/* 271 */           PgenTextDrawingTool.this.prevElem = null;
/*     */         }
/*     */         else {
/* 274 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */ 
/* 277 */         return true;
/*     */       }
/*     */ 
/* 282 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 296 */       if (!PgenTextDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 299 */       Coordinate loc = PgenTextDrawingTool.this.mapEditor.translateClick(x, y);
/* 300 */       if (loc == null) return false;
/*     */ 
/* 302 */       if (PgenTextDrawingTool.this.attrDlg != null)
/*     */       {
/* 304 */         AbstractDrawableComponent ghost = null;
/*     */ 
/* 306 */         if (((IText)PgenTextDrawingTool.this.attrDlg).getString().length > 0)
/*     */         {
/* 308 */           if ((PgenTextDrawingTool.this.addLabelToSymbol) && (PgenTextDrawingTool.this.prevElem.getPgenCategory() != null) && 
/* 309 */             (PgenTextDrawingTool.this.prevElem.getPgenCategory().equalsIgnoreCase("Front")))
/*     */           {
/* 311 */             String[] text = ((IText)PgenTextDrawingTool.this.attrDlg).getString();
/* 312 */             if (text.length == 1) {
/* 313 */               StringBuffer lbl = new StringBuffer(((TextAttrDlg)PgenTextDrawingTool.this.attrDlg).getString()[0]);
/*     */ 
/* 315 */               if (lbl.length() > 0) {
/* 316 */                 if (lbl.charAt(0) == '[') lbl.deleteCharAt(0);
/* 317 */                 if (lbl.charAt(lbl.length() - 1) == ']') lbl.deleteCharAt(lbl.length() - 1);
/*     */                 try
/*     */                 {
/* 320 */                   Integer.parseInt(lbl.toString());
/*     */ 
/* 322 */                   if (PgenTextDrawingTool.rightOfLine(PgenTextDrawingTool.this.mapEditor, loc, (Line)PgenTextDrawingTool.this.prevElem.getPrimaryDE()) >= 0)
/*     */                   {
/* 324 */                     ((TextAttrDlg)PgenTextDrawingTool.this.attrDlg).setText(new String[] { lbl + "]" });
/*     */                   }
/*     */                   else
/* 327 */                     ((TextAttrDlg)PgenTextDrawingTool.this.attrDlg).setText(new String[] { "[" + lbl });
/*     */                 }
/*     */                 catch (NumberFormatException localNumberFormatException)
/*     */                 {
/*     */                 }
/*     */               }
/*     */             }
/* 334 */             ghost = this.def.create(DrawableType.TEXT, PgenTextDrawingTool.this.attrDlg, 
/* 335 */               PgenTextDrawingTool.this.pgenCategory, PgenTextDrawingTool.this.pgenType, loc, PgenTextDrawingTool.this.drawingLayer.getActiveLayer());
/*     */           }
/*     */           else
/*     */           {
/* 339 */             ghost = this.def.create(DrawableType.TEXT, (IText)PgenTextDrawingTool.this.attrDlg, 
/* 340 */               PgenTextDrawingTool.this.pgenCategory, PgenTextDrawingTool.this.pgenType, loc, PgenTextDrawingTool.this.drawingLayer.getActiveLayer());
/*     */           }
/*     */         }
/*     */ 
/* 344 */         PgenTextDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 345 */         PgenTextDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 349 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 355 */       if ((!PgenTextDrawingTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 356 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenTextDrawingTool
 * JD-Core Version:    0.6.2
 */