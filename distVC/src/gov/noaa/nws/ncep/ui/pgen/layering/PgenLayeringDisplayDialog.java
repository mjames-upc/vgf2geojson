/*     */ package gov.noaa.nws.ncep.ui.pgen.layering;
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
/*     */ public class PgenLayeringDisplayDialog extends PgenLayeringDialog
/*     */ {
/*  48 */   private PgenLayeringControlDialog layeringDlg = null;
/*     */ 
/*  50 */   private Button colorBtn = null;
/*  51 */   private Button fillBtn = null;
/*  52 */   private ColorButtonSelector cs = null;
/*     */ 
/*     */   public PgenLayeringDisplayDialog(Shell parentShell, PgenLayeringControlDialog dlg)
/*     */   {
/*  59 */     super(parentShell);
/*     */ 
/*  61 */     this.layeringDlg = dlg;
/*     */   }
/*     */ 
/*     */   public void setTitle()
/*     */   {
/*  70 */     String title = this.layeringDlg.getColorModeLayerName();
/*     */ 
/*  72 */     if (title == null) {
/*  73 */       title = "Layer Display";
/*     */     }
/*     */ 
/*  76 */     this.shell.setText(title);
/*     */   }
/*     */ 
/*     */   public void setLayout()
/*     */   {
/*  85 */     GridLayout mainLayout = new GridLayout(1, true);
/*  86 */     mainLayout.marginHeight = 1;
/*  87 */     mainLayout.marginWidth = 1;
/*  88 */     this.shell.setLayout(mainLayout);
/*     */   }
/*     */ 
/*     */   public void setDefaultLocation(Shell parent)
/*     */   {
/*  97 */     Point pt = parent.getLocation();
/*  98 */     this.shell.setLocation(pt.x + 400, pt.y + 146);
/*     */   }
/*     */ 
/*     */   public void initializeComponents()
/*     */   {
/* 107 */     Group grp = new Group(this.shell, 0);
/* 108 */     grp.setText("Mono Color");
/* 109 */     grp.setLayout(new GridLayout(2, true));
/* 110 */     this.colorBtn = new Button(grp, 32);
/* 111 */     this.colorBtn.setSelection(this.layeringDlg.getLayerForColorMode().isMonoColor());
/* 112 */     this.cs = new ColorButtonSelector(grp);
/* 113 */     Color clr = this.layeringDlg.getLayerForColorMode().getColor();
/* 114 */     this.cs.setColorValue(new RGB(clr.getRed(), clr.getGreen(), clr.getBlue()));
/*     */ 
/* 118 */     Composite fillComp = new Composite(this.shell, 0);
/* 119 */     fillComp.setLayout(new GridLayout(1, true));
/*     */ 
/* 121 */     this.fillBtn = new Button(fillComp, 32);
/* 122 */     this.fillBtn.setText("Filled");
/* 123 */     this.fillBtn.setSelection(this.layeringDlg.getLayerForColorMode().isFilled());
/*     */ 
/* 125 */     Composite centeredComp = new Composite(this.shell, 0);
/* 126 */     GridLayout gl2 = new GridLayout(2, true);
/* 127 */     centeredComp.setLayout(gl2);
/* 128 */     GridData gd = new GridData(4, -1, true, false);
/* 129 */     centeredComp.setLayoutData(gd);
/*     */ 
/* 131 */     Button acceptBtn = new Button(centeredComp, 0);
/* 132 */     acceptBtn.setText("Accept");
/* 133 */     acceptBtn.setLayoutData(gd);
/* 134 */     acceptBtn.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 137 */         PgenLayeringDisplayDialog.this.layeringDlg.updateDisplayAttr(PgenLayeringDisplayDialog.this.colorBtn.getSelection(), 
/* 138 */           new Color(PgenLayeringDisplayDialog.this.cs.getColorValue().red, PgenLayeringDisplayDialog.this.cs.getColorValue().green, 
/* 139 */           PgenLayeringDisplayDialog.this.cs.getColorValue().blue, 
/* 140 */           PgenLayeringDisplayDialog.this.layeringDlg.getLayerForColorMode().getColor().getAlpha()), 
/* 141 */           PgenLayeringDisplayDialog.this.fillBtn.getSelection());
/*     */ 
/* 143 */         PgenLayeringDisplayDialog.this.shell.dispose();
/*     */       }
/*     */     });
/* 147 */     Button cancelBtn = new Button(centeredComp, 0);
/* 148 */     cancelBtn.setText("Cancel");
/* 149 */     cancelBtn.setLayoutData(gd);
/* 150 */     cancelBtn.addSelectionListener(new SelectionAdapter() {
/*     */       public void widgetSelected(SelectionEvent event) {
/* 152 */         PgenLayeringDisplayDialog.this.shell.dispose();
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.layering.PgenLayeringDisplayDialog
 * JD-Core Version:    0.6.2
 */