/*
Navicat MySQL Data Transfer
Source Host     : localhost:3306
Source Database : restrant
Target Host     : localhost:3306
Target Database : restrant
Date: 2011-5-30 ���� 12:33:59
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for foodinfo
-- ----------------------------
DROP TABLE IF EXISTS `foodinfo`;
CREATE TABLE `foodinfo` (
  `foodId` int(11) NOT NULL,
  `foodName` varchar(255) default NULL,
  `foodImage` varchar(255) default NULL,
  `foodPrice` int(11) default NULL,
  `foodDes` varchar(255) default NULL,
  `foodditals` varchar(255) default NULL,
  PRIMARY KEY  (`foodId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of foodinfo
-- ----------------------------
INSERT INTO `foodinfo` VALUES ('1', '土豆丝盖饭', '500008.jpg', '9', '好好吃哦', '家常小菜，既含有丰富的碳水化合物又含有丰富的维生素经济而又实惠！');
INSERT INTO `foodinfo` VALUES ('2', '青菜肉丝粥', '500022.jpg', '4', '原料：青菜、肉丝口味清淡，清香可口！', '青菜肉丝粥，清淡小粥，一份温馨！');
INSERT INTO `foodinfo` VALUES ('3', '刀削面', '500023.jpg', '6', '配料：青菜、猪肉、海带、骨头汤口味：微辣、中辣、超辣。', '“左手托面，右手持刀，从上往下，往汤锅里削。”这就是刀削面，简单美味，还含有丰富的维生素B哦！');
INSERT INTO `foodinfo` VALUES ('4', '拉面', '500024.jpg', '6', '配料：牛肉、骨头汤口味：微辣、中辣、超辣。', '营养丰富、风味独特、经济实惠、清淡可口、方便快捷！');
INSERT INTO `foodinfo` VALUES ('6', '西红柿打卤面', '500025.jpg', '7', '配料：西红柿、鸡蛋口味：清淡。', '口味独特，经济实惠，简单营养。');
INSERT INTO `foodinfo` VALUES ('7', '木须肉盖饭', '500026.jpg', '8', '原料：木耳、猪肉、青瓜、鸡蛋。口味清淡，美味营养。', '这是一款不错的营养配餐，营养价值极高，而且又美味哦！强烈推荐！');
INSERT INTO `foodinfo` VALUES ('8', '木须肉盖饭', '500033.jpg', '8', '原料：木耳、猪肉、青瓜、鸡蛋。口味适中，营养美味。', '这是一款不错的营养配餐，营养价值极高，而且又美味哦！强烈推荐！');
INSERT INTO `foodinfo` VALUES ('9', '特色炒饭', '500034.jpg', '7', '原料：蘑菇、鸡蛋、胡萝卜、青椒、绿色蔬菜。口感极好！', '这是一款适合不同口味的美食，口感极好！');
INSERT INTO `foodinfo` VALUES ('10', '米粉汤', '500035.jpg', '8', '原料：米粉、骨头汤、豆腐、肉丸汤味鲜美，口感极佳！', '汤味鲜香，美味营养！');
INSERT INTO `foodinfo` VALUES ('12', '重庆小面', '500036.jpg', '5', '正宗重庆街头特色小面！', '汤味醇厚，麻辣可口！');
INSERT INTO `foodinfo` VALUES ('14', '创意炒饭', '500038.jpg', '7', '原料：鸡蛋、胡萝卜、青豆。。。口味适中，非常爽口！', '这是一款极具家庭味的炒饭，更多的是制作人的心思，让你有种温馨的感觉！');
INSERT INTO `foodinfo` VALUES ('16', '酸豆角炒肉末盖饭', '500041.jpg', '8', '开胃可口！', '家常风味，是道非常开胃的美食！');
INSERT INTO `foodinfo` VALUES ('17', '香油抄手', '500042.jpg', '4', '川味小吃，鲜香可口！', '正宗川味小吃，陷大皮薄，再配以鲜美的汤料，是一道极佳的小吃！');
INSERT INTO `foodinfo` VALUES ('18', '西红柿炒鸡蛋', '500043.jpg', '6', '经典搭配！', '金黄的鸡蛋再配以新鲜的西红柿，经典的搭配，也绝对美味！');
INSERT INTO `foodinfo` VALUES ('19', '肉丝茄子', '500044.jpg', '10', '美味可口！', '风味家常小炒，口味极佳！');
INSERT INTO `foodinfo` VALUES ('20', '炸酱面', '500045.jpg', '8', '京味小吃！', '老北京炸酱面，正宗京味小吃！');
INSERT INTO `foodinfo` VALUES ('21', '清炒时蔬', '500046.jpg', '5', '时令绿色蔬菜！', '绿色蔬菜含有丰富的膳食纤维，每天食少量的蔬菜对身体益处多多！');
INSERT INTO `foodinfo` VALUES ('22', '皮蛋瘦肉粥', '500047.jpg', '5', '美味可口！', '皮蛋瘦肉粥，鲜美爽口的小粥！');

-- ----------------------------
-- Table structure for t_consumeinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_consumeinfo`;
CREATE TABLE `t_consumeinfo` (
  `consumeId` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `foodId` int(11) NOT NULL,
  `foodName` varchar(255) default NULL,
  `foodPrice` int(11) default NULL,
  `foodNum` int(11) default NULL,
  `foodTotprice` int(11) default NULL,
  `foodMark` int(11) default NULL,
  PRIMARY KEY  (`consumeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 4096 kB';

-- ----------------------------
-- Records of t_consumeinfo
-- ----------------------------
INSERT INTO `t_consumeinfo` VALUES ('5', '100001', '2', '青菜肉丝粥', '4', '1', '4', '9');
INSERT INTO `t_consumeinfo` VALUES ('7', '100001', '1', '土豆丝盖饭', '9', '1', '9', '10');
INSERT INTO `t_consumeinfo` VALUES ('8', '100001', '2', '青菜肉丝粥', '4', '1', '4', '1');
INSERT INTO `t_consumeinfo` VALUES ('9', '100001', '2', '青菜肉丝粥', '4', '8', '32', '1');
INSERT INTO `t_consumeinfo` VALUES ('10', '100024', '2', '青菜肉丝粥', '4', '1', '4', '1');
INSERT INTO `t_consumeinfo` VALUES ('11', '100024', '12', '重庆小面', '5', '1', '5', '12');
INSERT INTO `t_consumeinfo` VALUES ('12', '100024', '12', '重庆小面', '5', '7', '35', '1');
INSERT INTO `t_consumeinfo` VALUES ('13', '100024', '17', '香油抄手', '4', '1', '4', '1');
INSERT INTO `t_consumeinfo` VALUES ('14', '100024', '21', '清炒时蔬', '5', '1', '5', '1');
INSERT INTO `t_consumeinfo` VALUES ('15', '100024', '1', '土豆丝盖饭', '9', '1', '9', '1');
INSERT INTO `t_consumeinfo` VALUES ('16', '100027', '1', '土豆丝盖饭', '9', '1', '9', '8');
INSERT INTO `t_consumeinfo` VALUES ('17', '100027', '3', '刀削面', '6', '1', '6', '1');
INSERT INTO `t_consumeinfo` VALUES ('18', '100001', '1', '土豆丝盖饭', '9', '1', '9', '1');
INSERT INTO `t_consumeinfo` VALUES ('19', '100001', '3', '刀削面', '6', '1', '6', '10');
INSERT INTO `t_consumeinfo` VALUES ('20', '100001', '4', '拉面', '6', '1', '6', '1');
INSERT INTO `t_consumeinfo` VALUES ('26', '100001', '22', '皮蛋瘦肉粥', '5', '1', '5', '1');
INSERT INTO `t_consumeinfo` VALUES ('27', '100001', '2', '青菜肉丝粥', '4', '1', '4', '1');
INSERT INTO `t_consumeinfo` VALUES ('28', '100001', '4', '拉面', '6', '1', '6', '1');
INSERT INTO `t_consumeinfo` VALUES ('29', '100001', '1', '土豆丝盖饭', '9', '7', '63', '1');
INSERT INTO `t_consumeinfo` VALUES ('31', '100001', '4', '拉面', '6', '1', '6', '1');
INSERT INTO `t_consumeinfo` VALUES ('33', '100033', '7', '木须肉盖饭', '8', '1', '8', '1');
INSERT INTO `t_consumeinfo` VALUES ('34', '100033', '9', '特色炒饭', '7', '1', '7', '1');
INSERT INTO `t_consumeinfo` VALUES ('35', '100001', '1', '土豆丝盖饭', '9', '2', '18', '1');
INSERT INTO `t_consumeinfo` VALUES ('36', '100001', '6', '西红柿打卤面', '7', '1', '7', '1');
INSERT INTO `t_consumeinfo` VALUES ('37', '100001', '7', '木须肉盖饭', '8', '1', '8', '1');
INSERT INTO `t_consumeinfo` VALUES ('38', '100001', '19', '肉丝茄子', '10', '1', '10', '1');
INSERT INTO `t_consumeinfo` VALUES ('39', '100001', '21', '清炒时蔬', '5', '1', '5', '1');

-- ----------------------------
-- Table structure for t_orderinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_orderinfo`;
CREATE TABLE `t_orderinfo` (
  `orderId` int(11) NOT NULL auto_increment,
  `userName` varchar(255) default NULL,
  `orderName` varchar(255) NOT NULL default '',
  `orderAddr` varchar(255) default NULL,
  `orderphoneNo` varchar(255) default NULL,
  `orderMessage` varchar(255) default NULL,
  PRIMARY KEY  (`orderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 4096 kB; InnoDB free: 4096 kB';

-- ----------------------------
-- Records of t_orderinfo
-- ----------------------------
INSERT INTO `t_orderinfo` VALUES ('9', 'vivi', 'vivi', 'changsha', '15074974664', 'eee');
INSERT INTO `t_orderinfo` VALUES ('10', 'kaka', 'vivi', 'dd', '1155998888', 'df');
INSERT INTO `t_orderinfo` VALUES ('11', 'we', '', 'df', '112233', 'sdd');
INSERT INTO `t_orderinfo` VALUES ('12', 'ee', 'vivi', '1234', '11224343', 'rr');
INSERT INTO `t_orderinfo` VALUES ('13', '2', 'vivi', '23', '334444', 'sdd');
INSERT INTO `t_orderinfo` VALUES ('14', '43', 'vivi', 'erer', '3344555566', 'ff');
INSERT INTO `t_orderinfo` VALUES ('15', '33', 'vivi', '44', '33', 'fff');
INSERT INTO `t_orderinfo` VALUES ('16', '45', 'vivi', 'rr', '444545', 'rtr');
INSERT INTO `t_orderinfo` VALUES ('17', 'ff', 'vivi', 'fff', '33', 'ff');
INSERT INTO `t_orderinfo` VALUES ('18', 'dd', 'vivi', 'dd', '33', 'dd');
INSERT INTO `t_orderinfo` VALUES ('19', '3', 'vivi', '3', '33', '3');
INSERT INTO `t_orderinfo` VALUES ('20', '4', 'vivi', '44', '555555', 'ff');
INSERT INTO `t_orderinfo` VALUES ('21', 'xck', 'vivi', 'fff', '33', 'fff');
INSERT INTO `t_orderinfo` VALUES ('22', 'ff', 'vivi', 'erer', '3334', 'dfg');
INSERT INTO `t_orderinfo` VALUES ('23', 'uu', 'vivi', 'tuigiu', '8', '8909-0');
INSERT INTO `t_orderinfo` VALUES ('24', 'sdf', 'vv', 'er', '397', 'er');
INSERT INTO `t_orderinfo` VALUES ('25', 'wer', 'vv', 'er', '37', 'wer');
INSERT INTO `t_orderinfo` VALUES ('26', 'er', 'vv', 'fdf', '33', 'sdf');
INSERT INTO `t_orderinfo` VALUES ('27', 'zcs', 'cc', 'sdf', '373', 'df');
INSERT INTO `t_orderinfo` VALUES ('28', 'kiki', 'vivi', '深圳', '00882323', '辣椒多点');
INSERT INTO `t_orderinfo` VALUES ('29', 'nia', 'vivi', 'df', '32323', 'df');
INSERT INTO `t_orderinfo` VALUES ('30', 'sdf', 'vivi', 'df', '73', 'awf');
INSERT INTO `t_orderinfo` VALUES ('31', 'sdf', 'vivi', 'edf', '33', 'sdf');
INSERT INTO `t_orderinfo` VALUES ('32', 'kiki', 'vivi', '长大', '07315478', '多放点辣椒');
INSERT INTO `t_orderinfo` VALUES ('33', 'fa', 'zz', '长大', '121244', 'adfag');
INSERT INTO `t_orderinfo` VALUES ('34', 'df', 'vivi', 'fghfg', '7332243', 'ertfg\n');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userId` int(11) NOT NULL auto_increment,
  `userName` varchar(255) NOT NULL,
  `userPwd` varchar(255) NOT NULL,
  PRIMARY KEY  (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('100001', 'vivi', '123456');
INSERT INTO `user` VALUES ('100002', 'kaka', '123456');
INSERT INTO `user` VALUES ('100003', 'meii', '123456');
INSERT INTO `user` VALUES ('100015', 'jsj', '123456');
INSERT INTO `user` VALUES ('100021', 'dd', '123456');
INSERT INTO `user` VALUES ('100022', 'h', '1');
INSERT INTO `user` VALUES ('100023', '33', '123456');
INSERT INTO `user` VALUES ('100024', 'vv', 'vv');
INSERT INTO `user` VALUES ('100025', 'vivivi', 'vivi');
INSERT INTO `user` VALUES ('100026', '', '');
INSERT INTO `user` VALUES ('100027', 'cc', 'cc');
INSERT INTO `user` VALUES ('100028', 'mm', 'mm');
INSERT INTO `user` VALUES ('100029', 'XyLina', 'df');
INSERT INTO `user` VALUES ('100030', 'lilei', '123456');
INSERT INTO `user` VALUES ('100031', 'viki', '123456');
INSERT INTO `user` VALUES ('100032', 'cc', '123456');
INSERT INTO `user` VALUES ('100033', 'zz', 'zz');
INSERT INTO `user` VALUES ('100034', 'kiki', '123456');
INSERT INTO `user` VALUES ('100035', 'kimi', '123456');
