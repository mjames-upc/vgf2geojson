/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProdType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.PgenStorageException;
/*     */ import gov.noaa.nws.ncep.ui.pgen.store.StorageUtils;
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import org.eclipse.jface.dialogs.Dialog;
/*     */ import org.eclipse.swt.events.DisposeEvent;
/*     */ import org.eclipse.swt.events.DisposeListener;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.graphics.FontMetrics;
/*     */ import org.eclipse.swt.graphics.GC;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class PgenProd extends AbstractPgenTool
/*     */ {
/*     */   protected void activateTool()
/*     */   {
/*  76 */     super.activateTool();
/*     */ 
/*  78 */     ProdDialog dlg = new ProdDialog(null);
/*  79 */     dlg.open();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/* 390 */     return null;
/*     */   }
/*     */ 
/*     */   private class ProdDialog extends Dialog
/*     */   {
/*     */     private HashMap<Button, ProdType> typeMap;
/*     */ 
/*     */     private ProdDialog()
/*     */     {
/* 104 */       super();
/* 105 */       this.typeMap = new LinkedHashMap();
/*     */     }
/*     */ 
/*     */     public Control createDialogArea(Composite parent)
/*     */     {
/* 114 */       getShell().setText("Products");
/*     */ 
/* 116 */       Composite top = (Composite)super.createDialogArea(parent);
/*     */ 
/* 119 */       GridLayout mainLayout = new GridLayout(2, false);
/* 120 */       mainLayout.marginHeight = 3;
/* 121 */       mainLayout.marginWidth = 3;
/* 122 */       mainLayout.horizontalSpacing = 30;
/* 123 */       top.setLayout(mainLayout);
/*     */ 
/* 126 */       String typeName = PgenProd.this.drawingLayer.getActiveProduct().getType();
/* 127 */       ProductType curType = (ProductType)ProductConfigureDialog.getProductTypes().get(
/* 128 */         typeName);
/*     */ 
/* 131 */       if (curType != null)
/*     */       {
/* 133 */         for (ProdType pt : curType.getProdType()) {
/* 134 */           Button ptBtn = new Button(top, 32);
/*     */ 
/* 137 */           ptBtn.setText(pt.getName());
/* 138 */           ptBtn.setSelection(true);
/*     */ 
/* 140 */           this.typeMap.put(ptBtn, pt);
/*     */ 
/* 142 */           Label typeLbl = new Label(top, 0);
/* 143 */           typeLbl.setText(pt.getType() == null ? "" : pt.getType());
/*     */         }
/*     */       }
/*     */       else {
/* 147 */         Label nothing = new Label(top, 0);
/* 148 */         nothing.setText("No product type in current product.");
/*     */       }
/*     */ 
/* 151 */       return top;
/*     */     }
/*     */ 
/*     */     public void createButtonsForButtonBar(Composite parent)
/*     */     {
/* 160 */       super.createButtonsForButtonBar(parent);
/* 161 */       getButton(0).setText("Go");
/* 162 */       getButton(1).setText("Close");
/*     */     }
/*     */ 
/*     */     public int open()
/*     */     {
/* 172 */       if (getShell() == null) {
/* 173 */         create();
/*     */       }
/*     */ 
/* 176 */       getShell().setLocation(
/* 177 */         getShell().getParent().getLocation().x + 30, 
/* 178 */         getShell().getParent().getLocation().y);
/*     */ 
/* 180 */       return super.open();
/*     */     }
/*     */ 
/*     */     public boolean close()
/*     */     {
/* 187 */       PgenSession.getInstance().getPgenResource().deactivatePgenTools();
/* 188 */       return super.close();
/*     */     }
/*     */ 
/*     */     public void okPressed()
/*     */     {
/* 199 */       for (Button btn : this.typeMap.keySet())
/* 200 */         if (btn.getSelection())
/*     */         {
/* 202 */           PgenProd.ProdTextDlg msgDlg = new PgenProd.ProdTextDlg(PgenProd.this, 
/* 203 */             getShell(), (ProdType)this.typeMap.get(btn), null);
/*     */ 
/* 205 */           msgDlg.setBlockOnOpen(true);
/* 206 */           Product prod = PgenProd.this.drawingLayer.getActiveProduct();
/* 207 */           msgDlg.setProduct(prod);
/* 208 */           PgenProd.ProdTextDlg.access$1(msgDlg, ((ProdType)this.typeMap.get(btn)).generateProd(prod));
/*     */ 
/* 210 */           int rt = msgDlg.open();
/* 211 */           if (rt == 1)
/* 212 */             return;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ProdTextDlg extends Dialog
/*     */   {
/*     */     ProdType pt;
/*     */     Product prod;
/*     */     private Composite top;
/*     */     private Text messageBox;
/*     */     private String message;
/* 239 */     private final int NUM_LINES = 25;
/*     */ 
/* 241 */     private final int NUM_COLUMNS = 80;
/*     */ 
/*     */     private ProdTextDlg(Shell parentShell, ProdType pt)
/*     */     {
/* 247 */       super();
/* 248 */       setShellStyle(65632);
/* 249 */       this.pt = pt;
/* 250 */       this.message = "";
/*     */     }
/*     */ 
/*     */     public void setProduct(Product prod) {
/* 254 */       this.prod = prod;
/*     */     }
/*     */ 
/*     */     public Control createDialogArea(Composite parent)
/*     */     {
/* 264 */       getShell().setText(this.pt.getName());
/*     */ 
/* 266 */       this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 271 */       GridLayout mainLayout = new GridLayout(1, true);
/* 272 */       mainLayout.marginHeight = 3;
/* 273 */       mainLayout.marginWidth = 3;
/* 274 */       this.top.setLayout(mainLayout);
/*     */ 
/* 279 */       this.messageBox = new Text(this.top, 2826);
/*     */ 
/* 281 */       this.messageBox.setFont(new Font(this.messageBox.getDisplay(), "Courier", 12, 
/* 282 */         0));
/* 283 */       GridData gd = new GridData(768);
/*     */ 
/* 287 */       gd.heightHint = (25 * this.messageBox.getLineHeight());
/* 288 */       GC gc = new GC(this.messageBox);
/* 289 */       FontMetrics fm = gc.getFontMetrics();
/* 290 */       gd.widthHint = (80 * fm.getAverageCharWidth());
/*     */ 
/* 292 */       this.messageBox.setLayoutData(gd);
/* 293 */       this.messageBox.setText(this.message);
/*     */ 
/* 296 */       this.messageBox.addDisposeListener(new DisposeListener()
/*     */       {
/*     */         public void widgetDisposed(DisposeEvent e)
/*     */         {
/* 300 */           Text w = (Text)e.widget;
/* 301 */           w.getFont().dispose();
/*     */         }
/*     */       });
/* 306 */       return this.top;
/*     */     }
/*     */ 
/*     */     public int open()
/*     */     {
/* 315 */       if (getShell() == null) {
/* 316 */         create();
/*     */       }
/*     */ 
/* 319 */       getShell().setLocation(
/* 320 */         getShell().getParent().getLocation().x + 300, 
/* 321 */         getShell().getParent().getLocation().y);
/*     */ 
/* 323 */       return super.open();
/*     */     }
/*     */ 
/*     */     public void okPressed()
/*     */     {
/* 330 */       if ((this.pt.getOutputFile() != null) && (!this.pt.getOutputFile().isEmpty()))
/*     */       {
/* 332 */         String pd = 
/* 333 */           ((ProductType)ProductConfigureDialog.getProductTypes()
/* 333 */           .get(PgenProd.this.drawingLayer.getActiveProduct().getType()))
/* 334 */           .getType();
/* 335 */         String pd1 = pd.replaceAll(" ", "_");
/*     */ 
/* 337 */         String dirPath = PgenUtil.getPgenOprDirectory() + 
/* 338 */           File.separator + pd1 + File.separator + "prod" + 
/* 339 */           File.separator + this.pt.getType().replaceAll(" ", "_");
/*     */ 
/* 341 */         String filePath = dirPath + File.separator + this.pt.getOutputFile();
/* 342 */         String name = this.pt.getOutputFile();
/*     */ 
/* 344 */         String dataURI = null;
/*     */         try {
/* 346 */           dataURI = StorageUtils.storeProduct(this.prod);
/*     */         } catch (PgenStorageException e) {
/* 348 */           StorageUtils.showError(e);
/*     */         }
/*     */ 
/* 351 */         if (dataURI != null) {
/*     */           try {
/* 353 */             StorageUtils.storeDerivedProduct(dataURI, name, 
/* 354 */               this.pt.getType(), this.message, true);
/*     */           } catch (PgenStorageException e) {
/* 356 */             StorageUtils.showError(e);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 384 */       super.okPressed();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenProd
 * JD-Core Version:    0.6.2
 */