//获取主机地址之后的目录如：/Tmall/index.jsp
var pathName=window.document.location.pathname;
//获取带"/"的项目名，如：/Tmall
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
var ws = null;//WebSocket
var wsReconnect=true;
var wsConnect=false;
var isOpenReplay = false;
var openLayerIndex = "";
var uavCache = new Array();//放置无人机最后航迹对应的Mark，数组内对象{'pn':编号, 'lastMark':最后航迹, 'hisMarks':所有航迹（不包含最后航迹）}
var wsRcvMessageReturn = true;//wsRcvMessage方法返回
var isReplay = false;//是否重放
var isStopReplay=false;//是否点击结束重放按钮
var noDealUavDownArry = [];// 未处理的降落的无人机消息

//保持websocket处于长时间通信状态
function keepAlive(){
	sendMessageToWebSocket("");
	console.info("保持websocket处于长时间通信状态");
	setTimeout("keepAlive()", 30000);
}

// 判断字符串是否是JSON
function isJsonString(str) {
    try {
        if (typeof JSON.parse(str) == "object") {
            return true;
        }
    } catch(e) {
    }
    return false;
}

//打开WebSocket连接
function createWebSocket() {
	if ("WebSocket" in window) {
		ws = new WebSocket((location.protocol === 'https:' ? "wss://" : "ws://")+window.location.host+projectName+"/uavOnlineWebSocket");
		ws.onopen = function() {
			console.info("WebSocket连接成功");
			wsConnect=true;
		};
		ws.onmessage = function(event) {
			if(event.data && event.data!=''){
				var received_msg = isJsonString(event.data) ? JSON.parse(event.data) : event.data;
				if(isReplay){//重放
					//console.info("=========重放中=====");
					if(received_msg == 'recurReady'){//准备数据完成
						setTimeout(function(){
							$('#loading').modal('hide');//隐藏等待框
						},1000);
						$("#replayTime").show();//展示回放时间的div
						alert("无人机数据已准备完毕，即将开始重放模式");
					}else if(received_msg == 'recurFinish'){//重放完成
						$("#startReplay").show();
						$("#pauseReplay").hide();
						$("#continueReplay").hide();
						$("#endReplay").hide();
						$("#cancelReplay").show();
						layer.confirm("重放已完成，是否再次重放？", {
							closeBtn: 0,
							btn: ["再次重放","取消重放"] //按钮
						}, function(index){
							layer.close(index);
							reReplay();
						}, function(index){
							layer.close(index);
							endReplay();
							cancelReplay();
							//console.info("重放完成，取消重放");
						});
					}else if(received_msg == 'recurPauseToStop'){//由于暂停时间过长被迫停止重放
						layer.confirm("由于暂停时间过长被迫停止重放，是否再次重放？", {
							closeBtn: 0,
							btn: ["再次重放","取消重放"] //按钮
						}, function(index){
							layer.close(index);
							reReplay();
						}, function(index){
							layer.close(index);
							endReplay();
						});
					}else if(received_msg == 'recurStop'){//停止重放
						console.info("停止重放");
						cancelReplay();
					}else{
						if(!isStopReplay){
							received_msg = JSON.parse(event.data);
							if(wsRcvMessageReturn){
								wsRcvMessageReturn = false;
								setTimeout(function(){
									if(received_msg.uav){
										wsRcvMessageReturn = wsRcvMessage(received_msg.uav);
									}
								},0);
								if(received_msg.warn){
									//console.info("告警数据");
									wsRcvWarnMessage(received_msg.warn);
								}
								if(received_msg.time){
									$("#replayTimeStr").html(received_msg.time);
								}
							}
							if(received_msg.adsb){
								//获得推送的adsb飞机
								updateAdsbPlane(received_msg.adsb);
							}
						}
					}
				}else{//未重放
					setTimeout(function(){
						if(received_msg=='plan'){
							//获取计划信息
							getReqPlanList();
						}
						if(received_msg == 'space'){
							//获取空域信息
							getInfoSpaceList();
						}
						if(received_msg == 'bizEvent'){
							//获取有事件的无人机PN
							getEvenPnList();
						}
						if(received_msg == 'runSpace'){
							//获取空域边界和适飞空域
							getRunSpaceList();
						}
						if(received_msg.adsb){
							//获得推送的adsb飞机
							updateAdsbPlane(received_msg.adsb);
						}
						if(received_msg.gudingshi){
							// 获得的推送的固定式状态
							modifyGudingshiList(received_msg.gudingshi);
						}
						if(received_msg.warn){
							//console.info("接收到告警信息！");
							wsRcvWarnMessage(received_msg.warn);
						}
						if(received_msg.uav){
							//console.info("接收到无人机航迹信息！");
							//console.info("接收：无人机数量=="+(received_msg.uav ? received_msg.uav.length : 0) +",告警数量=="+(received_msg.warn ? received_msg.warn.length : 0) + ",处理结果=="+wsRcvMessageReturn);
							if(wsRcvMessageReturn){
								wsRcvMessageReturn = false;
								var begTime = new Date().getTime();
								if(noDealUavDownArry.length>0){
									var tempNoDealUavDownArry = [].concat(noDealUavDownArry);
									noDealUavDownArry = [];
									//将降落无人机的消息放到本次处理的方法中
									for(var i=0;i<tempNoDealUavDownArry.length;i++){
										received_msg.uav.push(tempNoDealUavDownArry[i]);
									}
									console.info("需要本次处理的抛弃的降落无人机的数量：" + tempNoDealUavDownArry.length);
									tempNoDealUavDownArry = [];
								}
								wsRcvMessageReturn = wsRcvMessage(received_msg.uav);
								var endTime = new Date().getTime();
								//console.info("耗时：" + (endTime - begTime) + "毫秒");
							}else{
								//console.info("抛弃：received_msg.uav内容==" + JSON.stringify(received_msg.uav));
								for(var i=0;i<received_msg.uav.length;i++){
									//将抛弃的数据中降落无人机的消息提取出来
									if(received_msg.uav[i].tol=='4'){
										noDealUavDownArry.push(received_msg.uav[i]);
										console.info("待处理的抛弃的降落无人机：" + JSON.stringify(noDealUavDownArry));
									}
								}
							}
						}
					},0);
				}
			}
		};
		ws.onclose = function() {
			console.info("WebSocket连接关闭");
			wsConnect=false;
			if(!wsReconnect)
				return;
			
			layer.confirm("网络异常，无法连接到服务器，是否重试？", {
				closeBtn: 0,
				btn: ["重试","取消"] //按钮
			}, function(index){
				layer.close(index);
				createWebSocket();	
			}, function(index){
				layer.close(index);
				closeWebSocket();
			});
		};
		//连接发生错误的回调方法
		ws.onerror = function () {
			
		};
	} else {
		// 浏览器不支持 WebSocket
		alert("您的浏览器不支持 WebSocket!");
	}
}
//关闭WebSocket连接
function closeWebSocket() {
	if(ws!=null) ws.close();
}

