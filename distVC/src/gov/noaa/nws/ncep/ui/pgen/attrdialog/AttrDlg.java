/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.CcfpAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VaaCloudDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.VolcanoVaaAttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DECollection;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetBarb;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetHash;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetLine;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Jet.JetText;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.AbstractSigmet;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.Sigmet;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public abstract class AttrDlg extends Dialog
/*     */   implements IAttribute
/*     */ {
/*  74 */   public static int ctrlBtnWidth = 70;
/*  75 */   public static int ctrlBtnHeight = 28;
/*     */ 
/*  83 */   protected PgenResource drawingLayer = null;
/*     */ 
/*  90 */   protected AbstractEditor mapEditor = null;
/*  91 */   protected String pgenCategory = null;
/*  92 */   protected String pgenType = null;
/*     */   protected static final int CHK_WIDTH = 16;
/*     */   protected static final int CHK_HEIGHT = 28;
/*  96 */   protected static String mouseHandlerName = null;
/*  97 */   protected static AbstractDrawableComponent de = null;
/*     */   protected Point shellLocation;
/*     */ 
/*     */   public AttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/* 108 */     super(parShell);
/* 109 */     setShellStyle(96);
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 115 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/* 116 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*     */ 
/* 118 */     super.createButtonsForButtonBar(parent);
/* 119 */     getButton(1).setEnabled(false);
/* 120 */     getButton(0).setEnabled(false);
/*     */ 
/* 122 */     getButton(1).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/* 123 */     getButton(0).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*     */   }
/*     */ 
/*     */   public Control createButtonBar(Composite parent)
/*     */   {
/* 130 */     Control bar = super.createButtonBar(parent);
/* 131 */     GridData gd = new GridData(16777216, -1, true, false);
/* 132 */     gd.heightHint = (ctrlBtnHeight + 5);
/*     */ 
/* 134 */     bar.setLayoutData(gd);
/* 135 */     return bar;
/*     */   }
/*     */ 
/*     */   public void handleShellCloseEvent()
/*     */   {
/* 145 */     this.drawingLayer.removeSelected();
/* 146 */     this.drawingLayer.removeGhostLine();
/* 147 */     super.handleShellCloseEvent();
/* 148 */     PgenUtil.setSelectingMode();
/*     */   }
/*     */ 
/*     */   public abstract void setAttrForDlg(IAttribute paramIAttribute);
/*     */ 
/*     */   public void setDrawingLayer(PgenResource dl)
/*     */   {
/* 159 */     this.drawingLayer = dl;
/*     */   }
/*     */ 
/*     */   public void setMapEditor(AbstractEditor me)
/*     */   {
/* 169 */     this.mapEditor = me;
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 181 */     DrawableElement de = this.drawingLayer.getSelectedDE();
/*     */     Jet newJet;
/* 182 */     if ((de != null) && (
/* 183 */       ((de instanceof Jet.JetBarb)) || 
/* 184 */       ((de instanceof Jet.JetHash)) || 
/* 185 */       ((de instanceof Jet.JetText)) || 
/* 186 */       ((de instanceof Jet.JetLine))))
/*     */     {
/* 188 */       DrawableElement newEl = (DrawableElement)de.copy();
/*     */ 
/* 191 */       if ((de instanceof Jet.JetBarb)) {
/* 192 */         DECollection wind = (DECollection)de.getParent();
/* 193 */         if ((wind != null) && (wind.getCollectionName().equalsIgnoreCase("WindInfo"))) {
/* 194 */           DECollection parent = (DECollection)wind.getParent();
/* 195 */           if ((parent != null) && (parent.getCollectionName().equalsIgnoreCase("jet"))) {
/* 196 */             Jet oldJet = (Jet)parent;
/* 197 */             newJet = oldJet.copy();
/* 198 */             DECollection newWind = wind.copy();
/* 199 */             newJet.replace(newJet.getNearestComponent(((Jet.JetBarb)de).getLocation()), newWind);
/* 200 */             this.drawingLayer.replaceElement(oldJet, newJet);
/*     */ 
/* 202 */             newWind.replace(newWind.getNearestComponent(((Jet.JetBarb)de).getLocation()), newEl);
/* 203 */             if ((newEl instanceof Jet.JetBarb)) {
/* 204 */               newEl.update(this);
/* 205 */               ((Jet.JetBarb)newEl).setSpeed(((Jet.JetBarb)newEl).getSpeed());
/* 206 */               JetAttrDlg.getInstance(getShell()).updateBarbTemplate((Jet.JetBarb)newEl);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 212 */         newEl.update(this);
/* 213 */         this.drawingLayer.replaceElement(de, newEl);
/*     */ 
/* 216 */         if ((de instanceof Jet.JetLine)) {
/* 217 */           AbstractDrawableComponent adc = (AbstractDrawableComponent)AttrSettings.getInstance().getSettings().get("JET");
/* 218 */           if ((adc instanceof Jet)) {
/* 219 */             ((Jet)adc).getJetLine().update(this);
/*     */           }
/*     */         }
/* 222 */         else if ((de instanceof Jet.JetHash)) {
/* 223 */           JetAttrDlg.getInstance(getShell()).updateHashTemplate((Jet.JetHash)newEl);
/*     */         }
/* 225 */         else if ((de instanceof Jet.JetText)) {
/* 226 */           JetAttrDlg.getInstance(getShell()).updateFlTemplate((Jet.JetText)newEl);
/*     */         }
/*     */       }
/*     */ 
/* 230 */       this.drawingLayer.removeSelected();
/* 231 */       this.drawingLayer.setSelected(newEl);
/*     */     }
/*     */     else
/*     */     {
/* 235 */       ArrayList adcList = null;
/* 236 */       ArrayList newList = new ArrayList();
/*     */ 
/* 239 */       if (this.drawingLayer != null)
/* 240 */         adcList = (ArrayList)this.drawingLayer.getAllSelected();
/*     */       ArrayList oldList;
/* 243 */       if ((adcList != null) && (!adcList.isEmpty())) {
/* 244 */         DrawableElement newEl = null;
/*     */ 
/* 246 */         for (AbstractDrawableComponent adc : adcList)
/*     */         {
/* 248 */           DrawableElement el = adc.getPrimaryDE();
/*     */ 
/* 250 */           if (el != null)
/*     */           {
/* 253 */             newEl = (DrawableElement)el.copy();
/*     */ 
/* 256 */             newEl.update(this);
/*     */ 
/* 258 */             if (((adc instanceof DECollection)) && (el.getParent() == adc))
/*     */             {
/* 260 */               DECollection dec = (DECollection)adc.copy();
/* 261 */               dec.remove(dec.getPrimaryDE());
/* 262 */               dec.add(0, newEl);
/* 263 */               newList.add(dec);
/*     */             }
/*     */             else {
/* 266 */               newList.add(newEl);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 271 */         if (newEl != null) {
/* 272 */           AttrSettings.getInstance().setSettings(newEl);
/*     */         }
/*     */ 
/* 275 */         oldList = new ArrayList(adcList);
/* 276 */         this.drawingLayer.replaceElements(null, oldList, newList);
/*     */       }
/*     */ 
/* 279 */       this.drawingLayer.removeSelected();
/*     */ 
/* 282 */       for (AbstractDrawableComponent adc : newList) {
/* 283 */         this.drawingLayer.addSelected(adc);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 288 */     if (this.mapEditor != null)
/* 289 */       this.mapEditor.refresh();
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 299 */     this.drawingLayer.removeSelected();
/* 300 */     this.drawingLayer.removeGhostLine();
/* 301 */     super.cancelPressed();
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 310 */     if (getShell() == null) {
/* 311 */       create();
/*     */     }
/* 313 */     if (this.shellLocation == null)
/* 314 */       getShell().setLocation(getShell().getParent().getLocation());
/*     */     else {
/* 316 */       getShell().setLocation(this.shellLocation);
/*     */     }
/*     */ 
/* 319 */     final Shell shell = getShell();
/*     */ 
/* 329 */     Display.getDefault().asyncExec(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 335 */         if ((shell != null) && (!shell.isDisposed()))
/* 336 */           AttrDlg.this.open();
/*     */       }
/*     */     });
/* 341 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 348 */     if (getShell() != null) {
/* 349 */       Rectangle bounds = getShell().getBounds();
/* 350 */       this.shellLocation = new Point(bounds.x, bounds.y);
/*     */     }
/* 352 */     return super.close();
/*     */   }
/*     */ 
/*     */   public void enableButtons()
/*     */   {
/* 360 */     getButton(1).setEnabled(true);
/* 361 */     getButton(0).setEnabled(true);
/*     */   }
/*     */ 
/*     */   public void setPgenType(String pgenType)
/*     */   {
/* 372 */     this.pgenType = pgenType;
/*     */   }
/*     */ 
/*     */   public void setPgenCategory(String pgenCategory)
/*     */   {
/* 383 */     this.pgenCategory = pgenCategory;
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 390 */     return null;
/*     */   }
/*     */ 
/*     */   public float getLineWidth() {
/* 394 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   public double getSizeScale() {
/* 398 */     return 1.0D;
/*     */   }
/*     */ 
/*     */   public String getType() {
/* 402 */     return null;
/*     */   }
/*     */ 
/*     */   public void setType(String type)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void addSeparator(Composite top)
/*     */   {
/* 414 */     GridData gd = new GridData(768);
/* 415 */     Label sepLbl = new Label(top, 258);
/* 416 */     sepLbl.setLayoutData(gd);
/*     */   }
/*     */ 
/*     */   public void setMouseHandlerName(String name) {
/* 420 */     mouseHandlerName = name;
/*     */   }
/*     */ 
/*     */   public void setDrawableElement(AbstractDrawableComponent adc) {
/* 424 */     if ((adc instanceof DrawableElement)) {
/* 425 */       DrawableElement de = (DrawableElement)adc;
/* 426 */       if ("INTL_SIGMET".equals(this.pgenType)) {
/* 427 */         ((SigmetAttrDlg)this).setSigmet(de);
/* 428 */         ((SigmetAttrDlg)this).copyEditableAttrToSigmetAttrDlg((Sigmet)de);
/* 429 */       } else if ("VOLC_SIGMET".equals(this.pgenType)) {
/* 430 */         ((VolcanoVaaAttrDlg)this).setVolcano(de);
/* 431 */       } else if ("VACL_SIGMET".equals(this.pgenType)) {
/* 432 */         ((VaaCloudDlg)this).setSigmet(de);
/* 433 */       } else if ("SIGMET".equalsIgnoreCase(this.pgenCategory))
/*     */       {
/* 435 */         if ("CCFP_SIGMET".equals(this.pgenType)) {
/* 436 */           ((CcfpAttrDlg)this).setAbstractSigmet(de);
/* 437 */           return;
/*     */         }
/*     */ 
/* 440 */         ((SigmetCommAttrDlg)this).setAbstractSigmet(de);
/* 441 */         ((SigmetCommAttrDlg)this).copyEditableAttrToSigmetAttrDlg((AbstractSigmet)de);
/*     */       }
/*     */       else {
/* 444 */         de = de;
/*     */       }
/*     */     }
/*     */     else {
/* 448 */       de = adc;
/*     */     }
/*     */   }
/*     */ 
/*     */   public AbstractDrawableComponent getDrawableElement() {
/* 453 */     return de;
/*     */   }
/*     */ 
/*     */   public void setDefaultAttr()
/*     */   {
/* 461 */     AbstractDrawableComponent adc = (AbstractDrawableComponent)AttrSettings.getInstance().getSettings().get(this.pgenType);
/* 462 */     if (adc != null)
/* 463 */       setAttr(adc);
/*     */   }
/*     */ 
/*     */   public void setAttr(AbstractDrawableComponent adc)
/*     */   {
/* 472 */     if ((adc instanceof IAttribute))
/* 473 */       setAttrForDlg((IAttribute)adc);
/*     */   }
/*     */ 
/*     */   public boolean isAddLineMode()
/*     */   {
/* 482 */     return false;
/*     */   }
/*     */ 
/*     */   public void resetLabeledLineBtns()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected GridLayout getCompactGridLayout(int numCol)
/*     */   {
/* 496 */     return getGridLayout(numCol, false, 0, 0, 0, 0);
/*     */   }
/*     */ 
/*     */   protected GridLayout getGridLayout(int numCol, boolean equal_width, int marginHeight, int marginWidth, int horizontalSpacing, int verticalSpacing)
/*     */   {
/* 506 */     GridLayout gl = new GridLayout(numCol, equal_width);
/* 507 */     gl.marginHeight = marginHeight;
/* 508 */     gl.marginWidth = marginWidth;
/* 509 */     gl.horizontalSpacing = horizontalSpacing;
/* 510 */     gl.verticalSpacing = verticalSpacing;
/*     */ 
/* 512 */     return gl;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg
 * JD-Core Version:    0.6.2
 */