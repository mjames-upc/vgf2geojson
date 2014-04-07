/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.viz.ui.dialogs.CaveJFACEDialog;
/*     */ import com.vividsolutions.jts.geom.Coordinate;
/*     */ import gov.noaa.nws.ncep.edex.common.stationTables.Station;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Symbol;
/*     */ import gov.noaa.nws.ncep.ui.pgen.sigmet.SigmetInfo;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.eclipse.swt.events.ModifyEvent;
/*     */ import org.eclipse.swt.events.ModifyListener;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Menu;
/*     */ import org.eclipse.swt.widgets.MenuItem;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.swt.widgets.ToolBar;
/*     */ import org.eclipse.swt.widgets.ToolItem;
/*     */ 
/*     */ public class VolcanoAttrDlg extends LabeledSymbolAttrDlg
/*     */ {
/*  55 */   private static VolcanoAttrDlg INSTANCE = null;
/*  56 */   private static VolcanoListDlg volList = null;
/*     */ 
/*  58 */   private String volText = "";
/*  59 */   private String volLocation = "";
/*     */ 
/*     */   private VolcanoAttrDlg(Shell parShell)
/*     */     throws VizException
/*     */   {
/*  68 */     super(parShell);
/*  69 */     volList = new VolcanoListDlg(parShell);
/*     */   }
/*     */ 
/*     */   public static SymbolAttrDlg getInstance(Shell parShell)
/*     */   {
/*  82 */     if (INSTANCE == null)
/*     */     {
/*     */       try
/*     */       {
/*  86 */         INSTANCE = new VolcanoAttrDlg(parShell);
/*     */       }
/*     */       catch (VizException e)
/*     */       {
/*  90 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  95 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public int open()
/*     */   {
/* 102 */     int rt = super.open();
/*     */ 
/* 104 */     volList.setBlockOnOpen(false);
/*     */ 
/* 106 */     Point loc = getShell().getLocation();
/*     */ 
/* 108 */     if ((de != null) && ((de instanceof Symbol))) {
/* 109 */       this.latitudeText.setText(new DecimalFormat("###.000").format(((Symbol)de).getLocation().y));
/* 110 */       this.longitudeText.setText(new DecimalFormat("###.000").format(((Symbol)de).getLocation().x));
/*     */     }
/* 112 */     this.labelChkBox.setSelection(true);
/*     */ 
/* 114 */     volList.create();
/* 115 */     volList.getShell().setLocation(loc.x + getShell().getSize().x, loc.y);
/* 116 */     volList.open();
/*     */ 
/* 118 */     return rt;
/*     */   }
/*     */ 
/*     */   public boolean close()
/*     */   {
/* 125 */     volList.close();
/* 126 */     return super.close();
/*     */   }
/*     */ 
/*     */   public void setLatitude(double lat)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setLongitude(double lon)
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getVolText()
/*     */   {
/* 153 */     return this.volText + "\n" + this.volLocation;
/*     */   }
/*     */ 
/*     */   private class VolcanoListDlg extends CaveJFACEDialog
/*     */   {
/*     */     private Text volName;
/*     */ 
/*     */     protected VolcanoListDlg(Shell parentShell)
/*     */     {
/* 166 */       super();
/* 167 */       setShellStyle(96);
/*     */     }
/*     */ 
/*     */     public Control createDialogArea(Composite parent)
/*     */     {
/* 176 */       VolcanoAttrDlg.this.top = ((Composite)super.createDialogArea(parent));
/*     */ 
/* 179 */       GridLayout mainLayout = new GridLayout(2, false);
/* 180 */       mainLayout.marginHeight = 3;
/* 181 */       mainLayout.marginWidth = 3;
/* 182 */       VolcanoAttrDlg.this.top.setLayout(mainLayout);
/*     */ 
/* 185 */       initializeComponents();
/*     */ 
/* 187 */       return VolcanoAttrDlg.this.top;
/*     */     }
/*     */ 
/*     */     protected void initializeComponents()
/*     */     {
/* 196 */       getShell().setText("Volcano List");
/*     */ 
/* 198 */       this.volName = new Text(VolcanoAttrDlg.this.top, 16384);
/* 199 */       this.volName.setLayoutData(new GridData(180, 20));
/*     */ 
/* 201 */       this.volName.addModifyListener(new ModifyListener()
/*     */       {
/*     */         public void modifyText(ModifyEvent e)
/*     */         {
/* 205 */           VolcanoAttrDlg.this.volText = ((Text)e.widget).getText();
/* 206 */           VolcanoAttrDlg.this.volLocation = "";
/*     */         }
/*     */       });
/* 211 */       createVolList(VolcanoAttrDlg.this.top);
/*     */     }
/*     */ 
/*     */     public Control createButtonBar(Composite parent)
/*     */     {
/* 229 */       return parent;
/*     */     }
/*     */ 
/*     */     private void createVolList(Composite comp)
/*     */     {
/* 234 */       Shell shell = getShell();
/*     */ 
/* 236 */       final ToolBar tb = new ToolBar(comp, 256);
/* 237 */       final ToolItem ti = new ToolItem(tb, 4);
/*     */ 
/* 239 */       final Menu mu = new Menu(shell, 8);
/*     */ 
/* 241 */       for (int i = 0; i < SigmetInfo.VOL_NAME_BUCKET_ARRAY.length; i++) {
/* 242 */         if (i == 0) {
/* 243 */           MenuItem mi1 = new MenuItem(mu, 8);
/* 244 */           mi1.setText(SigmetInfo.VOL_NAME_BUCKET_ARRAY[i]);
/*     */ 
/* 246 */           mi1.addListener(13, new Listener()
/*     */           {
/*     */             public void handleEvent(Event e) {
/*     */             }
/*     */           });
/*     */         }
/*     */         else {
/* 253 */           MenuItem mi1 = new MenuItem(mu, 64);
/* 254 */           mi1.setText(SigmetInfo.VOL_NAME_BUCKET_ARRAY[i]);
/* 255 */           Menu mi1Menu = new Menu(shell, 4);
/* 256 */           mi1.setMenu(mi1Menu);
/*     */ 
/* 258 */           List list = (List)SigmetInfo.VOLCANO_BUCKET_MAP.get(SigmetInfo.VOL_NAME_BUCKET_ARRAY[i]);
/* 259 */           int size = list.size();
/* 260 */           for (int j = 0; j < size; j++) {
/* 261 */             final MenuItem mi1MenuMi1 = new MenuItem(mi1Menu, 8);
/* 262 */             mi1MenuMi1.setText((String)list.get(j));
/* 263 */             mi1MenuMi1.addListener(13, new Listener()
/*     */             {
/*     */               public void handleEvent(Event e)
/*     */               {
/* 268 */                 VolcanoAttrDlg.VolcanoListDlg.this.volName.setText(mi1MenuMi1.getText());
/* 269 */                 VolcanoAttrDlg.VolcanoListDlg.this.setLatLonFields(mi1MenuMi1.getText());
/* 270 */                 VolcanoAttrDlg.this.placeBtn.setEnabled(true);
/*     */               }
/*     */             });
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 277 */       ti.addListener(13, new Listener()
/*     */       {
/*     */         public void handleEvent(Event event)
/*     */         {
/* 281 */           Rectangle bounds = ti.getBounds();
/* 282 */           Point point = tb.toDisplay(bounds.x, bounds.y + bounds.height);
/* 283 */           mu.setLocation(point);
/* 284 */           mu.setVisible(true);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     private void setLatLonFields(String volName)
/*     */     {
/* 299 */       List list = SigmetInfo.VOLCANO_STATION_LIST;
/*     */ 
/* 301 */       Station stn = null;
/*     */ 
/* 303 */       for (Station station : list) {
/* 304 */         if ((volName != null) && (volName.equals(station.getStnname()))) {
/* 305 */           stn = station;
/*     */         }
/*     */       }
/* 308 */       if (stn != null) {
/* 309 */         VolcanoAttrDlg.this.longitudeText.setText(String.valueOf(stn.getLongitude()));
/* 310 */         VolcanoAttrDlg.this.latitudeText.setText(String.valueOf(stn.getLatitude()));
/* 311 */         setVolText(volName, stn.getLongitude().floatValue(), stn.getLatitude().floatValue());
/*     */       }
/*     */     }
/*     */ 
/*     */     private void setVolText(String name, double lon, double lat) {
/* 316 */       VolcanoAttrDlg.this.volText = name;
/* 317 */       VolcanoAttrDlg.this.volLocation = 
/* 320 */         ((lat >= 0.0D ? new DecimalFormat("###.0").format(lat) + "N" : 
/* 318 */         new StringBuilder(String.valueOf(new DecimalFormat("###.0").format(lat * -1.0D))).append("S").toString()) + 
/* 319 */         " " + (
/* 320 */         lon >= 0.0D ? new DecimalFormat("###.0").format(lon) + "E" : 
/* 321 */         new StringBuilder(String.valueOf(new DecimalFormat("###.0").format(lon * -1.0D))).append("W").toString()));
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.VolcanoAttrDlg
 * JD-Core Version:    0.6.2
 */