/*     */ package gov.noaa.nws.ncep.standalone.vgfConverter;
/*     */ 
/*     */ import com.sun.jna.Native;
/*     */ import gov.noaa.nws.ncep.standalone.util.Util;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.Products;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Convert
/*     */ {
/*     */   public int convertMap(String in, String out, String activity, String subActivity, String contTbl)
/*     */     throws IOException
/*     */   {
/*  40 */     List vgfFiles = new ArrayList();
/*  41 */     int counter = 0;
/*  42 */     String outFile = null;
/*     */ 
/*  44 */     if ((in.endsWith("*")) || (in.endsWith("*.vgf"))) {
/*  45 */       in = in.substring(0, in.lastIndexOf("/"));
/*     */     }
/*     */ 
/*  48 */     File vgfDir = new File(in);
/*  49 */     boolean oneFileIn = false;
/*  50 */     if (in.endsWith(".vgf")) {
/*  51 */       vgfFiles.add(vgfDir);
/*  52 */       oneFileIn = true;
/*     */     }
/*     */     else
/*     */     {
/*  56 */       FilenameFilter filter = new FilenameFilter() {
/*     */         public boolean accept(File dir, String name) {
/*  58 */           return name.endsWith(".vgf");
/*     */         }
/*     */       };
/*  61 */       File[] files = vgfDir.listFiles(filter);
/*     */ 
/*  63 */       if ((files == null) || (files.length == 0)) {
/*  64 */         System.out.println("The vgf file does not exist.");
/*  65 */         return 0;
/*     */       }
/*     */ 
/*  68 */       for (int x = 0; x < files.length; x++) {
/*  69 */         vgfFiles.add(files[x]);
/*     */       }
/*     */     }
/*     */ 
/*  73 */     if (vgfFiles != null) {
/*  74 */       for (int i = 0; i < vgfFiles.size(); i++) {
/*  75 */         File theFile = (File)vgfFiles.get(i);
/*  76 */         if (theFile.length() != 0L)
/*     */         {
/*  78 */           String f = theFile.getAbsolutePath();
/*     */ 
/*  80 */           if ((oneFileIn) && (out.endsWith(".xml"))) {
/*  81 */             if (out.contains("/")) {
/*  82 */               outFile = new String(out);
/*     */             }
/*     */             else
/*  85 */               outFile = new String("./" + out);
/*     */           }
/*     */           else
/*     */           {
/*  89 */             String s = theFile.getName();
/*  90 */             String s1 = s.substring(0, s.lastIndexOf("."));
/*  91 */             s = s1 + ".xml";
/*     */ 
/*  93 */             if (out.endsWith("//")) {
/*  94 */               outFile = out.substring(0, out.lastIndexOf("/")) + s;
/*     */             }
/*  96 */             else if (out.endsWith("/")) {
/*  97 */               outFile = out + s;
/*     */             }
/*     */             else {
/* 100 */               outFile = out + "/" + s;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 105 */           WrapperC.VgfXml wrap = (WrapperC.VgfXml)Native.loadLibrary("VgfXml", WrapperC.VgfXml.class);
/* 106 */           wrap.vgfToXml(f, outFile, activity, subActivity, contTbl);
/*     */ 
/* 109 */           Products convertedRight = Util.read(outFile);
/* 110 */           if (convertedRight != null) {
/* 111 */             counter++;
/* 112 */             System.out.println("The file " + f + " is converted to " + outFile);
/*     */           }
/*     */         }
/*     */         else {
/* 116 */           System.out.println("***The vgf file " + theFile + " is 0 length and is not converted.");
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 121 */     if (counter > 1)
/* 122 */       System.out.println(counter + " files are converted.  " + "The Conversion is finished.\n");
/* 123 */     else if (counter <= 1) {
/* 124 */       System.out.println(counter + " file is converted.  " + "The Conversion is finished.\n");
/*     */     }
/* 126 */     return counter;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args) throws IOException {
/* 130 */     String fileName = "";
/* 131 */     String activity = "";
/* 132 */     String subActivity = "";
/*     */ 
/* 134 */     if ((args.length == 0) || (args.length == 1)) {
/* 135 */       System.out.println("Please specify the source and the destination.\n");
/* 136 */       return;
/*     */     }
/*     */ 
/* 139 */     if (!new File(args[0]).exists()) {
/* 140 */       System.out.println("The Source directory or file does not exist.\n");
/* 141 */       return;
/*     */     }
/*     */ 
/* 147 */     if ((!args[0].endsWith(".vgf")) || (!args[1].endsWith(".xml"))) {
/* 148 */       File des = new File(args[1]);
/* 149 */       if ((!des.exists()) && 
/* 150 */         (!des.mkdirs())) {
/* 151 */         System.out.println("The Destination directory does not exist.\n");
/* 152 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 157 */     for (int i = 0; i < args.length; i++) {
/* 158 */       if ((args[i].equalsIgnoreCase("-t")) && 
/* 159 */         (i + 1 < args.length) && (args[(i + 1)] != null) && (!args[(i + 1)].equalsIgnoreCase("-a"))) {
/* 160 */         fileName = args[(i + 1)];
/*     */       }
/* 162 */       if ((args[i].equalsIgnoreCase("-a")) && 
/* 163 */         (i + 1 < args.length) && (args[(i + 1)] != null) && (!args[(i + 1)].equalsIgnoreCase("-t"))) {
/* 164 */         activity = args[(i + 1)];
/* 165 */         if ((i + 2 < args.length) && (args[(i + 2)] != null) && (!args[(i + 2)].equalsIgnoreCase("-t"))) {
/* 166 */           subActivity = args[(i + 2)];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 171 */     new Convert().convertMap(args[0], args[1], activity, subActivity, fileName);
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.standalone.vgfConverter.Convert
 * JD-Core Version:    0.6.2
 */