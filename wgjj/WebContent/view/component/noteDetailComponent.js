var detailTempStyle=`
	<style>
		.detailTable tr td:nth-child(1){
			line-height: 30px;
			text-align: right;
			width: 32%;
		}
		.detailTable tr td:nth-child(2){
			line-height: 30px;
		}
	</style>
`;
document.write(detailTempStyle);
var pageTemp=`
  <div class="block">
	<div style="margin-bottom:5px;"><b>基本信息</b></div>
	<table class="detailTable">
		<tr>
			<td>照会编号：</td>
			<td>{{note.noteSeq}}</td>
		</tr>
		<tr>
			<td>国家：</td>
			<td>{{note.nationality}}</td>
		</tr>
		<tr>
			<td>许可证号：</td>
			<td>{{note.licenseNo}}</td>
		</tr>
		<tr>
			<td>注册号：</td>
			<td>{{note.regNo}}</td>
		</tr>
		<tr>
			<td>呼号：</td>
			<td>{{note.acid}}</td>
		</tr>
		<tr>
			<td>二次雷达代码：</td>
			<td>{{note.ssrCode}}</td>
		</tr>
		<tr>
			<td>机型：</td>
			<td>{{note.model}}</td>
		</tr>
		<tr>
			<td>机组人数：</td>
			<td>{{note.personNumber}}</td>
		</tr>
		<tr>
			<td>来函单位：</td>
			<td>{{note.letterUnit}}</td>
		</tr>
		<tr>
			<td>联系人：</td>
			<td>{{note.personName}}</td>
		</tr>
		<tr>
			<td>电话：</td>
			<td>{{note.telNo}}</td>
		</tr>
		<tr>
			<td>任务目的：</td>
			<td>{{note.mission}}</td>
		</tr>
		<tr>
			<td>文书扫描件：</td>
			<td><a :href="'${ctx}/download.action?fileId='+note.noteZipFileId">下载</a></td>
		</tr>
		<tr v-if="caac!=null">
			<td>民航意见扫描件：</td>
			<td><a :href="'${ctx}/download.action?fileId='+caac.caacZipFileId">下载</a></td>
		</tr>
	</table>
</div>`;
Vue.component('note-detail',{
	template:pageTemp,
    props: {
      note: {
        type: Object,
        default: {}
      },
      caac: Object
    },
    data() {
      return {
      }
    },
    computed: {
    },
    methods: {
    }
});