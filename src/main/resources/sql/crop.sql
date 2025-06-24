INSERT INTO tb_crop (
    id, crop_id, crop_kor_name, crop_eng_name,
    category, crops_image, crop_cost, ugly_cost,
    deleted, created_at, updated_at
)
VALUES
    ('1', '151', '고구마', 'Sweet Potato', '식량작물', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/a1fe0fab-8781-41b4-a7e7-2a28450989ff_temp9198833090129261199sweetpotato.png', NULL, NULL, 'N', NOW(), NOW()),
    ('2', '152', '감자', 'Potato', '식량작물', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/4ebf16d1-740e-47bd-a9ed-5928dcbd8e70_temp3515120557017058253potato.png', NULL, NULL, 'N', NOW(), NOW()),
    ('3', '212', '양배추', 'Cabbage', '채소', 'cabbage.jpghttps://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/b69c2437-7aa3-4771-b20a-6df7ee193bb6_temp14316684187942941772cabbage.png', NULL, NULL, 'N', NOW(), NOW()),
    ('4', '211', '배추', 'Chinese Cabbage', '채소', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/5d172893-fd22-45c7-9f71-58a93b9c934e_temp3944310872358324135chinesecabbage.png', NULL, NULL, 'N', NOW(), NOW()),
    ('5', '221', '수박', 'Watermelon', '채소', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/2e9446d5-3b54-483a-9c45-e086613318b1_temp13157574946682259777watermelon.png', NULL, NULL, 'N', NOW(), NOW()),
    ('6', '214', '상추', 'Lettuce', '채소', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/41d22945-3c4a-4583-81ac-af27c544100d_temp13244036630544607165greenonion.png', NULL, NULL, 'N', NOW(), NOW()),
    ('7', '224', '호박', 'Pumpkin', '채소', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/1fb1b18e-1c96-412c-9b23-f2bcc8b4d2bc_temp7081831203534968381pumpkin.png', NULL, NULL, 'N', NOW(), NOW()),
    ('8', '226', '딸기', 'Strawberry', '채소', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/a6af2200-d4f5-4fc0-821c-2638f7a16d7d_temp17734756738992423695strawberry.png', NULL, NULL, 'N', NOW(), NOW()),
    ('9', '231', '무', 'Radish', '채소', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/a2fb608d-6f34-4503-b39a-e802f8d3ecfd_temp6322046348977991717radish.png', NULL, NULL, 'N', NOW(), NOW()),
    ('10', '246', '파', 'Green Onion', '채소', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/41d22945-3c4a-4583-81ac-af27c544100d_temp13244036630544607165greenonion.png', NULL, NULL, 'N', NOW(), NOW()),
    ('11', '245', '양파', 'Onion', '채소', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/fc7882f3-2e38-45bb-9557-222178e927ba_temp12938941052915849620onion.png', NULL, NULL, 'N', NOW(), NOW()),
    ('12', '411', '사과', 'Apple', '과일', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/27f21611-f622-47d4-b25a-b634bef14e1c_temp8083182264635439749apple.png', NULL, NULL, 'N', NOW(), NOW()),
    ('13', '412', '배', 'Pear', '과일', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/7213adfb-780e-4a37-b4a0-f6643b7bdce1_temp13735969454059884776pear.png', NULL, NULL, 'N', NOW(), NOW()),
    ('14', '415', '감귤', 'Tangerine', '과일', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/a4b81dd6-70fc-4c8e-b466-5745dbd12967_temp17763313778307232235tangerine.png', NULL, NULL, 'N', NOW(), NOW()),
    ('15', '222', '참외', 'Oriental Melon', '채소', 'https://agriculutre-contest.s3.ap-northeast-2.amazonaws.com/agriculutre-contest/3b4e7f40-4e87-4c86-8442-043d7efb23ee_temp1412793091350801766orientalmelon.png', NULL, NULL, 'N', NOW(), NOW());



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
