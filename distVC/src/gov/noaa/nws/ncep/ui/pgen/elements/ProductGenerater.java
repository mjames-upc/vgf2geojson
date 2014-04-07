/*     */ package gov.noaa.nws.ncep.ui.pgen.elements;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Scanner;
/*     */ 
/*     */ public class ProductGenerater
/*     */ {
/*     */   static ArrayList<Product> productManager;
/*     */ 
/*     */   static void start()
/*     */   {
/*  61 */     productManager = new ArrayList();
/*     */ 
/*  63 */     System.out.println("Started Product/Layer/Drawbale managers......\n");
/*     */   }
/*     */ 
/*     */   static Product generateOneProduct()
/*     */   {
/*  72 */     Scanner stdin = new Scanner(System.in);
/*     */ 
/*  74 */     System.out.println("Enter the product name:");
/*  75 */     String name = stdin.nextLine();
/*     */ 
/*  77 */     System.out.println("Enter the product type:");
/*  78 */     String type = stdin.nextLine();
/*     */ 
/*  80 */     System.out.println("Enter the forecaster name:");
/*  81 */     String forecaster = stdin.nextLine();
/*     */ 
/*  83 */     ProductInfo info = new ProductInfo();
/*  84 */     ProductTime time = new ProductTime();
/*  85 */     ArrayList layers = new ArrayList(0);
/*     */ 
/*  87 */     return new Product(name, type, forecaster, info, time, layers);
/*     */   }
/*     */ 
/*     */   static Layer generateOneLayer()
/*     */   {
/*  96 */     Scanner stdin = new Scanner(System.in);
/*     */ 
/*  98 */     System.out.println("Enter the layer name:");
/*  99 */     String name = stdin.nextLine();
/*     */ 
/* 101 */     System.out.println("Enter onOff flag (0 or 1):");
/* 102 */     boolean onOff = stdin.nextInt() != 0;
/*     */ 
/* 104 */     System.out.println("Enter the color mode (0 or 1):");
/* 105 */     boolean colorMode = stdin.nextInt() != 0;
/*     */ 
/* 107 */     System.out.println("Enter the color:");
/* 108 */     System.out.println("1 - Red\t2 - Green\t3 - Blue");
/*     */ 
/* 110 */     int col = stdin.nextInt();
/* 111 */     Color color = Color.red;
/* 112 */     if (col == 2) {
/* 113 */       color = Color.green;
/*     */     }
/* 115 */     else if (col == 3) {
/* 116 */       color = Color.blue;
/*     */     }
/*     */ 
/* 119 */     System.out.println("Enter the fill mode (0 or 1):");
/* 120 */     boolean fillMode = stdin.nextInt() != 0;
/*     */ 
/* 122 */     ArrayList drawables = new ArrayList();
/*     */ 
/* 124 */     return new Layer(name, onOff, colorMode, color, fillMode, drawables);
/*     */   }
/*     */ 
/*     */   static DrawableElement generateOneDrawable()
/*     */   {
/* 132 */     Scanner stdin = new Scanner(System.in);
/*     */ 
/* 134 */     System.out.println("Please select a Drawable type:");
/* 135 */     for (DrawableType dt : DrawableType.values()) {
/* 136 */       System.out.println(dt.ordinal() + " - " + dt + "\t");
/*     */     }
/*     */ 
/* 139 */     int classType = stdin.nextInt();
/* 140 */     DrawableType dtType = DrawableType.ANY;
/* 141 */     for (DrawableType dt : DrawableType.values()) {
/* 142 */       if (dt.ordinal() == classType) {
/* 143 */         dtType = dt;
/* 144 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 148 */     AttributeGenerator newAttribute = new AttributeGenerator(dtType);
/* 149 */     DrawableElementFactory df = new DrawableElementFactory();
/*     */ 
/* 151 */     AbstractDrawableComponent element = null;
/* 152 */     if (dtType == DrawableType.SYMBOL) {
/* 153 */       element = df.create(dtType, newAttribute, "Symbol", 
/* 154 */         newAttribute.getType(), 
/* 155 */         newAttribute.getLocation(), null);
/*     */     }
/* 157 */     else if ((dtType == DrawableType.KINKLINE) || 
/* 158 */       (dtType == DrawableType.LINE)) {
/* 159 */       element = df.create(dtType, newAttribute, "Lines", 
/* 160 */         newAttribute.getLinePattern(), 
/* 161 */         newAttribute.getLinePoints(), null);
/*     */     }
/*     */ 
/* 164 */     return (DrawableElement)element;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/* 176 */     System.out.println("Start Product Generation......\n");
/*     */ 
/* 178 */     start();
/*     */ 
/* 180 */     System.out.println("\nDo you want to generate a new product (Yes/No)?");
/*     */ 
/* 182 */     Scanner stdin = new Scanner(System.in);
/*     */ 
/* 184 */     while ((stdin.hasNext()) && (stdin.next().equalsIgnoreCase("Yes")))
/*     */     {
/* 186 */       Product oneProduct = generateOneProduct();
/*     */ 
/* 188 */       System.out.println("Add a new Layer for this Product(Yes/No)?");
/* 189 */       while ((stdin.hasNext()) && (stdin.next().equalsIgnoreCase("Yes"))) {
/* 190 */         Layer oneLayer = generateOneLayer();
/* 191 */         oneProduct.addLayer(oneLayer);
/*     */ 
/* 193 */         System.out.println("Add a new Drawable for this Layer(Yes/No)?");
/*     */ 
/* 195 */         while ((stdin.hasNext()) && (stdin.next().equalsIgnoreCase("Yes"))) {
/* 196 */           oneLayer.addElement(generateOneDrawable());
/* 197 */           System.out.println("Add another Drawable for this Layer(Yes/No)?");
/*     */         }
/*     */ 
/* 200 */         System.out.println("Add another Layer for this Product(Yes/No)?");
/*     */       }
/*     */ 
/* 203 */       productManager.add(oneProduct);
/*     */ 
/* 205 */       System.out.println("Do you want to generate another product (Yes/No)?");
/*     */     }
/*     */ 
/* 209 */     System.out.println("\nTotal of " + productManager.size() + " product in PGEN:");
/* 210 */     for (Product prd : productManager) {
/* 211 */       System.out.println(prd);
/*     */     }
/*     */ 
/* 214 */     System.out.println("\nDo you want to EXIT product generation (Yes/No)?");
/* 215 */     if ((stdin.hasNext()) && (stdin.next().equalsIgnoreCase("Yes"))) {
/* 216 */       System.out.println("\nExited Product Generation......");
/* 217 */       stdin.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.elements.ProductGenerater
 * JD-Core Version:    0.6.2
 */