//向websocket发送消息
function sendMessageToWebSocket(message){
	if(ws!=null && ws.readyState == WebSocket.OPEN){
		ws.send(message);
	}
}

//主动重连websocket
var reConnectNum = 1;
//此方法会出现等连接可以连的时候，会连上很多个
function reConnect() {
    if(reConnectNum == 13) { //函数结束条件
        clearTimeout(t); 

		layer.confirm("网络异常，无法连接到服务器，是否重试？", {
			closeBtn: 0,
			btn: ["重试","取消"] //按钮
		}, function(index){
			layer.close(index);
			reConnectNum = 1;
			createWebSocket();	
		}, function(index){
			layer.close(index);
	        reConnectNum = 1;
			closeWebSocket();
		});
		
        return;
    }
    
    console.info("主动尝试重连：第" + reConnectNum + "次");
    createWebSocket();	
    
	reConnectNum++;          
    
	t = setTimeout("reConnect()", 5000); //单位是毫秒 
}

//将数组转成“,”拼接的字符串
function arrayToStr(str){
	var _str = str.join(',');
	return _str;
}

//将“,”拼接的字符串转成数组
function strToArray(str){
	var _arr = str.split(',');
	return _arr;
}

//打开重放设置页面
function openReplay(obj){
	obj.blur();//使重放按钮失去焦点
	if(isOpenReplay){
		//打开状态的话就隐藏
		if(openLayerIndex){
			layer.close(openLayerIndex);
			isOpenReplay = false;
			openLayerIndex = "";
		}
	}else{
		//隐藏状态的话如果不是回放状态就重置内容
		if(!isReplay){
			Validator.Validate_onLoad($("#replayForm")[0],3);
			$("#startReplay").show();
			$("#cancelReplay").show();
		}
		layer.open({
			skin: 'demo-class',
			type: 1,
			title:'重放设置',
			area: ['300px', '210px'],
			//offset: ['200px', '700px'],//上，左
			content: $("#replayDiv"),
			shade: 0,     //不显示遮罩
			closeBtn:0,//不显示关闭按钮
			zIndex:100,
			success: function(layero, index){//当前层DOM当前层索引
				isOpenReplay = true;
				openLayerIndex = index;
			},
			end:function(){
				isOpenReplay = false;
			}
		});
	}
}

