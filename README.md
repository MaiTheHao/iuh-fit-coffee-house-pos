# HỆ THỐNG QUẢN LÝ BÁN HÀNG TẠI COFFEE HOUSE (POS SYSTEM)

## 1. GIỚI THIỆU MỤC TIÊU DỰ ÁN
- **Bối cảnh:** Đây là một đồ án môn học tích hợp mang tính học thuật tại Đại học Công nghiệp TP.HCM (IUH).
- **Mục tiêu hệ thống:** Xây dựng một ứng dụng Desktop đơn nhất vận hành trực tiếp trên máy tính tại quầy thu ngân, phục vụ tối ưu cho các quán cà phê quy mô vừa và nhỏ.
- **Giá trị nghiệp vụ:** - Hệ thống tích hợp hai chế độ làm việc song song: Chế độ Bán hàng ưu tiên tốc độ xử lý đơn và tính tiền nhanh chóng (đảm bảo KPI giao dịch dưới 60 giây), và Chế độ Quản trị giúp chủ cửa hàng dễ dàng quản lý thực đơn, xem báo cáo tức thì.
  - Toàn bộ dữ liệu được thiết kế để lưu trữ cục bộ, đảm bảo hệ thống POS hoạt động độc lập, xuyên suốt và không bị gián đoạn do phụ thuộc vào kết nối Internet.

## 2. ĐỘI NGŨ PHÁT TRIỂN (DEV TEAM)
Dự án được phân bổ nguồn lực rõ ràng, bám sát kiến trúc phần mềm được thiết kế:

- **Mai Thế Hào (Team Leader)**
  - *Vai trò:* Trưởng nhóm & BE.
  - *Nhiệm vụ:* Định hình và thiết kế cấu trúc lớp (Class Diagram), mô hình hóa cơ sở dữ liệu (ERD), UI/UX Designer và chịu trách nhiệm xử lý toàn bộ luồng dữ liệu (Data) cùng logic nghiệp vụ (Backend).

- **Nguyễn Lương Triều Vỹ**
  - *Vai trò:* Kỹ sư Frontend (GUI).
  - *Nhiệm vụ:* Hiện thực hóa giao diện người dùng dựa trên nền tảng Java Swing (JDK 17).

- **Trần Thanh Nhựt**
  - *Vai trò:* Kỹ sư Frontend (GUI).
  - *Nhiệm vụ:* Hiện thực hóa giao diện người dùng dựa trên nền tảng Java Swing (JDK 17).

## 3. CÔNG NGHỆ VẬN HÀNH (TECH STACK)
Hệ thống được phát triển theo định hướng raw (không dùng build tool), tập trung vào kiến thức lõi và hiệu suất.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JDK 17](https://img.shields.io/badge/JDK-17-007396?style=for-the-badge&logo=java&logoColor=white)
![Raw Project](https://img.shields.io/badge/Project-Raw_(No_Build_Tool)-4CAF50?style=for-the-badge)
![Backend](https://img.shields.io/badge/Backend-Raw_Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Frontend](https://img.shields.io/badge/Frontend-Java_Swing-orange?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JDBC](https://img.shields.io/badge/Data_Access-JDBC-lightgrey?style=for-the-badge)
![MySQL Connector/J](https://img.shields.io/badge/Connector%2FJ-8.3.x-4CAF50?style=for-the-badge)
![JUnit](https://img.shields.io/badge/JUnit_Platform-6.0.3-25A162?style=for-the-badge&logo=junit5&logoColor=white)

## 4. KHỞI CHẠY ỨNG DỤNG
Thực hiện các bước sau theo đúng thứ tự.

### 4.1. Chuẩn bị MySQL
- Đảm bảo MySQL service đang chạy và phiên bản tương thích với MySQL Connector/J 8.3.x.
- Tạo database theo đúng tên bạn muốn triển khai.

### 4.2. Cập nhật cấu hình kết nối
- Mở [resources/config.properties](resources/config.properties) và cập nhật các giá trị `db.url`, `db.user`, `db.password` cho đúng với database triển khai.

### 4.3. Khởi tạo schema
- Thực thi các file SQL theo thứ tự trong [infra/database/migrations](infra/database/migrations).
- Hãy chạy tuần tự theo thứ tự tên file.

### 4.4. Khởi tạo seed data
- Thực thi file SQL [infra/database/seeds/seed_data.sql](infra/database/seeds/base.sql) để thêm dữ liệu base vào database.
- Bạn có thể tùy chỉnh file này để thêm dữ liệu mẫu phù hợp với nhu cầu thử nghiệm của mình.

### 4.4. Chạy ứng dụng
- Dùng script [run.sh](run.sh) để biên dịch và chạy ứng dụng:

```bash
bash run.sh
```

Tùy chọn khác:

```bash
bash run.sh --compile-only
bash run.sh --run-only
bash run.sh --test
bash run.sh --test-only
bash run.sh --help
```