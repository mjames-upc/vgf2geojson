/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class PgenExtrapDlg extends AttrDlg
/*     */ {
/*  49 */   static PgenExtrapDlg INSTANCE = null;
/*     */   public static final double S2HR = 3600.0D;
/*     */   public static final double HR2S = 0.0002777999907266349D;
/*     */   public static final double SM2M = 1609.3399658203125D;
/*     */   public static final double M2SM = 0.0006210000137798488D;
/*     */   public static final double MS2SMH = 2.235600049607456D;
/*     */   public static final double SMH2MS = 0.4470746275808857D;
/*     */   public static final double NM2M = 1852.0D;
/*     */   public static final double M2NM = 0.000539999979082495D;
/*     */   public static final double MS2NMH = 1.943999924696982D;
/*     */   public static final double NMH2MS = 0.5144855828257278D;
/*  63 */   public static String[] SpeedUnit = { "KTS", "MPH", "M/S" };
/*  64 */   public static String[] DurationOption = { "00:15", "00:30", "01:00", "02:00", 
/*  65 */     "06:00", "12:00", "Other" };
/*     */ 
/*  67 */   private Composite top = null;
/*     */ 
/*  69 */   private Text speedText = null;
/*  70 */   private Combo speedUnitCombo = null;
/*  71 */   private Text directionText = null;
/*     */ 
/*  73 */   private Text durationText = null;
/*  74 */   private Combo durationOptionCombo = null;
/*     */ 
/*  76 */   private Button copyBtn = null;
/*  77 */   private Button moveBtn = null;
/*     */ 
/*     */   private PgenExtrapDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  86 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static PgenExtrapDlg getInstance(Shell parShell)
/*     */   {
/*  99 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/* 103 */         INSTANCE = new PgenExtrapDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 107 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 112 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 125 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 128 */     GridLayout mainLayout = new GridLayout(3, false);
/* 129 */     mainLayout.marginHeight = 3;
/* 130 */     mainLayout.marginWidth = 3;
/* 131 */     this.top.setLayout(mainLayout);
/*     */ 
/* 134 */     initializeComponents();
/*     */ 
/* 136 */     return this.top;
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 154 */     getShell().setText("Extrapolation");
/*     */ 
/* 159 */     GridData textGridData = new GridData(40, 20);
/*     */ 
/* 161 */     Label speedLbl = new Label(this.top, 16384);
/* 162 */     speedLbl.setText("Speed:");
/*     */ 
/* 164 */     this.speedText = new Text(this.top, 2052);
/* 165 */     this.speedText.setLayoutData(textGridData);
/* 166 */     this.speedText.setEditable(true);
/* 167 */     this.speedText.setText("30.0");
/*     */ 
/* 169 */     this.speedUnitCombo = new Combo(this.top, 12);
/* 170 */     for (String st : SpeedUnit) {
/* 171 */       this.speedUnitCombo.add(st);
/*     */     }
/*     */ 
/* 174 */     this.speedUnitCombo.select(1);
/*     */ 
/* 176 */     Label directionLbl = new Label(this.top, 16384);
/* 177 */     directionLbl.setText("Direction:");
/*     */ 
/* 182 */     this.directionText = new Text(this.top, 2052);
/* 183 */     this.directionText.setLayoutData(textGridData);
/* 184 */     this.directionText.setEditable(true);
/* 185 */     this.directionText.setText("270.0");
/*     */ 
/* 187 */     Label dummyLbl = new Label(this.top, 16384);
/* 188 */     dummyLbl.setText("");
/*     */ 
/* 193 */     Label durationLbl = new Label(this.top, 16384);
/* 194 */     durationLbl.setText("Duration:");
/*     */ 
/* 196 */     int defaultDuration = 4;
/*     */ 
/* 198 */     this.durationText = new Text(this.top, 2052);
/* 199 */     this.durationText.setLayoutData(textGridData);
/* 200 */     this.durationText.setText(DurationOption[defaultDuration]);
/* 201 */     this.durationText.setEnabled(false);
/*     */ 
/* 203 */     this.durationOptionCombo = new Combo(this.top, 12);
/* 204 */     for (String st : DurationOption) {
/* 205 */       this.durationOptionCombo.add(st);
/*     */     }
/* 207 */     this.durationOptionCombo.select(defaultDuration);
/*     */ 
/* 209 */     this.durationOptionCombo.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent e) {
/* 211 */         String selected = PgenExtrapDlg.DurationOption[PgenExtrapDlg.this.durationOptionCombo.getSelectionIndex()];
/*     */ 
/* 213 */         if (selected.equalsIgnoreCase("Other")) {
/* 214 */           PgenExtrapDlg.this.durationText.setEnabled(true);
/*     */         }
/*     */         else {
/* 217 */           PgenExtrapDlg.this.durationText.setEnabled(false);
/* 218 */           PgenExtrapDlg.this.durationText.setText(selected);
/*     */         }
/*     */       }
/*     */     });
/* 226 */     this.copyBtn = new Button(this.top, 16);
/* 227 */     this.copyBtn.setText("Copy");
/* 228 */     this.copyBtn.setSelection(true);
/*     */ 
/* 230 */     Label grpLbl = new Label(this.top, 16384);
/* 231 */     grpLbl.setText("");
/*     */ 
/* 233 */     this.moveBtn = new Button(this.top, 16);
/* 234 */     this.moveBtn.setText("Move");
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 243 */     HashMap attr = new HashMap();
/*     */ 
/* 245 */     return attr;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute attr)
/*     */   {
/*     */   }
/*     */ 
/*     */   public double getSpeed()
/*     */   {
/* 259 */     String unit = SpeedUnit[this.speedUnitCombo.getSelectionIndex()];
/*     */ 
/* 261 */     double speed = Double.parseDouble(this.speedText.getText());
/*     */ 
/* 263 */     if (unit.equalsIgnoreCase("KTS")) {
/* 264 */       speed *= 0.5144855828257278D;
/*     */     }
/* 266 */     else if (unit.equalsIgnoreCase("MPH")) {
/* 267 */       speed *= 0.4470746275808857D;
/*     */     }
/*     */ 
/* 270 */     return speed;
/*     */   }
/*     */ 
/*     */   public double getDirection()
/*     */   {
/* 282 */     double dir = Double.parseDouble(this.directionText.getText());
/*     */ 
/* 284 */     dir -= (int)dir / 360 * 360;
/*     */ 
/* 286 */     if (dir < 0.0D) dir += 360.0D;
/*     */ 
/* 288 */     this.directionText.setText(dir);
/*     */ 
/* 290 */     return dir;
/*     */   }
/*     */ 
/*     */   public double getDistance()
/*     */   {
/* 299 */     return getSpeedInMeter() * getDuration();
/*     */   }
/*     */ 
/*     */   public boolean isCopy()
/*     */   {
/* 307 */     return this.copyBtn.getSelection();
/*     */   }
/*     */ 
/*     */   private double getSpeedInMeter()
/*     */   {
/* 316 */     String unit = SpeedUnit[this.speedUnitCombo.getSelectionIndex()];
/*     */ 
/* 318 */     double speed = Double.parseDouble(this.speedText.getText());
/*     */ 
/* 320 */     if (unit.equalsIgnoreCase("KTS")) {
/* 321 */       speed *= 0.5144855828257278D;
/*     */     }
/* 323 */     else if (unit.equalsIgnoreCase("MPH")) {
/* 324 */       speed *= 0.4470746275808857D;
/*     */     }
/*     */ 
/* 330 */     if (speed < 0.0D) {
/* 331 */       speed *= -1.0D;
/* 332 */       this.speedText.setText(speed);
/*     */     }
/*     */ 
/* 335 */     return speed;
/*     */   }
/*     */ 
/*     */   private double getDuration()
/*     */   {
/* 344 */     String[] st = this.durationText.getText().split(":");
/*     */ 
/* 346 */     double duration = 0.0D;
/*     */ 
/* 348 */     if (st.length > 0) {
/* 349 */       duration += 3600.0D * Integer.parseInt(st[0]);
/*     */     }
/*     */ 
/* 352 */     if (st.length > 1) {
/* 353 */       duration += 60.0D * Integer.parseInt(st[1]);
/*     */     }
/*     */ 
/* 359 */     if (duration < 0.0D)
/*     */     {
/* 361 */       int durationIndex = 1;
/*     */ 
/* 363 */       this.durationText.setText(DurationOption[durationIndex]);
/* 364 */       this.durationOptionCombo.select(durationIndex);
/*     */ 
/* 366 */       duration = 1800.0D;
/*     */     }
/*     */ 
/* 370 */     return duration;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenExtrapDlg
 * JD-Core Version:    0.6.2
 */