//清空地图上所有覆盖物，还原页面
function restorePage(){
	closePanel();
	$("#replayTime").hide();
	$("#replayTimeStr").html("");
//	mapIframe.clearOverlay();
	//清空无人机与有人机
	var uavOverlays=[];
	uavCache.forEach(item=>{
		uavOverlays.push(item.lastMark);
	});
	mapIframe.clearPointOverlays(uavOverlays);
	for(var key in adsbVm.adsbMap){
		mapIframe.clearAdsbMarker(adsbVm.adsbMap[key].adsbMarker);
	}

	uavCache = new Array();//还原无人机缓存
	drawSpaceList = [];//还原空域缓存
	planSpaceArray = new Array();//还原计划空域的信息
	//清空告警表格
	$("#warnNum").text("0");
	$("#warnListCanScrollDiv").empty();
	//清空无人机表格
	$("#uavNum").text("0");
	$("#uavListCanScrollDiv").empty();
	//清空有人机表格
	$("#adsbPlaneNum").text("0");
	adsbVm.adsbMap={};
	
	$("#startReplay").hide();
	$("#pauseReplay").hide();
	$("#continueReplay").hide();
	$("#endReplay").hide();
	$("#cancelReplay").hide();
	$("input[name='spaceTypes']:checked").each(function(index,item){
		checkedSpaceType(item);
	});
	$("input[name='reqPlanSpaces']").each(function(index,item){
		$(item).removeAttr("checked");
	});
	//去掉无人机小窗口
	closeAllUavMap();
}

//开始重放
function startReplay(){
	var myForm = document.getElementById("replayForm");
	if (Validator.Validate(myForm, 3)) {
		var ajaxData = {};
		ajaxData.begDate = $("#replayBegDate").val();
		ajaxData.endDate = $("#replayEndDate").val();
		ajaxData.speed=$("#replaySpeed").val();
		//alert("调用开始重放的方法");
		sendMessageToWebSocket("recurStart_" + ajaxData.begDate + "_" + ajaxData.endDate+ "_" + ajaxData.speed);//recurStart_begtime_endtime_speed
		$('#loading').modal('show');//弹出等待框
		isReplay = true;
		restorePage();//还原页面
		$("#startReplay").hide();
		$("#pauseReplay").show();
		$("#endReplay").show();
		$("#cancelReplay").hide();
		$("#replayBegDate").attr("disabled","disabled");
		$("#replayEndDate").attr("disabled","disabled");
		$("#replaySpeed").attr("disabled","disabled");
		if(isOpenReplay){
			//打开状态的话就隐藏
			if(openLayerIndex){
				layer.close(openLayerIndex);
				isOpenReplay = false;
				openLayerIndex = "";
			}
		}
	}
}

