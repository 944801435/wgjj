function onLoad_validate(){
     var forms=document.forms;
     for(var i=0;i<forms.length;i++){
         Validator.Validate_onLoad(forms[i],3);
     }
}
window.onload=onLoad_validate;

Validator = 
{
CEN_:/^[0-9A-z_\u4e00-\u9fa5]+$/,
EN_:/^[0-9A-z_]+$/,
Require:/\S+/, 
Email:/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, 
Phone:/^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/, 
Mobile:/^1\d{10}$/, Url:/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/, 
IdCard:/^\d{15}(\d{2}[A-Za-z0-9])?$/, 
Currency:/^\d+(\.\d+)?$/, 
Angle:/^\d+(\.\d{1,2})?$/, 
Number:/^\d+$/, 
Int:/^[0-9]\d*$/,
Zip:/^[1-9]\d{5}$/, 
QQ:/^[1-9]\d{4,8}$/, 
Integer:/^[-\+]?\d+$/, 
Double:/^[-\+]?\d+(\.\d+)?$/, 
Float:/^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/,
FloatOrZero:/^\d+(\.\d+)?$/,
English:/^[A-Za-z]+$/, 
Chinese:/^[\u0391-\uFFE5]+$/, 
UnSafe:/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/, 
IsSafe:function (str) {
	return !this.UnSafe.test(str);
}, 
DFMS:/^E\d+\°\d+\′\d+\″,N\d+\°\d+\′\d+\″$/,
JWD:/^[\-\+]?(0(\.\d{1,10})?|([1-9](\d)?)(\.\d{1,10})?|1[0-7]\d{1}(\.\d{1,10})?|180\.0{1,10}),[\-\+]?((0|([1-8]\d?))(\.\d{1,10})?|90(\.0{1,10})?)$/,
SafeString:"this.IsSafe(value)", 
Limit:"this.limit(value.length,getAttribute('min'), getAttribute('max'))", 
LimitB:"this.limit(this.LenB(value), getAttribute('min'), getAttribute('max'))", 
Date:"this.IsDate(value, getAttribute('min'), getAttribute('format'))", 
Repeat:"value == document.getElementsByName(getAttribute('to'))[0].value", 
Range:"getAttribute('min') =< value && value =< getAttribute('max')", 
Compare:"this.compare(value,getAttribute('operator'),getAttribute('to'))", 
Custom:"this.Exec(value, getAttribute('regexp'))", 
Group:"this.MustChecked(getAttribute('name'), getAttribute('min'), getAttribute('max'))", 
ErrorItem:[document.forms[0]], 
ErrorMessage:[""], 
Validate_onLoad:function (theForm, mode) {
    var maxLen=10;
	var obj = theForm || event.srcElement;
	var count = obj.elements.length;
	this.ErrorMessage.length = 1;
	this.ErrorItem.length = 1;
	this.ErrorItem[0] = obj;
	for (var i = 0; i < count; i++) {
		with (obj.elements[i]) {
			var _dataType = getAttribute("dataType");
			if(_dataType!=null){
			   if(_dataType.indexOf(',')>0){
			   var array_=_dataType.split(',');
			   if(typeof(_dataType)=="object"){
			       continue;
			   }
			   for(var n=0;n<array_.length;n++){
			       if(typeof(this[array_[n]])=="undefined"){
			           break;
			       }
			   }
			}else if (typeof (_dataType) == "object" || typeof (this[_dataType]) == "undefined") {
				continue;
			}
			}else{
			   continue;
			}
			
			this.ClearState(obj.elements[i]);
			if (getAttribute("require") == "false" && value == "") {
				continue;
			}
			if(_dataType.indexOf('Require')>-1){
			    this.AddError_ts(i);
			}
		    
		}
	}
	if (this.ErrorMessage.length > 1) {
		mode = mode || 1;
		var errCount = this.ErrorItem.length;
		switch (mode) {
		  case 2:
			for (var i = 1; i < errCount; i++) {
				this.ErrorItem[i].style.color = "red";
			}
		  case 1:
			alert(this.ErrorMessage.join("\n"));
			this.ErrorItem[1].focus();
			break;
		  case 3:
			for (var i = 1; i < errCount; i++) {
				try {
                    var span = document.createElement("SPAN");
                    span.id = "__ErrorMessagePanel";
                    span.style.color = "red";
                    span.style.padding="5px";
                    this.ErrorItem[i].parentNode.appendChild(span);
                    span.innerHTML = this.ErrorMessage[i];
				}
				catch (e) {
					alert(e.description);
				}
			}
			this.ErrorItem[1].focus();
			break;
		  default:
			alert(this.ErrorMessage.join("\n"));
			break;
		}
		return false;
	}
	return true;
},
Validate:function (theForm, mode) {
    var maxLen=10;
	var obj = theForm || event.srcElement;
	var count = obj.elements.length;
	this.ErrorMessage.length = 1;
	this.ErrorItem.length = 1;
	this.ErrorItem[0] = obj;
	for (var i = 0; i < count; i++) {
		with (obj.elements[i]) {
			var _dataType = getAttribute("dataType");
			if(_dataType!=null){
			   if(_dataType.indexOf(',')>0){
			   var array_=_dataType.split(',');
			   if(typeof(_dataType)=="object"){
			       continue;
			   }
			   for(var n=0;n<array_.length;n++){
			       if(typeof(this[array_[n]])=="undefined"){
			           break;
			       }
			   }
			}else if (typeof (_dataType) == "object" || typeof (this[_dataType]) == "undefined") {
				continue;
			}
			}else{
			   continue;
			}
			
			this.ClearState(obj.elements[i]);
			if (getAttribute("require") == "false" && value == "") {
				continue;
			}
			var array_=_dataType.split(",");
			for(var k=0;k<array_.length;k++){
			       var flag=false;
			       switch (array_[k]) {
			              case "Date":
			              case "Repeat":
			              case "Custom":
			              case "Group":
			              case "LimitB":
			              case "SafeString":
				               if (!eval(this[_dataType])) {
				               	        flag=true;
					                this.AddError(i, getAttribute("msg"));
				               }
				               break;
			              case "MONEY_TO_10000":
			            	  if(value%10000!=0){
			            		  flag=true;
			            		  this.AddError(i,getAttribute("msg"));
			            	  }
			            	  break;
			              case "Range":
			            	  var min = getAttribute("min");
			            	  var max = getAttribute("max");
			            	  if(parseFloat(value) < parseFloat(min) || parseFloat(value) > parseFloat(max)){
			            		  flag=true;
			            		  this.AddError(i,getAttribute("msg"));
			            	  }
			            	  break;
			              case "Compare":
			            	  var op2=getAttribute("to");
			            	  var op2_value=document.getElementsByName(op2)[0].value;
			            	  if(!this.compare(value, "equal", op2_value)){
			            		  flag=true;
			            		  this.AddError(i,getAttribute("msg"));
			            	  }
			            	  break;
			              case "Equal":
			                   if(obj.elements[i-1].value!=value || obj.elements[i-1].value.length!=value.length){
			                   	flag=true;
			                        this.AddError(i,getAttribute("msg"));
			                   }
			                   break;
			              case "Limit":
			                   var num=Bytelen(value);
			                   var len=getAttribute("len");
			                   if(len!=null){
			                         maxLen=len;
			                   }
			                   if(maxLen<num){
			                   	flag=true;
			                         this.AddError(i,getAttribute("msg"));
			                   }
			                   break;
			              case "Require":
			            	  if(!this[array_[k]].test(value)){
			            		  flag=true;
			                      this.AddError(i, getAttribute("msg"));
			            	  }
			            	  break;
			              case "gt_lt":
			            	  var op_model=getAttribute("op");
			            	  var op_value=document.getElementById(getAttribute("to")).value;
			            	  if(op_model=='gt'){
			            		  if(parseFloat(value)<parseFloat(op_value)){
			            			  flag=true;
			            			  this.AddError(i, getAttribute("msg"));
			            		  }
			            	  }else if(op_model=='lt'){
			            		  if(parseFloat(value)>parseFloat(op_value)){
			            			  flag=true;
			            			  this.AddError(i, getAttribute("msg"));
			            		  }
			            	  }
			            	  break;
			              default:
			                  if(!this[array_[k]].test(value)){
			                	  if(value.length==0 && array_[k]!='Require'){
			                		  continue;
			                	  }
			                      flag=true;
			                      this.AddError(i, getAttribute("msg"));
			                  }
				              break;
			          }
			          if(flag){
			              break;
			          }
			  }
		}
	}
	if (this.ErrorMessage.length > 1) {
		mode = mode || 1;
		var errCount = this.ErrorItem.length;
		switch (mode) {
		  case 2:
			for (var i = 1; i < errCount; i++) {
				this.ErrorItem[i].style.color = "red";
			}
		  case 1:
			//alert(this.ErrorMessage.join("\n"));
			alert(this.ErrorMessage.join("\n").replace(/\d+:/, ""));
			this.ErrorItem[1].focus();
			break;
		  case 3:
			for (var i = 1; i < errCount; i++) {
				try {
                    var span = document.createElement("SPAN");
                    span.id = "__ErrorMessagePanel";
                    span.style.color = "red";
                    span.style.padding="5px";
                    this.ErrorItem[i].parentNode.appendChild(span);
                    span.innerHTML = this.ErrorMessage[i].replace(/\d+:/,"&nbsp;<b>*</b>");
					//alert(this.ErrorMessage[i].replace(/\d+:/, ""));
					//break;
				}
				catch (e) {
					alert(e.description);
				}
			}
			this.ErrorItem[1].focus();
			break;
		  default:
			alert(this.ErrorMessage.join("\n"));
			break;
		}
		return false;
	}
	return true;
}, limit:function (len, min, max) {
	min = min || 0;
	max = max || Number.MAX_VALUE;
	return min <= len && len <= max;
}, LenB:function (str) {
	return str.replace(/[^\x00-\xff]/g, "**").length;
}, ClearState:function (elem) {
	with (elem) {
		if (style.color == "red") {
			style.color = "";
		}
		var lastNode = parentNode.childNodes[parentNode.childNodes.length - 1];
		if (lastNode.id == "__ErrorMessagePanel") {
			parentNode.removeChild(lastNode);
		}
	}
}, AddError:function (index, str) {
	this.ErrorItem[this.ErrorItem.length] = this.ErrorItem[0].elements[index];
	this.ErrorMessage[this.ErrorMessage.length] = this.ErrorMessage.length + ":" + str;
},AddError_ts:function (index) {
	this.ErrorItem[this.ErrorItem.length] = this.ErrorItem[0].elements[index];
	this.ErrorMessage[this.ErrorMessage.length] ="&nbsp;<b>*</b>";
}, Exec:function (op, reg) {
	return new RegExp(reg, "g").test(op);
}, compare:function (op1, operator, op2) {
	switch (operator) {
	  case "NotEqual":
		return (op1 != op2);
	  case "GreaterThan":
		return (op1 > op2);
	  case "GreaterThanEqual":
		return (op1 >= op2);
	  case "LessThan":
		return (op1 < op2);
	  case "LessThanEqual":
		return (op1 <= op2);
	  default:
		return (op1 == op2);
	}
}, MustChecked:function (name, min, max) {
	var groups = document.getElementsByName(name);
	var hasChecked = 0;
	min = min || 1;
	max = max || groups.length;
	for (var i = groups.length - 1; i >= 0; i--) {
		if (groups[i].checked) {
			hasChecked++;
		}
	}
	return min <= hasChecked && hasChecked <= max;
}, IsDate:function (op, formatString) {
	formatString = formatString || "ymd";
	var m, year, month, day;
	switch (formatString) {
	  case "ymd":
		m = op.match(new RegExp("^((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})$"));
		if (m == null) {
			return false;
		}
		day = m[6];
		month = m[5]--;
		year = (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
		break;
	  case "dmy":
		m = op.match(new RegExp("^(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))$"));
		if (m == null) {
			return false;
		}
		day = m[1];
		month = m[3]--;
		year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
		break;
	  default:
		break;
	}
	if (!parseInt(month)) {
		return false;
	}
	month = month == 12 ? 0 : month;
	var date = new Date(year, month, day);
	return (typeof (date) == "object" && year == date.getFullYear() && month == date.getMonth() && day == date.getDate());
	function GetFullYear(y) {
		return ((y < 30 ? "20" : "19") + y) | 0;
	}
}};
//获取字符串长度（汉字算三个字符，字母数字算一个）
function Bytelen(entryVal){
	//根据数据库字符集计算字符长度，如：oracle utf8每个汉字算3个字符；mysql utf8每个汉字算1个字符
	/*
	var entryLen=entryVal.length; 
	var cnChar=entryVal.match(/[^\x00-\x80]/g);//利用match方法检索出中文字符并返回一个存放中文的数组  
	if(cnChar!=""&&cnChar!=null){
		entryLen+=(cnChar.length*2);//算出实际的字符长度 ，每个汉字算三个字符
	}
	return entryLen;
	*/
	return entryVal.length;
}

function getByteLen(entryVal) {
	
}
//火狐设置
if(window.navigator.userAgent.toLowerCase().indexOf("firefox")>=0){ //firefox innerText  
   HTMLElement.prototype.__defineGetter__(     "innerText",  
    function(){  
     var anyString = "";  
     var childS = this.childNodes;  
     for(var i=0; i<childS.length; i++) {  
      if(childS[i].nodeType==1)  
       anyString += childS[i].tagName=="BR" ? '\n' : childS[i].textContent;  
      else if(childS[i].nodeType==3)  
       anyString += childS[i].nodeValue;  
     }  
     return anyString;  
    }  
   );  
   HTMLElement.prototype.__defineSetter__(     "innerText",  
    function(sText){  
     this.textContent=sText;  
    }  
   );  
}


