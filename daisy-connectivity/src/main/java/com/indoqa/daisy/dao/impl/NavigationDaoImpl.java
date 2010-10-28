package com.indoqa.daisy.dao.impl;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Stack;

import org.apache.cocoon.pipeline.NonCachingPipeline;
import org.apache.cocoon.pipeline.Pipeline;
import org.apache.cocoon.sax.AbstractSAXSerializer;
import org.apache.cocoon.sax.SAXPipelineComponent;
import org.apache.cocoon.sax.component.XMLGenerator;
import org.apache.cocoon.sax.component.XMLSerializer;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Repository;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.indoqa.commons.spring.util.URLStringUtils;
import com.indoqa.daisy.dao.DaisyException;
import com.indoqa.daisy.dao.NavigationDao;
import com.indoqa.daisy.entity.Navigation;
import com.indoqa.daisy.entity.NavigationElement;
import com.indoqa.daisy.pipeline.DaisyGenerator;

@Repository
public class NavigationDaoImpl extends AbstractDaisyDao implements NavigationDao {

    public Navigation get(String id, Locale locale) {
        return new Navigation().setRoot(this.parseDocument(this.getNavigationDocument(id, locale)));
    }

    @Override
    public String getNavDocId() {
        return super.getNavDocId();
    }

    protected String getNavigationDocument(String id, Locale locale) {
        // prepare components
        DaisyGenerator daisyGenerator = new DaisyGenerator();
        daisyGenerator.setDocumentId(id);
        daisyGenerator.setIsNavDoc(true);
        daisyGenerator.setDaisyAccessFacade(this.getDaisyRepositoryAccessFacade());
        daisyGenerator.setCocoonSettings(this.getSettings());

        // setup the pipeline
        Pipeline<SAXPipelineComponent> daisyPipeline = new NonCachingPipeline<SAXPipelineComponent>();
        daisyPipeline.addComponent(daisyGenerator);
        daisyPipeline.addComponent(XMLSerializer.createXMLSerializer());

        // run the pipeline (throw away the output)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        daisyPipeline.setup(baos);
        try {
            daisyPipeline.execute();
            return new String(baos.toByteArray());
        } catch (Exception e) {
            throw new DaisyException("Can't execute a Cocoon pipeline that accesses a Daisy document. id=" + id);
        }
    }

    private NavigationElement parseDocument(String navigationDocument) {
        Pipeline<SAXPipelineComponent> pipeline = new NonCachingPipeline<SAXPipelineComponent>();

        final Stack<NavigationElement> navElStack = new Stack<NavigationElement>();
        final NavigationElement root = NavigationElement.createRoot();
        navElStack.push(root);

        pipeline.addComponent(new XMLGenerator(navigationDocument));
        pipeline.addComponent(new AbstractSAXSerializer() {

            @Override
            public void endElement(String uri, String loc, String raw) throws SAXException {
                if ("doc".equals(loc) || "group".equals(loc)) {
                    navElStack.pop();
                }
            }

            @Override
            public void startElement(String uri, String loc, String raw, Attributes a) throws SAXException {
                if ("doc".equals(loc) || "group".equals(loc)) {
                    NavigationElement parent = navElStack.peek();
                    NavigationElement navigationElement = new NavigationElement(parent);
                    navElStack.push(navigationElement);

                    navigationElement.setDocumentId(a.getValue("documentId"));
                    navigationElement.setId(this.getId(a));
                    navigationElement.setLabel(a.getValue("label"));
                    navigationElement.setPath(this.calcPath(a, parent.getPath()));
                    navigationElement.setOriginalPath(this.calcOriginalPath(a));
                }
            }

            private String calcOriginalPath(Attributes a) {
                String originalPath = a.getValue("path");

                if (originalPath.startsWith("/")) {
                    return originalPath.substring(1);
                }

                return originalPath;
            }

            private String calcPath(Attributes a, String parentPath) {
                String id = this.getId(a);
                String path = id;

                if (id != null && id.startsWith("qn")) {
                    id = id.substring(2);
                }

                if (NumberUtils.isDigits(id)) {
                    path = a.getValue("label");
                }

                String calcPath = parentPath + "/" + URLStringUtils.clean(path);
                if (calcPath.startsWith("/")) {
                    return calcPath.substring(1);
                }
                return calcPath;
            }

            private String getId(Attributes a) {
                return a.getValue("id");
            }
        });

        pipeline.setup(new NullOutputStream());
        try {
            pipeline.execute();
        } catch (Exception e) {
            throw new DaisyException(e);
        }

        return root;
    }
}
