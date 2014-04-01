<xsl:stylesheet xmlns:i18n="http://apache.org/cocoon/i18n/2.1"
	xmlns:dri="http://di.tamu.edu/DRI/1.0/"
	xmlns:mets="http://www.loc.gov/METS/"
	xmlns:xlink="http://www.w3.org/TR/xlink/"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:dim="http://www.dspace.org/xmlns/dspace/dim"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:mods="http://www.loc.gov/mods/v3"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="i18n dri mets xlink xsl dim xhtml mods dc">

  <xsl:output indent="yes"/>
  
  <xsl:template match="dri:help" mode="compositeComponent">
    <span class="composite-help">
      <xsl:attribute name="help">
        <xsl:value-of select="."/>
      </xsl:attribute>
      <xsl:attribute name="i18n:attr">help</xsl:attribute>
      <xsl:apply-templates/>
    </span>
  </xsl:template>

  <xsl:template match="dri:list[@n='externalLinks']/dri:item/dri:figure">
    <xsl:if test="@target">
      <a target="_blank">
        <xsl:attribute name="href">
          <xsl:value-of select="@target"/>
        </xsl:attribute>
        <xsl:if test="@title">
          <xsl:attribute name="title">
            <xsl:value-of select="@title"/>
          </xsl:attribute>
        </xsl:if>
        <xsl:if test="@rend">
          <xsl:attribute name="class">
            <xsl:value-of select="@rend"/>
          </xsl:attribute>
        </xsl:if>
        <img>
          <xsl:attribute name="src">
            <xsl:value-of select="@source"/>
          </xsl:attribute>
          <xsl:attribute name="alt">
            <xsl:apply-templates />
          </xsl:attribute>
        </img>
        <xsl:attribute name="border">
          <xsl:text>none</xsl:text>
        </xsl:attribute>
      </a>
    </xsl:if>
    <xsl:if test="not(@target)">
      <img>
        <xsl:attribute name="src">
          <xsl:value-of select="@source"/>
        </xsl:attribute>
        <xsl:attribute name="alt">
          <xsl:apply-templates />
        </xsl:attribute>
      </img>
    </xsl:if>
  </xsl:template>


</xsl:stylesheet>
