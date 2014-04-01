<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:i18n="http://apache.org/cocoon/i18n/2.1"
	xmlns="http://di.tamu.edu/DRI/1.0/"
  xmlns:dri="http://di.tamu.edu/DRI/1.0/"
	xmlns:mets="http://www.loc.gov/METS/"
	xmlns:xlink="http://www.w3.org/TR/xlink/"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:dim="http://www.dspace.org/xmlns/dspace/dim"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:mods="http://www.loc.gov/mods/v3"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	exclude-result-prefixes="i18n mets xlink xsl dim xhtml mods dc">
 
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="@* | node()">
      <xsl:copy>
          <xsl:apply-templates select="@* | node()"/>
      </xsl:copy>    
  </xsl:template>
        
  <xsl:template match="dri:list[@n='statistics']">
  </xsl:template>
  
  <xsl:template match="dri:options">
    <xsl:copy>
      <xsl:apply-templates />
      
      <list id="externalLinks" n="externalLinks">
        <head>Links</head>
        <item>
          <xref target="http://www.sherpa.ac.uk/romeo/‎">SHERPA/RoMEO</xref>
        </item>
        <item>
          <figure source="http://www.ntua.gr/gold_pyrforos_small301.png" target="http://www.ntua.gr" rend="_new">NTUA</figure>
        </item>
      </list>
    
    </xsl:copy>
  
  </xsl:template>
</xsl:stylesheet>
