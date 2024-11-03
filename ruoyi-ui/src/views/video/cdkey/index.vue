<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="激活码编码" prop="cdkeyCode">
        <el-input
          v-model="queryParams.cdkeyCode"
          placeholder="请输入激活码编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="绑定用户" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入绑定用户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:cdkey:add']"
        >新增</el-button>
      </el-col>
      <!--<el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:cdkey:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:cdkey:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:cdkey:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cdkeyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="10" align="center" />
      <el-table-column label="激活码ID" align="center" prop="cdkeyId" />
      <el-table-column label="激活码编码" align="center" prop="cdkeyCode" width="300"/>
      <el-table-column label="绑定用户" align="center" prop="userName" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.cdkey_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--<el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:cdkey:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:cdkey:remove']"
          >删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加激活码对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="生成数量" prop="createNumber">
          <el-input-number v-model="form.createNumber" placeholder="请输入生成数量"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 修改激活码对话框 -->
    <el-dialog :title="title" :visible.sync="editOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="激活码编码" prop="cdkeyCode">
          <el-input v-model="form.cdkeyCode" placeholder="请输入激活码编码" />
        </el-form-item>
        <el-form-item label="绑定用户" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入绑定用户" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCdkey, getCdkey, delCdkey, addCdkey, updateCdkey } from "@/api/video/cdkey";

export default {
  dicts: ['cdkey_status'],
  name: "Cdkey",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 激活码表格数据
      cdkeyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层-新增
      open: false,
      // 是否显示弹出层-编辑
      editOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cdkeyCode: null,
        userId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        createNumber: [{
          required: true,
          message: '请输入生成数量',
          trigger: 'blur'
        }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询激活码列表 */
    getList() {
      this.loading = true;
      listCdkey(this.queryParams).then(response => {
        this.cdkeyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.editOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        cdkeyId: null,
        cdkeyCode: null,
        userId: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.cdkeyId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加激活码";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const cdkeyId = row.cdkeyId || this.ids
      getCdkey(cdkeyId).then(response => {
        this.form = response.data;
        this.editOpen = true;
        this.title = "修改激活码";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.cdkeyId != null) {
            updateCdkey(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.editOpen = false;
              this.getList();
            });
          } else {
            addCdkey(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const cdkeyIds = row.cdkeyId || this.ids;
      this.$modal.confirm('是否确认删除激活码编号为"' + cdkeyIds + '"的数据项？').then(function() {
        return delCdkey(cdkeyIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/cdkey/export', {
        ...this.queryParams
      }, `cdkey_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
