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
package org.apache.fineract.organisation.academic.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.InvalidJsonException;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.organisation.academic.api.EDXAcademicYearApiConstants;
import org.apache.fineract.organisation.holiday.api.HolidayApiConstants;
import org.apache.fineract.organisation.office.service.OfficeWritePlatformServiceJpaRepositoryImpl;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

/**
 * Deserializer of JSON for Academics API.
 * @author Ram
 */
@Component
public class EDXAcademicYearCommandFromApiJsonDeserializer {
	private final FromJsonHelper fromApiJsonHelper;
   
	@Autowired
	public EDXAcademicYearCommandFromApiJsonDeserializer(FromJsonHelper fromApiJsonHelper) {
		super();
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	public void validateForCreate(final String json) {
		if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }
		
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
		this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, EDXAcademicYearApiConstants.ACADEMIC_YEAR_CREATE_OR_UPDATE_REQUEST_DATA_PARAMETERS);
		
		final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource(EDXAcademicYearApiConstants.ACADEMIC_YEAR_RESOURCE_NAME);
        
        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        final String name = this.fromApiJsonHelper.extractStringNamed(EDXAcademicYearApiConstants.nameParamName, element);
        baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.nameParamName).value(name).notBlank().notExceedingLengthOf(250);
        
        final String shortName = this.fromApiJsonHelper.extractStringNamed(EDXAcademicYearApiConstants.shortNameParamName, element);
        baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.shortNameParamName).value(shortName).notBlank().notExceedingLengthOf(100);
        
        final String description = this.fromApiJsonHelper.extractStringNamed(EDXAcademicYearApiConstants.descriptionParamName, element);
        baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.descriptionParamName).value(description).notExceedingLengthOf(250);
        
        final LocalDate startDate = this.fromApiJsonHelper.extractLocalDateNamed(EDXAcademicYearApiConstants.startDateParamName, element);
        baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.startDateParamName).value(startDate).notNull();
        
        final LocalDate endDate = this.fromApiJsonHelper.extractLocalDateNamed(EDXAcademicYearApiConstants.endDateParamName, element);
        baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.endDateParamName).value(endDate).notNull();
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
        
	}
	
	private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
	        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
	                "Validation errors exist.", dataValidationErrors);
	        }
	}
	
	public void validateForUpdate(final String json){
		
		if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }
		
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
		final JsonElement element = this.fromApiJsonHelper.parse(json);
		final List<ApiParameterError> dataValidationErrors = new ArrayList<>();

	    final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
	                .resource(EDXAcademicYearApiConstants.ACADEMIC_YEAR_RESOURCE_NAME);
	    
	    if (this.fromApiJsonHelper.parameterExists(EDXAcademicYearApiConstants.nameParamName, element)) {
            final String name = this.fromApiJsonHelper.extractStringNamed(EDXAcademicYearApiConstants.nameParamName, element);
            baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.nameParamName).value(name).notNull().notExceedingLengthOf(250);
        }
	    
	    if (this.fromApiJsonHelper.parameterExists(EDXAcademicYearApiConstants.shortNameParamName, element)) {
            final String shortName = this.fromApiJsonHelper.extractStringNamed(EDXAcademicYearApiConstants.shortNameParamName, element);
            baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.shortNameParamName).value(shortName).notNull().notExceedingLengthOf(100);
        }
	    
	    if (this.fromApiJsonHelper.parameterExists(EDXAcademicYearApiConstants.descriptionParamName, element)) {
            final String description = this.fromApiJsonHelper.extractStringNamed(EDXAcademicYearApiConstants.descriptionParamName, element);
            baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.descriptionParamName).value(description).notNull().notExceedingLengthOf(100);
        }
	    
	    if (this.fromApiJsonHelper.parameterExists(EDXAcademicYearApiConstants.startDateParamName, element)) {
            final LocalDate startDate = this.fromApiJsonHelper.extractLocalDateNamed(EDXAcademicYearApiConstants.startDateParamName, element);
            baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.startDateParamName).value(startDate).notNull();
        }
	    
	    if (this.fromApiJsonHelper.parameterExists(EDXAcademicYearApiConstants.endDateParamName, element)) {
            final LocalDate endDate = this.fromApiJsonHelper.extractLocalDateNamed(EDXAcademicYearApiConstants.endDateParamName, element);
            baseDataValidator.reset().parameter(EDXAcademicYearApiConstants.endDateParamName).value(endDate).notNull();
        }
	    
	    throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}
	
}
