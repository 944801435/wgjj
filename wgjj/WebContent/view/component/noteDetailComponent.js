/**
 * 照会基本信息组件
 * 用法：
 * 	<note-detail :note="note"></note>
 * 	可以显示计划相关信息
 * 	<note-detail :note="note" :plan="plan"></note>
 */
var detailTempStyle=`
	<style>
		.noteDetailTable tr td:nth-child(odd){
			text-align: right;
			width: 10%;
		}
		.noteDetailTable tr td:nth-child(even){
			width: 23%;
		}
	</style>
`;
document.write(detailTempStyle);
var pageTemp=`
<div>
	<div class="right_content_all_top my-collapse" :href="'#'+panelId">
		<span>基本信息</span>
	</div>
	<div :id="panelId" class="right_content_table">
         <table class="table table-bordered table_list noteDetailTable">
         	<tr>
				<td>照会编号：</td>
				<td>{{note.noteSeq}}</td>
				<td>国家：</td>
				<td>{{note.nationality}}</td>
				<td>照会号：</td>
				<td>{{note.noteNo}}</td>
         	</tr>
         	<tr>
				<td>营运方：</td>
				<td>{{note.operator}}</td>
				<td>机型：</td>
				<td>{{note.model}}</td>
				<td>架数：</td>
				<td>{{note.planeNumber}}</td>
         	</tr>
			<tr>
				<td>呼号：</td>
				<td>{{note.callSign}}</td>
				<td>注册号：</td>
				<td>{{note.regNo}}</td>
				<td>机组人数：</td>
				<td>{{note.personNumber}}</td>
			</tr>
			<tr>
				<td>来函单位：</td>
				<td>{{note.letterUnit}}</td>
				<td>联系人：</td>
				<td>{{note.personName}}</td>
				<td>电话：</td>
				<td>{{note.telNo}}</td>
			</tr>
			<tr>
				<td>任务目的：</td>
				<td colspan="3">{{note.mission}}</td>
				<td>文书扫描件：</td>
				<td>
					<a :href="'${ctx}/download.action?fileId='+note.noteZipFileId">下载</a>
				</td>
			</tr>
			<tr v-if="plan!=null">
				<td>审批结果：</td>
				<td>{{plan.planSts | planStsFilter}}</td>
				<td>拒绝原因：</td>
				<td colspan="3">{{plan.rejectMsg}}</td>
			</tr>
			<tr v-if="plan!=null">
				<td>审批表：</td>
				<td>
					<span v-if="plan.backZipFileId!=null&&plan.backZipFileId!=''">
						<a :href="'${ctx}/download.action?fileId='+plan.backZipFileId">下载</a>
						<a @click="viewFile">预览</a>
						<div id="fileDiv" style="display: none;">
							<view-file :filelist="planbackfilelist"></view-file>
						</div>
					</span>
				</td>
				<td>许可函：</td>
				<td colspan="3">
					<a v-if="plan.rptPdfFileId!=null&&plan.rptPdfFileId!=''" :href="'${ctx}/download.action?fileId='+plan.rptPdfFileId">下载</a>
				</td>
			</tr>
		</table>
	</div>
</div>`;
Vue.component('note-detail',{
	template:pageTemp,
    props: {
      note: {
        type: Object,
        default: {}
      },
      plan: Object,
      planbackfilelist: Array
    },
    data() {
      return {
    	  panelId: new Date().getTime()
      }
    },
    computed: {
    },
    methods: {
    	viewFile(){
    		layer.open({
    			type:1,
    			title:'审批表扫描件',
    			content: $("#fileDiv"),
    			shadeClose: false,    //开启遮罩关闭
    			shade: false,
    			area:['800px','500px'],
    			maxmin: true,
    			closeBtn: 1,// 不显示关闭按钮
    			isOutAnim: false,// 关闭关闭图层动画效果
    			anim: -1,// 关闭打开图层动画效果
    			success:function(layero,index){
    			}
    		});
    	}
    }
});