//再次重放
function reReplay(){
	$('#loading').modal('show');//弹出等待框
	sendMessageToWebSocket("recurReplay");
	restorePage();//还原页面
	$("#startReplay").hide();
	$("#pauseReplay").show();
	$("#endReplay").show();
	$("#cancelReplay").hide();
	isReplay = true;
	pauseAllAudio();
}

//继续重放
function continueReplay(){
	$("#pauseReplay").show();
	$("#continueReplay").hide();
	sendMessageToWebSocket("recurContinue");
}

//暂停重放
function pauseReplay(){
	$("#pauseReplay").hide();
	$("#continueReplay").show();
	sendMessageToWebSocket("recurPause");
}

//结束重放
function endReplay(){
	$('#loading1').modal('show');
	isStopReplay = true;
	sendMessageToWebSocket("recurStop");
	pauseAllAudio();
	setTimeout(function(){
		$('#loading1').modal('hide');
	},2000);
}

//取消重放
function cancelReplay(){
	isReplay = false;
	isStopReplay = false;
	if(openLayerIndex){
		layer.close(openLayerIndex);
	}
	$("#replayBegDate").val("");
	$("#replayEndDate").val("");
	$("#replaySpeed").val("1");
	$('#loading').modal('hide');//隐藏等待框
	restorePage();//还原页面
	$("#replayBegDate").removeAttr("disabled");
	$("#replayEndDate").removeAttr("disabled");
	$("#replaySpeed").removeAttr("disabled");
	pauseAllAudio();
}

//点击取消重放按钮
function buttonCancelReplay(){
	if(openLayerIndex){
		layer.close(openLayerIndex);
	}
	$("#replayBegDate").val("");
	$("#replayEndDate").val("");
	$("#replaySpeed").val("1");
	$("#replayBegDate").removeAttr("disabled");
	$("#replayEndDate").removeAttr("disabled");
	$("#replaySpeed").removeAttr("disabled");
	pauseAllAudio();
}
//保存告警处理结果
function saveDeal(){
	var myForm = document.getElementById("dealForm");
	if (Validator.Validate(myForm, 3)) {
		var ajaxData = {};
		ajaxData.warnSeq = $("#dealWarnSeq").val();
		ajaxData.dealDesc = $("#dealDesc").val();
		$.ajax({
			url:ctx+"/uavOnlineSaveWarnDeal.action",
			type:"post",
			data:ajaxData,
			dataType:"json",
			async:false,
			success:function(data){
				alert(data.errMsg);
				if(data.errCode=="0"){   //失败
				}else{ //成功
					//触发下已知的方法
					//$("#warnTr_"+$("#dealWarnSeq").val()).find("a").eq(0).trigger("click");
					warn_knownWarn($("#makeEventWarnSeq").val(),$("#unKnownButton_"+$("#makeEventWarnSeq").val())[0]);
					cancelDeal();//处理成功后调用一次取消隐藏处理页面清空处理结果
				}
			}
		});
	}
}

//取消告警处理
function cancelDeal(){
	clearDealForm();
	$("#dealDiv").modal("hide");//隐藏处理页面
	removeWarnDealFromTable();
}

//清空告警处理表单数据
function clearDealForm(){
	$("#dealWarnSeq").val("");//清空处理页面内容
	$("#dealDesc").val("");
	Validator.Validate_onLoad($("#dealForm")[0],3);
}

