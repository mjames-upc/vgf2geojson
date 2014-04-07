/*     */ package com.raytheon.uf.common.serialization.util;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.DynamicSerializationManager;
/*     */ import com.raytheon.uf.common.serialization.DynamicSerializationManager.SerializationMetadata;
/*     */ import com.raytheon.uf.common.serialization.annotations.DynamicSerialize;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ThriftFileGenerator
/*     */ {
/*  51 */   private static Set<Class<?>> pendingClasses = new HashSet();
/*     */ 
/*  53 */   private static Set<Class<?>> completedClasses = new HashSet();
/*     */ 
/*     */   public static void main(String[] args) {
/*  56 */     if (args.length != 1) {
/*  57 */       System.out.println("Provide a class name to inspect");
/*  58 */       System.exit(0);
/*     */     }
/*     */ 
/*  61 */     StringBuffer sb = new StringBuffer();
/*  62 */     Class c = null;
/*     */     try {
/*  64 */       c = Class.forName(args[0]);
/*     */     } catch (ClassNotFoundException e1) {
/*  66 */       e1.printStackTrace();
/*  67 */       System.out.println("Class not found");
/*  68 */       System.exit(0);
/*     */     }
/*     */ 
/*  71 */     buildThriftForClass(sb, c);
/*     */     Iterator localIterator;
/*  73 */     for (; pendingClasses.size() != 0; 
/*  74 */       localIterator.hasNext()) { localIterator = new HashSet(pendingClasses).iterator(); continue; Class pending = (Class)localIterator.next();
/*  75 */       buildThriftForClass(sb, pending);
/*  76 */       completedClasses.add(pending);
/*  77 */       pendingClasses.remove(pending);
/*     */     }
/*     */ 
/*  81 */     System.out.println(sb.toString());
/*     */   }
/*     */ 
/*     */   private static void buildThriftForClass(StringBuffer sb, Class<?> c)
/*     */   {
/*  86 */     DynamicSerializationManager.SerializationMetadata md = DynamicSerializationManager.inspect(c);
/*     */ 
/*  88 */     if (md.serializationFactory != null) {
/*  89 */       System.out
/*  90 */         .println(c.getName() + 
/*  91 */         ":: This class uses serialization factory.  Not currently supported");
/*  92 */       return;
/*     */     }
/*     */ 
/*  95 */     sb.append("struct ");
/*  96 */     sb.append(c.getName().replace(".", "_"));
/*  97 */     sb.append(" {\n");
/*     */ 
/*  99 */     if (md.serializedAttributes != null) {
/* 100 */       int i = 1;
/* 101 */       for (String attribute : md.serializedAttributes) {
/*     */         try {
/* 103 */           Field field = null;
/* 104 */           Class classToSearch = c;
/*     */           do
/*     */             try {
/* 107 */               field = classToSearch.getDeclaredField(attribute);
/*     */             } catch (Exception e) {
/* 109 */               classToSearch = classToSearch.getSuperclass();
/* 110 */               if (classToSearch == null) {
/* 111 */                 System.out.println("Failed to find field: " + 
/* 112 */                   attribute);
/* 113 */                 System.exit(0);
/*     */               }
/*     */             }
/* 116 */           while (field == null);
/*     */ 
/* 118 */           Class declaredClazz = field.getType();
/* 119 */           String type = lookupType(declaredClazz);
/* 120 */           if (type != null) {
/* 121 */             if (i != 1) {
/* 122 */               sb.append(",\n");
/*     */             }
/* 124 */             sb.append(i + ": " + type + " " + attribute);
/* 125 */             i++;
/*     */           } else {
/* 127 */             System.out.println("Missing: " + attribute + ":: " + 
/* 128 */               declaredClazz);
/*     */           }
/*     */         }
/*     */         catch (Exception e) {
/* 132 */           e.printStackTrace();
/* 133 */           System.out.println("Error generating schema for field");
/* 134 */           System.exit(0);
/*     */         }
/*     */       }
/*     */ 
/* 138 */       sb.append("\n}\n");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String lookupType(Class<?> declaredClazz)
/*     */   {
/* 145 */     String type = null;
/* 146 */     if ((declaredClazz == Double.class) || (declaredClazz == Double.TYPE)) {
/* 147 */       type = "double";
/* 148 */     } else if (declaredClazz == String.class) {
/* 149 */       type = "string";
/* 150 */     } else if ((declaredClazz == Boolean.class) || 
/* 151 */       (declaredClazz == Boolean.TYPE)) {
/* 152 */       type = "bool";
/* 153 */     } else if ((declaredClazz == Byte.class) || (declaredClazz == Byte.TYPE)) {
/* 154 */       type = "byte";
/* 155 */     } else if ((declaredClazz == Integer.class) || 
/* 156 */       (declaredClazz == Integer.TYPE)) {
/* 157 */       type = "i32";
/* 158 */     } else if ((declaredClazz == Short.class) || (declaredClazz == Short.TYPE)) {
/* 159 */       type = "i16";
/* 160 */     } else if ((declaredClazz == Long.class) || (declaredClazz == Long.TYPE)) {
/* 161 */       type = "i64";
/* 162 */     } else if (declaredClazz.isArray()) {
/* 163 */       String subType = lookupType(declaredClazz.getComponentType());
/*     */ 
/*  21 */       if (subType != null)
/*     */       {
/* 165 */         type = "list<" + subType + ">";
/*     */       }
/* 167 */     } else if (declaredClazz.getAnnotation(DynamicSerialize.class) != null) {
/* 168 */       type = declaredClazz.getName().replace(".", "_");
/* 169 */       if (!completedClasses.contains(declaredClazz)) {
/* 170 */         pendingClasses.add(declaredClazz);
/*     */       }
/*     */     }
/*     */ 
/* 174 */     return type;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.util.ThriftFileGenerator
 * JD-Core Version:    0.6.2
 */