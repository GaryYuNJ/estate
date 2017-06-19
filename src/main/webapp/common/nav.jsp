<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Main content starts -->
<div class="content">
	<!-- Sidebar -->
	<div class="sidebar">
		<div class="sidebar-dropdown">
			<a href="#">导航</a>
		</div>
		<!--- Sidebar navigation -->
		<!-- If the main navigation has sub navigation, then add the class "has_sub" to "li" of main navigation. -->
		<ul id="nav">
			<!-- Main menu with font awesome icon 
					<li>
						<a href="<c:url value="/manage/resourceManagePage" />"><i class="icon-home"></i> 首页</a>
					</li>
					-->
			<li><a href="<c:url value="/organization/page" />"><i
					class="icon-list-alt"></i> 组织架构 </a></li>
			<li><a href="<c:url value="/buser/page" />"><i
					class="icon-list-alt"></i> 用户管理 </a></li>
			<li><a href="<c:url value="/fwpages/bfwlist" />"><i
					class="icon-list-alt"></i> 审批列表 </a></li>
			<li><a href="<c:url value="/fwpages/newformtable" />"><i
					class="icon-list-alt"></i> 新增审批 </a></li>
	</div>
	<!-- Sidebar ends -->