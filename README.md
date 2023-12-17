#                           WEB ĐỌC SÁCH

### Thành viên nhóm
|STT|Mã số sinh viên|Họ tên|
|-|-|-|
|1|Ngô Văn Nam|...|
|2|...|...|
|3|...|...|

### Cách cài đặt
- Cài NodeJS tại [đây](https://nodejs.org/en/download/)
- Cài [JDK](https://www.oracle.com/java/technologies/javase-downloads.html), sau đó kiểm tra java bằng lệnh `java --version`
- Cài [VScode](https://code.visualstudio.com/download) và các extension cho Java
- Cài Maven tại [đây](https://maven.apache.org/download.cgi) theo [hướng dẫn](https://shareprogramming.net/cach-tai-va-cai-bien-moi-truong-maven-tren-windows/)
- Cài MySQL tại [đây](https://dev.mysql.com/downloads/installer/)
(Các bước cài theo [hướng dẫn](https://www.sql.edu.vn/mysql/mysql-community-server/))
            
__Chú ý bước đặt mật khẩu MySQL phải nhớ mật khẩu đó__

- Backend cài thêm [Postman](https://www.postman.com/downloads/) để test API

### Cách chạy
- Lấy source code: 
    Nếu có git: Clone project này về máy bằng lệnh 
        `git clone https://github.com/NVNamCoder8523/webdocsach.git`
    Nếu không: Download zip tại [đây](https://github.com/NVNamCoder8523/webdocsach)
- Mở VScode ở thư mục vừa clone về

- Vào [đây](/springboot-backend/src/main/resources/application.properties) và sửa các thông số kết nối database (password là mật khẩu đã đặt cho MySQL ở trên)
```
spring.datasource.url=jdbc:mysql://localhost:3306/webdocsach
spring.datasource.username=root
spring.datasource.password=123456
```


- Mở terminal bằng Ctrl + `
- Chạy lệnh để vào folder backend

        cd springboot-backend

- Chạy để cài đặt các thư viện cần thiết

        mvn clean install

- Chạy lệnh để chạy backend

        mvn spring-boot:run

![](backend_run.png)

- Chạy file [Data.sql](/springboot-backend/Data.sql) để thêm dữ liệu vào database



- Mở 1 terminal khác song song bằng phím + trên thanh công cụ của terminal hoặc Ctrl + Shift + `
- Chạy lệnh để vào folder frontend

        cd nextjs-frontend

- Chạy để cài đặt các thư viện cần thiết

        npm install --save-dev

- Chạy lệnh để chạy frontend

        npm run dev

![](frontend_run.png)

- Mở trình duyệt và truy cập vào địa chỉ `http://localhost:3000/` để xem kết quả

(Các lần sau khi chạy, có thể bỏ qua các lệnh tải thư viện sau `;`)

    cd nextjs-frontend
    npm install --save-dev

    cd springboot-backend
    mvn clean install


[Postman ở đây](https://app.getpostman.com/join-team?invite_code=449081af150ae5bd21b12bc0a9c304a6&target_code=f8413d91384359c66f1c83303446931c)