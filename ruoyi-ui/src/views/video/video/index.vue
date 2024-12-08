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
          <el-radio-group v-model="form.type">
            <el-radio
              v-for="dict in dict.type.video_type"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="视频" prop="appFile">
          <file-upload v-model="form.url" limit="1" fileSize="500" :fileType="['mp4', 'avi', 'rmvb']"/>
        </el-form-item>
        <el-form-item label="视频url" prop="url">
          <el-input v-model="form.url" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="视频图片" prop="appFile">
          <file-upload v-model="form.img" limit="1" fileSize="50" :fileType="['jpg','jpeg','bmp','gif','png']"/>
        </el-form-item>
        <el-form-item label="视频标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入视频标题"/>
        </el-form-item>
        <el-form-item label="播放量" prop="playNum">
          <el-input v-model="form.playNum" placeholder="播放量"/>
        </el-form-item>
        <el-form-item label="点赞量" prop="likeNum">
          <el-input v-model="form.likeNum" placeholder="点赞量"/>
        </el-form-item>
        <el-form-item label="金币" prop="money">
          <el-input v-model="form.money" placeholder="金币"/>
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
  </div>
</template>

<script>
import { listVideo, getVideo, delVideo, addVideo, updateVideo } from "@/api/video/video";
import { treeSelect } from "@/api/video/category";

export default {
  name: "Video",
  dicts: ['video_type','video_category'],
  data() {
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
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
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
      const categoryMenu = this.getTreeSelect();
      alert(categoryMenu)
      const id = row.id || this.ids
      getVideo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.$nextTick(() => {
          categoryMenu.then(res => {
            let checkedKeys = res.checkedKeys
            checkedKeys.forEach((v) => {
                this.$nextTick(()=>{
                    this.$refs.menu.setChecked(v, true ,false);
                })
            })
          });
        });
        this.title = "修改视频";
      });
    },
    /** 提交按钮 */
    submitForm() {
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
