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
/**
 * Enum representation of academic year status states.
 * @author Ram
 */
public enum EDXAcademicYearStatus {
	INVALID(0, "academicYearStatusType.invalid"), //
	PENDING(100,"academicYearStatusType.pending"),
	ACTIVE(300, "academicYearStatusType.active"), //
	CLOSED(600, "academicYearStatusType.closed"), 
	DELETED(700,"academicYearStatusType.deleted");
	/**Note- If academic year closed as per Curriculum then the status will be  CLOSED i.e 600 
	 * If academic year has any issues and user wants to delete it and create a new in that case status will be DELETED i.e. 700
	 * IMP- The content w.r.t to DELETED status will not be appear anywhere through entire application currently.
	 * TODO-  AS per future scope we need to handle the DELETED status
	 * */
	
	private final Integer value;
    private final String code;
    
	private EDXAcademicYearStatus(Integer value, String code) {
		this.value = value;
		this.code = code;
	}
    
	public static EDXAcademicYearStatus fromInt(final Integer statusValue){
		EDXAcademicYearStatus enumeration = EDXAcademicYearStatus.INVALID;
		switch (statusValue) {
		    
		    case 100:
            enumeration = EDXAcademicYearStatus.PENDING;
            break;
             
		    case 300:
             enumeration = EDXAcademicYearStatus.ACTIVE;
            break;
            
		    case 600:
                enumeration = EDXAcademicYearStatus.CLOSED;
            break;
            
		    case 700:
                enumeration = EDXAcademicYearStatus.DELETED;
            break;
		}
		return enumeration;
	}

	public Integer getValue() {
		return value;
	}

	public String getCode() {
		return code;
	}
    
	public boolean isActive() {
	   return this.value.equals(EDXAcademicYearStatus.ACTIVE.getValue());
	}
	  
	public boolean isClosed() {
	    return this.value.equals(EDXAcademicYearStatus.CLOSED.getValue());
	}
	
	public boolean isDeleted(){
		return this.value.equals(EDXAcademicYearStatus.DELETED.getValue());
	}
}
