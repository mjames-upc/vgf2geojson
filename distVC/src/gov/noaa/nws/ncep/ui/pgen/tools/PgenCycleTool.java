/*     */ package gov.noaa.nws.ncep.ui.pgen.tools;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.rsc.IInputHandler;
/*     */ import com.raytheon.viz.ui.editor.AbstractEditor;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public class PgenCycleTool extends AbstractPgenDrawingTool
/*     */ {
/*  36 */   private static int cycleDay = 1;
/*     */ 
/*  39 */   private static int cycleHour = 2;
/*     */ 
/*  42 */   private static boolean isRoutine = true;
/*     */ 
/*  45 */   private static final int[] CYCLES_SUMMER = { 2, 8, 14, 20 };
/*     */ 
/*  48 */   private static final int[] CYCLES_WINTER = { 3, 9, 15, 21 };
/*     */ 
/*     */   public PgenCycleTool()
/*     */   {
/*  57 */     Calendar cal = Calendar.getInstance();
/*  58 */     int hour = cal.get(11);
/*  59 */     int[] CYCLES = getCyclesArray();
/*     */ 
/*  61 */     for (int i = 0; i < CYCLES.length; i++) {
/*  62 */       if (hour < CYCLES[i]) {
/*  63 */         cycleHour = CYCLES[i];
/*  64 */         break;
/*     */       }
/*     */     }
/*  67 */     if (hour >= CYCLES[(CYCLES.length - 1)]) {
/*  68 */       cycleHour = CYCLES[0];
/*  69 */       cal.add(5, 1);
/*     */     }
/*     */ 
/*  72 */     cycleDay = cal.get(5);
/*     */ 
/*  74 */     isRoutine = true;
/*     */ 
/*  76 */     updateTitle();
/*     */   }
/*     */ 
/*     */   public IInputHandler getMouseHandler()
/*     */   {
/*  86 */     if (this.mouseHandler == null) {
/*  87 */       this.mouseHandler = new PgenCycleHandler();
/*     */     }
/*     */ 
/*  90 */     return this.mouseHandler;
/*     */   }
/*     */ 
/*     */   public static int getCycleDay()
/*     */   {
/* 141 */     return cycleDay;
/*     */   }
/*     */ 
/*     */   public static void setCycleDay(int day) {
/* 145 */     cycleDay = day;
/* 146 */     updateTitle();
/*     */   }
/*     */ 
/*     */   public static int getCycleHour() {
/* 150 */     return cycleHour;
/*     */   }
/*     */ 
/*     */   public static String getCycleHourStr() {
/* 154 */     return pad(cycleHour);
/*     */   }
/*     */ 
/*     */   public static void setCycleHour(int hour) {
/* 158 */     cycleHour = hour;
/* 159 */     updateTitle();
/*     */   }
/*     */ 
/*     */   public static int[] getCyclesArray()
/*     */   {
/* 164 */     if (TimeZone.getDefault().inDaylightTime(new Date())) {
/* 165 */       return CYCLES_SUMMER;
/*     */     }
/* 167 */     return CYCLES_WINTER;
/*     */   }
/*     */ 
/*     */   public static boolean isRoutine() {
/* 171 */     return isRoutine;
/*     */   }
/*     */ 
/*     */   public static void setCycleRoutine(boolean isRoutine) {
/* 175 */     isRoutine = isRoutine;
/* 176 */     updateTitle();
/*     */   }
/*     */ 
/*     */   public static String pad(int in)
/*     */   {
/* 187 */     return in;
/*     */   }
/*     */ 
/*     */   public static void updateTitle() {
/* 191 */     String title = " -- Day/Cycle:  " + pad(cycleDay) + "/" + pad(cycleHour) + "Z ";
/* 192 */     title = title + (isRoutine ? "Routine" : "Update");
/*     */ 
/* 194 */     PgenUtil.setCaveTitle(title);
/*     */   }
/*     */ 
/*     */   public class PgenCycleHandler extends InputHandlerDefaultImpl
/*     */   {
/*     */     public PgenCycleHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDown(int anX, int aY, int button)
/*     */     {
/* 110 */       if (!PgenCycleTool.this.isResourceEditable()) return false;
/*     */ 
/* 113 */       Coordinate loc = PgenCycleTool.this.mapEditor.translateClick(anX, aY);
/* 114 */       if ((loc == null) || (this.shiftDown)) {
/* 115 */         return false;
/*     */       }
/* 117 */       if (button == 1)
/*     */       {
/* 119 */         PgenCycleTool.this.mapEditor.refresh();
/*     */ 
/* 121 */         return true;
/*     */       }
/* 123 */       if (button == 3)
/*     */       {
/* 125 */         PgenUtil.setSelectingMode();
/* 126 */         return true;
/*     */       }
/* 128 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean handleMouseDownMove(int x, int y, int mouseButton)
/*     */     {
/* 134 */       if ((!PgenCycleTool.this.isResourceEditable()) || (this.shiftDown)) return false;
/* 135 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.tools.PgenCycleTool
 * JD-Core Version:    0.6.2
 */