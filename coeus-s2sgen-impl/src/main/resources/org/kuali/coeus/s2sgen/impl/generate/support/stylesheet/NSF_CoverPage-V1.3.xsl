<!--
   - Kuali Coeus, a comprehensive research administration system for higher education.
   - 
   - Copyright 2005-2015 Kuali, Inc.
   - 
   - This program is free software: you can redistribute it and/or modify
   - it under the terms of the GNU Affero General Public License as
   - published by the Free Software Foundation, either version 3 of the
   - License, or (at your option) any later version.
   - 
   - This program is distributed in the hope that it will be useful,
   - but WITHOUT ANY WARRANTY; without even the implied warranty of
   - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   - GNU Affero General Public License for more details.
   - 
   - You should have received a copy of the GNU Affero General Public License
   - along with this program.  If not, see <http://www.gnu.org/licenses/>.
 -->
<xsl:stylesheet version="1.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:fo="http://www.w3.org/1999/XSL/Format" 
xmlns:NSF_CoverPage_1_3="http://apply.grants.gov/forms/NSF_CoverPage_1_3-V1.3"
xmlns:footer="http://apply.grants.gov/system/Footer-V1.0" 
xmlns:att="http://apply.grants.gov/system/Attachments-V1.0" 
xmlns:glob="http://apply.grants.gov/system/Global-V1.0" 
xmlns:globLib="http://apply.grants.gov/system/GlobalLibrary-V2.0" 
xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:variable name="fo:layout-master-set">
		<fo:layout-master-set>
			<fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.4in" margin-right="0.4in">
				<fo:region-body margin-top="0.9in" margin-bottom="0.6in"/>
				<fo:region-before extent=".5in"/>
			</fo:simple-page-master>
		</fo:layout-master-set>
	</xsl:variable>
	<xsl:template match="NSF_CoverPage_1_3:NSF_CoverPage_1_3">
		<fo:root>
			<xsl:copy-of select="$fo:layout-master-set"/>
			<fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
				<fo:static-content flow-name="xsl-region-before">
					<fo:table width="100%" table-layout="fixed">
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell line-height="11pt" display-align="after" text-align="right">
									<fo:block>
										<fo:inline text-align="right" font-size="6px">OMB Number 3145-0058</fo:inline>
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<fo:leader leader-pattern="space"/>
						<fo:leader leader-pattern="space"/>
						<fo:block text-align="center">
							<fo:inline font-weight="bold" font-size="12pt"> National Science Foundation</fo:inline>
						</fo:block>
						<fo:block text-align="center">
							<fo:inline font-weight="bold" font-size="12pt"> Grant Application Cover Page</fo:inline>
						</fo:block>
						<fo:table border-style="solid" border-color="black" space-before.optimum="5pt" space-after.optimum="2pt" border-width="1pt" width="100%" table-layout="fixed">
							<fo:table-column column-width="proportional-column-width(1)"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" border-style="solid" border-before-width="0pt" border-color="black" border-width="1pt">
										<fo:block>
											<fo:table width="100%" table-layout="fixed">
												<fo:table-column column-width=".9in"/>
												<fo:table-column column-width=".9in"/>
												<fo:table-column column-width="2.0in"/>
												<fo:table-column column-width="1.1in"/>
												<fo:table-column column-width="1.1in"/>
												<fo:table-column column-width="proportional-column-width(1)"/>
												<fo:table-body>
													<fo:table-row border-style="solid" border-color="black" border-before-width="0pt" border-width="1pt">
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt" number-columns-spanned="6">
															<fo:block font-size="8pt">Please complete the following NSF forms in conjunction with the relevant Research and Related forms. If you are an organization or individual and you are not registered with NSF FastLane, please complete the Organization and Individual Registration Form in this package.</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt" number-columns-spanned="6">
															<fo:block font-size="8pt" font-weight="bold">1. Funding Opportunity Number</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt" number-columns-spanned="2">
															<fo:block font-size="8pt">*Funding Opportunity Number:&#160;</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" hyphenate="true" language="en">
															<fo:block font-size="8pt">
																<xsl:value-of select="NSF_CoverPage_1_3:FundingOpportunityNumber"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" number-columns-spanned="3">
															<fo:block font-size="8pt">Opportunity closing date:
														
														
															
																<xsl:for-each select="NSF_CoverPage_1_3:DueDate">
																	<xsl:value-of select="format-number(substring(.,6,2), '00')"/>
																	<xsl:text>-</xsl:text>
																	<xsl:value-of select="format-number(substring(.,9,2), '00')"/>
																	<xsl:text>-</xsl:text>
																	<xsl:value-of select="format-number(substring(.,1,4), '0000')"/>
																</xsl:for-each>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</fo:table-body>
											</fo:table>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt" border-style="solid" border-color="black" border-width="1pt">
										<fo:block>
											<fo:table space-before.optimum="1pt" space-after.optimum="2pt" width="100%" table-layout="fixed">
												<fo:table-column column-width="1.1in"/>
												<fo:table-column column-width="proportional-column-width(1)"/>
												<fo:table-column column-width="1.2in"/>
												<fo:table-column column-width="proportional-column-width(1)"/>
												<fo:table-body>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt" number-columns-spanned="4">
															<fo:block font-size="8pt" font-weight="bold">2. NSF Unit Consideration</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt" number-columns-spanned="4">
															<fo:block font-size="8pt">Go to	https://www.fastlane.nsf.gov/pgmannounce.jsp and follow the instructions to find the Division and Program information for this funding opportunity.</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt">
															<fo:block font-size="8pt">*Division Code:</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt" hyphenate="true" language="en">
																<xsl:value-of select="NSF_CoverPage_1_3:NSFUnitConsideration/NSF_CoverPage_1_3:DivisionCode"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt">Division Name:</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt" hyphenate="true" language="en">
																<xsl:value-of select="NSF_CoverPage_1_3:NSFUnitConsideration/NSF_CoverPage_1_3:DivisionName"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt">
															<fo:block font-size="8pt">*Program Code:</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt" hyphenate="true" language="en">
																<xsl:value-of select="NSF_CoverPage_1_3:NSFUnitConsideration/NSF_CoverPage_1_3:ProgramCode"/>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt">Program Name:</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt" hyphenate="true" language="en">
																<xsl:value-of select="NSF_CoverPage_1_3:NSFUnitConsideration/NSF_CoverPage_1_3:ProgramName"/>
															</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</fo:table-body>
											</fo:table>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" border-style="solid" border-color="black" border-width="1pt">
										<fo:block>
											<fo:table space-before.optimum="1pt" space-after.optimum="2pt" width="100%" table-layout="fixed">
												<fo:table-column column-width="proportional-column-width(1)"/>
												<fo:table-column column-width="proportional-column-width(1)"/>
												<fo:table-column column-width="proportional-column-width(1)"/>
												<fo:table-column column-width="proportional-column-width(1)"/>
												<fo:table-body>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt" number-columns-spanned="4">>
														<fo:block font-size="8pt" font-weight="bold">3. Principal Investigator (PI) Information</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt" number-columns-spanned="4">
															<fo:block font-size="8pt">
																<xsl:choose>
																	<xsl:when test="NSF_CoverPage_1_3:PIInfo/NSF_CoverPage_1_3:isCurrentPI !='N: No'">
																		<fo:inline text-decoration="underline" font-family="ZapfDingbats" font-size="8pt" padding-start="1.5pt" padding-end="1.5pt">&#x2714;</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline text-decoration="underline" color="black">
																			<fo:leader leader-length="7pt" leader-pattern="rule"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
															Check here if you are currently serving (or have previously served) as a PI, co-PI or Program Director (PD) on any Federally funded project.</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</fo:table-body>
											</fo:table>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" border-style="solid" border-color="black" border-width="1pt">
										<fo:block>
											<fo:table space-before.optimum="1pt" space-after.optimum="2pt" width="100%" table-layout="fixed">
												<fo:table-column column-width="proportional-column-width(5)"/>
												<fo:table-column column-width="proportional-column-width(45)"/>
												<fo:table-column column-width="proportional-column-width(2)"/>
												<fo:table-column column-width="proportional-column-width(48)"/>
												<fo:table-body>
													<fo:table-row>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="15pt" number-columns-spanned="4">
															<fo:block font-size="8pt" font-weight="bold">4. Other Information</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt" number-columns-spanned="4">
															<fo:block font-size="8pt">Check Appropriate Box (es) if this proposal includes any of the items listed below.</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="15pt">
															<fo:block font-size="8pt">
																<xsl:choose>
																	<xsl:when test="NSF_CoverPage_1_3:OtherInfo/NSF_CoverPage_1_3:isBeginInvestigator !='N: No'">
																		<fo:inline text-decoration="underline" font-family="ZapfDingbats" font-size="8pt" padding-start="1.5pt" padding-end="1.5pt">&#x2714;</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline text-decoration="underline" color="black">
																			<fo:leader leader-length="7pt" leader-pattern="rule"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt">Beginning Investigator (Grant Proposal Guide(GPG), Chapter I.G.2)</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt">
																<xsl:choose>
																	<xsl:when test="NSF_CoverPage_1_3:OtherInfo/NSF_CoverPage_1_3:isDisclosureLobbyingActivities !='N: No'">
																		<fo:inline text-decoration="underline" font-family="ZapfDingbats" font-size="8pt" padding-start="1.5pt" padding-end="1.5pt">&#x2714;</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline text-decoration="underline" color="black">
																			<fo:leader leader-length="7pt" leader-pattern="rule"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt">Disclosure of Lobbying Activities (GPG, Chapter II.C.1.e)</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="15pt">
															<fo:block font-size="8pt">
																<xsl:choose>
																	<xsl:when test="NSF_CoverPage_1_3:OtherInfo/NSF_CoverPage_1_3:isRapidResponseGrant !='N: No'">
																		<fo:inline text-decoration="underline" font-family="ZapfDingbats" font-size="8pt" padding-start="1.5pt" padding-end="1.5pt">&#x2714;</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline text-decoration="underline" color="black">
																			<fo:leader leader-length="7pt" leader-pattern="rule"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt">RAPID (GPG, Chapter II.D.1)</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="1pt" number-rows-spanned="2">
															<fo:block font-size="8pt">
																<xsl:choose>
																	<xsl:when test="NSF_CoverPage_1_3:OtherInfo/NSF_CoverPage_1_3:isHighResolutionGraphics !='N: No'">
																		<fo:inline text-decoration="underline" font-family="ZapfDingbats" font-size="8pt" padding-start="1.5pt" padding-end="1.5pt">&#x2714;</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline text-decoration="underline" color="black">
																			<fo:leader leader-length="7pt" leader-pattern="rule"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="1pt" number-rows-spanned="2">
															<fo:block font-size="8pt">
