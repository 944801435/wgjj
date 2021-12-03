var flightTempStyle=`
	<style>
		.flightImg{
			width: 100%;
			height: 150px;
		}
	</style>
`;
document.write(flightTempStyle);
var flightTemp=`
	<div class="block" style="margin-top: 0;border-top: none;">
		<div style="margin-bottom:5px;"><b>{{title}}</b></div>
		<div v-if="caac!=null" style="line-height: 20px;">导入时间：{{caac.crtTime}}</div>
		<div v-if="caac!=null" style="line-height: 20px;">版本：{{caac.appVer}}</div>
		<div v-for="item in flightlist">
			<div style="line-height: 20px; text-align: right;">{{item.planDate}}</div>
			<div style="line-height: 20px;">
				起飞机场：{{item.upAirport}}
				<span style="width: 30px;"></span>
				降落机场：{{item.downAirport}}
			</div>
			<div>
				{{item.flightBody}}
			</div>
			<div>
				<img v-if="item.flightFileId!=null && item.flightFileId!=''" class="flightImg" :src="'${ctx}/photo.action?fileId='+item.flightFileId">
			</div>
			<hr>
		</div>
	</div>
`;
Vue.component('flight',{
	template:flightTemp,
    props: {
    	title: String,
    	flightlist: Array,
    	caac: Object
    },
    data() {
    },
    watch: {
    },
    methods: {
    },
    mounted:function(){
    }
});