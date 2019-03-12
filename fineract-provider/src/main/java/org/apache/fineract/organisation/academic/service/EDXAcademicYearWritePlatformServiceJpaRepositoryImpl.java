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

import java.util.Map;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.core.exception.PlatformDataIntegrityException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.organisation.academic.api.EDXAcademicYearApiConstants;
import org.apache.fineract.organisation.academic.domain.EDXAcademicYear;
import org.apache.fineract.organisation.academic.domain.EDXAcademicYearRepositoryWrapper;
import org.apache.fineract.organisation.academic.exception.EDXAcademicYearDateException;
import org.apache.fineract.organisation.academic.serialization.EDXAcademicYearCommandFromApiJsonDeserializer;
import org.apache.fineract.useradministration.domain.AppUser;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ram
 * */
@Service
public class EDXAcademicYearWritePlatformServiceJpaRepositoryImpl implements EDXAcademicYearWritePlatformService{

	private final static Logger logger = LoggerFactory.getLogger(EDXAcademicYearWritePlatformServiceJpaRepositoryImpl.class);
	
	private final PlatformSecurityContext context;
	private final FromJsonHelper fromApiJsonHelper;
	private final EDXAcademicYearCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final EDXAcademicYearRepositoryWrapper academicYearRepository;
	
	@Autowired
	public EDXAcademicYearWritePlatformServiceJpaRepositoryImpl(final PlatformSecurityContext context,
			final FromJsonHelper fromApiJsonHelper, final EDXAcademicYearCommandFromApiJsonDeserializer fromApiJsonDeserializer,
			final EDXAcademicYearRepositoryWrapper academicYearRepository) {
		super();
		this.context = context;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.fromApiJsonDeserializer = fromApiJsonDeserializer;
		this.academicYearRepository = academicYearRepository;
	}

    @Transactional
	@Override
	public CommandProcessingResult createAcademicYear(JsonCommand command) {
		try{
			final AppUser currentUser = this.context.authenticatedUser();
            this.fromApiJsonDeserializer.validateForCreate(command.json());
            validateInputDates(command);
			
            EDXAcademicYear newAcademicYear = EDXAcademicYear.createNew(currentUser, command);
            this.academicYearRepository.save(newAcademicYear);
            
            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(newAcademicYear.getId()).build();
		}catch (final DataIntegrityViolationException dve) {
            handleDataIntegrityIssues(command, dve.getMostSpecificCause(), dve);
            return CommandProcessingResult.empty();
        }catch (final PersistenceException dve) {
        	Throwable throwable = ExceptionUtils.getRootCause(dve.getCause()) ;
        	handleDataIntegrityIssues(command, throwable, dve);
        	return CommandProcessingResult.empty();
        }
	}
    
    private void validateInputDates(final JsonCommand command) {
    	final LocalDate startDate = command.localDateValueOfParameterNamed(EDXAcademicYearApiConstants.startDateParamName);
        final LocalDate endDate = command.localDateValueOfParameterNamed(EDXAcademicYearApiConstants.endDateParamName);
       
        this.validateInputDates(startDate, endDate);
        
    }
    
    private void validateInputDates(final LocalDate startDate, final LocalDate endDate){
    	String defaultUserMessage = "";
    	
    	if (endDate.isBefore(startDate)) {
            defaultUserMessage = "Academic year end date cannot be before the Academic year start date.";
            throw new EDXAcademicYearDateException("to.date.cannot.be.before.from.date", defaultUserMessage, startDate.toString(),
            		endDate.toString());
        }
    }
    
    private void handleDataIntegrityIssues(final JsonCommand command, final Throwable realCause, final Exception dve) {
        if (realCause.getMessage().contains("academic_year_name")) {
            final String name = command.stringValueOfParameterNamed("name");
            throw new PlatformDataIntegrityException("error.msg.academic.year.duplicate.name", "Academic Year with name `" + name + "` already exists",
                    "name", name);
        }

        logger.error(dve.getMessage(), dve);
        throw new PlatformDataIntegrityException("error.msg.office.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource.");
    }

    @Transactional
	@Override
	public CommandProcessingResult updateAcademicYear(JsonCommand command) {
		try{
			this.context.authenticatedUser();
			this.fromApiJsonDeserializer.validateForUpdate(command.json());
			
			final EDXAcademicYear academicYear = this.academicYearRepository.findOneWithNotFoundDetection(command.entityId());
			
			Map<String, Object> changes = academicYear.update(command);
			
			validateInputDates(academicYear.getStartDateLocalDate(),academicYear.getEndDateLocalDate());
			
			if(changes !=null && !changes.isEmpty()){
				this.academicYearRepository.saveAndFlush(academicYear);
			}
			return new CommandProcessingResultBuilder()
					.withCommandId(command.commandId())
					.withEntityId(academicYear.getId())
					.with(changes)
					.build();

		} catch (final DataIntegrityViolationException dve) {
            handleDataIntegrityIssues(command, dve.getMostSpecificCause(), dve);
            return CommandProcessingResult.empty();
        }catch (final PersistenceException dve) {
        	Throwable throwable = ExceptionUtils.getRootCause(dve.getCause()) ;
        	handleDataIntegrityIssues(command, throwable, dve);
        	return CommandProcessingResult.empty();
        }
	}

    @Transactional
	@Override
	public CommandProcessingResult deleteAcademicYear(Long academicYearId) {
	  this.context.authenticatedUser();
	  final EDXAcademicYear academicYear = this.academicYearRepository.findOneWithNotFoundDetection(academicYearId);
	  academicYear.delete();
	  this.academicYearRepository.saveAndFlush(academicYear);
	  return new CommandProcessingResultBuilder()
			     .withEntityId(academicYearId)
			     .build();
	}

    @Transactional
	@Override
	public CommandProcessingResult activateAcademicYear(Long academicYearId) {
      this.context.authenticatedUser();
   	  final EDXAcademicYear academicYear = this.academicYearRepository.findOneWithNotFoundDetection(academicYearId);
   	  academicYear.activate();
   	  this.academicYearRepository.saveAndFlush(academicYear);
	  return new CommandProcessingResultBuilder()
			     .withEntityId(academicYearId)
			     .build();
	}

    @Transactional
	@Override
	public CommandProcessingResult closeAcademicYear(Long academicYearId) {
      this.context.authenticatedUser();
   	  final EDXAcademicYear academicYear = this.academicYearRepository.findOneWithNotFoundDetection(academicYearId);
   	  academicYear.close();
      this.academicYearRepository.saveAndFlush(academicYear);
	  return new CommandProcessingResultBuilder()
			     .withEntityId(academicYearId)
			     .build();
	}
    
    
}
