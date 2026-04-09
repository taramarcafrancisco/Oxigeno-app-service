package com.oxigeno.portal.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
 

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class XMLtools {
	private static final Logger logger = LoggerFactory.getLogger(XMLtools.class);
	
//	public static Element xmlAddValue (Document xmldoc, Element root, String sName, float value) {
//		return XMLtools.xmlAddValue(xmldoc, root, sName, String.valueOf(value));
//	}
//
//	public static Element xmlAddValue (Document xmldoc, Element root, String sName, double value) {
//		return XMLtools.xmlAddValue(xmldoc, root, sName, String.valueOf(value));
//	}
//
//	public static Element xmlAddValue (Document xmldoc, Element root, String sName, int value) {
//		return XMLtools.xmlAddValue(xmldoc, root, sName, String.valueOf(value));
//	}
//
//	public static Element xmlAddValue (Document xmldoc, Element root, String sName, boolean value) {
//		return XMLtools.xmlAddValue(xmldoc, root, sName, String.valueOf(value));
//	}
//
//	public static Element xmlAddValue (Document xmldoc, Element root, String sName, String sValue) {
//		Element eE = xmldoc.createElement(sName);
//		root.appendChild(eE);
//		if (sValue != null) {
//			//			org.w3c.dom.CDATASection t = xmldoc.createCDATASection(sValue);
//			//			eE.appendChild(t);
//			org.w3c.dom.Text t = xmldoc.createTextNode(sValue);
//			eE.appendChild(t);
//		}
//		return eE;
//	}

	public static boolean validateEmail(String email) {
		boolean isValid = false;
		try {
			// Create InternetAddress object and validated the supplied
			// address which is this case is an email address.
			InternetAddress internetAddress = new InternetAddress(email, true); // strict
			internetAddress.validate();
			isValid = true;
		} catch (AddressException e) {
			isValid = false;
		}
		return isValid;
	}

//	public static Transformer loadTransformer(InputStream is) throws TransformerConfigurationException, FileNotFoundException {
//		TransformerFactory tFactory = TransformerFactory.newInstance();
//		tFactory.setURIResolver(new URIResolver() {
//			public Source resolve(String href, String base) throws TransformerException {
//				Source src = null;
//				try {
//					InputStream is = this.getClass().getResourceAsStream(href);
//					if (is == null) throw new FileNotFoundException(href + " no encontrado"); 
//					src = new StreamSource(is);
//				}
//				catch (FileNotFoundException ex) {
//					//slogger.error (ex, ex) ;
//					throw new TransformerException(ex.getMessage(), ex);
//				}
//				return src;
//			}
//		}
//				);
//		Source xslSource = new StreamSource(is);
//		return tFactory.newTransformer(xslSource);
//	}
//
//	public static String makeHTML(String sXML, Transformer tXSL) throws
//	IOException, TransformerException {
//		// Perform the transformation, sending the output to the response.
//		Source xmlSource = new StreamSource(new ByteArrayInputStream(sXML.getBytes()));
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		tXSL.transform(xmlSource, new StreamResult(bos));
//		bos.close();
//		if (logger.isDebugEnabled()) 
//			logger.debug("Render XSL:" + bos.toString());
//		return bos.toString();
//	}
//
//	public static void makeXML(Document xmldoc, OutputStream os, String encoding) throws IOException, TransformerException {
//		if (encoding == null) encoding = "iso-8859-1";
//		/*java.util.Properties xmlProps = OutputPropertiesFactory.getDefaultMethodProperties("xml");
//		//xmlProps.setProperty(OutputKeys.INDENT, "yes");
//		xmlProps.setProperty(OutputKeys.STANDALONE, "no");
//		xmlProps.setProperty(OutputKeys.ENCODING, encoding);
//	
//		Serializer serializer = SerializerFactory.getSerializer(xmlProps);                             
//		serializer.setOutputStream(os);
//		serializer.asDOMSerializer().serialize (xmldoc.getDocumentElement());
//		 */
//		TransformerFactory tFactory =
//	    TransformerFactory.newInstance();
//	    Transformer transformer = tFactory.newTransformer();
//	    
//		DOMSource source = new DOMSource(xmldoc);
//		StreamResult result = new StreamResult(os);
//		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//		if (encoding != null) transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
//		//transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//		transformer.transform(source, result);
//		if (logger.isDebugEnabled()) 
//			logger.debug("Render XML:" + os.toString());
//	}
//
//	public static void inspectObject (Object obj, List columns, List data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		Class<?> aClass = obj.getClass();
//		Method[] declaredMethods = aClass.getDeclaredMethods();
//
//		for (Method method : declaredMethods) {
//			method.setAccessible(true);
//			if ((method.getName().startsWith("get") || method.getName().startsWith("is")) && method.getParameterCount() == 0 && method.getParameterCount() == 0 && !method.getReturnType().equals(Void.class)) {
//				columns.add (method.getName());
//				Object result = null;
//				try {
//					 result = method.invoke(obj);
//				} catch (Exception ex) {
//					logger.error(method.getName () + "" + ex.getMessage(), ex);
//				}
//				data.add (result);	
//			}
//		}
//	}
//
//	public static String toXML(String sText) {
//		if (sText == null) {
//			return "";
//		}
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < sText.length(); i++) {
//			char c = sText.charAt(i);
//			switch (c) {
//			case '<':
//				sb.append("&lt;");
//				break;
//			case '>':
//				sb.append("&gt;");
//				break;
//			case '&':
//				sb.append("&amp;");
//				break;
//			case '"':
//				sb.append("&quot;");
//				break;
//			default:
//				if (c > 127) {
//					sb.append("&#" + (int) c + ";");
//				}
//				else {
//					sb.append(c);
//				}
//			}
//		}
//		return sb.toString();
//	}
}
