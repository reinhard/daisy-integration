<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: daisy-part-extractor.xslt 1550 2009-01-04 23:15:03Z rpoetz $ -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:p="http://outerx.org/daisy/1.0#publisher"
  xmlns:d="http://outerx.org/daisy/1.0" version="1.0">
  
  <xsl:param name="part" />
  <xsl:param name="alternativeTitle"/>
  
  <xsl:template match="/">
    <xsl:variable name="doc" select="/p:publisherResponse/p:document/p:preparedDocuments/p:preparedDocument/p:publisherResponse/d:document"/>
    <xsl:variable name="part" select="$doc/d:parts/d:part[@name=$part]"/>
    <xsl:choose>
      <xsl:when test="$part">
        <xsl:variable name="atText" select="$doc/d:fields/d:field[@name=$alternativeTitle]/d:string/text()" />
        <html>
          <head>
            <xsl:choose>
              <xsl:when test="$alternativeTitle and $atText">
                <title><xsl:value-of select="$doc/d:fields/d:field[@name=$alternativeTitle]/d:string/text()" /></title>
              </xsl:when>
              <xsl:otherwise>
                <title><xsl:value-of select="$doc/@name"/></title>              
              </xsl:otherwise>
            </xsl:choose>
          </head>
          <body class="daisy-doc">
            <xsl:copy-of select="$part/html/body/*" />
          </body>
        </html>
      </xsl:when>
      <xsl:otherwise>
        <html>
          <head/>
          <body class="daisy-empty"/>
        </html>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>