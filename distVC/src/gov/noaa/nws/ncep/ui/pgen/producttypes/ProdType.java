/*     */ package gov.noaa.nws.ncep.ui.pgen.producttypes;
/*     */ 
/*     */ import com.raytheon.uf.common.localization.LocalizationFile;
/*     */ import com.raytheon.uf.common.serialization.SerializationUtil;
/*     */ import gov.noaa.nws.ncep.common.staticdata.IStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenStaticDataProvider;
/*     */ import gov.noaa.nws.ncep.ui.pgen.PgenUtil;
/*     */ import gov.noaa.nws.ncep.ui.pgen.elements.Product;
/*     */ import gov.noaa.nws.ncep.ui.pgen.file.ProductConverter;
/*     */ import java.io.File;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.w3c.dom.Document;
/*     */ 
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="")
/*     */ @XmlRootElement(name="ProdType")
/*     */ public class ProdType
/*     */ {
/*     */ 
/*     */   @XmlAttribute(name="Type")
/*     */   protected String type;
/*     */ 
/*     */   @XmlAttribute(name="Name")
/*     */   protected String name;
/*     */ 
/*     */   @XmlAttribute(name="StyleSheetFile")
/*     */   protected String styleSheetFile;
/*     */ 
/*     */   @XmlAttribute(name="OutputFile")
/*     */   protected String outputFile;
/*     */ 
/*     */   public String getType()
/*     */   {
/*  86 */     return this.type;
/*     */   }
/*     */ 
/*     */   public void setType(String value)
/*     */   {
/*  98 */     this.type = value;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 110 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String value)
/*     */   {
/* 122 */     this.name = value;
/*     */   }
/*     */ 
/*     */   public String getStyleSheetFile()
/*     */   {
/* 134 */     return this.styleSheetFile;
/*     */   }
/*     */ 
/*     */   public void setStyleSheetFile(String value)
/*     */   {
/* 146 */     this.styleSheetFile = value;
/*     */   }
/*     */ 
/*     */   public String getOutputFile()
/*     */   {
/* 158 */     return this.outputFile;
/*     */   }
/*     */ 
/*     */   public void setOutputFile(String value)
/*     */   {
/* 170 */     this.outputFile = value;
/*     */   }
/*     */ 
/*     */   public String generateProd(Product prd)
/*     */   {
/* 180 */     List lp = new ArrayList();
/* 181 */     lp.add(prd);
/*     */ 
/* 183 */     String ret = "";
/*     */ 
/* 185 */     Document sw = null;
/*     */     try
/*     */     {
/* 188 */       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 189 */       dbf.setNamespaceAware(true);
/* 190 */       DocumentBuilder db = dbf.newDocumentBuilder();
/* 191 */       sw = db.newDocument();
/* 192 */       Marshaller mar = SerializationUtil.getJaxbContext().createMarshaller();
/* 193 */       mar.marshal(ProductConverter.convert(lp), sw);
/*     */     } catch (Exception e) {
/* 195 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 198 */     if (this.type.equalsIgnoreCase("Text Prod")) {
/* 199 */       DOMSource ds = new DOMSource(sw);
/* 200 */       ret = PgenUtil.applyStyleSheet(ds, getStyleSheetFilePath());
/*     */     }
/* 202 */     else if (this.type.equalsIgnoreCase("KML")) {
/* 203 */       DOMSource ds = new DOMSource(sw);
/* 204 */       ret = formatXML(PgenUtil.applyStyleSheet(ds, getStyleSheetFilePath()));
/*     */     }
/* 206 */     else if (this.type.equalsIgnoreCase("XML")) {
/* 207 */       ret = getStringFromDoc(sw);
/*     */     }
/*     */ 
/* 210 */     return ret;
/*     */   }
/*     */ 
/*     */   private String getStringFromDoc(Document doc)
/*     */   {
/*     */     try
/*     */     {
/* 221 */       DOMSource domSource = new DOMSource(doc);
/* 222 */       StringWriter writer = new StringWriter();
/* 223 */       StreamResult result = new StreamResult(writer);
/* 224 */       TransformerFactory tf = TransformerFactory.newInstance();
/* 225 */       Transformer transformer = tf.newTransformer();
/*     */ 
/* 227 */       transformer.setOutputProperty("indent", "yes");
/* 228 */       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
/*     */ 
/* 230 */       transformer.transform(domSource, result);
/* 231 */       writer.flush();
/* 232 */       return writer.toString();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 236 */       ex.printStackTrace();
/* 237 */     }return null;
/*     */   }
/*     */ 
/*     */   private String formatXML(String xmlStr)
/*     */   {
/* 242 */     StreamSource is = new StreamSource(new StringReader(xmlStr));
/*     */     try
/*     */     {
/* 245 */       StringWriter writer = new StringWriter();
/* 246 */       StreamResult result = new StreamResult(writer);
/* 247 */       TransformerFactory tf = TransformerFactory.newInstance();
/* 248 */       Transformer transformer = tf.newTransformer();
/*     */ 
/* 250 */       transformer.setOutputProperty("indent", "yes");
/* 251 */       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
/*     */ 
/* 253 */       transformer.transform(is, result);
/* 254 */       writer.flush();
/* 255 */       return writer.toString();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 259 */       ex.printStackTrace();
/* 260 */     }return "";
/*     */   }
/*     */ 
/*     */   private String getStyleSheetFilePath()
/*     */   {
/* 265 */     String ret = "";
/*     */     try
/*     */     {
/* 268 */       LocalizationFile lFile = PgenStaticDataProvider.getProvider().getStaticLocalizationFile(getStyleSheetFileName(this.styleSheetFile));
/*     */ 
/* 270 */       ret = lFile.getFile().getAbsolutePath();
/*     */     }
/*     */     catch (Exception e) {
/* 273 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 276 */     return ret;
/*     */   }
/*     */ 
/*     */   private String getStyleSheetFileName(String fname) {
/* 280 */     String ret = "";
/* 281 */     if ((fname == null) || (fname.isEmpty())) return ret;
/*     */ 
/* 283 */     return PgenStaticDataProvider.getProvider().getPgenLocalizationRoot() + File.separator + "xslt" + File.separator + "prod" + File.separator + fname;
/*     */   }
/*     */ }

/* Location:           /Users/mj/vgf2geojson/distVC/vgfConverter.jar
 * Qualified Name:     gov.noaa.nws.ncep.ui.pgen.producttypes.ProdType
 * JD-Core Version:    0.6.2
 */