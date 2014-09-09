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

  <!--<xsl:template match="dri:list[@n='account']">
  </xsl:template>-->
  
  <xsl:template match="dri:item/dri:field[@id='aspect.discovery.SimpleSearch.field.scope']">
  </xsl:template>

  <!--<xsl:template match="dri:body/dri:div[@n='community-view']">
  </xsl:template>-->

  <!--<xsl:template match="dri:referenceSet">
  </xsl:template>-->

  <xsl:template match="dri:referenceSet[dri:reference/@url = '/metadata/handle/123456789/474/mets.xml']">
    <referenceSet type="summaryList" rend="hierarchy">
      <head>
        <i18n:text catalogue="default">xmlui.ArtifactBrowser.CommunityViewer.head_sub_communities</i18n:text>
      </head>
      <reference type="DSpace Community" url="/metadata/handle/123456789/483/mets.xml" repositoryID="123456789"/>
      <reference type="DSpace Community" url="/metadata/handle/123456789/482/mets.xml" repositoryID="123456789"/>      
      <xref target="http://journals.lib.ntua.gr/index.php/pyrforos">
        Πυρφόρος
      </xref>      
    </referenceSet>
  </xsl:template>



  <xsl:template match="dri:options">
    <xsl:copy>
      <xsl:apply-templates />

      <list id="guideLinks" n="guideLinks">
        <head>
          <i18n:text>xmlui.guide.head</i18n:text>
        </head>
        <item>
          <xref>
            <xsl:attribute name="target"><xsl:value-of select="/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='contextPath'][not(@qualifier)]"/>/page/submission-guide</xsl:attribute>
            <i18n:text>xmlui.guide.submission</i18n:text>
          </xref>
        </item>
        <item>
          <xref>
            <xsl:attribute name="target"><xsl:value-of select="/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='contextPath'][not(@qualifier)]"/>/page/help</xsl:attribute>
            <i18n:text>xmlui.guide.help</i18n:text>
          </xref>
        </item>
        <item>
          <xref>
            <xsl:attribute name="target"><xsl:value-of select="/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='contextPath'][not(@qualifier)]"/>/page/faq</xsl:attribute>
            <i18n:text>xmlui.guide.faq</i18n:text>
          </xref>
        </item>      
      </list>
      
        <list id="externalLinks" n="externalLinks">
        <head>
          <i18n:text>xmlui.links.head</i18n:text>
        </head>
        <item>          
          <figure target="http://www.sherpa.ac.uk/romeo/‎" rend="_new">
            <xsl:attribute name="source"><xsl:value-of select="/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='contextPath'][not(@qualifier)]"/>/static/icons/SHERPA-RoMEO-long-logo.gif</xsl:attribute>
            SHERPA/RoMEO
          </figure>
        </item>
        <item>          
          <figure target="http://phdtheses.ekt.gr/eadd/" rend="_new">
            <xsl:attribute name="source"><xsl:value-of select="/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='contextPath'][not(@qualifier)]"/>/static/icons/logo_eadd.png</xsl:attribute>
            ΕΚΤ - Εθνικό Αρχείο Διδακτορικών Διατριβών
          </figure>
        </item>
        <item>          
          <figure target="https://www.openaire.eu/" rend="_new">
            <xsl:attribute name="source"><xsl:value-of select="/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='contextPath'][not(@qualifier)]"/>/static/icons/OpenAIREplus_logo.png</xsl:attribute>
            OpenAIRE
          </figure>
        </item>
        <item>          
          <figure target="http://www.europeana.eu/portal/" rend="_new">
            <xsl:attribute name="source"><xsl:value-of select="/dri:document/dri:meta/dri:pageMeta/dri:metadata[@element='contextPath'][not(@qualifier)]"/>/static/icons/europeana-logo-2.png</xsl:attribute>
            Europeana
          </figure>
        </item>          
        <!--<item>
          <figure source="http://www.ntua.gr/gold_pyrforos_small301.png" target="http://www.ntua.gr" rend="_new">NTUA</figure>
        </item>-->
      </list>
    
    </xsl:copy>
  
  </xsl:template>
</xsl:stylesheet>
