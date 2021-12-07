/**
 * 照会基本信息组件
 * 用法：
 * 	<audit-form :note="note"></note>
 * 	可以显示计划相关信息
 * 	<audit-form :note="note" :plan="plan"></note>
 */
var formTempStyle=`
	<style>
		.tab-content .right_content_all{
			position: relative;
			margin-left: 15%;
			margin-right: 15%;
		}
		.audit_table{
			table-layout: fixed;
		}
		.audit_table table tr td{
			padding-left: 0px;
			text-align: center;
		}
		.table_title h1{
			text-align: center;
		}
		.table_title p{
			float: right;
			margin-top: -20px;
		}
		.audit_table .tr1{
			height: 160px;
		}
		.audit_table .tr2{
			height: 240px;
		}
		.table_bottom {
			height: 1rem;
		}
		.table_bottom p{
			float: left;
			width: 33%;
		}
	</style>
`;
document.write(formTempStyle);
var pageTemp=`
<div style="height: 80%;padding-top: 5px;">
    <ul id="myTab" class="nav nav-tabs">
        <li :class="appfoot.show ? 'active' : 'hide'"><a href="#flag_0" data-toggle="tab">审批表</a></li>
        <li :class="appfoot.show ? '':'active'"><a href="#flag_1" data-toggle="tab">许可函</a></li>
    </ul>

    <div id="myTabContent" class="tab-content " style="height: calc(100% - 38px);">
        <div :class="appfoot.show ? 'tab-pane fade in active' : 'tab-pane fade'" id="flag_0"  style="height: 98%">
            <div class="right_content">
                <div class="right_content_all">
                    <div class="right_content_table audit_table">
                        <div class="table_title">
                            <h1>审批表{{appfoot.stat ? '(建议稿)':''}}</h1>
                            <p>{{plan.appYear}}年{{plan.appXh}}号</p>
                        </div>
                        <table class="table table-bordered table_list" style="width: 100%;" align="center">
                            <tr>
                                <td style="width: 45px;height: 0px;" v-for="item in 8"></td>
                            </tr>
                            <tr>
                                <td colspan="4">
                                    <label>来电来函单位</label>
                                </td>
                                <td>
                                    <label>姓名</label>
                                </td>
                                <td colspan="3">
                                    <label>电话</label>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4">
                                    <label>{{note.letterUnit}}</label>
                                </td>
                                <td>
                                    <label>{{note.personName}}</label>
                                </td>
                                <td colspan="3">
                                    <label>{{note.telNo}}</label>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <label>国家/照会号</label>
                                </td>
                                <td colspan="2">
                                    <label>机型</label>
                                </td>
                                <td>
                                    <label>架数</label>
                                </td>
                                <td colspan="3">
                                    <label>飞行日期</label>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <label>{{note.nationality}}/{{note.noteNo}}</label>
                                </td>
                                <td colspan="2">
                                    <label>{{note.model}}</label>
                                </td>
                                <td>
                                    <label>{{note.planeNumber}}</label>
                                </td>
                                <td colspan="3">
                                    <label>{{wordvo.fxrq}}</label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>任务目的</label>
                                </td>
                                <td colspan="7" style="text-align: left">
                                    <label>&#12288;&#12288;&#12288;{{note.mission}}</label>
                                </td>
                            </tr>
                            <tr class="tr1">
                                <td>
                                    <label>飞行</br></br>航线</label>
                                </td>
                                <td colspan="7" style="text-align: left">
                                    <p style="text-indent: 40px" v-for="item in wordvo.fxhxList" v-html="item"></p>
                                </td>
                            </tr>
                            <tr class="tr1">
                                <td>
                                    <label>局领导</br></br>审批</label>
                                </td>
                                <td colspan="7">
                                    <p></p>
                                </td>
                            </tr>
                            <tr class="tr1">
                                <td>
                                    <label>呈办</br></br>意见</label>
                                </td>
                                <td colspan="7" style="text-align: left">
                                    <label style="height: 100px;">&#12288;&#12288;&#12288;{{wordvo.cbyj}}</label>
                                    <p style="margin: 0px 13rem 2rem 0px;text-align: right">中部组</p>
                                    <p style="margin: 0px 4rem 0px 0px;text-align: right">&#12288;年&#12288;月&#12288;日</p>
                                </td>
                            </tr>
                        </table>
                        <div class="table_bottom">
                            <p>承办单位：<span v-if="appfoot !=null">{{appfoot.cbdw}}</span></p>
                            <p>联系人：<span v-if="appfoot !=null">{{appfoot.lxr}}</p>
                            <p>电话：<span v-if="appfoot !=null">{{appfoot.dh}}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div :class="appfoot.show ? 'tab-pane fade' : 'tab-pane fade in active'" id="flag_1"  style="height: 98%">
            <div class="right_content">
                <div class="right_content_all">
                    <div class="right_content_table audit_table">
                        <div class="table_title">
                            <h1>许可函{{appfoot.stat ? '(建议稿)':''}}</h1>
                            <p>{{plan.appYear}}年{{plan.appXh}}号</p>
                        </div>
                        <table class="table table-bordered table_list" style="width: 100%;" align="center">
                            <tr>
                                <td style="width: 45px;height: 0px;" v-for="item in 8"></td>
                            </tr>
                            <tr>
                                <td colspan="4">
                                    <label>来电来函单位</label>
                                </td>
                                <td>
                                    <label>姓名</label>
                                </td>
                                <td colspan="3">
                                    <label>电话</label>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4">
                                    <label>{{note.letterUnit}}</label>
                                </td>
                                <td>
                                    <label>{{note.personName}}</label>
                                </td>
                                <td colspan="3">
                                    <label>{{note.telNo}}</label>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <label>国家/照会号</label>
                                </td>
                                <td colspan="2">
                                    <label>机型</label>
                                </td>
                                <td>
                                    <label>架数</label>
                                </td>
                                <td colspan="3">
                                    <label>飞行日期</label>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <label>{{note.nationality}}/{{note.noteNo}}</label>
                                </td>
                                <td colspan="2">
                                    <label>{{note.model}}</label>
                                </td>
                                <td>
                                    <label>{{note.planeNumber}}</label>
                                </td>
                                <td colspan="3">
                                    <label>{{wordvo.fxrq}}</label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>任务目的</label>
                                </td>
                                <td colspan="7" style="text-align: left">
                                    <label>&#12288;&#12288;&#12288;{{note.mission}}</label>
                                </td>
                            </tr>
                            <tr class="tr2">
                                <td>
                                    <label>飞行</br></br>航线</label>
                                </td>
                                <td colspan="7" style="text-align: left">
                                    <p style="text-indent: 40px" v-for="item in wordvo.fxhxList" v-html="item"></p>
                                </td>
                            </tr>
                            <tr class="tr2">
                                <td>
                                    <label>通知</br></br>单位</label>
                                </td>
                                <td colspan="7" style="text-align: left">
                                    <p style="text-indent: 40px" v-for="item in unitlist">{{item.unitName}}</p>
                                </td>
                            </tr>
                        </table>
                        <div class="table_bottom">
                            <p>承办单位：<span v-if="appfoot !=null">{{rptfoot.cbdw}}</span></p>
                            <p>联系人：<span v-if="appfoot !=null">{{rptfoot.lxr}}</p>
                            <p>电话：<span v-if="appfoot !=null">{{rptfoot.dh}}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>`;
Vue.component('audit-form',{
	template:pageTemp,
    props: {
        note: {
            type: Object,
            default: {}
        },
        plan: {
            type: Object,
            default: {}
        },
        wordvo: {
            type: Object,
            default: {}
        },
        appfoot: {
            type: Object,
            default: {}
        },
        unitlist: {
            type: Array,
            default: []
        },
        rptfoot: {
            type: Object,
            default: {}
        }
    },
    data() {
      return {}
    },
    computed: {
    },
    methods: {
    }
});