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
package org.apache.fineract.organisation.academic.data;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.joda.time.LocalDate;

/**
 * @author Ram
 * */
public class EDXAcademicYearData {
	@SuppressWarnings("unused")
    private final Long id;
    @SuppressWarnings("unused")
    private final String name;
    @SuppressWarnings("unused")
    private final String shortName;
    @SuppressWarnings("unused")
    private final String description;
    @SuppressWarnings("unused")
    private final LocalDate startDate;
    @SuppressWarnings("unused")
    private final LocalDate endDate;
    @SuppressWarnings("unused")
    private final EnumOptionData status;
    
	public EDXAcademicYearData(Long id, String name, String shortName, String description, LocalDate startDate,
			LocalDate endDate, EnumOptionData status) {
		super();
		this.id = id;
		this.name = name;
		this.shortName = shortName;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

}
