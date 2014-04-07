/*     */ package gov.noaa.nws.ncep.ui.pgen.store;
/*     */ 
/*     */ import com.raytheon.uf.common.dataplugin.PluginException;
/*     */ import com.raytheon.uf.common.dataquery.requests.DbQueryRequest;
/*     */ import com.raytheon.uf.common.dataquery.requests.RequestConstraint;
/*     */ import com.raytheon.uf.common.dataquery.requests.RequestConstraint.ConstraintType;
/*     */ import com.raytheon.uf.common.dataquery.responses.DbQueryResponse;
/*     */ import com.raytheon.uf.common.datastorage.DataStoreFactory;
/*     */ import com.raytheon.uf.common.datastorage.IDataStore;
/*     */ import com.raytheon.uf.common.message.response.AbstractResponseMessage;
/*     */ import com.raytheon.uf.common.message.response.ResponseMessageError;
/*     */ import com.raytheon.uf.common.message.response.ResponseMessageGeneric;
/*     */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*     */ import com.raytheon.uf.common.time.DataTime;
/*     */ import com.raytheon.uf.viz.core.HDF5Util;
/*     */ import com.raytheon.uf.viz.core.exception.VizException;
/*     */ import com.raytheon.uf.viz.core.requests.ThriftClient;
/*     */ import com.raytheon.viz.core.mode.CAVEMode;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.ActivityInfo;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.DerivedProduct;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.PgenRecord;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.ResponseMessageValidate;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.request.RetrieveActivityRequest;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.request.StoreActivityRequest;
/*     */ import gov.noaa.nws.ncep.common.dataplugin.pgen.request.StoreDerivedProductRequest;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.ProductTime;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.IWorkbenchWindow;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ 
/*     */ public class StorageUtils
/*     */ {
/*     */   private static final String STORAGE_ERROR = "Pgen Storage Exception";
/*     */ 
/*     */   public static String storeProduct(Product prod)
/*     */     throws PgenStorageException
/*     */   {
/*  71 */     return storeProduct(prod, false);
/*     */   }
/*     */ 
/*     */   public static String storeProduct(Product prod, boolean promptOnOverwrite)
/*     */     throws PgenStorageException
/*     */   {
/*  88 */     ActivityInfo info = new ActivityInfo();
/*  89 */     info.setActivityName(prod.getName());
/*  90 */     info.setActivityType(prod.getType());
/*  91 */     info.setActivityLabel(prod.getOutputFile());
/*  92 */     info.setRefTime(prod.getTime().getStartTime());
/*  93 */     info.setSite(prod.getCenter());
/*  94 */     info.setForecaster(prod.getForecaster());
/*  95 */     info.setMode(CAVEMode.getMode().name());
/*     */ 
/*  97 */     return storeProduct(info, prod, promptOnOverwrite);
/*     */   }
/*     */ 
/*     */   public static String storeProduct(ActivityInfo info, Product prod)
/*     */     throws PgenStorageException
/*     */   {
/* 117 */     return storeProduct(info, prod, false);
/*     */   }
/*     */ 
/*     */   public static String storeProduct(ActivityInfo info, Product prod, boolean promptOnOverwrite)
/*     */     throws PgenStorageException
/*     */   {
/* 133 */     ResponseMessageValidate result = null;
/*     */ 
/* 135 */     if (promptOnOverwrite) {
/* 136 */       boolean answer = promptIfActivityExists(info);
/* 137 */       if (!answer)
/* 138 */         return null;
/*     */     }
/*     */     try
/*     */     {
/* 142 */       String activityXML = serializeProduct(prod);
/*     */ 
/* 144 */       StoreActivityRequest request = new StoreActivityRequest(info, 
/* 145 */         activityXML);
/* 146 */       result = (ResponseMessageValidate)
/* 147 */         ThriftClient.sendRequest(request);
/*     */     } catch (Exception e) {
/* 149 */       e.printStackTrace();
/* 150 */       throw new PgenStorageException("Unable to store PGEN Activity.", e);
/*     */     }
/*     */ 
/* 153 */     if (result.getResult() == Boolean.FALSE) {
/* 154 */       throw new PgenStorageException(
/* 155 */         "Request to store PGEN Activity failed:" + 
/* 156 */         result.getMessage());
/*     */     }
/* 158 */     return result.getDataURI();
/*     */   }
/*     */ 
/*     */   private static boolean promptIfActivityExists(ActivityInfo info)
/*     */     throws PgenStorageException
/*     */   {
/* 170 */     boolean canWrite = false;
/*     */ 
/* 172 */     if (activityExists(info))
/*     */     {
/* 174 */       String msg = "Activity already exists. Overwrite?";
/* 175 */       MessageDialog confirmDlg = new MessageDialog(
/* 176 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 177 */         "Confirm", null, msg, 3, new String[] { 
/* 178 */         "OK", "Cancel" }, 0);
/* 179 */       confirmDlg.open();
/*     */ 
/* 181 */       if (confirmDlg.getReturnCode() == 0)
/* 182 */         canWrite = true;
/*     */     }
/*     */     else {
/* 185 */       canWrite = true;
/*     */     }
/*     */ 
/* 188 */     return canWrite;
/*     */   }
/*     */ 
/*     */   private static boolean activityExists(ActivityInfo info)
/*     */     throws PgenStorageException
/*     */   {
/* 197 */     boolean exists = false;
/*     */ 
/* 199 */     PgenRecord record = new PgenRecord();
/* 200 */     record.setActivityLabel(info.getActivityLabel());
/* 201 */     record.setActivityName(info.getActivityName());
/* 202 */     record.setActivityType(info.getActivityType());
/* 203 */     record.setActivitySubtype(info.getActivitySubtype());
/* 204 */     record.setSite(info.getSite());
/* 205 */     record.setDesk(info.getDesk());
/* 206 */     record.setForecaster(info.getForecaster());
/* 207 */     record.setOperatingMode(info.getMode());
/* 208 */     record.setStatus(info.getStatus());
/*     */ 
/* 210 */     record.setDataTime(new DataTime(info.getRefTime()));
/* 211 */     record.setPluginName("pgen");
/*     */     try {
/* 213 */       record.constructDataURI();
/*     */     } catch (PluginException e1) {
/* 215 */       throw new PgenStorageException("Error constructing dataURI", e1);
/*     */     }
/*     */ 
/* 218 */     String dataURI = record.getDataURI();
/*     */ 
/* 220 */     DbQueryRequest request = new DbQueryRequest();
/* 221 */     request.setEntityClass(PgenRecord.class.getName());
/* 222 */     request.addRequestField("dataURI");
/* 223 */     request.addConstraint("dataURI", new RequestConstraint(
/* 224 */       dataURI, RequestConstraint.ConstraintType.EQUALS));
/*     */     try
/*     */     {
/* 228 */       DbQueryResponse response = (DbQueryResponse)ThriftClient.sendRequest(request);
/* 229 */       if (response.getResults().size() == 1)
/* 230 */         exists = true;
/* 231 */       System.out.println("GOT RESPONSE BACK = " + 
/* 232 */         response.getResults().size());
/*     */     } catch (Exception e) {
/* 234 */       throw new PgenStorageException(
/* 235 */         "Error determinimg if activity exists", e);
/*     */     }
/*     */     DbQueryResponse response;
/* 238 */     return exists;
/*     */   }
/*     */ 
/*     */   public static List<Product> retrieveProduct(String dataURI)
/*     */     throws PgenStorageException
/*     */   {
/* 251 */     RetrieveActivityRequest req = new RetrieveActivityRequest(dataURI);
/*     */     try
/*     */     {
/* 254 */       resp = (AbstractResponseMessage)ThriftClient.sendRequest(req);
/*     */     }
/*     */     catch (VizException e)
/*     */     {
/*     */       AbstractResponseMessage resp;
/* 256 */       throw new PgenStorageException(
/* 257 */         "Error sending activity retrieval request.", e);
/*     */     }
/*     */     AbstractResponseMessage resp;
/* 260 */     if ((resp instanceof ResponseMessageError)) {
/* 261 */       ResponseMessageError err = (ResponseMessageError)resp;
/* 262 */       StringBuilder sb = new StringBuilder(
/* 263 */         "Error Retrieving Activity from EDEX");
/* 264 */       if (((ResponseMessageError)resp).getErrorChain() != null) {
/* 265 */         for (String msg : err.getErrorChain()) {
/* 266 */           sb.append("\n");
/* 267 */           sb.append(msg);
/*     */         }
/*     */       }
/* 270 */       throw new PgenStorageException(sb.toString());
/*     */     }
/*     */ 
/* 273 */     String xml = (String)((ResponseMessageGeneric)resp).getContents();
/* 274 */     List prods = deserializeProduct(xml);
/* 275 */     return prods;
/*     */   }
/*     */ 
/*     */   public static String serializeProduct(Product prod)
/*     */     throws PgenStorageException
/*     */   {
/* 288 */     ArrayList prodlist = new ArrayList();
/* 289 */     prodlist.add(prod);
/*     */ 
/* 291 */     Products filePrds = ProductConverter.convert(prodlist);
/*     */     try
/*     */     {
/* 294 */       return SerializationUtil.marshalToXml(filePrds);
/*     */     } catch (JAXBException e) {
/* 296 */       throw new PgenStorageException("Unable to serialize PGEN Activity", 
/* 297 */         e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static List<Product> deserializeProduct(String activityXML)
/*     */     throws PgenStorageException
/*     */   {
/* 311 */     Products prods = null;
/*     */     try
/*     */     {
/* 314 */       prods = (Products)SerializationUtil.unmarshalFromXml(Products.class, 
/* 315 */         activityXML);
/* 316 */       return ProductConverter.convert(prods);
/*     */     } catch (Exception e) {
/* 318 */       throw new PgenStorageException(
/* 319 */         "Unable to deserialize PGEN Activity", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void showError(Exception e)
/*     */   {
/* 331 */     StringBuilder sb = new StringBuilder(e.getMessage());
/*     */ 
/* 333 */     Throwable temp = e;
/* 334 */     while ((temp = temp.getCause()) != null) {
/* 335 */       sb.append("\n");
/* 336 */       sb.append(temp.getMessage());
/*     */     }
/*     */ 
/* 339 */     MessageDialog errorDlg = new MessageDialog(PlatformUI.getWorkbench()
/* 340 */       .getActiveWorkbenchWindow().getShell(), "Pgen Storage Exception", null, 
/* 341 */       sb.toString(), 1, new String[] { "OK" }, 0);
/*     */ 
/* 343 */     errorDlg.open();
/*     */   }
/*     */ 
/*     */   public static void storeDerivedProduct(String dataURI, String name, String productType, Object product)
/*     */     throws PgenStorageException
/*     */   {
/* 361 */     storeDerivedProduct(dataURI, name, productType, product, false);
/*     */   }
/*     */ 
/*     */   public static void storeDerivedProduct(String dataURI, String name, String productType, Object product, boolean promptOnOverwrite)
/*     */     throws PgenStorageException
/*     */   {
/* 383 */     ResponseMessageValidate result = null;
/*     */ 
/* 385 */     if (promptOnOverwrite) {
/* 386 */       boolean answer = promptIfProductExists(dataURI, name);
/* 387 */       if (!answer) {
/* 388 */         return;
/*     */       }
/*     */     }
/* 391 */     StoreDerivedProductRequest request = new StoreDerivedProductRequest(
/* 392 */       dataURI, name, productType, product);
/*     */     try {
/* 394 */       result = (ResponseMessageValidate)
/* 395 */         ThriftClient.sendRequest(request);
/*     */     } catch (VizException e) {
/* 397 */       throw new PgenStorageException(
/* 398 */         "Unable to store PGEN Derived Product.", e);
/*     */     }
/*     */ 
/* 401 */     if (result.getResult() == Boolean.FALSE)
/* 402 */       throw new PgenStorageException(
/* 403 */         "Request to store PGEN DerivedProduct failed:" + 
/* 404 */         result.getMessage());
/*     */   }
/*     */ 
/*     */   private static boolean promptIfProductExists(String dataURI, String name)
/*     */     throws PgenStorageException
/*     */   {
/* 414 */     boolean canWrite = false;
/*     */ 
/* 416 */     if (productExists(dataURI, name))
/*     */     {
/* 418 */       String msg = "Derived Product " + name + 
/* 419 */         " already exists. Overwrite?";
/* 420 */       MessageDialog confirmDlg = new MessageDialog(
/* 421 */         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
/* 422 */         "Confirm", null, msg, 3, new String[] { 
/* 423 */         "OK", "Cancel" }, 0);
/* 424 */       confirmDlg.open();
/*     */ 
/* 426 */       if (confirmDlg.getReturnCode() == 0)
/* 427 */         canWrite = true;
/*     */     }
/*     */     else {
/* 430 */       canWrite = true;
/*     */     }
/*     */ 
/* 433 */     return canWrite;
/*     */   }
/*     */ 
/*     */   private static boolean productExists(String dataURI, String name)
/*     */     throws PgenStorageException
/*     */   {
/* 442 */     boolean exists = false;
/*     */ 
/* 444 */     PgenRecord record = new PgenRecord(dataURI);
/* 445 */     File loc = HDF5Util.findHDF5Location(record);
/* 446 */     IDataStore dataStore = DataStoreFactory.getDataStore(loc);
/* 447 */     String[] products = new String[0];
/*     */     try {
/* 449 */       products = dataStore.getDatasets(dataURI);
/*     */     } catch (Exception e) {
/* 451 */       throw new PgenStorageException(
/* 452 */         "Cannot retrieve list of derived product names.", e);
/*     */     }
/* 454 */     for (String prod : products) {
/* 455 */       if (prod.equals(name)) {
/* 456 */         return true;
/*     */       }
/*     */     }
/* 459 */     return exists;
/*     */   }
/*     */ 
/*     */   public static void storeDerivedProducts(String dataURI, List<DerivedProduct> prodList)
/*     */     throws PgenStorageException
/*     */   {
/* 474 */     ResponseMessageValidate result = null;
/*     */ 
/* 476 */     StoreDerivedProductRequest request = new StoreDerivedProductRequest();
/* 477 */     request.setDataURI(dataURI);
/* 478 */     request.setProductList(prodList);
/*     */     try
/*     */     {
/* 481 */       result = (ResponseMessageValidate)
/* 482 */         ThriftClient.sendRequest(request);
/*     */     } catch (VizException e) {
/* 484 */       throw new PgenStorageException(
/* 485 */         "Unable to store PGEN Derived Product.", e);
/*     */     }
/*     */ 
/* 488 */     if (result.getResult() == Boolean.FALSE)
/* 489 */       throw new PgenStorageException(
/* 490 */         "Request to store PGEN DerivedProduct failed:" + 
/* 491 */         result.getMessage());
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.store.StorageUtils
 * JD-Core Version:    0.6.2
 */