/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.IAttribute;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.gfa.Gfa;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Group;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ 
/*     */ public class CycleDlg extends AttrDlg
/*     */ {
/*     */   static CycleDlg instance;
/*     */   private Composite top;
/*     */   private Combo dayCbo;
/*     */   private Combo cycleCbo;
/*     */   private Button routineBtn;
/*     */   private Button updateBtn;
/*     */ 
/*     */   private CycleDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  70 */     super(parShell);
/*     */   }
/*     */ 
/*     */   public static CycleDlg getInstance(Shell parShell)
/*     */   {
/*  82 */     if (instance == null) {
/*     */       try {
/*  84 */         instance = new CycleDlg(parShell);
/*     */       } catch (VizException e) {
/*  86 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/*  90 */     return instance;
/*     */   }
/*     */ 
/*     */   public Control createDialogArea(Composite parent)
/*     */   {
/* 103 */     this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 106 */     GridLayout mainLayout = new GridLayout(5, false);
/* 107 */     mainLayout.marginHeight = 3;
/* 108 */     mainLayout.marginWidth = 3;
/* 109 */     this.top.setLayout(mainLayout);
/*     */ 
/* 112 */     initializeComponents();
/*     */ 
/* 114 */     return this.top;
/*     */   }
/*     */ 
/*     */   private void initializeComponents()
/*     */   {
/* 124 */     getShell().setText("Cycle Selection");
/*     */ 
/* 126 */     Label dayLbl = new Label(this.top, 16384);
/* 127 */     dayLbl.setText("Day:");
/*     */ 
/* 129 */     this.dayCbo = new Combo(this.top, 12);
/* 130 */     for (int i = 1; i <= 31; i++) {
/* 131 */       this.dayCbo.add(i);
/*     */     }
/* 133 */     int day = PgenCycleTool.getCycleDay();
/* 134 */     int index = this.dayCbo.indexOf(day);
/* 135 */     this.dayCbo.select(index);
/*     */ 
/* 137 */     Label cycleLbl = new Label(this.top, 16384);
/* 138 */     cycleLbl.setText("Cycle:");
/*     */ 
/* 140 */     this.cycleCbo = new Combo(this.top, 12);
/* 141 */     int[] cycles = PgenCycleTool.getCyclesArray();
/* 142 */     for (int i = 0; i < cycles.length; i++) {
/* 143 */       this.cycleCbo.add(cycles[i]);
/*     */     }
/* 145 */     int h = PgenCycleTool.getCycleHour();
/* 146 */     index = this.cycleCbo.indexOf(h);
/* 147 */     this.cycleCbo.select(index);
/*     */ 
/* 149 */     Group group = new Group(this.top, 0);
/* 150 */     GridData gridData = new GridData(1);
/* 151 */     gridData.horizontalAlignment = 4;
/* 152 */     group.setLayoutData(gridData);
/* 153 */     GridLayout layout = new GridLayout(1, false);
/* 154 */     layout.marginHeight = 3;
/* 155 */     layout.marginWidth = 3;
/* 156 */     group.setLayout(layout);
/* 157 */     this.routineBtn = new Button(group, 16);
/* 158 */     this.routineBtn.setSelection(PgenCycleTool.isRoutine());
/* 159 */     this.routineBtn.setText("Routine");
/* 160 */     this.updateBtn = new Button(group, 16);
/* 161 */     this.updateBtn.setSelection(!PgenCycleTool.isRoutine());
/* 162 */     this.updateBtn.setText("Update");
/* 163 */     addSeparator(this.top.getParent());
/*     */   }
/*     */ 
/*     */   public void okPressed()
/*     */   {
/* 170 */     String cycle = this.cycleCbo.getText();
/* 171 */     if (cycle.startsWith("0"))
/* 172 */       cycle = cycle.substring(1, cycle.length());
/* 173 */     PgenCycleTool.setCycleHour(Integer.parseInt(cycle));
/*     */ 
/* 175 */     String day = this.dayCbo.getText();
/* 176 */     if (day.startsWith("0"))
/* 177 */       day = day.substring(1, day.length());
/* 178 */     PgenCycleTool.setCycleDay(Integer.parseInt(day));
/*     */ 
/* 180 */     PgenCycleTool.setCycleRoutine(this.routineBtn.getSelection());
/*     */ 
/* 187 */     if (this.drawingLayer != null)
/*     */     {
/* 193 */       for (AbstractDrawableComponent adc : this.drawingLayer.getActiveLayer().getDrawables()) {
/* 194 */         if ((adc instanceof Gfa))
/*     */         {
/* 196 */           this.drawingLayer.resetElement((Gfa)adc);
/*     */ 
/* 199 */           ((Gfa)adc).setGfaCycleDay(PgenCycleTool.getCycleDay());
/* 200 */           ((Gfa)adc).setGfaCycleHour(PgenCycleTool.getCycleHour());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 216 */     if (this.mapEditor != null) {
/* 217 */       this.mapEditor.refresh();
/*     */     }
/*     */ 
/* 220 */     close();
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 228 */     if (getShell() == null) {
/* 229 */       create();
/*     */     }
/* 231 */     if (this.shellLocation == null) {
/* 232 */       this.shellLocation = centerOfParent();
/*     */     }
/*     */ 
/* 235 */     int op = super.open();
/* 236 */     super.enableButtons();
/*     */ 
/* 239 */     return op;
/*     */   }
/*     */ 
/*     */   public Point centerOfParent() {
/* 243 */     Rectangle parentSize = getParentShell().getBounds();
/* 244 */     Rectangle mySize = getShell().getBounds();
/*     */ 
/* 247 */     int locationX = (parentSize.width - mySize.width) / 2 + parentSize.x;
/* 248 */     int locationY = (parentSize.height - mySize.height) / 2 + parentSize.y;
/*     */ 
/* 250 */     return new Point(locationX, locationY);
/*     */   }
/*     */ 
/*     */   public HashMap<String, Object> getAttrFromDlg()
/*     */   {
/* 258 */     HashMap attr = new HashMap();
/*     */ 
/* 260 */     return attr;
/*     */   }
/*     */ 
/*     */   public void setAttrForDlg(IAttribute attr)
/*     */   {
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.CycleDlg
 * JD-Core Version:    0.6.2
 */