<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th>审批人</th>
			<th>备注</th>
			<th>日期</th>
		</tr>
	</thead>
	<tbody>
		<tr v-for="item in commentsList">
			<td>{{item.userId}}</td>
			<td>{{item.message}}</td>
			<td>{{item.time | time}}</td>
		</tr>
	</tbody>
</table>

<div class="widget-foot">
	<div class="pagination pull-right">状态：{{State}}</div>
	<!-- <ul class="pagination pull-right">
											<li><a href="#">Prev</a></li>
											<li><a href="#">1</a></li>
											<li><a href="#">2</a></li>
											<li><a href="#">3</a></li>
											<li><a href="#">4</a></li>
											<li><a href="#">Next</a></li>
										</ul> -->
	<div class="clearfix"></div>
</div>