// 清除全局缓存
var Tool = {};
//登录信息
Tool.userInfo = function(){
			var html = '';
			html += '<table class="table table-hover table-striped table-bordered">';
			html += '<tr><td width="200">账号</td><td>'+userbean.userName+'</td></tr>';
			html += '<tr><td>手机</td><td>'+userbean.phone+'</td></tr>';
			html += '<tr><td>机构</td><td>'+userbean.deptName+'</td></tr>';
			html += '<tr><td>最后登录IP</td><td>'+userbean.lastIp+'</td></tr>';
			html += '<tr><td>最后登录时间</td><td>'+userbean.lastTime+'</td></tr>';
			html += '</table>';					
			base.Dialog('个人资料',html);
}
