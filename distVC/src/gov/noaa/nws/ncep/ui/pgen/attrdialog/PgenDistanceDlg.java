/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.SelectionListener;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.layout.RowLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class PgenDistanceDlg extends AttrDlg
/*     */ {
/*  52 */   static PgenDistanceDlg INSTANCE = null;
/*     */   public static final String STATUTE_MILES = "sm";
/*     */   public static final String NAUTICAL_MILES = "nm";
/*     */   public static final String KILOMETERS = "km";
/*     */   public static final String COMPASS_16_PT = "16-pt";
/*     */   public static final String DIR_DEGREES = "DEG";
/*  77 */   private DistanceDisplayProperties distProps = new DistanceDisplayProperties(false, 
/*  78 */     "nm", "16-pt");
/*     */   private static final String DIALOG_LABEL = "Distance Options";
/*     */   private static final String CLOSE_LABEL = "Close";
/*  83 */   private Composite top = null;
/*     */ 
/*  85 */   private Button distanceDisplay = null;
/*  86 */   private Combo distanceUnits = null;
/*  87 */   private Combo directionUnits = null;
/*     */ 
/*     */   private PgenDistanceDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  96 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static PgenDistanceDlg getInstance(Shell parShell)
/*     */   {
/* 109 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/* 113 */         INSTANCE = new PgenDistanceDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 117 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 122 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 135 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 138 */     GridLayout mainLayout = new GridLayout(1, true);
/* 139 */     mainLayout.marginHeight = 3;
/* 140 */     mainLayout.marginWidth = 3;
/* 141 */     this.top.setLayout(mainLayout);
/*     */ 
/* 144 */     initializeComponents();
/*     */ 
/* 146 */     return this.top;
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 155 */     createButton(parent, 12, "Close", true);
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 165 */     getShell().setText("Distance Options");
/*     */ 
/* 167 */     this.distanceDisplay = new Button(this.top, 32);
/* 168 */     this.distanceDisplay.setText("Display Distance");
/* 169 */     this.distanceDisplay.setSelection(this.distProps.displayDistance);
/* 170 */     GridData gd = new GridData(16777216, 16777216, false, false);
/* 171 */     this.distanceDisplay.setLayoutData(gd);
/* 172 */     this.distanceDisplay.addSelectionListener(new DistancePropertiesListener());
/*     */ 
/* 174 */     Composite distanceUnitsGroup = new Composite(this.top, 0);
/* 175 */     RowLayout rl1 = new RowLayout();
/* 176 */     rl1.justify = true;
/* 177 */     rl1.pack = false;
/* 178 */     distanceUnitsGroup.setLayout(rl1);
/*     */ 
/* 183 */     Label du = new Label(distanceUnitsGroup, 0);
/* 184 */     du.setText("Distance Units:");
/*     */ 
/* 189 */     this.distanceUnits = new Combo(distanceUnitsGroup, 12);
/* 190 */     this.distanceUnits.add("sm");
/* 191 */     this.distanceUnits.add("nm");
/* 192 */     this.distanceUnits.add("km");
/* 193 */     int index = this.distanceUnits.indexOf(this.distProps.distanceUnits);
/* 194 */     this.distanceUnits.select(index);
/*     */ 
/* 196 */     this.distanceUnits.setEnabled(this.distProps.displayDistance);
/* 197 */     this.distanceUnits.addSelectionListener(new DistancePropertiesListener());
/*     */ 
/* 199 */     Composite directionUnitsGroup = new Composite(this.top, 0);
/* 200 */     RowLayout rl2 = new RowLayout();
/* 201 */     rl2.justify = true;
/* 202 */     rl2.pack = false;
/* 203 */     directionUnitsGroup.setLayout(rl2);
/*     */ 
/* 208 */     Label dir = new Label(directionUnitsGroup, 0);
/* 209 */     dir.setText("Direction Units:");
/*     */ 
/* 214 */     this.directionUnits = new Combo(directionUnitsGroup, 12);
/* 215 */     this.directionUnits.add("16-pt");
/* 216 */     this.directionUnits.add("DEG");
/* 217 */     int idx = this.directionUnits.indexOf(this.distProps.directionUnits);
/* 218 */     this.directionUnits.select(idx);
/*     */ 
/* 220 */     this.directionUnits.setEnabled(this.distProps.displayDistance);
/* 221 */     this.directionUnits.addSelectionListener(new DistancePropertiesListener());
/*     */   }
/*     */ 
/*     */   protected void buttonPressed(int buttonId)
/*     */   {
/* 232 */     if (12 == buttonId)
/*     */     {
/* 234 */       PgenUtil.setSelectingMode();
/*     */     }
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 244 */     HashMap attr = new HashMap();
/*     */ 
/* 246 */     return attr;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute attr)
/*     */   {
/*     */   }
/*     */ 
/*     */   public DistanceDisplayProperties getDistanceProperties()
/*     */   {
/* 256 */     return this.distProps;
/*     */   }
/*     */ 
/*     */   public static class DistanceDisplayProperties
/*     */   {
/*     */     public boolean displayDistance;
/*     */     public String distanceUnits;
/*     */     public String directionUnits;
/*     */ 
/*     */     public DistanceDisplayProperties(boolean displayDistance, String distanceUnits, String directionUnits)
/*     */     {
/*  70 */       this.displayDistance = displayDistance;
/*  71 */       this.distanceUnits = distanceUnits;
/*  72 */       this.directionUnits = directionUnits;
/*     */     }
/*     */   }
/*     */ 
/*     */   class DistancePropertiesListener
/*     */     implements SelectionListener
/*     */   {
/*     */     DistancePropertiesListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void widgetSelected(SelectionEvent e)
/*     */     {
/* 270 */       if ((e.getSource() instanceof Button)) {
/* 271 */         Button b = (Button)e.getSource();
/* 272 */         if (b.getSelection()) {
/* 273 */           PgenDistanceDlg.this.distanceUnits.setEnabled(true);
/* 274 */           PgenDistanceDlg.this.directionUnits.setEnabled(true);
/* 275 */           PgenDistanceDlg.this.distProps.displayDistance = true;
/*     */         }
/*     */         else {
/* 278 */           PgenDistanceDlg.this.distanceUnits.setEnabled(false);
/* 279 */           PgenDistanceDlg.this.directionUnits.setEnabled(false);
/* 280 */           PgenDistanceDlg.this.distProps.displayDistance = false;
/*     */         }
/*     */ 
/*     */       }
/* 284 */       else if (e.getSource() == PgenDistanceDlg.this.distanceUnits) {
/* 285 */         PgenDistanceDlg.this.distProps.distanceUnits = PgenDistanceDlg.this.distanceUnits.getText();
/*     */       }
/* 288 */       else if (e.getSource() == PgenDistanceDlg.this.directionUnits) {
/* 289 */         PgenDistanceDlg.this.distProps.directionUnits = PgenDistanceDlg.this.directionUnits.getText();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void widgetDefaultSelected(SelectionEvent e)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenDistanceDlg
 * JD-Core Version:    0.6.2
 */