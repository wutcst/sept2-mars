<template>
  <div>
    <Header />
  </div>

  <el-image style="width: 90%; height:80%" :src="url" />

  <div class="a">
  <el-descriptions title="" :column="3" border>
    <el-descriptions-item
        label="房间名"
        label-align="right"
        align="center"
        label-class-name="my-label"
        class-name="my-content"
        width="150px"
    >{{location}}</el-descriptions-item
    >
    <el-descriptions-item label="gold" label-align="right" align="center"
    >{{gold}}</el-descriptions-item
    >
    <el-descriptions-item label="cost" label-align="right" align="center"
    >{{cost}}</el-descriptions-item
    >
    <el-descriptions-item label="Remarks" label-align="right" align="center">
      <el-tag size="small">Room</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="房间描述信息" label-align="right" align="center"
    >{{descriptions}}</el-descriptions-item
    >
  </el-descriptions>
  </div>
  <div class="b">
      <el-row class="mb-4">
        <el-button type="success" plain @click="move(0,-1)">WEST </el-button>
        <el-button type="success" plain @click="move(0,1)">EAST </el-button>
        <el-button type="success" plain @click="move(-1,0)">NORTH</el-button>
        <el-button type="success" plain @click="move(1,0)">SOUTH</el-button>
        <el-button type="success" plain @click="back">BACK</el-button>
        <el-button type="warning" plain @click="take">TAKE</el-button>
        <el-button type="primary" plain @click="open">rule</el-button>
<!--        <el-button type="primary" plain @click="open">SAVE</el-button>-->
      </el-row>
  </div>
  <div>
    <el-dialog
        v-model="dialogVisible3"
        title="巨洞出口"
        width="30%"
        :before-close="handleClose"
    >
      <span>恭喜你，获得了离开洞穴的钥匙，并离开了洞穴</span>
      <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="gameWin()">
          确定
        </el-button>
      </span>
      </template>
    </el-dialog>
      <el-dialog
          v-model="dialogVisible4"
          title="game over"
          width="30%"
          :before-close="handleClose"
      >
        <span>你体力不支，倒在了洞穴里</span>
        <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="gameOver()">
          确定
        </el-button>
      </span>
        </template>
      </el-dialog>
  </div>

</template>

