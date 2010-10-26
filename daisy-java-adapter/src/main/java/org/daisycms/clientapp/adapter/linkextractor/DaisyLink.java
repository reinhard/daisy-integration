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
package org.daisycms.clientapp.adapter.linkextractor;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.daisycms.clientapp.adapter.DaisyDocument;

public class DaisyLink {
    
    private long docId;
    private String name;    
    private String branchId;
    private String languageId;
    private String path;
    private DaisyDocument parent;

    
    public DaisyLink(final long docId, final String name, final String branchId, final String languageId, final String path, final DaisyDocument parent) {
        Validate.isTrue(docId > 0, "The document id must be a positive number of type long.");   
        this.docId = docId;
        this.name = name;
        if("".equals(branchId) || branchId == null) {
            this.branchId = "1";
        } else {
            this.branchId = branchId;
        }
        if("".equals(languageId) || languageId == null) {
            this.languageId = "1";
        } else {
            this.languageId = languageId;
        }
        this.path = path;
        this.parent = parent;
    }

    public long getDocId() {
        return this.docId;
    }

    public String getName() {
        return this.name;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public String getLanguageId() {
        return this.languageId;
    }
    
    public String getPath() {
        return this.path;
    }

    public DaisyDocument getParent() {
        return this.parent;
    }
    
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof DaisyLink)) { // if o == null, instance of is false
            return false;
        }
        DaisyLink comp = (DaisyLink) o;        
        return new EqualsBuilder()
            .append(this.docId, comp.docId)
            .append(this.branchId, comp.branchId)
            .append(this.languageId, comp.languageId)
            .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.docId)
            .append(this.branchId)
            .append(this.languageId)
            .toHashCode();
    }
    
    public String toString() {
        return "DaisyLink[id=" + this.getDocId() + " branchId=" + this.getBranchId() + 
            " languageId=" + this.getLanguageId() + " parent=" + this.getParent() + "]";
    }

    public static String createUniqeFileName(DaisyLink link) {
        return link.getDocId() + "_" + link.getBranchId() + "_" + link.getLanguageId();
    }
    
}
