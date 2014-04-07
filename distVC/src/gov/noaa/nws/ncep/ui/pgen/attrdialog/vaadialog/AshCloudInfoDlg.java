/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class AshCloudInfoDlg extends AttrDlg
/*     */ {
/*     */   private static AshCloudInfoDlg INSTANCE;
/*  53 */   protected Composite top = null;
/*     */ 
/*  58 */   private Text txtObs = null;
/*     */ 
/*  63 */   private Text txtFcst6 = null;
/*     */ 
/*  68 */   private Text txtFcst12 = null;
/*     */ 
/*  73 */   private Text txtFcst18 = null;
/*     */ 
/*  78 */   private String date = null;
/*     */ 
/*  83 */   private String time = null;
/*     */ 
/*  88 */   private static VolcanoVaaAttrDlg vaaAttrDlg = null;
/*     */ 
/*     */   public AshCloudInfoDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  96 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static AshCloudInfoDlg getInstance(Shell parShell)
/*     */   {
/* 105 */     if (INSTANCE == null) {
/*     */       try {
/* 107 */         INSTANCE = new AshCloudInfoDlg(parShell);
/*     */       } catch (VizException e) {
/* 109 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 112 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 119 */     return new HashMap();
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute ia)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void setFcstInfo()
/*     */   {
/* 133 */     vaaAttrDlg.setVacInfoFhr6(this.txtFcst6.getText().trim());
/* 134 */     vaaAttrDlg.setVacInfoFhr12(this.txtFcst12.getText().trim());
/* 135 */     vaaAttrDlg.setVacInfoFhr18(this.txtFcst18.getText().trim());
/*     */   }
/*     */ 
/*     */   private void reset()
/*     */   {
/* 142 */     this.txtObs.setText("");
/* 143 */     this.txtFcst6.setText("");
/* 144 */     this.txtFcst12.setText("");
/* 145 */     this.txtFcst18.setText("");
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 153 */     setFcstInfo();
/* 154 */     reset();
/* 155 */     setReturnCode(1);
/* 156 */     close();
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 166 */     createButton(parent, 1, "Close", true);
/* 167 */     getButton(1).setEnabled(true);
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 178 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 180 */     getShell().setText("VAA Ash Cloud Info");
/*     */ 
/* 182 */     GridLayout mainLayout = new GridLayout(1, false);
/* 183 */     mainLayout.marginHeight = 3;
/* 184 */     mainLayout.marginWidth = 3;
/* 185 */     this.top.setLayout(mainLayout);
/*     */ 
/* 187 */     Label lblObs = new Label(this.top, 16384);
/* 188 */     lblObs.setText("Obs Ash Cloud:");
/*     */ 
/* 190 */     this.txtObs = new Text(this.top, 2114);
/* 191 */     this.txtObs.setLayoutData(new GridData(400, 64));
/* 192 */     this.txtObs.setEditable(false);
/*     */ 
/* 194 */     Label lblFcst6 = new Label(this.top, 16384);
/* 195 */     lblFcst6.setText("Fcst Ash Cloud +06hr:");
/*     */ 
/* 197 */     this.txtFcst6 = new Text(this.top, 2114);
/* 198 */     this.txtFcst6.setLayoutData(new GridData(400, 64));
/*     */ 
/* 200 */     Label lblFcst12 = new Label(this.top, 16384);
/* 201 */     lblFcst12.setText("Fcst Ash Cloud +12hr:");
/*     */ 
/* 203 */     this.txtFcst12 = new Text(this.top, 2114);
/* 204 */     this.txtFcst12.setLayoutData(new GridData(400, 64));
/*     */ 
/* 206 */     Label lblFcst18 = new Label(this.top, 16384);
/* 207 */     lblFcst18.setText("Fcst Ash Cloud +18hr:");
/*     */ 
/* 209 */     this.txtFcst18 = new Text(this.top, 2114);
/* 210 */     this.txtFcst18.setLayoutData(new GridData(400, 64));
/*     */ 
/* 212 */     String[] fhrDT = VaaInfo.getFhrTimes(this.date, this.time);
/* 213 */     String txt00 = VaaInfo.getAshCloudInfo("00");
/* 214 */     this.txtObs.setText(txt00);
/* 215 */     this.txtFcst6.setText(fhrDT[0] + "  " + (isNotSeen(txt00) ? "" : VaaInfo.getAshCloudInfo(VaaInfo.LAYERS[2])));
/* 216 */     this.txtFcst12.setText(fhrDT[1] + "  " + (isNotSeen(txt00) ? "" : VaaInfo.getAshCloudInfo(VaaInfo.LAYERS[3])));
/* 217 */     this.txtFcst18.setText(fhrDT[2] + "  " + (isNotSeen(txt00) ? "" : VaaInfo.getAshCloudInfo(VaaInfo.LAYERS[4])));
/*     */ 
/* 219 */     return this.top;
/*     */   }
/*     */ 
/*     */   public void setDateTimeForDlg(String date, String time)
/*     */   {
/* 228 */     this.date = date;
/* 229 */     this.time = time;
/*     */   }
/*     */ 
/*     */   public void setVaaAttrDlg(VolcanoVaaAttrDlg vaDlg)
/*     */   {
/* 237 */     vaaAttrDlg = vaDlg;
/*     */   }
/*     */ 
/*     */   private boolean isNotSeen(String txt00)
/*     */   {
/* 246 */     if (txt00 == null) {
/* 247 */       return false;
/*     */     }
/* 249 */     return txt00.contains("VA NOT IDENTIFIABLE FROM SATELLITE DATA");
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.AshCloudInfoDlg
 * JD-Core Version:    0.6.2
 */