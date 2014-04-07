/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*     */ 
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.events.SelectionListener;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Slider;
/*     */ import org.eclipse.swt.widgets.Spinner;
/*     */ 
/*     */ public class SpinnerSlider extends Composite
/*     */ {
/*     */   private Slider scale;
/*     */   private Spinner spinner;
/*     */   private int thumb;
/*     */   private int offset;
/*     */ 
/*     */   public SpinnerSlider(Composite parent, int style, int thumb)
/*     */   {
/*  32 */     super(parent, 0);
/*     */ 
/*  34 */     boolean horizontal = (style & 0x100) != 0;
/*  35 */     GridLayout layout = new GridLayout(horizontal ? 2 : 1, false);
/*  36 */     layout.marginHeight = 0;
/*  37 */     layout.marginWidth = 0;
/*  38 */     layout.horizontalSpacing = 0;
/*  39 */     layout.verticalSpacing = 0;
/*  40 */     setLayout(layout);
/*     */ 
/*  43 */     this.scale = new Slider(this, style);
/*     */     GridData layoutData;
/*  44 */     if (horizontal)
/*  45 */       layoutData = new GridData(4, 16777216, true, false);
/*     */     else {
/*  47 */       layoutData = new GridData(16777216, 4, false, true);
/*     */     }
/*  49 */     this.scale.setLayoutData(layoutData);
/*     */ 
/*  51 */     this.scale.setThumb(thumb);
/*  52 */     setThumb(thumb);
/*     */ 
/*  54 */     this.spinner = new Spinner(this, 2048);
/*  55 */     GridData layoutData = new GridData(-1, -1);
/*  56 */     layoutData.minimumWidth = 30;
/*  57 */     this.spinner.setLayoutData(layoutData);
/*     */ 
/*  59 */     this.scale.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent event) {
/*  62 */         SpinnerSlider.this.spinner.setSelection(SpinnerSlider.this.scale.getSelection() - SpinnerSlider.this.offset);
/*     */       }
/*     */     });
/*  66 */     this.spinner.addSelectionListener(new SelectionAdapter()
/*     */     {
/*     */       public void widgetSelected(SelectionEvent event) {
/*  69 */         SpinnerSlider.this.scale.setSelection(SpinnerSlider.this.spinner.getSelection() + SpinnerSlider.this.offset);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void setSelection(int value)
/*     */   {
/*  83 */     this.scale.setSelection(value + this.offset);
/*  84 */     this.spinner.setSelection(value);
/*     */   }
/*     */ 
/*     */   public int getSelection()
/*     */   {
/*  93 */     return this.spinner.getSelection();
/*     */   }
/*     */ 
/*     */   public void setMinimum(int value)
/*     */   {
/* 106 */     this.spinner.setMinimum(value);
/*     */ 
/* 108 */     int min = this.spinner.getMinimum();
/* 109 */     if (min < 0) {
/* 110 */       this.offset = (-min);
/*     */     }
/*     */ 
/* 113 */     this.scale.setMinimum(min + this.offset);
/* 114 */     this.scale.setMaximum(this.spinner.getMaximum() + this.offset);
/*     */   }
/*     */ 
/*     */   public int getMinimum()
/*     */   {
/* 123 */     return this.spinner.getMinimum();
/*     */   }
/*     */ 
/*     */   public void setMaximum(int value)
/*     */   {
/* 137 */     this.spinner.setMaximum(value);
/* 138 */     this.scale.setMaximum(this.spinner.getMaximum() + this.thumb);
/*     */   }
/*     */ 
/*     */   public int getMaximum()
/*     */   {
/* 147 */     return this.spinner.getMaximum();
/*     */   }
/*     */ 
/*     */   public void setIncrement(int value)
/*     */   {
/* 158 */     this.scale.setIncrement(value);
/* 159 */     this.spinner.setIncrement(value);
/*     */   }
/*     */ 
/*     */   public int getIncrement()
/*     */   {
/* 169 */     return this.spinner.getIncrement();
/*     */   }
/*     */ 
/*     */   public void setDigits(int value)
/*     */   {
/* 186 */     this.spinner.setDigits(value);
/*     */   }
/*     */ 
/*     */   public int getDigits()
/*     */   {
/* 195 */     return this.spinner.getDigits();
/*     */   }
/*     */ 
/*     */   public int getTextLimit()
/*     */   {
/* 206 */     return this.spinner.getTextLimit();
/*     */   }
/*     */ 
/*     */   public void setTextLimit(int limit)
/*     */   {
/* 221 */     this.spinner.setTextLimit(limit);
/*     */   }
/*     */ 
/*     */   public void setValues(int selection, int minimum, int maximum, int digits, int increment, int pageIncrement)
/*     */   {
/* 247 */     this.spinner.setValues(selection, minimum, maximum, digits, increment, 
/* 248 */       pageIncrement);
/*     */ 
/* 250 */     int min = this.spinner.getMinimum();
/* 251 */     int max = this.spinner.getMaximum();
/* 252 */     if (min < 0) {
/* 253 */       this.offset = (-min);
/*     */     }
/*     */ 
/* 256 */     if (max > this.scale.getMinimum()) {
/* 257 */       this.scale.setMaximum(max + this.offset);
/* 258 */       this.scale.setMinimum(min + this.offset);
/*     */     } else {
/* 260 */       this.scale.setMinimum(min + this.offset);
/* 261 */       this.scale.setMaximum(max + this.offset);
/*     */     }
/* 263 */     this.scale.setSelection(this.spinner.getSelection() + this.offset);
/* 264 */     this.scale.setIncrement(this.spinner.getIncrement());
/* 265 */     this.scale.setPageIncrement(this.spinner.getPageIncrement());
/*     */   }
/*     */ 
/*     */   public void setPageIncrement(int value)
/*     */   {
/* 278 */     this.scale.setPageIncrement(value);
/* 279 */     this.spinner.setPageIncrement(value);
/*     */   }
/*     */ 
/*     */   public int getPageIncrement()
/*     */   {
/* 289 */     return this.spinner.getPageIncrement();
/*     */   }
/*     */ 
/*     */   public void addSelectionListener(SelectionListener listener)
/*     */   {
/* 305 */     this.scale.addSelectionListener(listener);
/* 306 */     this.spinner.addSelectionListener(listener);
/*     */   }
/*     */ 
/*     */   public void removeSelectionListener(SelectionListener listener)
/*     */   {
/* 317 */     this.scale.removeSelectionListener(listener);
/* 318 */     this.spinner.removeSelectionListener(listener);
/*     */   }
/*     */ 
/*     */   public void setThumb(int value)
/*     */   {
/* 323 */     this.thumb = value;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.SpinnerSlider
 * JD-Core Version:    0.6.2
 */