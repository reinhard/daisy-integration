/*
 * Licensed to the Outerthought bvba and Schaubroeck NV under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding
 * copyright ownership.  Outerthought bvba and Schaubroeck NV license
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.indoqa.daisy.dao.pipeline;

public class DaisyPartReader {

    // private OutputStream outputStream;
    // private Response response;
    // private BlobInfo blobInfo;
    // private DaisyRepositoryAccessFacade facade;
    // private Settings cocoonSettings;
    //
    // public void dispose() {
    // if (this.blobInfo != null) {
    // blobInfo.dispose();
    // }
    // }
    //
    // public void generate() throws IOException, SAXException, ProcessingException {
    // response.setIntHeader("Content-Length", (int)blobInfo.getSize());
    // try {
    // IOUtils.copy(blobInfo.getInputStream(), outputStream);
    // } catch (RepositoryException e) {
    // throw new ProcessingException(e);
    // }
    // }
    //
    // public long getLastModified() {
    // return blobInfo.getLastModified().getTime();
    // }
    //
    // public void setup(SourceResolver sourceResolver, Map objectModel, String s, Parameters parameters)
    // throws ProcessingException, SAXException, IOException {
    // boolean useLast = false;
    // if (SettingsHelper.useLast(this.cocoonSettings)) {
    // useLast = true;
    // }
    // this.blobInfo = new BlobInfoHelper(this.facade, parameters, useLast).getBlogInfo();
    // this.response = ObjectModelHelper.getResponse(objectModel);
    // }
    //
    // public void setOutputStream(OutputStream outputStream) throws IOException {
    // this.outputStream = outputStream;
    // }
    //
    // public String getMimeType() {
    // return blobInfo.getMimeType();
    // }
    //
    // public boolean shouldSetContentLength() {
    // return false;
    // }
    //
    // public void setDaisyAccessFacade(DaisyRepositoryAccessFacade facade) {
    // this.facade = facade;
    // }
    //
    // public void setCocoonSettings(Settings cSettings) {
    // this.cocoonSettings = cSettings;
    // }
}
