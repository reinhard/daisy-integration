<?xml version="1.0" encoding="UTF-8"?>
<!--
 Licensed to the Outerthought bvba and Schaubroeck NV under one
 or more contributor license agreements. See the NOTICE file
 distributed with this work for additional information regarding 
 copyright ownership.  Outerthought bvba and Schaubroeck NV license 
 this file to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License. 
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:p="http://outerx.org/daisy/1.0#publisher"
  xmlns:d="http://outerx.org/daisy/1.0"
  exclude-result-prefixes="p d">
  
  <xsl:output method="xml"/>
  <xsl:output omit-xml-declaration="no"/>
  <xsl:preserve-space elements="pre"/>
  
  <xsl:param name="author"/>

  <xsl:template match="/">
    <links>
      <xsl:apply-templates select="//p:linkInfo"/>
    </links>
  </xsl:template>

  <!--+
      | Get all links
      +-->
  <xsl:template match="p:linkInfo">
    <xsl:copy-of select="."/> 
  </xsl:template>
  
  <!--+
      | default templates
      +-->
  <xsl:template match="*|@*|node()" priority="-2">
    <xsl:apply-templates select="@*|node()"/>
  </xsl:template>

</xsl:stylesheet>
