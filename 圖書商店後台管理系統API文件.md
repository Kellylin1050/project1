# 圖書商店後台管理系統(Restful API)

## 系統簡介
系統主要由 RESTful API 組成，支持用戶認證、權限管理、員工資料管理和書本管理等功能。系統採用 JWT 驗證，用於保護 API，並使用 Swagger 生成的文檔來幫助開發者了解和測試 API。

## 基本訊息
API Base URL: /v3/api-docs

Content-Type: application/json

認證方式: JWT（JSON Web Token）

授權機制: 基於角色的權限控制

## 主要功能

### 使用者管理API
1. 使用者註冊登入後，會得到 user 身分，即有權限可以更新個人資料，登出功能
1. 管理員（admin）可以查詢、新增、修改、刪除所有使用者資料
1. 註冊、登入、忘記密碼則是沒有權限限制
### 書籍管理API
只有管理員可以對書本新增、修改、刪除，一般使用者只能查詢書本


### JWT token
當使用者成功登入後可以獲得JWT token，過期時間預設為一小時

Token過期可以使用refreshToken來刷新Token，過期時間預設為7天

### API測試
建立controller單元測試、集成測試
使用swagger產生API文件，方便快速了解及測試API

## 系統架構設計
### 環境設置
- 編輯器：Intellij
- 開發語言：JAVA 17
- 使用框架：Springboot 3.2.5
- Project管理工具：Maven
- 版控工具：Git、sourceTree介面化工具
- 資料庫：MariaDB
### 系統組成
- **API 層**:
  - **Controller**: 處理 HTTP 請求
  - **路由**: 定義 API 路徑和請求方法
  - **DTO**: 請求和響應數據結構

- **業務邏輯層**:
  - **Service**: 業務邏輯處理

- **數據訪問層**:
  - **Repository**: 資料庫操作
  - **Spring Data JPA**: JPA 數據訪問

- **身份驗證和授權**:
  - **Spring Security**: 身份驗證和授權
  - **JWT**: 令牌處理

- **配置和管理**:
  - **配置文件**: `application.properties`, Config
  - **環境配置**: 開發、測試、生產環境

### 系統設計
- **資料庫設計**: 表結構、字段、關聯
- **API 設計**: 路徑、HTTP 方法、錯誤處理

### 測試和部署
- **測試**: 單元測試、集成測試
- **部署**: Docker

### 文檔和維護
- **API 文檔**: Swagger
- **維護計劃**: 定期檢查和更新

### MariaDB資料庫(設計、關聯、索引)
### 資料表設計

#### 書本表 (`book`)

| 參數名稱    | 必要 | 資料型態  | 參數敘述  |
| ----------- | ---- | --------- | --------- |
| id          | Y    | INT(11)    | 主鍵&書籍ID      |
| title       | Y    | String(255) | 書名      |
| author      | Y    | String(255) | 作者      |
| description | N    | String(500) | 描述      |
| price       | Y    | Decimal    | 定價      |
| sellPrice   | Y    | Decimal    | 售價      |


#### 員工表 (`user`)

| 參數名稱    | 必要 | 資料型態  | 參數敘述     |
| ----------- | ---- | --------- | ----------- |
| id          | Y    | INT(11)    | 主鍵&使用者ID        |
| username    | Y    | String(255) | 唯一&使用者名稱   |
| password    | Y    | String(255) | 密碼         |
| name        | Y    | String(255) | 姓名         |
| phone       | N    | String(20)  | 電話         |
| email       | Y    | String(255) | 唯一&電子郵件     |


#### 角色表 (`role`)
| 參數名稱    | 必要 | 資料型態  | 參數敘述  |
| ----------- | ---- | --------- | --------- |
| role_id     | Y    | INT(11)    | 主鍵&角色id      |
| name        | Y    | String(255) | 角色名稱  |


#### 員工角色表 (`user_role`)
| 參數名稱    | 必要 | 資料型態  | 參數敘述  |
| ----------- | ---- | --------- | --------- |
| user_role_id| Y    | INT(11)    | 主鍵&使用者角色ID      |
| user_id     | Y    | INT(11)   | 使用者ID    |
| role_id     | Y    | INT(11)    | 角色ID    |


### 資料表關聯
- **員工表** 和 **角色表** 之間存在多對多的關聯，通過 **user_role** 中介表實現。

