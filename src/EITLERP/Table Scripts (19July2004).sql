# Connection: eitl
# Host: 172.16.49.221
# Saved: 2004-07-19 09:45:10
# 
# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_BANK_MASTER'
# 
CREATE TABLE `D_COM_BANK_MASTER` (
  `COMPANY_ID` varchar(10) NOT NULL default '',
  `BANK_ID` bigint(3) NOT NULL default '0',
  `BANK_NAME` varchar(125) NOT NULL default '',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`BANK_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_BILLING'
# 
CREATE TABLE `D_COM_BILLING` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `BILL_ID` bigint(20) NOT NULL default '0',
  `ADD1` varchar(40) default '',
  `ADD2` varchar(40) default '',
  `ADD3` varchar(40) default '',
  `CITY` varchar(30) default '',
  `STATE` varchar(30) default '',
  `PINCODE` varchar(15) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`BILL_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_COLUMNS'
# 
CREATE TABLE `D_COM_COLUMNS` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `SR_NO` bigint(20) NOT NULL default '0',
  `MODULE_ID` bigint(20) NOT NULL default '0',
  `HEADER_LINE` char(1) NOT NULL default '',
  `COL_ORDER` bigint(20) NOT NULL default '0',
  `TAX_ID` bigint(20) NOT NULL default '0',
  `CAPTION` varchar(100) default '',
  `VARIABLE_NAME` varchar(50) default '',
  PRIMARY KEY  (`COMPANY_ID`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_COMPANY_MASTER'
# 
CREATE TABLE `D_COM_COMPANY_MASTER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `COMPANY_NAME` varchar(30) default '',
  `ADD1` varchar(30) default '',
  `ADD2` varchar(30) default '',
  `ADD3` varchar(30) default '',
  `CITY` varchar(20) default '',
  `STATE` varchar(20) default '',
  `PINCODE` varchar(6) default '',
  `PHONE` varchar(50) default '',
  `FAX` varchar(50) default '',
  `E_MAIL` varchar(50) default '',
  `URL` varchar(100) default '',
  `ECC_NO` varchar(25) default '',
  `ECC_DATE` date default '0000-00-00',
  `RANGE` varchar(10) default '',
  `COMMISIONRATE` varchar(20) default '',
  `DIVISION` varchar(20) default '',
  `CST_NO` varchar(25) default '',
  `CST_DATE` date default '0000-00-00',
  `GST_NO` varchar(100) default '',
  `GST_DATE` date default '0000-00-00',
  `LICENSE_NO` varchar(30) default '',
  `REGISTERATION_NO` varchar(30) default '',
  `CREATED_BY` int(11) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` int(11) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_CURRENCY_MASTER'
# 
CREATE TABLE `D_COM_CURRENCY_MASTER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `CURRENCY_ID` bigint(20) NOT NULL default '0',
  `CURRENCY_DESC` varchar(20) NOT NULL default '',
  `CURRENCY_DATE` date default '0000-00-00',
  `CURRENCY_RATE` double default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`CURRENCY_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_DEPT_MASTER'
# 
CREATE TABLE `D_COM_DEPT_MASTER` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `DEPT_ID` bigint(2) NOT NULL default '0',
  `DEPT_DESC` varchar(50) NOT NULL default '',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`DEPT_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_DOC_DATA'
# 
CREATE TABLE `D_COM_DOC_DATA` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `MODULE_ID` bigint(20) NOT NULL default '0',
  `DOC_NO` varchar(10) NOT NULL default '',
  `DOC_DATE` date NOT NULL default '0000-00-00',
  `USER_ID` bigint(20) NOT NULL default '0',
  `STATUS` char(1) NOT NULL default '',
  `TYPE` char(1) NOT NULL default '',
  `REMARKS` varchar(255) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `FROM_USER_ID` bigint(20) NOT NULL default '0',
  `FROM_REMARKS` varchar(255) NOT NULL default '',
  `RECEIVED_DATE` date default '0000-00-00',
  `ACTION_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`MODULE_ID`,`DOC_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_FIN_YEAR'
# 
CREATE TABLE `D_COM_FIN_YEAR` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `SR_NO` int(11) NOT NULL default '0',
  `YEAR_FROM` int(11) NOT NULL default '0',
  `YEAR_TO` int(11) NOT NULL default '0',
  `OPEN_STATUS` char(1) NOT NULL default '',
  `DATABASE_URL` varchar(200) default '',
  PRIMARY KEY  (`COMPANY_ID`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_FIN_YEAR_HISTORY'
# 
CREATE TABLE `D_COM_FIN_YEAR_HISTORY` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `FIN_SR_NO` int(11) NOT NULL default '0',
  `SR_NO` int(11) NOT NULL default '0',
  `ACTION` char(1) NOT NULL default '',
  `DONE_BY` bigint(20) NOT NULL default '0',
  `DONE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`FIN_SR_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_FIRSTFREE'
# 
CREATE TABLE `D_COM_FIRSTFREE` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `FIRSTFREE_NO` bigint(3) NOT NULL default '0',
  `PREFIX_CHARS` char(2) NOT NULL default '',
  `LAST_USED_NO` varchar(10) NOT NULL default '',
  `BLOCKED` char(1) NOT NULL default '',
  `BLOCKED_DATE` date NOT NULL default '0000-00-00',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` bigint(3) default '0',
  `MODIFIED_BY` varchar(100) default '',
  `MODIFIED_DATE` date default '0000-00-00',
  `MODULE_ID` bigint(2) NOT NULL default '0',
  `LAST_NO` bigint(20) default '0',
  `PADDING_BY` char(1) NOT NULL default '',
  `NO_LENGTH` bigint(2) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`FIRSTFREE_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_HIERARCHY'
# 
CREATE TABLE `D_COM_HIERARCHY` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `HIERARCHY_ID` bigint(3) NOT NULL default '0',
  `MODULE_ID` bigint(2) NOT NULL default '0',
  `CREATED_BY` bigint(2) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(2) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `IS_DEFAULT` tinyint(1) default '0',
  `HIERARCHY_NAME` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`COMPANY_ID`,`HIERARCHY_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_HIERARCHY_RIGHTS'
# 
CREATE TABLE `D_COM_HIERARCHY_RIGHTS` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `HIERARCHY_ID` bigint(3) NOT NULL default '0',
  `USER_ID` bigint(3) NOT NULL default '0',
  `APPROVER` tinyint(1) NOT NULL default '0',
  `FINAL_APPROVER` tinyint(1) NOT NULL default '0',
  `CREATOR` tinyint(1) NOT NULL default '0',
  `APPROVAL_SEQUENCE` bigint(2) NOT NULL default '0',
  `SKIP_SEQUENCE` tinyint(1) NOT NULL default '0',
  `GRANT_OTHER` tinyint(1) NOT NULL default '0',
  `FROM_DATE` date NOT NULL default '0000-00-00',
  `TO_DATE` date NOT NULL default '0000-00-00',
  `GRANT_USER_ID` bigint(3) NOT NULL default '0',
  `RESTORE` tinyint(1) NOT NULL default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_dATE` date default '0000-00-00',
  `SR_NO` bigint(3) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`HIERARCHY_ID`,`USER_ID`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_MENU_FUNCTION'
# 
CREATE TABLE `D_COM_MENU_FUNCTION` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `MENU_ID` bigint(20) NOT NULL default '0',
  `SR_NO` bigint(20) NOT NULL default '0',
  `FUNCTION_ID` bigint(20) NOT NULL default '0',
  `FUNCTION_NAME` varchar(100) default '',
  PRIMARY KEY  (`COMPANY_ID`,`MENU_ID`,`FUNCTION_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_MENU_MASTER'
# 
CREATE TABLE `D_COM_MENU_MASTER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `MENU_ID` bigint(20) NOT NULL default '0',
  `MENU_TYPE` char(2) NOT NULL default '',
  `PARENT_ID` bigint(20) default '0',
  `MENU_CAPTION` varchar(50) default '',
  `MODULE_ID` bigint(20) default '0',
  `CLASS_NAME` varchar(200) default '',
  PRIMARY KEY  (`COMPANY_ID`,`MENU_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_MODULES'
# 
CREATE TABLE `D_COM_MODULES` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `MODULE_ID` bigint(2) NOT NULL default '0',
  `MODULE_DESC` varchar(30) NOT NULL default '',
  `MAINTAIN_STOCK` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`MODULE_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_PARAMETER_MAST'
# 
CREATE TABLE `D_COM_PARAMETER_MAST` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `PARA_ID` varchar(15) NOT NULL default '',
  `PARA_CODE` bigint(2) NOT NULL default '0',
  `DESC` varchar(100) NOT NULL default '',
  `REMARKS` varchar(200) default '',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `VALUE` float(10,2) default '0.00',
  PRIMARY KEY  (`COMPANY_ID`,`PARA_ID`,`PARA_CODE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_REASON_MASTER'
# 
CREATE TABLE `D_COM_REASON_MASTER` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `REASON_ID` bigint(3) NOT NULL default '0',
  `REASON_DESC` text NOT NULL,
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`REASON_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_SHIPING'
# 
CREATE TABLE `D_COM_SHIPING` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `SHIP_ID` bigint(20) NOT NULL default '0',
  `ADD1` varchar(30) default '',
  `ADD2` varchar(30) default '',
  `ADD3` varchar(30) default '',
  `CITY` varchar(30) default '',
  `STATE` varchar(30) default '',
  `PINCODE` varchar(6) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`SHIP_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_SUPP_ITEM'
# 
CREATE TABLE `D_COM_SUPP_ITEM` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `SUPP_ID` bigint(20) NOT NULL default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `PART_NO` varchar(30) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`SUPP_ID`,`ITEM_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_SUPP_ITEMCATEGORY'
# 
CREATE TABLE `D_COM_SUPP_ITEMCATEGORY` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `SUPP_ID` bigint(20) NOT NULL default '0',
  `CATEGORY_ID` bigint(20) NOT NULL default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`CATEGORY_ID`,`SUPP_ID`,`COMPANY_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_SUPP_MASTER'
# 
CREATE TABLE `D_COM_SUPP_MASTER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `SUPP_ID` bigint(20) NOT NULL default '0',
  `SUPPLIER_CODE` varchar(10) NOT NULL default '',
  `DUMMY_SUPPLIER_CODE` varchar(10) default '',
  `SUPP_NAME` varchar(50) NOT NULL default '',
  `ATTN` varchar(20) default '',
  `ADD1` varchar(50) NOT NULL default '',
  `ADD2` varchar(50) NOT NULL default '',
  `ADD3` varchar(50) default '',
  `CITY` varchar(30) NOT NULL default '',
  `STATE` varchar(30) default '',
  `COUNTRY` varchar(30) default NULL,
  `PINCODE` varchar(20) default '',
  `PHONE_O` varchar(20) default '',
  `PHONE_RES` varchar(20) default '',
  `FAX_NO` varchar(20) default NULL,
  `MOBILE_NO` varchar(20) default '',
  `EMAIL_ADD` varchar(30) default '',
  `URL` varchar(20) default '',
  `WEB_SITE` varchar(25) default '',
  `REGISTERATION` varchar(100) default NULL,
  `SERVICETAX_NO` varchar(25) default '',
  `SERVICETAX_DATE` date default '0000-00-00',
  `CST_NO` varchar(25) default '',
  `CST_DATE` date default '0000-00-00',
  `GST_NO` varchar(25) default '',
  `GST_DATE` date default '0000-00-00',
  `ECC_NO` varchar(25) default '',
  `ECC_DATE` date default '0000-00-00',
  `FORM` bigint(20) default '0',
  `ST35_REGISTERED` tinyint(1) default '0',
  `CONTACT_PERSON_1` varchar(25) default '',
  `CONTACT_PERSON_2` varchar(25) default '',
  `ONETIME_SUPPLIER` tinyint(1) default '0',
  `FROM_DATE_REG` date default '0000-00-00',
  `TO_DATE_REG` date default '0000-00-00',
  `BLOCKED` char(1) default '',
  `SLOW_MOVING` tinyint(1) default '0',
  `PAYMENT_DAYS` bigint(20) default '30',
  `LAST_TRANS_DATE` date default '0000-00-00',
  `LAST_PO_NO` varchar(10) default NULL,
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `BANK_ID` bigint(3) default '0',
  `BANK_NAME` varchar(125) default '',
  `HIERARCHY_ID` bigint(20) default NULL,
  `APPROVED` tinyint(4) default NULL,
  `APPROVED_DATE` date default NULL,
  `REJECTED` tinyint(4) default NULL,
  `REJECTED_DATE` date default NULL,
  `SSIREG` tinyint(1) default '0',
  `SSIREG_NO` varchar(30) default '',
  `SSIREG_DATE` date default '0000-00-00',
  `ESIREG_NO` varchar(30) default '',
  `ESIREG_DATE` date default '0000-00-00',
  `PAYMENT_CODE` int(11) default '0',
  `PAYMENT_DESC` varchar(100) default '',
  `DESPATCH_CODE` int(11) default '0',
  `DESPATCH_DESC` varchar(100) default '',
  `INSURANCE_CODE` int(11) default '0',
  `INSURANCE_DESC` varchar(100) default '',
  `LICENSE_CODE` int(11) default '0',
  `LICENSE_DESC` varchar(100) default '',
  `PACKING_CODE` int(11) default '0',
  `PACKING_DESC` varchar(100) default '',
  `FORWARDING_CODE` int(11) default '0',
  `FORWARDING_DESC` varchar(100) default '',
  `EXCISE_CODE` int(11) default '0',
  `EXCISE` varchar(100) default '',
  `OCTROI_CODE` int(11) default '0',
  `OCTROI` varchar(100) default '',
  `FREIGHT_CODE` int(11) default '0',
  `FREIGHT` varchar(100) default '',
  `TCC_CODE` int(11) default '0',
  `TCC` varchar(100) default '',
  `SERVICETAX_CODE` int(11) default '0',
  `SERVICETAX_DESC` varchar(100) default '',
  `ST_CODE` int(11) default '0',
  `ST_DESC` varchar(100) default '',
  `ESI_CODE` int(11) default '0',
  `ESI_DESC` varchar(100) default '',
  `FOR_CODE` int(11) default '0',
  `FOR_DESC` varchar(100) default '',
  `FOB_CODE` int(11) default '0',
  `FOB_DESC` varchar(100) default '',
  PRIMARY KEY  (`COMPANY_ID`,`SUPP_ID`,`SUPPLIER_CODE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_SYSTEM_COLUMNS'
# 
CREATE TABLE `D_COM_SYSTEM_COLUMNS` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `MODULE_ID` bigint(20) NOT NULL default '0',
  `SR_NO` bigint(20) NOT NULL default '0',
  `ORDER` bigint(20) NOT NULL default '0',
  `CAPTION` varchar(100) default '',
  `READONLY` tinyint(1) default '0',
  `HIDDEN` tinyint(1) default '0',
  `VARIABLE` varchar(200) default '',
  `NUMERIC` tinyint(1) NOT NULL default '0',
  `SHOW_LAST` tinyint(1) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`MODULE_ID`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_TAX_DETAIL'
# 
CREATE TABLE `D_COM_TAX_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `TAX_ID` bigint(20) NOT NULL default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `FORMULA` varchar(200) NOT NULL default '',
  PRIMARY KEY  (`COMPANY_ID`,`TAX_ID`,`ITEM_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_TAX_HEADER'
# 
CREATE TABLE `D_COM_TAX_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `TAX_ID` bigint(20) NOT NULL default '0',
  `MODULE_ID` bigint(20) NOT NULL default '0',
  `CAPTION` varchar(100) NOT NULL default '',
  `VISIBLE_ON_FORM` tinyint(1) NOT NULL default '0',
  `OPERATION` char(1) NOT NULL default '',
  `INPUT` bigint(20) NOT NULL default '0',
  `FORMULA` varchar(200) NOT NULL default '',
  `NO_CALCULATION` tinyint(1) default '0',
  `USE_PERCENT` tinyint(1) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`TAX_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_USER_MASTER'
# 
CREATE TABLE `D_COM_USER_MASTER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `USER_ID` bigint(20) NOT NULL default '0',
  `USER_NAME` varchar(20) default '',
  `LOGIN_ID` varchar(10) NOT NULL default '',
  `PASSWORD` varchar(10) default '',
  `DEPT_ID` bigint(20) default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` datetime default '0000-00-00 00:00:00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` datetime default '0000-00-00 00:00:00',
  PRIMARY KEY  (`COMPANY_ID`,`USER_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_COM_USER_RIGHTS'
# 
CREATE TABLE `D_COM_USER_RIGHTS` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `USER_ID` bigint(20) NOT NULL default '0',
  `MENU_ID` bigint(20) NOT NULL default '0',
  `FUNCTION_ID` bigint(20) NOT NULL default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` datetime default '0000-00-00 00:00:00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` datetime default '0000-00-00 00:00:00',
  PRIMARY KEY  (`COMPANY_ID`,`USER_ID`,`MENU_ID`,`FUNCTION_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_CATEGORY_MASTER'
# 
CREATE TABLE `D_INV_CATEGORY_MASTER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `CATEGORY_ID` bigint(20) NOT NULL default '0',
  `CATEGORY_DESC` varchar(30) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`CATEGORY_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_DECLARATION_CONSUMED_DETAIL'
# 
CREATE TABLE `D_INV_DECLARATION_CONSUMED_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `CONSUMEDDOC_ID` bigint(20) NOT NULL default '0',
  `DECLARATION_ID` varchar(20) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `ITEM_CODE` varchar(10) NOT NULL default '',
  `UNIT` int(11) default '0',
  `CONSUMED_QTY` double(15,3) default '0.000',
  `REMARKS` varchar(50) default '',
  `CANCELLED` tinyint(1) default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00'
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_DECLARATION_CONSUMED_HEADER'
# 
CREATE TABLE `D_INV_DECLARATION_CONSUMED_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `CONSUMEDDOC_ID` bigint(20) NOT NULL default '0',
  `DECLARATION_ID` varchar(20) NOT NULL default '',
  `CONSUMEDDOC_DATE` date NOT NULL default '0000-00-00',
  `REMARKS` varchar(200) default '',
  `CANCELLED` tinyint(1) default '0',
  `HIERARCHY_ID` bigint(20) NOT NULL default '0',
  `CREATED_BY` bigint(20) default '0',
  `CERATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`CONSUMEDDOC_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_DECLARATION_DETAIL'
# 
CREATE TABLE `D_INV_DECLARATION_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `DECLARATION_ID` varchar(20) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `ITEM_CODE` varchar(10) default NULL,
  `DECLARATION_DESC` varchar(50) default NULL,
  `UNIT` int(11) default NULL,
  `RECD_QTY` double(15,3) default NULL,
  `BAL_QTY` double(15,3) default NULL,
  `RETURNED` tinyint(1) default NULL,
  `EXP_RETURN_DATE` date default NULL,
  `RETURNED_DATE` date default NULL,
  `REMARKS` varchar(50) default NULL,
  `CANCELED` tinyint(1) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `CREATED_DATE` date default NULL,
  `MODIFIED_BY` bigint(20) default NULL,
  `MODIFIED_DATE` date default NULL,
  PRIMARY KEY  (`COMPANY_ID`,`DECLARATION_ID`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_DECLARATION_DETAIL_DETAIL'
# 
CREATE TABLE `D_INV_DECLARATION_DETAIL_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `DECLARATION_ID` varchar(20) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `SRNO` bigint(20) NOT NULL default '0',
  `ITEM_ID` varchar(10) default NULL,
  `LOT_NO` varchar(10) default NULL,
  `LOT_QTY` double(15,3) default NULL,
  `TOTAL_ISSUED_QTY` double(15,3) default NULL,
  `BAL_QTY` double(15,3) default NULL,
  `CANCELED` tinyint(1) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `CREATED_DATE` date default NULL,
  `MODIFIED_BY` bigint(20) default NULL,
  `MODIFIED_DATE` date default NULL,
  PRIMARY KEY  (`COMPANY_ID`,`DECLARATION_ID`,`SR_NO`,`SRNO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_DECLARATION_HEADER'
# 
CREATE TABLE `D_INV_DECLARATION_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `DECLARATION_ID` varchar(20) NOT NULL default '',
  `DECLARATION_DATE` date default NULL,
  `CONTRACTOR_NAME` varchar(50) default NULL,
  `ADD1` varchar(50) default NULL,
  `ADD2` varchar(50) default NULL,
  `ADD3` varchar(50) default NULL,
  `CITY` varchar(30) default NULL,
  `PINCODE` varchar(20) default NULL,
  `PO_NO` varchar(10) default NULL,
  `PO_DATE` date default NULL,
  `FOR_DEPT_ID` bigint(2) default NULL,
  `RECEIVED_BY` varchar(30) default NULL,
  `PURPOSE` varchar(100) default NULL,
  `APPROVED` tinyint(1) default NULL,
  `APPROVED_DATE` date default NULL,
  `REMARKS` varchar(200) default NULL,
  `CANCELLED` tinyint(1) default NULL,
  `HIERARCHY_ID` bigint(20) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `CREATED_DATE` date default NULL,
  `MODIFIED_BY` bigint(20) default NULL,
  `MODIFIED_DATE` date default NULL,
  PRIMARY KEY  (`COMPANY_ID`,`DECLARATION_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_GATEPASS_REQ_DETAIL'
# 
CREATE TABLE `D_INV_GATEPASS_REQ_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `GATEPASS_REQ_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `ITEM_CODE` varchar(10) default '',
  `QTY` double default '0',
  `UNIT` int(11) default '0',
  `PACKING` varchar(10) default '',
  `REMARKS` varchar(100) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`GATEPASS_REQ_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_GATEPASS_REQ_HEADER'
# 
CREATE TABLE `D_INV_GATEPASS_REQ_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `GATEPASS_REQ_NO` varchar(10) NOT NULL default '',
  `GATEPASS_REQ_DATE` date default '0000-00-00',
  `SOURCE_DEPT_ID` int(11) default '0',
  `DEST_DEPT_ID` int(11) default '0',
  `PURPOSE` varchar(100) default '',
  `REQUIRED_DATE` date default '0000-00-00',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `CANCELLED` tinyint(1) default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `GATEPASS_TYPE` char(1) NOT NULL default '',
  `HIERARCHY_ID` bigint(20) default '0',
  `REMARKS` varchar(200) default '',
  `USER_ID` int(11) default '0',
  `PARTY_NAME` varchar(100) default '',
  `EXP_RETURN_DATE` date default '0000-00-00',
  `DESPATCH_MODE` varchar(30) default '',
  `FREIGHT_PAID_BY` varchar(20) default '',
  PRIMARY KEY  (`COMPANY_ID`,`GATEPASS_REQ_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_GATEPASS_REQ_LOT'
# 
CREATE TABLE `D_INV_GATEPASS_REQ_LOT` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `GATEPASS_REQ_NO` varchar(10) NOT NULL default '',
  `GATEPASS_SR_NO` int(11) NOT NULL default '0',
  `ITEM_LOT_NO` varchar(10) NOT NULL default '',
  `LOT_QTY` double default '0',
  `SR_NO` int(11) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`GATEPASS_REQ_NO`,`GATEPASS_SR_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_GRN_DETAIL'
# 
CREATE TABLE `D_INV_GRN_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `GRN_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `WAREHOUSE_ID` varchar(10) NOT NULL default '',
  `LOCATION_ID` varchar(10) NOT NULL default '',
  `MIR_NO` varchar(10) default '',
  `MIR_SR_NO` int(11) default '0',
  `PO_NO` varchar(10) default '',
  `PO_SR_NO` int(11) default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `BOE_NO` varchar(10) default '',
  `BOE_SR_NO` varchar(10) default '',
  `BOE_DATE` date default '0000-00-00',
  `MIR_QTY` double default '0',
  `QTY` double NOT NULL default '0',
  `REJECTED_QTY` double default '0',
  `REJECTED_REASON` varchar(100) default '',
  `UNIT` bigint(20) default '0',
  `RATE` double NOT NULL default '0',
  `TOTAL_AMOUNT` double default '0',
  `SHADE` varchar(10) default '',
  `W_MIE` varchar(10) default '',
  `NO_CASE` varchar(10) default '',
  `IMPORT_CONCESS` tinyint(1) default '0',
  `REMARKS` varchar(200) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(100) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(100) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(100) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(100) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(100) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(100) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(100) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(100) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(100) default '',
  `MATERIAL_CODE` varchar(10) default '',
  `MATERIAL_DESC` varchar(50) default '',
  `QUALITY_NO` varchar(10) default '',
  `PAGE_NO` varchar(10) default '',
  `EXCESS` double default '0',
  `SHORTAGE` double default '0',
  `L_F_NO` varchar(15) default '',
  `CHALAN_QTY` double default '0',
  `NET_AMOUNT` double default '0',
  `GRN_TYPE` bigint(20) NOT NULL default '0',
  `PO_TYPE` int(11) default '0',
  `MIR_TYPE` int(11) default '0',
  `LANDED_RATE` double default '0',
  `TOLERANCE_LIMIT` double default '0',
  PRIMARY KEY  (`COMPANY_ID`,`GRN_NO`,`SR_NO`,`GRN_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_GRN_HEADER'
# 
CREATE TABLE `D_INV_GRN_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `GRN_NO` varchar(10) NOT NULL default '',
  `GRN_DATE` date NOT NULL default '0000-00-00',
  `SUPP_ID` varchar(10) NOT NULL default '',
  `CHALAN_NO` varchar(10) default '',
  `CHALAN_DATE` date default '0000-00-00',
  `LR_NO` varchar(10) default '',
  `LR_DATE` date default '0000-00-00',
  `INVOICE_NO` varchar(10) default '',
  `INVOICE_DATE` date default '0000-00-00',
  `TRANSPORTER` bigint(20) default '0',
  `GATEPASS_NO` varchar(10) default '',
  `IMPORT_CONCESS` tinyint(1) NOT NULL default '0',
  `ACCESSABLE_VALUE` double NOT NULL default '0',
  `CURRENCY_ID` bigint(20) default '0',
  `CURRENCY_RATE` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `GROSS_AMOUNT` double default '0',
  `GRN_PENDING` tinyint(1) default '0',
  `GRN_PENDING_REASON` bigint(20) default '0',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` bigint(20) default '0',
  `CENVATED_ITEMS` tinyint(1) default '0',
  `CANCELLED` tinyint(1) default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(200) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(200) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(200) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(200) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(200) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(200) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(100) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(200) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(200) default '',
  `GRN_TYPE` bigint(20) NOT NULL default '0',
  `OPEN_STATUS` char(1) default '',
  `FOR_STORE` bigint(20) default '0',
  `REMARKS` varchar(200) default '',
  `APPROVED_ON` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`GRN_NO`,`GRN_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_GRN_LOT'
# 
CREATE TABLE `D_INV_GRN_LOT` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `GRN_NO` varchar(10) NOT NULL default '',
  `GRN_SR_NO` int(11) NOT NULL default '0',
  `SR_NO` int(11) NOT NULL default '0',
  `ITEM_LOT_NO` varchar(10) NOT NULL default '',
  `LOT_QTY` double NOT NULL default '0',
  `GRN_TYPE` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`GRN_NO`,`GRN_SR_NO`,`SR_NO`,`GRN_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_INDENT_DETAIL'
# 
CREATE TABLE `D_INV_INDENT_DETAIL` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `INDENT_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `MR_NO` varchar(10) NOT NULL default '',
  `MR_SR_NO` bigint(3) default '0',
  `ITEM_CODE` varchar(10) NOT NULL default '',
  `UNIT` bigint(2) NOT NULL default '0',
  `QTY` float(10,3) NOT NULL default '0.000',
  `BAL_QTY` float(10,3) NOT NULL default '0.000',
  `PO_QTY` float(10,3) NOT NULL default '0.000',
  `TOTAL_REQ_QTY` float(10,3) NOT NULL default '0.000',
  `ALLOCATED_QTY` float(10,3) NOT NULL default '0.000',
  `STOCK_QTY` float(10,3) NOT NULL default '0.000',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `REMARKS` varchar(100) NOT NULL default '',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `NET_AMT` float(10,2) NOT NULL default '0.00',
  `RATE` float(9,2) NOT NULL default '0.00',
  `OTHER_COMPANY_STOCK` double default '0',
  `LAST_SUPP_ID` varchar(10) default '',
  PRIMARY KEY  (`COMPANY_ID`,`INDENT_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_INDENT_HEADER'
# 
CREATE TABLE `D_INV_INDENT_HEADER` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `INDENT_NO` varchar(10) NOT NULL default '',
  `INDENT_DATE` date NOT NULL default '0000-00-00',
  `INDENT_TYPE` char(1) NOT NULL default '',
  `FOR_UNIT` bigint(2) NOT NULL default '0',
  `FOR_DEPT_ID` bigint(2) NOT NULL default '0',
  `PURPOSE` varchar(30) NOT NULL default '',
  `REQUIRED_DATE` date NOT NULL default '0000-00-00',
  `BUYER` bigint(3) NOT NULL default '0',
  `APPROVED` tinyint(1) NOT NULL default '0',
  `APPROVED_DATE` date NOT NULL default '0000-00-00',
  `REJECTED_DATE` date NOT NULL default '0000-00-00',
  `REJECTED_REMARKS` varchar(200) NOT NULL default '',
  `REMARKS` varchar(100) NOT NULL default '',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `STATUS` char(1) NOT NULL default '',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` varchar(100) default '',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` bigint(5) NOT NULL default '0',
  `REJECTED` tinyint(1) NOT NULL default '0',
  `GROSS_AMOUNT` float(10,2) NOT NULL default '0.00',
  PRIMARY KEY  (`COMPANY_ID`,`INDENT_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_ISSUE_DETAIL'
# 
CREATE TABLE `D_INV_ISSUE_DETAIL` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `ISSUE_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `WAREHOUSE_ID` varchar(5) NOT NULL default '',
  `LOCATION_ID` varchar(5) NOT NULL default '',
  `ITEM_CODE` varchar(10) NOT NULL default '',
  `ITEM_LOT_NO` varchar(5) NOT NULL default '',
  `BOE_NO` varchar(10) NOT NULL default '',
  `BOE_SR_NO` bigint(3) NOT NULL default '0',
  `QTY` float(10,3) NOT NULL default '0.000',
  `UNIT` bigint(3) NOT NULL default '0',
  `SHADE` varchar(10) NOT NULL default '',
  `W_MIE` varchar(10) NOT NULL default '',
  `NO_CASE` bigint(4) NOT NULL default '0',
  `ISSUE_DESC` varchar(100) NOT NULL default '',
  `PER` float(5,2) NOT NULL default '0.00',
  `QTY_REQD` float(10,3) NOT NULL default '0.000',
  `MFG_PROG_NO` varchar(15) NOT NULL default '',
  `MATERIAL_CODE_NO` varchar(15) NOT NULL default '',
  `ISSUE_QTY` float(10,3) NOT NULL default '0.000',
  `BALE_NO` varchar(15) NOT NULL default '',
  `REQ_NO` varchar(10) NOT NULL default '',
  `REQ_SRNO` int(3) NOT NULL default '0',
  `CENTER_CODE_NO` varchar(10) NOT NULL default '',
  `REMARKS` varchar(100) NOT NULL default '',
  `LF_NO` varchar(15) NOT NULL default '',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `CREATED_BY` varchar(100) default '',
  `CREATED_DATE` varchar(100) default '',
  `MODIFIED_BY` varchar(100) default '',
  `MODIFIED_DATE` varchar(100) default '',
  `DEPT_ID` int(11) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`ISSUE_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_ISSUE_DETAIL_DETAIL'
# 
CREATE TABLE `D_INV_ISSUE_DETAIL_DETAIL` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `ISSUE_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `SRNO` bigint(3) NOT NULL default '0',
  `LOT_NO` varchar(10) NOT NULL default '',
  `LOT_QTY` float(10,3) NOT NULL default '0.000',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`ISSUE_NO`,`SR_NO`,`SRNO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_ISSUE_HEADER'
# 
CREATE TABLE `D_INV_ISSUE_HEADER` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `ISSUE_NO` varchar(10) NOT NULL default '',
  `ISSUE_DATE` date NOT NULL default '0000-00-00',
  `FOR_STORE` char(1) NOT NULL default '',
  `FOR_DEPT_ID` bigint(3) NOT NULL default '0',
  `ISSUE_TYPE` char(3) NOT NULL default '',
  `BLEND_CODE` bigint(3) NOT NULL default '0',
  `PURPOSE` varchar(50) NOT NULL default '',
  `APPROVED` tinyint(1) NOT NULL default '0',
  `APPROVED_DATE` date NOT NULL default '0000-00-00',
  `REMARKS` varchar(200) NOT NULL default '',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `ISSUE_WITH_LOT` bigint(1) NOT NULL default '0',
  `REJECTED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`ISSUE_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_ITEM_HIERARCHY_DETAIL'
# 
CREATE TABLE `D_INV_ITEM_HIERARCHY_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `APPROVAL_NO` bigint(20) NOT NULL default '0',
  `SR_NO` bigint(20) NOT NULL default '0',
  `USER_ID` bigint(20) NOT NULL default '0',
  `CAN_SKIP` tinyint(1) NOT NULL default '0',
  `CAN_CHANGE` tinyint(1) NOT NULL default '0',
  `CAN_FINAL_APPROVE` tinyint(1) NOT NULL default '0',
  `CREATED_BY` bigint(20) NOT NULL default '0',
  `CREATED_DATE` date NOT NULL default '0000-00-00',
  `MODIFIED_BY` bigint(20) NOT NULL default '0',
  `MODIFIED_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`APPROVAL_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_ITEM_HIERARCHY_MASTER'
# 
CREATE TABLE `D_INV_ITEM_HIERARCHY_MASTER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `APPROVAL_NO` bigint(20) NOT NULL default '0',
  `DESCRIPTION` varchar(255) NOT NULL default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `IS_DEFAULT` tinyint(1) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`APPROVAL_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_ITEM_LOT_MASTER'
# 
CREATE TABLE `D_INV_ITEM_LOT_MASTER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `LOT_NO` varchar(10) NOT NULL default '',
  `BOE_NO` varchar(20) NOT NULL default '',
  `BOE_SR_NO` varchar(5) default '',
  `OPENING_QTY` double default '0',
  `OPENING_RATE` double default '0',
  `TOTAL_RECEIPT_QTY` double default '0',
  `TOTAL_ISSUED_QTY` double default '0',
  `LAST_RECEIPT_DATE` date default '0000-00-00',
  `LAST_ISSUED_DATE` date default '0000-00-00',
  `ZERO_RECEIPT_QTY` double default '0',
  `ZERO_ISSUED_QTY` double default '0',
  `REJECTED_QTY` double default '0',
  `ON_HAND_QTY` double default '0',
  `AVAILABLE_QTY` double default '0',
  `ALLOCATED_QTY` double default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `WAREHOUSE_ID` varchar(10) default '',
  `LOCATION_ID` varchar(20) default '',
  `BOE_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`ITEM_ID`,`LOT_NO`,`BOE_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_ITEM_MASTER'
# 
CREATE TABLE `D_INV_ITEM_MASTER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `ITEM_SYS_ID` bigint(20) default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `ITEM_DESCRIPTION` varchar(100) default '',
  `GROUP_CODE` bigint(20) default '0',
  `SEARCH_KEY` varchar(5) default '',
  `WAREHOUSE_ID` varchar(5) default '',
  `CATEGORY_ID` bigint(20) default '0',
  `LOCATION_ID` varchar(10) default '0',
  `DESC` bigint(20) default '0',
  `MAKE` bigint(20) default '0',
  `SIZE` bigint(20) default '0',
  `ABC` char(1) default '',
  `XYZ` char(1) default '',
  `VEN` char(1) default '',
  `FSN` char(1) default '',
  `MF` char(1) default '',
  `MAINTAIN_STOCK` tinyint(1) NOT NULL default '0',
  `AVG_QTY` double default '0',
  `MIN_QTY` double default '0',
  `MAX_QTY` double default '0',
  `UNIT` bigint(20) default '0',
  `UNIT_RATE` double default '0',
  `DNP` double default '0',
  `AVG_RATE` double default '0',
  `QTR1_RATE` double default '0',
  `QTR2_RATE` double default '0',
  `QTR3_RATE` double default '0',
  `QTR4_RATE` double default '0',
  `REBATE` double default '0',
  `EXCISE_APPLICABLE` tinyint(1) default '0',
  `EXCISE` double default '0',
  `CHAPTER_NO` bigint(20) default '0',
  `TAXABLE` tinyint(1) default '0',
  `OPENING_QTY` double default '0',
  `OPENING_VALUE` double default '0',
  `TOTAL_RECEIPT_QTY` double default '0',
  `TOTAL_ISSUED_QTY` double default '0',
  `LAST_RECEIPT_DATE` date default '0000-00-00',
  `LAST_ISSUED_DATE` date default '0000-00-00',
  `ZERO_RECEIPT_QTY` double default '0',
  `ZERO_ISSUED_QTY` double default '0',
  `ZERO_VALUE_QTY` double default '0',
  `REJECTED_QTY` double default '0',
  `ON_HAND_QTY` double default '0',
  `AVAILABLE_QTY` double default '0',
  `ALLOCATED_QTY` double default '0',
  `LAST_TRANS_DATE` date default '0000-00-00',
  `LAST_GRN_NO` varchar(10) default '',
  `LAST_GRN_DATE` date default '0000-00-00',
  `LAST_PO_NO` varchar(10) default '',
  `LAST_PO_DATE` date default '0000-00-00',
  `CAPTIVE_CONSUMABLE` tinyint(1) default '0',
  `BLOCKED` tinyint(1) default '0',
  `REORDER_LEVEL` double default '0',
  `SUPPLIER_ID` varchar(10) default '',
  `ITEM_HIERARCHY_ID` bigint(20) default '0',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `AVG_CONSUMPTION` double default '0',
  `INSP_QTY` double default '0',
  `INDENT_QTY` double default '0',
  `PO_QTY` double default '0',
  `LAST_PO_QTY` double default '0',
  `SUPP_ID` varchar(10) default '',
  `SUPP_NAME` varchar(100) default '',
  `LAST_GRN_RATE` double default '0',
  PRIMARY KEY  (`COMPANY_ID`,`ITEM_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_LOCATION_MASTER'
# 
CREATE TABLE `D_INV_LOCATION_MASTER` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `WAREHOUSE_ID` varchar(5) NOT NULL default '',
  `LOCATION_ID` varchar(5) NOT NULL default '',
  `LOCATION_NAME` varchar(20) NOT NULL default '',
  `TOTAL_CAPACITY` float(10,3) NOT NULL default '0.000',
  `WARNING_CAPACITY` float(10,3) NOT NULL default '0.000',
  `MULTI_LEVEL_LOCATION` tinyint(1) NOT NULL default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`WAREHOUSE_ID`,`LOCATION_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_MIR_DETAIL'
# 
CREATE TABLE `D_INV_MIR_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `MIR_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `WAREHOUSE_ID` varchar(10) NOT NULL default '',
  `LOCATION_ID` varchar(10) NOT NULL default '',
  `PO_NO` varchar(10) default '',
  `PO_SR_NO` int(11) default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `BOE_NO` varchar(10) default '',
  `BOE_SR_NO` varchar(10) default '',
  `BOE_DATE` date default '0000-00-00',
  `QTY` double NOT NULL default '0',
  `UNIT` bigint(20) default '0',
  `RATE` double NOT NULL default '0',
  `TOTAL_AMOUNT` double default '0',
  `SHADE` varchar(10) default '',
  `W_MIE` varchar(10) default '',
  `NO_CASE` varchar(10) default '',
  `IMPORT_CONCESS` tinyint(1) default '0',
  `REMARKS` varchar(200) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(100) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(100) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(100) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(100) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(100) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(100) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(100) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(100) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(100) default '',
  `MATERIAL_CODE` varchar(10) default '',
  `MATERIAL_DESC` varchar(50) default '',
  `QUALITY_NO` varchar(10) default '',
  `PAGE_NO` varchar(10) default '',
  `EXCESS` double default '0',
  `SHORTAGE` double default '0',
  `L_F_NO` varchar(15) default '',
  `CHALAN_QTY` double default '0',
  `INDENT_NO` varchar(10) default '',
  `INDENT_SR_NO` bigint(20) default '0',
  `STM_NO` varchar(10) default '',
  `STM_SR_NO` bigint(20) default '0',
  `NET_AMOUNT` double default '0',
  `GRN_RECD_QTY` double default '0',
  `BAL_QTY` double default '0',
  `MIR_TYPE` bigint(20) NOT NULL default '0',
  `PO_TYPE` int(11) default '0',
  `STM_TYPE` int(11) default '0',
  `LANDED_RATE` double default '0',
  `TOLERANCE_LIMIT` double default '0',
  PRIMARY KEY  (`COMPANY_ID`,`MIR_NO`,`SR_NO`,`MIR_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_MIR_HEADER'
# 
CREATE TABLE `D_INV_MIR_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `MIR_NO` varchar(10) NOT NULL default '',
  `MIR_DATE` date NOT NULL default '0000-00-00',
  `SUPP_ID` varchar(10) NOT NULL default '',
  `CHALAN_NO` varchar(10) default '',
  `CHALAN_DATE` date default '0000-00-00',
  `LR_NO` varchar(10) default '',
  `LR_DATE` date default '0000-00-00',
  `INVOICE_NO` varchar(10) default '',
  `INVOICE_DATE` date default '0000-00-00',
  `TRANSPORTER` bigint(20) default '0',
  `GATEPASS_NO` varchar(10) default '',
  `IMPORT_CONCESS` tinyint(1) NOT NULL default '0',
  `ACCESSABLE_VALUE` double NOT NULL default '0',
  `CURRENCY_ID` bigint(20) default '0',
  `CURRENCY_RATE` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `GROSS_AMOUNT` double default '0',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` bigint(20) default '0',
  `CENVATED_ITEMS` tinyint(1) default '0',
  `CANCELLED` tinyint(1) default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(200) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(200) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(200) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(200) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(200) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(200) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(100) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(200) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(200) default '',
  `MIR_TYPE` bigint(20) NOT NULL default '0',
  `OPEN_STATUS` char(1) default '',
  `FOR_STORE` bigint(20) default '0',
  `REMARKS` varchar(200) default '',
  `INVOICE_AMOUNT` double default '0',
  PRIMARY KEY  (`COMPANY_ID`,`MIR_NO`,`MIR_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_MIR_LOT'
# 
CREATE TABLE `D_INV_MIR_LOT` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `MIR_NO` varchar(10) NOT NULL default '',
  `MIR_SR_NO` int(11) NOT NULL default '0',
  `SR_NO` int(11) NOT NULL default '0',
  `ITEM_LOT_NO` varchar(10) NOT NULL default '',
  `LOT_QTY` double NOT NULL default '0',
  `MIR_TYPE` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`MIR_NO`,`MIR_SR_NO`,`SR_NO`,`MIR_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_NRGP_DETAIL'
# 
CREATE TABLE `D_INV_NRGP_DETAIL` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `GATEPASS_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `WAREHOUSE_ID` varchar(5) default '',
  `LOCATION_ID` varchar(5) default '',
  `ITEM_CODE` varchar(10) NOT NULL default '',
  `NRGP_DESC` varchar(100) default '',
  `UNIT` bigint(3) NOT NULL default '0',
  `QTY` float(10,3) NOT NULL default '0.000',
  `RJN_NO` varchar(10) default '',
  `RJN_SRNO` bigint(3) default '0',
  `GATEPASSREQ_NO` varchar(10) default '',
  `GATEPASSREQ_SRNO` bigint(20) default '0',
  `DECLARATION_ID` varchar(10) default '',
  `DECLARATION_SRNO` bigint(3) default '0',
  `REMARKS` varchar(50) default '',
  `CANCELED` tinyint(1) default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `RJN_TYPE` int(11) default '0',
  `RATE` float(10,2) NOT NULL default '0.00',
  `FREIGHT` double default '0',
  `OCTROI` double default '0',
  PRIMARY KEY  (`COMPANY_ID`,`GATEPASS_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_NRGP_DETAIL_DETAIL'
# 
CREATE TABLE `D_INV_NRGP_DETAIL_DETAIL` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `GATEPASS_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `SRNO` bigint(3) NOT NULL default '0',
  `LOT_NO` varchar(10) NOT NULL default '',
  `LOT_QTY` float(10,3) NOT NULL default '0.000',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `FREIGHT` double default '0',
  `OCTROI` double default '0',
  PRIMARY KEY  (`COMPANY_ID`,`GATEPASS_NO`,`SR_NO`,`SRNO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_NRGP_HEADER'
# 
CREATE TABLE `D_INV_NRGP_HEADER` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `GATEPASS_NO` varchar(10) NOT NULL default '',
  `GATEPASS_DATE` date NOT NULL default '0000-00-00',
  `FOR_DEPT` bigint(2) NOT NULL default '0',
  `GATEPASS_TYPE` char(3) NOT NULL default '',
  `NRGP_WITH_LOT` tinyint(1) NOT NULL default '0',
  `SUPP_ID` varchar(6) NOT NULL default '',
  `MODE_TRANSPORT` bigint(3) default '0',
  `TRANSPORTER` bigint(3) default '0',
  `TOTAL_AMOUNT` float(10,2) NOT NULL default '0.00',
  `APPROVED` tinyint(1) NOT NULL default '0',
  `APPROVED_DATE` date NOT NULL default '0000-00-00',
  `REJECTED_DATE` date NOT NULL default '0000-00-00',
  `REJECTED_REMARKS` varchar(100) NOT NULL default '',
  `REMARKS` varchar(200) default '',
  `CANCELED` tinyint(1) default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` bigint(3) NOT NULL default '0',
  `REJECTED` tinyint(1) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`GATEPASS_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_PUR_DETAIL'
# 
CREATE TABLE `D_INV_PUR_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `PO_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `ITEM_DESC` varchar(100) default '',
  `PART_NO` varchar(30) default '',
  `QTY` double NOT NULL default '0',
  `RATE` double NOT NULL default '0',
  `RECD_QTY` double default '0',
  `PENDING_QTY` double default '0',
  `DISC_PER` double default '0',
  `DISC_AMT` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `NET_AMOUNT` double default '0',
  `INDENT_NO` varchar(10) default '',
  `INDENT_SR_NO` bigint(20) default '0',
  `REFERENCE` varchar(30) default '',
  `DELIVERY_DATE` date default '0000-00-00',
  `REMARKS` varchar(100) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(200) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(200) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(200) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(200) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(200) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(200) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(200) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(200) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(200) default '',
  PRIMARY KEY  (`COMPANY_ID`,`PO_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_REQ_DETAIL'
# 
CREATE TABLE `D_INV_REQ_DETAIL` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `REQ_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `ITEM_CODE` varchar(10) NOT NULL default '',
  `REQ_QTY` float(10,3) NOT NULL default '0.000',
  `INDENT_QTY` float(10,3) NOT NULL default '0.000',
  `BAL_QTY` float(10,3) NOT NULL default '0.000',
  `UNIT` bigint(2) NOT NULL default '0',
  `REMARKS` varchar(100) NOT NULL default '',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `ISSUED_QTY` bigint(103) NOT NULL default '0',
  `REQUIRED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`REQ_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_REQ_HEADER'
# 
CREATE TABLE `D_INV_REQ_HEADER` (
  `COMPANY_ID` bigint(3) NOT NULL default '0',
  `REQ_NO` varchar(10) NOT NULL default '',
  `REQ_DATE` date NOT NULL default '0000-00-00',
  `STORE` char(1) NOT NULL default '',
  `SOURCE_DEPT_ID` bigint(2) NOT NULL default '0',
  `DEST_DEPT_ID` bigint(2) NOT NULL default '0',
  `PURPOSE` varchar(30) NOT NULL default '',
  `REQUIRED_DATE` date NOT NULL default '0000-00-00',
  `BUYER` bigint(3) NOT NULL default '0',
  `APPROVED` tinyint(1) NOT NULL default '0',
  `APPROVED_DATE` date NOT NULL default '0000-00-00',
  `REJECTED_DATE` date NOT NULL default '0000-00-00',
  `REJECTED_REMARKS` varchar(50) NOT NULL default '',
  `REMARKS` varchar(200) NOT NULL default '',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `STATUS` char(1) NOT NULL default '',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` int(11) default '0',
  `REJECTED` tinyint(1) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`REQ_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_RGP_DETAIL'
# 
CREATE TABLE `D_INV_RGP_DETAIL` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `GATEPASS_NO` char(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `WAREHOUSE_ID` char(5) NOT NULL default '',
  `LOCATION_ID` char(5) NOT NULL default '',
  `ITEM_CODE` char(10) NOT NULL default '',
  `RGP_DESC` char(100) NOT NULL default '',
  `UNIT` bigint(3) NOT NULL default '0',
  `QTY` float(10,3) NOT NULL default '0.000',
  `RJN_NO` char(10) NOT NULL default '',
  `RJN_SRNO` bigint(3) NOT NULL default '0',
  `GATEPASSREQ_NO` char(10) NOT NULL default '',
  `GATEPASSREQ_SRNO` bigint(20) NOT NULL default '0',
  `DECLARATION_ID` char(10) NOT NULL default '',
  `DECLARATION_SRNO` bigint(3) NOT NULL default '0',
  `REMARKS` char(50) NOT NULL default '',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `BAL_QTY` float(10,2) NOT NULL default '0.00',
  `RJN_TYPE` int(11) default '0',
  `RATE` float(10,2) NOT NULL default '0.00',
  PRIMARY KEY  (`COMPANY_ID`,`GATEPASS_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_RGP_DETAIL_DETAIL'
# 
CREATE TABLE `D_INV_RGP_DETAIL_DETAIL` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `GATEPASS_NO` char(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `SRNO` bigint(3) NOT NULL default '0',
  `LOT_NO` char(10) NOT NULL default '',
  `LOT_QTY` float(10,3) NOT NULL default '0.000',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`GATEPASS_NO`,`SR_NO`,`SRNO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_RGP_HEADER'
# 
CREATE TABLE `D_INV_RGP_HEADER` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `GATEPASS_NO` char(10) NOT NULL default '',
  `GATEPASS_DATE` date NOT NULL default '0000-00-00',
  `FOR_DEPT` bigint(2) NOT NULL default '0',
  `GATEPASS_TYPE` char(3) NOT NULL default '',
  `RGP_WITH_LOT` tinyint(1) NOT NULL default '0',
  `SUPP_ID` char(6) NOT NULL default '',
  `MODE_TRANSPORT` bigint(3) NOT NULL default '0',
  `TRANSPORTER` bigint(3) NOT NULL default '0',
  `TOTAL_AMOUNT` float(10,2) NOT NULL default '0.00',
  `APPROVED` tinyint(1) NOT NULL default '0',
  `APPROVED_DATE` date NOT NULL default '0000-00-00',
  `REJECTED_DATE` date NOT NULL default '0000-00-00',
  `REJECTED_REMARKS` char(100) NOT NULL default '',
  `REMARKS` char(200) NOT NULL default '',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`GATEPASS_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_RJN_DETAIL'
# 
CREATE TABLE `D_INV_RJN_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `RJN_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `GRN_NO` varchar(10) default '',
  `GRN_SR_NO` bigint(20) default '0',
  `PO_NO` varchar(10) default '',
  `PO_SR_NO` bigint(20) default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `BOE_NO` varchar(10) default '',
  `BOE_SR_NO` varchar(10) default '',
  `BOE_DATE` date default '0000-00-00',
  `QTY` double NOT NULL default '0',
  `UNIT` bigint(20) default '0',
  `REASON` varchar(255) default '',
  `REMARKS` varchar(200) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(200) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(200) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(200) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(200) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(200) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(200) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(200) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(200) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(200) default '',
  `WAREHOUSE_ID` varchar(10) default '',
  `LOCATION_ID` varchar(10) default '',
  `TOTAL_AMOUNT` double default '0',
  `NET_AMOUNT` double default '0',
  `RJN_TYPE` bigint(20) NOT NULL default '0',
  `PO_TYPE` int(11) default '0',
  `GRN_TYPE` int(11) default '0',
  `BAL_QTY` float(10,3) NOT NULL default '0.000',
  PRIMARY KEY  (`COMPANY_ID`,`RJN_NO`,`SR_NO`,`RJN_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_RJN_HEADER'
# 
CREATE TABLE `D_INV_RJN_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `RJN_NO` varchar(10) NOT NULL default '',
  `RJN_DATE` date default '0000-00-00',
  `FOR_STORE` bigint(20) default '0',
  `SUPP_ID` varchar(10) default '',
  `MODE_TRANSPORT` bigint(20) default '0',
  `TRANSPORTER` bigint(20) default '0',
  `GATEPASS_TYPE` char(1) default '',
  `GATEPASS_NO` varchar(10) default '',
  `GATEPASS_DATE` date default '0000-00-00',
  `CURRENCY_ID` bigint(20) default '0',
  `CURRENCY_RATE` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `NET_AMOUNT` double default '0',
  `REMARKS` varchar(200) default '',
  `CANCELLED` tinyint(1) default '0',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` bigint(20) default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(200) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(200) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(200) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(200) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(200) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(200) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(200) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(200) default '',
  `COLUMN_5_CAPTION` varchar(200) default '',
  `RJN_TYPE` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`RJN_NO`,`RJN_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_RJN_LOT'
# 
CREATE TABLE `D_INV_RJN_LOT` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `RJN_NO` varchar(10) NOT NULL default '',
  `RJN_SR_NO` bigint(20) NOT NULL default '0',
  `SR_NO` bigint(20) NOT NULL default '0',
  `ITEM_LOT_NO` varchar(10) NOT NULL default '',
  `LOT_QTY` double default '0',
  `RJN_TYPE` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`RJN_NO`,`RJN_SR_NO`,`SR_NO`,`RJN_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_STM_DETAIL'
# 
CREATE TABLE `D_INV_STM_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `STM_NO` varchar(10) NOT NULL default '',
  `STM_TYPE` int(11) NOT NULL default '0',
  `SR_NO` int(11) NOT NULL default '0',
  `WAREHOUSE_ID` varchar(10) NOT NULL default '',
  `LOCATION_ID` varchar(10) NOT NULL default '',
  `ITEM_ID` varchar(10) default '',
  `BOE_NO` varchar(10) default '',
  `BOE_SR_NO` varchar(10) default '',
  `BOE_DATE` date default '0000-00-00',
  `QTY` double NOT NULL default '0',
  `UNIT` int(11) default '0',
  `RATE` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `NET_AMOUNT` double default '0',
  `SHADE` varchar(10) default '',
  `W_MIE` varchar(10) default '',
  `NO_CASE` varchar(10) default '',
  `STM_DESC` varchar(200) default '',
  `PER` double default '0',
  `QTY_REQD` double default '0',
  `MFG_PROG_NO` varchar(15) default '',
  `MATERIAL_CODE_NO` varchar(15) default '',
  `BALE_NO` varchar(15) default '',
  `CAPTIVE_CONSUMABLE` tinyint(1) default '0',
  `INDENT_NO` varchar(10) default '',
  `INDENT_SR_NO` int(11) default '0',
  `CENTER_CODE_NO` varchar(10) default '',
  `LF_NO` varchar(15) default '',
  `REMARKS` varchar(100) default '',
  `ACCEPTED_TO_UNIT` tinyint(1) default '0',
  `CREATED_BY` int(11) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` int(11) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` int(11) default '0',
  `COLUMN_1_FORMULA` varchar(100) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(100) default '',
  `COLUMN_2_ID` int(11) default '0',
  `COLUMN_2_FORMULA` varchar(100) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_3_ID` int(11) default '0',
  `COLUMN_3_FORMULA` varchar(100) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(100) default '',
  `COLUMN_4_ID` int(11) default '0',
  `COLUMN_4_FORMULA` varchar(100) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(100) default '',
  `COLUMN_5_ID` int(11) default '0',
  `COLUMN_5_FORMULA` varchar(100) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(100) default '',
  `COLUMN_6_ID` int(11) default '0',
  `COLUMN_6_FORMULA` varchar(100) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(100) default '',
  `COLUMN_7_ID` int(11) default '0',
  `COLUMN_7_FORMULA` varchar(100) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(100) default '',
  `COLUMN_8_ID` int(11) default '0',
  `COLUMN_8_FORMULA` varchar(100) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(100) default '',
  `COLUMN_9_ID` int(11) default '0',
  `COLUMN_9_FORMULA` varchar(100) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(100) default '',
  `COLUMN_10_ID` int(11) default '0',
  `COLUMN_10_FORMULA` varchar(100) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(100) default '',
  `COLUMN_2_CAPTION` varchar(100) default '',
  `RECEIVED_QTY` double default '0',
  PRIMARY KEY  (`COMPANY_ID`,`STM_NO`,`STM_TYPE`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_STM_HEADER'
# 
CREATE TABLE `D_INV_STM_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `STM_NO` varchar(10) NOT NULL default '',
  `STM_DATE` date default '0000-00-00',
  `FOR_STORE` int(11) default '0',
  `FOR_DEPT_ID` int(11) default '0',
  `GATEPASS_NO` varchar(10) default '',
  `MODE_TRANSPORT` int(11) default '0',
  `TRANSPORTER` int(11) default '0',
  `CURRENCY_ID` int(11) default '0',
  `CURRENCY_RATE` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `GROSS_AMOUNT` double default '0',
  `CAPTIVE_CONSUMABLE` tinyint(1) default '0',
  `TRANSFER_TO_UNIT` tinyint(1) default '0',
  `PURPOSE` varchar(100) default '',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` int(11) default '0',
  `REMARKS` varchar(200) default '',
  `STATUS` char(1) default '',
  `CENVATED_ITEMS` tinyint(1) default '0',
  `CANCELLED` tinyint(1) default '0',
  `CREATED_BY` int(11) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` int(11) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` int(11) default '0',
  `COLUMN_1_FORMULA` varchar(100) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(100) default '',
  `COLUMN_2_ID` int(11) default '0',
  `COLUMN_2_FORMULA` varchar(100) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(100) default '',
  `COLUMN_3_ID` int(11) default '0',
  `COLUMN_3_FORMULA` varchar(100) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(100) default '',
  `COLUMN_4_ID` int(11) default '0',
  `COLUMN_4_FORMULA` varchar(100) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_5_ID` int(11) default '0',
  `COLUMN_5_FORMULA` varchar(100) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(100) default '',
  `COLUMN_6_ID` int(11) default '0',
  `COLUMN_6_FORMULA` varchar(100) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(100) default '',
  `COLUMN_7_ID` int(11) default '0',
  `COLUMN_7_FORMULA` varchar(100) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(100) default '',
  `COLUMN_8_ID` int(11) default '0',
  `COLUMN_8_FORMULA` varchar(100) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(100) default '',
  `COLUMN_9_ID` int(11) default '0',
  `COLUMN_9_FORMULA` varchar(100) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(100) default '',
  `COLUMN_10_ID` int(11) default '0',
  `COLUMN_10_FORMULA` varchar(100) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(100) default '',
  `STM_TYPE` int(11) NOT NULL default '0',
  `COLUMN_4_CAPTION` varchar(100) default '',
  `OTHER_COMPANY_REF` tinyint(1) NOT NULL default '0',
  `FOR_UNIT` bigint(3) NOT NULL default '0',
  `STMTYPE` char(1) NOT NULL default '',
  PRIMARY KEY  (`COMPANY_ID`,`STM_NO`,`STM_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_STM_LOT'
# 
CREATE TABLE `D_INV_STM_LOT` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `STM_NO` varchar(10) NOT NULL default '',
  `STM_SR_NO` int(11) NOT NULL default '0',
  `SR_NO` int(11) NOT NULL default '0',
  `ITEM_LOT_NO` varchar(10) default '',
  `LOT_QTY` double default '0',
  `STM_TYPE` int(11) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`STM_NO`,`STM_SR_NO`,`STM_TYPE`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_INV_WAREHOUSE_MASTER'
# 
CREATE TABLE `D_INV_WAREHOUSE_MASTER` (
  `COMPANY_ID` bigint(5) NOT NULL default '0',
  `WAREHOUSE_ID` varchar(5) NOT NULL default '',
  `WAREHOUSE_NAME` varchar(35) NOT NULL default '',
  `RESPONSIBLE_PERSON` bigint(20) default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`WAREHOUSE_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_AMEND_DETAIL'
# 
CREATE TABLE `D_PUR_AMEND_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `PO_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `ITEM_DESC` varchar(100) default NULL,
  `PART_NO` varchar(30) default NULL,
  `QTY` double NOT NULL default '0',
  `RATE` double default '0',
  `RECD_QTY` double default '0',
  `PENDING_QTY` double default '0',
  `DISC_PER` double default '0',
  `DISC_AMT` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `NET_AMOUNT` double default '0',
  `INDENT_NO` varchar(10) default NULL,
  `INDENT_SR_NO` bigint(20) default '0',
  `REFERENCE` varchar(30) default NULL,
  `DELIVERY_DATE` date default NULL,
  `REMARKS` varchar(100) default NULL,
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default NULL,
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default NULL,
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default NULL,
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(200) default NULL,
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default NULL,
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(200) default NULL,
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default NULL,
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(200) default NULL,
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default NULL,
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(200) default NULL,
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default NULL,
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(200) default NULL,
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default NULL,
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(200) default NULL,
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default NULL,
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(200) default NULL,
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default NULL,
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(200) default NULL,
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default NULL,
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default NULL,
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default NULL,
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(200) default NULL,
  `AMEND_NO` varchar(10) NOT NULL default '',
  `PO_TYPE` int(11) NOT NULL default '0',
  `UNIT` int(11) default '0',
  `LAST_AMENDMENT` tinyint(1) default '0',
  `IMPORT_CONCESS` tinyint(1) default '0',
  `LANDED_RATE` double default '0',
  `QUOT_ID` varchar(10) default '',
  `QUOT_SR_NO` int(11) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`AMEND_NO`,`SR_NO`,`PO_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_AMEND_HEADER'
# 
CREATE TABLE `D_PUR_AMEND_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `PO_NO` varchar(10) NOT NULL default '',
  `PO_DATE` date NOT NULL default '0000-00-00',
  `PO_REF` char(1) default '',
  `SUPP_ID` varchar(10) NOT NULL default '',
  `REF_A` varchar(30) default '',
  `REF_B` varchar(30) default '',
  `QUOTATION_NO` varchar(10) default '',
  `QUOTATION_DATE` date default '0000-00-00',
  `INQUIRY_NO` varchar(10) default '',
  `INQUIRY_DATE` date default '0000-00-00',
  `BUYER` bigint(20) default '0',
  `PURPOSE` varchar(100) default '',
  `SUBJECT` varchar(200) default '',
  `CURRENCY_ID` bigint(20) default '0',
  `CURRENCY_RATE` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `NET_AMOUNT` double default '0',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `STATUS` char(1) default '',
  `ATTACHEMENT` tinyint(1) default '0',
  `ATTACHEMENT_PATH` varchar(100) default '',
  `REMARKS` varchar(255) default '',
  `SHIP_ID` bigint(20) default '0',
  `DELIVERY_DATE` date default '0000-00-00',
  `PAYMENT_DESC` varchar(100) default '',
  `DESPATCH_DESC` varchar(100) default '',
  `INSURANCE` varchar(100) default '',
  `LICENSE_NO` varchar(100) default '',
  `SERVICE_TAX` varchar(100) default '',
  `PACKING_DESC` varchar(100) default '',
  `EXCISE` varchar(100) default '',
  `OCTROI` varchar(100) default '',
  `FREIGHT` varchar(100) default '',
  `TCC` varchar(100) default '',
  `FORWARDING_DESC` varchar(100) default '',
  `CANCELLED` tinyint(1) default '0',
  `HIERARCHY_ID` bigint(20) default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(200) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(200) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(200) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(200) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(200) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(200) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(200) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(200) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(200) default '',
  `AMEND_NO` varchar(10) NOT NULL default '',
  `AMEND_DATE` date NOT NULL default '0000-00-00',
  `AMEND_SR_NO` bigint(20) NOT NULL default '0',
  `PO_TYPE` int(11) NOT NULL default '0',
  `LAST_AMENDMENT` tinyint(1) default '0',
  `PAYMENT_CODE` int(11) default '0',
  `DESPATCH_CODE` int(11) default '0',
  `PACKING_CODE` int(11) default '0',
  `FORWARDING_CODE` int(11) default '0',
  `INSURANCE_CODE` int(11) default '0',
  `LICENSE_CODE` int(11) default '0',
  `SERVICETAX_CODE` int(11) default '0',
  `OCTROI_CODE` int(11) default '0',
  `TCC_CODE` int(11) default '0',
  `EXCISE_CODE` int(11) default '0',
  `FREIGHT_CODE` int(11) default '0',
  `REASON_CODE` int(11) default '0',
  `AMEND_REASON` varchar(200) default '',
  `IMPORT_CONCESS` tinyint(1) default '0',
  `PRINT_LINE_1` varchar(100) default '',
  `PRINT_LINE_2` varchar(100) default '',
  `ST_CODE` int(11) default '0',
  `ST_DESC` varchar(100) default '',
  `ESI_CODE` int(11) default '0',
  `ESI_DESC` varchar(100) default '',
  `FOR_CODE` int(11) default '0',
  `FOR_DESC` varchar(100) default '',
  `FOB_CODE` int(11) default '0',
  `FOB_DESC` varchar(100) default '',
  PRIMARY KEY  (`COMPANY_ID`,`AMEND_NO`,`PO_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_CIF_DETAIL'
# 
CREATE TABLE `D_PUR_CIF_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `CIF_NO` varchar(10) NOT NULL default '',
  `SR_NO` int(11) NOT NULL default '0',
  `ITEM_ID` varchar(10) default '',
  `ITEM_DESC` varchar(200) default '',
  `ORDER_MODE` int(11) default '0',
  `AIR_FREIGHT` double default '0',
  `GROSS_WEIGHT` double default '0',
  `CARTONS` double default '0',
  `VOLUME_CM` double default '0',
  `VOLUME_WEIGHT` double default '0',
  `REMARKS` varchar(200) default '',
  `TOTAL_AMOUNT` double default '0',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(100) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(100) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(100) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(100) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(100) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(100) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(100) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(100) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(100) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(100) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(100) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(100) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(100) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(100) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(100) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(100) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(100) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(100) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(100) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(100) default '',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`CIF_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_CIF_HEADER'
# 
CREATE TABLE `D_PUR_CIF_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `CIF_NO` varchar(10) NOT NULL default '',
  `CIF_DATE` date NOT NULL default '0000-00-00',
  `PO_NO` varchar(10) NOT NULL default '',
  `PO_DATE` date default '0000-00-00',
  `PO_TYPE` int(11) NOT NULL default '0',
  `SUBJECT` varchar(200) default '',
  `INVOICE_NO` varchar(10) default '',
  `INVOICE_DATE` date default '0000-00-00',
  `PURPOSE` varchar(200) default '',
  `PRIORITY` int(11) default '0',
  `REMARKS` varchar(200) default '',
  `DECISION` varchar(200) default '',
  `CIF_COST` double NOT NULL default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` int(11) default '0',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `CANCELLED` tinyint(1) default '0',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(100) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(100) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(100) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(100) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(100) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(100) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(100) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(100) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(100) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(100) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(100) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(100) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(100) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(100) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(100) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(100) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(100) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(100) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(100) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(100) default '',
  PRIMARY KEY  (`COMPANY_ID`,`CIF_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_INQUIRY_DETAIL'
# 
CREATE TABLE `D_PUR_INQUIRY_DETAIL` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `INQUIRY_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `ITEM_CODE` varchar(10) NOT NULL default '',
  `UNIT` bigint(3) NOT NULL default '0',
  `QTY` float(10,3) NOT NULL default '0.000',
  `DELIVERY_DATE` date NOT NULL default '0000-00-00',
  `REMARKS` varchar(50) NOT NULL default '',
  `CANCELED` tinyint(1) NOT NULL default '0',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `INDENT_NO` varchar(10) NOT NULL default '',
  `INDENT_SRNO` bigint(3) NOT NULL default '0',
  PRIMARY KEY  (`COMPANY_ID`,`INQUIRY_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_INQUIRY_HEADER'
# 
CREATE TABLE `D_PUR_INQUIRY_HEADER` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `INQUIRY_NO` varchar(10) NOT NULL default '',
  `INQUIRY_DATE` date NOT NULL default '0000-00-00',
  `INQUIRY_TYPE` char(1) NOT NULL default '',
  `PROJECT` varchar(50) NOT NULL default '',
  `BUYER` varchar(30) NOT NULL default '0',
  `PURPOSE` varchar(50) NOT NULL default '',
  `REMARKS` varchar(100) NOT NULL default '',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `STATUS` char(1) NOT NULL default '',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` int(11) default '0',
  `LAST_QUOT_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`INQUIRY_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_INQUIRY_SUPP'
# 
CREATE TABLE `D_PUR_INQUIRY_SUPP` (
  `COMPANY_ID` bigint(2) NOT NULL default '0',
  `INQUIRY_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(3) NOT NULL default '0',
  `SUPP_ID` varchar(10) NOT NULL default '',
  `CREATED_BY` bigint(3) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(3) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`INQUIRY_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_PO_DETAIL'
# 
CREATE TABLE `D_PUR_PO_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `PO_NO` varchar(10) NOT NULL default '',
  `SR_NO` bigint(20) NOT NULL default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `ITEM_DESC` varchar(100) default NULL,
  `PART_NO` varchar(30) default NULL,
  `QTY` double NOT NULL default '0',
  `RATE` double default '0',
  `RECD_QTY` double default '0',
  `PENDING_QTY` double default '0',
  `DISC_PER` double default '0',
  `DISC_AMT` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `NET_AMOUNT` double default '0',
  `INDENT_NO` varchar(10) default NULL,
  `INDENT_SR_NO` bigint(20) default '0',
  `REFERENCE` varchar(30) default NULL,
  `DELIVERY_DATE` date default NULL,
  `REMARKS` varchar(100) default NULL,
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default NULL,
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default NULL,
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default NULL,
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(200) default NULL,
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default NULL,
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(200) default NULL,
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default NULL,
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(200) default NULL,
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default NULL,
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(200) default NULL,
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default NULL,
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(200) default NULL,
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default NULL,
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(200) default NULL,
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default NULL,
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(200) default NULL,
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default NULL,
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(200) default NULL,
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default NULL,
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default NULL,
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default NULL,
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(200) default NULL,
  `PO_TYPE` int(11) NOT NULL default '0',
  `UNIT` int(11) default '0',
  `IMPORT_CONCESS` tinyint(1) default '0',
  `LANDED_RATE` double default '0',
  `QUOT_ID` varchar(10) default '',
  `QUOT_SR_NO` int(11) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`PO_NO`,`SR_NO`,`PO_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_PO_HEADER'
# 
CREATE TABLE `D_PUR_PO_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `PO_NO` varchar(10) NOT NULL default '',
  `PO_DATE` date NOT NULL default '0000-00-00',
  `PO_REF` char(1) default '',
  `SUPP_ID` varchar(10) NOT NULL default '',
  `REF_A` varchar(30) default '',
  `REF_B` varchar(30) default '',
  `QUOTATION_NO` varchar(10) default '',
  `QUOTATION_DATE` date default '0000-00-00',
  `INQUIRY_NO` varchar(10) default '',
  `INQUIRY_DATE` date default '0000-00-00',
  `BUYER` bigint(20) default '0',
  `PURPOSE` varchar(100) default '',
  `SUBJECT` varchar(200) default '',
  `CURRENCY_ID` bigint(20) default '0',
  `CURRENCY_RATE` double default '0',
  `TOTAL_AMOUNT` double default '0',
  `NET_AMOUNT` double default '0',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `STATUS` char(1) default '',
  `ATTACHEMENT` tinyint(1) default '0',
  `ATTACHEMENT_PATH` varchar(100) default '',
  `REMARKS` varchar(255) default '',
  `SHIP_ID` bigint(20) default '0',
  `DELIVERY_DATE` date default '0000-00-00',
  `PAYMENT_DESC` varchar(100) default '',
  `DESPATCH_DESC` varchar(100) default '',
  `INSURANCE` varchar(100) default '',
  `LICENSE_NO` varchar(100) default '',
  `SERVICE_TAX` varchar(100) default '',
  `PACKING_DESC` varchar(100) default '',
  `EXCISE` varchar(100) default '',
  `OCTROI` varchar(100) default '',
  `FREIGHT` varchar(100) default '',
  `TCC` varchar(100) default '',
  `FORWARDING_DESC` varchar(100) default '',
  `CANCELLED` tinyint(1) default '0',
  `HIERARCHY_ID` bigint(20) default '0',
  `CREATED_BY` bigint(20) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` bigint(20) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `COLUMN_1_ID` bigint(20) default '0',
  `COLUMN_1_FORMULA` varchar(200) default '',
  `COLUMN_1_PER` double default '0',
  `COLUMN_1_AMT` double default '0',
  `COLUMN_1_CAPTION` varchar(200) default '',
  `COLUMN_2_ID` bigint(20) default '0',
  `COLUMN_2_FORMULA` varchar(200) default '',
  `COLUMN_2_PER` double default '0',
  `COLUMN_2_AMT` double default '0',
  `COLUMN_2_CAPTION` varchar(200) default '',
  `COLUMN_3_ID` bigint(20) default '0',
  `COLUMN_3_FORMULA` varchar(200) default '',
  `COLUMN_3_PER` double default '0',
  `COLUMN_3_AMT` double default '0',
  `COLUMN_3_CAPTION` varchar(200) default '',
  `COLUMN_4_ID` bigint(20) default '0',
  `COLUMN_4_FORMULA` varchar(200) default '',
  `COLUMN_4_PER` double default '0',
  `COLUMN_4_AMT` double default '0',
  `COLUMN_4_CAPTION` varchar(200) default '',
  `COLUMN_5_ID` bigint(20) default '0',
  `COLUMN_5_FORMULA` varchar(200) default '',
  `COLUMN_5_PER` double default '0',
  `COLUMN_5_AMT` double default '0',
  `COLUMN_5_CAPTION` varchar(200) default '',
  `COLUMN_6_ID` bigint(20) default '0',
  `COLUMN_6_FORMULA` varchar(200) default '',
  `COLUMN_6_PER` double default '0',
  `COLUMN_6_AMT` double default '0',
  `COLUMN_6_CAPTION` varchar(200) default '',
  `COLUMN_7_ID` bigint(20) default '0',
  `COLUMN_7_FORMULA` varchar(200) default '',
  `COLUMN_7_PER` double default '0',
  `COLUMN_7_AMT` double default '0',
  `COLUMN_7_CAPTION` varchar(200) default '',
  `COLUMN_8_ID` bigint(20) default '0',
  `COLUMN_8_FORMULA` varchar(200) default '',
  `COLUMN_8_PER` double default '0',
  `COLUMN_8_AMT` double default '0',
  `COLUMN_8_CAPTION` varchar(200) default '',
  `COLUMN_9_ID` bigint(20) default '0',
  `COLUMN_9_FORMULA` varchar(200) default '',
  `COLUMN_9_PER` double default '0',
  `COLUMN_9_AMT` double default '0',
  `COLUMN_9_CAPTION` varchar(200) default '',
  `COLUMN_10_ID` bigint(20) default '0',
  `COLUMN_10_FORMULA` varchar(200) default '',
  `COLUMN_10_PER` double default '0',
  `COLUMN_10_AMT` double default '0',
  `COLUMN_10_CAPTION` varchar(200) default '',
  `PO_TYPE` int(11) NOT NULL default '0',
  `PAYMENT_CODE` int(11) default '0',
  `DESPATCH_CODE` int(11) default '0',
  `PACKING_CODE` int(11) default '0',
  `FORWARDING_CODE` int(11) default '0',
  `INSURANCE_CODE` int(11) default '0',
  `LICENSE_CODE` int(11) default '0',
  `SERVICETAX_CODE` int(11) default '0',
  `OCTROI_CODE` int(11) default '0',
  `TCC_CODE` int(11) default '0',
  `EXCISE_CODE` int(11) default '0',
  `FREIGHT_CODE` int(11) default '0',
  `IMPORT_CONCESS` tinyint(1) default '0',
  `PRINT_LINE_1` varchar(100) default '',
  `PRINT_LINE_2` varchar(100) default '',
  `ST_CODE` int(11) default '0',
  `ST_DESC` varchar(100) default '',
  `ESI_CODE` int(11) default '0',
  `ESI_DESC` varchar(100) default '',
  `FOR_CODE` int(11) default '0',
  `FOR_DESC` varchar(100) default '',
  `FOB_CODE` int(11) default '0',
  `FOB_DESC` varchar(100) default '',
  PRIMARY KEY  (`COMPANY_ID`,`PO_NO`,`PO_TYPE`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_QUOT_APPROVAL_DETAIL'
# 
CREATE TABLE `D_PUR_QUOT_APPROVAL_DETAIL` (
  `COMPANY_ID` int(11) NOT NULL default '0',
  `APPROVAL_NO` varchar(10) NOT NULL default '',
  `SR_NO` int(11) NOT NULL default '0',
  `INQUIRY_NO` varchar(10) default '',
  `INQUIRY_SR_NO` int(11) default '0',
  `QUOT_ID` varchar(10) default '',
  `QUOT_SR_NO` int(11) default '0',
  `SUPP_ID` varchar(10) NOT NULL default '',
  `ITEM_CODE` varchar(10) default '',
  `QTY` double default '0',
  `RATE` double default '0',
  `LAND_COST` double default '0',
  `LAST_PO_NO` varchar(10) default '',
  `LAST_PO_DATE` date default '0000-00-00',
  `REMARKS` varchar(200) default '',
  `CREATED_BY` int(11) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` int(11) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `APPROVED` tinyint(1) default '0',
  `NET_AMOUNT` double default '0',
  `TAX_AMOUNT` double default '0',
  `LAST_LANDED_RATE` double default '0',
  PRIMARY KEY  (`COMPANY_ID`,`APPROVAL_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_QUOT_APPROVAL_HEADER'
# 
CREATE TABLE `D_PUR_QUOT_APPROVAL_HEADER` (
  `COMPANY_ID` int(11) NOT NULL default '0',
  `APPROVAL_NO` varchar(10) NOT NULL default '',
  `APPROVAL_DATE` date NOT NULL default '0000-00-00',
  `INQUIRY_NO` varchar(10) default '',
  `INQUIRY_DATE` date default '0000-00-00',
  `STATUS` char(1) default '',
  `CREATED_BY` int(11) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` int(11) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` int(11) default '0',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `CANCELLED` tinyint(1) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`APPROVAL_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_QUOT_DETAIL'
# 
CREATE TABLE `D_PUR_QUOT_DETAIL` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `QUOT_ID` varchar(10) NOT NULL default '',
  `SR_NO` int(11) NOT NULL default '0',
  `INQUIRY_NO` varchar(10) default NULL,
  `INQUIRY_SRNO` bigint(20) default NULL,
  `ITEM_ID` varchar(10) default NULL,
  `UNIT` int(11) default NULL,
  `QTY` double(15,3) default NULL,
  `RATE` double(15,3) default NULL,
  `TOTAL_AMOUNT` double(15,3) default NULL,
  `ACCESS_AMOUNT` double(15,3) default NULL,
  `REMARKS` varchar(200) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `CREATED_DATE` date default NULL,
  `MODIFIED_BY` bigint(20) default NULL,
  `MODIFIED_DATE` date default NULL,
  `COLUMN_1_ID` bigint(20) default NULL,
  `COLUMN_1_FORMULA` varchar(200) default NULL,
  `COLUMN_1_PER` double(15,3) default NULL,
  `COLUMN_1_AMT` double(15,3) default NULL,
  `COLUMN_1_CAPTION` varchar(100) default NULL,
  `COLUMN_2_ID` bigint(20) default NULL,
  `COLUMN_2_FORMULA` varchar(200) default NULL,
  `COLUMN_2_PER` double(15,3) default NULL,
  `COLUMN_2_AMT` double(15,3) default NULL,
  `COLUMN_2_CAPTION` varchar(200) default NULL,
  `COLUMN_3_ID` bigint(20) default NULL,
  `COLUMN_3_FORMULA` varchar(200) default NULL,
  `COLUMN_3_PER` double(15,3) default NULL,
  `COLUMN_3_AMT` double(15,3) default NULL,
  `COLUMN_3_CAPTION` varchar(200) default NULL,
  `COLUMN_4_ID` bigint(20) default NULL,
  `COLUMN_4_FORMULA` varchar(200) default NULL,
  `COLUMN_4_PER` double(15,3) default NULL,
  `COLUMN_4_AMT` double(15,3) default NULL,
  `COLUMN_4_CAPTION` varchar(200) default NULL,
  `COLUMN_5_ID` bigint(20) default NULL,
  `COLUMN_5_FORMULA` varchar(200) default NULL,
  `COLUMN_5_PER` double(15,3) default NULL,
  `COLUMN_5_AMT` double(15,3) default NULL,
  `COLUMN_5_CAPTION` varchar(200) default NULL,
  `COLUMN_6_ID` bigint(20) default NULL,
  `COLUMN_6_FORMULA` varchar(200) default NULL,
  `COLUMN_6_PER` double(15,3) default NULL,
  `COLUMN_6_AMT` double(15,3) default NULL,
  `COLUMN_6_CAPTION` varchar(200) default NULL,
  `COLUMN_7_ID` bigint(20) default NULL,
  `COLUMN_7_FORMULA` varchar(200) default NULL,
  `COLUMN_7_PER` double(15,3) default NULL,
  `COLUMN_7_AMT` double(15,3) default NULL,
  `COLUMN_7_CAPTION` varchar(200) default NULL,
  `COLUMN_8_ID` bigint(20) default NULL,
  `COLUMN_8_FORMULA` varchar(200) default NULL,
  `COLUMN_8_PER` double(15,3) default NULL,
  `COLUMN_8_AMT` double(15,3) default NULL,
  `COLUMN_8_CAPTION` varchar(200) default NULL,
  `COLUMN_9_ID` bigint(20) default NULL,
  `COLUMN_9_FORMULA` varchar(200) default NULL,
  `COLUMN_9_PER` double(15,3) default NULL,
  `COLUMN_9_AMT` double(15,3) default NULL,
  `COLUMN_9_CAPTION` char(1) default NULL,
  `COLUMN_10_ID` bigint(20) default NULL,
  `COLUMN_10_FORMULA` varchar(200) default NULL,
  `COLUMN_10_PER` double(15,3) default NULL,
  `COLUMN_10_AMT` double(15,3) default NULL,
  `COLUMN_10_CAPTION` varchar(200) default NULL,
  `APPROVED` tinyint(1) default '0',
  `PO_QTY` double default '0',
  `BAL_QTY` double default '0',
  `SUPP_ITEM_DESC` varchar(100) default '',
  PRIMARY KEY  (`COMPANY_ID`,`QUOT_ID`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_QUOT_HEADER'
# 
CREATE TABLE `D_PUR_QUOT_HEADER` (
  `COMPANY_ID` bigint(20) NOT NULL default '0',
  `QUOT_ID` varchar(10) NOT NULL default '',
  `QUOT_NO` varchar(10) NOT NULL default '',
  `QUOT_DATE` date default '0000-00-00',
  `INQUIRY_NO` varchar(10) default NULL,
  `SUPP_ID` varchar(10) default NULL,
  `REMARKS` varchar(200) default NULL,
  `HIERARCHY_ID` bigint(20) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `CREATED_DATE` date default NULL,
  `MODIFIED_BY` bigint(20) default NULL,
  `MODIFIED_DATE` date default NULL,
  `COLUMN_1_ID` bigint(20) default NULL,
  `COLUMN_1_FORMULA` varchar(200) default NULL,
  `COLUMN_1_PER` double(15,3) default NULL,
  `COLUMN_1_AMT` double(15,3) default NULL,
  `COLUMN_1_CAPTION` varchar(100) default NULL,
  `COLUMN_2_ID` bigint(20) default NULL,
  `COLUMN_2_FORMULA` varchar(200) default NULL,
  `COLUMN_2_PER` double(15,3) default NULL,
  `COLUMN_2_AMT` double(15,3) default NULL,
  `COLUMN_2_CAPTION` varchar(200) default NULL,
  `COLUMN_3_ID` bigint(20) default NULL,
  `COLUMN_3_FORMULA` varchar(200) default NULL,
  `COLUMN_3_PER` double(15,3) default NULL,
  `COLUMN_3_AMT` double(15,3) default NULL,
  `COLUMN_3_CAPTION` varchar(200) default NULL,
  `COLUMN_4_ID` bigint(20) default NULL,
  `COLUMN_4_FORMULA` varchar(200) default NULL,
  `COLUMN_4_PER` double(15,3) default NULL,
  `COLUMN_4_AMT` double(15,3) default NULL,
  `COLUMN_4_CAPTION` varchar(200) default NULL,
  `COLUMN_5_ID` bigint(20) default NULL,
  `COLUMN_5_FORMULA` varchar(200) default NULL,
  `COLUMN_5_PER` double(15,3) default NULL,
  `COLUMN_5_AMT` double(15,3) default NULL,
  `COLUMN_5_CAPTION` varchar(200) default NULL,
  `COLUMN_6_ID` bigint(20) default NULL,
  `COLUMN_6_FORMULA` varchar(200) default NULL,
  `COLUMN_6_PER` double(15,3) default NULL,
  `COLUMN_6_AMT` double(15,3) default NULL,
  `COLUMN_6_CAPTION` varchar(200) default NULL,
  `COLUMN_7_ID` bigint(20) default NULL,
  `COLUMN_7_FORMULA` varchar(200) default NULL,
  `COLUMN_7_PER` double(15,3) default NULL,
  `COLUMN_7_AMT` double(15,3) default NULL,
  `COLUMN_7_CAPTION` varchar(200) default NULL,
  `COLUMN_8_ID` bigint(20) default NULL,
  `COLUMN_8_FORMULA` varchar(200) default NULL,
  `COLUMN_8_PER` double(15,3) default NULL,
  `COLUMN_8_AMT` double(15,3) default NULL,
  `COLUMN_8_CAPTION` varchar(200) default NULL,
  `COLUMN_9_ID` bigint(20) default NULL,
  `COLUMN_9_FORMULA` varchar(200) default NULL,
  `COLUMN_9_PER` double(15,3) default NULL,
  `COLUMN_9_AMT` double(15,3) default NULL,
  `COLUMN_9_CAPTION` char(1) default NULL,
  `COLUMN_10_ID` bigint(20) default NULL,
  `COLUMN_10_FORMULA` varchar(200) default NULL,
  `COLUMN_10_PER` double(15,3) default NULL,
  `COLUMN_10_AMT` double(15,3) default NULL,
  `COLUMN_10_CAPTION` varchar(200) default NULL,
  `APPROVED` tinyint(4) default NULL,
  `APPROVED_DATE` date default NULL,
  `REJECTED` tinyint(4) default NULL,
  `REJECTED_DATE` date default NULL,
  `PAYMENT_DESC` varchar(100) default '',
  `PAYMENT_CODE` int(11) default '0',
  `DESPATCH_DESC` varchar(100) default '',
  `DESPATCH_CODE` int(11) default '0',
  `INSURANCE_DESC` varchar(100) default '',
  `INSURANCE_CODE` int(11) default '0',
  `LICENSE_DESC` varchar(100) default '',
  `LICENSE_CODE` int(11) default '0',
  `PACKING_DESC` varchar(100) default '',
  `PACKING_CODE` int(11) default '0',
  `FORWARDING_DESC` varchar(100) default '',
  `FORWARDING_CODE` int(11) default '0',
  `EXCISE` varchar(100) default '',
  `EXCISE_CODE` int(11) default '0',
  `OCTROI` varchar(100) default '',
  `OCTROI_CODE` int(11) default '0',
  `FREIGHT` varchar(100) default '',
  `FREIGHT_CODE` int(11) default '0',
  `TCC` varchar(100) default '',
  `TCC_CODE` int(11) default '0',
  `SERVICETAX_DESC` varchar(100) default '',
  `SERVICETAX_CODE` int(11) default '0',
  `ST_DESC` varchar(100) default '',
  `ST_CODE` int(11) default '0',
  `ESI_DESC` varchar(100) default '',
  `ESI_CODE` int(11) default '0',
  `FOR_DESC` varchar(100) default '',
  `FOR_CODE` int(11) default '0',
  `FOB_DESC` varchar(100) default '',
  `FOB_CODE` int(11) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`QUOT_ID`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_RATE_APPROVAL_DETAIL'
# 
CREATE TABLE `D_PUR_RATE_APPROVAL_DETAIL` (
  `COMPANY_ID` int(11) NOT NULL default '0',
  `APPROVAL_NO` varchar(10) NOT NULL default '',
  `SR_NO` int(11) NOT NULL default '0',
  `ITEM_ID` varchar(10) NOT NULL default '',
  `SUPP_ID` varchar(10) NOT NULL default '',
  `QUOT_ID` varchar(10) default '',
  `QUOT_SR_NO` int(11) default '0',
  `LAST_PO_NO` varchar(10) default '',
  `LAST_PO_DATE` date default '0000-00-00',
  `LAST_PO_RATE` double default '0',
  `LAST_PO_QTY` double default '0',
  `CURRENT_RATE` double default '0',
  `CURRENT_QTY` double default '0',
  `RATE_DIFFERENCE` double default '0',
  `RATE_DIFFERENCE_PER` double default '0',
  `REMARKS` varchar(100) default '',
  `CREATED_BY` int(11) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` int(11) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  PRIMARY KEY  (`COMPANY_ID`,`APPROVAL_NO`,`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'D_PUR_RATE_APPROVAL_HEADER'
# 
CREATE TABLE `D_PUR_RATE_APPROVAL_HEADER` (
  `COMPANY_ID` int(11) NOT NULL default '0',
  `APPROVAL_NO` varchar(10) NOT NULL default '',
  `APPROVAL_DATE` date default '0000-00-00',
  `INQUIRY_NO` varchar(10) default '',
  `INQUIRY_DATE` date default '0000-00-00',
  `REQ_NO` varchar(10) default '',
  `REQ_DATE` date default '0000-00-00',
  `REMARKS` varchar(100) default '',
  `APPROVED` tinyint(1) default '0',
  `APPROVED_DATE` date default '0000-00-00',
  `REJECTED` tinyint(1) default '0',
  `REJECTED_DATE` date default '0000-00-00',
  `HIERARCHY_ID` int(11) default '0',
  `CREATED_BY` int(11) default '0',
  `CREATED_DATE` date default '0000-00-00',
  `MODIFIED_BY` int(11) default '0',
  `MODIFIED_DATE` date default '0000-00-00',
  `DEPT_ID` int(11) default '0',
  `USER_ID` int(11) default '0',
  PRIMARY KEY  (`COMPANY_ID`,`APPROVAL_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'IMAGE'
# 
CREATE TABLE `IMAGE` (
  `SR_NO` int(11) NOT NULL default '0',
  `IMAGE` blob,
  PRIMARY KEY  (`SR_NO`)
) TYPE=MyISAM; 

# Host: 172.16.49.221
# Database: Eitlerp
# Table: 'TEAM'
# 
CREATE TABLE `TEAM` (
  `Name` varchar(100) default '',
  `temp1` bigint(10) default NULL,
  `THE_DATE` date default '0000-00-00'
) TYPE=MyISAM; 

