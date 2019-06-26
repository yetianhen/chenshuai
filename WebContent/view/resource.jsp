<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    ResourceBundle res = ResourceBundle.getBundle("urls"); 
%>
<!-- 公共资源CSS,JS  -->
<!--Css -->
<link rel="stylesheet" type="text/css" href="<%=res.getString("easyuiUrl")%>/themes/bootstrap/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=res.getString("easyuiUrl")%>/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=res.getString("msUrl")%>/css/base.css">
<!-- ** Javascript ** -->
<script type="text/javascript" src="<%=res.getString("msUrl")%>/js/commons/jquery.min.js"></script>
<script type="text/javascript" src="<%=res.getString("msUrl")%>/js/commons/jquery.form.js"></script>
<script type="text/javascript" src="<%=res.getString("msUrl")%>/js/commons/package.js"></script>
<script type="text/javascript" src="<%=res.getString("easyuiUrl")%>/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=res.getString("msUrl")%>/js/commons/urls.js?v=11"></script>
<script type="text/javascript" src="<%=res.getString("msUrl")%>/js/commons/base.js?v=11"></script>