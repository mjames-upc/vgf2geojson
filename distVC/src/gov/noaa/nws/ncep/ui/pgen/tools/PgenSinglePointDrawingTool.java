/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.LabeledSymbolAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.SymbolAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.VolcanoAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElementFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ 
/*     */ public class PgenSinglePointDrawingTool extends AbstractPgenDrawingTool
/*     */ {
/*     */   private static DECollection dec;
/*     */   private AbstractDrawableComponent prevElem;
/*     */   boolean usePrevColor;
/*     */ 
/*     */   public PgenSinglePointDrawingTool()
/*     */   {
/*  60 */     dec = null;
/*     */   }
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/*  72 */     super.activateTool();
/*     */ 
/*  74 */     if ((this.attrDlg != null) && (!isDelObj()))
/*     */     {
/*  77 */       if ((this.event.getTrigger() instanceof AbstractDrawableComponent))
/*  78 */         this.prevElem = ((AbstractDrawableComponent)this.event.getTrigger());
/*     */       String param;
/*  80 */       if ((param = this.event.getParameter("usePrevColor")) != null)
/*     */       {
/*  82 */         if (param.equalsIgnoreCase("true")) {
/*  83 */           this.usePrevColor = true;
/*     */         }
/*  85 */         if (this.usePrevColor)
/*  86 */           ((SymbolAttrDlg)this.attrDlg).setColor(this.prevElem.getPrimaryDE().getColors()[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 100 */     if (this.mouseHandler == null)
/*     */     {
/* 102 */       this.mouseHandler = new PgenSinglePointDrawingHandler();
/*     */     }
/*     */ 
/* 106 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   private DrawableType getDrawableType()
/*     */   {
/* 284 */     DrawableType which = DrawableType.SYMBOL;
/* 285 */     if (this.pgenCategory.equals("Combo")) which = DrawableType.COMBO_SYMBOL;
/* 286 */     return which;
/*     */   }
/*     */ 
/*     */   public static DECollection getCollection()
/*     */   {
/* 303 */     return dec;
/*     */   }
/*     */ 
/*     */   public class PgenSinglePointDrawingHandler extends InputHandlerDefaultImpl
/*     */   {
/* 121 */     private DrawableElementFactory def = new DrawableElementFactory();
/*     */ 
/* 126 */     private DrawableElement elem = null;
/*     */ 
/*     */     public PgenSinglePointDrawingHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 136 */       if (!PgenSinglePointDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 139 */       Coordinate loc = PgenSinglePointDrawingTool.this.mapEditor.translateClick(anX, aY);
/* 140 */       if ((loc == null) || (this.shiftDown)) return false;
/*     */ 
/* 142 */       if (button == 1)
/*     */       {
/* 145 */         this.elem = ((DrawableElement)this.def.create(PgenSinglePointDrawingTool.this.getDrawableType(), PgenSinglePointDrawingTool.this.attrDlg, 
/* 146 */           PgenSinglePointDrawingTool.this.pgenCategory, PgenSinglePointDrawingTool.this.pgenType, loc, 
/* 147 */           PgenSinglePointDrawingTool.this.drawingLayer.getActiveLayer()));
/*     */ 
/* 149 */         ((SymbolAttrDlg)PgenSinglePointDrawingTool.this.attrDlg).setLatitude(loc.y);
/* 150 */         ((SymbolAttrDlg)PgenSinglePointDrawingTool.this.attrDlg).setLongitude(loc.x);
/* 151 */         ((SymbolAttrDlg)PgenSinglePointDrawingTool.this.attrDlg).enableUndoBtn(true);
/*     */ 
/* 154 */         if (this.elem != null) {
/* 155 */           if ((PgenSinglePointDrawingTool.this.prevElem != null) && (PgenSinglePointDrawingTool.this.prevElem.getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE))) {
/* 156 */             ((DECollection)PgenSinglePointDrawingTool.this.prevElem).add(this.elem);
/*     */           }
/* 158 */           else if (((SymbolAttrDlg)PgenSinglePointDrawingTool.this.attrDlg).labelEnabled()) {
/* 159 */             PgenSinglePointDrawingTool.dec = new DECollection("labeledSymbol");
/* 160 */             PgenSinglePointDrawingTool.dec.setPgenCategory(PgenSinglePointDrawingTool.this.pgenCategory);
/* 161 */             PgenSinglePointDrawingTool.dec.setPgenType(PgenSinglePointDrawingTool.this.pgenType);
/* 162 */             PgenSinglePointDrawingTool.dec.addElement(this.elem);
/* 163 */             PgenSinglePointDrawingTool.this.drawingLayer.addElement(PgenSinglePointDrawingTool.dec);
/*     */           }
/*     */           else {
/* 166 */             PgenSinglePointDrawingTool.this.drawingLayer.addElement(this.elem);
/*     */           }
/*     */ 
/* 169 */           PgenSinglePointDrawingTool.this.attrDlg.setDrawableElement(this.elem);
/* 170 */           AttrSettings.getInstance().setSettings(this.elem);
/* 171 */           PgenSinglePointDrawingTool.this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 174 */         if (PgenSinglePointDrawingTool.this.prevElem != null) {
/* 175 */           PgenSinglePointDrawingTool.this.usePrevColor = false;
/* 176 */           if (PgenSinglePointDrawingTool.this.prevElem.getName().equalsIgnoreCase(Outlook.OUTLOOK_LABELED_LINE)) {
/* 177 */             PgenUtil.loadOutlookDrawingTool();
/*     */           }
/* 179 */           PgenSinglePointDrawingTool.this.prevElem = null;
/*     */         }
/*     */ 
/* 182 */         return true;
/*     */       }
/*     */ 
/* 185 */       if (button == 3)
/*     */       {
/* 187 */         if ((this.elem != null) && (((SymbolAttrDlg)PgenSinglePointDrawingTool.this.attrDlg).labelEnabled())) {
/* 188 */           PgenSinglePointDrawingTool.this.drawingLayer.removeGhostLine();
/* 189 */           PgenSinglePointDrawingTool.this.mapEditor.refresh();
/*     */ 
/* 191 */           String defaultTxt = "";
/* 192 */           if ((PgenSinglePointDrawingTool.this.attrDlg instanceof VolcanoAttrDlg)) {
/* 193 */             defaultTxt = ((VolcanoAttrDlg)PgenSinglePointDrawingTool.this.attrDlg).getVolText();
/* 194 */             PgenSinglePointDrawingTool.dec.setCollectionName("Volcano");
/*     */           }
/*     */ 
/* 198 */           if ((PgenSinglePointDrawingTool.dec == null) && (((SymbolAttrDlg)PgenSinglePointDrawingTool.this.attrDlg).labelEnabled())) {
/* 199 */             PgenSinglePointDrawingTool.dec = new DECollection("labeledSymbol");
/* 200 */             PgenSinglePointDrawingTool.dec.setPgenCategory(PgenSinglePointDrawingTool.this.pgenCategory);
/* 201 */             PgenSinglePointDrawingTool.dec.setPgenType(PgenSinglePointDrawingTool.this.pgenType);
/* 202 */             PgenSinglePointDrawingTool.dec.addElement(this.elem);
/* 203 */             PgenSinglePointDrawingTool.this.drawingLayer.replaceElement(this.elem, PgenSinglePointDrawingTool.dec);
/*     */           }
/*     */ 
/* 206 */           PgenUtil.setDrawingTextMode(true, ((LabeledSymbolAttrDlg)PgenSinglePointDrawingTool.this.attrDlg).useSymbolColor(), defaultTxt, PgenSinglePointDrawingTool.dec);
/* 207 */           PgenSinglePointDrawingTool.dec = null;
/* 208 */           this.elem = null;
/*     */         }
/* 212 */         else if (PgenSinglePointDrawingTool.this.prevElem != null) {
/* 213 */           PgenSinglePointDrawingTool.this.usePrevColor = false;
/* 214 */           if (PgenSinglePointDrawingTool.this.prevElem.getParent().getPgenCategory().equalsIgnoreCase("OUTLOOK")) {
/* 215 */             PgenUtil.loadOutlookDrawingTool();
/*     */           }
/* 217 */           PgenSinglePointDrawingTool.this.prevElem = null;
/*     */         }
/*     */         else {
/* 220 */           this.elem = null;
/* 221 */           PgenUtil.setSelectingMode();
/*     */         }
/*     */ 
/* 225 */         return true;
/*     */       }
/*     */ 
/* 230 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseMove(int x, int y)
/*     */     {
/* 244 */       if (!PgenSinglePointDrawingTool.this.isResourceEditable()) return false;
/*     */ 
/* 247 */       Coordinate loc = PgenSinglePointDrawingTool.this.mapEditor.translateClick(x, y);
/* 248 */       if (loc == null) return false;
/*     */ 
/* 250 */       if (PgenSinglePointDrawingTool.this.attrDlg != null)
/*     */       {
/* 252 */         AbstractDrawableComponent ghost = null;
/*     */ 
/* 258 */         ghost = this.def.create(PgenSinglePointDrawingTool.this.getDrawableType(), PgenSinglePointDrawingTool.this.attrDlg, 
/* 259 */           PgenSinglePointDrawingTool.this.pgenCategory, PgenSinglePointDrawingTool.this.pgenType, loc, 
/* 260 */           PgenSinglePointDrawingTool.this.drawingLayer.getActiveLayer());
/* 261 */         PgenSinglePointDrawingTool.this.drawingLayer.setGhostLine(ghost);
/* 262 */         PgenSinglePointDrawingTool.this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 266 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int aX, int aY, int button)
/*     */     {
/* 273 */       if ((!PgenSinglePointDrawingTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 274 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenSinglePointDrawingTool
 * JD-Core Version:    0.6.2
 */