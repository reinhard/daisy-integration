package com.indoqa.daisy.dao.impl;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import org.apache.cocoon.pipeline.NonCachingPipeline;
import org.apache.cocoon.pipeline.Pipeline;
import org.apache.cocoon.sax.SAXPipelineComponent;
import org.outerj.daisy.repository.Document;
import org.outerj.daisy.repository.Part;
import org.springframework.stereotype.Repository;

import com.indoqa.daisy.dao.BinaryDocumentDao;
import com.indoqa.daisy.dao.DaisyException;
import com.indoqa.daisy.entity.BinaryDocument;
import com.indoqa.daisy.pipeline.DaisyBinaryPartReader;
import com.indoqa.daisy.pipeline.DaisyDocumentNotFoundException;

@Repository
public class BinaryDocumentDaoImpl extends AbstractDaisyDao implements BinaryDocumentDao {

    @Override
    public BinaryDocument get(String id, Locale locale, String part, String fileName) {
        return this.getBinaryDocument(id, locale, part, fileName);
    }

    @Override
    public String getFileName(String id, String partName, Locale locale) {
        Document jDocument = this.getDaisyRepositoryAccessFacade().getJDocument(id);
        Part part = jDocument.getPart(partName);
        return part.getFileName();
    }

    protected BinaryDocument getBinaryDocument(String id, Locale locale, String part, String fileName) {
        DaisyBinaryPartReader binaryPartReader = new DaisyBinaryPartReader();
        binaryPartReader.setDocumentId(id);
        binaryPartReader.setIsNavDoc(false);
        binaryPartReader.setDaisyAccessFacade(this.getDaisyRepositoryAccessFacade());
        binaryPartReader.setCocoonSettings(this.getSettings());
        binaryPartReader.setPart(part);
        binaryPartReader.setFileName(fileName);

        Pipeline<SAXPipelineComponent> daisyPipeline = new NonCachingPipeline<SAXPipelineComponent>();
        daisyPipeline.addComponent(binaryPartReader);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        daisyPipeline.setup(outputStream);
        try {
            daisyPipeline.execute();
        } catch (DaisyDocumentNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw new DaisyException("Can't execute a Cocoon pipeline that accesses a Daisy document.", e);
        }

        BinaryDocument doc = new BinaryDocument();
        doc.setId(id);
        doc.setLocale(locale);
        doc.setContent(outputStream.toByteArray());
        doc.setMimeType(daisyPipeline.getContentType());
        doc.setLastModified(binaryPartReader.getLastModificationTime());

        return doc;
    }
}
