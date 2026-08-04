import React, { useState, useEffect } from 'react';
import { ShoppingCart, Zap, Clock, CheckCircle2, AlertCircle } from 'lucide-react';
import axios from 'axios';

function App() {
  // State quản lý sản phẩm
  const [stock, setStock] = useState(100);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });
  
  // State đồng hồ đếm ngược (Ví dụ: còn 10 phút)
  const [timeLeft, setTimeLeft] = useState(600);

  // Effect đếm ngược thời gian
  useEffect(() => {
    if (timeLeft <= 0) return;
    const timer = setInterval(() => {
      setTimeLeft((prev) => prev - 1);
    }, 1000);
    return () => clearInterval(timer);
  }, [timeLeft]);

  // Format thời gian HH:MM:SS
  const formatTime = (seconds) => {
    const h = Math.floor(seconds / 3600).toString().padStart(2, '0');
    const m = Math.floor((seconds % 3600) / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `${h}:${m}:${s}`;
  };

  // Hàm xử lý khi bấm nút "Săn Deal"
  const handleBuy = async () => {
    setLoading(true);
    setMessage({ type: '', text: '' });

    try {
      // Giả lập/Gọi API sang Spring Boot Backend (Giai đoạn sau sẽ nối thật)
      // const res = await axios.post('http://localhost:8080/api/v1/flashsale/buy', { userId: 101, itemId: 1, quantity: 1 });
      
      // Test giả lập UI
      setTimeout(() => {
        if (stock > 0) {
          setStock((prev) => prev - 1);
          setMessage({ type: 'success', text: '🎉 Săn deal thành công! Đơn hàng đang được xử lý.' });
        } else {
          setMessage({ type: 'error', text: '❌ Hết hàng mất rồi! Bạn nhanh tay vào đợt sau nhé.' });
        }
        setLoading(false);
      }, 300);

    } catch (error) {
      setMessage({ type: 'error', text: 'Có lỗi xảy ra hoặc Server quá tải!' });
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 text-white flex flex-col items-center justify-center p-4">
      {/* Header Flash Sale */}
      <div className="max-w-md w-full bg-slate-800 rounded-2xl shadow-2xl overflow-hidden border border-slate-700">
        <div className="bg-gradient-to-r from-red-600 to-orange-500 p-4 flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Zap className="w-6 h-6 text-yellow-300 animate-bounce" />
            <h1 className="text-xl font-extrabold tracking-wider uppercase">FLASH SALE GIỜ VÀNG</h1>
          </div>
          <div className="flex items-center space-x-1 bg-black/30 px-3 py-1 rounded-full text-xs font-mono">
            <Clock className="w-4 h-4 text-yellow-400" />
            <span>{formatTime(timeLeft)}</span>
          </div>
        </div>

        {/* Thông tin sản phẩm */}
        <div className="p-6 text-center space-y-4">
          <div className="relative w-48 h-48 mx-auto rounded-xl overflow-hidden bg-slate-700 flex items-center justify-center border border-slate-600">
            <span className="text-6xl">📱</span>
            <span className="absolute top-2 right-2 bg-red-600 text-xs font-bold px-2 py-1 rounded">
              -50%
            </span>
          </div>

          <div>
            <h2 className="text-xl font-bold text-slate-100">iPhone 15 Pro Max 256GB - Flash Edition</h2>
            <div className="mt-2 flex items-center justify-center space-x-3">
              <span className="text-2xl font-black text-red-500">15.990.000đ</span>
              <span className="text-sm text-slate-400 line-through">31.980.000đ</span>
            </div>
          </div>

          {/* Thanh Tiến Độ Tồn Kho */}
          <div className="space-y-2">
            <div className="flex justify-between text-xs text-slate-400 font-semibold">
              <span>Còn lại: <strong className="text-orange-400">{stock}</strong> sản phẩm</span>
              <span>Đã bán: {100 - stock}%</span>
            </div>
            <div className="w-full bg-slate-700 h-3 rounded-full overflow-hidden">
              <div 
                className="bg-gradient-to-r from-orange-500 to-red-600 h-full transition-all duration-300"
                style={{ width: `${Math.max(0, stock)}%` }}
              ></div>
            </div>
          </div>

          {/* Thông báo kết quả */}
          {message.text && (
            <div className={`p-3 rounded-lg text-sm flex items-center justify-center space-x-2 ${
              message.type === 'success' ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30' : 'bg-red-500/20 text-red-400 border border-red-500/30'
            }`}>
              {message.type === 'success' ? <CheckCircle2 className="w-5 h-5" /> : <AlertCircle className="w-5 h-5" />}
              <span>{message.text}</span>
            </div>
          )}

          {/* Nút Săn Deal */}
          <button
            onClick={handleBuy}
            disabled={loading || stock <= 0}
            className={`w-full py-3.5 px-6 rounded-xl font-bold text-lg shadow-lg flex items-center justify-center space-x-2 transition-all duration-200 ${
              stock <= 0
                ? 'bg-slate-700 text-slate-500 cursor-not-allowed'
                : 'bg-gradient-to-r from-orange-500 to-red-600 hover:from-orange-600 hover:to-red-700 text-white hover:shadow-orange-500/25 active:scale-95'
            }`}
          >
            <ShoppingCart className="w-5 h-5" />
            <span>{loading ? 'Đang tranh hàng...' : stock <= 0 ? 'HẾT HÀNG' : 'SĂN DEAL NGAY'}</span>
          </button>
        </div>
      </div>
    </div>
  );
}

export default App;