<script>
import request from "@/utils/request";
import Header from "@/components/Header.vue";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "product",
  components: {Header},
  data(){
    return{
      url:'',
      form:{},
      dialogVisible:false,
      dialogVisible2:false,
      dialogVisible3:false,
      dialogVisible4:false,
      currentPage:1,
      pageSize:10,
      total:10,
      search:'',
      tableData:[],
      rooms:[{'name':'outside','id':1,'gold':'1','cost':'-1','descriptions':'outside the main entrance of the colossal cave','picture':'assets/images/outside.jpg','takeflag':0},
        {'name':'theater','id':2,'gold':'2','cost':'-1','descriptions':'in the theater ,Mysterious songs are scattered throughout the opera house','picture':'assets/images/theater.jpg','takeflag':0},
        {'name':'pub','id':3,'gold':'4','cost':'-3','descriptions':'in the quiet bar, the clinking sound of wine glasses','picture':'assets/images/pub.jpg','takeflag':0},
        {'name':'lab','id':4,'gold':'3','cost':'-2','descriptions':'in a scientific laboratory, only the sound of machines running','picture':'assets/images/lab.jpg','takeflag':0},
        {'name':'treasure','id':5,'gold':'5','cost':'-1','descriptions':'in the treasure room, maybe there has some treasure','picture':'assets/images/treasure.jpg','takeflag':0},
        {'name':'randomRoom','id':6,'gold':'0','cost':'0','descriptions':'in a random room,where do you want to go','picture':'assets/images/randomroom.jpg','takeflag':1},
        {'name':'exit','id':7,'gold':'0','cost':'0','descriptions':'a way to leave this place!','picture':'assets/images/exit.jpg','takeflag':0},
        {'name':'corridor','id':8,'gold':'0','cost':'0','descriptions':'a long long corridor with cold wind','picture':'assets/images/corridor.jpg','takeflag':1}],
      items:[{'name':'cookie','id':1,'roomId':5,'descriptions':'增加5体力'},
        {'name':'key','id':2,'roomId':7,'descriptions':'通关的关键物品'}], //物品设置
      location:'', //当前位置
      gold:'', //金币
      cost:'', //体力消耗
      descriptions:'', //房间描述
      id:'', //房间id
      lastroom:'', //上次房间信息
      powerFlag:false, //玩家体力状态
      maps:[[1,2,3], [5,6,4], [8,7,0]] //地图
    }
  },
  created() {
    this.load()
  },
  methods:{
    /**
     * @description 游戏规则说明
     * @return void
     * */
    open(){
      this.$notify({
        title: '游戏规则说明',
        message: '巨洞探险：一个精彩的、充满想像力的复杂游戏，包括要在一个复杂的洞穴系统中找到出路、寻找隐藏的财宝、使用暗语和其他一些神秘工具，最终的目的是获得高分。',
        duration: 4500
      });
    },
    /**
     * @description 获取随机数函数
     * @param {int} min
     * @param {int} max
     * @return {int} 随机数值
     * */
    getRandomInt1(min, max) {
      min = Math.ceil(min);
      max = Math.floor(max);
      return Math.floor(Math.random() * (max - min)) + min; //不含最大值，含最小值
     },
    /**
     * @description 加载页面信息
     * @return void
     * */
    load(){
      request.get("/user"
      ).then(res=>{
        this.tableData=res.data.records
        this.total=res.data.total
        let user=JSON.parse(sessionStorage.getItem("user"))
        for(let data in res.data.records){
          if(res.data.records[data].id===user["id"]){
            this.location=res.data.records[data].currentroom
            this.lastroom=res.data.records[data].lastroom
          }
        }
        for(let index in this.rooms){
            let map=this.rooms[index]
            if(map['name']===this.location){
              this.gold=map['gold']
              this.cost=map['cost']
              this.descriptions="Now,you are "+map['descriptions']
              this.id=map['id']
              this.url=require("@/"+this.rooms[this.id-1]['picture'])
          }
        }
        if(this.location==="randomRoom"){
          alert("您处于随机房间，将会被传送离开")
          this.portal()
        }
      })
    },
    /**
     * @description 传送至randomRoom四周任意房间
     * @return void
     * */
    portal(){
      let a=this.getRandomInt1(1,5)
      switch (a){
        case 1:
          this.move(0,-1)
          break;
        case 2:
          this.move(0,1)
          break;
        case 3:
          this.move(-1,0)
          break;
        case 4:
          this.move(1,0)
          break;
      }
    },
    /**
     * @description 拿取房间内物品
     * @return void
     * */
    take(){
      let judgeFlag
      let num
      for(let index in this.rooms){
        if(this.rooms[index].name === this.location){
          judgeFlag=this.rooms[index].takeflag
          num=index
        }
      }
      if(judgeFlag==1) {
        alert("房间内无物品可以拿取")
      }else{
        let oldgold
        let oldcoin
        request.get("/user"
        ).then(res=>{
          let user=JSON.parse(sessionStorage.getItem("user"))
          for(let data in res.data.records){
            if(res.data.records[data].id===user["id"]){
              oldgold=res.data.records[data].gold
              oldcoin=res.data.records[data].coin
            }
          }
          let gold=(parseInt(oldgold)+parseInt(this.gold)).toString()
          let coin=(parseInt(oldcoin)+parseInt(this.cost)).toString()

          let mesage="gold+"+this.gold+"  power"+this.cost
          for(let index in this.items){
            if(this.items[index].roomId===parseInt(this.id)){
              if(parseInt(coin)<0){
                this.$message({
                  type:"error",
                  message:"您的体力不足"
                })
                return
              }else{
                coin=(parseInt(coin)+5).toString()
                mesage=mesage+" you get a "+this.items[index].name+" your power +5"
              }
            }
          }
          let data={"id":user["id"],"name":user["name"],"password":user["password"],"gender":user["gender"],"currentroom":this.location,"gold":gold,"coin":coin}
          request.put("/user",data).then(res => {
            console.log(res)
            if(res.code==="0"){
              this.$message({
                type:"success",
                message:mesage
              })
            }else {
              this.$message({
                type:"error",
                message:res.msg
              })
            }
            if(this.location==="exit"){
              this.dialogVisible3=true
            }
            this.load()
          })
        })
        judgeFlag=1
        this.rooms[num].takeflag=1
      }
    },
    /**
     * @description 刷新用户信息
     * */
    refresh(){
      let oldgold
      let oldcoin
      request.get("/user"
      ).then(res=>{
        let user=JSON.parse(sessionStorage.getItem("user"))
        for(let data in res.data.records){
          if(res.data.records[data].id===user["id"]){
            oldgold=res.data.records[data].gold
            oldcoin=res.data.records[data].coin
          }
        }
        oldcoin=(parseInt(oldcoin)-1).toString()
        if(parseInt(oldcoin)<=0){//体力不足
          this.dialogVisible4=true
        }else{
          let data={"id":user["id"],"name":user["name"],"password":user["password"],"gender":user["gender"],"currentroom":this.location,"gold":oldgold,"coin":oldcoin,"lastroom":this.lastroom}
          request.put("/user",data).then(res => {
            if(res.code==="0"){
              this.$message({
                type:"success",
                message:"成功"
              })
            }else {
              this.$message({
                type:"error",
                message:res.msg
              })
            }
            this.load()
          })
        }
      })
    },
    /**
     * @description 获取当前地图上玩家所处的位置
     * @return {array} 玩家所处的位置坐标数组，例如 [x, y]，x 表示行，y 表示列
     **/
    getPlayerPosition() {
      for(let i = 0; i < 3; i++) {
        for(let j = 0; j < 3; j++) {
          if(this.maps[i][j] === this.id) {
            return [i, j];
          }
        }
      }
    },

    /**
     * @description 判断新位置是否有效
     * @param {array} newPosition 新位置的坐标数组，例如 [x, y]，x 表示行，y 表示列
     * @return {boolean} 若新位置有效则返回 true，否则返回 false
     **/
    isValidPosition(newPosition) {
      const [x, y] = newPosition;
      return x >= 0 && y >= 0 && x <= 2 && y <= 2 && this.maps[x][y] !== 0;
    },

    /**
     * @description 根据新位置更新玩家状态
     * @param {array} newPosition 新位置的坐标数组，例如 [x, y]，x 表示行，y 表示列
     * @return void
     **/
    updatePlayerStatus(newPosition) {
      for(let index in this.rooms) {
        let map=this.rooms[index]
        if(map['id']===this.maps[newPosition[0]][newPosition[1]]){
          this.location=map['name']
          this.refresh()
        }
      }
    },

    /**
     * @description 移动玩家到新位置
     * @param {int} deltaX 表示南北方向的移动
     * @param {int} deltaY 表示东西方向的移动
     * @return void
     **/
    move(deltaX, deltaY) {
      const [x, y] = this.getPlayerPosition();
      const newPosition = [x + deltaX, y + deltaY];
      if (this.isValidPosition(newPosition)) {
        this.updatePlayerStatus(newPosition);
      } else {
        alert("此路不通");
      }
    },

    /**
     * @description 返回上一房间
     * @return void
     * */
    back(){
      let lastroom = this.lastroom
      this.lastroom=this.location
      this.location=lastroom
      this.refresh()
    },
    /**
     * @description 保存状态信息
     * @return void
     * */
    save(){
      if(this.form.id){
        request.put("/user",this.form).then(res => {
          console.log(res)
          if(res.code==="0"){
            this.$message({
              type:"success",
              message:"更新成功"
            })
          }else {
            this.$message({
              type:"error",
              message:res.msg
            })
          }
          this.load()
          this.dialogVisible=false
        })
      }
    },
    // eslint-disable-next-line no-unused-vars
    /**
     * @description 游戏胜利
     * @return void
     * */
    gameWin(){
      this.dialogVisible3 = false
      this.reset()
    },
    /**
     * @description 游戏结束（未获胜）
     * @return void
     * */
    gameOver(){
      this.dialogVisible4 = false
      this.reset()
    },
    /**
     * @description 重置玩家状态
     * @return void
     * */
    reset(){
      request.get("/user"
      ).then(res=>{
        let user=JSON.parse(sessionStorage.getItem("user"))
        let oldgold
        let oldcoin
        for(let data in res.data.records){
          if(res.data.records[data].id===user["id"]){
            oldgold=res.data.records[data].gold
            oldcoin=res.data.records[data].coin
          }
        }
        oldgold = "0"
        oldcoin = "20"
        let data={"id":user["id"],"name":user["name"],"password":user["password"],"gender":user["gender"],"currentroom":"outside","gold":oldgold,"coin":oldcoin}
        request.put("/user",data).then(res => {
          console.log(res)
          if(res.code==="0"){
            this.$message({
              type:"success",
              message:res.msg
            })
          }else {
            this.$message({
              type:"error",
              message:res.msg
            })
          }
          this.load()
        })
      })
      for(let index in this.rooms){
        this.rooms[index].takeflag=0
      }
      this.rooms[8].takeflag=1
      this.rooms[6].takeflag=1
    }
  }
}
</script>


<style scoped>
.a{
  margin: auto;
  position: absolute;
  top: 50px;
  left: 0;
  right: 0;
  bottom: 0;
}
.b{
  margin: auto;
  position: absolute;
  top: 160px;
  left: 400px;
  right: 0;
  bottom: 0;
}
</style>