<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true"  v-show="showSearch" label-width="68px">
      <el-form-item label="视频id" prop="videoId">
        <el-input
          v-model="queryParams.videoId"
          placeholder="请输入视频id"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['system:ad:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:ad:edit']"
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
          v-hasPermi="['system:ad:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:ad:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="adList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="广告id" align="center" prop="adId" />
      <el-table-column label="广告url" align="center" prop="adUrl" />
      <el-table-column label="广告标题" align="center" prop="adTitle" />
      <el-table-column label="广告类型" align="center" prop="adType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ad_type" :value="scope.row.adType"/>
        </template>
      </el-table-column>
      <el-table-column label="广告弹出时间/s" align="center" prop="adShowTime" />
      <el-table-column label="广告持续时间/s" align="center" prop="adContinueTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:ad:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:ad:remove']"
          >删除</el-button>
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

    <!-- 添加或修改视频广告关联对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="视频" prop="videoId">
          {{ form.videoId }}
        </el-form-item>
        <el-form-item label="广告" prop="adId">
          <el-input v-model="form.adId" placeholder="请输入广告id" @click.native="openSelectAd"/>
        </el-form-item>
        <el-form-item label="广告播放时间" prop="adShowTime">
          <el-input v-model="form.adShowTime" placeholder="请输入广告在视频播放多久时间后弹出/s" />
        </el-form-item>
        <el-form-item label="广告持续时间" prop="adContinueTime">
          <el-input v-model="form.adContinueTime" placeholder="请输入广告持续显示多久时间后可关闭/s" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <select-ad ref="select" @input="handleAdIdChange" />
  </div>
</template>

<script>
import { listAd, getAd, delAd, addAd, updateAd } from "@/api/video/video_ad";
import selectAd from "./selectAd";

export default {
  name: "videoAd",
  dicts: ['ad_type'],
  components: { selectAd },
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
      showSearch: false,
      // 总条数
      total: 0,
      // 视频广告关联表格数据
      adList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        videoId: null,
        adId: null,
        adShowTime: null,
        adContinueTime: null,
      },
      // 表单参数
      form: {}
    };
  },
  created() {
    const videoId = this.$route.params && this.$route.params.videoId;
    this.queryParams.videoId = videoId;
    this.form.videoId = videoId;
    this.getList();
  },
  methods: {
    /** 查询视频广告关联列表 */
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
        videoId: null,
        adId: null,
        adShowTime: null,
        adContinueTime: null,
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      const videoId = this.$route.params && this.$route.params.videoId;
      this.form.videoId = videoId;
      this.title = "添加视频广告关联";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAd(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改视频广告关联";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAd(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAd(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除视频广告关联编号为"' + ids + '"的数据项？').then(function() {
        return delAd(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/ad/export', {
        ...this.queryParams
      }, `ad_${new Date().getTime()}.xlsx`)
    },
    /** 打开选择广告弹窗 */
    openSelectAd() {
      this.$refs.select.show();
    },
    handleAdIdChange(newAdId) {
      this.form.adId = newAdId;
    }
  }
};
</script>
