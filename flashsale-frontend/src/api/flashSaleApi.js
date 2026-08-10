import axios from 'axios';

// 1. Base URL chỉ dừng ở tên Controller
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL 
  ? `${import.meta.env.VITE_API_BASE_URL}/api/flash-sales` 
  : 'https://high-concurrency-flash-sale-engine.onrender.com/api/flash-sales';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 5000,
});

export const flashSaleService = {
  // 2. Ghép đúng chuẩn /1/items
  getFlashSaleItem: async (flashSaleId = 1) => {
    // Gọi tới: http://localhost:8080/api/flash-sales/1/items
    const response = await apiClient.get(`/${flashSaleId}/items`);
    
    // Đảm bảo bóc tách mảng nếu Backend trả về mảng [ {...} ]
    if (Array.isArray(response.data) && response.data.length > 0) {
      return response.data[0];
    }
    return response.data;
  },
  // 2. 🔥 LẤY TỒN KHO THỰC TẾ TỪ REDIS (Endpoint trong ảnh của bạn)
  getRedisStock: async (itemId = 1) => {
    const response = await apiClient.get(`/items/${itemId}/redis-stock`);
    return response.data; // Trả về số Long (ví dụ: 99, 98...)
  },
  // 2. Gọi chuẩn endpoint /items/{itemId}/deduct-test (HTTP GET)
  placeOrder: async (userId, itemId, quantity = 1) => {
    const response = await apiClient.get(`/items/${itemId}/deduct-test`, {
      params: { quantity }
    });
    // Trả về chuỗi text thành công: "Đặt hàng thành công! Stock còn lại..."
    return response.data;
  },
};