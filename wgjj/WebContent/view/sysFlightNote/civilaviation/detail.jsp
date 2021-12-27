<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>民航信息详情</title>
<%@ include file="../../tool.jsp"%>
<script type="text/javascript">
	function previewImg(url){
		if(url!=""&&url!=undefined){
			$("#previewImg").attr("src",url);
		}
		$('#myModal').modal('toggle');
	}

</script>
</head>
<body>
	<div class="container" style="margin:15px 35px;width: 95%">
		<table class="table table-bordered">
			<caption class="baseHead">基本信息详情</caption>
			<tbody>
				<tr>
					<td class="tipLabel">来电来函单位：</td>
					<td class="tipVal"><input type="text" disabled value="${civilAviationVO.planInfo.letterUnit}"  placeholder="来电来函单位"></td>
					<td class="tipLabel">姓  名：</td>
					<td class="tipVal"><input type="text" disabled value="${civilAviationVO.planInfo.personName}"  placeholder="姓  名"></td>
					<td class="tipLabel">电  话：</td>
					<td class="tipVal"><input type="text" disabled value="${civilAviationVO.planInfo.telNo}"  placeholder="电  话"></td>
				</tr>
				<tr>
					<td class="tipLabel">国家/照会号：</td>
					<td class="tipVal"><input type="text" disabled value="${civilAviationVO.planInfo.nationality}" placeholder="国家/照会号"></td>
					<td class="tipLabel">机  型：</td>
					<td class="tipVal"><input type="text" disabled value="${civilAviationVO.planInfo.model}" placeholder="机  型"></td>
					<td class="tipLabel">架  数：</td>
					<td class="tipVal"><input type="text" disabled value="${civilAviationVO.planInfo.airNumber}" placeholder="架  数"></td>
				</tr>
				<tr>
					<td class="tipLabel">飞行日期：</td>
					<td class="tipVal"><input type="text" disabled value="${civilAviationVO.planInfo.flightTime}" placeholder="飞行日期"></td>
					<td class="tipLabel" colspan="4"></td>
				</tr>
			</tbody>
		</table>

		<table class="table table-bordered mt15">
			<caption class="baseHead">照会文件列表</caption>
			<thead>
			<tr>
				<th>文件名</th>
				<th>上传时间</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:choose>
				<c:when test="${empty civilAviationVO.noteFiles}">
					<tr>
						<td colspan="3" style="text-align: center">暂无照会文书</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="noteFile" items="${civilAviationVO.noteFiles }">
					<tr>
						<td>${noteFile.fileNameCn} | ${noteFile.fileNameEn}
						</td>
						<td>${noteFile.createTime}</td>
						<td >
							<button type="button"  onclick="previewImg('${ctx}/detail_online_preview.action?id=${noteFile.id}')">预览</button>
						</td>
					</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			</tbody>
		</table>

		<table class="table table-bordered mt15">
			<caption class="baseHead">飞机有关飞行详情</caption>
			<tbody>
			<tr>
				<td colspan="4">飞机有关信息：</td>
			</tr>
			<tr>
				<td class="flyLabel">机型（属性）：</td>
				<td class="flyVal"><input type="text" disabled value="${civilAviationVO.planInfo.model}" class="wpc95" placeholder="机型（属性）"></td>
				<td class="flyLabel">架  数：</td>
				<td class="flyVal"><input type="text" disabled value="${civilAviationVO.planInfo.airNumber}" class="wpc95" placeholder="架  数"></td>
			</tr>
			<tr>
				<td class="flyLabel">国  家：</td>
				<td class="flyVal"><input type="text" disabled value="${civilAviationVO.planInfo.nationality}" class="wpc95" placeholder="国  家"></td>
				<td class="flyLabel">运营方：</td>
				<td class="flyVal"><input type="text" disabled value="${civilAviationVO.planInfo.operator}" class="wpc95" placeholder="运营方"></td>
			</tr>
			<tr>
				<td class="flyLabel">呼  号：</td>
				<td class="flyVal"><textarea rows="2" disabled value="${civilAviationVO.planInfo.callNumber}"  class="wpc95"></textarea> </td>
				<td class="flyLabel">注册号：</td>
				<td class="flyVal"><textarea rows="2" disabled value="${civilAviationVO.planInfo.regNo}" class="wpc95"> </textarea> </td>
			</tr>
			<tr>
				<td class="flyLabel">任务目的：</td>
				<td class="flyVal" colspan="3"><input disabled value="${civilAviationVO.planInfo.mission}"  type="text"  class="wpc98"/> </td>
			</tr>
			<tr>
				<td class="flyLabel">其  他：</td>
				<td class="flyVal" colspan="3"><textarea disabled type="text" class="wpc98" >${civilAviationVO.planInfo.other}
				</textarea> </td>
			</tr>
			<tr>
				<td colspan="4">飞行计划信息：</td>
			</tr>
			<c:choose>
				<c:when test="${empty civilAviationVO.planFlights}">
					<tr>
						<td colspan="4" style="text-align: center">暂无飞行计划</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="planFlight" items="${civilAviationVO.planFlights }">
						<tr>
							<td class="flyLabel">计划日期（UTC时间）：</td>
							<td class="flyVal"><input type="text" disabled value="${planFlight.flightTime}" class="wpc95" placeholder="计划日期（UTC时间）"></td>
							<td class="flyLabel">起飞机场：</td>
							<td class="flyVal"><input type="text" disabled value="${planFlight.departureAirport}" class="wpc95" placeholder="起飞机场"></td>
						</tr>
						<tr>
							<td class="flyLabel">降落机场：</td>
							<td class="flyVal"><input type="text" disabled value="${planFlight.landAirport}" class="wpc95" placeholder="降落机场"></td>
							<td class="flyLabel">入境点名称：</td>
							<td class="flyVal"><input type="text" disabled value="${planFlight.entryName}" class="wpc95" placeholder="入境点名称"></td>
						</tr>
						<tr>
							<td class="flyLabel">出境点名称：</td>
							<td class="flyVal" colspan="3"><input disabled value="${planFlight.exitName}" type="text" class="wpc98" placeholder="出境点名称"></td>
						</tr>
						<tr>
							<td class="flyLabel">计划航线：</td>
							<td class="flyVal" colspan="3"><textarea disabled type="text" class="wpc98" >${planFlight.planRoute}
							</textarea> </td>
						</tr>
						<tr>
							<td class="flyLabel">备用航线：</td>
							<td class="flyVal" colspan="3"><textarea disabled type="text" class="wpc98" >${planFlight.alternateRoute}
							</textarea> </td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>

			</tbody>
		</table>
		<table class="table table-bordered mt15">
			<caption class="baseHead">民航回复信息</caption>
			<tbody>
			<form id="replyForm">
				<input type="hidden" name="noteId"  value="${civilAviationVO.planInfo.noteId }">
				<tr>
					<td class="flyLabel">民航许可号：</td>
					<td class="flyVal"><input type="text" disabled name="permitNumber" value="${civilAviationVO.noteCivilReply.permitNumber}"  class="wpc95 required" style="width: 90%" dataType="Require,Limit" len="100" msg="请输入(1~100)个字符的民航许可号！" maxlength="100" placeholder="民航许可号"></td>
					<td class="flyLabel">计划日期（UTC时间）：</td>
					<td class="flyVal"><input type="text" disabled name="planTime" value="${civilAviationVO.noteCivilReply.planTime}" class="wpc95 Wdate"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" placeholder="计划日期（UTC时间）"></td>
				</tr>
				<tr>
					<td class="flyLabel">降落机场：</td>
					<td class="flyVal"><input type="text" disabled name="downAirport" value="${civilAviationVO.noteCivilReply.downAirport}" class="wpc95" placeholder="降落机场"></td>
					<td class="flyLabel">起飞机场：</td>
					<td class="flyVal"><input type="text" disabled name="upAirport"  value="${civilAviationVO.noteCivilReply.upAirport}" class="wpc95" placeholder="起飞机场"></td>
				</tr>
				<tr>
					<td class="flyLabel">计划航线：</td>
					<td class="flyVal" colspan="3"><textarea  disabled name="planRoute" type="text" class="wpc98" >${civilAviationVO.noteCivilReply.planRoute}</textarea> </td>
				</tr>
				<tr>
					<td class="flyLabel">备用航线：</td>
					<td class="flyVal" colspan="3"><textarea  disabled name="bakRoute" type="text" class="wpc98" >${civilAviationVO.noteCivilReply.bakRoute}</textarea></td>
				</tr>
				<tr>
					<td class="flyLabel">添加附件：</td>
					<td class="flyVal">
						<input type="text" name="fileUrl"  disabled value="${civilAviationVO.noteCivilReply.fileUrl}" class="wpc95" placeholder="附件地址">
						<input type="hidden" name="fileName" value="${civilAviationVO.noteCivilReply.fileName}" class="wpc95" placeholder="附件地址">
					</td>
					<td class="flyVal" colspan="2" style="text-align:left!important;">
						<input type="file" hidden value="浏览" name="file" />
						<input class="btn" type="button" value="浏览" onclick="upload()">
						<c:if test="${not empty civilAviationVO.noteCivilReply.fileUrl}">
							<a class="btn" href="${ctx}/preview.action?path=${civilAviationVO.noteCivilReply.fileUrl}" target="_blank">下载</a>
						</c:if>
					</td>
				</tr>
			</form>
			</tbody>
		</table>
		<div style="text-align: center"><button class="btn" type="button" onclick="window.history.back()">返回</button></div>


		<!-- Modal -->
		<div id="myModal" class="modal hide fade" style="width:62%" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h6 id="myModalLabel">照会文件预览</h6>
			</div>
			<div class="modal-body">
				<img src="${ctx}/images/hkfwz_login.png" id="previewImg" class="img-responsive" alt="Cinque Terre" width="100%">
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			</div>
		</div>

	</div>


</body>
<style>
	.baseHead{
		text-align: left;
		font-size: 16px;
		margin-bottom: 10px;
		padding-left: 10px;
		background: #efefef;
		line-height: 45px;
		border-radius: 5px;
		font-weight: bold;
	}
	.mt15{
		margin-top: 15px;
	}
	.wpc100{
		width: 100%;
	}
	.wpc95{
		width: 95%;
	}
	.wpc98{
		width: 98%;
	}
	.tipLabel{
		padding: 0px;
		text-align: right!important;;
		width: 10%;
	}
	.tipVal{
		width: 23.3%;
		text-align: center!important;
	}
	.tipVal input{
		margin-bottom: 0px;
	}

	.flyLabel{
		padding: 0px;
		text-align: right!important;;
		width: 15%;
	}
	.flyVal{
		width: 35%;
		text-align: center!important;
		margin-right: 15px;
	}
	.flyVal input{
		margin-bottom: 0px;
	}
	.flyVal area{
		margin-bottom: 0px;
	}
</style>
</html>
