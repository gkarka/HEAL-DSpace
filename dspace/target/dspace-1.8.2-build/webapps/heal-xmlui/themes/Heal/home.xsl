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

  <xsl:template match="dri:body/dri:div[@n='comunity-browser']">
  </xsl:template>

  <xsl:template match="dri:body/dri:div[@n='front-page-search']">
  </xsl:template>

  
  <xsl:template match="dri:body">
    <xsl:copy>
      <xsl:apply-templates />

      <div id="aspect.artifactbrowser.CommunityViewer.div.community-view" rend="secondary" n="community-view">
        <referenceSet id="aspect.artifactbrowser.CommunityViewer.referenceSet.community-view" n="community-view" type="detailView">
          <reference repositoryID="123456789" type="DSpace Community" url="/metadata/handle/123456789/1/mets.xml">
            <referenceSet rend="hierarchy" type="summaryList">
              <head>
                <i18n:text catalogue="default">xmlui.ArtifactBrowser.CommunityViewer.head_sub_collections</i18n:text>
              </head>
              <reference repositoryID="123456789" type="DSpace Collection" url="/metadata/handle/123456789/2/mets.xml"/>
            </referenceSet>
          </reference>
        </referenceSet>
      </div>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
