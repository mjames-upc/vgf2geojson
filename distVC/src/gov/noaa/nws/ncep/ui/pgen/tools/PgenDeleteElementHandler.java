/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.ContourMinmax;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Outlook;
/*     */ import gov.noaa.nws.ncep.ui.pgen.filter.AcceptFilter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ 
/*     */ public class PgenDeleteElementHandler extends InputHandlerDefaultImpl
/*     */ {
/*     */   protected AbstractEditor mapEditor;
/*     */   protected PgenResource pgenrsc;
/*     */   protected AbstractPgenTool tool;
/*     */   protected AttrDlg attrDlg;
/*     */   private boolean preempt;
/*     */ 
/*     */   public PgenDeleteElementHandler(AbstractPgenTool tool)
/*     */   {
/*  52 */     this.tool = tool;
/*  53 */     this.pgenrsc = tool.getDrawingLayer();
/*  54 */     this.mapEditor = tool.mapEditor;
/*     */ 
/*  56 */     if ((tool instanceof AbstractPgenDrawingTool))
/*  57 */       this.attrDlg = ((AbstractPgenDrawingTool)tool).getAttrDlg();
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDown(int anX, int aY, int button)
/*     */   {
/*  69 */     if (!this.tool.isResourceEditable()) return false;
/*     */ 
/*  71 */     this.preempt = false;
/*     */ 
/*  74 */     Coordinate loc = this.mapEditor.translateClick(anX, aY);
/*  75 */     if (loc == null) return false;
/*     */ 
/*  77 */     if (button == 1)
/*     */     {
/*  79 */       if (this.pgenrsc.getSelectedComp() != null) {
/*  80 */         doDelete();
/*  81 */         this.preempt = false;
/*     */       }
/*     */       else
/*     */       {
/*  85 */         AbstractDrawableComponent elSelected = this.pgenrsc.getNearestComponent(loc, new AcceptFilter(), true);
/*     */ 
/*  88 */         if (((elSelected instanceof DECollection)) && (elSelected.getName().equalsIgnoreCase("Watch")) && 
/*  89 */           (this.pgenrsc.getNearestElement(loc).getPgenType().equalsIgnoreCase("POINTED_ARROW"))) {
/*  90 */           elSelected = this.pgenrsc.getNearestElement(loc);
/*     */         }
/*  92 */         else if (((elSelected instanceof Outlook)) && (((Outlook)elSelected).getDEs() > 1)) {
/*  93 */           AbstractDrawableComponent adc = this.pgenrsc.getNearestElement(loc);
/*  94 */           elSelected = adc.getParent();
/*     */         }
/*     */ 
/*  97 */         if (elSelected != null) {
/*  98 */           this.pgenrsc.setSelected(elSelected);
/*  99 */           this.preempt = true;
/*     */         }
/* 101 */         this.mapEditor.refresh();
/*     */       }
/*     */ 
/* 104 */       return this.preempt;
/*     */     }
/*     */ 
/* 107 */     if (button == 2)
/*     */     {
/* 109 */       return true;
/*     */     }
/*     */ 
/* 112 */     if (button == 3)
/*     */     {
/* 114 */       if (this.pgenrsc.getSelectedComp() != null)
/*     */       {
/* 116 */         this.pgenrsc.removeSelected();
/* 117 */         this.mapEditor.refresh();
/*     */       }
/*     */       else
/*     */       {
/* 121 */         PgenUtil.setSelectingMode();
/*     */       }
/*     */ 
/* 124 */       return true;
/*     */     }
/*     */ 
/* 129 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */   {
/* 137 */     if ((!this.tool.isResourceEditable()) || (this.shiftDown)) return false;
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   public void preprocess()
/*     */   {
/* 148 */     if (this.pgenrsc.getSelectedComp() != null) {
/* 149 */       if ((this.attrDlg != null) && (
/* 150 */         ((this.pgenrsc.getSelectedComp().getParent() instanceof Layer)) || 
/* 151 */         (this.pgenrsc.getSelectedComp().getParent().getName().equalsIgnoreCase("labeledSymbol")))) {
/* 152 */         this.attrDlg.close();
/*     */       }
/*     */ 
/* 155 */       doDelete();
/* 156 */       this.tool.resetMouseHandler();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void doDelete()
/*     */   {
/* 165 */     AbstractDrawableComponent adc = this.pgenrsc.getSelectedComp();
/*     */ 
/* 167 */     if (((adc.getParent() instanceof ContourMinmax)) || 
/* 168 */       (adc.getParent().getName().equalsIgnoreCase("labeledSymbol"))) {
/* 169 */       this.pgenrsc.removeElement(adc.getParent());
/*     */     }
/*     */     else {
/* 172 */       this.pgenrsc.removeElement(adc);
/*     */     }
/*     */ 
/* 175 */     this.pgenrsc.removeSelected();
/* 176 */     this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   public AbstractEditor getMapEditor() {
/* 180 */     return this.mapEditor;
/*     */   }
/*     */ 
/*     */   public PgenResource getPgenrsc() {
/* 184 */     return this.pgenrsc;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenDeleteElementHandler
 * JD-Core Version:    0.6.2
 */