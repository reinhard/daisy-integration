package com.indoqa.daisy.pipeline;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import org.apache.cocoon.pipeline.ProcessingException;
import org.apache.cocoon.pipeline.caching.CacheKey;
import org.apache.cocoon.pipeline.component.CachingPipelineComponent;
import org.apache.cocoon.pipeline.component.Finisher;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.outerj.daisy.repository.Document;
import org.outerj.daisy.repository.Part;
import org.outerj.daisy.repository.RepositoryException;

public class DaisyBinaryPartReader extends AbstractDaisyProducer implements Finisher, CachingPipelineComponent {

    private Part attachementDataPart;
    private String fileName;
    private OutputStream outputStream;
    private long lastModificationTime;

    @Override
    public CacheKey constructCacheKey() {
        return new DaisyDocumentPartCacheKey(this.documentId, this.part, this.getDaisyDocument().getDocument().getLastModified());
    }

    @Override
    public void execute() {
        Document document = null;
        try {
            document = this.getDaisyDocument().getDocument();
            this.attachementDataPart = document.getPart(this.part);
        } catch (Exception e) {
            throw new DaisyDocumentNotFoundException("Can't find data attachement of document with ID " + this.documentId);
        }

        if (this.fileName == null || !this.fileName.equals(this.attachementDataPart.getFileName())) {
            throw new DaisyDocumentNotFoundException("Can't find data attachement of document with ID " + this.documentId);
        }

        try {
            this.lastModificationTime = document.getLastModified().getTime();
            IOUtils.copy(this.attachementDataPart.getDataStream(), this.outputStream);
        } catch (IOException e) {
            throw new ProcessingException("Can't read the attachment data for doc ID " + this.documentId + ".", e);
        } catch (RepositoryException e) {
            throw new ProcessingException("Can't access the Daisy repository for doc ID " + this.documentId + ".", e);
        }
    }

    @Override
    public String getContentType() {
        return this.attachementDataPart.getMimeType();
    }

    public long getLastModificationTime() {
        return this.lastModificationTime;
    }

    @Override
    public void setConfiguration(Map<String, ? extends Object> configuration) {
        super.setConfiguration(configuration);

        this.fileName = (String) configuration.get("fileName");
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void setOutputStream(OutputStream os) {
        this.outputStream = os;
    }

    private static final class DaisyDocumentPartCacheKey implements CacheKey {

        private static final long serialVersionUID = 1L;

        private final String id;
        private String jmxGroupName;
        private final String partName;
        private final Date timestamp;

        public DaisyDocumentPartCacheKey(String id, String partName, Date date) {
            this.id = id;
            this.partName = partName;
            this.timestamp = date;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof DaisyDocumentPartCacheKey)) {
                return false;
            }

            DaisyDocumentPartCacheKey other = (DaisyDocumentPartCacheKey) obj;
            return this.id == other.id && this.partName.equals(other.partName);
        }

        @Override
        public String getJmxGroupName() {
            return this.jmxGroupName;
        }

        @Override
        public long getLastModified() {
            return this.getTimestamp().getTime();
        }

        public Date getTimestamp() {
            return this.timestamp;
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(this.id).append(this.partName).toHashCode();
        }

        @Override
        public boolean hasJmxGroupName() {
            return false;
        }

        @Override
        public boolean isValid(CacheKey cacheKey) {
            if (!this.equals(cacheKey)) {
                return false;
            }

            DaisyDocumentPartCacheKey other = (DaisyDocumentPartCacheKey) cacheKey;
            return this.timestamp == other.timestamp;
        }

        @Override
        public void setJmxGroupName(String jmxGroupName) {
            this.jmxGroupName = jmxGroupName;
        }
    }
}
