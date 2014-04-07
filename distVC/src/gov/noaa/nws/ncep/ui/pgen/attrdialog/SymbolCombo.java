/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.display.SymbolImageUtil;
/*     */ import java.util.HashMap;
/*     */ import org.eclipse.swt.events.ControlListener;
/*     */ import org.eclipse.swt.events.DisposeEvent;
/*     */ import org.eclipse.swt.events.DisposeListener;
/*     */ import org.eclipse.swt.graphics.Device;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ import org.eclipse.swt.graphics.Point;
/*     */ import org.eclipse.swt.graphics.Rectangle;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Listener;
/*     */ import org.eclipse.swt.widgets.Menu;
/*     */ import org.eclipse.swt.widgets.MenuItem;
/*     */ import org.eclipse.swt.widgets.ToolBar;
/*     */ import org.eclipse.swt.widgets.ToolItem;
/*     */ import org.eclipse.swt.widgets.Widget;
/*     */ 
/*     */ public class SymbolCombo extends Composite
/*     */ {
/*     */   private ToolBar tb;
/*     */   private ToolItem ti;
/*     */   private Menu menu;
/*     */   private Device device;
/*     */   private HashMap<String, Image> iconMap;
/*  52 */   private static final String NO_TEXT = new String("");
/*     */ 
/*     */   public SymbolCombo(Composite parent)
/*     */   {
/*  60 */     super(parent.getParent(), 0);
/*  61 */     this.device = parent.getDisplay();
/*  62 */     this.iconMap = new HashMap();
/*     */ 
/*  64 */     this.tb = new ToolBar(parent, 256);
/*  65 */     this.ti = new ToolItem(this.tb, 4);
/*  66 */     this.menu = new Menu(parent.getShell(), 8);
/*  67 */     this.ti.addListener(13, new Listener()
/*     */     {
/*     */       public void handleEvent(Event event)
/*     */       {
/*  71 */         Rectangle bounds = SymbolCombo.this.ti.getBounds();
/*  72 */         Point point = SymbolCombo.this.tb.toDisplay(bounds.x, bounds.y + bounds.height);
/*  73 */         SymbolCombo.this.menu.setLocation(point);
/*  74 */         SymbolCombo.this.menu.setVisible(true);
/*     */       }
/*     */     });
/*  81 */     addDisposeListener(new DisposeListener()
/*     */     {
/*     */       public void widgetDisposed(DisposeEvent e) {
/*  84 */         if (SymbolCombo.this.iconMap != null) {
/*  85 */           for (Image im : SymbolCombo.this.iconMap.values()) {
/*  86 */             im.dispose();
/*     */           }
/*  88 */           SymbolCombo.this.iconMap.clear();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void addControlListener(ControlListener listener)
/*     */   {
/*  99 */     this.tb.addControlListener(listener);
/*     */   }
/*     */ 
/*     */   public void setItems(String[] ids)
/*     */   {
/* 108 */     removeAll();
/*     */ 
/* 110 */     for (String id : ids)
/*     */     {
/* 113 */       MenuItem mi = new MenuItem(this.menu, 8);
/* 114 */       mi.setData(id);
/*     */ 
/* 116 */       if (id != null)
/*     */       {
/* 118 */         Image icon = SymbolImageUtil.createIcon(this.device, id);
/*     */ 
/* 121 */         if (icon != null) {
/* 122 */           icon.setBackground(this.tb.getBackground());
/* 123 */           this.iconMap.put(id, icon);
/* 124 */           mi.setImage(icon);
/*     */         } else {
/* 126 */           mi.setText(id);
/*     */         }
/*     */       }
/*     */ 
/* 130 */       mi.addListener(13, new Listener()
/*     */       {
/*     */         public void handleEvent(Event event)
/*     */         {
/* 135 */           String text = (String)event.widget.getData();
/* 136 */           SymbolCombo.this.setSelectedText(text);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 141 */     select(0);
/* 142 */     this.tb.pack();
/*     */   }
/*     */ 
/*     */   public void setItems(String[] ids, Image[] icons)
/*     */   {
/* 151 */     removeAll();
/*     */ 
/* 153 */     for (int ii = 0; ii < ids.length; ii++)
/*     */     {
/* 156 */       MenuItem mi = new MenuItem(this.menu, 8);
/* 157 */       mi.setData(ids[ii]);
/*     */ 
/* 160 */       if (icons[ii] != null)
/*     */       {
/* 162 */         this.iconMap.put(ids[ii], icons[ii]);
/* 163 */         mi.setImage(icons[ii]);
/*     */       }
/*     */       else {
/* 166 */         mi.setText(ids[ii]);
/*     */       }
/*     */ 
/* 169 */       mi.addListener(13, new Listener()
/*     */       {
/*     */         public void handleEvent(Event event)
/*     */         {
/* 174 */           String text = (String)event.widget.getData();
/* 175 */           SymbolCombo.this.setSelectedText(text);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 180 */     select(0);
/*     */ 
/* 182 */     this.tb.pack();
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 189 */     for (MenuItem mi : this.menu.getItems()) {
/* 190 */       mi.dispose();
/*     */     }
/* 192 */     this.tb.pack();
/*     */   }
/*     */ 
/*     */   public void select(int index)
/*     */   {
/* 202 */     MenuItem[] items = this.menu.getItems();
/* 203 */     if ((index >= 0) && (index < items.length)) {
/* 204 */       String text = (String)items[index].getData();
/* 205 */       setSelectedText(text);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getSelectedText()
/*     */   {
/* 215 */     return (String)this.ti.getData();
/*     */   }
/*     */ 
/*     */   public void setSelectedText(String text)
/*     */   {
/* 224 */     this.ti.setData(text);
/*     */ 
/* 229 */     if (this.iconMap.containsKey(text)) {
/* 230 */       this.ti.setText(NO_TEXT);
/* 231 */       this.ti.setImage((Image)this.iconMap.get(text));
/*     */     }
/*     */     else {
/* 234 */       this.ti.setText(text);
/* 235 */       this.ti.setImage(null);
/*     */     }
/* 237 */     this.tb.pack();
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean enable)
/*     */   {
/* 245 */     if (!enable) {
/* 246 */       String disable = new String("Not Applicable");
/* 247 */       this.ti.setData(disable);
/* 248 */       this.ti.setText(disable);
/* 249 */       this.ti.setImage(null);
/*     */     }
/*     */ 
/* 252 */     this.tb.setEnabled(enable);
/* 253 */     this.tb.pack();
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.SymbolCombo
 * JD-Core Version:    0.6.2
 */