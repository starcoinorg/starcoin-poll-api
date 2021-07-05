
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for poll_item
-- ----------------------------
CREATE TABLE `poll_item`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `creator` varchar(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `for_votes` bigint(20) NOT NULL DEFAULT '0' COMMENT '投票数',
  `against_votes` bigint(20) NOT NULL DEFAULT '0' COMMENT '反票数',
  `end_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '结束时间戳',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '链接',
  `network` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '动态网络',
  `type_args_1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '中文标题',
  `title_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '英文标题',
  `description` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '中文说明',
  `description_en` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '英文说明',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '状态 1:pending,2:active,3:defeated,4:agreed,5:queued,6:executable,7:extracted',
  `created_at` bigint(20) NULL DEFAULT NULL COMMENT '创建表时间',
  `updated_at` bigint(20) NULL DEFAULT NULL COMMENT '更新表时间',
  `deleted_at` bigint(20) NULL DEFAULT NULL COMMENT '删除表时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