//保存告警生成事件结果
function saveMakeEvent(){
	var myForm = document.getElementById("makeEventForm");
	var validateResult = true;
	var ajaxData = {};
	ajaxData.warnSeq = $("#makeEventWarnSeq").val();
	var makeEventMode = $("#makeEventMode").val();
	if(makeEventMode=="0"){
		if (Validator.Validate(myForm, 3)) {
			//生成新的事件
			ajaxData.makeEventMode = "0";
			ajaxData.eventDesc = $("#eventDesc").val();
		}else{
			validateResult = false;
		}
	}else{
		//选择已有事件合并
		ajaxData.makeEventMode = "1";
		ajaxData.eventId = $("input[name='eventId']").val();
	}
	if(validateResult){
		$.ajax({
			url:ctx+"/uavOnlineSaveEvent.action",
			type:"post",
			data:ajaxData,
			dataType:"json",
			async:false,
			success:function(data){
				alert(data.errMsg);
				if(data.errCode=="0"){   //失败
				}else{ //成功
					//触发下已知的方法
					//$("#warnTr_"+$("#makeEventWarnSeq").val()).find("a").eq(0).trigger("click");
					warn_knownWarn($("#makeEventWarnSeq").val(),$("#unKnownButton_"+$("#makeEventWarnSeq").val())[0]);
					
					cancelMakeEvent();//生成事件成功后调用一次取消隐藏生成事件页面清空生成事件结果
					modifyWarnInPanel(data.data.warn);
					
					//添加有事件信息的无人机pn，用于修改无人机图标
					addEventPn(data.data.warn.pn);
				}
			}
		});
	}
}

//取消告警生成事件
function cancelMakeEvent(){
	clearMakeEventForm();
	$("#makeEventDiv").modal("hide");//隐藏生成事件页面
	removeEventFromTable();
}

//清空告警生成事件表单数据
function clearMakeEventForm(){
	$("#makeEventWarnSeq").val("");//清空生成事件页面内容
	$("#makeEventMode").val("0");
	$("#makeEventMode").trigger("change");
	$("#eventDesc").val("");
	Validator.Validate_onLoad($("#makeEventForm")[0],3);
}

//初始化告警相关
function warnInit(){
	//设置已知A标签样式
	//页面初始化时获取cookie中的已知告警id，设置tr的样式
	var warnSeq = $.cookie("warnSeq");
	if(warnSeq){
		var warnSeqs = strToArray(warnSeq);
		for(var i=0;i<warnSeqs.length;i++){
			var trId = warnSeqs[i];
			
			var warnSeqStr = trId.split("_")[1];
			$("#"+trId).attr("src", ctx + "/view/uavOnline/image/warn_remove.png");
			$("#knownButton_" + warnSeqStr).show();
			$("#unKnownButton_" + warnSeqStr).hide();
			$("#warnSeqDiv_"+warnSeqStr).removeClass("dangerDiv");
		}
	}

	//在调用 show 方法后触发。
	$('#dealDiv').on('show.bs.modal', function () {
		$.ajax({
			url:ctx+"/uavOnlineGetWarnDealList.action",
			type:"post",
			data:{warnSeq:$("#dealWarnSeq").val()},
			dataType:"json",
			async:false,
			success:function(data){
				if(data.errCode=="0"){   //失败
					alert("获取告警处理详情失败");
				}else{ //成功
					removeWarnDealFromTable();
					addWarnDealToTable(data.data.warnDealList);
				}
			}
		});
	});

	//在调用 show 方法后触发。
	$('#makeEventDiv').on('show.bs.modal', function () {
		$.ajax({
			url:ctx+"/uavOnlineGetEventList.action",
			type:"post",
			data:{},
			dataType:"json",
			async:false,
			success:function(data){
				if(data.errCode=="0"){   //失败
					alert("获取事件详情失败");
				}else{ //成功
					removeEventFromTable();
					addEventToTable(data.data.eventList);
				}
			}
		});
	});
	//给生成事件方式绑定change事件
	$("#makeEventMode").change(function(){
		var makeEventMode = $(this).val();
		if(makeEventMode == "1"){
			$(".eventDiv0").hide();
			$("#eventDiv1").show();
		}else{
			$("#eventDiv1").hide();
			$(".eventDiv0").show();
		}
	});
	
	//给选择事件详情列表每一行tr绑定click事件
	$("body").on("click",".bizEventTr",function(){
		$(this).find("input[type='radio']")[0].checked = true;
	});
}

