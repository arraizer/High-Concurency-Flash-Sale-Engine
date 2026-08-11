import axios from 'axios';

// 1. Base URL chỉ giữ Domain gốc (không thêm subpath ở đây)
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'https://high-concurrency-flash-sale-engine.onrender.com';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 300000,
});

export const flashSaleService = {
  // 2. Viết đầy đủ /api/flash-sales/ ở từng endpoint
  getFlashSaleItem: async (flashSaleId = 1) => {
    // Gọi đúng: https://high-concurrency-flash-sale-engine.onrender.com/api/flash-sales/1/items
    const response = await apiClient.get(`/api/flash-sales/${flashSaleId}/items`);
    
    if (Array.isArray(response.data) && response.data.length > 0) {
      return response.data[0];
    }
    return response.data;
  },

  getRedisStock: async (itemId = 1) => {
    // Gọi đúng: https://high-concurrency-flash-sale-engine.onrender.com/api/flash-sales/items/1/redis-stock
    const response = await apiClient.get(`/api/flash-sales/items/${itemId}/redis-stock`);
    return response.data;
  },

  placeOrder: async (userId, itemId, quantity = 1) => {
  // Ép kiểu ép số rõ ràng để không bị dính chuỗi ":1"
  const cleanItemId = Number(itemId) || 1;
  const cleanQuantity = Number(quantity) || 1;

  const response = await apiClient.get(`/api/flash-sales/items/${cleanItemId}/deduct-test`, {
    params: { quantity: cleanQuantity }
  });
  return response.data;
},
};