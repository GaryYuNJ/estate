<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Main content starts -->
<div id="main" class="content">
	<!-- Sidebar -->
	<div class="sidebar">
		<div class="sidebar-dropdown">
			<a href="#">导航</a>
		</div>
		<!--- Sidebar navigation -->
		<!-- If the main navigation has sub navigation, then add the class "has_sub" to "li" of main navigation. -->
		<ul id="nav">
			<!-- Main menu with font awesome icon -->
			<li><a href="<c:url value="ffwlist?user={{user}}" />"><i
					class="icon-list-alt"></i> 新建审批 </a></li>
			<li><a href="<c:url value="tohandlelist?user={{user}}" />"><i
					class="icon-list-alt"></i> 等待我审批 </a></li>
			<li><a href="<c:url value="myapplylist?user={{user}}" />"><i
					class="icon-list-alt"></i> 我申请的 </a></li>
			<li><a href="<c:url value="handlebymehis?user={{user}}" />"><i
					class="icon-list-alt"></i> 我审批的 </a></li>
			<li><a href="<c:url value="frontlogin" />"><i
					class="icon-list-alt"></i> 登陆页 </a></li>
		</ul>
	</div>
	<!-- Sidebar ends -->