<?xml version='1.0'?>
<!--
  Copyright 2002-2005 The Apache Software Foundation or its licensors,
  as applicable.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:strip-space elements="*"/>


<xsl:template match="rss">
  <document>
    <header>
      <title><xsl:value-of select="channel/title"/></title>
    </header>
    <body>
      <xsl:apply-templates select="channel"/>
    </body>
  </document>
</xsl:template>

<xsl:template match="channel">
  <section>
    <title><xsl:value-of select="title" disable-output-escaping="yes"/></title>
    <xsl:apply-templates select="item"/>
  </section>
</xsl:template>

<xsl:template match="item">
    <p class="itemTitle"><xsl:value-of select="title" disable-output-escaping="yes"/></p>
    <p  class="itemLink"><link href="{link}"><xsl:value-of select="link"/></link></p>
    <p  class="itemDescription"><xsl:value-of select="description" disable-output-escaping="yes"/></p>
</xsl:template>

</xsl:stylesheet>
