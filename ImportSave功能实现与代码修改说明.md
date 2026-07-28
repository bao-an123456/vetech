# ImportSave（覆盖保存导入与自动弹出进度框面板）说明文档

本文档汇总记录了为实现 **ImportSave（覆盖保存/存量更新与增量新增）** 及 **自动弹出进度框（Progress Dialog Modal）** 功能所新增与修改的所有代码文件及具体实现逻辑。

---

## 1. 智能自动弹出进度框设计

1. **自动弹出进程框（无需手动调 Progress 接口）**：
   - 提供了全新的交互式可视化面板 `import_progress_ui.html`。
   - 当用户在前端点击 **“一键开启覆盖保存导入”** 时，前端 UI **自动弹出极具现代感（暗黑磨砂玻璃风）的进程框 Modal**。
   - 进程框自动绑定请求返回的 `taskId`，内部自动进行高频平滑进度追踪，无需人工干预或手动请求接口。

2. **预热与内存路由（替代 Redis 降低宕机风险）**：
   - 在解析 Excel 前，根据 `QYBH` 与 Excel 中的工号/部门编号列表，一次性检索数据库索引关系 `(ID + QYBH + GH/BH)` 加载至内存黑板。
   - 匹配存在的记录 ➔ 赋予原数据库主键 `ID`，打上 `UPDATE` 标记放入待更新队列。
   - 未匹配的新记录 ➔ 生成新 `ID`，打上 `INSERT` 标记放入待新增队列。

3. **分批落库与防 OOM 清空**：
   - 待更新或待新增队列满足 `BATCH_SIZE = 50` 时，分别执行批量插入 `insertBatch` 或批量更新 `updateBatch`。
   - 每次批量落库完成后，强制调用 `list.clear()` 释放内存。

---

## 2. 界面与代码变动清单汇总

### 模块一：`前端智能自动弹出进度框 UI` [新增]

#### 1. `import_progress_ui.html`
- **路径**：`d:\Desktop\javademo\import_progress_ui.html` 及 `demo-rest/src/main/resources/static/import_progress_ui.html`
- **特点**：
  - 支持员工与部门标签页切换。
  - **自动弹出进程框**：包含 0%~100% 渐变 Glow 进度条、实时条数 `2500 / 5000` 统计、耗时毫秒数与自动完成打卡。

---

### 模块二：`demo-fccapi`（DTO 实体与 API 接口定义）

1. **`VeEmpImportSaveRequest.java` / `VeEmpImportSaveResponse.java` / `VeEmpImportSaveFccApiService.java`**：员工覆盖保存导入接口。
2. **`VeDeptImportSaveRequest.java` / `VeDeptImportSaveResponse.java` / `VeDeptImportSaveFccApiService.java`**：部门覆盖保存导入接口。
3. **`VeImportProgressRequest.java` / `VeImportProgressResponse.java` / `VeImportProgressFccApiService.java`**：导入任务进度自动查询接口。

---

### 模块三：`demo-server`（核心业务引擎与 Mapper）

1. **`VeImportSaveService.java`**：覆盖保存导入服务引擎（预热索引 + 内存路由 + 分批 `clear()` 防 OOM + 实时进度更新）。
2. **`VeEmpMapper.java` / `VeEmpMapper.xml`**：`updateBatch` 批量更新员工。
3. **`VePositionMapper.java` / `VePositionMapper.xml`**：`updateBatch` 批量更新员工岗位履历。
4. **`VeDeptMapper.java` / `VeDeptMapper.xml`**：`insertBatch` 批量新增部门与 `updateBatch` 批量更新部门。

---

### 模块四：`demo-rest`（REST Controller 控制器实现）

1. **`VeEmpImportSaveImplFccService.java`**：`/fccapi/DEMO_B2G_ImportSaveVeEmp`
2. **`VeDeptImportSaveImplFccService.java`**：`/fccapi/DEMO_B2G_ImportSaveVeDept`
3. **`VeImportProgressImplFccService.java`**：`/fccapi/DEMO_B2G_QueryImportProgress`

---

## 3. 使用方法

直接双击打开本地 [import_progress_ui.html](file:///d:/Desktop/javademo/import_progress_ui.html) 或启动项目后访问 `http://localhost:8080/import_progress_ui.html`：
1. 选择“员工数据覆盖保存”或“部门数据覆盖保存”。
2. 点击 **“一键开启覆盖保存导入”**。
3. **进程框将自动弹窗**，动态展示实时百分比进度条与完成状态，无需手动调用任何接口！
