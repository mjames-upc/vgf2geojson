/*     */ package gov.noaa.nws.ncep.ui.pgen.controls;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.core.mode.CAVEMode;
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.ActivityInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResourceData;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import java.util.Calendar;
/*     */ import java.util.TimeZone;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.DateTime;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class StoreActivityDialog extends CaveJFACEDialog
/*     */ {
/*  56 */   private String title = null;
/*     */   private Shell shell;
/*     */   private static final int SAVE_ID = 4865;
/*     */   private static final String SAVE_LABEL = "Save";
/*     */   private static final int CANCEL_ID = 4866;
/*     */   private static final String CANCEL_LABEL = "Cancel";
/*  68 */   private Button cancelBtn = null;
/*     */ 
/*  70 */   private Button saveBtn = null;
/*     */ 
/*  72 */   private Text infoText = null;
/*     */ 
/*  74 */   private Text nameText = null;
/*     */ 
/*  76 */   private Text typeText = null;
/*     */ 
/*  78 */   private Text subtypeText = null;
/*     */ 
/*  80 */   private Text siteText = null;
/*     */ 
/*  82 */   private Text deskText = null;
/*     */ 
/*  84 */   private Text forecasterText = null;
/*     */   private Text modeText;
/*     */   private Text statusText;
/*     */   private DateTime validDate;
/*     */   private DateTime validTime;
/*     */   private Button autoSaveOffBtn;
/*     */   private Button autoSaveOnBtn;
/*     */   private PgenResource rsc;
/*     */   private Product activity;
/*     */   private Text messageText;
/*     */ 
/*     */   public StoreActivityDialog(Shell parShell, String btnName)
/*     */     throws VizException
/*     */   {
/* 110 */     super(parShell);
/* 111 */     setStoreMode(btnName);
/* 112 */     this.rsc = PgenSession.getInstance().getPgenResource();
/* 113 */     this.activity = this.rsc.getActiveProduct();
/*     */   }
/*     */ 
/*     */   private void setStoreMode(String btnName)
/*     */   {
/* 122 */     if (btnName.equals("Open"))
/* 123 */       this.title = "Open a PGEN Product file";
/* 124 */     else if ((btnName.equals("Save")) || (btnName.equals("Save All")))
/* 125 */       this.title = "Save the PGEN Product";
/* 126 */     else if (btnName.equals("Save As"))
/* 127 */       this.title = "Save the PGEN Product as";
/*     */   }
/*     */ 
/*     */   protected void configureShell(Shell shell)
/*     */   {
/* 141 */     setShellStyle(32784);
/* 142 */     super.configureShell(shell);
/*     */ 
/* 144 */     this.shell = shell;
/* 145 */     if (this.title != null)
/* 146 */       shell.setText(this.title);
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 158 */     Composite dlgAreaForm = (Composite)super.createDialogArea(parent);
/* 159 */     GridLayout mainLayout = new GridLayout(1, true);
/* 160 */     mainLayout.marginHeight = 3;
/* 161 */     mainLayout.marginWidth = 3;
/* 162 */     dlgAreaForm.setLayout(mainLayout);
/*     */ 
/* 167 */     Composite g1 = new Composite(dlgAreaForm, 0);
/* 168 */     g1.setLayoutData(new GridData(768));
/* 169 */     createActivityInfoArea(g1);
/*     */ 
/* 174 */     Composite g2 = new Composite(dlgAreaForm, 0);
/* 175 */     g2.setLayoutData(new GridData(768));
/* 176 */     createRefTimeArea(g2);
/*     */ 
/* 181 */     Composite g3 = new Composite(dlgAreaForm, 0);
/* 182 */     g3.setLayoutData(new GridData(768));
/* 183 */     createAutoSaveArea(g3);
/*     */ 
/* 188 */     Group g4 = new Group(dlgAreaForm, 0);
/* 189 */     g4.setLayoutData(new GridData(768));
/* 190 */     createMessageArea(g4);
/*     */ 
/* 192 */     setDialogFields();
/* 193 */     return dlgAreaForm;
/*     */   }
/*     */ 
/*     */   private void createActivityInfoArea(Composite g1)
/*     */   {
/* 198 */     g1.setLayout(new GridLayout(2, false));
/* 199 */     GridData gdata = new GridData(4, 16777216, true, false);
/*     */ 
/* 201 */     Label infoLabel = new Label(g1, 0);
/* 202 */     infoLabel.setText("Activity Label:");
/*     */ 
/* 204 */     this.infoText = new Text(g1, 0);
/* 205 */     this.infoText.setLayoutData(gdata);
/*     */ 
/* 207 */     Label nameLabel = new Label(g1, 0);
/* 208 */     nameLabel.setText("Activity Name:");
/*     */ 
/* 210 */     this.nameText = new Text(g1, 0);
/* 211 */     this.nameText.setLayoutData(gdata);
/*     */ 
/* 213 */     Label typeLabel = new Label(g1, 0);
/* 214 */     typeLabel.setText("Activity Type:");
/*     */ 
/* 216 */     this.typeText = new Text(g1, 0);
/* 217 */     this.typeText.setLayoutData(gdata);
/*     */ 
/* 219 */     Label subtypeLabel = new Label(g1, 0);
/* 220 */     subtypeLabel.setText("Activity Subtype:");
/*     */ 
/* 222 */     this.subtypeText = new Text(g1, 0);
/* 223 */     this.subtypeText.setLayoutData(gdata);
/*     */ 
/* 225 */     Label siteLabel = new Label(g1, 0);
/* 226 */     siteLabel.setText("Site:");
/*     */ 
/* 228 */     this.siteText = new Text(g1, 0);
/* 229 */     this.siteText.setLayoutData(gdata);
/*     */ 
/* 231 */     Label deskLabel = new Label(g1, 0);
/* 232 */     deskLabel.setText("Desk:");
/*     */ 
/* 234 */     this.deskText = new Text(g1, 0);
/* 235 */     this.deskText.setLayoutData(gdata);
/*     */ 
/* 237 */     Label forecasterLabel = new Label(g1, 0);
/* 238 */     forecasterLabel.setText("Forecaster:");
/*     */ 
/* 240 */     this.forecasterText = new Text(g1, 0);
/* 241 */     this.forecasterText.setLayoutData(gdata);
/*     */ 
/* 243 */     Label modeLabel = new Label(g1, 0);
/* 244 */     modeLabel.setText("Operating Mode:");
/*     */ 
/* 246 */     this.modeText = new Text(g1, 0);
/* 247 */     this.modeText.setLayoutData(gdata);
/*     */ 
/* 249 */     Label statusLabel = new Label(g1, 0);
/* 250 */     statusLabel.setText("Activity Status:");
/*     */ 
/* 252 */     this.statusText = new Text(g1, 0);
/* 253 */     this.statusText.setLayoutData(gdata);
/*     */   }
/*     */ 
/*     */   private void createRefTimeArea(Composite g2)
/*     */   {
/* 259 */     g2.setLayout(new GridLayout(4, false));
/*     */ 
/* 261 */     Label refTimeLabel = new Label(g2, 0);
/* 262 */     refTimeLabel.setText("Ref Time:");
/*     */ 
/* 264 */     this.validDate = new DateTime(g2, 2080);
/* 265 */     this.validTime = new DateTime(g2, 34944);
/*     */ 
/* 267 */     Label utcLabel = new Label(g2, 0);
/* 268 */     utcLabel.setText("UTC");
/*     */   }
/*     */ 
/*     */   private void createAutoSaveArea(Composite g3)
/*     */   {
/* 273 */     g3.setLayout(new GridLayout(3, false));
/*     */ 
/* 275 */     Label autoSaveLbl = new Label(g3, 0);
/* 276 */     autoSaveLbl.setText("Auto Save:");
/*     */ 
/* 278 */     this.autoSaveOffBtn = new Button(g3, 16);
/* 279 */     this.autoSaveOffBtn.setText("Off");
/* 280 */     this.autoSaveOffBtn.setSelection(true);
/*     */ 
/* 282 */     this.autoSaveOnBtn = new Button(g3, 16);
/* 283 */     this.autoSaveOnBtn.setText("On");
/* 284 */     this.autoSaveOnBtn.setSelection(false);
/*     */   }
/*     */ 
/*     */   private void createMessageArea(Composite g4)
/*     */   {
/* 289 */     g4.setLayout(new GridLayout(1, true));
/* 290 */     GridData gdata = new GridData(4, 16777216, true, false);
/*     */ 
/* 292 */     this.messageText = new Text(g4, 778);
/*     */ 
/* 294 */     this.messageText.setLayoutData(gdata);
/*     */   }
/*     */ 
/*     */   protected void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 303 */     this.saveBtn = createButton(parent, 4865, "Save", true);
/*     */ 
/* 305 */     this.cancelBtn = createButton(parent, 4866, "Cancel", true);
/*     */   }
/*     */ 
/*     */   protected void buttonPressed(int buttonId)
/*     */   {
/* 312 */     if (buttonId == 4865)
/* 313 */       storeProducts();
/* 314 */     else if (buttonId == 4866)
/* 315 */       close();
/*     */   }
/*     */ 
/*     */   private void setDialogFields()
/*     */   {
/* 321 */     if (this.activity.getOutputFile() != null)
/* 322 */       this.infoText.setText(this.activity.getOutputFile());
/* 323 */     if (this.activity.getName() != null)
/* 324 */       this.nameText.setText(this.activity.getName());
/* 325 */     if (this.activity.getType() != null)
/* 326 */       this.typeText.setText(this.activity.getType());
/* 327 */     if (this.activity.getCenter() != null)
/* 328 */       this.siteText.setText(this.activity.getCenter());
/* 329 */     if (this.activity.getForecaster() != null)
/* 330 */       this.forecasterText.setText(this.activity.getForecaster());
/* 331 */     this.modeText.setText(CAVEMode.getMode().name());
/* 332 */     this.statusText.setText("Unknown");
/*     */ 
/* 334 */     Calendar datetime = this.activity.getTime().getStartTime();
/* 335 */     if (datetime != null) {
/* 336 */       this.validDate.setYear(datetime.get(1));
/* 337 */       this.validDate.setMonth(datetime.get(2));
/* 338 */       this.validDate.setDay(datetime.get(5));
/* 339 */       this.validTime.setHours(datetime.get(11));
/* 340 */       this.validTime.setMinutes(datetime.get(12));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void storeProducts()
/*     */   {
/* 350 */     String activityLabel = this.infoText.getText();
/*     */ 
/* 352 */     if ((activityLabel == null) || (activityLabel.isEmpty())) {
/* 353 */       MessageDialog confirmDlg = new MessageDialog(
/* 354 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow()
/* 355 */         .getShell(), 
/* 356 */         "Need More Information", 
/* 357 */         null, 
/* 358 */         "Activity Info field is required.\nPlease enter an appropriate string and then try saving!", 
/* 359 */         4, new String[] { "OK" }, 0);
/*     */ 
/* 361 */       confirmDlg.open();
/* 362 */       this.infoText.forceFocus();
/* 363 */       return;
/*     */     }
/*     */ 
/* 367 */     this.rsc.setAutosave(this.autoSaveOnBtn.getSelection());
/* 368 */     this.rsc.setAutoSaveFilename(activityLabel);
/*     */ 
/* 372 */     this.activity.setInputFile(activityLabel);
/* 373 */     this.activity.setOutputFile(activityLabel);
/*     */ 
/* 375 */     ActivityInfo info = getActivityInfo();
/* 376 */     this.messageText.setText("Sending Activity...");
/* 377 */     this.messageText.setBackground(this.shell.getDisplay().getSystemColor(
/* 378 */       22));
/* 379 */     this.messageText.redraw();
/*     */     try
/*     */     {
/* 382 */       StorageUtils.storeProduct(info, this.activity, true);
/*     */     } catch (PgenStorageException e) {
/* 384 */       e.printStackTrace();
/* 385 */       this.messageText.setText(e.getMessage());
/* 386 */       this.messageText.setBackground(this.shell.getDisplay().getSystemColor(
/* 387 */         3));
/* 388 */       if (e.getMessage().contains("\n")) {
/* 389 */         this.messageText.setSize(-1, 
/* 390 */           5 * this.messageText.getLineHeight());
/* 391 */         this.messageText.getShell().pack();
/* 392 */         this.messageText.getShell().layout();
/*     */       }
/* 394 */       return;
/*     */     }
/*     */ 
/* 397 */     close();
/* 398 */     PgenFileNameDisplay.getInstance().setFileName(activityLabel);
/* 399 */     PgenUtil.setSelectingMode();
/* 400 */     ((PgenResourceData)this.rsc.getResourceData()).setNeedsSaving(false);
/*     */   }
/*     */ 
/*     */   private ActivityInfo getActivityInfo()
/*     */   {
/* 405 */     ActivityInfo info = new ActivityInfo();
/* 406 */     info.setActivityLabel(this.infoText.getText());
/* 407 */     info.setActivityName(this.nameText.getText());
/* 408 */     info.setActivityType(this.typeText.getText());
/* 409 */     info.setActivitySubtype(this.subtypeText.getText());
/* 410 */     info.setSite(this.siteText.getText());
/* 411 */     info.setDesk(this.deskText.getText());
/* 412 */     info.setForecaster(this.forecasterText.getText());
/* 413 */     info.setMode(this.modeText.getText());
/* 414 */     info.setStatus(this.statusText.getText());
/*     */ 
/* 416 */     Calendar refTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/* 417 */     refTime.set(this.validDate.getYear(), this.validDate.getMonth(), 
/* 418 */       this.validDate.getDay(), this.validTime.getHours(), 
/* 419 */       this.validTime.getMinutes(), 0);
/* 420 */     refTime.set(14, 0);
/*     */ 
/* 422 */     info.setRefTime(refTime);
/*     */ 
/* 424 */     return info;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.controls.StoreActivityDialog
 * JD-Core Version:    0.6.2
 */