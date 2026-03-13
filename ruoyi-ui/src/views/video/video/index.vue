<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="视频标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入视频标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="视频分类" prop="category">
          <el-tree
            class="tree-border"
            :data="categoryOptions"
            show-checkbox
            ref="searchCategory"
            node-key="id"
            :check-strictly="!form.categoryCheckStrictly"
            empty-text="加载中，请稍候"
            :props="defaultProps"
          ></el-tree>
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
          v-hasPermi="['system:video:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:video:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:video:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['system:video:export']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:video:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="videoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="视频url" align="center" prop="url"/>
      <el-table-column label="视频分类" align="center" prop="category"/>
      <el-table-column label="视频标题" align="center" prop="title"/>
      <el-table-column label="播放量" align="center" prop="playNum"/>
      <el-table-column label="点赞量" align="center" prop="likeNum"/>
      <el-table-column label="金币" align="center" prop="money"/>
      <el-table-column label="描述" align="center" prop="remark"/>
      <el-table-column label="视频类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.video_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleAdConf(scope.row)"
            v-hasPermi="['system:video:edit']"
          >广告配置
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:video:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:video:remove']"
          >删除
          </el-button>
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

    <!-- 添加或修改视频对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="视频分类">
          <el-checkbox v-model="categoryExpand" @change="handleCheckedTreeExpand($event)">展开/折叠
          </el-checkbox>
          <el-checkbox v-model="categoryNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选
          </el-checkbox>
          <el-checkbox v-model="form.categoryCheckStrictly" @change="handleCheckedTreeConnect($event)">
            父子联动
          </el-checkbox>
          <el-tree
            class="tree-border"
            :data="categoryOptions"
            show-checkbox
            ref="category"
            node-key="id"
            :check-strictly="!form.categoryCheckStrictly"
            empty-text="加载中，请稍候"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
        <el-form-item label="视频类型" prop="type">
          <el-radio-group v-model="form.type" @change="handleTypeChange">
            <el-radio
              v-for="dict in dict.type.video_type"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="视频" prop="appFile">
          <file-upload v-model="form.url" :limit="1" :fileSize="500" :fileType="['mp4', 'avi', 'rmvb']"/>
        </el-form-item>
        <el-form-item label="视频url" prop="url">
          <el-input v-model="form.url" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="视频图片" prop="appFile">
          <file-upload v-model="form.img" :limit="1" :fileSize="50" :fileType="['jpg','jpeg','bmp','gif','png']"/>
        </el-form-item>
        <el-form-item label="视频标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入视频标题"/>
        </el-form-item>
        <el-form-item label="播放量" prop="playNum">
          <el-input v-model="form.playNum" placeholder="播放量" oninput="value=value.replace(/[^\d]/g,'')"/>
        </el-form-item>
        <el-form-item label="点赞量" prop="likeNum">
          <el-input v-model="form.likeNum" placeholder="点赞量" oninput="value=value.replace(/[^\d]/g,'')"/>
        </el-form-item>
        <el-form-item label="金币" prop="money">
          <el-input v-model="form.money" placeholder="金币" oninput="value=value.replace(/[^\d]/g,'')"/>
        </el-form-item>
        <el-form-item label="描述" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入描述"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的视频数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listVideo, getVideo, delVideo, addVideo, updateVideo } from "@/api/video/video";
import { treeSelect } from "@/api/video/category";
import { getToken } from "@/utils/auth";