High Resolution Graphics/Other Graphics Where Exact Color Representation Is Required For Proper Interpretation (GPG, Chapter I.G.1)															</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
																											<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="15pt">
																												<fo:block font-size="8pt">
																													<xsl:choose>
																														<xsl:when test="NSF_CoverPage_1_3:OtherInfo/NSF_CoverPage_1_3:isEarlyConceptGrant !='N: No'">
																															<fo:inline text-decoration="underline" font-family="ZapfDingbats" font-size="8pt" padding-start="1.5pt" padding-end="1.5pt">&#x2714;</fo:inline>
																														</xsl:when>
																														<xsl:otherwise>
																															<fo:inline text-decoration="underline" color="black">
																																<fo:leader leader-length="7pt" leader-pattern="rule"/>
																															</fo:inline>
																														</xsl:otherwise>
																													</xsl:choose>
																												</fo:block>
																											</fo:table-cell>
																											<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="1pt">
																												<fo:block font-size="8pt">EAGER (GPG, Chapter II.D.2)																		</fo:block>
																												<fo:block font-size="4pt"> &#160;</fo:block>
																											</fo:table-cell>
																											
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="15pt">
															<fo:block font-size="8pt">
																<xsl:choose>
																	<xsl:when test="NSF_CoverPage_1_3:OtherInfo/NSF_CoverPage_1_3:isAccomplishmentRenewal !='N: No'">
																		<fo:inline text-decoration="underline" font-family="ZapfDingbats" font-size="8pt" padding-start="1.5pt" padding-end="1.5pt">&#x2714;</fo:inline>
																	</xsl:when>
																	<xsl:otherwise>
																		<fo:inline text-decoration="underline" color="black">
																			<fo:leader leader-length="7pt" leader-pattern="rule"/>
																		</fo:inline>
																	</xsl:otherwise>
																</xsl:choose>
															</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="1pt" padding-after="1pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt">Accomplishment-Based Renewal  (GPG, Chapter V.B)																		</fo:block>
															<fo:block font-size="4pt"> &#160;</fo:block>
														</fo:table-cell>
														
													</fo:table-row>
												</fo:table-body>
											</fo:table>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt">
										<fo:block>
											<fo:table space-before.optimum="1pt" space-after.optimum="2pt" width="100%" table-layout="fixed">
												<fo:table-column column-width="proportional-column-width(1)"/>
												<fo:table-column column-width="proportional-column-width(1)"/>
												<fo:table-body>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt">
															<fo:block font-size="8pt">
																<fo:inline font-weight="bold">5. Additional Single-Copy Documents</fo:inline> &#160;&#160;&#160;Attach PDF Files</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt">
															<fo:block font-size="8pt">File Name:</fo:block>
														</fo:table-cell>
														<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt">
															<fo:block font-size="8pt">Mime Type:</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<xsl:for-each select="NSF_CoverPage_1_3:Single-CopyDocuments/att:AttachedFile">
														<fo:table-row>
															<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="15pt">
																<fo:block font-size="8pt" hyphenate="true" language="en">
																	<xsl:value-of select="att:FileName"/>
																</fo:block>
															</fo:table-cell>
															<fo:table-cell padding-before="2pt" padding-after="2pt" padding-end="1pt" padding-start="1pt">
																<fo:block font-size="8pt" hyphenate="true" language="en">
																	<xsl:value-of select="att:MimeType"/>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
													</xsl:for-each>
												</fo:table-body>
											</fo:table>
										</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
