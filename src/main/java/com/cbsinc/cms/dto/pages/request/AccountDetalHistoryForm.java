package com.cbsinc.cms.dto.pages.request;

/*
 * /cmsmanhattan/WebContent/xsl/shops.online-spb.com/en/accounthistorydetal.xsl
 * 
 *   <FORM name="searchcreform"  action="OrderList.jsp"  method="POST">				
						  	   							
									           <TABLE cellSpacing="0" cellPadding="0" width="100%" align="center"  class="bacgraundBoxLeft" border="0" rightmargin="0"  leftmargin="0" topmargin="0">
												        
													        <TBODY>
													        <TR>
													          <TD align="middle">
													            <TABLE class="pollstableborder_s3" cellSpacing="0" cellPadding="0" border="0">
													            <TBODY>
													              <TR>
													                 <TD class="width sectiontableentry2" vAlign="top">
													                	<LABEL for="voteid25">from date: </LABEL>
													                </TD>
													              </TR>
													              <INPUT SIZE="0"  AUTOCOMPLETE="off" TYPE="HIDDEN" NAME="searchquery" value="2"></INPUT>
													              <TR>
													                 <TD class="width sectiontableentry2" vAlign="top">
													                 	<INPUT SIZE="0"  AUTOCOMPLETE="off" TYPE="HIDDEN" NAME="datefrom" id="datefrom"></INPUT>
													                	<INPUT id="datepicker1" size="5" readonly="true"  type="text" style="width:127px; color: black"></INPUT>
													                	
													                </TD>
													              </TR>
													              
													              <TR>
													                 <TD class="width sectiontableentry2" vAlign="top">
													                	<LABEL for="voteid25">before date: </LABEL>
													                </TD>
													              </TR>
													              <TR>
													                 <TD class="width sectiontableentry2" vAlign="top">
													                 	<INPUT SIZE="0"  AUTOCOMPLETE="off" TYPE="HIDDEN" id="dateto" NAME="dateto"></INPUT>
													                	<INPUT id="datepicker2" size="5"  readonly="true" type="text" style="width:127px; color: black"></INPUT>
													                </TD>
													              </TR>
													              										           												                                                         												           																		            																		            																            															              																											      
													      </TBODY>
													      </TABLE>
													      </TD>
													      </TR>
													       	 <TR>
													          	<TD>
													            <DIV style="padding-left:20px; padding-top: 10px">
																	<INPUT class="button"  type="submit" size="20" value="Search"  tabindex="30002"   />
													            </DIV>
													            </TD>
													            </TR>
													            <TR>
													          	<TD>
													            <DIV style="padding-left:20px; padding-top: 10px">
																	
													            </DIV>
													            </TD>
													            </TR>
													         </TBODY>
														</TABLE>
					</FORM>        
 */


public class AccountDetalHistoryForm {
	
	String searchquery = "2" ;
	String datefrom = "" ;
	String datepicker1 = "" ;
	String dateto = "" ;
	String datepicker2 = "" ;
	String amountId = "0" ;
	
	public String getSearchquery() {
		return searchquery;
	}
	public void setSearchquery(String searchquery) {
		this.searchquery = searchquery;
	}
	public String getDatefrom() {
		return datefrom;
	}
	public void setDatefrom(String datefrom) {
		this.datefrom = datefrom;
	}
	public String getDateto() {
		return dateto;
	}
	public void setDateto(String dateto) {
		this.dateto = dateto;
	}
	public String getAmountId() {
		return amountId;
	}
	public void setAmountId(String amountId) {
		this.amountId = amountId;
	}
	
	@Override
	public String toString() {
		return "AccountDetalHistoryForm [searchquery=" + searchquery + ", datefrom=" + datefrom + ", datepicker1="
				+ datepicker1 + ", dateto=" + dateto + ", datepicker2=" + datepicker2 + ", amountId=" + amountId + "]";
	}
	
	
	

}



