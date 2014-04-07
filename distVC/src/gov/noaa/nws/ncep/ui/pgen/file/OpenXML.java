/*    */ package gov.noaa.nws.ncep.ui.pgen.file;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class OpenXML
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 30 */     String inputFile = "/export/cdbsrv/jwu/workbak/PGEN/T63/xml2java/file1/myproduct.xml";
/* 31 */     String outputFile = "/export/cdbsrv/jwu/workbak/PGEN/T63/xml2java/file1/out_product.xml";
/*    */ 
/* 33 */     Products products = FileTools.read(inputFile);
/* 34 */     FileTools.write(outputFile, products);
/*    */ 
/* 36 */     List listOfProducts = products.getProduct();
/*    */ 
/* 44 */     int nn = 0; int mm = 0; int kk = 0;
/*    */     Iterator localIterator2;
/* 45 */     for (Iterator localIterator1 = listOfProducts.iterator(); localIterator1.hasNext(); 
/* 54 */       localIterator2.hasNext())
/*    */     {
/* 45 */       Product product = (Product)localIterator1.next();
/*    */ 
/* 47 */       System.out.println("Product: " + nn++);
/* 48 */       System.out.println("Name = " + product.getName());
/* 49 */       System.out.println("Forecaster = " + product.getForecaster());
/*    */ 
/* 52 */       mm = 0;
/* 53 */       List listOfLayers = product.getLayer();
/* 54 */       localIterator2 = listOfLayers.iterator(); continue; Layer layer = (Layer)localIterator2.next();
/*    */ 
/* 56 */       System.out.println("\tLayer:" + mm++);
/* 57 */       System.out.println("\t\tName:" + layer.getName());
/*    */ 
/* 59 */       Color color1 = layer.getColor();
/* 60 */       System.out.println("\t\tColor:");
/* 61 */       System.out.println("\t\t\tRed = " + color1.getRed());
/* 62 */       System.out.println("\t\t\tGreen = " + color1.getGreen());
/* 63 */       System.out.println("\t\t\tBlue = " + color1.getBlue());
/* 64 */       System.out.println("\t\t\tAlpha = " + color1.getAlpha());
/*    */ 
/* 66 */       DrawableElement DE = layer.getDrawableElement();
/* 67 */       kk = 0;
/* 68 */       List listOfLines = DE.getLine();
/* 69 */       for (Iterator localIterator3 = listOfLines.iterator(); localIterator3.hasNext(); 
/* 90 */         ???.hasNext())
/*    */       {
/* 69 */         Line line = (Line)localIterator3.next();
/* 70 */         System.out.println("\t\t\t\tline " + kk++ + "Category:" + line.getPgenCategory());
/* 71 */         System.out.println("\t\t\t\tlineWidth:" + line.getLineWidth());
/* 72 */         System.out.println("\t\t\t\tsizeScale:" + line.getSizeScale());
/* 73 */         System.out.println("\t\t\t\tfilled:" + line.isFilled());
/* 74 */         System.out.println("\t\t\t\tclosed:" + line.isClosed());
/* 75 */         System.out.println("\t\t\t\tsmoothFactor:" + line.getSmoothFactor());
/* 76 */         System.out.println("\t\t\t\tfillPattern:" + line.getFillPattern());
/* 77 */         System.out.println("\t\t\t\tlinePattern:" + line.getPgenType());
/*    */ 
/* 79 */         List listOfColors = line.getColor();
/* 80 */         System.out.println("\t\t\t\tColor:");
/* 81 */         for (Color color : listOfColors) {
/* 82 */           System.out.println("\t\t\t\tRed = " + color.getRed());
/* 83 */           System.out.println("\t\t\t\tGreen = " + color.getGreen());
/* 84 */           System.out.println("\t\t\t\tBlue = " + color.getBlue());
/* 85 */           System.out.println("\t\t\t\tAlpha = " + color.getAlpha());
/*    */         }
/*    */ 
/* 88 */         List listOfPoints = line.getPoint();
/* 89 */         System.out.println("\t\t\t\tPoints:");
/* 90 */         ??? = listOfPoints.iterator(); continue; Point point = (Point)???.next();
/* 91 */         System.out.println("\t\t\t\tLat = " + point.getLat());
/* 92 */         System.out.println("\t\t\t\tLon = " + point.getLon());
/*    */       }
/*    */ 
/* 96 */       kk = 0;
/* 97 */       List listOfSymbols = DE.getSymbol();
/* 98 */       for (Symbol symbol : listOfSymbols) {
/* 99 */         System.out.println("\t\t\t\tSymbol " + kk++ + "Type:" + symbol.getPgenType());
/* 100 */         System.out.println("\t\t\t\tlineWidth:" + symbol.getLineWidth());
/* 101 */         System.out.println("\t\t\t\tsizeScale:" + symbol.getSizeScale());
/* 102 */         System.out.println("\t\t\t\tcleared:" + symbol.isClear());
/*    */ 
/* 104 */         List listOfColors = symbol.getColor();
/* 105 */         System.out.println("\t\t\t\tColor:");
/* 106 */         for (Color color : listOfColors) {
/* 107 */           System.out.println("\t\t\t\tRed = " + color.getRed());
/* 108 */           System.out.println("\t\t\t\tGreen = " + color.getGreen());
/* 109 */           System.out.println("\t\t\t\tBlue = " + color.getBlue());
/* 110 */           System.out.println("\t\t\t\tAlpha = " + color.getAlpha());
/*    */         }
/*    */ 
/* 113 */         System.out.println("\t\t\t\tLocation:");
/* 114 */         System.out.println("\t\t\t\tLat = " + symbol.getPoint().getLat());
/* 115 */         System.out.println("\t\t\t\tLon = " + symbol.getPoint().getLon());
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.file.OpenXML
 * JD-Core Version:    0.6.2
 */