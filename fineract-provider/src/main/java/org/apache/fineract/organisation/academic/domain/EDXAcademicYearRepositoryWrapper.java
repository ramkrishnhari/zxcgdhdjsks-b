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
import org.apache.fineract.organisation.academic.exception.EDXAcademicYearNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ram
 * */
@Service
public class EDXAcademicYearRepositoryWrapper {
  
	private final EDXAcademicYearRepository academicYearRepository;
    
	@Autowired
	public EDXAcademicYearRepositoryWrapper(EDXAcademicYearRepository academicYearRepository) {
		super();
		this.academicYearRepository = academicYearRepository;
	}
	
	
	public EDXAcademicYear findOneWithNotFoundDetection(final Long id) {
        final EDXAcademicYear academicYear = this.academicYearRepository.findOne(id);
        if (academicYear == null) { throw new EDXAcademicYearNotFoundException(id); }
        return academicYear;
    }

    public void save(final EDXAcademicYear academicYear) {
        this.academicYearRepository.save(academicYear);
    }

    public void save(final Iterable<EDXAcademicYear> academicYear) {
        this.academicYearRepository.save(academicYear);
    }

    public void saveAndFlush(final EDXAcademicYear academicYear) {
        this.academicYearRepository.saveAndFlush(academicYear);
    }

    public void delete(final EDXAcademicYear academicYear) {
        this.academicYearRepository.delete(academicYear);
    }
	
}
