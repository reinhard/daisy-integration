package com.indoqa.daisy.wicket;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WicketURLDecoder;
import org.apache.wicket.protocol.http.request.WebErrorCodeResponseTarget;
import org.apache.wicket.protocol.http.servlet.AbortWithWebErrorCodeException;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.coding.AbstractRequestTargetUrlCodingStrategy;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Time;

import com.indoqa.daisy.entity.BinaryDocument;
import com.indoqa.daisy.service.ContentService;

public class DaisyBinaryPartsUrlCodingStrategy extends AbstractRequestTargetUrlCodingStrategy {

    @SpringBean
    protected ContentService contentService;

    protected final String partType;

    protected final String mountPath;

    public DaisyBinaryPartsUrlCodingStrategy(String mountPath, String partType) {
        super(mountPath);

        this.mountPath = mountPath;
        this.partType = partType;

        InjectorHolder.getInjector().inject(this);
    }

    @Override
    public IRequestTarget decode(RequestParameters requestParameters) {
        return new IRequestTarget() {

            @Override
            public void detach(RequestCycle requestCycle) {
                // nothing to do
            }

            @Override
            public void respond(RequestCycle requestCycle) {
                String path = WicketURLDecoder.PATH_INSTANCE.decode(RequestCycle.get().getRequest().getPath());

                int cutOff = DaisyBinaryPartsUrlCodingStrategy.this.mountPath.length() + 1;
                if (cutOff > path.length()) {
                    this.display404();
                }

                path = path.substring(cutOff);
                int sep = path.indexOf('/');
                if (sep < 0 || path.length() == 0) {
                    this.display404();
                }

                String id = path.substring(0, sep);
                String fileName = path.substring(sep + 1);

                BinaryDocument binaryDocument = DaisyBinaryPartsUrlCodingStrategy.this.contentService.getBinaryDocument(id,
                        DaisyBinaryPartsUrlCodingStrategy.this.partType, fileName, Session.get().getLocale());

                if (binaryDocument == null) {
                    this.display404();
                    return;
                }

                try {
                    long lastModified = binaryDocument.getLastModified();

                    if (requestCycle instanceof WebRequestCycle) {
                        HttpServletRequest httpServletRequest = ((WebRequestCycle) requestCycle).getWebRequest()
                                .getHttpServletRequest();
                        long ifModifiedSince = httpServletRequest.getDateHeader("If-Modified-Since");
                        if (ifModifiedSince > 0 && lastModified / 1000 >= ifModifiedSince / 1000) {
                            DaisyBinaryPartsUrlCodingStrategy.this.getHttpServletResopnse().setStatus(
                                    HttpServletResponse.SC_NOT_MODIFIED);
                            return;
                        }
                    }

                    Response response = requestCycle.getResponse();
                    response.setLastModifiedTime(Time.valueOf(lastModified));
                    response.setContentType(binaryDocument.getMimeType());
                    response.setContentLength(binaryDocument.getContent().length);
                    response.getOutputStream().write(binaryDocument.getContent());
                } catch (IOException e) {
                    WebApplication.get().getExceptionSettings().setUnexpectedExceptionDisplay(
                            IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
                    RequestCycle.get().setRequestTarget(new WebErrorCodeResponseTarget(500));
                    throw new AbortWithWebErrorCodeException(500);
                }
            }

            private void display404() {
                WebApplication.get().getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
                RequestCycle.get().setRequestTarget(new WebErrorCodeResponseTarget(404));

                throw new AbortWithWebErrorCodeException(404);
            }
        };
    }

    @Override
    public CharSequence encode(IRequestTarget requestTarget) {
        return null;
    }

    @Override
    public boolean matches(IRequestTarget requestTarget) {
        return false;
    }

    protected HttpServletResponse getHttpServletResopnse() {
        return ((WebRequestCycle) RequestCycle.get()).getWebResponse().getHttpServletResponse();
    }
}
