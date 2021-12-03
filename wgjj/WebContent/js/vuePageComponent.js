
var styleTemp=`
<style>
.pagination ul>li>a:focus{
	background-color: #fff;
}
.pagination ul>li>a:hover, 
.pagination ul>.active>a{
    background-color: #f5f5f5 !important;
}
</style>
`;
document.write(styleTemp);
var pageTemp=`
  <div class="pagination" style="width:100%" v-if="totalCount>0">
    <ul id="page" class="pagination" v-if="totalPage>1">
    	<li class="prev" @click="turn(1)"><a href="javascript:void(0)">首页</a></li>
    	<li class="prev" :class="{'disabled':curPage<=1}" @click="curPage<=1?'':turn(curPage-1)"><a href="javascript:void(0)">上一页</a></li>
    	<li v-for="i in showPageBtn" :class="{'active':i==curPage}"  @click="turn(i)"><a href="javascript:void(0)">{{i}}</a></li>
    	<li class="next" :class="{'disabled':curPage>=totalPage}"  @click="curPage>=totalPage?'':turn(curPage+1)"><a href="javascript:void(0)">下一页</a></li>
    	<li class="next" @click="turn(totalPage)"><a href="javascript:void(0)">末页</a></li>
	</ul>
    <span class="pagination_sub">显示<span>{{(curPage-1)*pageSize+1 }}</span>-<span>{{endCount }}</span>条，共<span>{{totalCount }}</span>条</span>
  </div>`;
Vue.component('pagination',{
	template:pageTemp,
    props: {
      curPage: {//当前页
        type: Number,
        default: 1
      },
      pageSize:{//每页显示条数
        type: Number,
        default: 20
      },
      totalCount:{//总条数
        type: Number,
        default:0
      }
    },
    data() {
      return {
      }
    },
    computed: {
      totalPage(){
        return Math.ceil(this.totalCount / this.pageSize);
      },
	  endCount(){
		return this.curPage<this.totalPage ? this.curPage*this.pageSize : this.totalCount;
	  },
      showPageBtn() {
        let arr = [];
        if (this.totalPage <= 5) {
          for(let i = 1; i <= this.totalPage; i++) {
            arr.push(i);
          }
          return arr;
        }
        if(this.curPage<5){
        	return [1,2,3,4,5];
        }else if (this.curPage >=5 ) {
        	if(this.curPage+2<=this.totalPage){
        		return [this.curPage-2,this.curPage-1,this.curPage,this.curPage+1,this.curPage+2];
        	}else{
        		return [this.totalPage-4,this.totalPage-3,this.totalPage-2,this.totalPage-1,this.totalPage];
        	}
        }
      }
    },
    methods: {
      //翻页，显示条数变化
      turn(page) {
        let i = parseInt(Number(page));
        if(i<1){
          i=1;
        }else if(i>this.totalPage){
          i=this.totalPage;
        }
        this.$emit('update:curPage', i);   //使用.sync 修饰符实现父子双向绑定  显示调用触发
        this.$emit('turn',i);
      }
    }
});