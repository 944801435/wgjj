/**
My97DatePicker  双向绑定指令
示例 <input v-selected dateFormat="yyyy-MM-dd" v-model="dateTime"/>
 */
Vue.directive('selected',{
	inserted:(el,binding,vnode)=>{
		let defaultDateFmt="yyyy-MM-dd HH:mm:ss";
		let dateFormat=$(el).attr('dateFormat');
		let minDate=$(el).attr('minDate');
		let maxDate=$(el).attr('maxDate');
		let isShowClear=$(el).attr('isShowClear');
		$(el).on('click',function(){
			WdatePicker({
				dateFmt:(dateFormat!=null && dateFormat!='') ? dateFormat : defaultDateFmt,
				isShowClear:(isShowClear!=null && isShowClear!='') ? ('false'==isShowClear? false: true) : true,
				startDate:'%y-%M-%d %H:00:00',
				minDate:minDate,
				maxDate:maxDate,
				onpicked:()=>{
					triggerInput(el,'input');
				},
				oncleared:()=>{
					triggerInput(el,'input');
				}
			});
		});
		el.onchange=function(){
			triggerInput(el,'input');
		}
	}
	
}); 
function triggerInput(el, type) {
    var e = document.createEvent("HTMLEvents");
    e.initEvent(type, true, true);
    el.dispatchEvent(e);
}

/**
select2 双向绑定指令
示例： <input v-select2 v-model="selectName"/>
 */
Vue.directive('select2', {
    inserted: function (el, binding, vnode) {
        let options = binding.value || {};
        $(el).select2(options).on("select2:select", (e) => {
            el.dispatchEvent(new Event('change', {target: e.target}));
        });
    },
    update: function (el, binding, vnode) {
		for (var i = 0; i < vnode.data.directives.length; i++) {
            if (vnode.data.directives[i].name == "model") {
            	$(el).val(vnode.data.directives[i].value);
				break;
            }
        }
		$(el).trigger("change");
    }
});
