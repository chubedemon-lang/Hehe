# Đua đội LIVE — bản nguồn thử nghiệm

Game dọc Android 8+, 4 đội, mỗi vòng 60 giây. Bình luận 1–4 cộng 1 điểm và chọn đội; quà cộng diamonds × count cho đội đã chọn. Kết quả hòa được hiển thị đủ các đội.

## Trạng thái bàn giao

APK được tạo bằng GitHub Actions: mở tab Actions, chọn bản chạy Build APK mới nhất, tải artifact LiveRace-APK và giải nén. Chỉ có APK sau khi bản chạy thành công. Mã Android chưa được chạy trên thiết bị thật. Game HTML có thể mở độc lập để thử bằng trình duyệt.

Không có tích hợp TikTok trực tiếp. Chỉ một điện thoại và tài khoản TikTok chưa đủ để bản này tự đọc bình luận/quà. Cần một dịch vụ cầu nối bên ngoài nhận sự kiện LIVE rồi gửi WebSocket. Dịch vụ có thể chạy trên cloud, không bắt buộc có máy tính khi live. Dự án không cung cấp dịch vụ đó và chưa kiểm chứng với TikTok LIVE thật. Không nhập mật khẩu TikTok vào game.

## Tạo APK bằng điện thoại qua GitHub

1. Tạo repository GitHub, tải toàn bộ nội dung thư mục này lên, giữ đúng cấu trúc; phải có cả `.github/workflows/apk.yml`.
2. Trong Actions chọn Build APK → Run workflow.
3. Sau khi build thành công, tải artifact LiveRace-APK, giải nén lấy app-debug.apk.
4. Cho phép trình quản lý tệp cài ứng dụng từ nguồn này, rồi cài APK. Đây là bản debug để thử, chưa phải bản phát hành cửa hàng.

Hoặc trên máy đã cài JDK 17, Gradle 8.9 và Android SDK 35: `gradle assembleDebug`.

## Dùng khi LIVE

Mở game, bấm Bắt đầu vòng. Các nút thử mô phỏng bình luận/quà. Nếu TikTok cấp tính năng LIVE game/chia sẻ màn hình, chọn chia sẻ màn hình game. Ứng dụng không tự phát livestream, không mở khóa quyền LIVE của TikTok. Kiểm tra thông báo riêng tư trước khi chia sẻ màn hình.

## Giao thức cầu nối (cần tự triển khai dịch vụ)

Dùng wss:// với chứng chỉ hợp lệ. Gửi từng JSON qua WebSocket:

```json
{"id":"comment-1","type":"comment","user":"viewer123","text":"2"}
```
```json
{"id":"gift-1","type":"gift","user":"viewer123","diamonds":1,"count":5,"repeatEnd":true}
```

`user` là ID ổn định; `id` là ID duy nhất cho mỗi sự kiện. Với quà dạng chuỗi, máy chủ chỉ gửi tổng cuối cùng (repeatEnd=true), tránh đếm lặp. Game bỏ qua repeatEnd=false. Bình luận chọn đội trước khi tặng. Chỉ nhận điểm khi vòng đang chạy. `diamonds` là giá trị mỗi quà; `count` là tổng số quà trong chuỗi. ID trùng bị bỏ qua trong vòng hiện tại; nên luôn gửi ID. Mỗi sự kiện quà giới hạn 100.000 điểm. Giao thức này do game quy định, không phải API chính thức TikTok.

Máy chủ cần tự xác thực người dùng, gắn đúng phòng LIVE, kiểm tra dữ liệu và giới hạn lưu lượng. Không đưa token bí mật vào URL đã chia sẻ công khai. Kết nối không tự nối lại; bấm Kết nối khi cần. Sự kiện ngoài vòng bị bỏ qua.
