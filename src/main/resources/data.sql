use 5core;

-- 차량 데이터 18대
INSERT INTO vehicle(
    model_code, name, brand, vehicle_type, trim, base_price, final_price, file_name,
    fuel_type, year, displacement, fuel_efficiency
)
VALUES
('avante', '아반떼', '현대', '세단', 'Standard', 20348000, 0, 'avante_01.png', '휘발유', '2025', 1598, 15.20),

('avante-n', '아반떼 N', '현대', '세단', 'Standard', 36176000, 0, 'avante_n_01.png', '휘발유', '2025', 1998, 10.50),

('sonata', '쏘나타', '현대', '세단', 'Standard', 28674000, 0, 'sonata_01.png', '휘발유', '2025', 1999, 13.80),

('sonata-hybrid', '쏘나타 하이브리드', '현대', '세단', 'Standard', 34000000, 0, 'sonata_hybrid_01.png', '하이브리드', '2025', 1999, 19.10),

('grandeur', '그랜저', '현대', '세단', 'Standard', 39638000, 0, 'grandeur_01.png', '휘발유', '2025', 2497, 11.20),

('grandeur-hybrid', '그랜저 하이브리드', '현대', '세단', 'Standard', 46179000, 0, 'grandeur_hybrid_01.png', '하이브리드', '2025', 2399, 18.00),

('tucson', '투싼', '현대', 'SUV', 'Standard', 32527000, 0, 'tucson_01.png', '휘발유', '2025', 1598, 12.60),

('tucson-hybrid', '투싼 하이브리드', '현대', 'SUV', 'Standard', 38772000, 0, 'tucson_hybrid_01.png', '하이브리드', '2025', 1598, 16.20),

('santafe', '싼타페', '현대', 'SUV', 'Standard', 41611000, 0, 'santafe_01.png', '디젤', '2025', 2199, 13.50),

('santafe-hybrid', '싼타페 하이브리드', '현대', 'SUV', 'Standard', 47916000, 0, 'santafe_hybrid_01.png', '하이브리드', '2025', 1598, 14.50),

('palisade', '팰리세이드', '현대', 'SUV', 'Standard', 50570000, 0, 'palisade_01.png', '디젤', '2025', 2199, 10.80),

('kona', '코나', '현대', 'SUV', 'Standard', 26844000, 0, 'kona_01.png', '휘발유', '2025', 1598, 13.90),

('casper', '캐스퍼', '현대', '경차', 'Standard', 14528000, 0, 'casper_01.png', '휘발유', '2025', 998, 14.30),

('venue', '베뉴', '현대', 'SUV', 'Standard', 22879000, 0, 'venue_01.png', '휘발유', '2025', 1598, 13.70),

-- 전기차 (displacement = PS / 연비 = km/kWh)
('kona-ev', '코나 EV', '현대', 'SUV', 'Standard', 48650000, 0, 'kona_ev_01.png', '전기', '2025', 204, 5.80),

('ioniq5', '아이오닉 5', '현대', 'SUV', 'Standard', 54790000, 0, 'ioniq5_01.png', '전기', '2025', 229, 5.10),

('ioniq6', '아이오닉 6', '현대', '세단', 'Standard', 52150000, 0, 'ioniq6_01.png', '전기', '2025', 229, 6.20),

('casper-ev', '캐스퍼 EV', '현대', '경차', 'Standard', 31290000, 0, 'casper_ev_01.png', '전기', '2025', 136, 5.60);


-- 모델 1페이지에 있는 차량 8대에 대한 색상 및 색상 이미지 데이터
INSERT INTO vehicle_color (color, image_color_url, vehicle_id)
VALUES
('white', 'grandeur_white.png', (select id from vehicle where model_code = 'grandeur')),
('black', 'grandeur_black.png', (select id from vehicle where model_code = 'grandeur')),
('silver', 'grandeur_silver.png', (select id from vehicle where model_code = 'grandeur')),
('blue', 'grandeur_blue.png', (select id from vehicle where model_code = 'grandeur')),

('white', 'grandeur_white.png', (select id from vehicle where model_code = 'grandeur-hybrid')),
('black', 'grandeur_black.png', (select id from vehicle where model_code = 'grandeur-hybrid')),
('silver', 'grandeur_silver.png', (select id from vehicle where model_code = 'grandeur-hybrid')),
('blue', 'grandeur_blue.png', (select id from vehicle where model_code = 'grandeur-hybrid')),

('white', 'venue_white.png', (select id from vehicle where model_code = 'venue')),
('black', 'venue_black.png', (select id from vehicle where model_code = 'venue')),
('red', 'venue_red.png', (select id from vehicle where model_code = 'venue')),
('blue', 'venue_blue.png', (select id from vehicle where model_code = 'venue')),

('white', 'santafe_white.png', (select id from vehicle where model_code = 'santafe')),
('black', 'santafe_black.png', (select id from vehicle where model_code = 'santafe')),
('brown', 'santafe_brown.png', (select id from vehicle where model_code = 'santafe')),
('blue', 'santafe_blue.png', (select id from vehicle where model_code = 'santafe')),

('white', 'santafe_white.png', (select id from vehicle where model_code = 'santafe-hybrid')),
('black', 'santafe_black.png', (select id from vehicle where model_code = 'santafe-hybrid')),
('brown', 'santafe_brown.png', (select id from vehicle where model_code = 'santafe-hybrid')),
('blue', 'santafe_blue.png', (select id from vehicle where model_code = 'santafe-hybrid')),

('white', 'sonata_white.png', (select id from vehicle where model_code = 'sonata')),
('black', 'sonata_black.png', (select id from vehicle where model_code = 'sonata')),
('red', 'sonata_red.png', (select id from vehicle where model_code = 'sonata')),
('blue', 'sonata_blue.png', (select id from vehicle where model_code = 'sonata')),

('white', 'sonata_white.png', (select id from vehicle where model_code = 'sonata-hybrid')),
('black', 'sonata_black.png', (select id from vehicle where model_code = 'sonata-hybrid')),
('red', 'sonata_red.png', (select id from vehicle where model_code = 'sonata-hybrid')),
('blue', 'sonata_blue.png', (select id from vehicle where model_code = 'sonata-hybrid')),

('white', 'avante_white.png', (select id from vehicle where model_code = 'avante')),
('black', 'avante_black.png', (select id from vehicle where model_code = 'avante')),
('silver', 'avante_silver.png', (select id from vehicle where model_code = 'avante')),
('blue', 'avante_blue.png', (select id from vehicle where model_code = 'avante'));