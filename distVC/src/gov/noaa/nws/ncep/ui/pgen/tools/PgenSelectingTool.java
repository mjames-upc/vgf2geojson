/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.IDisplayPaneContainer;
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.EditorUtil;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlgFactory;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.ContoursAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.GfaAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.JetAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.contours.Contours;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetBarb;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Line;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Vector;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.Iterator;
/*     */ import org.eclipse.core.commands.ExecutionEvent;
/*     */ import org.eclipse.ui.IEditorPart;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenSelectingTool extends AbstractPgenDrawingTool
/*     */   implements IJetBarb
/*     */ {
/*     */   private IInputHandler selectHandler;
/*     */   private Jet jet;
/*     */   private Contours selectedContours;
/*     */   private boolean selectInContours;
/* 100 */   private DrawableElement prevDe = null;
/*     */ 
/*     */   protected void activateTool()
/*     */   {
/* 116 */     this.prevDe = null;
/*     */ 
/* 118 */     if (PgenSession.getInstance().getPgenPalette() == null) return;
/*     */ 
/* 120 */     IEditorPart ep = EditorUtil.getActiveEditor();
/* 121 */     if (!(ep instanceof AbstractEditor)) {
/* 122 */       return;
/*     */     }
/*     */ 
/* 125 */     if ((this.attrDlg != null) && (!(this.attrDlg instanceof ContoursAttrDlg))) this.attrDlg.close();
/* 126 */     this.attrDlg = null;
/* 127 */     if (this.buttonName == null) this.buttonName = new String("Select");
/* 128 */     PgenSession.getInstance().getPgenPalette().setDefaultAction();
/*     */ 
/* 130 */     super.activateTool();
/*     */ 
/* 135 */     Object de = this.event.getTrigger();
/* 136 */     if ((de instanceof Contours)) {
/* 137 */       this.selectInContours = true;
/* 138 */       this.selectedContours = ((Contours)de);
/*     */     }
/* 140 */     else if ((de instanceof Gfa)) {
/* 141 */       this.attrDlg = AttrDlgFactory.createAttrDlg(((Gfa)de).getPgenCategory(), ((Gfa)de).getPgenType(), 
/* 142 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/* 143 */       this.attrDlg.setBlockOnOpen(false);
/* 144 */       if (this.attrDlg.getShell() == null) this.attrDlg.open();
/* 145 */       ((GfaAttrDlg)this.attrDlg).enableMoveTextBtn(true);
/* 146 */       this.drawingLayer.setSelected((Gfa)de);
/* 147 */       this.editor.refresh();
/*     */     }
/*     */     else {
/* 150 */       this.selectInContours = false;
/* 151 */       resetMouseHandler();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deactivateTool()
/*     */   {
/* 164 */     this.prevDe = this.drawingLayer.getSelectedDE();
/*     */ 
/* 166 */     super.deactivateTool();
/* 167 */     if ((this.mouseHandler != null) && ((this.mouseHandler instanceof PgenSelectHandler)))
/* 168 */       ((PgenSelectHandler)this.mouseHandler).closeDlg();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 178 */     if ((this.mouseHandler == null) || (this.mapEditor != ((PgenSelectHandler)this.mouseHandler).getMapEditor()) || 
/* 179 */       (this.drawingLayer != ((PgenSelectHandler)this.mouseHandler).getPgenrsc()))
/*     */     {
/* 181 */       this.mouseHandler = new PgenSelectHandler(this, this.mapEditor, this.drawingLayer, this.attrDlg);
/* 182 */       this.selectHandler = this.mouseHandler;
/*     */     }
/*     */ 
/* 185 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public void resetMouseHandler()
/*     */   {
/* 192 */     setHandler(this.selectHandler);
/*     */   }
/*     */ 
/*     */   public void setAddingBarbHandler()
/*     */   {
/* 200 */     setHandler(new PgenJetBarbAddingHandler(this.mapEditor, this.drawingLayer, 
/* 201 */       this, (JetAttrDlg)this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void setDeletingBarbHandler()
/*     */   {
/* 210 */     setHandler(new PgenJetBarbDeletingHandler(this.mapEditor, this.drawingLayer, 
/* 211 */       this, (JetAttrDlg)this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void setAddingHashHandler()
/*     */   {
/* 220 */     setHandler(new PgenJetHashAddingHandler(this.mapEditor, this.drawingLayer, 
/* 221 */       this, (JetAttrDlg)this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void setDeletingHashHandler()
/*     */   {
/* 230 */     setHandler(new PgenJetHashDeletingHandler(this.mapEditor, this.drawingLayer, 
/* 231 */       this, (JetAttrDlg)this.attrDlg));
/*     */   }
/*     */ 
/*     */   public void applyBarbAttrOnJet(IAttribute attr, String type)
/*     */   {
/* 240 */     if (this.jet != null)
/*     */     {
/* 242 */       Jet newJet = this.jet.copy();
/*     */ 
/* 244 */       Iterator it = newJet.createDEIterator();
/*     */ 
/* 246 */       while (it.hasNext()) {
/* 247 */         DrawableElement de = (DrawableElement)it.next();
/* 248 */         if (de.getPgenType().equalsIgnoreCase(type)) {
/* 249 */           double sp = ((Vector)de).getSpeed();
/* 250 */           double dir = ((Vector)de).getDirection();
/* 251 */           de.update(attr);
/* 252 */           if (type.equalsIgnoreCase("Barb")) {
/* 253 */             ((Jet.JetBarb)de).setSpeedOnly(sp);
/*     */           }
/* 255 */           ((Vector)de).setDirection(dir);
/*     */         }
/*     */       }
/*     */ 
/* 259 */       this.drawingLayer.replaceElement(this.jet, newJet);
/* 260 */       this.drawingLayer.setSelected(newJet);
/* 261 */       this.jet = newJet;
/*     */ 
/* 263 */       this.mapEditor.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void applyFLAttrOnJet(IAttribute attr)
/*     */   {
/* 273 */     if (this.jet != null) {
/* 274 */       Jet newJet = this.jet.copy();
/*     */ 
/* 276 */       Iterator it = newJet.createDEIterator();
/*     */ 
/* 278 */       while (it.hasNext()) {
/* 279 */         DrawableElement de = (DrawableElement)it.next();
/* 280 */         if (de.getPgenType().equalsIgnoreCase("General Text")) {
/* 281 */           String[] txt = ((Text)de).getText();
/* 282 */           double rot = ((Text)de).getRotation();
/* 283 */           de.update(attr);
/* 284 */           ((Text)de).setText(txt);
/* 285 */           ((Text)de).setRotation(rot);
/*     */         }
/*     */       }
/* 288 */       this.drawingLayer.replaceElement(this.jet, newJet);
/* 289 */       this.drawingLayer.setSelected(newJet);
/* 290 */       this.jet = newJet;
/* 291 */       this.mapEditor.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setJet(Jet jet)
/*     */   {
/* 300 */     this.jet = jet;
/* 301 */     this.drawingLayer.setSelected(jet);
/*     */   }
/*     */ 
/*     */   public Jet getJet()
/*     */   {
/* 308 */     return this.jet;
/*     */   }
/*     */ 
/*     */   public void changeSelectedLineType(String type)
/*     */   {
/* 315 */     if ((this.prevDe != null) && ((this.prevDe.getPgenCategory().equalsIgnoreCase("Lines")) || 
/* 316 */       (this.prevDe.getPgenCategory().equalsIgnoreCase("Front"))))
/*     */     {
/* 318 */       AttrDlg dlg = AttrDlgFactory.createAttrDlg(this.prevDe.getPgenCategory(), type, 
/* 319 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
/*     */ 
/* 321 */       DrawableElement de = this.prevDe;
/*     */ 
/* 323 */       activateTool();
/*     */ 
/* 325 */       this.attrDlg = dlg;
/*     */ 
/* 327 */       this.attrDlg.open();
/* 328 */       this.attrDlg.setPgenType(type);
/* 329 */       this.attrDlg.setDefaultAttr();
/* 330 */       this.attrDlg.enableButtons();
/*     */ 
/* 334 */       Line ln = (Line)((Line)de).copy();
/* 335 */       ln.setPgenType(type);
/*     */ 
/* 338 */       ln.update(this.attrDlg);
/*     */ 
/* 340 */       this.drawingLayer.replaceElement(de, ln);
/* 341 */       this.drawingLayer.setSelected(ln);
/* 342 */       this.mapEditor.refresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   public DrawableElement getPreviousSelectedDE() {
/* 347 */     return this.prevDe;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenSelectingTool
 * JD-Core Version:    0.6.2
 */