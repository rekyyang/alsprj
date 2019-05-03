

DROP TABLE IF EXISTS alarmSetting;
CREATE TABLE `alarmSetting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `alarmItem` varchar(255) NOT NULL COMMENT '报警项目',
  `ceiling` double NOT NULL COMMENT '阈值上限',
  `floor` double NOT NULL COMMENT '阈值下限',
  `note` varchar(255) NOT NULL COMMENT '特定值',
  `type` varchar(255) NOT NULL COMMENT '类别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS ALSData;
CREATE TABLE `ALSData` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `surgeryNo` VARCHAR(255) NOT NULL COMMENT '手术号',
  `timestamp` bigint(20) NOT NULL COMMENT '时间戳',
  `dPacc` double DEFAULT NULL COMMENT '采血压',
  `dPart` double DEFAULT NULL COMMENT '动脉压',
  `dPven` double DEFAULT NULL COMMENT '静脉压',
  `dP1st` double DEFAULT NULL COMMENT '血浆压P1st',
  `dTMP` double DEFAULT NULL COMMENT '跨膜压',
  `dP2nd` double DEFAULT NULL COMMENT '血浆入口P2nd',
  `dP3rd` double DEFAULT NULL COMMENT '滤过压',
  `cumulativeTime` bigint(20) DEFAULT NULL COMMENT '累计时间',
  `iBPSpeed` int(11) DEFAULT NULL COMMENT '血泵',
  `iFPSpeed` int(11) DEFAULT NULL COMMENT '分离泵',
  `iDPSpeed` int(11) DEFAULT NULL COMMENT '透析泵',
  `iRPSpeed` int(11) DEFAULT NULL COMMENT '返液泵RP',
  `iFP2SPeed` int(11) DEFAULT NULL COMMENT '滤过泵FP2',
  `iCPSpeed` int(11) DEFAULT NULL COMMENT '循环泵CP',
  `iSPSpeed` int(11) DEFAULT NULL COMMENT '肝素泵',
  `warmer` double DEFAULT NULL COMMENT '加热温度',
  `dBPT` double DEFAULT NULL COMMENT '血液泵累计 精确到0.1L',
  `dFPT` double DEFAULT NULL COMMENT 'FP累计 精确到0.1L',
  `dDPT` double DEFAULT NULL COMMENT 'DP累计 精确到0.1L',
  `dRPT` double DEFAULT NULL COMMENT 'RP累计 精确到0.1L',
  `dFP2T` double DEFAULT NULL COMMENT 'FP2累计 精确到0.1L',
  `dSPT` double DEFAULT NULL COMMENT 'SP累计跨膜压',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=487 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS guardianData;
CREATE TABLE `guardianData` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `surgeryNo` varchar(255) NOT NULL COMMENT '手术号',
  `timestamp` bigint(20) NOT NULL COMMENT '时间戳',
  `heartRate` int(11) DEFAULT NULL COMMENT '心率',
  `systolicPressure` int(11) DEFAULT NULL COMMENT '收缩压',
  `diastolicPressure` int(11) DEFAULT NULL COMMENT '舒张压',
  `bloodOxygen` int(11) DEFAULT NULL COMMENT '血氧',
  `ecgFeatures` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS surgery;
CREATE TABLE `surgery` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `surgeryNo` varchar(255) NOT NULL COMMENT '手术号',
  `startTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '开始时间',
  `endTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '结束时间',
  `patient` varchar(255) DEFAULT NULL COMMENT '病人',
  `patientId` varchar(255) DEFAULT NULL COMMENT '病人身份证号',
  `doctor` varchar(255) DEFAULT NULL COMMENT '主治医生',
  `doctorId` varchar(255) DEFAULT NULL COMMENT '主治医生Id',
  `description` varchar(255) DEFAULT NULL COMMENT '病情描述',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '手术状态',
  `alarmPerson` varchar(255) NOT NULL COMMENT '报警人',
  `type` varchar(255) NOT NULL COMMENT '手术类型',
  `lastAlarm` bigint(20) NOT NULL DEFAULT '0' COMMENT '上次报警时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `surgeryNo` (`surgeryNo`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS user;
CREATE TABLE user(
  userId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  userName VARCHAR(255) NOT NULL COMMENT '用户名',
  password VARCHAR(255) NOT NULL COMMENT '密码',
  role VARCHAR(64) NOT NULL COMMENT '用户角色',
  PRIMARY KEY(userId)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;
INSERT INTO user(userName, password, role) VALUE ("zhengzhiqi","zzqshyk","admin");
DROP TABLE IF EXISTS authority;
CREATE TABLE authority(
  authorityId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  role INT NOT NULL COMMENT '用户角色',
  authorities VARCHAR(64) NOT NULL COMMENT '用户权限',
  PRIMARY KEY (authorityId)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;