export default {
  name: "Video",
  dicts: ['video_type','video_category'],
  data() {
    const validateMoney = (rule, value, callback) => {
      let money = value == null ? 0 : parseInt(value);
      if (isNaN(money)) {
        callback(new Error("金额必须为数字"));
      } else if (money < 0) {
        callback(new Error("金额必须大于等于0"));
      } else if (money > 9999) {
        callback(new Error("金额不能超过9999"));
      } else if (this.form.type === "2") {
        if (money <= 0) {
          callback(new Error("收费视频金额必须大于0"));
        } else {
          callback();
        }
      } else {
        if (money !== 0) {
          callback(new Error("免费视频金额需为0"));
        } else {
          callback();
        }
      }
    };
    const validateNumber = (rule, value, callback) => {
      if (value != null && value !== "" && isNaN(parseInt(value))) {
        callback(new Error("请输入数字"));
      } else {
        callback();
      }
    };
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 菜单列表
      categoryOptions: [],
      categoryExpand: false,
      categoryNodeAll: false,
      categoryCheckStrictly: true,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 视频表格数据
      videoList: [],
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
        category: null,
        status: null,
        type: null,
      },
       // 用户导入参数
      upload: {
        // 是否显示弹出层（视频导入）
        open: false,
        // 弹出层标题（视频导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 1,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/video/importData"
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        title: [
          { required: true, message: "视频标题不能为空", trigger: "blur" },
          { min: 1, max: 100, message: "标题长度需在1-100字符之间", trigger: "blur" },
          { pattern: /^[\u4e00-\u9fa5a-zA-Z0-9_-]+$/, message: "标题包含非法特殊字符，仅允许中文、英文、数字、-_", trigger: "blur" }
        ],
        url: [
          { required: true, message: "视频URL不能为空", trigger: "blur" },
          { max: 500, message: "视频URL长度不能超过500字符", trigger: "blur" },
          { pattern: /^(http|https):\/\/.*\.(mp4|avi|mov|rmvb|flv|wmv)$/, message: "视频URL格式错误，仅支持mp4/avi/mov等格式", trigger: "blur" }
        ],
        money: [
          { validator: validateMoney, trigger: "blur" }
        ],
        playNum: [
          { validator: validateNumber, trigger: "blur" }
        ],
        likeNum: [
          { validator: validateNumber, trigger: "blur" }
        ],
        type: [
          { required: true, message: "请选择视频类型", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getTreeSelect();
  },
  methods: {
    getTreeSelect() {
      treeSelect().then(response => {
        this.categoryOptions = response.data;
      });
    },
    handleTypeChange() {
      this.$nextTick(() => {
        this.$refs["form"].validateField("money");
      });
    },
    trimFormFields() {
      if (this.form.title) {
        this.form.title = this.form.title.trim();
      }
      if (this.form.url) {
        this.form.url = this.form.url.trim();
      }
    },
    // 所有菜单节点数据-查询条件
    getAllCheckedKeysSearch() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.searchCategory.getCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.searchCategory.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
        // 所有菜单节点数据
    getAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.category.getCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.category.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    /** 查询视频列表 */
    getList() {
      this.loading = true;
      listVideo(this.queryParams).then(response => {
        this.videoList = response.rows;
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
        category: null,
        status: null,
        remark: null,
        type: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        categoryExpand: false,
        categoryNodeAll: false
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.category = this.getAllCheckedKeysSearch().join(",");
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
      this.getTreeSelect();
      this.open = true;
      this.title = "添加视频";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeSelect();
      const id = row.id || this.ids
      getVideo(id).then(response => {
        this.form = response.data;
        this.open = true;
        let checkedKeys = response.data.category.split(",");
          this.categoryOptions.forEach(res => {
           checkedKeys.forEach((v) => {
              if(res.id == v){
                this.$nextTick(()=>{
                    this.$refs.category.setChecked(v, true ,false);
                })
              }
            })
          let children = res.children;
           children.forEach((c) => {
            checkedKeys.forEach((v) => {
              if(c.id == v){
                this.$nextTick(()=>{
                    this.$refs.category.setChecked(v, true ,false);
                })
              }
            })
           })
        });
        this.title = "修改视频";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.trimFormFields();
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.form.category = this.getAllCheckedKeys().join(",");
            updateVideo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.category = this.getAllCheckedKeys().join(",");
            addVideo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除视频编号为"' + ids + '"的数据项？').then(function() {
        return delVideo(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "视频导入";
      this.upload.open = true;
    },
     /** 下载模板操作 */
    importTemplate() {
      this.download('system/video/importTemplate', {
      }, `video_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/video/export', {
        ...this.queryParams
      }, `video_${new Date().getTime()}.xlsx`)
    },
    handleAdConf(row){
      const videoId = row.id;
      this.$router.push("/video/ad-data/index/" + videoId);
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value) {
        let treeList = this.categoryOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.category.store.nodesMap[treeList[i].id].expanded = value;
        }
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value) {
        this.$refs.category.setCheckedNodes(value ? this.categoryOptions: []);
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value) {
        this.form.categoryCheckStrictly = value ? true: false;
    }
  }
};


</script>
