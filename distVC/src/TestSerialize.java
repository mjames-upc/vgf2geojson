/*     */ import com.raytheon.uf.common.serialization.DynamicSerializationManager;
/*     */ import com.raytheon.uf.common.serialization.DynamicSerializationManager.SerializationType;
/*     */ import com.raytheon.uf.common.serialization.IDeserializationContext;
/*     */ import com.raytheon.uf.common.serialization.ISerializableObject;
/*     */ import com.raytheon.uf.common.serialization.ISerializationContext;
/*     */ import com.raytheon.uf.common.serialization.ISerializationTypeAdapter;
/*     */ import com.raytheon.uf.common.serialization.SerializationException;
/*     */ import com.raytheon.uf.common.serialization.annotations.DynamicSerialize;
/*     */ import com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement;
/*     */ import com.raytheon.uf.common.serialization.annotations.DynamicSerializeTypeAdapter;
/*     */ import com.raytheon.uf.common.util.FileUtil;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.junit.Assert;
/*     */ import org.junit.Test;
/*     */ 
/*     */ public class TestSerialize
/*     */ {
/*     */   @Test
/*     */   public void testBasicFunctionality()
/*     */   {
/* 544 */     TestSerialize.Test inTest = new TestSerialize.Test();
/* 545 */     inTest.set_byte((byte)1);
/* 546 */     inTest.set_Byte(new Byte((byte)2));
/* 547 */     inTest.set_double(2.1D);
/* 548 */     inTest.set_Double(new Double(4.4D));
/* 549 */     inTest.set_float(4.2F);
/* 550 */     inTest.set_Float(Float.valueOf(5.5F));
/* 551 */     inTest.set_int(8);
/* 552 */     inTest.set_Integer(new Integer(32));
/* 553 */     inTest.set_long(1000000L);
/* 554 */     inTest.set_Long(new Long(10000000000000L));
/* 555 */     inTest.set_short((short)8);
/* 556 */     inTest.set_Short(new Short((short)92));
/* 557 */     inTest.set_String("abcdefg");
/*     */ 
/* 559 */     DynamicSerializationManager dmgr = 
/* 560 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/*     */ 
/* 562 */     DynamicSerializationManager.inspect(inTest.getClass());
/* 563 */     byte[] bdata = null;
/*     */     try {
/* 565 */       bdata = dmgr.serialize(inTest);
/* 566 */       System.out.println("Serialized data of size: " + bdata.length);
/*     */     } catch (Throwable e) {
/* 568 */       e.printStackTrace();
/* 569 */       Assert.fail(e.getMessage());
/*     */     }
/*     */ 
/* 572 */     TestSerialize.Test outTest = null;
/*     */     try
/*     */     {
/* 575 */       ByteArrayInputStream bais = new ByteArrayInputStream(bdata);
/* 576 */       outTest = (TestSerialize.Test)dmgr.deserialize(bais);
/*     */     } catch (Throwable e) {
/* 578 */       e.printStackTrace();
/* 579 */       Assert.fail(e.getMessage());
/*     */     }
/*     */ 
/* 583 */     Assert.assertNotSame(inTest, outTest);
/*     */ 
/* 586 */     Assert.assertEquals(inTest.get_double(), outTest.get_double(), 0.0001D);
/* 587 */     Assert.assertEquals(inTest.get_Double(), outTest.get_Double());
/*     */ 
/* 589 */     Assert.assertEquals(inTest.get_byte(), outTest.get_byte());
/* 590 */     Assert.assertEquals(inTest.get_Byte(), outTest.get_Byte());
/*     */ 
/* 592 */     Assert.assertEquals(inTest.get_float(), outTest.get_float(), 0.0001D);
/* 593 */     Assert.assertEquals(inTest.get_Float(), outTest.get_Float());
/*     */ 
/* 595 */     Assert.assertEquals(inTest.get_int(), outTest.get_int());
/* 596 */     Assert.assertEquals(inTest.get_Integer(), outTest.get_Integer());
/*     */ 
/* 598 */     Assert.assertEquals(inTest.get_long(), outTest.get_long());
/* 599 */     Assert.assertEquals(inTest.get_Long(), outTest.get_Long());
/*     */ 
/* 601 */     Assert.assertEquals(inTest.get_short(), outTest.get_short());
/* 602 */     Assert.assertEquals(inTest.get_Short(), outTest.get_Short());
/*     */ 
/* 604 */     Assert.assertEquals(inTest.get_String(), outTest.get_String());
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testContainerFunctionality()
/*     */   {
/* 611 */     DynamicSerializationManager dmgr = 
/* 612 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/*     */ 
/* 614 */     TestSerialize.TestContainer inContainer = new TestSerialize.TestContainer();
/*     */ 
/* 616 */     TestSerialize.Test2 inTest2_1 = new TestSerialize.Test2();
/* 617 */     inTest2_1.setX("hijklmnop");
/* 618 */     inTest2_1.setY(127);
/*     */ 
/* 620 */     TestSerialize.Test2 inTest2_2 = new TestSerialize.Test2();
/* 621 */     inTest2_2.setX("0123456");
/* 622 */     inTest2_2.setY(256);
/*     */ 
/* 624 */     float[] in = new float[262792];
/* 625 */     for (int i = 0; i < in.length; i++) {
/* 626 */       in[i] = i;
/*     */     }
/*     */ 
/* 629 */     inContainer.setArray(in);
/*     */ 
/* 631 */     Set inFloatSet = new HashSet();
/* 632 */     inFloatSet.add(Float.valueOf(1.0F));
/* 633 */     inFloatSet.add(Float.valueOf(1.3F));
/* 634 */     inFloatSet.add(Float.valueOf(2.2F));
/* 635 */     inContainer.setSet(inFloatSet);
/*     */ 
/* 637 */     List inDoubleList = new ArrayList();
/* 638 */     inDoubleList.add(Double.valueOf(1.2D));
/* 639 */     inDoubleList.add(Double.valueOf(0.5D));
/* 640 */     inContainer.setList(inDoubleList);
/*     */ 
/* 642 */     List inTest2List = new ArrayList();
/* 643 */     inTest2List.add(inTest2_1);
/* 644 */     inTest2List.add(inTest2_2);
/* 645 */     inContainer.setList2(inTest2List);
/*     */ 
/* 647 */     Map inMap = new HashMap();
/* 648 */     inMap.put("abc", inTest2_1);
/* 649 */     inContainer.setMap(inMap);
/*     */ 
/* 651 */     DynamicSerializationManager.inspect(inContainer.getClass());
/* 652 */     byte[] bdata = null;
/*     */     try {
/* 654 */       bdata = dmgr.serialize(inContainer);
/* 655 */       System.out.println("Serialized data of size: " + bdata.length);
/*     */     } catch (Throwable e) {
/* 657 */       e.printStackTrace();
/* 658 */       Assert.fail(e.getMessage());
/*     */     }
/* 660 */     TestSerialize.TestContainer outContainer = null;
/*     */     try
/*     */     {
/* 663 */       ByteArrayInputStream bais = new ByteArrayInputStream(bdata);
/* 664 */       outContainer = (TestSerialize.TestContainer)dmgr.deserialize(bais);
/*     */     } catch (Throwable e) {
/* 666 */       e.printStackTrace();
/* 667 */       Assert.fail(e.getMessage());
/*     */     }
/*     */ 
/* 671 */     Assert.assertNotSame(inContainer, outContainer);
/* 672 */     Assert.assertNotSame(inContainer.getArray(), outContainer.getArray());
/*     */ 
/* 675 */     Assert.assertEquals(inContainer.getArray().length, 
/* 676 */       outContainer.getArray().length);
/* 677 */     for (int i = 0; i < inContainer.getArray().length; i++) {
/* 678 */       Assert.assertEquals(inContainer.getArray()[i], 
/* 679 */         outContainer.getArray()[i], 0.0001D);
/*     */     }
/*     */ 
/* 682 */     Assert.assertEquals(inContainer.getList().size(), outContainer
/* 683 */       .getList().size());
/* 684 */     for (int i = 0; i < inContainer.getList().size(); i++) {
/* 685 */       Assert.assertEquals(inContainer.getList().get(i), outContainer
/* 686 */         .getList().get(i));
/*     */     }
/*     */ 
/* 689 */     Assert.assertEquals(inContainer.getList2().size(), outContainer
/* 690 */       .getList2().size());
/* 691 */     for (int i = 0; i < inContainer.getList2().size(); i++)
/*     */     {
/* 693 */       Assert.assertEquals(((TestSerialize.Test2)inContainer.getList2().get(i)).getX(), 
/* 694 */         ((TestSerialize.Test2)outContainer.getList2().get(i)).getX());
/* 695 */       Assert.assertEquals(((TestSerialize.Test2)inContainer.getList2().get(i)).getY(), 
/* 696 */         ((TestSerialize.Test2)outContainer.getList2().get(i)).getY());
/*     */     }
/*     */ 
/* 699 */     Assert.assertEquals(inContainer.getSet().size(), outContainer.getSet()
/* 700 */       .size());
/* 701 */     Iterator iterator1 = inContainer.getSet().iterator();
/* 702 */     Iterator iterator2 = outContainer.getSet().iterator();
/* 703 */     while (iterator1.hasNext())
/* 704 */       Assert.assertEquals(iterator1.next(), iterator2.next());
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testInheritance()
/*     */   {
/* 711 */     TestSerialize.TestChild tc = new TestSerialize.TestChild();
/* 712 */     TestSerialize.AdapterObject ao = new TestSerialize.AdapterObject();
/* 713 */     ao.setInnerObject("parent");
/* 714 */     tc.setAdapterObject(ao);
/* 715 */     tc.setChildString("child");
/*     */ 
/* 717 */     DynamicSerializationManager dmgr = 
/* 718 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/*     */ 
/* 720 */     DynamicSerializationManager.inspect(tc.getClass());
/* 721 */     DynamicSerializationManager.inspect(TestSerialize.AdapterObject.class);
/* 722 */     byte[] bdata = null;
/*     */     try {
/* 724 */       bdata = dmgr.serialize(tc);
/* 725 */       System.out.println("Serialized data of size: " + bdata.length);
/* 726 */       FileUtil.bytes2File(bdata, new File("/tmp/javaOut"));
/*     */     } catch (Throwable e) {
/* 728 */       e.printStackTrace();
/* 729 */       Assert.fail(e.getMessage());
/*     */     }
/* 731 */     TestSerialize.TestChild outTest = null;
/*     */     try
/*     */     {
/* 734 */       ByteArrayInputStream bais = new ByteArrayInputStream(bdata);
/* 735 */       outTest = (TestSerialize.TestChild)dmgr.deserialize(bais);
/*     */     } catch (Throwable e) {
/* 737 */       e.printStackTrace();
/* 738 */       Assert.fail(e.getMessage());
/*     */     }
/*     */ 
/* 741 */     Assert.assertEquals(tc.getAdapterObject().getInnerObject(), outTest
/* 742 */       .getAdapterObject().getInnerObject());
/* 743 */     Assert.assertEquals(tc.getChildString(), outTest.getChildString());
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testAdvancedCollections()
/*     */   {
/* 749 */     TestSerialize.TestAdvancedCollection1 coll = new TestSerialize.TestAdvancedCollection1();
/*     */ 
/* 751 */     LinkedHashMap lhm = new LinkedHashMap();
/* 752 */     lhm.put("1", "one");
/* 753 */     lhm.put("2", "two");
/*     */ 
/* 755 */     coll.setFoo(lhm);
/* 756 */     DynamicSerializationManager dmgr = 
/* 757 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/*     */ 
/* 759 */     DynamicSerializationManager.inspect(coll.getClass());
/* 760 */     byte[] bdata = null;
/*     */     try {
/* 762 */       bdata = dmgr.serialize(coll);
/* 763 */       System.out.println("Serialized data of size: " + bdata.length);
/*     */     } catch (Throwable e) {
/* 765 */       e.printStackTrace();
/* 766 */       Assert.fail(e.getMessage());
/*     */     }
/* 768 */     TestSerialize.TestAdvancedCollection1 outTest = null;
/*     */     try
/*     */     {
/* 771 */       ByteArrayInputStream bais = new ByteArrayInputStream(bdata);
/* 772 */       outTest = (TestSerialize.TestAdvancedCollection1)dmgr.deserialize(bais);
/* 773 */       System.out.println(outTest);
/*     */     } catch (Throwable e) {
/* 775 */       e.printStackTrace();
/* 776 */       Assert.fail(e.getMessage());
/*     */     }
/*     */ 
/* 779 */     Assert.assertEquals(LinkedHashMap.class, outTest.getFoo().getClass());
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testNullsInArrays()
/*     */   {
/* 800 */     DynamicSerializationManager dmgr = 
/* 801 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/*     */ 
/* 803 */     DynamicSerializationManager.inspect(TestSerialize.NullTester.class);
/*     */ 
/* 805 */     TestSerialize.NullTester nt = new TestSerialize.NullTester();
/* 806 */     Object[] obj = new Object[3];
/* 807 */     obj[0] = new String("test");
/* 808 */     obj[1] = null;
/* 809 */     obj[2] = new Integer(3);
/*     */ 
/* 811 */     nt.setObjArr(obj);
/*     */ 
/* 813 */     byte[] d = null;
/*     */     try {
/* 815 */       d = dmgr.serialize(nt);
/*     */     } catch (SerializationException e) {
/* 817 */       e.printStackTrace();
/* 818 */       Assert.fail(e.getMessage());
/*     */     }
/*     */     try
/*     */     {
/* 822 */       Object result = dmgr.deserialize(new ByteArrayInputStream(d));
/*     */ 
/* 824 */       Assert.assertTrue(result instanceof TestSerialize.NullTester);
/* 825 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr() != null);
/* 826 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr() instanceof Object[]);
/* 827 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr().length == obj.length);
/* 828 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr()[0]
/* 829 */         .equals(obj[0]));
/* 830 */       Assert.assertNull(((TestSerialize.NullTester)result).getObjArr()[1]);
/* 831 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr()[2]
/* 832 */         .equals(obj[2]));
/*     */     } catch (SerializationException e) {
/* 834 */       e.printStackTrace();
/* 835 */       Assert.fail(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   @Test
/*     */   public void testByteArrayInObjectArray()
/*     */   {
/* 842 */     DynamicSerializationManager dmgr = 
/* 843 */       DynamicSerializationManager.getManager(DynamicSerializationManager.SerializationType.Thrift);
/*     */ 
/* 845 */     DynamicSerializationManager.inspect(TestSerialize.NullTester.class);
/*     */ 
/* 847 */     TestSerialize.NullTester nt = new TestSerialize.NullTester();
/* 848 */     Object[] obj = new Object[3];
/* 849 */     obj[0] = new String("test");
/* 850 */     obj[1] = null;
/* 851 */     obj[2] = new Integer(3);
/*     */ 
/* 853 */     nt.setObjArr(obj);
/*     */ 
/* 855 */     byte[] d = null;
/*     */     try {
/* 857 */       d = dmgr.serialize(nt);
/*     */     } catch (SerializationException e) {
/* 859 */       e.printStackTrace();
/* 860 */       Assert.fail(e.getMessage());
/*     */     }
/*     */     try
/*     */     {
/* 864 */       Object result = dmgr.deserialize(new ByteArrayInputStream(d));
/*     */ 
/* 866 */       Assert.assertTrue(result instanceof TestSerialize.NullTester);
/* 867 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr() != null);
/* 868 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr() instanceof Object[]);
/* 869 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr().length == obj.length);
/* 870 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr()[0]
/* 871 */         .equals(obj[0]));
/* 872 */       Assert.assertNull(((TestSerialize.NullTester)result).getObjArr()[1]);
/* 873 */       Assert.assertTrue(((TestSerialize.NullTester)result).getObjArr()[2]
/* 874 */         .equals(obj[2]));
/*     */     } catch (SerializationException e) {
/* 876 */       e.printStackTrace();
/* 877 */       Assert.fail(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   @DynamicSerialize
/*     */   @DynamicSerializeTypeAdapter(factory=TestSerialize.TestAdapter.class)
/*     */   public static class AdapterObject
/*     */   {
/*     */     private String innerObject;
/*     */ 
/*     */     public String getInnerObject()
/*     */     {
/* 487 */       return this.innerObject;
/*     */     }
/*     */ 
/*     */     public void setInnerObject(String innerObject)
/*     */     {
/* 495 */       this.innerObject = innerObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   @DynamicSerialize
/*     */   public static class DataURINotificationMessage
/*     */     implements ISerializableObject
/*     */   {
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private String[] dataURIs;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private int[] ids;
/*     */ 
/*     */     public String[] getDataURIs()
/*     */     {
/* 896 */       return this.dataURIs;
/*     */     }
/*     */ 
/*     */     public int[] getIds() {
/* 900 */       return this.ids;
/*     */     }
/*     */ 
/*     */     public void setIds(int[] ids)
/*     */     {
/* 908 */       this.ids = ids;
/*     */     }
/*     */ 
/*     */     public void setDataURIs(String[] dataURIs)
/*     */     {
/* 916 */       this.dataURIs = dataURIs;
/*     */     }
/*     */   }
/*     */ 
/*     */   @DynamicSerialize
/*     */   public static class NullTester
/*     */   {
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private Object[] objArr;
/*     */ 
/*     */     public Object[] getObjArr()
/*     */     {
/* 789 */       return this.objArr;
/*     */     }
/*     */ 
/*     */     public void setObjArr(Object[] objArr) {
/* 793 */       this.objArr = objArr;
/*     */     }
/*     */   }
/*     */ 
/*     */   @DynamicSerialize
/*     */   public static class Test
/*     */   {
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private String _String;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private int _int;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private Integer _Integer;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private float _float;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private Float _Float;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private byte _byte;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private Byte _Byte;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private long _long;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private Long _Long;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private double _double;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private Double _Double;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private short _short;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private Short _Short;
/*     */ 
/*     */     public String get_String()
/*     */     {
/* 107 */       return this._String;
/*     */     }
/*     */ 
/*     */     public void set_String(String string)
/*     */     {
/* 115 */       this._String = string;
/*     */     }
/*     */ 
/*     */     public int get_int()
/*     */     {
/* 122 */       return this._int;
/*     */     }
/*     */ 
/*     */     public void set_int(int _int)
/*     */     {
/* 130 */       this._int = _int;
/*     */     }
/*     */ 
/*     */     public Integer get_Integer()
/*     */     {
/* 137 */       return this._Integer;
/*     */     }
/*     */ 
/*     */     public void set_Integer(Integer integer)
/*     */     {
/* 145 */       this._Integer = integer;
/*     */     }
/*     */ 
/*     */     public float get_float()
/*     */     {
/* 152 */       return this._float;
/*     */     }
/*     */ 
/*     */     public void set_float(float _float)
/*     */     {
/* 160 */       this._float = _float;
/*     */     }
/*     */ 
/*     */     public Float get_Float()
/*     */     {
/* 167 */       return this._Float;
/*     */     }
/*     */ 
/*     */     public void set_Float(Float float1)
/*     */     {
/* 175 */       this._Float = float1;
/*     */     }
/*     */ 
/*     */     public byte get_byte()
/*     */     {
/* 182 */       return this._byte;
/*     */     }
/*     */ 
/*     */     public void set_byte(byte _byte)
/*     */     {
/* 190 */       this._byte = _byte;
/*     */     }
/*     */ 
/*     */     public Byte get_Byte()
/*     */     {
/* 197 */       return this._Byte;
/*     */     }
/*     */ 
/*     */     public void set_Byte(Byte byte1)
/*     */     {
/* 205 */       this._Byte = byte1;
/*     */     }
/*     */ 
/*     */     public long get_long()
/*     */     {
/* 212 */       return this._long;
/*     */     }
/*     */ 
/*     */     public void set_long(long _long)
/*     */     {
/* 220 */       this._long = _long;
/*     */     }
/*     */ 
/*     */     public Long get_Long()
/*     */     {
/* 227 */       return this._Long;
/*     */     }
/*     */ 
/*     */     public void set_Long(Long long1)
/*     */     {
/* 235 */       this._Long = long1;
/*     */     }
/*     */ 
/*     */     public double get_double()
/*     */     {
/* 242 */       return this._double;
/*     */     }
/*     */ 
/*     */     public void set_double(double _double)
/*     */     {
/* 250 */       this._double = _double;
/*     */     }
/*     */ 
/*     */     public Double get_Double()
/*     */     {
/* 257 */       return this._Double;
/*     */     }
/*     */ 
/*     */     public void set_Double(Double double1)
/*     */     {
/* 265 */       this._Double = double1;
/*     */     }
/*     */ 
/*     */     public short get_short()
/*     */     {
/* 272 */       return this._short;
/*     */     }
/*     */ 
/*     */     public void set_short(short _short)
/*     */     {
/* 280 */       this._short = _short;
/*     */     }
/*     */ 
/*     */     public Short get_Short()
/*     */     {
/* 287 */       return this._Short;
/*     */     }
/*     */ 
/*     */     public void set_Short(Short short1)
/*     */     {
/* 295 */       this._Short = short1;
/*     */     }
/*     */   }
/*     */ 
/*     */   @DynamicSerialize
/*     */   public static class Test2
/*     */   {
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private String x;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private int y;
/*     */ 
/*     */     public String getX() {
/* 312 */       return this.x;
/*     */     }
/*     */ 
/*     */     public void setX(String x)
/*     */     {
/* 320 */       this.x = x;
/*     */     }
/*     */ 
/*     */     public int getY()
/*     */     {
/* 327 */       return this.y;
/*     */     }
/*     */ 
/*     */     public void setY(int y)
/*     */     {
/* 335 */       this.y = y;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class TestAdapter
/*     */     implements ISerializationTypeAdapter<TestSerialize.AdapterObject>
/*     */   {
/*     */     public TestSerialize.AdapterObject deserialize(IDeserializationContext deserializer)
/*     */       throws SerializationException
/*     */     {
/* 506 */       String s = deserializer.readString();
/* 507 */       TestSerialize.AdapterObject ao = new TestSerialize.AdapterObject();
/* 508 */       ao.setInnerObject(s);
/* 509 */       return ao;
/*     */     }
/*     */ 
/*     */     public void serialize(ISerializationContext serializer, TestSerialize.AdapterObject object)
/*     */       throws SerializationException
/*     */     {
/* 515 */       serializer.writeString(object.getInnerObject());
/*     */     }
/*     */   }
/*     */ 
/*     */   @DynamicSerialize
/*     */   public static class TestAdvancedCollection1
/*     */   {
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private LinkedHashMap<String, String> foo;
/*     */ 
/*     */     public LinkedHashMap<String, String> getFoo()
/*     */     {
/* 465 */       return this.foo;
/*     */     }
/*     */ 
/*     */     public void setFoo(LinkedHashMap<String, String> foo)
/*     */     {
/* 473 */       this.foo = foo;
/*     */     }
/*     */   }
/*     */ 
/*     */   @DynamicSerialize
/*     */   public static class TestChild extends TestSerialize.TestParent
/*     */   {
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private String childString;
/*     */ 
/*     */     public String getChildString()
/*     */     {
/* 529 */       return this.childString;
/*     */     }
/*     */ 
/*     */     public void setChildString(String childString)
/*     */     {
/* 537 */       this.childString = childString;
/*     */     }
/*     */   }
/*     */ 
/*     */   @DynamicSerialize
/*     */   public static class TestContainer
/*     */   {
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private float[] array;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private Set<Float> set;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private List<Double> list;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private List<TestSerialize.Test2> list2;
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private Map<String, TestSerialize.Test2> map;
/*     */ 
/*     */     public List<Double> getList()
/*     */     {
/* 361 */       return this.list;
/*     */     }
/*     */ 
/*     */     public void setList(List<Double> list)
/*     */     {
/* 369 */       this.list = list;
/*     */     }
/*     */ 
/*     */     public List<TestSerialize.Test2> getList2()
/*     */     {
/* 376 */       return this.list2;
/*     */     }
/*     */ 
/*     */     public void setList2(List<TestSerialize.Test2> list2)
/*     */     {
/* 384 */       this.list2 = list2;
/*     */     }
/*     */ 
/*     */     public Map<String, TestSerialize.Test2> getMap()
/*     */     {
/* 391 */       return this.map;
/*     */     }
/*     */ 
/*     */     public void setMap(Map<String, TestSerialize.Test2> map)
/*     */     {
/* 399 */       this.map = map;
/*     */     }
/*     */ 
/*     */     public float[] getArray()
/*     */     {
/* 406 */       return this.array;
/*     */     }
/*     */ 
/*     */     public void setArray(float[] array)
/*     */     {
/* 414 */       this.array = array;
/*     */     }
/*     */ 
/*     */     public Set<Float> getSet()
/*     */     {
/* 421 */       return this.set;
/*     */     }
/*     */ 
/*     */     public void setSet(Set<Float> set)
/*     */     {
/* 429 */       this.set = set;
/*     */     }
/*     */   }
/*     */ 
/*     */   @DynamicSerialize
/*     */   public static class TestParent
/*     */   {
/*     */ 
/*     */     @DynamicSerializeElement
/*     */     private TestSerialize.AdapterObject adapterObject;
/*     */ 
/*     */     public TestSerialize.AdapterObject getAdapterObject()
/*     */     {
/* 443 */       return this.adapterObject;
/*     */     }
/*     */ 
/*     */     public void setAdapterObject(TestSerialize.AdapterObject adapterObject)
/*     */     {
/* 451 */       this.adapterObject = adapterObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     TestSerialize
 * JD-Core Version:    0.6.2
 */