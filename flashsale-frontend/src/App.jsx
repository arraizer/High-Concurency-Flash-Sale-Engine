import React, { useState, useEffect } from 'react';
import { ShoppingCart, Zap, Clock, CheckCircle2, AlertCircle, RefreshCw, Ticket } from 'lucide-react';
import { flashSaleService } from './api/flashSaleApi';

function App() {
  const [item, setItem] = useState(null);
  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(true);
  const [message, setMessage] = useState({ type: '', text: '' });
  const [timeLeft, setTimeLeft] = useState(600);

  const FLASH_SALE_ID = 1;
  const USER_ID = Math.floor(Math.random() * 1000) + 1;

  // Lấy dữ liệu thật từ Backend


  useEffect(() => {
    fetchStock();
  }, []);

  // Effect đếm ngược
  useEffect(() => {
    if (timeLeft <= 0) return;
    const timer = setInterval(() => setTimeLeft((prev) => prev - 1), 1000);
    return () => clearInterval(timer);
  }, [timeLeft]);

  const formatTime = (seconds) => {
    const m = Math.floor(seconds / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `00:${m}:${s}`;
  };

  const formatMoney = (amount) => {
    return amount ? amount.toLocaleString('vi-VN') + 'đ' : '0đ';
  };

 const fetchStock = async () => {
    try {
      setFetching(true);
      
      // 1. Lấy thông tin item
      const itemData = await flashSaleService.getFlashSaleItem(FLASH_SALE_ID);
      
      if (itemData) {
        // 2. Lấy tồn kho thực tế realtime từ Redis
        const realTimeStock = await flashSaleService.getRedisStock(itemData.id);
        
        // 3. Ghi đè stock từ Redis vào state item
        setItem({
          ...itemData,
          stock: realTimeStock // Lấy đúng con số từ Redis!
        });
      }
    } catch (error) {
      console.error("Lỗi kết nối Backend:", error);
      setMessage({ type: 'error', text: 'Không thể tải thông tin tồn kho từ Redis!' });
    } finally {
      setFetching(false);
    }
  };

  // Hàm săn deal
  const handleBuy = async () => {
    if (!item) return;
    setLoading(true);
    setMessage({ type: '', text: '' });

    try {
      const resText = await flashSaleService.placeOrder(USER_ID, item.id, 1);
      setMessage({ type: 'success', text: `🎉 ${resText}` });
      
      // 🔥 Sau khi trừ kho thành công, gọi lại fetchStock() để pull con số mới từ Redis về!
      await fetchStock();

    } catch (error) {
      const errorMsg = error.response?.data || 'Trừ kho thất bại: Sản phẩm đã HẾT HÀNG!';
      setMessage({ type: 'error', text: `❌ ${errorMsg}` });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 text-white flex flex-col items-center justify-center p-4">
      <div className="max-w-md w-full bg-slate-800 rounded-2xl shadow-2xl overflow-hidden border border-slate-700">
        
        {/* Header */}
        <div className="bg-gradient-to-r from-purple-600 to-pink-600 p-4 flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Zap className="w-6 h-6 text-yellow-300 animate-bounce" />
            <h1 className="text-xl font-extrabold tracking-wider uppercase">SĂN VÉ CONCERT</h1>
          </div>
          <div className="flex items-center space-x-1 bg-black/30 px-3 py-1 rounded-full text-xs font-mono">
            <Clock className="w-4 h-4 text-yellow-400" />
            <span>{formatTime(timeLeft)}</span>
          </div>
        </div>

        {/* Product Details */}
        <div className="p-6 text-center space-y-4">
          <div className="relative w-48 h-48 mx-auto rounded-xl overflow-hidden bg-gradient-to-br from-purple-900 to-slate-800 flex flex-col items-center justify-center border border-purple-500/30 shadow-inner">
            <Ticket className="w-20 h-20 text-pink-400 animate-pulse" />
            <span className="text-xs font-bold text-pink-200 mt-2">HOT TICKET</span>
            <span className="absolute top-2 right-2 bg-red-600 text-xs font-bold px-2 py-1 rounded">
              FLASH SALE
            </span>
          </div>

          <div>
            <h2 className="text-xl font-bold text-slate-100">
              {item ? item.productName : 'Đang tải thông tin vé...'}
            </h2>
            {item && (
              <div className="mt-2 flex items-center justify-center space-x-3">
                <span className="text-2xl font-black text-pink-500">{formatMoney(item.flashPrice)}</span>
                <span className="text-sm text-slate-400 line-through">{formatMoney(item.originalPrice)}</span>
              </div>
            )}
          </div>

          {/* Stock Info */}
          <div className="space-y-2">
            <div className="flex justify-between items-center text-xs text-slate-400 font-semibold">
              <span>Còn lại: <strong className="text-pink-400">{fetching ? '...' : (item?.stock ?? 0)}</strong> vé</span>
              <button onClick={fetchStock} className="hover:text-white transition-colors flex items-center space-x-1">
                <RefreshCw className={`w-3 h-3 ${fetching ? 'animate-spin' : ''}`} />
                <span>Làm mới</span>
              </button>
            </div>
            <div className="w-full bg-slate-700 h-3 rounded-full overflow-hidden">
              <div 
                className="bg-gradient-to-r from-pink-500 to-purple-600 h-full transition-all duration-300"
                style={{ width: `${Math.min(100, Math.max(0, item?.stock ?? 0))}%` }}
              ></div>
            </div>
          </div>

          {/* Alert Message */}
          {message.text && (
            <div className={`p-3 rounded-lg text-sm flex items-center justify-center space-x-2 ${
              message.type === 'success' ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30' : 'bg-red-500/20 text-red-400 border border-red-500/30'
            }`}>
              {message.type === 'success' ? <CheckCircle2 className="w-5 h-5 shrink-0" /> : <AlertCircle className="w-5 h-5 shrink-0" />}
              <span>{message.text}</span>
            </div>
          )}

          {/* Submit Button */}
          <button
            onClick={handleBuy}
            disabled={loading || !item || item.stock <= 0 || fetching}
            className={`w-full py-3.5 px-6 rounded-xl font-bold text-lg shadow-lg flex items-center justify-center space-x-2 transition-all duration-200 ${
              !item || item.stock <= 0
                ? 'bg-slate-700 text-slate-500 cursor-not-allowed'
                : 'bg-gradient-to-r from-pink-500 to-purple-600 hover:from-pink-600 hover:to-purple-700 text-white hover:shadow-pink-500/25 active:scale-95'
            }`}
          >
            <ShoppingCart className="w-5 h-5" />
            <span>{loading ? 'Đang tranh vé...' : (item?.stock ?? 0) <= 0 ? 'HẾT VÉ' : 'SĂN VÉ NGAY'}</span>
          </button>
        </div>
      </div>
    </div>
  );
}

export default App;