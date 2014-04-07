/*    */ package gov.noaa.nws.ncep.standalone.util;
/*    */ 
/*    */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.FileReader;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.URL;
/*    */ import java.security.CodeSource;
/*    */ import java.security.ProtectionDomain;
/*    */ import javax.xml.bind.JAXBContext;
/*    */ import javax.xml.bind.JAXBException;
/*    */ import javax.xml.bind.Marshaller;
/*    */ import javax.xml.bind.Unmarshaller;
/*    */ 
/*    */ public class Util
/*    */ {
/* 32 */   private static String jaxbPackage = "gov.noaa.nws.ncep.ui.pgen.file";
/*    */ 
/*    */   public static StringBuilder getContent(String fileName)
/*    */     throws IOException
/*    */   {
/* 43 */     StringBuilder s = new StringBuilder(1000);
/* 44 */     File help = new File(fileName);
/* 45 */     String path = Util.class.getProtectionDomain().getCodeSource().getLocation().getPath();
/* 46 */     if (!help.exists()) {
/* 47 */       int lastIndex = path.lastIndexOf("/");
/* 48 */       path = path.substring(0, lastIndex + 1);
/* 49 */       help = new File(path + fileName);
/*    */     }
/* 51 */     if (!help.exists())
/*    */     {
/* 53 */       help = new File(path + "../hlp/" + fileName);
/*    */     }
/* 55 */     BufferedReader input = new BufferedReader(new FileReader(help));
/*    */     try {
/* 57 */       String line = null;
/* 58 */       while ((line = input.readLine()) != null) {
/* 59 */         s.append(line);
/* 60 */         s.append(System.getProperty("line.separator"));
/*    */       }
/*    */     } finally {
/* 63 */       input.close();
/*    */     }
/* 65 */     return s;
/*    */   }
/*    */ 
/*    */   public static Products read(String fileName)
/*    */     throws FileNotFoundException
/*    */   {
/* 74 */     Products products = null;
/*    */     try {
/* 76 */       JAXBContext context = JAXBContext.newInstance(jaxbPackage);
/* 77 */       Unmarshaller unmarshaller = context.createUnmarshaller();
/* 78 */       products = (Products)unmarshaller.unmarshal(new FileReader(fileName));
/*    */     }
/*    */     catch (JAXBException e) {
/* 81 */       System.out.println("*** The xml file " + fileName + " is not readable.");
/*    */     }
/*    */     catch (FileNotFoundException e) {
/* 84 */       System.out.println("*** The xml file " + fileName + " is not exist.");
/*    */     }
/* 86 */     return products;
/*    */   }
/*    */ 
/*    */   public static void write(String fileName, Products products, Class<?> clazz)
/*    */     throws FileNotFoundException
/*    */   {
/*    */     try
/*    */     {
/* 96 */       JAXBContext context = JAXBContext.newInstance(new Class[] { clazz });
/* 97 */       Marshaller marshaller = context.createMarshaller();
/* 98 */       marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
/* 99 */       marshaller.marshal(products, new FileWriter(fileName));
/*    */     } catch (JAXBException e) {
/* 101 */       throw new RuntimeException(e);
/*    */     } catch (IOException e) {
/* 103 */       throw new FileNotFoundException("An error occurred writing to " + fileName);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.standalone.util.Util
 * JD-Core Version:    0.6.2
 */