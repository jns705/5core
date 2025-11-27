use 5core;

INSERT INTO vehicle(model_code, name, brand, vehicle_type, trim, price, file_name, fuel_type, year, displacement)
VALUES
('avante', '아반떼', '현대', '세단', 'Standard', 20340000, 'avante_01.png', '휘발유', '2025', '1598'),

('avante-n', '아반떼 N', '현대', '세단', 'Standard', 36000000, 'avante_n_01.png', '휘발유', '2025', '1998'),

('sonata', '쏘나타', '현대', '세단', 'Standard', 28000000, 'sonata_01.png', '휘발유', '2025', '1999'),

('sonata-hybrid', '쏘나타 하이브리드', '현대', '세단', 'Standard', 34000000, 'sonata_hybrid_01.png', '하이브리드', '2025', '1999'),

('grandeur', '그랜저', '현대', '세단', 'Standard', 39000000, 'grandeur_01.png', '휘발유', '2025', '2497'),

('grandeur-hybrid', '그랜저 하이브리드', '현대', '세단', 'Standard', 46000000, 'grandeur_hybrid_01.png', '하이브리드', '2025', '2399'),

('tucson', '투싼', '현대', 'SUV', 'Standard', 32000000, 'tucson_01.png', '휘발유', '2025', '1598'),

('tucson-hybrid', '투싼 하이브리드', '현대', 'SUV', 'Standard', 38000000, 'tucson_hybrid_01.png', '하이브리드', '2025', '1598'),

('santafe', '싼타페', '현대', 'SUV', 'Standard', 41000000, 'santafe_01.png', '디젤', '2025', '2199'),

('santafe-hybrid', '싼타페 하이브리드', '현대', 'SUV', 'Standard', 47000000, 'santafe_hybrid_01.png', '하이브리드', '2025', '1598'),

('palisade', '팰리세이드', '현대', 'SUV', 'Standard', 50000000, 'palisade_01.png', '디젤', '2025', '2199'),

('kona', '코나', '현대', 'SUV', 'Standard', 26000000, 'kona_01.png', '휘발유', '2025', '1598'),

('casper', '캐스퍼', '현대', '경차', 'Standard', 14500000, 'casper_01.png', '휘발유', '2025', '998'),

('venue', '베뉴', '현대', 'SUV', 'Standard', 22000000, 'venue_01.png', '휘발유', '2025', '1598'),

-- 전기차 (마력값)
('kona-ev', '코나 EV', '현대', 'SUV', 'Standard', 48000000, 'kona_ev_01.png', '전기', '2025', '204'),

('ioniq5', '아이오닉 5', '현대', 'SUV', 'Standard', 54000000, 'ioniq5_01.png', '전기', '2025', '229'),

('ioniq6', '아이오닉 6', '현대', '세단', 'Standard', 52000000, 'ioniq6_01.png', '전기', '2025', '229'),

('casper-ev', '캐스퍼 EV', '현대', '경차', 'Standard', 31000000, 'casper_ev_01.png', '전기', '2025', '136');