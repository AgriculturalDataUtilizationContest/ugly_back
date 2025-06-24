INSERT INTO tb_crop (
    id, crop_id, crop_kor_name, crop_eng_name,
    category, crops_image, crop_cost, ugly_cost,
    deleted, created_at, updated_at
)
VALUES
    ('1', '151', '고구마', 'Sweet Potato', '식량작물', 'sweet_potato.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('2', '152', '감자', 'Potato', '식량작물', 'potato.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('3', '212', '양배추', 'Cabbage', '채소', 'cabbage.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('4', '211', '배추', 'Chinese Cabbage', '채소', 'chinese_cabbage.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('5', '221', '수박', 'Watermelon', '채소', 'watermelon.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('6', '214', '상추', 'Lettuce', '채소', 'lettuce.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('7', '224', '호박', 'Pumpkin', '채소', 'pumpkin.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('8', '226', '딸기', 'Strawberry', '채소', 'strawberry.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('9', '231', '무', 'Radish', '채소', 'radish.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('10', '246', '파', 'Green Onion', '채소', 'green_onion.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('11', '245', '양파', 'Onion', '채소', 'onion.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('12', '411', '사과', 'Apple', '과일', 'apple.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('13', '412', '배', 'Pear', '과일', 'pear.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('14', '415', '감귤', 'Tangerine', '과일', 'tangerine.jpg', NULL, NULL, 'N', NOW(), NOW()),
    ('15', '222', '참외', 'Oriental Melon', '채소', 'oriental_melon.jpg', NULL, NULL, 'N', NOW(), NOW());



# INSERT INTO tb_crop_summary (
#     id, crop_id, summary, deleted, created_at, updated_at
# ) VALUES
#       (1, '151', NULL, 'N', NOW(), NOW()), -- 고구마
#       (2, '152', NULL, 'N', NOW(), NOW()), -- 감자
#       (3, '211', NULL, 'N', NOW(), NOW()), -- 배추
#       (4, '212', NULL, 'N', NOW(), NOW()), -- 양배추
#       (5, '214', NULL, 'N', NOW(), NOW()), -- 상추
#       (6, '221', NULL, 'N', NOW(), NOW()), -- 수박
#       (7, '222', NULL, 'N', NOW(), NOW()), -- 참외
#       (8, '224', NULL, 'N', NOW(), NOW()), -- 호박
#       (9, '226', NULL, 'N', NOW(), NOW()), -- 딸기
#       (10, '231', NULL, 'N', NOW(), NOW()), -- 무
#       (11, '245', NULL, 'N', NOW(), NOW()), -- 양파
#       (12, '246', NULL, 'N', NOW(), NOW()), -- 파
#       (13, '411', NULL, 'N', NOW(), NOW()), -- 사과
#       (14, '412', NULL, 'N', NOW(), NOW()), -- 배
#       (15, '415', NULL, 'N', NOW(), NOW()); -- 감귤