### 索引
- **user** 表上的索引:
  - `username`（唯一索引）
  - `email`（唯一索引）


### Docker 
**安裝docker**

`$ docker build -t <image_name> .`：從 Dockerfile 建立 image。

`$ docker run -dp <host_port>:<container_port> --name <container_name> --env-file ./.env <image_name>`：

從 image 建立 container

**打包成映像檔**

`$ docker tag local-image:tagname new-repo:tagname .` 將我們現有的image標記成新的repo

`$ docker tag local-image:tagname new-repo:tagname .`使用docker push推送到hub

docker hub 連結 : https://hub.docker.com/repository/docker/kellylin1050/project1

### 使用工具
| 套件                | 敘述                              |
| ------------------- | --------------------------------- |
| lombok              | 註解自動生成getter&setter       |
| junit               | 測試框架                       |
| jjwt-api            | 所有的API接口，用於創建&解析API   |
| jjwt-impl           | API的具體實現，提供jwt操作功能    |
| jjwt-jackson        | 使用jackson來處理jwt的功        |
| mariadb-java-client | mariadb的jdbc驅動，用於連接資料庫 |
| HiKaricp            | 管理數據庫連接，提高性能          |
| mybatis             | 簡化mybatis配置                |
| swagger-ui          | 產生UI介面，提供API文件及測試     |
| Docker              | 打包成映像檔                   |

## API說明

### [使用者 User](#User)


