<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:daisy="http://outerx.org/daisy/1.0" exclude-result-prefixes="daisy">

  <xsl:param name="partName" />

  <xsl:template match="/">
    <div>
      <xsl:copy-of select="/daisy:document/daisy:parts/daisy:part[@name=$partName]/html/body/*" />
    </div>
  </xsl:template>

</xsl:stylesheet>
