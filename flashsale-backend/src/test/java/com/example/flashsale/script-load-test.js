import http from 'k6/http';
import { check, sleep } from 'k6';

// 1. Cấu hình kịch bản mô phỏng 1,000 Virtual Users (VUs)
export const options = {
  stages: [
    { duration: '5s', target: 100 },   // Tăng nhanh lên 100 user trong 5 giây đầu
    { duration: '10s', target: 1000 }, // Tăng vọt (Spike) lên 1,000 user trong 10 giây tiếp theo
    { duration: '15s', target: 1000 }, // Duy trì 1,000 user đồng thời xả đạn trong 15 giây
    { duration: '5s', target: 0 },     // Giảm dần tải về 0
  ],
  thresholds: {
    // Tiêu chí đánh giá: Lỗi hệ thống (5xx) phải dưới 1%
    'http_req_failed{status:500}': ['rate<0.01'], 
  },
};

export default function () {
  const url = 'http://localhost:8080/api/flash-sales/items/1/deduct-test?quantity=1';

  // Giả lập mỗi VU gọi API mua vé
  const res = http.get(url);

  // Check xem kết quả trả về là 200 (Thành công), 400 (Hết hàng), hay 429 (Rate Limited)
  check(res, {
    'Status 200 (Mua thành công)': (r) => r.status === 200,
    'Status 429 (Bị Chặn Spam)': (r) => r.status === 429,
    'Status 400 (Báo Hết Hàng)': (r) => r.status === 400,
  });

  // Nghỉ 0.1s giữa các lần bấm
  sleep(0.1);
}

//k6 run src/test/java/com/example/flashsale/script-load-test.js