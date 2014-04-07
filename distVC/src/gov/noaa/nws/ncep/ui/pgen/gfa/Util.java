/*    */ package gov.noaa.nws.ncep.ui.pgen.gfa;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import javax.xml.bind.JAXBContext;
/*    */ import javax.xml.bind.JAXBException;
/*    */ import javax.xml.bind.Unmarshaller;
/*    */ 
/*    */ public class Util
/*    */ {
/*    */   private static JAXBContext context;
/* 30 */   private static String jaxbPackage = "gov.noaa.nws.ncep.ui.pgen.file";
/*    */ 
/*    */   public static StringBuilder getContent(String fileName)
/*    */     throws IOException
/*    */   {
/* 41 */     StringBuilder s = new StringBuilder(1000);
/* 42 */     File help = new File(fileName);
/* 43 */     BufferedReader input = new BufferedReader(new FileReader(help));
/*    */     try {
/* 45 */       String line = null;
/* 46 */       while ((line = input.readLine()) != null) {
/* 47 */         s.append(line);
/* 48 */         s.append(System.getProperty("line.separator"));
/*    */       }
/*    */     } finally {
/* 51 */       input.close();
/*    */     }
/* 53 */     return s;
/*    */   }
/*    */ 
/*    */   public static Products read(String fileName)
/*    */     throws FileNotFoundException
/*    */   {
/* 62 */     Products products = null;
/*    */     try {
/* 64 */       getContext();
/* 65 */       Unmarshaller unmarshaller = context.createUnmarshaller();
/* 66 */       products = (Products)unmarshaller.unmarshal(new FileReader(fileName));
/*    */     } catch (JAXBException e) {
/* 68 */       throw new RuntimeException(e);
/*    */     } catch (FileNotFoundException e) {
/* 70 */       throw new FileNotFoundException("An error occurred while opening " + fileName);
/*    */     }
/* 72 */     return products;
/*    */   }
/*    */ 
/*    */   private static JAXBContext getContext() {
/* 76 */     if (context == null) {
/*    */       try {
/* 78 */         context = JAXBContext.newInstance(jaxbPackage);
/*    */       }
/*    */       catch (JAXBException e) {
/* 81 */         e.printStackTrace();
/*    */       }
/*    */     }
/* 84 */     return context;
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.gfa.Util
 * JD-Core Version:    0.6.2
 */