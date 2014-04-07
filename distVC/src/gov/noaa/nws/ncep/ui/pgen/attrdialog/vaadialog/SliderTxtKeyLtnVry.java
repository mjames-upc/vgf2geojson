/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import org.eclipse.swt.events.KeyAdapter;
/*     */ import org.eclipse.swt.events.KeyEvent;
/*     */ import org.eclipse.swt.events.VerifyEvent;
/*     */ import org.eclipse.swt.events.VerifyListener;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Slider;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ public class SliderTxtKeyLtnVry extends KeyAdapter
/*     */   implements VerifyListener
/*     */ {
/*     */   Slider widthSlider;
/*     */   Text widthText;
/*  44 */   int MIN_LINE_WIDTH = 0; int MAX_LINE_WIDTH = 0;
/*  45 */   double MIN_SIZE = 0.0D; double MAX_SIZE = 10.0D; double DEFAULT_SIZE = 2.0D;
/*  46 */   boolean isDouble = false;
/*  47 */   String iniValue = "";
/*     */ 
/*     */   public SliderTxtKeyLtnVry(Slider ws, Text wt, int minw, int maxw)
/*     */   {
/*  51 */     this.widthSlider = ws;
/*  52 */     this.widthText = wt;
/*  53 */     this.MIN_LINE_WIDTH = minw;
/*  54 */     this.MAX_LINE_WIDTH = maxw;
/*     */ 
/*  56 */     this.widthSlider.addListener(13, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/*  61 */         SliderTxtKeyLtnVry.this.widthText.setText(String.valueOf(SliderTxtKeyLtnVry.this.widthSlider.getSelection()));
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public SliderTxtKeyLtnVry(Slider ws, Text wt, double minw, double maxw)
/*     */   {
/*  70 */     this.widthSlider = ws;
/*  71 */     this.widthText = wt;
/*  72 */     this.MIN_SIZE = minw;
/*  73 */     this.MAX_SIZE = maxw;
/*  74 */     this.isDouble = true;
/*     */ 
/*  76 */     this.widthSlider.addListener(13, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/*  81 */         SliderTxtKeyLtnVry.this.widthText.setText(String.format("%1$4.1f", new Object[] { Double.valueOf(SliderTxtKeyLtnVry.getTxtFrmSlider(SliderTxtKeyLtnVry.this.widthSlider)) }));
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void keyReleased(KeyEvent e)
/*     */   {
/*  91 */     if (this.isDouble)
/*     */     {
/*  93 */       double value = 0.0D;
/*  94 */       String in = this.widthText.getText();
/*     */       try
/*     */       {
/*  98 */         value = Double.parseDouble(in);
/*  99 */         if ((value >= this.MIN_SIZE) && (value <= this.MAX_SIZE))
/*     */         {
/* 101 */           this.widthSlider.setSelection(getIntFrmText(value));
/* 102 */           this.widthText.setToolTipText("");
/*     */         }
/* 104 */         else if (value > this.MAX_SIZE)
/*     */         {
/* 106 */           this.widthText.setText(String.format("%1$4.1f", new Object[] { Double.valueOf(getFrstAsNum(in)) }));
/* 107 */           this.widthSlider.setSelection(getIntFrmText(getFrstAsNum(in)));
/*     */         }
/*     */       }
/*     */       catch (NumberFormatException e1)
/*     */       {
/* 112 */         this.widthText.setText(String.format("%1$4.1f", new Object[] { Double.valueOf(getFrstAsNum(in)) }));
/* 113 */         this.widthSlider.setSelection(getIntFrmText(getFrstAsNum(in)));
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 118 */       int value = 0;
/*     */       try {
/* 120 */         value = Integer.parseInt(this.widthText.getText());
/* 121 */         if ((value >= this.MIN_LINE_WIDTH) && (value <= this.MAX_LINE_WIDTH)) {
/* 122 */           this.widthSlider.setSelection(value);
/* 123 */           this.widthText.setToolTipText("");
/*     */         }
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException1) {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void verifyText(VerifyEvent e) {
/* 132 */     e.doit = validateLineWidth(e);
/* 133 */     if (!e.doit) Display.getCurrent().beep();
/*     */   }
/*     */ 
/*     */   protected boolean validateLineWidth(VerifyEvent ve)
/*     */   {
/* 145 */     if (this.isDouble)
/*     */     {
/* 147 */       boolean stat = false;
/*     */ 
/* 149 */       if ((ve.widget instanceof Text)) {
/* 150 */         Text wText = (Text)ve.widget;
/* 151 */         StringBuffer str = new StringBuffer(wText.getText());
/* 152 */         str.replace(ve.start, ve.end, ve.text);
/*     */ 
/* 154 */         if (str.toString().isEmpty()) return true;
/*     */         try
/*     */         {
/* 157 */           double value = Double.parseDouble(str.toString());
/* 158 */           if ((value >= this.MIN_SIZE) && (value <= this.MAX_SIZE)) {
/* 159 */             stat = true;
/*     */           }
/*     */           else
/* 162 */             stat = false;
/*     */         } catch (NumberFormatException e1) {
/* 164 */           stat = false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 169 */       return stat;
/*     */     }
/*     */ 
/* 173 */     boolean stat = false;
/*     */ 
/* 175 */     if ((ve.widget instanceof Text)) {
/* 176 */       Text wText = (Text)ve.widget;
/* 177 */       StringBuffer str = new StringBuffer(wText.getText());
/* 178 */       str.replace(ve.start, ve.end, ve.text);
/*     */ 
/* 180 */       if (str.toString().isEmpty()) return true;
/*     */       try
/*     */       {
/* 183 */         int value = Integer.parseInt(str.toString());
/* 184 */         if ((value >= this.MIN_LINE_WIDTH) && (value <= this.MAX_LINE_WIDTH)) {
/* 185 */           stat = true;
/*     */         }
/*     */         else
/* 188 */           stat = false;
/*     */       } catch (NumberFormatException e1) {
/* 190 */         stat = false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 195 */     return stat;
/*     */   }
/*     */ 
/*     */   public static double getTxtFrmSlider(Slider s)
/*     */   {
/* 206 */     int i = s.getSelection();
/*     */ 
/* 208 */     int ii = i == 0 ? 1 : i;
/*     */ 
/* 210 */     return ii / 10.0D;
/*     */   }
/*     */ 
/*     */   public static int getIntFrmText(double d)
/*     */   {
/* 220 */     int i = (int)(d == 0.0D ? 1.0D : d * 10.0D);
/*     */ 
/* 222 */     return i;
/*     */   }
/*     */ 
/*     */   public static double getFrstAsNum(String t)
/*     */   {
/* 232 */     double d = 10.0D;
/*     */ 
/* 234 */     if ((t == null) || (t.isEmpty())) {
/* 235 */       return d;
/*     */     }
/*     */     try
/*     */     {
/* 239 */       String s = t.trim().substring(0, 1);
/* 240 */       d = Double.parseDouble(s);
/*     */     }
/*     */     catch (Exception e) {
/* 243 */       System.out.println("______Error: getFrstAsNum()" + e.getMessage());
/*     */     }
/*     */ 
/* 246 */     return d == 0.0D ? 1.0D : d;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.vaadialog.SliderTxtKeyLtnVry
 * JD-Core Version:    0.6.2
 */