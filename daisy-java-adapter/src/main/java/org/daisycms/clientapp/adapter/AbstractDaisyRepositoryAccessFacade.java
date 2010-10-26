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
package org.daisycms.clientapp.adapter;

import org.outerj.daisy.publisher.clientimpl.RemotePublisherProvider;
import org.outerj.daisy.repository.Credentials;
import org.outerj.daisy.repository.Repository;
import org.outerj.daisy.repository.clientimpl.RemoteRepositoryImpl;
import org.outerj.daisy.repository.clientimpl.RemoteRepositoryManager;

abstract class AbstractDaisyRepositoryAccessFacade {

    private RemoteRepositoryManager repositoryManager;
    
    private Repository templateRepository;

    private Credentials remoteAccessCredentials;

    private Credentials repoCredentials;

    private String serverUrl;

    private int numOfRetries = 3;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setNumOfRetries(int numOfRetries) {
        this.numOfRetries = numOfRetries;
    }

    public void setRepoCredentials(DaisyCredentials c) {
        this.repoCredentials = new Credentials(c.getLogin(), c.getPassword());
    }

    public void setRemoteAccessCredentials(DaisyCredentials c) {
        this.remoteAccessCredentials = new Credentials(c.getLogin(), c.getPassword());
    }

    protected int getNumOfRetries() {
        return this.numOfRetries;
    }

    protected Repository getRepository() {
        if (this.repositoryManager == null) {
            try {
                this.repositoryManager = new RemoteRepositoryManager(this.serverUrl, this.remoteAccessCredentials);
                this.repositoryManager.registerExtension("Publisher", new RemotePublisherProvider());
                this.templateRepository = this.repositoryManager.getRepository(this.repoCredentials);
            } catch (Exception e) {
                throw new DaisyAccessException("Problems occurred while accessing the Daisy repository server.", e);
            }
        }
        return (Repository)((RemoteRepositoryImpl) this.templateRepository).clone();
    }

}
