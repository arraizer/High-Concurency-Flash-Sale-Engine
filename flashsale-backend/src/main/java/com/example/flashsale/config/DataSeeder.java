package com.example.flashsale.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.flashsale.entity.FlashSaleEvent;
import com.example.flashsale.entity.FlashSaleItem;
import com.example.flashsale.entity.Inventory;
import com.example.flashsale.entity.Product;
import com.example.flashsale.entity.User;
import com.example.flashsale.enums.FlashSaleEventStatus;
import com.example.flashsale.enums.FlashSaleItemStatus;
import com.example.flashsale.repository.FlashSaleEventRepository;
import com.example.flashsale.repository.FlashSaleItemRepository;
import com.example.flashsale.repository.InventoryRepository;
import com.example.flashsale.repository.ProductRepository;
import com.example.flashsale.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final FlashSaleEventRepository flashSaleEventRepository;
    private final FlashSaleItemRepository flashSaleItemRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Nếu đã có data thì bỏ qua
        }

        // 1. Tạo User mẫu
        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("123456");
        user.setEmail("user@example.com");
        user.setPhone("0987654321");
        userRepository.save(user);

        // 2. Tạo Product mẫu
        Product product = new Product();
        product.setName("Vé Concert Anh Trai Vượt Ngàn Chông Gai");
        product.setDescription("Vé hạng VIP khu vực SVĐ Mỹ Đình");
        product.setPrice(new BigDecimal("1500000"));
        product.setImageUrl("https://example.com/ticket.jpg");
        productRepository.save(product);

        // 3. Tạo Inventory cho Product
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(1000);
        inventory.setVersion(0);
        inventoryRepository.save(inventory);

        // 4. Tạo Event Flash Sale
        FlashSaleEvent event = new FlashSaleEvent();
        event.setName("Flash Sale Vé Giờ Vàng");
        event.setStartTime(LocalDateTime.now().minusHours(1));
        event.setEndTime(LocalDateTime.now().plusHours(5));
        event.setStatus(FlashSaleEventStatus.ACTIVE);
        flashSaleEventRepository.save(event);

        // 5. Tạo Flash Sale Item
        FlashSaleItem item = new FlashSaleItem();
        item.setEvent(event);
        item.setProduct(product);
        item.setFlashPrice(new BigDecimal("990000")); // Giá sale đặc biệt
        item.setStock(100); // Kho riêng đợt sale
        item.setSoldCount(0);
        item.setVersion(0);
        item.setStatus(FlashSaleItemStatus.IN_STOCK);
        flashSaleItemRepository.save(item);

        System.out.println("✅ === ĐÃ KHỞI TẠO DỮ LIỆU MẪU THÀNH CÔNG ===");
    }
}