/**
 * 查看文件组件
 * 用法：
 * 	fileList是SysFile的集合
 * 	<view-file :filelist="fileList"></view-file>
 */
var viewFileTempStyle=`
	<style>
		.bigFileDiv{
			width: 100%;
			height: 460px;
		}
		.smallFileDiv{
			width: 100%;
			margin: 0 0 5px 5px;
		}
		.fileDiv img,.fileDiv embed{
			width: 100%;
			height: calc(100% - 14px);
		}
	</style>
`;
document.write(viewFileTempStyle);
var viewFileTemp=`
	<div class="block">
		<div style="margin-bottom:5px;"><b>{{title}}</b></div>
		<table style="width: 100%;">
			<tr>
				<td style="vertical-align: top; width: 80%; text-align: left;">
					<div v-if="bigFileObj!=null" class="bigFileDiv fileDiv">
						<a>{{bigFileObj.fileName}}</a>
						<embed v-if="bigFileObj.fileName.indexOf('.pdf')!=-1" :src="'${ctx}/photo.action?fileId='+bigFileObj.fileId">
						<img v-else :src="'${ctx}/photo.action?fileId='+bigFileObj.fileId">
					</div>
				</td>
				<td style="vertical-align: top;">
					<div v-for="file in filelist" class="smallFileDiv fileDiv">
						<a @click="toBigDiv(file)">{{file.fileName}}</a>
						<embed v-if="file.fileName.indexOf('.pdf')!=-1" :src="'${ctx}/photo.action?fileId='+file.fileId">
						<img v-else :src="'${ctx}/photo.action?fileId='+file.fileId">
					</div>
				</td>
			</tr>
		</table>
	</div>`;
Vue.component('view-file',{
	template:viewFileTemp,
    props: {
    	title: String,
    	filelist: Array
    },
    data() {
      return {
    	  bigFileObj: null
      }
    },
    watch: {
    	filelist: function(nval){
    		this.bigFileObj = nval.length>0 ? nval[0] : null;
    	}
    },
    methods: {
    	toBigDiv: function(fileObj){
    		this.bigFileObj = fileObj;
    	}
    },
    mounted:function(){
    	this.bigFileObj = this.filelist.length>0 ? this.filelist[0] : null;
    }
});