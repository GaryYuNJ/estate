<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Page heading -->
<div class="page-head">
	<h2 class="pull-left">
		<i class="icon-list-alt"></i> ${breadcrumbPage}
	</h2>
	<!-- Breadcrumb -->
	<div class="bread-crumb pull-right">
		<a href="#"><i class="icon-list-alt"></i> ${breadcrumbModul}</a>
		<!-- Divider -->
		<span class="divider">/</span> <a href="#" class="bread-current">${breadcrumbPage}</a>
	</div>
	<div class="clearfix"></div>
</div>
<!-- Page heading ends -->