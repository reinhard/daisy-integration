package org.daisycms.clientapp.cocoon;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import org.apache.cocoon.pipeline.ProcessingException;
import org.apache.cocoon.pipeline.caching.CacheKey;
import org.apache.cocoon.pipeline.component.CachingPipelineComponent;
import org.apache.cocoon.pipeline.component.Finisher;
import org.apache.commons.io.IOUtils;
import org.outerj.daisy.repository.Document;
import org.outerj.daisy.repository.Part;
import org.outerj.daisy.repository.RepositoryException;

public class DaisyBinaryPartReader extends AbstractDaisyProducer implements Finisher, CachingPipelineComponent {

    private Part attachementDataPart;
    private String fileName;
    private OutputStream outputStream;
    private long lastModificationTime;

    public CacheKey constructCacheKey() {
        return new DaisyDocumentPartCacheKey(this.documentId, this.part, this.getDaisyDocument().getDocument()
                .getLastModified());
    }

    public void execute() {
        Document document = null;
        try {
            document = this.getDaisyDocument().getDocument();
            this.attachementDataPart = document.getPart(this.part);
        } catch (Exception e) {
            throw new DaisyDocumentNotFoundException("Can't find data attachement of document with ID "
                    + this.documentId);
        }

        if (this.fileName == null || !this.fileName.equals(this.attachementDataPart.getFileName())) {
            throw new DaisyDocumentNotFoundException("Can't find data attachement of document with ID "
                    + this.documentId);
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

    public void setOutputStream(OutputStream os) {
        this.outputStream = os;
    }

    private static final class DaisyDocumentPartCacheKey implements CacheKey {

        private static final long serialVersionUID = 1L;

        private final long id;
        private String jmxGroupName;
        private final String partName;
        private final Date timestamp;

        public DaisyDocumentPartCacheKey(long id, String partName, Date date) {
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

        public String getJmxGroupName() {
            return this.jmxGroupName;
        }

        public long getLastModified() {
            return this.getTimestamp().getTime();
        }

        public Date getTimestamp() {
            return this.timestamp;
        }

        public boolean hasJmxGroupName() {
            return false;
        }

        public boolean isValid(CacheKey cacheKey) {
            if (!this.equals(cacheKey)) {
                return false;
            }

            DaisyDocumentPartCacheKey other = (DaisyDocumentPartCacheKey) cacheKey;
            return this.timestamp == other.timestamp;
        }

        public void setJmxGroupName(String jmxGroupName) {
            this.jmxGroupName = jmxGroupName;
        }
    }
}
