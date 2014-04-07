/*     */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.viz.common.ui.color.ColorButtonSelector;
/*     */ import java.awt.Color;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.RGB;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class LayeringDisplayDialog extends ProductDialog
/*     */ {
/*  48 */   private ProductManageDialog layeringDlg = null;
/*     */ 
/*  50 */   private Button colorBtn = null;
/*  51 */   private Button fillBtn = null;
/*  52 */   private ColorButtonSelector cs = null;
/*     */ 
/*     */   public LayeringDisplayDialog(Shell parentShell, ProductManageDialog dlg)
/*     */   {
/*  60 */     super(parentShell);
/*     */ 
/*  62 */     this.layeringDlg = dlg;
/*     */   }
/*     */ 
/*     */   public void setTitle()
/*     */   {
/*  71 */     String title = this.layeringDlg.getColorModeLayerName();
/*     */ 
/*  73 */     if (title == null) {
/*  74 */       title = "Layer Display";
/*     */     }
/*     */ 
/*  77 */     this.shell.setText(title);
/*     */   }
/*     */ 
/*     */   public void setLayout()
/*     */   {
/*  86 */     GridLayout mainLayout = new GridLayout(1, true);
/*  87 */     mainLayout.marginHeight = 1;
/*  88 */     mainLayout.marginWidth = 1;
/*  89 */     this.shell.setLayout(mainLayout);
/*     */   }
/*     */ 
/*     */   public void setDefaultLocation(Shell parent)
/*     */   {
/*  99 */     if (this.shellLocation == null) {
/* 100 */       Point pt = parent.getLocation();
/* 101 */       this.shell.setLocation(pt.x + 400, pt.y + 146);
/*     */     } else {
/* 103 */       this.shell.setLocation(this.shellLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/* 114 */     Group grp = new Group(this.shell, 0);
/* 115 */     grp.setText("Mono Color");
/* 116 */     grp.setLayout(new GridLayout(2, true));
/* 117 */     this.colorBtn = new Button(grp, 32);
/* 118 */     this.colorBtn.setSelection(this.layeringDlg.getLayerForColorMode().isMonoColor());
/*     */ 
/* 120 */     this.cs = new ColorButtonSelector(grp);
/* 121 */     Color clr = this.layeringDlg.getLayerForColorMode().getColor();
/* 122 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */ 
/* 124 */     Composite fillComp = new Composite(this.shell, 0);
/* 125 */     fillComp.setLayout(new GridLayout(1, true));
/*     */ 
/* 127 */     this.fillBtn = new Button(fillComp, 32);
/* 128 */     this.fillBtn.setText("Filled");
/* 129 */     this.fillBtn.setSelection(this.layeringDlg.getLayerForColorMode().isFilled());
/*     */ 
/* 131 */     Composite centeredComp = new Composite(this.shell, 0);
/* 132 */     GridLayout gl2 = new GridLayout(2, true);
/* 133 */     centeredComp.setLayout(gl2);
/* 134 */     GridData gd = new GridData(4, -1, true, false);
/* 135 */     centeredComp.setLayoutData(gd);
/*     */ 
/* 137 */     Button acceptBtn = new Button(centeredComp, 0);
/* 138 */     acceptBtn.setText("Accept");
/* 139 */     acceptBtn.setLayoutData(gd);
/* 140 */     acceptBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 143 */         LayeringDisplayDialog.this.layeringDlg.updateDisplayAttr(LayeringDisplayDialog.this.colorBtn.getSelection(), 
/* 144 */           new Color(LayeringDisplayDialog.this.cs.getColorValue().red, LayeringDisplayDialog.this.cs.getColorValue().green, 
/* 145 */           LayeringDisplayDialog.this.cs.getColorValue().blue, 
/* 146 */           LayeringDisplayDialog.this.layeringDlg.getLayerForColorMode().getColor().getAlpha()), 
/* 147 */           LayeringDisplayDialog.this.fillBtn.getSelection());
/* 148 */         LayeringDisplayDialog.this.close();
/*     */       }
/*     */     });
/* 152 */     Button cancelBtn = new Button(centeredComp, 0);
/* 153 */     cancelBtn.setText("Cancel");
/* 154 */     cancelBtn.setLayoutData(gd);
/* 155 */     cancelBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 157 */         LayeringDisplayDialog.this.close();
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.LayeringDisplayDialog
 * JD-Core Version:    0.6.2
 */