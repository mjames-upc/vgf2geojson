/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.DrawableElement;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.GfaFormat;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.SelectionListener;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class FromAttrDlg extends AttrDlg
/*     */ {
/*     */   private static FromAttrDlg instance;
/*     */   private Composite top;
/*  57 */   private boolean formatByTag = false;
/*     */ 
/*     */   private FromAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  67 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static FromAttrDlg getInstance(Shell parShell)
/*     */   {
/*  80 */     if (instance == null) {
/*     */       try {
/*  82 */         instance = new FromAttrDlg(parShell);
/*     */       } catch (VizException e) {
/*  84 */         e.printStackTrace();
/*     */       }
/*     */     }
/*  87 */     return instance;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 100 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 103 */     GridLayout layout = new GridLayout();
/* 104 */     layout.numColumns = 1;
/* 105 */     this.top.setLayout(layout);
/*     */ 
/* 108 */     initializeComponents();
/*     */ 
/* 110 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 120 */     getShell().setText("Generate FROM");
/*     */ 
/* 122 */     createButton("Format All", new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 124 */         FromAttrDlg.this.formatAllPressed();
/*     */       }
/*     */     });
/* 128 */     createButton("Format Layer", new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 130 */         FromAttrDlg.this.formatLayerPressed();
/*     */       }
/*     */     });
/* 134 */     createButton("Format Tag", new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 136 */         FromAttrDlg.this.formatTagPressed();
/*     */       }
/*     */     });
/* 140 */     createButton("Cancel", new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 142 */         FromAttrDlg.this.formatByTag = false;
/* 143 */         FromAttrDlg.this.cancelPressed();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private Button createButton(String label, SelectionListener listener) {
/* 149 */     Button btn = new Button(this.top, 8);
/*     */ 
/* 151 */     GridData gridData = new GridData();
/* 152 */     gridData.horizontalAlignment = 4;
/* 153 */     gridData.grabExcessHorizontalSpace = true;
/* 154 */     btn.setLayoutData(gridData);
/* 155 */     btn.setText(label);
/* 156 */     btn.addSelectionListener(listener);
/* 157 */     return btn;
/*     */   }
/*     */ 
/*     */   public Control createButtonBar(Composite parent)
/*     */   {
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */   public void createButtonsForButtonBar(Composite parent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 176 */     if (getShell() == null) {
/* 177 */       create();
/*     */     }
/*     */ 
/* 180 */     if (this.shellLocation == null) {
/* 181 */       this.shellLocation = initialLocation();
/*     */     }
/*     */ 
/* 184 */     this.formatByTag = false;
/*     */ 
/* 186 */     return super.open();
/*     */   }
/*     */ 
/*     */   public Point initialLocation() {
/* 190 */     Rectangle parentSize = getParentShell().getBounds();
/* 191 */     Rectangle mySize = getShell().getBounds();
/*     */ 
/* 194 */     int locationX = mySize.width * 2 + parentSize.x;
/* 195 */     int locationY = (parentSize.height - mySize.height) / 2 + parentSize.y;
/*     */ 
/* 197 */     return new Point(locationX, locationY);
/*     */   }
/*     */ 
/*     */   public void cancelPressed()
/*     */   {
/* 202 */     super.cancelPressed();
/* 203 */     PgenUtil.setSelectingMode();
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 211 */     HashMap attr = new HashMap();
/*     */ 
/* 213 */     return attr;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute attr)
/*     */   {
/*     */   }
/*     */ 
/*     */   private boolean formatConfirmed(String msg)
/*     */   {
/* 227 */     StringBuilder allmsg = new StringBuilder("This will delete and regenerate ALL AIRMETs and OUTLOOKs");
/* 228 */     if (msg != null) {
/* 229 */       allmsg = allmsg.append(msg);
/*     */     }
/* 231 */     allmsg.append(".\nOk to continue?");
/*     */ 
/* 233 */     MessageDialog confirmDlg = new MessageDialog(
/* 234 */       PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 235 */       "Confirm", null, allmsg.toString(), 
/* 236 */       3, new String[] { "OK", "Cancel" }, 0);
/* 237 */     confirmDlg.open();
/*     */ 
/* 239 */     return confirmDlg.getReturnCode() == 0;
/*     */   }
/*     */ 
/*     */   private void formatAllPressed()
/*     */   {
/* 247 */     this.formatByTag = false;
/* 248 */     if (formatConfirmed(null))
/*     */     {
/* 250 */       GfaFormat format = new GfaFormat(this.drawingLayer);
/*     */ 
/* 252 */       format.formatAllPressed();
/*     */ 
/* 254 */       refreshSelect();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void formatLayerPressed()
/*     */   {
/* 263 */     this.formatByTag = false;
/*     */ 
/* 265 */     String msg = null;
/* 266 */     if ((this.drawingLayer != null) && (this.drawingLayer.getProductManageDlg() != null)) {
/* 267 */       msg = " for layer - " + this.drawingLayer.getActiveLayer().getName();
/*     */     }
/*     */ 
/* 270 */     if (formatConfirmed(msg)) {
/* 271 */       GfaFormat format = new GfaFormat(this.drawingLayer);
/*     */ 
/* 273 */       format.formatLayerPressed();
/*     */ 
/* 275 */       refreshSelect();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void formatTagPressed()
/*     */   {
/* 284 */     this.formatByTag = true;
/*     */ 
/* 286 */     String msg = null;
/* 287 */     if (this.drawingLayer != null) {
/* 288 */       DrawableElement de = this.drawingLayer.getSelectedDE();
/* 289 */       if ((de instanceof Gfa)) {
/* 290 */         msg = " \nwith hazard type " + ((Gfa)de).getGfaHazard() + 
/* 291 */           " and tag " + ((Gfa)de).getGfaTag() + ((Gfa)de).getGfaDesk();
/*     */ 
/* 293 */         if (this.mapEditor != null) {
/* 294 */           this.mapEditor.refresh();
/*     */         }
/*     */ 
/* 297 */         if (formatConfirmed(msg))
/*     */         {
/* 299 */           GfaFormat format = new GfaFormat(this.drawingLayer);
/*     */ 
/* 301 */           format.formatTagPressed();
/*     */         }
/*     */ 
/* 304 */         this.drawingLayer.removeSelected();
/*     */ 
/* 306 */         if (this.mapEditor != null)
/* 307 */           this.mapEditor.refresh();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void refreshSelect()
/*     */   {
/* 315 */     if (this.mapEditor != null) {
/* 316 */       this.mapEditor.refresh();
/*     */     }
/* 318 */     close();
/* 319 */     PgenUtil.setSelectingMode();
/*     */   }
/*     */ 
/*     */   public boolean isFormatByTag()
/*     */   {
/* 329 */     return this.formatByTag;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.FromAttrDlg
 * JD-Core Version:    0.6.2
 */