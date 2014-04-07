/*      */ package com.raytheon.uf.common.serialization.thrift;
/*      */ 
/*      */ import com.facebook.thrift.TException;
/*      */ import com.facebook.thrift.protocol.TField;
/*      */ import com.facebook.thrift.protocol.TList;
/*      */ import com.facebook.thrift.protocol.TMap;
/*      */ import com.facebook.thrift.protocol.TMessage;
/*      */ import com.facebook.thrift.protocol.TSet;
/*      */ import com.facebook.thrift.protocol.TStruct;
/*      */ import com.raytheon.uf.common.serialization.BaseSerializationContext;
/*      */ import com.raytheon.uf.common.serialization.DynamicSerializationManager;
/*      */ import com.raytheon.uf.common.serialization.DynamicSerializationManager.EnclosureType;
/*      */ import com.raytheon.uf.common.serialization.DynamicSerializationManager.SerializationMetadata;
/*      */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*      */ import com.raytheon.uf.common.serialization.SerializationCache;
/*      */ import com.raytheon.uf.common.serialization.SerializationException;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Field;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import net.sf.cglib.beans.BeanMap;
/*      */ import net.sf.cglib.reflect.FastClass;
/*      */ 
/*      */ public class ThriftSerializationContext extends BaseSerializationContext
/*      */ {
/*      */   private static final String ENUM_VALUE_TAG = "__enumValue__";
/*      */   private final SelfDescribingBinaryProtocol protocol;
/*      */   private static Map<Class<?>, Byte> types;
/*   88 */   private static Map<String, Class<?>> fieldClass = new ConcurrentHashMap();
/*      */ 
/*      */   static
/*      */   {
/*   92 */     types = new HashMap();
/*   93 */     types.put(String.class, Byte.valueOf((byte)11));
/*   94 */     types.put(Integer.class, Byte.valueOf((byte)8));
/*   95 */     types.put(Integer.TYPE, Byte.valueOf((byte)8));
/*   96 */     types.put(Long.class, Byte.valueOf((byte)10));
/*   97 */     types.put(Long.TYPE, Byte.valueOf((byte)10));
/*   98 */     types.put(Short.class, Byte.valueOf((byte)6));
/*   99 */     types.put(Short.TYPE, Byte.valueOf((byte)6));
/*  100 */     types.put(Byte.class, Byte.valueOf((byte)3));
/*  101 */     types.put(Byte.TYPE, Byte.valueOf((byte)3));
/*  102 */     types.put(Float.class, Byte.valueOf((byte)64));
/*  103 */     types.put(Float.TYPE, Byte.valueOf((byte)64));
/*  104 */     types.put(Double.class, Byte.valueOf((byte)4));
/*  105 */     types.put(Double.TYPE, Byte.valueOf((byte)4));
/*  106 */     types.put(Boolean.class, Byte.valueOf((byte)2));
/*  107 */     types.put(Boolean.TYPE, Byte.valueOf((byte)2));
/*      */   }
/*      */ 
/*      */   public ThriftSerializationContext(SelfDescribingBinaryProtocol protocol, DynamicSerializationManager serializationManager)
/*      */   {
/*  118 */     super(serializationManager);
/*      */ 
/*  120 */     this.protocol = protocol;
/*      */   }
/*      */ 
/*      */   public byte[] readBinary()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  131 */       return this.protocol.readBinary();
/*      */     } catch (TException e) {
/*  133 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean readBool()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  145 */       return this.protocol.readBool();
/*      */     } catch (TException e) {
/*  147 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte readByte()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  159 */       return this.protocol.readByte();
/*      */     } catch (TException e) {
/*  161 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double readDouble()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  173 */       return this.protocol.readDouble();
/*      */     } catch (TException e) {
/*  175 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float readFloat()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  187 */       return this.protocol.readFloat();
/*      */     } catch (TException e) {
/*  189 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short readI16()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  201 */       return this.protocol.readI16();
/*      */     } catch (TException e) {
/*  203 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int readI32()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  215 */       return this.protocol.readI32();
/*      */     } catch (TException e) {
/*  217 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long readI64()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  229 */       return this.protocol.readI64();
/*      */     } catch (TException e) {
/*  231 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String readString()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  243 */       return this.protocol.readString();
/*      */     } catch (TException e) {
/*  245 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeBinary(byte[] arg0)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  258 */       this.protocol.writeBinary(arg0);
/*      */     } catch (TException e) {
/*  260 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeBool(boolean arg0)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  272 */       this.protocol.writeBool(arg0);
/*      */     } catch (TException e) {
/*  274 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeByte(byte arg0)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  286 */       this.protocol.writeByte(arg0);
/*      */     } catch (TException e) {
/*  288 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeDouble(double arg0)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  301 */       this.protocol.writeDouble(arg0);
/*      */     } catch (TException e) {
/*  303 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeFloat(float arg0)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  315 */       this.protocol.writeFloat(arg0);
/*      */     } catch (TException e) {
/*  317 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeI16(short arg0)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  329 */       this.protocol.writeI16(arg0);
/*      */     } catch (TException e) {
/*  331 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeI32(int arg0)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  343 */       this.protocol.writeI32(arg0);
/*      */     } catch (TException e) {
/*  345 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeI64(long arg0)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  357 */       this.protocol.writeI64(arg0);
/*      */     } catch (TException e) {
/*  359 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeString(String arg0)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  373 */       this.protocol.writeString(arg0);
/*      */     } catch (TException e) {
/*  375 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void serializeType(Object val, Class<?> valClass, byte type)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  393 */       switch (type) {
/*      */       case 3:
/*  395 */         this.protocol.writeByte(((Byte)val).byteValue());
/*  396 */         return;
/*      */       case 11:
/*  398 */         this.protocol.writeString((String)val);
/*  399 */         return;
/*      */       case 8:
/*  401 */         this.protocol.writeI32(((Integer)val).intValue());
/*  402 */         return;
/*      */       case 6:
/*  404 */         this.protocol.writeI16(((Short)val).shortValue());
/*  405 */         return;
/*      */       case 10:
/*  407 */         this.protocol.writeI64(((Long)val).longValue());
/*  408 */         return;
/*      */       case 2:
/*  410 */         this.protocol.writeBool(((Boolean)val).booleanValue());
/*  411 */         return;
/*      */       case 64:
/*  413 */         this.protocol.writeFloat(((Number)val).floatValue());
/*  414 */         return;
/*      */       case 4:
/*  416 */         this.protocol.writeDouble(((Number)val).doubleValue());
/*  417 */         return;
/*      */       case 12:
/*  419 */         this.serializationManager.serialize(this, val);
/*  420 */         return;
/*      */       case 14:
/*  422 */         Set set = (Set)val;
/*  423 */         Iterator iterator = set.iterator();
/*  424 */         TSet setObj = new TSet((byte)1, set.size());
/*  425 */         this.protocol.writeSetBegin(setObj);
/*  426 */         while (iterator.hasNext()) {
/*  427 */           Object v = iterator.next();
/*  428 */           this.serializationManager.serialize(this, v);
/*      */         }
/*  430 */         this.protocol.writeSetEnd();
/*  431 */         return;
/*      */       case 13:
/*  433 */         Map map = (Map)val;
/*  434 */         TMap tmap = new TMap((byte)1, (byte)1, map.size());
/*  435 */         this.protocol.writeMapBegin(tmap);
/*  436 */         for (Map.Entry entry : map.entrySet()) {
/*  437 */           this.serializationManager.serialize(this, entry.getKey());
/*  438 */           this.serializationManager.serialize(this, entry.getValue());
/*      */         }
/*  440 */         this.protocol.writeMapEnd();
/*  441 */         return;
/*      */       case 15:
/*  443 */         serializeArray(val, valClass);
/*  444 */         return;
/*      */       case 1:
/*  446 */         return;
/*      */       }
/*  448 */       throw new SerializationException("Unknown type! " + val);
/*      */     }
/*      */     catch (TException e) {
/*  451 */       throw new SerializationException(
/*  452 */         "Error occurred during serialization of base type", e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void serializeArray(Object val, Class<?> valClass)
/*      */     throws TException, SerializationException
/*      */   {
/*  473 */     if (valClass.isArray()) {
/*  474 */       Class c = valClass.getComponentType();
/*  475 */       Byte b = lookupType(c);
/*  476 */       int arrayLength = Array.getLength(val);
/*      */ 
/*  478 */       if (b == null)
/*      */       {
/*  482 */         b = Byte.valueOf((byte)12);
/*      */       }
/*      */ 
/*  485 */       TList list = new TList(b.byteValue(), arrayLength);
/*  486 */       this.protocol.writeListBegin(list);
/*      */ 
/*  490 */       if (c.equals(Float.TYPE)) {
/*  491 */         float[] d = (float[])val;
/*  492 */         this.protocol.writeF32List(d);
/*  493 */       } else if (c.equals(Double.TYPE)) {
/*  494 */         double[] d = (double[])val;
/*  495 */         this.protocol.writeD64List(d);
/*  496 */       } else if (c.equals(Byte.TYPE)) {
/*  497 */         byte[] d = (byte[])val;
/*  498 */         this.protocol.writeI8List(d);
/*  499 */       } else if (c.equals(Integer.TYPE)) {
/*  500 */         int[] d = (int[])val;
/*  501 */         this.protocol.writeI32List(d);
/*  502 */       } else if (c.equals(Short.TYPE)) {
/*  503 */         short[] d = (short[])val;
/*  504 */         this.protocol.writeI16List(d);
/*  505 */       } else if (c.equals(Long.TYPE)) {
/*  506 */         long[] d = (long[])val;
/*  507 */         this.protocol.writeI64List(d);
/*  508 */       } else if (c.equals(String.class)) {
/*  509 */         String[] d = (String[])val;
/*  510 */         for (int z = 0; z < arrayLength; z++)
/*  511 */           this.protocol.writeString(d[z]);
/*      */       }
/*      */       else
/*      */       {
/*  515 */         for (int k = 0; k < arrayLength; k++) {
/*  516 */           serializeType(Array.get(val, k), c, b.byteValue());
/*      */         }
/*      */       }
/*      */ 
/*  520 */       this.protocol.writeListEnd();
/*      */     } else {
/*  522 */       Iterator iterator = ((List)val).iterator();
/*  523 */       TList list = new TList((byte)12, ((List)val).size());
/*  524 */       this.protocol.writeListBegin(list);
/*  525 */       int i = 0;
/*  526 */       while (iterator.hasNext()) {
/*  527 */         Object v = iterator.next();
/*  528 */         this.serializationManager.serialize(this, v);
/*  529 */         i++;
/*      */       }
/*  531 */       this.protocol.writeListEnd();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Byte lookupType(Class<?> clazz) {
/*  536 */     Byte b = (Byte)types.get(clazz);
/*  537 */     if (b == null) {
/*  538 */       if (clazz.isArray()) {
/*  539 */         b = Byte.valueOf((byte)15);
/*      */       } else {
/*  541 */         DynamicSerializationManager.SerializationMetadata md = this.serializationManager
/*  542 */           .getSerializationMetadata(clazz.getName());
/*  543 */         if ((md != null) || (clazz.isEnum())) {
/*  544 */           b = Byte.valueOf((byte)12);
/*      */         } else {
/*  546 */           Class superClazz = clazz.getSuperclass();
/*  547 */           while ((superClazz != null) && (md == null)) {
/*  548 */             md = this.serializationManager
/*  549 */               .getSerializationMetadata(superClazz.getName());
/*  550 */             if (md == null) {
/*  551 */               superClazz = superClazz.getSuperclass();
/*      */             }
/*      */           }
/*  554 */           if (md != null)
/*  555 */             b = Byte.valueOf((byte)12);
/*  556 */           else if (Set.class.isAssignableFrom(clazz))
/*  557 */             b = Byte.valueOf((byte)14);
/*  558 */           else if (List.class.isAssignableFrom(clazz))
/*  559 */             b = Byte.valueOf((byte)15);
/*  560 */           else if (Map.class.isAssignableFrom(clazz)) {
/*  561 */             b = Byte.valueOf((byte)13);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  567 */     return b;
/*      */   }
/*      */ 
/*      */   public void serializeMessage(Object obj, BeanMap beanMap, DynamicSerializationManager.SerializationMetadata metadata)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/*  586 */       Byte b = null;
/*  587 */       if (obj == null)
/*  588 */         b = Byte.valueOf((byte)1);
/*      */       else {
/*  590 */         b = lookupType(obj.getClass());
/*      */       }
/*  592 */       if (b == null) {
/*  593 */         throw new SerializationException(
/*  594 */           "Don't know how to serialize class: " + obj.getClass());
/*      */       }
/*      */ 
/*  601 */       if (b.byteValue() == 12) {
/*  602 */         if ((metadata != null) && (metadata.serializationFactory != null))
/*      */         {
/*  607 */           String structName = metadata.adapterStructName.replace('.', 
/*  608 */             '_');
/*  609 */           TStruct struct = new TStruct(structName);
/*  610 */           this.protocol.writeStructBegin(struct);
/*      */ 
/*  612 */           Object o = obj;
/*  613 */           ISerializationTypeAdapter fact = metadata.serializationFactory;
/*  614 */           fact.serialize(this, o);
/*  615 */           return;
/*      */         }
/*      */ 
/*  619 */         String structName = obj.getClass().getName().replace('.', '_');
/*      */ 
/*  621 */         TStruct struct = new TStruct(structName);
/*  622 */         this.protocol.writeStructBegin(struct);
/*      */ 
/*  626 */         if (obj.getClass().isEnum())
/*      */         {
/*  628 */           TField enumValueField = new TField("__enumValue__", 
/*  629 */             (byte)11, (short)0);
/*  630 */           this.protocol.writeFieldBegin(enumValueField);
/*  631 */           this.protocol.writeString(((Enum)obj).name());
/*  632 */           this.protocol.writeFieldEnd();
/*      */         }
/*      */         else
/*      */         {
/*  639 */           short id = 1;
/*  640 */           for (String keyStr : metadata.serializedAttributes)
/*      */           {
/*  642 */             Object val = beanMap.get(keyStr);
/*      */ 
/*  644 */             Class valClass = null;
/*  645 */             Byte type = null;
/*  646 */             ISerializationTypeAdapter attributeFactory = null;
/*      */ 
/*  649 */             if (val != null) {
/*  650 */               valClass = val.getClass();
/*      */ 
/*  652 */               type = lookupType(valClass);
/*  653 */               attributeFactory = 
/*  654 */                 (ISerializationTypeAdapter)metadata.attributesWithFactories
/*  654 */                 .get(keyStr);
/*  655 */               if ((type == null) && (attributeFactory == null))
/*      */               {
/*  657 */                 throw new SerializationException(
/*  658 */                   "Unable to find serialization for " + 
/*  659 */                   valClass.getName());
/*      */               }
/*      */ 
/*  665 */               if (type == null)
/*  666 */                 type = Byte.valueOf((byte)12);
/*      */             }
/*      */             else
/*      */             {
/*  670 */               type = Byte.valueOf((byte)1);
/*      */             }
/*      */ 
/*  674 */             serializeField(val, valClass, type.byteValue(), keyStr, 
/*  675 */               attributeFactory, id);
/*  676 */             id = (short)(id + 1);
/*      */           }
/*      */ 
/*  680 */           this.protocol.writeFieldStop();
/*      */         }
/*  682 */         this.protocol.writeStructEnd();
/*      */       }
/*      */       else
/*      */       {
/*  687 */         TStruct tstruct = new TStruct(b);
/*  688 */         this.protocol.writeStructBegin(tstruct);
/*  689 */         Class clazz = null;
/*  690 */         if (obj != null) {
/*  691 */           clazz = obj.getClass();
/*      */         }
/*  693 */         serializeType(obj, clazz, b.byteValue());
/*  694 */         this.protocol.writeStructEnd();
/*      */       }
/*      */     } catch (TException e) {
/*  697 */       throw new SerializationException("Serialization failed", e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean serializeField(Object val, Class<?> valClass, byte type, String keyStr, ISerializationTypeAdapter adapter, short id)
/*      */     throws TException, SerializationException
/*      */   {
/*  716 */     TField field = new TField();
/*  717 */     field.type = type;
/*  718 */     field.id = id;
/*  719 */     field.name = keyStr;
/*  720 */     this.protocol.writeFieldBegin(field);
/*      */ 
/*  722 */     if (type != 1)
/*      */     {
/*  724 */       serializeType(val, valClass, type);
/*      */     }
/*  726 */     this.protocol.writeFieldEnd();
/*      */ 
/*  728 */     return false;
/*      */   }
/*      */ 
/*      */   public Object deserializeMessage()
/*      */     throws SerializationException
/*      */   {
/*  738 */     Object retObj = null;
/*  739 */     TStruct struct = this.protocol.readStructBegin();
/*      */ 
/*  741 */     String structName = struct.name.replace('_', '.');
/*      */ 
/*  743 */     char c0 = structName.charAt(0);
/*      */ 
/*  745 */     if (Character.isDigit(c0))
/*      */     {
/*  748 */       byte b = Byte.parseByte(structName);
/*  749 */       Object obj = deserializeType(b, null, null, "", 
/*  750 */         DynamicSerializationManager.EnclosureType.COLLECTION);
/*  751 */       return obj;
/*      */     }
/*  753 */     DynamicSerializationManager.SerializationMetadata md = this.serializationManager
/*  754 */       .getSerializationMetadata(structName);
/*      */     try
/*      */     {
/*  758 */       fc = SerializationCache.getFastClass(structName);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*      */       FastClass fc;
/*  760 */       throw new SerializationException("Unable to load class: " + 
/*  761 */         structName, e);
/*      */     }
/*      */     FastClass fc;
/*  764 */     if (md == null)
/*      */     {
/*  766 */       Class superClazz = fc.getJavaClass().getSuperclass();
/*  767 */       while ((superClazz != null) && (md == null)) {
/*  768 */         md = this.serializationManager
/*  769 */           .getSerializationMetadata(superClazz.getName());
/*  770 */         superClazz = superClazz.getSuperclass();
/*      */       }
/*      */     }
/*      */ 
/*  774 */     if (md != null) {
/*  775 */       if (md.serializationFactory != null) {
/*  776 */         ISerializationTypeAdapter factory = md.serializationFactory;
/*  777 */         return factory.deserialize(this);
/*      */       }
/*  779 */     } else if (!fc.getJavaClass().isEnum()) {
/*  780 */       throw new SerializationException("metadata is null for " + 
/*  781 */         structName);
/*      */     }
/*      */ 
/*  784 */     Object o = null;
/*  785 */     BeanMap bm = null;
/*      */     try
/*      */     {
/*  789 */       if (fc.getJavaClass().isEnum()) {
/*      */         try
/*      */         {
/*  792 */           TField enumField = this.protocol.readFieldBegin();
/*  793 */           if (!enumField.name.equals("__enumValue__")) {
/*  794 */             throw new SerializationException(
/*  795 */               "Expected to find enum payload.  Found: " + 
/*  796 */               enumField.name);
/*      */           }
/*  798 */           Object retVal = Enum.valueOf(fc.getJavaClass(), 
/*  799 */             this.protocol.readString());
/*  800 */           this.protocol.readFieldEnd();
/*  801 */           Object localObject2 = retVal;
/*      */ 
/*  828 */           if ((bm != null) && (o != null)) {
/*  829 */             retObj = bm.getBean();
/*  830 */             SerializationCache.returnBeanMap(bm, o);
/*      */           }
/*  801 */           return localObject2;
/*      */         } catch (Exception e) {
/*  803 */           throw new SerializationException(
/*  804 */             "Error constructing enum enum", e);
/*      */         }
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  810 */         o = fc.newInstance();
/*  811 */         bm = SerializationCache.getBeanMap(o);
/*      */       } catch (Exception e) {
/*  813 */         throw new SerializationException(
/*  814 */           "Error instantiating class: " + struct.name, e);
/*      */       }
/*      */ 
/*  817 */       while (deserializeField(md, o, fc, bm));
/*  823 */       this.protocol.readStructEnd();
/*      */     } catch (TException e) {
/*  825 */       throw new SerializationException("Error deserializing class " + 
/*  826 */         structName, e);
/*      */     } finally {
/*  828 */       if ((bm != null) && (o != null)) {
/*  829 */         retObj = bm.getBean();
/*  830 */         SerializationCache.returnBeanMap(bm, o);
/*      */       }
/*      */     }
/*  828 */     if ((bm != null) && (o != null)) {
/*  829 */       retObj = bm.getBean();
/*  830 */       SerializationCache.returnBeanMap(bm, o);
/*      */     }
/*      */ 
/*  834 */     return retObj;
/*      */   }
/*      */ 
/*      */   private boolean deserializeField(DynamicSerializationManager.SerializationMetadata md, Object o, FastClass fc, BeanMap bm)
/*      */     throws TException, SerializationException
/*      */   {
/*  850 */     TField field = this.protocol.readFieldBegin();
/*  851 */     Object obj = null;
/*      */ 
/*  853 */     if (field.type == 0) {
/*  854 */       return false;
/*      */     }
/*      */ 
/*  857 */     if (field.type != 1) {
/*  858 */       obj = deserializeType(field.type, o.getClass(), fc, field.name, 
/*  859 */         DynamicSerializationManager.EnclosureType.FIELD);
/*  860 */       if (field.type == 11) {
/*  861 */         Class fieldClass = findFieldClass(o.getClass(), field.name);
/*  862 */         if ((fieldClass != null) && (fieldClass.isEnum()))
/*      */         {
/*  867 */           obj = Enum.valueOf(fieldClass, (String)obj);
/*      */         }
/*      */       }
/*  870 */       bm.put(field.name, obj);
/*      */     }
/*  872 */     this.protocol.readFieldEnd();
/*      */ 
/*  874 */     return true;
/*      */   }
/*      */ 
/*      */   private Object deserializeType(byte type, Class clazz, FastClass fclazz, String fieldName, DynamicSerializationManager.EnclosureType enclosureType)
/*      */     throws SerializationException
/*      */   {
/*  891 */     switch (type) {
/*      */     case 11:
/*      */       try {
/*  894 */         return this.protocol.readString();
/*      */       } catch (TException e) {
/*  896 */         throw new SerializationException("Error reading string", e);
/*      */       }
/*      */     case 6:
/*      */       try
/*      */       {
/*  901 */         return Short.valueOf(this.protocol.readI16());
/*      */       } catch (TException e) {
/*  903 */         throw new SerializationException("Error reading short", e);
/*      */       }
/*      */     case 8:
/*      */       try
/*      */       {
/*  908 */         return Integer.valueOf(this.protocol.readI32());
/*      */       } catch (TException e) {
/*  910 */         throw new SerializationException("Error reading int", e);
/*      */       }
/*      */ 
/*      */     case 15:
/*  914 */       return deserializeArray(fclazz, fieldName);
/*      */     case 13:
/*  920 */       Map map = null;
/*      */       try {
/*  922 */         TMap tmap = this.protocol.readMapBegin();
/*  923 */         if (fclazz != null) {
/*  924 */           Class clazzToTry = fclazz.getJavaClass();
/*  925 */           boolean fieldFound = false;
/*  926 */           while ((!fieldFound) && (clazzToTry != null)) {
/*  927 */             Field listField = null;
/*      */             try {
/*  929 */               listField = clazzToTry.getDeclaredField(fieldName);
/*      */             }
/*      */             catch (NoSuchFieldException e) {
/*  932 */               clazzToTry = clazzToTry.getSuperclass();
/*  933 */               continue;
/*      */             }
/*  935 */             Class fieldClazz = listField.getType();
/*  936 */             if ((!fieldClazz.isInterface()) && 
/*  937 */               (Map.class.isAssignableFrom(fieldClazz))) {
/*  938 */               map = (Map)fieldClazz.newInstance();
/*      */             }
/*  940 */             fieldFound = true;
/*      */           }
/*      */ 
/*  943 */           if (!fieldFound) {
/*  944 */             throw new NoSuchFieldException(fieldName);
/*      */           }
/*      */         }
/*      */ 
/*  948 */         if (map == null)
/*      */         {
/*  950 */           map = new HashMap((int)(tmap.size / 0.75D) + 1, 0.75F);
/*      */         }
/*      */ 
/*  953 */         for (int i = 0; i < tmap.size; i++) {
/*  954 */           Object key = this.serializationManager.deserialize(this);
/*  955 */           Object val = this.serializationManager.deserialize(this);
/*  956 */           map.put(key, val);
/*      */         }
/*      */       } catch (Exception e) {
/*  959 */         throw new SerializationException("Error deserializing map", e);
/*      */       }
/*  961 */       return map;
/*      */     case 14:
/*  964 */       Set set = null;
/*      */       try {
/*  966 */         TSet tset = this.protocol.readSetBegin();
/*      */ 
/*  972 */         if (fclazz != null) {
/*  973 */           Class clazzToTry = fclazz.getJavaClass();
/*  974 */           boolean fieldFound = false;
/*  975 */           while ((!fieldFound) && (clazzToTry != null)) {
/*  976 */             Field listField = null;
/*      */             try {
/*  978 */               listField = clazzToTry.getDeclaredField(fieldName);
/*      */             }
/*      */             catch (NoSuchFieldException e) {
/*  981 */               clazzToTry = clazzToTry.getSuperclass();
/*  982 */               continue;
/*      */             }
/*  984 */             Class fieldClazz = listField.getType();
/*  985 */             if ((!fieldClazz.isInterface()) && 
/*  986 */               (Set.class.isAssignableFrom(fieldClazz))) {
/*  987 */               set = (Set)fieldClazz.newInstance();
/*      */             }
/*  989 */             fieldFound = true;
/*      */           }
/*      */ 
/*  992 */           if (!fieldFound) {
/*  993 */             throw new NoSuchFieldException(fieldName);
/*      */           }
/*      */         }
/*      */ 
/*  997 */         if (set == null)
/*      */         {
/*  999 */           set = new HashSet((int)(tset.size / 0.75D) + 1, 0.75F);
/*      */         }
/*      */ 
/* 1002 */         for (int i = 0; i < tset.size; i++) {
/* 1003 */           set.add(this.serializationManager.deserialize(this));
/*      */         }
/*      */ 
/* 1006 */         this.protocol.readSetEnd();
/* 1007 */         return set;
/*      */       } catch (Exception e) {
/* 1009 */         throw new SerializationException("Error deserializing set", e);
/*      */       }
/*      */     case 64:
/*      */       try
/*      */       {
/* 1014 */         return Float.valueOf(this.protocol.readFloat());
/*      */       } catch (TException e) {
/* 1016 */         throw new SerializationException("Error reading double", e);
/*      */       }
/*      */     case 3:
/*      */       try
/*      */       {
/* 1021 */         return Byte.valueOf(this.protocol.readByte());
/*      */       } catch (TException e) {
/* 1023 */         throw new SerializationException("Error reading double", e);
/*      */       }
/*      */     case 10:
/*      */       try
/*      */       {
/* 1028 */         return Long.valueOf(this.protocol.readI64());
/*      */       } catch (TException e) {
/* 1030 */         throw new SerializationException("Error reading double", e);
/*      */       }
/*      */     case 4:
/*      */       try
/*      */       {
/* 1035 */         return Double.valueOf(this.protocol.readDouble());
/*      */       } catch (TException e) {
/* 1037 */         throw new SerializationException("Error reading double", e);
/*      */       }
/*      */     case 2:
/*      */       try
/*      */       {
/* 1042 */         return Boolean.valueOf(this.protocol.readBool());
/*      */       } catch (TException e) {
/* 1044 */         throw new SerializationException("Error reading boolean", e);
/*      */       }
/*      */ 
/*      */     case 12:
/* 1048 */       return this.serializationManager.deserialize(this);
/*      */     case 1:
/* 1051 */       return null;
/*      */     }
/*      */ 
/* 1054 */     throw new SerializationException("Unhandled type: " + type);
/*      */   }
/*      */ 
/*      */   private Class<?> findFieldClass(Class<?> clazz, String fieldName)
/*      */   {
/* 1060 */     String key = clazz.getName() + "." + fieldName;
/* 1061 */     Class rval = (Class)fieldClass.get(key);
/*      */ 
/* 1063 */     if (rval != null) {
/* 1064 */       return rval;
/*      */     }
/*      */ 
/* 1067 */     rval = clazz;
/*      */ 
/* 1069 */     while (rval != null) {
/* 1070 */       Field listField = null;
/*      */       try {
/* 1072 */         listField = rval.getDeclaredField(fieldName);
/*      */       }
/*      */       catch (NoSuchFieldException e) {
/* 1075 */         rval = rval.getSuperclass();
/* 1076 */         continue;
/*      */       }
/* 1078 */       rval = listField.getType();
/* 1079 */       fieldClass.put(key, rval);
/* 1080 */       return rval;
/*      */     }
/*      */ 
/* 1083 */     return null;
/*      */   }
/*      */ 
/*      */   private Object deserializeArray(FastClass fclazz, String fieldName)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/* 1097 */       TList innerList = this.protocol.readListBegin();
/*      */ 
/* 1102 */       Class listFieldClazz = null;
/* 1103 */       Field listField = null;
/*      */ 
/* 1105 */       if (fclazz != null) {
/* 1106 */         Class c = fclazz.getJavaClass();
/*      */         do {
/*      */           try {
/* 1109 */             listField = c.getDeclaredField(fieldName);
/*      */           }
/*      */           catch (NoSuchFieldException localNoSuchFieldException) {
/*      */           }
/* 1113 */           c = c.getSuperclass();
/* 1114 */         }while ((c != null) && (listField == null));
/*      */ 
/* 1116 */         if (listField == null) {
/* 1117 */           throw new SerializationException("Cannot find field " + 
/* 1118 */             fieldName);
/*      */         }
/*      */ 
/* 1121 */         listFieldClazz = listField.getType();
/*      */       }
/*      */ 
/* 1128 */       switch (innerList.elemType) {
/*      */       case 64:
/* 1130 */         float[] fa = this.protocol.readF32List(innerList.size);
/* 1131 */         this.protocol.readListEnd();
/* 1132 */         return fa;
/*      */       case 4:
/* 1134 */         double[] doubleArray = this.protocol.readD64List(innerList.size);
/* 1135 */         this.protocol.readListEnd();
/* 1136 */         return doubleArray;
/*      */       case 8:
/* 1138 */         int[] intArray = this.protocol.readI32List(innerList.size);
/* 1139 */         this.protocol.readListEnd();
/* 1140 */         return intArray;
/*      */       case 3:
/* 1142 */         byte[] byteArray = this.protocol.readI8List(innerList.size);
/* 1143 */         this.protocol.readListEnd();
/* 1144 */         return byteArray;
/*      */       case 2:
/* 1146 */         boolean[] boolArray = new boolean[innerList.size];
/* 1147 */         for (int i = 0; i < boolArray.length; i++) {
/* 1148 */           boolArray[i] = readBool();
/*      */         }
/* 1150 */         this.protocol.readListEnd();
/* 1151 */         return boolArray;
/*      */       case 10:
/* 1153 */         long[] longArray = this.protocol.readI64List(innerList.size);
/* 1154 */         this.protocol.readListEnd();
/* 1155 */         return longArray;
/*      */       case 6:
/* 1157 */         short[] shortArray = this.protocol.readI16List(innerList.size);
/* 1158 */         this.protocol.readListEnd();
/* 1159 */         return shortArray;
/*      */       case 11:
/* 1161 */         if ((listFieldClazz == null) || (listFieldClazz.isArray())) {
/* 1162 */           String[] stringArray = new String[innerList.size];
/* 1163 */           for (int i = 0; i < stringArray.length; i++) {
/* 1164 */             stringArray[i] = readString();
/*      */           }
/* 1166 */           this.protocol.readListEnd();
/* 1167 */           return stringArray;
/*      */         }
/*      */ 
/* 1171 */         List list = null;
/* 1172 */         if ((listFieldClazz != null) && 
/* 1173 */           (!listFieldClazz.isInterface()) && 
/* 1174 */           (List.class.isAssignableFrom(listFieldClazz))) {
/* 1175 */           list = (List)listFieldClazz.newInstance();
/*      */         }
/*      */ 
/* 1178 */         if (list == null) {
/* 1179 */           list = new ArrayList(innerList.size);
/*      */         }
/* 1181 */         for (int i = 0; i < innerList.size; i++) {
/* 1182 */           list.add(readString());
/*      */         }
/* 1184 */         return list;
/*      */       }
/*      */ 
/* 1187 */       if ((listFieldClazz != null) && (listFieldClazz.isArray()))
/*      */       {
/* 1189 */         Class arrayComponent = listFieldClazz.getComponentType();
/* 1190 */         Byte serializedType = Byte.valueOf(innerList.elemType);
/* 1191 */         Object array = Array.newInstance(arrayComponent, 
/* 1192 */           innerList.size);
/*      */ 
/* 1194 */         for (int i = 0; i < innerList.size; i++) {
/* 1195 */           Array.set(
/* 1196 */             array, 
/* 1197 */             i, 
/* 1198 */             deserializeType(serializedType.byteValue(), null, null, "", 
/* 1199 */             DynamicSerializationManager.EnclosureType.COLLECTION));
/*      */         }
/* 1201 */         this.protocol.readListEnd();
/* 1202 */         return array;
/*      */       }
/*      */ 
/* 1208 */       List list = null;
/* 1209 */       if ((listFieldClazz != null) && 
/* 1210 */         (!listFieldClazz.isInterface()) && 
/* 1211 */         (List.class.isAssignableFrom(listFieldClazz))) {
/* 1212 */         list = (List)listFieldClazz.newInstance();
/*      */       }
/*      */ 
/* 1216 */       if (list == null) {
/* 1217 */         list = new ArrayList(innerList.size);
/*      */       }
/* 1219 */       for (int i = 0; i < innerList.size; i++) {
/* 1220 */         list.add(this.serializationManager.deserialize(this));
/*      */       }
/* 1222 */       this.protocol.readListEnd();
/* 1223 */       return list;
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1227 */       throw new SerializationException("Error deserializing list/array", 
/* 1228 */         e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeMessageEnd()
/*      */     throws SerializationException
/*      */   {
/* 1239 */     this.protocol.writeMessageEnd();
/*      */   }
/*      */ 
/*      */   public void writeMessageStart(String messageName)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/* 1253 */       this.protocol.writeMessageBegin(new TMessage(messageName, 
/* 1254 */         (byte)1, 0));
/*      */     } catch (TException e) {
/* 1256 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void readMessageEnd()
/*      */     throws SerializationException
/*      */   {
/* 1267 */     this.protocol.readMessageEnd();
/*      */   }
/*      */ 
/*      */   public String readMessageStart()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/* 1280 */       TMessage msg = this.protocol.readMessageBegin();
/* 1281 */       return msg.name;
/*      */     } catch (TException e) {
/* 1283 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float[] readFloatArray()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/* 1297 */       int sz = this.protocol.readI32();
/* 1298 */       return this.protocol.readF32List(sz);
/*      */     } catch (TException e) {
/* 1300 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double[] readDoubleArray()
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/* 1314 */       int sz = this.protocol.readI32();
/* 1315 */       return this.protocol.readD64List(sz);
/*      */     } catch (TException e) {
/* 1317 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeFloatArray(float[] floats)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/* 1331 */       this.protocol.writeI32(floats.length);
/* 1332 */       this.protocol.writeF32List(floats);
/*      */     } catch (TException e) {
/* 1334 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeDoubleArray(double[] dubs)
/*      */     throws SerializationException
/*      */   {
/*      */     try
/*      */     {
/* 1348 */       this.protocol.writeI32(dubs.length);
/* 1349 */       this.protocol.writeD64List(dubs);
/*      */     } catch (TException e) {
/* 1351 */       throw new SerializationException(e);
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     com.raytheon.uf.common.serialization.thrift.ThriftSerializationContext
 * JD-Core Version:    0.6.2
 */