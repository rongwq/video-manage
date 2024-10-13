<template>
  <el-dialog title="选择广告" :visible.sync="visible" width="800px" top="5vh" append-to-body>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="广告标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入广告标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="adList" highlight-current-row
              @current-change="handleCurrentChange">
      <el-table-column label="广告id" align="center" prop="id" />
      <el-table-column label="广告url" align="center" prop="url" />
      <el-table-column label="广告标题" align="center" prop="title" />
      <el-table-column label="描述" align="center" prop="remark" />
      <el-table-column label="广告类型" align="center" prop="type" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ad_type" :value="scope.row.type"/>
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
  </el-dialog>
</template>

<script>
import { listAd } from "@/api/video/ad";

export default {
  name: "Ad",
  dicts: ['ad_type'],
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
      // 广告表格数据
      adList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        url: null,
        title: null,
        status: null,
        type: null,
      },
      // 表单参数
      form: {},
      visible: false,
      selectedAdId: null,
    }
  },
  created() {
    this.getList();
  },
  methods: {
    handleCurrentChange(val) {
      if(val){
        this.selectedAdId = val.id;
        this.visible = false;
        this.$emit('input', this.selectedAdId);
      }
    },
      // 显示弹框
    show() {
      this.getList();
      this.visible = true;
    },
    /** 查询广告列表 */
    getList() {
      this.loading = true;
      listAd(this.queryParams).then(response => {
        this.adList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        url: null,
        title: null,
        status: null,
        remark: null,
        type: null,
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
    }
  }
};
</script>
