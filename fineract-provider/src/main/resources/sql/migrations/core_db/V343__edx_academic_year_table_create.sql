--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--
-- Create academic_table

CREATE TABLE `m_academic_year` (
	`id` INT(12) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(250) NULL DEFAULT NULL,
	`short_name` VARCHAR(100) NULL DEFAULT NULL,
	`description` VARCHAR(250) NULL DEFAULT NULL,
	`start_date` DATE NOT NULL,
	`end_date` DATE NOT NULL,
	`status_enum` INT(12) NOT NULL,
	`created_by_userid` BIGINT(20) NULL DEFAULT NULL,
	`modified_by_userid` BIGINT(20) NULL DEFAULT NULL,
	`created_on_date` DATETIME NULL DEFAULT NULL,
	`modified_on_date` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_m_academic_year_m_appuser` (`created_by_userid`),
	INDEX `FK_m_academic_year_m_appuser_2` (`modified_by_userid`),
	CONSTRAINT `FK_m_academic_year_m_appuser` FOREIGN KEY (`created_by_userid`) REFERENCES `m_appuser` (`id`),
	CONSTRAINT `FK_m_academic_year_m_appuser_2` FOREIGN KEY (`modified_by_userid`) REFERENCES `m_appuser` (`id`)
);

-- Insert permissions for academic_year related 
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) 
VALUES ('organisation', 'CREATE_ACADEMIC_YEAR', 'ACADEMIC_YEAR', 'CREATE', '0'),
       ('organisation', 'UPDATE_ACADEMIC_YEAR', 'ACADEMIC_YEAR', 'UPDATE', '0'),
       ('organisation', 'DELETE_ACADEMIC_YEAR', 'ACADEMIC_YEAR', 'DELETE', '0'),
       ('organisation', 'ACTIVATE_ACADEMIC_YEAR', 'ACADEMIC_YEAR', 'ACTIVATE', '0'),
       ('organisation', 'CLOSE_ACADEMIC_YEAR', 'ACADEMIC_YEAR', 'CLOSE', '0');