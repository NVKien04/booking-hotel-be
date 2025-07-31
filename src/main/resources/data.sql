INSERT INTO place_type (id, name, icon, description) VALUES
('hotel', 'Hotel', 'fa-hotel', 'Khách sạn cao cấp'),
  ('homestay', 'Homestay', 'fa-home', 'Nhà nghỉ gia đình'),
 ('apartment', 'Apartment', 'fa-building', 'Căn hộ dịch vụ riêng');
INSERT INTO amenities (id, name, icon) VALUES
                                           (1, 'Wi-fi', 'fa-wifi'),
                                           (2, 'Bếp', 'fa-utensils'),
                                           (3, 'Máy giặt', 'fa-soap'),
                                           (4, 'Máy sấy quần áo', 'fa-tshirt'),
                                           (5, 'Điều hòa nhiệt độ', 'fa-snowflake'),
                                           (6, 'Hệ thống sưởi', 'fa-thermometer-half'),
                                           (7, 'Không gian riêng để làm việc', 'fa-briefcase'),
                                           (8, 'TV', 'fa-tv'),
                                           (9, 'Máy sấy tóc', 'fa-hair-dryer'),
                                           (10, 'Bàn là', 'fa-ir on');
INSERT INTO cities (id, name, slug) VALUES
                                        (1, 'Thành phố Hồ Chí Minh', 'ho-chi-minh'),
                                        (2, 'Hà Nội', 'ha-noi'),
                                        (3, 'Đà Nẵng', 'da-nang');

INSERT INTO districts (id, name, slug, city_id) VALUES
                                                    (1, 'Quận 1', 'quan-1', 1),
                                                    (2, 'Quận 3', 'quan-3', 1),
                                                    (3, 'Quận 7', 'quan-7', 1)
                                                    (4, 'Quận Hoàn Kiếm', 'hoan-kiem', 2),
                                                    (5, 'Quận Ba Đình', 'ba-dinh', 2),
                                                    (6, 'Quận Đống Đa', 'dong-da', 2);
                                                    (7, 'Quận Hải Châu', 'hai-chau', 3),
                                                    (8, 'Quận Sơn Trà', 'son-tra', 3),
                                                    (9, 'Quận Ngũ Hành Sơn', 'ngu-hanh-son', 3);



