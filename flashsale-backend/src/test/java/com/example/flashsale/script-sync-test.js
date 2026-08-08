import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '5s', target: 100 },   // 5s đầu: Tăng lên 100 user
    { duration: '10s', target: 1000 }, // 10s tiếp: Vọt lên 1,000 user
    { duration: '15s', target: 1000 }, // Duy trì 1,000 user xả đạn liên tục
    { duration: '5s', target: 0 },     // Giảm dần về 0
  ],
};

export default function () {
  // Point tới endpoint SYNC trực tiếp DB
  const url = 'http://localhost:8080/api/flash-sales/items/1/deduct-sync-test?quantity=1';

  const res = http.get(url);

  check(res, {
    'Status 200 (Mua thành công)': (r) => r.status === 200,
    'Status 5xx / Lock / Timeout (Lỗi Server/DB)': (r) => r.status >= 500,
    'Status 400 (Hết hàng)': (r) => r.status === 400,
  });

  sleep(0.1);
}