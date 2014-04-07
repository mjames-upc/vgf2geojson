/*     */ package gov.noaa.nws.ncep.ui.pgen.file;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.SerializationException;
/*     */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.producttypes.ProductTypes;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.validation.Validator;
/*     */ 
/*     */ public class FileTools
/*     */ {
/*     */   public static Products read(String fileName)
/*     */   {
/*  59 */     Products products = null;
/*  60 */     Object result = null;
/*     */     try
/*     */     {
/*  64 */       result = SerializationUtil.jaxbUnmarshalFromXmlFile(fileName);
/*     */     }
/*     */     catch (SerializationException localSerializationException)
/*     */     {
/*     */     }
/*     */ 
/*  70 */     if ((result instanceof Products)) {
/*  71 */       products = (Products)result;
/*     */     }
/*     */ 
/*  74 */     return products;
/*     */   }
/*     */ 
/*     */   public static void write(String fileName, Products products)
/*     */   {
/*  83 */     int lind = fileName.lastIndexOf("/");
/*  84 */     if (lind >= 0) {
/*  85 */       String fdir = fileName.substring(0, lind);
/*  86 */       File checkDir = new File(fdir);
/*  87 */       if ((!checkDir.exists()) || (!checkDir.isDirectory())) {
/*  88 */         checkDir.mkdirs();
/*  89 */         checkDir.setReadable(true, false);
/*  90 */         checkDir.setWritable(true, false);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  97 */       SerializationUtil.jaxbMarshalToXmlFile(products, fileName);
/*  98 */       File xmlf = new File(fileName);
/*  99 */       if (xmlf.exists()) {
/* 100 */         xmlf.setReadable(true, false);
/* 101 */         xmlf.setWritable(true, false);
/*     */       }
/*     */     }
/*     */     catch (SerializationException e)
/*     */     {
/* 106 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ProductTypes readProductTypes(String fileName)
/*     */   {
/* 116 */     ProductTypes types = null;
/*     */     try
/*     */     {
/* 119 */       types = (ProductTypes)SerializationUtil.jaxbUnmarshalFromXmlFile(fileName);
/*     */     }
/*     */     catch (SerializationException e)
/*     */     {
/* 123 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 126 */     return types;
/*     */   }
/*     */ 
/*     */   public static void write(String fileName, ProductTypes types)
/*     */   {
/*     */     try
/*     */     {
/* 137 */       SerializationUtil.jaxbMarshalToXmlFile(types, fileName);
/*     */     }
/*     */     catch (SerializationException e)
/*     */     {
/* 141 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean validate(String fileName, String xsdFile)
/*     */   {
/* 153 */     boolean valid = true;
/*     */ 
/* 155 */     if (xsdFile == null) {
/* 156 */       xsdFile = PgenStaticDataProvider.getProvider().getFileAbsolutePath(
/* 157 */         PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + "product.xsd");
/*     */     }
/*     */ 
/* 160 */     File xsdf = new File(fileName);
/* 161 */     if ((!xsdf.exists()) || (!xsdf.canRead())) {
/* 162 */       return true;
/*     */     }
/*     */ 
/* 165 */     xsdFile = "file://" + xsdFile;
/*     */     try
/*     */     {
/* 169 */       URL schemaFile = new URL(xsdFile);
/* 170 */       Source xmlFile = new StreamSource(new File(fileName));
/*     */ 
/* 172 */       SchemaFactory schemaFactory = 
/* 173 */         SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/*     */ 
/* 175 */       Schema schema = schemaFactory.newSchema(schemaFile);
/*     */ 
/* 177 */       Validator validator = schema.newValidator();
/*     */       try
/*     */       {
/* 180 */         validator.validate(xmlFile);
/*     */       } catch (Exception e) {
/* 182 */         valid = false;
/*     */       }
/*     */     } catch (Exception e) {
/* 185 */       valid = false;
/*     */     }
/*     */ 
/* 188 */     return valid;
/*     */   }
/*     */ 
/*     */   public static void writeFile(String path, String contents)
/*     */   {
/* 197 */     File outf = new File(path);
/* 198 */     File parent = outf.getParentFile();
/*     */ 
/* 200 */     if ((parent != null) && (!parent.exists())) {
/* 201 */       parent.mkdirs();
/* 202 */       parent.setReadable(true, false);
/* 203 */       parent.setWritable(true, false);
/*     */     }
/* 205 */     else if ((parent != null) && (parent.exists()) && (!parent.canWrite())) {
/* 206 */       parent.setWritable(true, false);
/*     */     }
/*     */     try
/*     */     {
/* 210 */       FileWriter fw = new FileWriter(outf);
/* 211 */       fw.write(contents);
/* 212 */       fw.close();
/*     */ 
/* 214 */       outf.setReadable(true, false);
/* 215 */       outf.setWritable(true, false);
/*     */     }
/*     */     catch (Exception e) {
/* 218 */       System.out.println("[PGEN] Problem writing file " + outf.getAbsolutePath());
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.FileTools
 * JD-Core Version:    0.6.2
 */