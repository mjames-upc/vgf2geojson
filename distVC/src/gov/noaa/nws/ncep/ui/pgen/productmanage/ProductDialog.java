/*     */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.palette.PgenPaletteWindow;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenActions;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenClass;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenControls;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenObjects;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResourceData;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Dialog;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class ProductDialog extends Dialog
/*     */ {
/*  58 */   protected final Boolean returnValue = Boolean.valueOf(false);
/*     */ 
/*  63 */   protected Shell shell = null;
/*     */ 
/*  65 */   protected Display display = null;
/*     */ 
/*  70 */   protected PgenResource drawingLayer = null;
/*  71 */   protected Product currentProduct = null;
/*  72 */   protected Layer currentLayer = null;
/*     */   protected Point shellLocation;
/*     */ 
/*     */   public ProductDialog(Shell parentShell)
/*     */   {
/*  81 */     super(parentShell);
/*     */   }
/*     */ 
/*     */   public Object open()
/*     */   {
/*  93 */     this.drawingLayer = PgenSession.getInstance().getPgenResource();
/*  94 */     this.currentProduct = this.drawingLayer.getActiveProduct();
/*  95 */     this.currentLayer = this.drawingLayer.getActiveLayer();
/*     */ 
/*  98 */     PgenSession.getInstance().disableUndoRedo();
/*     */ 
/* 101 */     Shell parent = getParent();
/* 102 */     this.shell = createShell(parent);
/*     */ 
/* 105 */     setTitle();
/*     */ 
/* 108 */     setLayout();
/*     */ 
/* 111 */     setDefaultLocation(parent);
/*     */ 
/* 114 */     initializeComponents();
/*     */ 
/* 120 */     Listener[] closeListeners = this.shell.getListeners(21);
/* 121 */     if ((closeListeners != null) && (closeListeners.length > 0)) {
/* 122 */       for (Listener ls : closeListeners) {
/* 123 */         this.shell.removeListener(21, ls);
/*     */       }
/*     */     }
/*     */ 
/* 127 */     this.shell.addListener(21, new shellCloseListener(null));
/*     */ 
/* 130 */     this.shell.pack();
/* 131 */     this.shell.open();
/*     */ 
/* 137 */     popupSecondDialog();
/*     */ 
/* 140 */     this.display = parent.getDisplay();
/* 141 */     while (!this.shell.isDisposed()) {
/* 142 */       if (!this.display.readAndDispatch()) {
/* 143 */         this.display.sleep();
/*     */       }
/*     */     }
/*     */ 
/* 147 */     return this.returnValue;
/*     */   }
/*     */ 
/*     */   protected Shell createShell(Shell parent)
/*     */   {
/* 156 */     Shell newShell = new Shell(parent, 2144);
/* 157 */     return newShell;
/*     */   }
/*     */ 
/*     */   public void setTitle()
/*     */   {
/* 164 */     this.shell.setText("");
/*     */   }
/*     */ 
/*     */   public void setLayout()
/*     */   {
/* 172 */     GridLayout mainLayout = new GridLayout(1, true);
/*     */ 
/* 174 */     mainLayout.marginHeight = 1;
/* 175 */     mainLayout.marginWidth = 1;
/* 176 */     mainLayout.verticalSpacing = 2;
/* 177 */     mainLayout.horizontalSpacing = 1;
/*     */ 
/* 179 */     this.shell.setLayout(mainLayout);
/*     */   }
/*     */ 
/*     */   public void setDefaultLocation(Shell parent)
/*     */   {
/* 188 */     if (this.shellLocation == null) {
/* 189 */       Point pt = parent.getLocation();
/* 190 */       this.shell.setLocation(pt.x, pt.y);
/*     */     } else {
/* 192 */       this.shell.setLocation(this.shellLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 208 */     return (this.shell != null) && (!this.shell.isDisposed());
/*     */   }
/*     */ 
/*     */   protected void popupSecondDialog()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setButtonColor(Button btn, java.awt.Color clr)
/*     */   {
/* 224 */     btn.setBackground(new org.eclipse.swt.graphics.Color(this.display, 
/* 225 */       clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 235 */     if ((this.shell != null) && (!this.shell.isDisposed())) {
/* 236 */       Rectangle bounds = this.shell.getBounds();
/* 237 */       this.shellLocation = new Point(bounds.x, bounds.y);
/* 238 */       this.shell.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<String> getButtonList(ProductType ptyp)
/*     */   {
/* 249 */     List btnList = null;
/*     */ 
/* 251 */     if (ptyp != null)
/*     */     {
/* 253 */       btnList = new ArrayList();
/*     */ 
/* 255 */       if (ptyp.getPgenControls() != null) {
/* 256 */         btnList.addAll(ptyp.getPgenControls().getName());
/*     */       }
/*     */ 
/* 259 */       if (ptyp.getPgenActions() != null) {
/* 260 */         btnList.addAll(ptyp.getPgenActions().getName());
/*     */       }
/*     */ 
/* 263 */       for (PgenClass cls : ptyp.getPgenClass()) {
/* 264 */         btnList.add(cls.getName());
/*     */ 
/* 266 */         if ((cls != null) && (cls.getPgenObjects() != null)) {
/* 267 */           btnList.addAll(cls.getPgenObjects().getName());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 272 */     return btnList;
/*     */   }
/*     */ 
/*     */   public void refreshPgenPalette(ProductType ptyp)
/*     */   {
/* 279 */     if (PgenSession.getInstance().getPgenPalette() != null)
/* 280 */       PgenSession.getInstance().getPgenPalette().resetPalette(getButtonList(ptyp));
/*     */   }
/*     */ 
/*     */   protected void exit()
/*     */   {
/* 300 */     close();
/*     */   }
/*     */ 
/*     */   protected boolean needSaving()
/*     */   {
/* 307 */     return ((PgenResourceData)PgenSession.getInstance().getPgenResource().getResourceData()).isNeedsSaving();
/*     */   }
/*     */ 
/*     */   private class shellCloseListener
/*     */     implements Listener
/*     */   {
/*     */     private shellCloseListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void handleEvent(Event e)
/*     */     {
/* 288 */       switch (e.type) {
/*     */       case 21:
/* 290 */         ProductDialog.this.exit();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.ProductDialog
 * JD-Core Version:    0.6.2
 */