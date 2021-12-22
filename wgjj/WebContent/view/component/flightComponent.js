/**
 * 航线列表组件
 * 用法：
 * 	<flight :flightlist="flightList" title="照会文书原始航线"></flight>
 * 	<flight :flightlist="flightList" title="民航调整航线" :caac="caac"></flight>
 * 	<flight :flightlist="flightList" title="本次计划航线"></flight>
 * 	包含接收时间的字段
 * 	<flight :flightlist="flightList" title="本次计划航线" :opttime=""></flight>
 * 	可调整简化线路
 * 	<flight :flightlist="flightList" title="本次计划航线" updatesimple="true"></flight>
 * 	可调整航线:update为回调方法
 * 	<flight :flightlist="flightList" title="本次计划航线" updateflight="true" @turn="update"></flight>
 */
var flightTempStyle=`
	<style>
		.flightTable tr td{
			text-align: center;
			padding-left: 0 !important;
		}
	</style>
`;
document.write(flightTempStyle);
var flightTemp=`
<div>
	<div class="right_content_all_top my-collapse" :href="'#'+panelId">
		<span>{{title}}</span>
	</div>
	<div :id="panelId" class="right_content_table">
		<div v-if="caac!=null" style="line-height: 20px; font-size: 12px; margin-left: 5px;">
			导入时间：{{caac.crtTime}}&#12288;&#12288;
			版本：{{caac.appVer}}&#12288;&#12288;
			民航意见扫描件：<a :href="'${ctx}/download.action?fileId='+caac.caacZipFileId">下载</a>
		</div>
		<div v-if="opttime!=null" style="line-height: 20px; font-size: 12px; margin-left: 5px;">
			接收时间：{{opttime}}
		</div>
		<table class="table table-bordered table_list flightTable">
         	<tr>
				<td width="8%">飞行日期</td>
				<td width="10%">其他日期</td>
				<td width="8%">起飞机场</td>
				<td width="8%">降落机场</td>
				<td width="6%">入境点</td>
				<td width="6%">出境点</td>
				<td>航路</td>
				<td width="25%" v-if="updatesimple">简化航路</td>
				<td width="5%" v-if="updateflight">操作</td>
			</tr>
         	<tr v-for="item in flightlist">
				<td>{{item.planDate }}</td>
				<td>{{item.otherDate }}</td>
				<td>{{item.upAirport }}</td>
				<td>{{item.downAirport }}</td>
				<td>{{item.inPointIdent }}</td>
				<td>{{item.outPointIdent }}</td>
				<td style="text-align: left; padding-left: 3px !important;">{{item.flightBody }}</td>
				<td v-if="updatesimple" style="text-align: left; padding-left: 3px !important;">
					<textarea name="flightBodySimple" v-model="item.flightBodySimple" dataType="Require" msg="请输入航路，不同航路航点间以空格分割！" rows="3" style="width: 90%;"></textarea>
				</td>
				<td v-if="updateflight">
					<a @click="update(item)">调整</a>
				</td>
			</tr>
         </table>
     </div>
</div>
`;
Vue.component('flight',{
	template:flightTemp,
    props: {
    	title: String,
    	flightlist: Array,
    	caac: Object,
    	updatesimple: false,
    	updateflight: false,
    	opttime: null
    },
    data() {
        return {
        	panelId: new Date().getTime()
        }
    },
    watch: {
    },
    methods: {
    	update(item){
    		this.$emit('turn',item);
    	}
    },
    mounted:function(){
    }
});