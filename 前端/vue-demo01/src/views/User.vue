<template>
  <div>
    <Header />
  </div>

  <div style="padding: 10px">
    <div style="margin: 10px 0 ;display: flex">
      <el-button type="primary" @click="adduser">新增用户</el-button>
    </div>
   <el-table :data="tableData" stripe style="width: 100%" border>
     <el-table-column prop="id" label="ID" width="170" sortable/>
     <el-table-column prop="name" label="用户名" width="220" />
     <el-table-column prop="currentroom" label="当前房间" width="220" />
     <el-table-column prop="gold" label="金币" width="250" />
     <el-table-column prop="coin" label="硬币" width="250" />
     <el-table-column fixed="right" label="操作" width="350">
       <template #default="scope">
         <el-button type="text" size="small" @click="handleEdit(scope.row)"
         >修改</el-button
         >
<!--         <el-button type="text" size="small" @click="handleEdit2(scope.row)"-->
<!--         >编辑</el-button-->
<!--         >-->
         <el-popconfirm title="确定删除吗?" @confirm="handleDelete(scope.row.id)">
           <template #reference>
             <el-button type="text" size="small">删除</el-button>
           </template>
         </el-popconfirm>
       </template>
     </el-table-column>
   </el-table>
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]"
          :small="0"
          :disabled="false"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog
        v-model="dialogVisible"
        title="修改"
        width="30%"
        @close="closeAddDialog"
    >
      <el-form :model="form" label-width="120px">
        <el-form-item label="用户名" style="width: 80%">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="当前位置" style="width: 80%">
          <el-input v-model="form.currentroom" />
        </el-form-item>
        <el-form-item label="密码" style="width: 80%">
          <el-input v-model="form.password" />
        </el-form-item>
        <el-form-item label="权限" style="width: 80%">
          <el-input v-model="form.level" />
        </el-form-item>
        <el-form-item label="金币" style="width: 80%">
          <el-input v-model="form.gold" />
        </el-form-item>
        <el-form-item label="体力" style="width: 80%">
          <el-input v-model="form.coin" />
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeAddDialog">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
      </template>
    </el-dialog>
    <el-dialog
        v-model="dialogVisible2"
        title="新增用户"
        width="30%"
    ><el-form :model="form" label-width="120px">
      <el-form-item label="用户名" style="width: 80%">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="密码" style="width: 80%">
        <el-input v-model="form.password" />
      </el-form-item>
      <el-form-item label="当前位置" style="width: 80%">
        <el-input v-model="form.currentroom" />
      </el-form-item>
      <el-form-item label="金币" style="width: 80%">
        <el-input v-model="form.gold" />
      </el-form-item>
      <el-form-item label="体力" style="width: 80%">
        <el-input v-model="form.coin" />
      </el-form-item>
    </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取消</el-button>
        <el-button type="primary" @click="save"
        >确定</el-button
        >
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import Header from "@/components/Header";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "User",
  components: {Header},
  data(){
    return{
      form:{},
      dialogVisible:false, //修改用户信息窗口
      dialogVisible2:false, //添加新用户窗口
      currentPage:1, //当前页面
      pageSize:10, //页面大小
      total:10,
      search:'',
      tableData:[]
    }
  },
  created() {
    this.load()
  },
  methods:{
    /**
     * @description 加载页面信息
     * @return void
     * */
    load(){
      request.get("/user?pageNum="+this.currentPage+"&pageSize="+this.pageSize+"&search="+this.search,

        // eslint-disable-next-line no-unused-vars
      ).then(res=>{
        this.tableData=res.data.records
        this.total=res.data.records.length
      })
    },
    /**
     * @description 修改用户信息的窗口
     * @return void
     * */
    add(){
        this.dialogVisible=true
        this.form={}
    },
    /**
     * @description 添加新用户
     * @return void
     * */
    adduser(){
      this.dialogVisible2=true
    },
    /**
     * @description 清楚对话框的数据
     * @return void
     * */
    closeAddDialog() {
      this.dialogVisible = false   //关闭对话框
      // this.$refs.addRoleForm.resetFields();
      this.form = this.$options.data().form
    },
    /**
     * @description 保存用户信息
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
      }else{
        request.post("/user",this.form).then(res => {
          console.log(res)
          if(res.code==="0"){
            this.$message({
              type:"success",
              message:"新增成功"
            })
          }else {
            this.$message({
              type:"error",
              message:res.msg
            })
          }
          this.load()
          this.form = this.$options.data().form
          this.dialogVisible2=false
        })
      }
    },
    /**
     * @description 修改用户信息
     * @return void
     * */
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    /**
     * @description 删除用户
     * @return void
     * */
    handleDelete(id){
      request.delete("/user/"+id).then(res=>{
        if(res.code==="0"){
          this.$message({
            type:"success",
            message:"删除成功"
          })
        }else {
          this.$message({
            type:"error",
            message:res.msg
          })
        }
        this.load()
      })
    },
    handleSizeChange(pageSize){
      this.pageSize=pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.currentPage=pageNum
      this.load()
    }
  }

}
</script>

<style scoped>

</style>