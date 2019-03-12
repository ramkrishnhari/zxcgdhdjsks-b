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
package org.apache.fineract.organisation.academic.domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.organisation.academic.api.EDXAcademicYearApiConstants;
import org.apache.fineract.useradministration.domain.AppUser;
import org.joda.time.LocalDate;
/**
 * @author Ram
 */
@Entity
@Table(name = "m_academic_year", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }, name = "academic_year_name") })
public class EDXAcademicYear extends AbstractPersistableCustom<Long> {

   @Column(name = "name", unique = true, nullable = false, length = 250)
   private String name;
   
   @Column(name = "short_name", unique = true, nullable = false, length = 100)
   private String shortName;
   
   @Column(name = "description", nullable = true, length = 250)
   private String description;
   
   @Column(name = "start_date", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date startDate;
   
   @Column(name = "end_date", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date endDate;
   
   /**
    * A value from {@link EDXAcademicYearStatus}.
    */
   @Column(name = "status_enum", nullable = false)
   private Integer status;
   
   @ManyToOne(optional = true, fetch=FetchType.LAZY)
   @JoinColumn(name = "created_by_userid", nullable = true)
   private AppUser createdBy;
   
   @ManyToOne(optional = true, fetch=FetchType.LAZY)
   @JoinColumn(name = "modified_by_userid", nullable = true)
   private AppUser modifiedBy;
   
   @Column(name = "created_on_date", nullable = true)
   @Temporal(TemporalType.DATE)
   private Date createdOnDate;
   
   @Column(name = "modified_on_date", nullable = true)
   @Temporal(TemporalType.DATE)
   private Date modifiedOnDate;

   private EDXAcademicYear(final String name, final String shortName, final String description, final LocalDate startDate, final LocalDate endDate, 
		   final EDXAcademicYearStatus status, final AppUser createdBy,
		   final AppUser modifiedBy, final LocalDate createdOnDate, final LocalDate modifiedOnDate) {
		super();
		this.name = name;
		this.shortName = shortName;
		if(startDate!=null){
			this.startDate = startDate.toDateTimeAtStartOfDay().toDate();
		}
		if(endDate != null){
			this.endDate = startDate.toDateTimeAtStartOfDay().toDate();
		}
		
		this.status = status.getValue();
		
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		
		if(createdOnDate != null){
			this.createdOnDate  = createdOnDate.toDateTimeAtStartOfDay().toDate();
		}
	    
		if(modifiedOnDate != null){
			this.modifiedOnDate = modifiedOnDate.toDateTimeAtStartOfDay().toDate();
		}
		
		this.description = description;
	}
   
   
   public static EDXAcademicYear createNew(final AppUser currentUser,final JsonCommand command){
	   
	   final String name = command.stringValueOfParameterNamed(EDXAcademicYearApiConstants.nameParamName);
	   final String shortName = command.stringValueOfParameterNamed(EDXAcademicYearApiConstants.shortNameParamName);
	   final String description = command.stringValueOfParameterNamed(EDXAcademicYearApiConstants.descriptionParamName);
	   final LocalDate startDate = command.localDateValueOfParameterNamed(EDXAcademicYearApiConstants.startDateParamName);
	   final LocalDate endDate = command.localDateValueOfParameterNamed(EDXAcademicYearApiConstants.endDateParamName);
	   
	   //note - user can create a future academic year but in that case also created date should be system current date
	   LocalDate createdOnDate = new LocalDate();
	   
	   final AppUser modifiedBy = null;
	   final LocalDate modifiedOnDate = null;
	   // note - academic year must be created with pending status and user need to update it to active state
	   EDXAcademicYearStatus status = EDXAcademicYearStatus.PENDING;   
	   return new EDXAcademicYear(name,shortName,description,startDate,endDate,status,currentUser,modifiedBy,createdOnDate,modifiedOnDate);
	   
   }
   
   
   public Map<String, Object> update(final JsonCommand command) {
	   final Map<String, Object> actualChanges = new LinkedHashMap<>(7);
	   
	   final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
       final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("academic_year" + ".update");
       
       final EDXAcademicYearStatus currentStatus = EDXAcademicYearStatus.fromInt(this.status);
       
       final String dateFormatAsInput = command.dateFormat();
       final String localeAsInput = command.locale();
       
       if (command.isChangeInStringParameterNamed(EDXAcademicYearApiConstants.nameParamName, this.name)) {
           final String newValue = command.stringValueOfParameterNamed(EDXAcademicYearApiConstants.nameParamName);
           actualChanges.put(EDXAcademicYearApiConstants.nameParamName, newValue);
           this.name = StringUtils.defaultIfEmpty(newValue, null);
       }
       
       if (command.isChangeInStringParameterNamed(EDXAcademicYearApiConstants.shortNameParamName, this.shortName)) {
           final String newValue = command.stringValueOfParameterNamed(EDXAcademicYearApiConstants.shortNameParamName);
           actualChanges.put(EDXAcademicYearApiConstants.shortNameParamName, newValue);
           this.shortName = StringUtils.defaultIfEmpty(newValue, null);
       }
       
       if (command.isChangeInStringParameterNamed(EDXAcademicYearApiConstants.descriptionParamName, this.description)) {
           final String newValue = command.stringValueOfParameterNamed(EDXAcademicYearApiConstants.descriptionParamName);
           actualChanges.put(EDXAcademicYearApiConstants.descriptionParamName, newValue);
           this.description = StringUtils.defaultIfEmpty(newValue, null);
       }
       
       if (command.isChangeInLocalDateParameterNamed(EDXAcademicYearApiConstants.startDateParamName, getStartDateLocalDate())) {
           final String valueAsInput = command.stringValueOfParameterNamed(EDXAcademicYearApiConstants.startDateParamName);
           actualChanges.put(EDXAcademicYearApiConstants.startDateParamName, valueAsInput);
           actualChanges.put(EDXAcademicYearApiConstants.dateFormatParamName, dateFormatAsInput);
           actualChanges.put(EDXAcademicYearApiConstants.localeParamName, localeAsInput);
           final LocalDate newValue = command.localDateValueOfParameterNamed(EDXAcademicYearApiConstants.startDateParamName);
           this.startDate = newValue.toDate();
       }
       
       if (command.isChangeInLocalDateParameterNamed(EDXAcademicYearApiConstants.endDateParamName, getEndDateLocalDate())) {
           final String valueAsInput = command.stringValueOfParameterNamed(EDXAcademicYearApiConstants.endDateParamName);
           actualChanges.put(EDXAcademicYearApiConstants.endDateParamName, valueAsInput);
           actualChanges.put(EDXAcademicYearApiConstants.dateFormatParamName, dateFormatAsInput);
           actualChanges.put(EDXAcademicYearApiConstants.localeParamName, localeAsInput);
           final LocalDate newValue = command.localDateValueOfParameterNamed(EDXAcademicYearApiConstants.endDateParamName);
           this.endDate = newValue.toDate();
       }
       
       if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException(dataValidationErrors); }
       
       return actualChanges;
   }
   
   
   public LocalDate getStartDateLocalDate() {
       LocalDate startDate = null;
       if (this.startDate != null) {
    	   startDate = new LocalDate(this.startDate);
       }
       return startDate;
   }
   
   
   public LocalDate getEndDateLocalDate() {
       LocalDate endDate = null;
       if (this.endDate != null) {
    	   endDate = new LocalDate(this.endDate);
       }
       return endDate;
   }
   
   public void delete() {
       final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
       final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("academic_year" + ".delete");

       final EDXAcademicYearStatus currentStatus = EDXAcademicYearStatus.fromInt(this.status);
       if (currentStatus.isDeleted()) {
           baseDataValidator.reset().failWithCodeNoParameterAddedToErrorCode("already.in.deleted.state");
           if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException(dataValidationErrors); }
       }
       this.status = EDXAcademicYearStatus.DELETED.getValue();
   }
   
   public void activate(){
	   final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
       final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("academic_year" + ".delete");

       final EDXAcademicYearStatus currentStatus = EDXAcademicYearStatus.fromInt(this.status);
       if (currentStatus.isActive()) {
           baseDataValidator.reset().failWithCodeNoParameterAddedToErrorCode("already.in.deleted.state");
           if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException(dataValidationErrors); }
       }
       this.status = EDXAcademicYearStatus.ACTIVE.getValue();
   }
   
   public void close(){
	   final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
       final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("academic_year" + ".delete");

       final EDXAcademicYearStatus currentStatus = EDXAcademicYearStatus.fromInt(this.status);
       if (currentStatus.isClosed()) {
           baseDataValidator.reset().failWithCodeNoParameterAddedToErrorCode("already.in.deleted.state");
           if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException(dataValidationErrors); }
       }
       this.status = EDXAcademicYearStatus.CLOSED.getValue();
   }
}
