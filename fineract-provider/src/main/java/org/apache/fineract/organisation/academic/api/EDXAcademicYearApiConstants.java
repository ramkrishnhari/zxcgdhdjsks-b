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
package org.apache.fineract.organisation.academic.api;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * @author Ram
 * */
public class EDXAcademicYearApiConstants {
	
	public static final String ACADEMIC_YEAR_RESOURCE_NAME = "academic_year";
	
	// general
    public static final String localeParamName = "locale";
    public static final String dateFormatParamName = "dateFormat";
    
   // request parameters
    public static final String idParamName = "id";
    public static final String nameParamName = "name";
    public static final String shortNameParamName = "shortName";
    public static final String startDateParamName = "startDate";
    public static final String endDateParamName = "endDate";
    public static final String descriptionParamName = "description";
    public static final String activeParamName = "active";
    public static final String statusParamName = "status";
    
    public static final Set<String> ACADEMIC_YEAR_CREATE_OR_UPDATE_REQUEST_DATA_PARAMETERS = new HashSet<>(Arrays.asList(
    		nameParamName,shortNameParamName,descriptionParamName,activeParamName,startDateParamName,endDateParamName,localeParamName,dateFormatParamName));
    
    public static final Set<String> ACADEMIC_YEAR_RESPONSE_DATA_PARAMETERS = new HashSet<> (Arrays.asList(
    		nameParamName,shortNameParamName,descriptionParamName,activeParamName,startDateParamName,endDateParamName,statusParamName));
    
}
