/*     */ package gov.noaa.nws.ncep.ui.pgen.layering;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResourceData;
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
/*     */ public class PgenLayeringDialog extends Dialog
/*     */ {
/*  49 */   protected final Boolean returnValue = Boolean.valueOf(false);
/*     */ 
/*  54 */   protected Shell shell = null;
/*     */ 
/*  56 */   protected Display display = null;
/*     */ 
/*  61 */   protected PgenResource drawingLayer = null;
/*  62 */   protected Product currentProduct = null;
/*  63 */   protected Layer currentLayer = null;
/*     */   protected Point shellLocation;
/*     */ 
/*     */   public PgenLayeringDialog(Shell parentShell)
/*     */   {
/*  72 */     super(parentShell);
/*     */   }
/*     */ 
/*     */   public Object open()
/*     */   {
/*  86 */     this.drawingLayer = PgenSession.getInstance().getPgenResource();
/*  87 */     this.currentProduct = this.drawingLayer.getActiveProduct();
/*  88 */     this.currentLayer = this.drawingLayer.getActiveLayer();
/*     */ 
/*  91 */     PgenSession.getInstance().disableUndoRedo();
/*     */ 
/*  94 */     Shell parent = getParent();
/*  95 */     this.shell = new Shell(parent, 2144);
/*     */ 
/*  98 */     setTitle();
/*     */ 
/* 101 */     setLayout();
/*     */ 
/* 104 */     setDefaultLocation(parent);
/*     */ 
/* 107 */     initializeComponents();
/*     */ 
/* 113 */     Listener[] closeListeners = this.shell.getListeners(21);
/* 114 */     if ((closeListeners != null) && (closeListeners.length > 0)) {
/* 115 */       for (Listener ls : closeListeners) {
/* 116 */         this.shell.removeListener(21, ls);
/*     */       }
/*     */     }
/*     */ 
/* 120 */     this.shell.addListener(21, new shellCloseListener(null));
/*     */ 
/* 123 */     this.shell.pack();
/* 124 */     this.shell.open();
/*     */ 
/* 130 */     popupSecondDialog();
/*     */ 
/* 133 */     this.display = parent.getDisplay();
/* 134 */     while (!this.shell.isDisposed()) {
/* 135 */       if (!this.display.readAndDispatch()) {
/* 136 */         this.display.sleep();
/*     */       }
/*     */     }
/*     */ 
/* 140 */     return this.returnValue;
/*     */   }
/*     */ 
/*     */   public void setTitle()
/*     */   {
/* 147 */     this.shell.setText("");
/*     */   }
/*     */ 
/*     */   public void setLayout()
/*     */   {
/* 155 */     GridLayout mainLayout = new GridLayout(1, true);
/*     */ 
/* 157 */     mainLayout.marginHeight = 1;
/* 158 */     mainLayout.marginWidth = 1;
/* 159 */     mainLayout.verticalSpacing = 2;
/* 160 */     mainLayout.horizontalSpacing = 1;
/*     */ 
/* 162 */     this.shell.setLayout(mainLayout);
/*     */   }
/*     */ 
/*     */   public void setDefaultLocation(Shell parent)
/*     */   {
/* 170 */     if (this.shellLocation == null) {
/* 171 */       Point pt = parent.getLocation();
/* 172 */       this.shell.setLocation(pt.x, pt.y);
/*     */     } else {
/* 174 */       this.shell.setLocation(this.shellLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initializeComponents(boolean compact)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/* 188 */     initializeComponents(false);
/*     */   }
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 196 */     return (this.shell != null) && (!this.shell.isDisposed());
/*     */   }
/*     */ 
/*     */   protected void popupSecondDialog()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setButtonColor(Button btn, java.awt.Color clr)
/*     */   {
/* 212 */     btn.setBackground(new org.eclipse.swt.graphics.Color(this.display, 
/* 213 */       clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 222 */     if ((this.shell != null) && (!this.shell.isDisposed())) {
/* 223 */       Rectangle bounds = this.shell.getBounds();
/* 224 */       this.shellLocation = new Point(bounds.x, bounds.y);
/* 225 */       this.shell.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void exit()
/*     */   {
/* 246 */     close();
/*     */   }
/*     */ 
/*     */   protected boolean needSaving()
/*     */   {
/* 253 */     return ((PgenResourceData)PgenSession.getInstance().getPgenResource().getResourceData()).isNeedsSaving();
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
/* 234 */       switch (e.type) {
/*     */       case 21:
/* 236 */         PgenLayeringDialog.this.exit();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.layering.PgenLayeringDialog
 * JD-Core Version:    0.6.2
 */