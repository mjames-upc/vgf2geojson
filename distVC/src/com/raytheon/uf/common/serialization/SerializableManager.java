/*     */ package com.raytheon.uf.common.serialization;
/*     */ 
/*     */ import com.raytheon.uf.common.serialization.jaxb.JaxbDummyObject;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.Set;
/*     */ import javax.persistence.Embeddable;
/*     */ import javax.persistence.Entity;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlRegistry;
/*     */ 
/*     */ public class SerializableManager
/*     */   implements IJaxbableClassesLocator
/*     */ {
/*     */   private static SerializableManager instance;
/*  75 */   private final Map<String, List<Class<ISerializableObject>>> hibernatables = new HashMap();
/*     */ 
/*  77 */   private ArrayList<Class<ISerializableObject>> jaxbables = new ArrayList();
/*     */ 
/*     */   private synchronized void initialize()
/*     */   {
/*  93 */     this.hibernatables.clear();
/*  94 */     this.jaxbables.clear();
/*  95 */     long realStartTime = System.currentTimeMillis();
/*  96 */     Set clazzSet = new HashSet(
/*  97 */       500);
/*     */     try {
/*  99 */       Enumeration urls = SerializableManager.class.getClassLoader()
/* 100 */         .getResources(
/* 101 */         "META-INF/services/" + 
/* 102 */         ISerializableObject.class.getName());
/*     */ 
/* 105 */       boolean doHibernate = Boolean.getBoolean("initializeHibernatables");
/*     */ 
/* 110 */       int numThreads = 3;
/* 111 */       LoadSerializableClassesThread[] threads = new LoadSerializableClassesThread[numThreads];
/* 112 */       for (int i = 0; i < numThreads; i++) {
/* 113 */         threads[i] = new LoadSerializableClassesThread(urls, 
/* 114 */           doHibernate);
/* 115 */         threads[i].start();
/*     */       }
/*     */ 
/* 118 */       for (LoadSerializableClassesThread thread : threads) {
/* 119 */         thread.join();
/* 120 */         clazzSet.addAll(thread.getClazzList());
/*     */ 
/* 122 */         if (doHibernate)
/*     */         {
/* 124 */           Iterator localIterator = thread
/* 124 */             .getHibernatables().entrySet().iterator();
/*     */ 
/* 123 */           while (localIterator.hasNext()) {
/* 124 */             Map.Entry entry = (Map.Entry)localIterator.next();
/* 125 */             List list = 
/* 126 */               (List)this.hibernatables
/* 126 */               .get(entry.getKey());
/* 127 */             if (list == null) {
/* 128 */               list = new ArrayList();
/* 129 */               this.hibernatables.put((String)entry.getKey(), list);
/*     */             }
/* 131 */             list.addAll((Collection)entry.getValue());
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (Throwable e) {
/* 136 */       e.printStackTrace();
/*     */     }
/* 138 */     this.jaxbables = new ArrayList(
/* 139 */       clazzSet.size() + 1);
/*     */ 
/* 142 */     Class jaxb = JaxbDummyObject.class;
/* 143 */     this.jaxbables.add(jaxb);
/* 144 */     this.jaxbables.addAll(clazzSet);
/* 145 */     this.jaxbables.trimToSize();
/*     */ 
/* 147 */     System.out.println("Total time spent loading classes: " + (
/* 148 */       System.currentTimeMillis() - realStartTime) + "ms");
/*     */   }
/*     */ 
/*     */   private static void fail(Class service, String msg, Throwable cause)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 155 */     throw new ServiceConfigurationError(service.getName() + ": " + msg, 
/* 156 */       cause);
/*     */   }
/*     */ 
/*     */   private static void fail(Class service, String msg)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 162 */     throw new ServiceConfigurationError(service.getName() + ": " + msg);
/*     */   }
/*     */ 
/*     */   private static void fail(Class service, URL u, int line, String msg)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 168 */     fail(service, u + ":" + line + ": " + msg);
/*     */   }
/*     */ 
/*     */   private static int parseLine(Class service, URL u, BufferedReader r, int lc, List<String> names)
/*     */     throws IOException, ServiceConfigurationError
/*     */   {
/* 175 */     String ln = r.readLine();
/* 176 */     if (ln == null) {
/* 177 */       return -1;
/*     */     }
/* 179 */     int ci = ln.indexOf('#');
/* 180 */     if (ci >= 0) {
/* 181 */       ln = ln.substring(0, ci);
/*     */     }
/* 183 */     ln = ln.trim();
/* 184 */     int n = ln.length();
/* 185 */     if (n != 0) {
/* 186 */       if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0)) {
/* 187 */         fail(service, u, lc, "Illegal configuration-file syntax");
/*     */       }
/* 189 */       int cp = ln.codePointAt(0);
/* 190 */       if (!Character.isJavaIdentifierStart(cp)) {
/* 191 */         fail(service, u, lc, "Illegal provider-class name: " + ln);
/*     */       }
/* 193 */       for (int i = Character.charCount(cp); i < n; 
/* 194 */         i = i + 
/* 194 */         Character.charCount(cp)) {
/* 195 */         cp = ln.codePointAt(i);
/* 196 */         if ((!Character.isJavaIdentifierPart(cp)) && (cp != 46)) {
/* 197 */           fail(service, u, lc, "Illegal provider-class name: " + ln);
/*     */         }
/*     */       }
/*     */ 
/* 201 */       names.add(ln);
/*     */     }
/* 203 */     return lc + 1;
/*     */   }
/*     */ 
/*     */   public static synchronized SerializableManager getInstance()
/*     */   {
/* 212 */     if (instance == null) {
/* 213 */       instance = new SerializableManager();
/* 214 */       instance.initialize();
/*     */     }
/* 216 */     return instance;
/*     */   }
/*     */ 
/*     */   public List<Class<ISerializableObject>> getHibernatablesForPluginFQN(String pluginFQN)
/*     */   {
/* 226 */     List list = (List)this.hibernatables.get(pluginFQN);
/* 227 */     if (list == null) {
/* 228 */       list = Collections.emptyList();
/*     */     }
/* 230 */     return list;
/*     */   }
/*     */ 
/*     */   public Set<String> getHibernatablePluginFQNs()
/*     */   {
/* 237 */     return this.hibernatables.keySet();
/*     */   }
/*     */ 
/*     */   public List<Class<ISerializableObject>> getHibernatables()
/*     */   {
/* 246 */     List rval = new ArrayList();
/* 247 */     for (List list : this.hibernatables.values()) {
/* 248 */       rval.addAll(list);
/*     */     }
/* 250 */     return rval;
/*     */   }
/*     */ 
/*     */   public List<Class<ISerializableObject>> getJaxbables()
/*     */   {
/* 260 */     return this.jaxbables;
/*     */   }
/*     */ 
/*     */   private static class LoadSerializableClassesThread extends Thread
/*     */   {
/*     */     private final Enumeration<URL> urls;
/*     */     private final boolean doHibernate;
/*     */     private final List<Class<ISerializableObject>> clazzList;
/*     */     private final Map<String, List<Class<ISerializableObject>>> hibernatables;
/*     */ 
/*     */     public LoadSerializableClassesThread(Enumeration<URL> urls, boolean doHibernate)
/*     */     {
/* 275 */       this.urls = urls;
/* 276 */       this.doHibernate = doHibernate;
/* 277 */       this.clazzList = new ArrayList(500);
/* 278 */       this.hibernatables = new HashMap();
/*     */     }
/*     */ 
/*     */     public List<Class<ISerializableObject>> getClazzList() {
/* 282 */       return this.clazzList;
/*     */     }
/*     */ 
/*     */     public Map<String, List<Class<ISerializableObject>>> getHibernatables() {
/* 286 */       return this.hibernatables;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try {
/* 292 */         ClassLoader cl = getClass().getClassLoader();
/* 293 */         Set pluginHibernateSet = null;
/* 294 */         if (this.doHibernate) {
/* 295 */           pluginHibernateSet = new HashSet();
/*     */         }
/* 297 */         List names = new ArrayList();
/* 298 */         URL u = getNextUrl();
/* 299 */         while (u != null) {
/* 300 */           InputStream in = null;
/* 301 */           BufferedReader r = null;
/* 302 */           names.clear();
/* 303 */           String path = u.getPath();
/* 304 */           int endIndex = path.indexOf(".jar");
/* 305 */           if (endIndex < 0) {
/* 306 */             endIndex = path.length();
/*     */           }
/* 308 */           path = path.substring(0, endIndex);
/* 309 */           int startIndex = path.lastIndexOf("/");
/* 310 */           if (startIndex < 0) {
/* 311 */             startIndex = path.lastIndexOf(":");
/*     */           }
/* 313 */           startIndex++;
/* 314 */           String pluginFQN = path.substring(startIndex);
/*     */           try {
/* 317 */             in = u.openStream();
/* 318 */             r = new BufferedReader(new InputStreamReader(in, 
/* 319 */               "utf-8"));
/* 320 */             int lc = 1;
/* 321 */             while ((lc = SerializableManager.parseLine(ISerializableObject.class, u, r, 
/* 322 */               lc, names)) >= 0);
/*     */           } catch (IOException x) {
/* 326 */             SerializableManager.fail(ISerializableObject.class, 
/* 327 */               "Error reading configuration file", x);
/*     */             try
/*     */             {
/* 330 */               if (r != null) {
/* 331 */                 r.close();
/*     */               }
/* 333 */               if (in != null)
/* 334 */                 in.close();
/*     */             }
/*     */             catch (IOException y) {
/* 337 */               SerializableManager.fail(ISerializableObject.class, 
/* 338 */                 "Error closing configuration file", y);
/*     */             }
/*     */           }
/*     */           finally
/*     */           {
/*     */             try
/*     */             {
/* 330 */               if (r != null) {
/* 331 */                 r.close();
/*     */               }
/* 333 */               if (in != null)
/* 334 */                 in.close();
/*     */             }
/*     */             catch (IOException y) {
/* 337 */               SerializableManager.fail(ISerializableObject.class, 
/* 338 */                 "Error closing configuration file", y);
/*     */             }
/*     */           }
/*     */ 
/* 342 */           if (this.doHibernate) {
/* 343 */             pluginHibernateSet.clear();
/*     */           }
/*     */ 
/* 346 */           Iterator iter = names.iterator();
/* 347 */           while (iter.hasNext()) {
/* 348 */             String clazz = (String)iter.next();
/*     */             try {
/* 350 */               long t0 = System.currentTimeMillis();
/*     */ 
/* 352 */               Class c = 
/* 353 */                 Class.forName(clazz, true, cl);
/* 354 */               boolean added = false;
/* 355 */               if ((c.getAnnotation(XmlAccessorType.class) != null) || 
/* 356 */                 (c.getAnnotation(XmlRegistry.class) != null)) {
/* 357 */                 this.clazzList.add(c);
/* 358 */                 added = true;
/*     */               }
/*     */ 
/* 361 */               if ((this.doHibernate) && (
/* 362 */                 (c.getAnnotation(Entity.class) != null) || 
/* 363 */                 (c.getAnnotation(Embeddable.class) != null))) {
/* 364 */                 pluginHibernateSet.add(c);
/* 365 */                 added = true;
/*     */               }
/*     */ 
/* 369 */               long time = System.currentTimeMillis() - t0;
/* 370 */               if ((this.doHibernate) && (!added))
/* 371 */                 System.out
/* 372 */                   .println("Class: " + 
/* 373 */                   clazz + 
/* 374 */                   " should not be in ISerializableObject file, wasted " + 
/* 375 */                   time + "ms processing it!");
/*     */             }
/*     */             catch (ClassNotFoundException e) {
/* 378 */               System.out
/* 379 */                 .println("Unable to load class " + 
/* 380 */                 clazz + 
/* 381 */                 ".  Check that class is spelled correctly in ISerializableObject file");
/*     */             }
/*     */           }
/*     */ 
/* 385 */           if ((this.doHibernate) && (pluginHibernateSet.size() > 0)) {
/* 386 */             this.hibernatables.put(pluginFQN, 
/* 387 */               new ArrayList(
/* 388 */               pluginHibernateSet));
/*     */           }
/* 390 */           u = getNextUrl();
/*     */         }
/*     */       } catch (Throwable e) {
/* 393 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/*     */     private URL getNextUrl() {
/* 398 */       synchronized (this.urls) {
/* 399 */         if (this.urls.hasMoreElements()) {
/* 400 */           return (URL)this.urls.nextElement();
/*     */         }
/* 402 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.SerializableManager
 * JD-Core Version:    0.6.2
 */