//点击a.deal事件
function clickDealA(id){
	$("#dealWarnSeq").val(id);//给处理页面赋值
	$("#dealDiv").modal("show");//打开处理页面
}

//点击a.makeEvent事件
function clickMakeEventA(id){
	$("#makeEventWarnSeq").val(id);//给生成事件页面赋值
	$("#makeEventDiv").modal("show");//打开处理页面
}

//向告警处理详情的table添加内容
function addWarnDealToTable(warnDealList){
	if(warnDealList.length>0){
		$("#warnDealDiv").show();
	}
	for(var i=0;i<warnDealList.length;i++){
		var trDom = "<tr>";
			trDom+="<td>" +warnDealList[i].dealTime+"</td>";
			trDom+="<td>" +warnDealList[i].dealDeptName+"</td>";
			trDom+="<td>" +warnDealList[i].dealUserDesc+"</td>";
			trDom+="<td>" +warnDealList[i].dealDesc+"</td>";
			trDom+="</tr>";
		$("#warnDealTable").append(trDom);
	}
}
//移除告警处理详情内容
function removeWarnDealFromTable(){
	$("#warnDealTable tr:gt(0)").remove();//删除除了第一行以外的tr
	$("#warnDealDiv").hide();
}

//向生成事件的table添加内容
function addEventToTable(eventList){
	if(eventList.length>0){
		$("#makeEventMode").append("<option value=\"1\">并入已有的事件</option>");
	}
	for(var i=0;i<eventList.length;i++){
		var trDom = "<tr class='bizEventTr'>                                                "+
		"	<td style='text-align: center;'>                                                "+
		"		<input type=\"radio\" name=\"eventId\" value=\""+eventList[i].eventId+"\" ";
		if(i==0){
			trDom+=" checked ";
		}
		trDom+="	></td>                     "+
		"	<td>"+eventList[i].eventCrtTime+"</td>"+
		"	<td>"+eventList[i].eventDesc+"</td>"+
		"</tr>";
		$("#eventTable").append(trDom);
	}
}
//移除生成事件的table内容
function removeEventFromTable(){
	$("#makeEventMode option[value='1']").remove();
	$("#eventTable tr:gt(0)").remove();
}

function setTridToCookie(trid){
	//将已知的告警信息设置到cookie中，以保证每次进来的时候都是已知的
	var warnSeq = $.cookie("warnSeq");
	if(warnSeq){
		warnSeq = strToArray(warnSeq);//将字符串转换成数组
		if($.inArray(trid,warnSeq)==-1){//数组中不存在数据时
			warnSeq.push(trid);//将tr的id存放进去
		}
		warnSeq = arrayToStr(warnSeq);//将数组转换成字符串
	}else{
		warnSeq = trid;
	}
	$.cookie("warnSeq", warnSeq); //将标识为已知的告警信息存放到cookie
}

//将cookie中的指定的告警id去掉
function removeTridToCookie(trid){
	var warnSeq = $.cookie("warnSeq");
	if(warnSeq){
		warnSeq = strToArray(warnSeq);//将字符串转换成数组
		for(var i=0;i<warnSeq.length;i++){
			if(warnSeq[i] == trid){
				warnSeq.splice(i,1);
			}
		}
		warnSeq = arrayToStr(warnSeq);//将数组转换成字符串
	}
	$.cookie("warnSeq", warnSeq); //将标识为已知的告警信息存放到cookie
}

//页面初始化
function pageInit(){
	warnInit();
	createWebSocket();
	keepAlive();
}