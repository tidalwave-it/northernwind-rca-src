/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import it.tidalwave.northernwind.core.impl.util.XhtmlMarkupSerializer;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/***********************************************************************************************************************
 *
 * FIXME: partially copied from XsltFilter
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class XhtmlNormalizer
  {
    private String DOCTYPE_HTML = "<!doctype html>";

//    static FIXME: doesn't properly indent
//      {
//        try
//          {
//            final Field field = XhtmlMarkupSerializer.class.getDeclaredField("outputFormat");
//            field.setAccessible(true);
//            final OutputFormat outputFormat = (OutputFormat) field.get(null);
//            outputFormat.setIndent(2);
//            outputFormat.setIndenting(true);
//          }
//        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
//          {
//            log.error("", e);
//          }
//      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public String asNormalizedString (final @Nonnull String text)
      {
        log.trace("asNormalizedString()\n{}", text);
        try
          {
            final DOMResult result = new DOMResult();
            final Transformer transformer = createTransformer();
            // Fix for NW-100
            transformer.transform(new DOMSource(stringToNode(text.replace("xml:lang", "xml_lang").replace(DOCTYPE_HTML, ""))), result);

            final StringWriter stringWriter = new StringWriter();

//            if (text.startsWith(DOCTYPE_HTML))
//              {
                stringWriter.append(DOCTYPE_HTML).append("\n");
//              }

            // Fix for NW-96
            final XhtmlMarkupSerializer xhtmlSerializer = new XhtmlMarkupSerializer(stringWriter);
            xhtmlSerializer.serialize(result.getNode());
            return stringWriter.toString().replace("xml_lang", "xml:lang").replace(" xmlns=\"\"", "").replaceAll("<br />", "<br/>"); // FIXME:
          }
        catch (SAXParseException e)
          {
            log.error("XML parse error: {} at l{}:c{}", e.getMessage(), e.getLineNumber(), e.getColumnNumber());
            log.error(text);
            throw new RuntimeException(e);
          }
        catch (TransformerException e)
          {
            log.error("XSL error: {} at {}", e.toString(), e.getLocationAsString());
            throw new RuntimeException(e);
          }
        catch (IOException | SAXException | ParserConfigurationException e)
          {
            throw new RuntimeException(e);
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private Transformer createTransformer()
      throws TransformerConfigurationException
      {
        // FIXME: inject TransformerFactory
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

//        try
//          {
//            final URIResolver uriResolver = context.getBean(URIResolver.class);
//            log.trace("Using URIResolver: {}", uriResolver.getClass());
//            transformer.setURIResolver(uriResolver);
//          }
//        catch (NoSuchBeanDefinitionException e)
//          {
//            // ok, not installed
//          }

        return transformer;
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private Node stringToNode (final @Nonnull String string)
      throws IOException, SAXException, ParserConfigurationException
      {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final InputSource source = new InputSource(new StringReader(string));
        final Document document = builder.parse(source);
        return document;
      }
  }
