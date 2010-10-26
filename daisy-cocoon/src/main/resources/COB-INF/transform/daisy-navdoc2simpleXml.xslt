<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: daisy-navdoc2simpleXml.xslt 1548 2009-01-03 21:06:04Z rpoetz $ -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:p="http://outerx.org/daisy/1.0#publisher"
  xmlns:d="http://outerx.org/daisy/1.0" xmlns:n="http://outerx.org/daisy/1.0#navigation" version="1.0">
  
  <xsl:param name="partId" />
  
  <xsl:template match="/">
    <resources>
      <xsl:apply-templates select="/p:publisherResponse/n:navigationTree//n:doc" />
    </resources>
  </xsl:template>
  
  <xsl:template match="n:doc">
    <resource id="{@id}" documentId="{@documentId}"  branchId="{branchId}" languageid="{@languageId}" label="{@label}" path="{@path}" />
  </xsl:template>
</xsl:stylesheet>