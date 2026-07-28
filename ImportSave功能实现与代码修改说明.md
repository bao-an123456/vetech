# ImportSave（覆盖保存导入与进度查询）代码变动说明文档

本文档汇总记录了为实现 **ImportSave（覆盖保存/存量更新与增量新增）** 及 **异步进度条反馈** 功能所新增与修改的所有代码文件及具体实现逻辑。

---

## 1. 架构逻辑设计要点

1. **预热与内存路由（替代 Redis 降低宕机风险）**：
   - 在解析 Excel 前，根据 `QYBH` 与 Excel 中的工号/部门编号列表，一次性检索数据库索引关系 `(ID + QYBH + GH/BH)` 加载至内存黑板。
   - 匹配存在的记录 ➔ 赋予原数据库主键 `ID`，打上 `UPDATE` 标记放入待更新队列。
   - 未匹配的新记录 ➔ 生成新 `ID`，打上 `INSERT` 标记放入待新增队列。
2. **分批落库与防 OOM 清空**：
   - 待更新或待新增队列满足 `BATCH_SIZE = 50` 时，分别执行批量插入 `insertBatch` 或批量更新 `updateBatch`。
   - 每次批量落库完成后，强制调用 `list.clear()` 释放内存。
3. **异步解耦与前端百分比进度条**：
   - 后端接口接收文件后在 1 秒内创建任务并生成 `taskId` 立即响应前端。
   - 后台通过异步线程跑数据导入，每完成一批落库实时在 `t_drdc_rw_4849` 表中更新已完成记录数。
   - 前端通过进度查询接口带上 `taskId` 轮询，展示 0% ~ 100% 的进度条。

---

## 2. 代码变动清单汇总

### 模块一：`demo-fccapi`（DTO 实体与 API 接口定义）

#### 1. `VeEmpImportSaveRequest.java` [新增]
- **路径**：`demo-fccapi/src/main/java/cn/vetech/charge/cloud/demo/fccapi/importsave/VeEmpImportSaveRequest.java`
- **功能**：员工覆盖保存导入请求参数（包含 `fileUrl`）。

#### 2. `VeEmpImportSaveResponse.java` [新增]
- **路径**：`demo-fccapi/src/main/java/cn/vetech/charge/cloud/demo/fccapi/importsave/VeEmpImportSaveResponse.java`
- **功能**：员工覆盖保存导入响应参数（包含异步 `taskId` 与提示 `message`）。

#### 3. `VeEmpImportSaveFccApiService.java` [新增]
- **路径**：`demo-fccapi/src/main/java/cn/vetech/charge/cloud/demo/fccapi/importsave/VeEmpImportSaveFccApiService.java`
- **功能**：员工覆盖保存导入 OpenApi 接口契约（URL: `/fccapi/DEMO_B2G_ImportSaveVeEmp`）。

#### 4. `VeDeptImportSaveRequest.java` [新增]
- **路径**：`demo-fccapi/src/main/java/cn/vetech/charge/cloud/demo/fccapi/importsave/VeDeptImportSaveRequest.java`
- **功能**：部门覆盖保存导入请求参数。

#### 5. `VeDeptImportSaveResponse.java` [新增]
- **路径**：`demo-fccapi/src/main/java/cn/vetech/charge/cloud/demo/fccapi/importsave/VeDeptImportSaveResponse.java`
- **功能**：部门覆盖保存导入响应参数。

#### 6. `VeDeptImportSaveFccApiService.java` [新增]
- **路径**：`demo-fccapi/src/main/java/cn/vetech/charge/cloud/demo/fccapi/importsave/VeDeptImportSaveFccApiService.java`
- **功能**：部门覆盖保存导入 OpenApi 接口契约（URL: `/fccapi/DEMO_B2G_ImportSaveVeDept`）。

#### 7. `VeImportProgressRequest.java` [新增]
- **路径**：`demo-fccapi/src/main/java/cn/vetech/charge/cloud/demo/fccapi/importsave/VeImportProgressRequest.java`
- **功能**：导入任务进度查询请求参数（包含 `taskId`）。