| Description                       | Method | Path                   | ROLE       |
| --------------------------------- | ------ | ---------------------- | ---------- |
| [註冊使用者](#註冊使用者)         | POST   | /users/register        |            |
| [使用者登入](#使用者登入)         | POST   | /users/login           |            |
| [使用者登出](#使用者登出)         | POST   | /users/logout          | ADMIN&USER |
| [查詢所有使用者](#查詢所有使用者) | GET    | /users/doFindAllUsers  | ADMIN      |
| [查詢指定使用者](#查詢指定使用者) | GET    | /users/user            | ADMIN      |
| [更新使用者](#更新使用者)         | POST   | /users/updateUser      | ADMIN&USER |
| [新增使用者](#新增使用者)         | POST   | /users/doSaveUser      | ADMIN      |
| [刪除使用者](#刪除使用者)         | POST   | /users/deleteUser/{id} | ADMIN      |
| [忘記密碼](#忘記密碼)             | POST   | /users/forgetpassword  |            |
| [更新Token](#更新Token)|POST |/users/refreshToken|            |
| [403錯誤頁面](#錯誤頁面)          |        | /users/403             |            |

### [管理書籍 BOOK](#Book)


| Description                   | Method | Path                     | ROLE  |
| ----------------------------- | ------ | ------------------------ | ----- |
| [查詢書籍](#查詢書籍)         | GET    | /NewBook/book            |       |
| [更新書的訊息](#更新書的訊息) | POST   | /NewBook/doUpdateNewBook | ADMIN |
| [新增書籍](#新增書籍)         | POST   | /NewBook/doSaveNewBook   | ADMIN |
| [刪除書籍](#刪除書籍)         | POST   | /NewBook/delete/{id}     | ADMIN |
| [403錯誤頁面](#錯誤頁面)      |        | /NewBook/403             |       |

## API Details

Request 和 Response 的範例

### User
#### 註冊使用者
Request Example


| 參數名稱 | 必要 | 資料型態    | 參數敘述    |
| -------- | ---- | ----------- | --- |
| name     | Y    | String(255) | 姓名    |
| email    | Y    | String(255) | 信箱    |
| password | Y    | String(255) | 密碼    |
| username | Y    | String(255) | 使用者名稱|
| roles    | N    | String(255) | 角色    |

```json=
{
  "name": "Sandra",
  "email": "Sandra_1849@gmail.com",
  "password": "djfi937v7",
  "username": "Sandra_lin",
  "roles": [
    "string"
  ]
}
```

Response Example
```json=
使用者 Sandra_lin 註冊成功
```
**API回應說明**
| HTTP status | 中文敘述 |
| ----------- | -------- |
| 201         | 使用者註冊成功 |
| 400         | 註冊請求有誤|

#### 使用者登入
Request Example
| 參數名稱 | 必要 | 資料型態    | 參數敘述    |
| -------- | ---- | ----------- | --- |
| password | Y    | String(255) | 密碼    |
| username | Y    | String(255) | 使用者名稱|

```json=
{
  "username": "admin",
  "password": "admin"
}
```
Response Example
| 參數名稱 | 必要 | 資料型態    | 參數敘述    |
| -------- | ---- | ----------- | --- |
| token | Y    | String(255) | jwt token   |
| loginSuccessful | Y    | String(255) |登入成功訊息|
```json=
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNDEzNjMwNiwiZXhwIjoxNzI0MTM5OTA2fQ.yD7-EVhBCUxEPKWow7tfQR3kH945WxPQ57eLd2anRgU",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNDEzNjMwNiwiZXhwIjoxNzI0NzQxMTA2fQ.PYMC1Q5las66ppi5gmFzHO7VOgIm_i4pYUZZXmRR9v8",
  "loginSuccessful": "login Successful"
}
```
**API回應說明**


| HTTP status | 中文敘述 |
| ----------- | -------- |
| 200         | 登入成功 |
| 400         | 使用者名稱或密碼錯誤|


#### 使用者登出
使當前JWT token 無效
Response Example
```json=
Successfully logged out
```
**API回應說明**


| HTTP status | 中文敘述 |
| ----------- | -------- |
| 200         | 登出成功 |
| 400         | 無效的認證令牌|


#### 查詢所有使用者
Response Example

| 參數名稱 | 必要 | 資料型態    | 參數敘述    |
| -------- | ---- | ----------- | --- |
| name     | Y    | String(255) | 姓名    |
| email    | Y    | String(255) | 信箱    |
| username | Y    | String(255) | 使用者名稱|
| roles    | Y    | String(255) | 角色    |
```json=
[
  {
    "name": "2349808",
    "email": "save1234@gmail.com",
    "username": "dsadasd",
    "roles": []
  },
  {
    "name": "fjdig",
    "email": "test12@gmail.com",
    "username": "fjdig",
    "roles": [
      "ROLE_USER"
    ]
  },
  {
    "name": "qqq",
    "email": "test50@gmail.com",
    "username": "qqq",
    "roles": [
      "ROLE_USER"
    ]
  },
  {
    "name": "aaatgs",
    "email": "test10@gmail.com",
    "username": "aaatgs",
    "roles": [
      "ROLE_USER"
    ]
  },
  {
    "name": "john",
    "email": "john.doe@example.com",
    "username": "john",
    "roles": [
      "ROLE_USER"
    ]
  },
  {
    "name": "xxx",
    "email": "test40@gmail.com",
    "username": "xxx",
    "roles": [
      "ROLE_USER"
    ]
  },
  {
    "name": "admin",
    "email": "admin@gmail.com",
    "username": "admin",
    "roles": [
      "ROLE_ADMIN"
    ]
  },
  {
    "name": "mmm",
    "email": "test54@gmail.com",
    "username": "mmm",
    "roles": [
      "ROLE_USER"
    ]
  },
  {
    "name": "Sandra",
    "email": "Sandra_1849@gmail.com",
    "username": "Sandra_lin",
    "roles": [
      "ROLE_USER"
    ]
  }
]
```
**API回應說明**
| HTTP status | 中文敘述 |
| ----------- | -------- |
| 200         | 成功返回所有使用者 |
| 404         | 未找到使用者|

#### 查詢指定使用者

Request Example
| 參數名稱 | 必要 | 資料型態    | 參數敘述    |
| -------- | ---- | ----------- | --- |
| username | Y    | String(255) | 使用者名稱|

輸入Parameters:

| Name     | Description            |
|----------|------------------------|
| username * | 要查詢的使用者名稱     |

**Example**: `Sandra_lin`

Response Example
| 參數名稱 | 必要 | 資料型態    | 參數敘述    |
| -------- | ---- | ----------- | --- |
| name     | Y    | String(255) | 姓名    |
| email    | Y    | String(255) | 信箱    |
| username | Y    | String(255) | 使用者名稱|
| roles    | Y    | String(255) | 角色    |

```json=
{
  "name": "Sandra",
  "email": "Sandra_1849@gmail.com",
  "username": "Sandra_lin",
  "roles": [
    "ROLE_USER"
  ]
}
```
**API回應說明**
| HTTP status | 中文敘述       |
| ----------- | -------------- |
| 200         | 成功返回使用者 |
| 404         | 使用者未找到   |
| 500         | 伺服器錯誤     |
#### 更新使用者
Request Example

| 參數名稱 | 必要 | 資料型態    | 參數敘述   |
| -------- | ---- | ----------- | ---------- |
| id       | Y    | INT(11)     | 使用者ID    |
| name     | Y    | String(255) | 姓名       |
| phone    | Y    | String(255) | 密碼       |
| username | Y    | String(255) | 使用者名稱 |


```json=
{
  "id": 13,
  "name": "Diana",
  "phone": "0947382613",
  "username": "Diana_liu"
}
```
Response Example

```json=
USER Diana updated successfully
```
**API回應說明**
| HTTP status | 中文敘述       |
| ----------- | -------------- |
| 200         | 使用者更新成功 |
| 404         | 使用者未找到   |
| 400         | 更新失敗     |
#### 新增使用者
Request Example

| 參數名稱 | 必要 | 資料型態    | 參數敘述   |
| -------- | ---- | ----------- | ---------- |
| id       | Y    | INT(11)     | 使用者ID    |
| name     | Y    | String(255) | 姓名       |
| email    | Y    | String(255) | 信箱       |
| password | Y    | String(255) | 密碼       |
| username | Y    | String(255) | 使用者名稱 |
| roles    | N    | String(255) | 角色       |

```json=
{
  "id": 1,
  "username": "William_chou",
  "password": "4730jihfuiw",
  "name": "William",
  "phone": "0947382645",
  "email": "William38201@gmail.com",
  "enabled": true,
  "roles": [
    {
      "id": 1,
      "name": "ADMIN"
    }
  ]
}
```
Response Example
| 參數名稱 | 必要 | 資料型態    | 參數敘述   |
| -------- | ---- | ----------- | ---------- |
| message  | Y    | String(255) | 回應訊息   |
| id       | Y    | INT(11)     | 使用者ID   |
| username | Y    | String(255) | 使用者名稱 |
| password | Y    | String(255) | 密碼       |
| name     | Y    | String(255) | 姓名       |
| phone    | Y    | String(255) | 電話       |
| email    | Y    | String(255) | 信箱       |
| roles    | Y    | String(255) | 角色       |
```json=
{
  "message": "使用者 William_chou 創建成功",
  "user": {
    "id": 33,
    "username": "William_chou",
    "password": "4730jihfuiw",
    "name": "William",
    "phone": "0947382645",
    "email": "William38201@gmail.com",
    "enabled": true,
    "roles": [
      {
        "id": 1,
        "name": "ADMIN"
      }
    ]
  }
}
```
**API回應說明**
| HTTP status | 中文敘述       |
| ----------- | -------------- |
| 201         | 使用者創建成功 |
| 500         | 伺服器錯誤    |
#### 刪除使用者

Request Example
| 參數名稱 | 必要 | 資料型態 | 參數敘述 |
| -------- | ---- | -------- | -------- |
| ID       | Y    | INT(11)  | 使用者ID |


輸入Parameters:

| Name     | Description            |
|----------|------------------------|
| ID *     | 要刪除的使用者ID     |

**Example**: `19`

Response Example

```json=
User with ID 19 deleted successfully
```
**API回應說明**
| HTTP status | 中文敘述       |
| ----------- | -------------- |
| 200         | 使用者刪除成功 |
| 404         | 使用者未找到   |

#### 更新Token

Request Example
| 參數名稱     | 必要 | 資料型態    | 參數敘述  |
| ------------ | ---- | ----------- | --------- |
| refreshToken | Y    | String(255) | 登入時獲得的更新Token |
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNDEzNjMwNiwiZXhwIjoxNzI0NzQxMTA2fQ.PYMC1Q5las66ppi5gmFzHO7VOgIm_i4pYUZZXmRR9v8"
}
```
Response Example
| 參數名稱     | 必要 | 資料型態    | 參數敘述              |
| ------------ | ---- | ----------- | --------------------- |
| accessToken  | Y    | String(255) | 新的有效Token        |
| refreshToken | Y    | String(255) | 獲得的更新Token |

```json=
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNDEzNjM0MiwiZXhwIjoxNzI0MTM5OTQyfQ._GYChNt3DGc1vev-v5gLP5JK_FzTD3-e09zsYJvJP1c",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNDEzNjM0MiwiZXhwIjoxNzI0NzQxMTQyfQ.cYDH8FecKli1tl5d2leTgVSExxp2WPiwVRz7Sp-oFdM"
}
```
**API回應說明**
| HTTP status | 中文敘述      |
| ----------- | ------------- |
| 200         | 成功更新Token |
| 400         | 缺少Token     |
| 403         | 無效的Token   |
| 500         | 伺服器錯誤    |

#### 忘記密碼
重新設定密碼
| 參數名稱 | 必要 | 資料型態    | 參數敘述   |
| -------- | ---- | ----------- | ---------- |
| id       | Y    | INT(11)     | 使用者ID    |
| password | Y    | String(255) | 密碼       |

Request Example
```json=
{
  "id": 10,
  "password": "123456"
}
```
Response Example

```json=
Password reset successfully
```
**API回應說明**
| HTTP status | 中文敘述       |
| ----------- | -------------- |
| 200         | 密碼重製成功 |
| 400         | 使用者未找到或重置密碼失敗   |
### Book

#### 查詢書籍
Request Example
| 參數名稱 | 必要 | 資料型態 | 參數敘述 |
| -------- | ---- | -------- | -------- |
| title    | Y    | String(255)  | 書名 |


輸入Parameters:

| Name     | Description            |
|----------|------------------------|
| title *     | 要查詢的書名     |

**Example**: `Java Programming`

Response Example

```json=
Successfully found the book : Java Programming
```
**API回應說明**
| HTTP status | 中文敘述       |
| ----------- | -------------- |
| 200         | 成功返回書名 |
| 404         | 書籍未找到   |
| 500         | 伺服器錯誤     |
#### 更新書的訊息
Request Example
| 參數名稱    | 必要 | 資料型態    | 參數敘述 |
| ----------- | ---- | ----------- | -------- |
| id          | Y    | INT(11)     | 書籍ID   |
| title       | Y    | String(255) | 書名     |
| author      | Y    | String(255) | 作者     |
| description | Y    | String(255) | 書籍介紹 |
| price       | Y    | INT(11)     | 定價     |
| sellprice   | Y    | INT(11)     | 售價     |

```json=
{
  "title": "Java Programming",
  "author": "John Doe",
  "description": "一本關於java的指南",
  "price": 500,
  "sellprice": 520,
  "id": 5
}
```
Response Example

```json=
Book updated successfully
```
**API回應說明**
| HTTP status | 中文敘述       |
| ----------- | -------------- |
| 200         | 成功更新書籍 |
| 400         | 書籍更新失敗  |

#### 新增書籍
Request Example
| 參數名稱    | 必要 | 資料型態    | 參數敘述 |
| ----------- | ---- | ----------- | -------- |
| id          | Y    | INT(11)     | 書籍ID   |
| title       | Y    | String(255) | 書名     |
| author      | Y    | String(255) | 作者     |
| description | Y    | String(255) | 書籍介紹 |
| price       | Y    | INT(11)     | 定價     |
| sellprice   | Y    | INT(11)     | 售價     |

```json=
{
  "title": "Slow Dance",
  "author": "Rainbow Rowell",
  "description": "Shiloh questions if Kerry still wants to reconnect after all the time and changes.",
  "price": 600,
  "sellprice": 550,
  "id": 1
}
```
Response Example

```json=
Book save successfully
```
**API回應說明**
| HTTP status | 中文敘述       |
| ----------- | -------------- |
| 201         | 書籍創建成功 |
| 500         | 伺服器錯誤  |
#### 刪除書籍
Request Example
| 參數名稱 | 必要 | 資料型態 | 參數敘述 |
| -------- | ---- | -------- | -------- |
| ID       | Y    | INT(11)  | 書籍ID |


輸入Parameters:

| Name     | Description            |
|----------|------------------------|
| ID *     | 要刪除的書籍ID     |

**Example**: `5`

Response Example

```json=
Book with ID 5 deleted successfully
```
**API回應說明**
| HTTP status | 中文敘述       |
| ----------- | -------------- |
| 200         | 書籍刪除成功 |
| 404         | 書籍未找到   |
