/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenInterpolationTool;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.events.VerifyEvent;
/*     */ import org.eclipse.swt.events.VerifyListener;
/*     */ import org.eclipse.swt.layout.FormAttachment;
/*     */ import org.eclipse.swt.layout.FormData;
/*     */ import org.eclipse.swt.layout.FormLayout;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Spinner;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenInterpDlg extends AttrDlg
/*     */ {
/*  60 */   static PgenInterpDlg INSTANCE = null;
/*     */ 
/*  62 */   PgenInterpolationTool interpolationTool = null;
/*     */   private static final int INTERP_ID = 8609;
/*     */   private static final String INTERP_LABEL = "Interpolate";
/*  67 */   private Composite top = null;
/*     */ 
/*  69 */   private Text startTimeText = null;
/*  70 */   private Text endTimeText = null;
/*  71 */   private Spinner interval = null;
/*     */ 
/*     */   private PgenInterpDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  80 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static PgenInterpDlg getInstance(Shell parShell)
/*     */   {
/*  93 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/*  97 */         INSTANCE = new PgenInterpDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/* 101 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 106 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 119 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 122 */     FormLayout mainLayout = new FormLayout();
/* 123 */     mainLayout.marginHeight = 3;
/* 124 */     mainLayout.marginWidth = 3;
/* 125 */     this.top.setLayout(mainLayout);
/*     */ 
/* 128 */     initializeComponents();
/*     */ 
/* 130 */     return this.top;
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 139 */     ((GridLayout)parent.getLayout()).verticalSpacing = 0;
/* 140 */     ((GridLayout)parent.getLayout()).marginHeight = 3;
/*     */ 
/* 142 */     createButton(parent, 8609, "Interpolate", true);
/* 143 */     getButton(8609).setEnabled(false);
/*     */ 
/* 145 */     getButton(8609).setLayoutData(new GridData(ctrlBtnWidth + 10, ctrlBtnHeight));
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 156 */     getShell().setText("Interpolation");
/*     */ 
/* 161 */     Label startTime = new Label(this.top, 0);
/* 162 */     startTime.setText("Starting Time:");
/* 163 */     FormData fd = new FormData();
/* 164 */     fd.left = new FormAttachment(0, 10);
/* 165 */     fd.top = new FormAttachment(0, 10);
/* 166 */     startTime.setLayoutData(fd);
/*     */ 
/* 171 */     this.startTimeText = new Text(this.top, 2052);
/* 172 */     fd = new FormData();
/* 173 */     fd.right = new FormAttachment(100, -10);
/* 174 */     fd.top = new FormAttachment(0, 10);
/* 175 */     fd.left = new FormAttachment(startTime, 50, 131072);
/* 176 */     this.startTimeText.setLayoutData(fd);
/*     */ 
/* 179 */     this.startTimeText.addVerifyListener(new DigitVerifyListener());
/*     */ 
/* 184 */     Label endTime = new Label(this.top, 0);
/* 185 */     endTime.setText("Ending Time:");
/* 186 */     fd = new FormData();
/* 187 */     fd.left = new FormAttachment(0, 10);
/* 188 */     endTime.setLayoutData(fd);
/*     */ 
/* 193 */     this.endTimeText = new Text(this.top, 2052);
/* 194 */     fd = new FormData();
/* 195 */     fd.right = new FormAttachment(100, -10);
/* 196 */     fd.top = new FormAttachment(this.startTimeText, 10);
/* 197 */     fd.left = new FormAttachment(this.startTimeText, 0, 16384);
/* 198 */     this.endTimeText.setLayoutData(fd);
/*     */ 
/* 201 */     this.endTimeText.addVerifyListener(new DigitVerifyListener());
/*     */ 
/* 204 */     fd = (FormData)endTime.getLayoutData();
/* 205 */     fd.top = new FormAttachment(this.endTimeText, 0, 128);
/*     */ 
/* 210 */     Label intervalLabel = new Label(this.top, 0);
/* 211 */     intervalLabel.setText("Interval (hrs):");
/* 212 */     fd = new FormData();
/* 213 */     fd.left = new FormAttachment(0, 10);
/* 214 */     fd.top = new FormAttachment(endTime, 10, 1024);
/* 215 */     intervalLabel.setLayoutData(fd);
/*     */ 
/* 220 */     this.interval = new Spinner(this.top, 2048);
/* 221 */     this.interval.setMinimum(1);
/* 222 */     this.interval.setMaximum(500);
/* 223 */     fd = new FormData();
/* 224 */     fd.right = new FormAttachment(100, -10);
/* 225 */     fd.top = new FormAttachment(this.endTimeText, 10);
/* 226 */     fd.left = new FormAttachment(this.endTimeText, 0, 16384);
/* 227 */     this.interval.setLayoutData(fd);
/*     */ 
/* 230 */     fd = (FormData)intervalLabel.getLayoutData();
/* 231 */     fd.top = new FormAttachment(this.interval, 0, 128);
/*     */   }
/*     */ 
/*     */   protected void buttonPressed(int buttonId)
/*     */   {
/* 243 */     if (8609 == buttonId)
/*     */     {
/* 245 */       if ((this.startTimeText.getText().isEmpty()) || (this.endTimeText.getText().isEmpty())) {
/* 246 */         String msg = "Please provide both a valid Start Time and End Time.";
/* 247 */         MessageDialog messageDlg = new MessageDialog(
/* 248 */           PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 249 */           "Warning", null, msg, 
/* 250 */           4, new String[] { "OK" }, 0);
/* 251 */         messageDlg.open();
/*     */       }
/*     */       else {
/* 254 */         this.interpolationTool.performInterpolation();
/* 255 */         enableStartTime();
/* 256 */         enableEndTime();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 267 */     HashMap attr = new HashMap();
/*     */ 
/* 269 */     return attr;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute attr)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getStartTime()
/*     */   {
/* 282 */     if (this.startTimeText.getText().isEmpty()) return 0;
/* 283 */     return Integer.parseInt(this.startTimeText.getText());
/*     */   }
/*     */ 
/*     */   public int getEndTime()
/*     */   {
/* 291 */     if (this.endTimeText.getText().isEmpty()) return 0;
/* 292 */     return Integer.parseInt(this.endTimeText.getText());
/*     */   }
/*     */ 
/*     */   public int getInterval()
/*     */   {
/* 300 */     return this.interval.getSelection();
/*     */   }
/*     */ 
/*     */   public void arm(PgenInterpolationTool tool)
/*     */   {
/* 308 */     this.interpolationTool = tool;
/* 309 */     getButton(8609).setEnabled(true);
/*     */   }
/*     */ 
/*     */   public void disarm()
/*     */   {
/* 316 */     this.interpolationTool = null;
/* 317 */     getButton(8609).setEnabled(false);
/*     */   }
/*     */ 
/*     */   public void setStartTime(String startTime)
/*     */   {
/* 348 */     if (startTime != null) {
/* 349 */       for (Listener ls : this.startTimeText.getListeners(25)) {
/* 350 */         this.startTimeText.removeListener(25, ls);
/*     */       }
/*     */ 
/* 353 */       this.startTimeText.setText(startTime);
/*     */ 
/* 355 */       this.startTimeText.addVerifyListener(new DigitVerifyListener());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setEndTime(String endTime)
/*     */   {
/* 363 */     if (endTime != null) {
/* 364 */       for (Listener ls : this.endTimeText.getListeners(25)) {
/* 365 */         this.endTimeText.removeListener(25, ls);
/*     */       }
/*     */ 
/* 368 */       this.endTimeText.setText(endTime);
/*     */ 
/* 370 */       this.endTimeText.addVerifyListener(new DigitVerifyListener());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void disableStartTime()
/*     */   {
/* 379 */     this.startTimeText.setEnabled(false);
/*     */   }
/*     */ 
/*     */   public void enableStartTime()
/*     */   {
/* 386 */     this.startTimeText.setEnabled(true);
/*     */   }
/*     */ 
/*     */   public void disableEndTime()
/*     */   {
/* 393 */     this.endTimeText.setEnabled(false);
/*     */   }
/*     */ 
/*     */   public void enableEndTime()
/*     */   {
/* 400 */     this.endTimeText.setEnabled(true);
/*     */   }
/*     */ 
/*     */   class DigitVerifyListener
/*     */     implements VerifyListener
/*     */   {
/*     */     DigitVerifyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void verifyText(VerifyEvent e)
/*     */     {
/* 331 */       char BACKSPACE = '\b';
/* 332 */       char DELETE = '';
/*     */ 
/* 334 */       if ((Character.isDigit(e.character)) || 
/* 335 */         (e.character == '\b') || (e.character == '')) { e.doit = true;
/*     */       } else {
/* 337 */         e.doit = false;
/* 338 */         Display.getCurrent().beep();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.PgenInterpDlg
 * JD-Core Version:    0.6.2
 */