/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.CloudAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.TurbAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.CcfpAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.labeledlines.LabeledLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class PgenLabeledLineDrawingTool extends AbstractPgenDrawingTool
/*     */   implements ILabeledLine
/*     */ {
/*     */   LabeledLine labeledLine;
/*  51 */   CcfpAttrDlg ccdlg = null;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  60 */     super.activateTool();
/*  61 */     if (super.isDelObj()) return;
/*     */ 
/*  63 */     if ((this.attrDlg instanceof CloudAttrDlg)) {
/*  64 */       ((CloudAttrDlg)this.attrDlg).setCloudDrawingTool(this);
/*     */     }
/*  66 */     else if ((this.attrDlg instanceof TurbAttrDlg)) {
/*  67 */       ((TurbAttrDlg)this.attrDlg).setTurbDrawingTool(this);
/*     */     }
/*  69 */     else if ((this.attrDlg instanceof CcfpAttrDlg)) {
/*  70 */       this.ccdlg = ((CcfpAttrDlg)this.attrDlg);
/*  71 */       ((CcfpAttrDlg)this.attrDlg).setCcfpDrawingTool(this);
/*     */     }
/*  73 */     resetMouseHandler();
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/*  94 */     super.deactivateTool();
/*     */ 
/*  96 */     this.labeledLine = null;
/*  97 */     if ((this.mouseHandler instanceof PgenLabeledLineDrawingHandler)) {
/*  98 */       PgenLabeledLineDrawingHandler cdh = (PgenLabeledLineDrawingHandler)this.mouseHandler;
/*  99 */       if (cdh != null) cdh.clearPoints();
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 110 */     if (this.mouseHandler == null)
/*     */     {
/* 112 */       this.mouseHandler = new PgenLabeledLineDrawingHandler(null);
/*     */     }
/*     */ 
/* 116 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public void setLabeledLine(LabeledLine labeledLine)
/*     */   {
/* 343 */     this.labeledLine = labeledLine;
/*     */   }
/*     */ 
/*     */   public LabeledLine getLabeledLine() {
/* 347 */     return this.labeledLine;
/*     */   }
/*     */ 
/*     */   public void resetMouseHandler() {
/* 351 */     setHandler(new PgenLabeledLineDrawingHandler(null));
/*     */   }
/*     */ 
/*     */   public void setAddingLabelHandler() {
/* 355 */     setHandler(new PgenAddLabelHandler(this.mapEditor, this.drawingLayer, this, this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void setDeleteHandler(boolean delLine, boolean flipFlag, boolean openClose)
/*     */   {
/* 360 */     setHandler(new PgenLabeledLineDelHandler(this.mapEditor, this.drawingLayer, this, this.attrDlg, delLine, flipFlag, openClose));
/*     */   }
/*     */ 
/*     */   private class PgenLabeledLineDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/* 130 */     boolean ccfpTxtFlag = false;
/*     */ 
/* 135 */     protected ArrayList<Coordinate> points = new ArrayList();
/*     */     protected AbstractDrawableComponent elem;
/* 146 */     protected DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/*     */     private PgenLabeledLineDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 156 */       if (!PgenLabeledLineDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 159 */       Coordinate loc = PgenLabeledLineDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 160 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 162 */       if (button == 1)
/*     */       {
/* 164 */         if (("CCFP_SIGMET".equalsIgnoreCase(PgenLabeledLineDrawingTool.this.pgenType)) || 
/* 165 */           (PgenLabeledLineDrawingTool.this.attrDlg.isAddLineMode())) {
/* 166 */           this.points.add(loc);
/*     */         }
/*     */ 
/* 169 */         return true;
/*     */       }
/*     */ 
/* 172 */       if (button == 3)
/*     */       {
/* 174 */         if (this.points.size() == 0)
/*     */         {
/* 176 */           if (PgenLabeledLineDrawingTool.this.attrDlg.isAddLineMode())
/*     */           {
/* 178 */             PgenLabeledLineDrawingTool.this.attrDlg.resetLabeledLineBtns();
/*     */           }
/*     */           else
/*     */           {
/* 182 */             closeAttrDlg(PgenLabeledLineDrawingTool.this.attrDlg, PgenLabeledLineDrawingTool.this.pgenType);
/* 183 */             PgenLabeledLineDrawingTool.this.attrDlg = null;
/* 184 */             PgenUtil.setSelectingMode();
/*     */           }
/*     */ 
/* 187 */           PgenLabeledLineDrawingTool.this.labeledLine = null;
/*     */         }
/* 190 */         else if (this.points.size() < 2)
/*     */         {
/* 192 */           PgenLabeledLineDrawingTool.this.drawingLayer.removeGhostLine();
/* 193 */           this.points.clear();
/*     */ 
/* 195 */           PgenLabeledLineDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */         else
/*     */         {
/* 201 */           if (PgenLabeledLineDrawingTool.this.attrDlg.isAddLineMode())
/*     */           {
/* 217 */             this.elem = this.def.createLabeledLine(PgenLabeledLineDrawingTool.this.pgenCategory, PgenLabeledLineDrawingTool.this.pgenType, PgenLabeledLineDrawingTool.this.attrDlg, 
/* 218 */               this.points, null, PgenLabeledLineDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 220 */             PgenLabeledLineDrawingTool.this.drawingLayer.addElement(this.elem);
/* 221 */             PgenLabeledLineDrawingTool.this.labeledLine = ((LabeledLine)this.elem);
/* 222 */             AttrSettings.getInstance().setSettings(this.elem);
/*     */           }
/*     */ 
/* 227 */           if ("CCFP_SIGMET".equals(PgenLabeledLineDrawingTool.this.pgenType)) {
/* 228 */             this.elem = this.def.createLabeledLine(PgenLabeledLineDrawingTool.this.pgenCategory, PgenLabeledLineDrawingTool.this.pgenType, PgenLabeledLineDrawingTool.this.attrDlg, 
/* 229 */               this.points, null, PgenLabeledLineDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 231 */             PgenLabeledLineDrawingTool.this.drawingLayer.addElement(this.elem);
/* 232 */             PgenLabeledLineDrawingTool.this.labeledLine = ((LabeledLine)this.elem);
/*     */ 
/* 235 */             if (PgenLabeledLineDrawingTool.this.ccdlg.isAreaType())
/*     */             {
/* 237 */               this.ccfpTxtFlag = true;
/* 238 */               PgenLabeledLineDrawingTool.this.setAddingLabelHandler();
/*     */             }
/*     */           }
/*     */ 
/* 242 */           PgenLabeledLineDrawingTool.this.drawingLayer.removeGhostLine();
/* 243 */           if (!this.ccfpTxtFlag) this.points.clear();
/*     */ 
/* 245 */           PgenLabeledLineDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 249 */         return true;
/*     */       }
/*     */ 
/* 252 */       if (button == 2) {
/* 253 */         return true;
/*     */       }
/*     */ 
/* 256 */       return false;
/*     */     }
/*     */ 
/*     */     private void closeAttrDlg(AttrDlg attrDlgObject, String pgenTypeString)
/*     */     {
/* 261 */       if (attrDlgObject == null)
/* 262 */         return;
/* 263 */       attrDlgObject.close();
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 274 */       if (!PgenLabeledLineDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 277 */       Coordinate loc = PgenLabeledLineDrawingTool.this.mapEditor.translateClick(x, y);
/* 278 */       if (loc == null) return false;
/*     */ 
/* 280 */       String lineType = "LINE_DASHED_4";
/* 281 */       if (PgenLabeledLineDrawingTool.this.pgenType.equalsIgnoreCase("Cloud")) {
/* 282 */         lineType = "SCALLOPED";
/*     */       }
/* 284 */       else if (PgenLabeledLineDrawingTool.this.pgenType.equalsIgnoreCase("Turbulence"))
/* 285 */         lineType = "LINE_DASHED_4";
/* 286 */       else if ("CCFP_SIGMET".equalsIgnoreCase(PgenLabeledLineDrawingTool.this.pgenType)) {
/* 287 */         lineType = PgenLabeledLineDrawingTool.this.ccdlg.getCcfpLineType();
/*     */       }
/*     */ 
/* 291 */       AbstractDrawableComponent ghost = this.def.create(DrawableType.LINE, PgenLabeledLineDrawingTool.this.attrDlg, 
/* 292 */         "Lines", lineType, this.points, PgenLabeledLineDrawingTool.this.drawingLayer.getActiveLayer());
/*     */ 
/* 294 */       if ((this.points != null) && (this.points.size() >= 1))
/*     */       {
/* 296 */         ArrayList ghostPts = new ArrayList(this.points);
/* 297 */         ghostPts.add(loc);
/* 298 */         Line ln = (Line)ghost;
/* 299 */         ln.setLinePoints(new ArrayList(ghostPts));
/*     */ 
/* 301 */         if ("CCFP_SIGMET".equalsIgnoreCase(PgenLabeledLineDrawingTool.this.pgenType)) {
/* 302 */           ln.setClosed(Boolean.valueOf(PgenLabeledLineDrawingTool.this.ccdlg.isAreaType()));
/* 303 */           ln.setFilled(Boolean.valueOf(PgenLabeledLineDrawingTool.this.ccdlg.isAreaType()));
/*     */         }
/* 305 */         PgenLabeledLineDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 306 */         PgenLabeledLineDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 310 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 316 */       if ((!PgenLabeledLineDrawingTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 317 */       return true;
/*     */     }
/*     */ 
/*     */     private void clearPoints() {
/* 321 */       this.points.clear();
/*     */     }
/*     */ 
/*     */     private LabeledLine getLabeledLineInCurrentLayer(String type)
/*     */     {
/* 330 */       Iterator it = PgenLabeledLineDrawingTool.this.drawingLayer.getActiveLayer().getComponentIterator();
/* 331 */       while (it.hasNext()) {
/* 332 */         AbstractDrawableComponent adc = (AbstractDrawableComponent)it.next();
/* 333 */         if (((adc instanceof LabeledLine)) && (adc.getPgenType().equalsIgnoreCase(type))) {
/* 334 */           return (LabeledLine)adc;
/*     */         }
/*     */       }
/* 337 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenLabeledLineDrawingTool
 * JD-Core Version:    0.6.2
 */