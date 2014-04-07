/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.SpinnerSlider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Spenes;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class SpenesAttrDlg extends LineAttrDlg
/*     */ {
/*     */   private static final int CREATE_ID = 201206;
/*     */   private static final int EDIT_ID = 201207;
/*  56 */   private static SpenesAttrDlg INSTANCE = null;
/*  57 */   private static SpenesFormatDlg sfDlg = null;
/*     */ 
/*     */   private SpenesAttrDlg(Shell parShell) throws VizException
/*     */   {
/*  61 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static SpenesAttrDlg getInstance(Shell parShell)
/*     */   {
/*  73 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/*  77 */         INSTANCE = new SpenesAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/*  81 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  86 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   protected void initializeComponents()
/*     */   {
/*  95 */     getShell().setText("SPENES Attributes");
/*     */ 
/*  98 */     Composite panel0 = new Composite(this.top, 0);
/*     */ 
/* 101 */     GridLayout p0Layout = new GridLayout(2, false);
/* 102 */     p0Layout.marginHeight = 3;
/* 103 */     p0Layout.marginWidth = 3;
/* 104 */     panel0.setLayout(p0Layout);
/*     */ 
/* 106 */     createColorAttr(panel0);
/* 107 */     createWidthAttr(panel0);
/* 108 */     createSmoothAttr(panel0);
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 120 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/* 121 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*     */ 
/* 123 */     createButton(parent, 0, "Apply", true);
/* 124 */     createButton(parent, 201206, "Create", true);
/* 125 */     createButton(parent, 201207, "Edit", true);
/* 126 */     createButton(parent, 1, "Cancel", true);
/*     */ 
/* 128 */     getButton(0).setEnabled(false);
/* 129 */     getButton(1).setEnabled(false);
/* 130 */     getButton(201206).setEnabled(true);
/* 131 */     getButton(201207).setEnabled(true);
/*     */ 
/* 133 */     getButton(1).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/* 134 */     getButton(0).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/* 135 */     getButton(201206).setLayoutData(new GridData(ctrlBtnWidth + 10, ctrlBtnHeight));
/* 136 */     getButton(201207).setLayoutData(new GridData(ctrlBtnWidth, ctrlBtnHeight));
/*     */   }
/*     */ 
/*     */   protected Button createButton(Composite parent, int id, String label, boolean defaultButton)
/*     */   {
/* 142 */     Button button = super.createButton(parent, id, label, defaultButton);
/* 143 */     button.setLayoutData(new GridData(60, 30));
/* 144 */     return button;
/*     */   }
/*     */ 
/*     */   protected void buttonPressed(int buttonId)
/*     */   {
/* 153 */     if (buttonId == 0)
/* 154 */       okPressed();
/* 155 */     else if (1 == buttonId)
/* 156 */       cancelPressed();
/* 157 */     else if (201206 == buttonId)
/* 158 */       createPressed();
/* 159 */     else if (201207 == buttonId)
/* 160 */       editPressed();
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute iattr)
/*     */   {
/* 171 */     Spenes attr = (Spenes)iattr;
/* 172 */     Color[] clr = attr.getColors();
/* 173 */     if (clr != null) setColor(clr);
/*     */ 
/* 175 */     float lw = attr.getLineWidth();
/* 176 */     if (lw > 0.0F) {
/* 177 */       this.widthSpinnerSlider.setSelection((int)lw);
/*     */     }
/* 179 */     double ps = attr.getSmoothFactor();
/* 180 */     if (ps > 0.0D) {
/* 181 */       this.smoothLvlCbo.select((int)ps);
/*     */     }
/* 183 */     setSpenes(attr);
/*     */   }
/*     */ 
/*     */   private void createColorAttr(Composite comp)
/*     */   {
/* 193 */     this.colorLbl = new Label(comp, 16384);
/* 194 */     this.colorLbl.setText("Color:");
/* 195 */     this.cs = new ColorButtonSelector(comp);
/* 196 */     this.cs.setColorValue(new RGB(255, 0, 0));
/*     */   }
/*     */ 
/*     */   private void createWidthAttr(Composite comp)
/*     */   {
/* 204 */     this.widthLbl = new Label(comp, 16384);
/* 205 */     this.widthLbl.setText("Line Width:");
/*     */ 
/* 207 */     GridLayout gl = new GridLayout(3, false);
/* 208 */     Group widthGrp = new Group(comp, 0);
/* 209 */     widthGrp.setLayout(gl);
/*     */ 
/* 211 */     this.widthSpinnerSlider = 
/* 212 */       new SpinnerSlider(widthGrp, 256, 1);
/* 213 */     this.widthSpinnerSlider.setLayoutData(new GridData(180, 30));
/* 214 */     this.widthSpinnerSlider.setMinimum(1);
/* 215 */     this.widthSpinnerSlider.setMaximum(10);
/* 216 */     this.widthSpinnerSlider.setIncrement(1);
/* 217 */     this.widthSpinnerSlider.setPageIncrement(3);
/* 218 */     this.widthSpinnerSlider.setDigits(0);
/*     */   }
/*     */ 
/*     */   private void createSmoothAttr(Composite comp)
/*     */   {
/* 226 */     this.smoothLbl = new Label(comp, 16384);
/* 227 */     this.smoothLbl.setText("Smooth Level:");
/*     */ 
/* 229 */     this.smoothLvlCbo = new Combo(comp, 12);
/*     */ 
/* 231 */     this.smoothLvlCbo.add("0");
/* 232 */     this.smoothLvlCbo.add("1");
/* 233 */     this.smoothLvlCbo.add("2");
/*     */ 
/* 235 */     this.smoothLvlCbo.select(0);
/*     */   }
/*     */ 
/*     */   public Color[] getColors()
/*     */   {
/* 245 */     Color[] colors = new Color[2];
/*     */ 
/* 247 */     colors[0] = new Color(this.cs.getColorValue().red, 
/* 248 */       this.cs.getColorValue().green, this.cs.getColorValue().blue);
/*     */ 
/* 250 */     colors[1] = Color.green;
/*     */ 
/* 252 */     return colors;
/*     */   }
/*     */ 
/*     */   public void setColor(Color[] clr)
/*     */   {
/* 261 */     this.cs.setColorValue(new RGB(clr[0].getRed(), clr[0].getGreen(), clr[0].getBlue()));
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 269 */     return this.widthSpinnerSlider.getSelection();
/*     */   }
/*     */ 
/*     */   public int getSmoothFactor()
/*     */   {
/* 276 */     return this.smoothLvlCbo.getSelectionIndex();
/*     */   }
/*     */ 
/*     */   public double getSizeScale()
/*     */   {
/* 282 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public Boolean isClosedLine()
/*     */   {
/* 288 */     return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   public Boolean isFilled()
/*     */   {
/* 295 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 303 */     super.okPressed();
/* 304 */     if (de != null) {
/* 305 */       Spenes spenes = (Spenes)de;
/* 306 */       MessageDialog infoDlg = null;
/* 307 */       if (!PgenStaticDataProvider.getProvider().isRfcLoaded()) {
/* 308 */         infoDlg = new MessageDialog(
/* 309 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 310 */           "Information", null, "Loading state, cwa, and rfc maps.\nPlease wait.", 
/* 311 */           2, new String[] { "OK" }, 0);
/* 312 */         infoDlg.setBlockOnOpen(false);
/* 313 */         infoDlg.open();
/*     */       }
/* 315 */       spenes.generateStatesWfosRfcs();
/* 316 */       spenes.setStateZ000(spenes.getStates());
/* 317 */       spenes.setLocation(spenes.getStates());
/* 318 */       spenes.setAttnWFOs(spenes.getCwas());
/* 319 */       spenes.setAttnRFCs(spenes.getRfcs());
/* 320 */       spenes.setLatLon(spenes.getLinePoints());
/*     */ 
/* 322 */       if ((infoDlg != null) && (infoDlg.getShell() != null)) infoDlg.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void createPressed()
/*     */   {
/* 330 */     if (de != null)
/*     */     {
/* 332 */       okPressed();
/*     */ 
/* 334 */       ((Spenes)de).setInitTime();
/* 335 */       sfDlg = SpenesFormatDlg.getInstance(getShell(), INSTANCE, (Spenes)de);
/*     */ 
/* 337 */       sfDlg.setBlockOnOpen(false);
/*     */ 
/* 339 */       sfDlg.open();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void editPressed()
/*     */   {
/* 350 */     if (de != null) {
/* 351 */       sfDlg = SpenesFormatDlg.getInstance(getShell(), INSTANCE, (Spenes)de);
/* 352 */       sfDlg.setBlockOnOpen(false);
/*     */ 
/* 354 */       sfDlg.open();
/*     */     }
/*     */     else {
/* 357 */       MessageDialog infoDlg = null;
/* 358 */       infoDlg = new MessageDialog(
/* 359 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 360 */         "Information", null, "No drawing element to edit.", 
/* 361 */         2, new String[] { "OK" }, 0);
/* 362 */       infoDlg.setBlockOnOpen(false);
/* 363 */       infoDlg.open();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSpenes(Spenes elem)
/*     */   {
/* 372 */     de = elem;
/*     */   }
/*     */ 
/*     */   public Spenes getSpenes()
/*     */   {
/* 380 */     return (Spenes)de;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.SpenesAttrDlg
 * JD-Core Version:    0.6.2
 */