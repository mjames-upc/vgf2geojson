/*     */ package gov.noaa.nws.ncep.ui.pgen.attrdialog;
/*     */ 
/*     */ import com.raytheon.uf.common.localization.LocalizationFile;
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.AbstractDrawableComponent;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Text;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Track;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.FileTools;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*     */ import gov.noaa.nws.ncep.ui.pgen.productmanage.ProductConfigureDialog;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public class AttrSettings
/*     */ {
/*  52 */   private static AttrSettings INSTANCE = null;
/*     */ 
/*  54 */   private static HashMap<String, AbstractDrawableComponent> settings = null;
/*     */ 
/*  56 */   private static String settingsTblLocal = "." + File.separator;
/*     */ 
/*  58 */   public static String settingsFileName = "settings_tbl.xml";
/*     */ 
/*     */   private AttrSettings()
/*     */     throws VizException
/*     */   {
/*  67 */     settings = new HashMap();
/*  68 */     loadSettingsTable();
/*     */   }
/*     */ 
/*     */   public static AttrSettings getInstance()
/*     */   {
/*  79 */     if (INSTANCE == null) {
/*     */       try
/*     */       {
/*  82 */         INSTANCE = new AttrSettings();
/*     */       } catch (VizException e) {
/*  84 */         e.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  89 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   public HashMap<String, AbstractDrawableComponent> getSettings()
/*     */   {
/*  97 */     return settings;
/*     */   }
/*     */ 
/*     */   public void setSettings(AbstractDrawableComponent de)
/*     */   {
/* 122 */     String pgenID = de.getPgenType();
/*     */ 
/* 124 */     settings.put(pgenID, de);
/*     */   }
/*     */ 
/*     */   private void loadSettingsTable()
/*     */   {
/* 136 */     File settingsFile = PgenStaticDataProvider.getProvider().getFile(
/* 137 */       PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + 
/* 138 */       settingsFileName);
/*     */ 
/* 140 */     if (settingsFile == null) {
/* 141 */       System.out.println("Unable to fing pgen settings table");
/*     */     }
/*     */ 
/* 144 */     loadPgenSettings(settingsFile.getAbsolutePath());
/*     */   }
/*     */ 
/*     */   public void loadProdSettings(String prodName)
/*     */   {
/* 149 */     if ((prodName == null) || (prodName.isEmpty()))
/* 150 */       loadSettingsTable();
/*     */     else
/*     */       try
/*     */       {
/* 154 */         String pt = 
/* 155 */           ((ProductType)ProductConfigureDialog.getProductTypes()
/* 155 */           .get(prodName)).getType();
/* 156 */         String pt1 = pt.replaceAll(" ", "_");
/*     */ 
/* 158 */         LocalizationFile lFile = PgenStaticDataProvider.getProvider()
/* 159 */           .getStaticLocalizationFile(
/* 160 */           ProductConfigureDialog.getSettingFullPath(pt1));
/*     */ 
/* 162 */         String filePath = lFile.getFile().getAbsolutePath();
/* 163 */         if (!new File(filePath).canRead())
/* 164 */           loadSettingsTable();
/*     */         else
/* 166 */           loadPgenSettings(filePath);
/*     */       }
/*     */       catch (Exception e) {
/* 169 */         loadSettingsTable();
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean loadPgenSettings(String fileName)
/*     */   {
/* 176 */     boolean ret = false;
/*     */ 
/* 178 */     HashMap newSettings = new HashMap();
/*     */ 
/* 180 */     File sFile = new File(fileName);
/*     */     try
/*     */     {
/* 183 */       if (sFile.canRead()) {
/* 184 */         Products products = FileTools.read(fileName);
/*     */ 
/* 188 */         List prds = ProductConverter.convert(products);
/*     */         Iterator localIterator2;
/* 190 */         for (Iterator localIterator1 = prds.iterator(); localIterator1.hasNext(); 
/* 192 */           localIterator2.hasNext())
/*     */         {
/* 190 */           Product p = (Product)localIterator1.next();
/*     */ 
/* 193 */           localIterator2 = p
/* 193 */             .getLayers().iterator(); continue; Layer layer = (Layer)localIterator2.next();
/*     */ 
/* 196 */           Iterator localIterator3 = layer
/* 196 */             .getDrawables().iterator();
/*     */ 
/* 195 */           while (localIterator3.hasNext()) {
/* 196 */             AbstractDrawableComponent de = (AbstractDrawableComponent)localIterator3.next();
/*     */ 
/* 198 */             String pgenID = null;
/* 199 */             pgenID = de.getPgenType();
/*     */ 
/* 201 */             if (pgenID != null) {
/* 202 */               newSettings.put(pgenID, de);
/*     */             }
/*     */ 
/* 205 */             if (pgenID.equalsIgnoreCase("General Text")) {
/* 206 */               ((Text)de).setText(new String[] { "" });
/* 207 */             } else if (pgenID.equalsIgnoreCase("STORM_TRACK"))
/*     */             {
/* 209 */               Calendar cal1 = Calendar.getInstance(
/* 210 */                 TimeZone.getTimeZone("GMT"));
/* 211 */               Calendar cal2 = (Calendar)cal1.clone();
/* 212 */               cal2.add(11, 1);
/*     */ 
/* 214 */               ((Track)de).setFirstTimeCalendar(cal1);
/* 215 */               ((Track)de).setSecondTimeCalendar(cal2);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 222 */         if (newSettings.size() > 0) {
/* 223 */           settings.clear();
/* 224 */           settings.putAll(newSettings);
/* 225 */           ret = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 230 */       ret = false;
/*     */     }
/*     */ 
/* 233 */     return ret;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.attrdialog.AttrSettings
 * JD-Core Version:    0.6.2
 */