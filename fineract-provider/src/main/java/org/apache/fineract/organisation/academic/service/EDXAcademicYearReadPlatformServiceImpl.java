/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.organisation.academic.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.organisation.academic.data.EDXAcademicYearData;
import org.apache.fineract.organisation.academic.domain.EDXAcademicYearEnumeration;
import org.apache.fineract.organisation.academic.exception.EDXAcademicYearNotFoundException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ram
 * */
@Service
public class EDXAcademicYearReadPlatformServiceImpl implements EDXAcademicYearReadPlatformService{
	
	private final PlatformSecurityContext context;
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
	public EDXAcademicYearReadPlatformServiceImpl(PlatformSecurityContext context, final RoutingDataSource dataSource) {
		super();
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

    private static final class EDXAcademicYearMapper implements RowMapper<EDXAcademicYearData> {

        private final String schema;

        public EDXAcademicYearMapper() {
            final StringBuilder sqlBuilder = new StringBuilder(200);
            sqlBuilder.append("ay.id as id, ay.name as name, ay.short_name as shortName, ay.description as description, ");
            sqlBuilder.append("ay.status_enum as statusEnum, ay.start_date as startDate, ay.end_date as endDate ");
            sqlBuilder.append("from m_academic_year ay ");
            this.schema = sqlBuilder.toString();
        }

        public String schema() {
            return this.schema;
        }

        @Override
        public EDXAcademicYearData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final Long id = rs.getLong("id");
            final String name = rs.getString("name");
            final String shortName = rs.getString("shortName");
            final String description = rs.getString("description");
            final Integer statusEnum = JdbcSupport.getInteger(rs, "statusEnum");
            final LocalDate startDate = JdbcSupport.getLocalDate(rs, "startDate");
            final LocalDate endDate = JdbcSupport.getLocalDate(rs, "endDate");
    
            final EnumOptionData status = EDXAcademicYearEnumeration.academicYearStatusType(statusEnum);

            return new EDXAcademicYearData(id,name,shortName,description,startDate,endDate,status);
        }

    }
    
    @Transactional(readOnly=true)
	@Override
	public EDXAcademicYearData retriveOne(Long academicYearId) {
    	this.context.authenticatedUser();
    	try {
             final EDXAcademicYearMapper rm = new EDXAcademicYearMapper();

             final String sql = " select " + rm.schema() + " where ay.id = ?";

             return this.jdbcTemplate.queryForObject(sql, rm, new Object[] { academicYearId });
         } catch (final EmptyResultDataAccessException e) {
             throw new EDXAcademicYearNotFoundException(academicYearId);
         }
	}
    
    @Transactional(readOnly=true)
	@Override
	//Note - we are excluding the deleted academic year. Please see EDXAcademicYearStatus for more details
	public Collection<EDXAcademicYearData> retriveAll() {
		this.context.authenticatedUser();
    	try {
             final EDXAcademicYearMapper rm = new EDXAcademicYearMapper();

             final String sql = " select " + rm.schema() + " where ay.status_enum != 700 ";

             return this.jdbcTemplate.query(sql, rm, new Object[] {});
         } catch (final EmptyResultDataAccessException e) {
             throw new EDXAcademicYearNotFoundException(null);
         }
	}
    
    
    
    
}