#### 8. `VeImportProgressResponse.java` [新增]
- **路径**：`demo-fccapi/src/main/java/cn/vetech/charge/cloud/demo/fccapi/importsave/VeImportProgressResponse.java`
- **功能**：导入任务进度查询响应参数（包含 `status`, `totalCount`, `processedCount`, `percent`, `costTime`）。

#### 9. `VeImportProgressFccApiService.java` [新增]
- **路径**：`demo-fccapi/src/main/java/cn/vetech/charge/cloud/demo/fccapi/importsave/VeImportProgressFccApiService.java`
- **功能**：异步导入进度查询 OpenApi 接口契约（URL: `/fccapi/DEMO_B2G_QueryImportProgress`）。

---

### 模块二：`demo-server`（核心业务引擎与 Mapper）

#### 1. `VeImportSaveService.java` [新增]
- **路径**：`demo-server/src/main/java/cn/vetech/charge/cloud/demo/server/service/VeImportSaveService.java`
- **核心逻辑**：
  - `importSaveEmpAsync`：员工异步 Save 引擎，执行索引预热、路由判定、分批插入/更新、进度累加、`clear()` 防 OOM。
  - `importSaveDeptAsync`：部门异步 Save 引擎。
  - `getTaskProgress`：进度查询，计算百分比（完成时固定返回 100%）。

#### 2. `VeEmpMapper.java` / `VeEmpMapper.xml` [修改]
- **路径**：
  - `demo-server/src/main/java/cn/vetech/charge/cloud/demo/server/mapper/VeEmpMapper.java`
  - `demo-server/src/main/resources/mapper/VeEmpMapper.xml`
- **修改点**：追加 `updateBatch` 批量更新员工方法与 SQL 映射。

#### 3. `VePositionMapper.java` / `VePositionMapper.xml` [修改]
- **路径**：
  - `demo-server/src/main/java/cn/vetech/charge/cloud/demo/server/mapper/VePositionMapper.java`
  - `demo-server/src/main/resources/mapper/VePositionMapper.xml`
- **修改点**：追加 `updateBatch` 批量更新员工任职履历方法与 SQL 映射。

#### 4. `VeDeptMapper.java` / `VeDeptMapper.xml` [修改]
- **路径**：
  - `demo-server/src/main/java/cn/vetech/charge/cloud/demo/server/mapper/VeDeptMapper.java`
  - `demo-server/src/main/resources/mapper/VeDeptMapper.xml`
- **修改点**：追加 `insertBatch` 批量插入部门与 `updateBatch` 批量更新部门方法及 SQL 映射。

---

### 模块三：`demo-rest`（REST Controller 控制器实现）

#### 1. `VeEmpImportSaveImplFccService.java` [新增]
- **路径**：`demo-rest/src/main/java/cn/vetech/charge/cloud/demo/fccapi/veimport/VeEmpImportSaveImplFccService.java`
- **功能**：接收员工 Save 导入请求，立即返回 `taskId`。

#### 2. `VeDeptImportSaveImplFccService.java` [新增]
- **路径**：`demo-rest/src/main/java/cn/vetech/charge/cloud/demo/fccapi/veimport/VeDeptImportSaveImplFccService.java`
- **功能**：接收部门 Save 导入请求，立即返回 `taskId`。

#### 3. `VeImportProgressImplFccService.java` [新增]
- **路径**：`demo-rest/src/main/java/cn/vetech/charge/cloud/demo/fccapi/veimport/VeImportProgressImplFccService.java`
- **功能**：查询导入任务实时进度。

---

## 3. 接口调用说明

1. **发起员工 Save 导入**：
   - **请求 URL**：`/fccapi/DEMO_B2G_ImportSaveVeEmp`
   - **响应**：`{"taskId": "a1b2c3d4...", "message": "文件已接收..."}`
2. **前端轮询进度条**：
   - **请求 URL**：`/fccapi/DEMO_B2G_QueryImportProgress`
   - **请求参数**：`{"taskId": "a1b2c3d4..."}`
   - **响应示例**：
     ```json
     {
       "taskId": "a1b2c3d4...",
       "taskName": "员工覆盖保存(ImportSave)",
       "status": "1",
       "totalCount": 5000,
       "processedCount": 2500,
       "percent": 50,
       "costTime": 1200
     }
     ```
