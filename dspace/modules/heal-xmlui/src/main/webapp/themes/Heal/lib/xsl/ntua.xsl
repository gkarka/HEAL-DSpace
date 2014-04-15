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

    <!--<xsl:template name="pick-label">
      <xsl:choose>
            <xsl:when test="dri:field/dri:label">
                <label class="ds-form-label">
                        <xsl:choose>
                                <xsl:when test="./dri:field/@id">
                                        <xsl:attribute name="for">
                                                <xsl:value-of select="translate(./dri:field/@id,'.','_')"/>
                                        </xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise></xsl:otherwise>
                        </xsl:choose>
                    <xsl:apply-templates select="dri:field/dri:label" mode="formComposite"/>
                    <xsl:text>:</xsl:text>
                </label>
            </xsl:when>
            <xsl:when test="string-length(string(preceding-sibling::*[1][local-name()='label'])) > 0">
                <xsl:choose>
                        <xsl:when test="./dri:field/@id">
                                <label>
                                        <xsl:apply-templates select="preceding-sibling::*[1][local-name()='label']"/>
                                    <xsl:text>:</xsl:text>
                                </label>
                        </xsl:when>
                        <xsl:otherwise>
                                <span>
                                        <xsl:apply-templates select="preceding-sibling::*[1][local-name()='label']"/>
                                    <xsl:text>:</xsl:text>
                                </span>
                        </xsl:otherwise>
                </xsl:choose>
                
            </xsl:when>
            <xsl:when test="dri:field">
                <xsl:choose>
                        <xsl:when test="preceding-sibling::*[1][local-name()='label']">
                                <label class="ds-form-label">
                                        <xsl:choose>
                                                <xsl:when test="./dri:field/@id">
                                                        <xsl:attribute name="for">
                                                                <xsl:value-of select="translate(./dri:field/@id,'.','_')"/>
                                                        </xsl:attribute>
                                                </xsl:when>
                                                <xsl:otherwise></xsl:otherwise>
                                        </xsl:choose>
                                    <xsl:apply-templates select="preceding-sibling::*[1][local-name()='label']"/>&#160;
                                </label>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:apply-templates select="preceding-sibling::*[1][local-name()='label']"/>&#160;
                                -<xsl:apply-templates select="preceding-sibling::*[1][local-name()='label']"/>&#160;-
                            </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                --><!-- If the label is empty and the item contains no field, omit the label. This is to
                    make the text inside the item (since what else but text can be there?) stretch across
                    both columns of the list. --><!--
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="dri:field/@required='yes'"><span style="color:red">*</span></xsl:if>
    </xsl:template>-->

</xsl:stylesheet>
