/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrDlg;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.VaaInfo;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.List;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class AshCloudInfoSourceDlg extends AttrDlg
/*     */ {
/*     */   private static AshCloudInfoSourceDlg INSTANCE;
/*  51 */   protected Composite top = null;
/*     */ 
/*  56 */   private Label lblHint = null;
/*     */   private static final String SELECT_HINTS = "Single mouse click:\t  select one row;\nSingle click+Shift: \t  continuous rows;\nSingle click+Ctrl : \t  separate rows;";
/*  68 */   private List listInfoSour = null;
/*     */ 
/*     */   public AshCloudInfoSourceDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  76 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static AshCloudInfoSourceDlg getInstance(Shell parShell)
/*     */   {
/*  86 */     if (INSTANCE == null) {
/*     */       try {
/*  88 */         INSTANCE = new AshCloudInfoSourceDlg(parShell);
/*     */       } catch (VizException e) {
/*  90 */         e.printStackTrace();
/*     */       }
/*     */     }
/*  93 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/*  99 */     return new HashMap();
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute ia)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 113 */     VolcanoVaaAttrDlg.getInstance(getParentShell()).setInfoSource(this.listInfoSour.getSelection());
/* 114 */     cancelPressed();
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 122 */     setReturnCode(1);
/* 123 */     close();
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/* 132 */     createButton(parent, 0, "OK", true);
/* 133 */     createButton(parent, 1, "Cancel", true);
/*     */ 
/* 135 */     getButton(0).setEnabled(true);
/* 136 */     getButton(1).setEnabled(true);
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 147 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 149 */     this.lblHint = new Label(this.top, 16384);
/* 150 */     this.lblHint.setText("Single mouse click:\t  select one row;\nSingle click+Shift: \t  continuous rows;\nSingle click+Ctrl : \t  separate rows;");
/*     */ 
/* 152 */     this.listInfoSour = new List(this.top, 2562);
/* 153 */     GridData gData = new GridData(256, 512);
/* 154 */     gData.horizontalSpan = 7;
/* 155 */     this.listInfoSour.setLayoutData(gData);
/* 156 */     this.listInfoSour.setItems(getInfoSourItems());
/*     */ 
/* 159 */     return this.top;
/*     */   }
/*     */ 
/*     */   public static String[] getInfoSourItems()
/*     */   {
/* 171 */     String[] s = (String[])VaaInfo.VAA_INFO_SINGLE_MAP.get("information-source");
/*     */ 
/* 173 */     return s;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.AshCloudInfoSourceDlg
 * JD-Core Version:    0.6.2
 */