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

import org.apache.fineract.infrastructure.core.data.EnumOptionData;
/**
 * @author Ram
 * */
public class EDXAcademicYearEnumeration {

	 public static EnumOptionData academicYearStatusType(final int id) {
	        return academicYearStatusType(EDXAcademicYearStatus.fromInt(id));
	    }
	 
	 public static EnumOptionData academicYearStatusType(final EDXAcademicYearStatus type) {
	        EnumOptionData optionData = null;
	        switch (type) {
	            case INVALID:
	                optionData = new EnumOptionData(EDXAcademicYearStatus.INVALID.getValue().longValue(), EDXAcademicYearStatus.INVALID.getCode(),
	                        "Invalid");
	            break;
	            case PENDING:
	                optionData = new EnumOptionData(EDXAcademicYearStatus.PENDING.getValue().longValue(), EDXAcademicYearStatus.INVALID.getCode(),
	                        "Pending for activation");
	            break;
	            case ACTIVE:
	                optionData = new EnumOptionData(EDXAcademicYearStatus.ACTIVE.getValue().longValue(), EDXAcademicYearStatus.ACTIVE.getCode(),
	                        "Active");
	            break;
	            case DELETED:
	                optionData = new EnumOptionData(EDXAcademicYearStatus.DELETED.getValue().longValue(), EDXAcademicYearStatus.DELETED.getCode(),
	                        "Deleted");
	            break;
	            
	            case CLOSED:
	                optionData = new EnumOptionData(EDXAcademicYearStatus.CLOSED.getValue().longValue(), EDXAcademicYearStatus.CLOSED.getCode(),
	                        "Closed");
	            break;
	        }
	        return optionData;
	    }
}
