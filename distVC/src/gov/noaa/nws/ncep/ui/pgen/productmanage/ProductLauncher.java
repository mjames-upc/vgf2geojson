/*     */ package gov.noaa.nws.ncep.ui.pgen.productmanage;
/*     */ 
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenSession;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Layer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductInfo;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.PgenLayer;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductType;
/*     */ import gov.noaa.nws.ncep.ui.pgen.rsc.PgenResource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import org.eclipse.jface.action.ContributionItem;
/*     */ import org.eclipse.swt.events.SelectionAdapter;
/*     */ import org.eclipse.swt.events.SelectionEvent;
/*     */ import org.eclipse.swt.widgets.Menu;
/*     */ import org.eclipse.swt.widgets.MenuItem;
/*     */ 
/*     */ public class ProductLauncher extends ContributionItem
/*     */ {
/*  58 */   protected LinkedHashMap<String, ProductType> prdTypeMap = null;
/*     */ 
/*     */   public ProductLauncher() {
/*     */   }
/*     */ 
/*     */   public ProductLauncher(String id) {
/*  64 */     super(id);
/*     */   }
/*     */ 
/*     */   public void fill(Menu menu, int index)
/*     */   {
/*  74 */     this.prdTypeMap = ProductConfigureDialog.getProductTypes();
/*  75 */     ArrayList typeUsed = new ArrayList();
/*     */ 
/*  78 */     int ii = 0;
/*  79 */     for (String ptyp : this.prdTypeMap.keySet())
/*     */     {
/*  81 */       ProductType prdType = (ProductType)this.prdTypeMap.get(ptyp);
/*  82 */       LinkedHashMap subtypesNalias = getSubtypes(prdType.getType(), true);
/*     */ 
/*  84 */       if (((ptyp.equals(prdType.getName())) && 
/*  85 */         (!prdType.getType().equals(prdType.getName()))) || 
/*  86 */         (!hasSubtypes(subtypesNalias.values())))
/*     */       {
/*  88 */         MenuItem typeItem = new MenuItem(menu, 8, ii);
/*     */ 
/*  90 */         typeItem.setText(ptyp);
/*  91 */         typeItem.addSelectionListener(new SelectionAdapter()
/*     */         {
/*     */           public void widgetSelected(SelectionEvent e)
/*     */           {
/*  97 */             ProductLauncher.this.quickLaunch(((MenuItem)e.widget).getText());
/*     */           }
/*     */ 
/*     */         });
/*     */       }
/*     */       else
/*     */       {
/* 104 */         if (typeUsed.contains(prdType.getType()))
/*     */         {
/*     */           continue;
/*     */         }
/* 108 */         typeUsed.add(prdType.getType());
/*     */ 
/* 111 */         MenuItem typeItem = new MenuItem(menu, 64, ii);
/*     */ 
/* 113 */         typeItem.setText(prdType.getType());
/* 114 */         Menu submenu = new Menu(typeItem);
/* 115 */         typeItem.setMenu(submenu);
/*     */ 
/* 117 */         for (String styp : subtypesNalias.keySet()) {
/* 118 */           MenuItem subtypeItem = new MenuItem(submenu, 8);
/* 119 */           subtypeItem.setText((String)subtypesNalias.get(styp));
/*     */ 
/* 121 */           subtypeItem.setData(styp);
/*     */ 
/* 123 */           subtypeItem.addSelectionListener(new SelectionAdapter()
/*     */           {
/*     */             public void widgetSelected(SelectionEvent e)
/*     */             {
/* 129 */               ProductLauncher.this.quickLaunch(((MenuItem)e.widget).getData().toString());
/*     */             }
/*     */           });
/*     */         }
/*     */       }
/*     */ 
/* 135 */       ii++;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void quickLaunch(String prdtype)
/*     */   {
/* 150 */     List productList = new ArrayList();
/*     */ 
/* 152 */     Product prd = new Product(prdtype, "Default", "Default", 
/* 153 */       new ProductInfo(), new ProductTime(), 
/* 154 */       new ArrayList());
/* 155 */     prd.setType(prdtype);
/* 156 */     prd.setOnOff(false);
/* 157 */     prd.setOutputFile(null);
/*     */ 
/* 159 */     List players = getPgenLayers((ProductType)this.prdTypeMap.get(prdtype));
/*     */ 
/* 161 */     if (players != null) {
/* 162 */       for (PgenLayer plyr : players) {
/* 163 */         Layer lyr = new Layer();
/* 164 */         lyr.setName(plyr.getName());
/* 165 */         lyr.setOnOff(plyr.isOnOff().booleanValue());
/* 166 */         lyr.setMonoColor(plyr.isMonoColor().booleanValue());
/* 167 */         lyr.setFilled(plyr.isFilled().booleanValue());
/* 168 */         lyr.setInputFile(plyr.getInputFile());
/* 169 */         lyr.setOutputFile(plyr.getOutputFile());
/*     */ 
/* 171 */         java.awt.Color clr = new java.awt.Color(plyr.getColor().getRed(), 
/* 172 */           plyr.getColor().getGreen(), 
/* 173 */           plyr.getColor().getBlue(), 
/* 174 */           plyr.getColor().getAlpha().intValue());
/* 175 */         lyr.setColor(clr);
/*     */ 
/* 177 */         prd.addLayer(lyr);
/*     */       }
/*     */     }
/*     */     else {
/* 181 */       Layer lyr = new Layer();
/* 182 */       lyr.setName(prdtype);
/* 183 */       prd.addLayer(lyr);
/*     */     }
/*     */ 
/* 186 */     productList.add(prd);
/*     */ 
/* 192 */     List curPrdList = PgenSession.getInstance().getPgenResource().getProducts();
/* 193 */     prd.setName(findUniqueActivityName(curPrdList, prd.getName()));
/* 194 */     PgenSession.getInstance().getPgenResource().addProduct(productList);
/*     */   }
/*     */ 
/*     */   private List<PgenLayer> getPgenLayers(ProductType ptype)
/*     */   {
/* 203 */     List players = null;
/*     */ 
/* 205 */     if (ptype != null) {
/* 206 */       players = ptype.getPgenLayer();
/* 207 */       if (players.size() <= 0) {
/* 208 */         players = null;
/*     */       }
/*     */     }
/*     */ 
/* 212 */     return players;
/*     */   }
/*     */ 
/*     */   private LinkedHashMap<String, String> getSubtypes(String ptype, boolean noAlias)
/*     */   {
/* 225 */     LinkedHashMap stypes = new LinkedHashMap();
/*     */ 
/* 227 */     for (String typeID : this.prdTypeMap.keySet()) {
/* 228 */       ProductType prdType = (ProductType)this.prdTypeMap.get(typeID);
/* 229 */       if (prdType.getType().equals(ptype)) {
/* 230 */         if (noAlias) {
/* 231 */           if ((prdType.getName() == null) || (prdType.getName().trim().length() == 0) || 
/* 232 */             (prdType.getName().equals(prdType.getType()))) {
/* 233 */             stypes.put(typeID, prdType.getSubtype());
/*     */           }
/*     */         }
/*     */         else {
/* 237 */           stypes.put(typeID, prdType.getSubtype());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 242 */     return stypes;
/*     */   }
/*     */ 
/*     */   private boolean hasSubtypes(Collection<String> subtypes)
/*     */   {
/* 252 */     boolean hasSubtypes = true;
/* 253 */     if ((subtypes == null) || (subtypes.size() == 0)) {
/* 254 */       hasSubtypes = false;
/*     */     }
/* 256 */     else if (subtypes.size() == 1) {
/* 257 */       for (String st : subtypes) {
/* 258 */         if (st.equalsIgnoreCase("None")) {
/* 259 */           hasSubtypes = false;
/* 260 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 265 */     return hasSubtypes;
/*     */   }
/*     */ 
/*     */   private String findUniqueActivityName(List<Product> prds, String actName)
/*     */   {
/* 275 */     String outName = new String(actName);
/*     */ 
/* 277 */     int ii = 1;
/* 278 */     while (!isUniqueActivityName(prds, outName)) {
/* 279 */       outName = new String(outName + " " + ii);
/* 280 */       ii++;
/*     */     }
/*     */ 
/* 283 */     return outName;
/*     */   }
/*     */ 
/*     */   private boolean isUniqueActivityName(List<Product> prds, String actName)
/*     */   {
/* 292 */     boolean isUnique = true;
/*     */ 
/* 294 */     for (Product prd : prds) {
/* 295 */       if (prd.getName().equalsIgnoreCase(actName)) {
/* 296 */         isUnique = false;
/* 297 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 301 */     return isUnique;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.productmanage.ProductLauncher
 * JD-Core Version:    0.6.2
 */