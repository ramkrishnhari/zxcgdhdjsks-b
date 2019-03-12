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
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.exception.UnrecognizedQueryParamException;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.organisation.academic.data.EDXAcademicYearData;
import org.apache.fineract.organisation.academic.service.EDXAcademicYearReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Ram
*/
@Path("/academics")
@Component
@Scope("singleton")
public class EDXAcademicYearApiResource {

	private final DefaultToApiJsonSerializer<EDXAcademicYearData> toApiJsonSerializer;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final PlatformSecurityContext context;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final EDXAcademicYearReadPlatformService academicYearReadService;
    
    @Autowired
	public EDXAcademicYearApiResource(final DefaultToApiJsonSerializer<EDXAcademicYearData> toApiJsonSerializer,
			final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
			final PlatformSecurityContext context, final ApiRequestParameterHelper apiRequestParameterHelper,
			final EDXAcademicYearReadPlatformService academicYearReadService) {
		super();
		this.toApiJsonSerializer = toApiJsonSerializer;
		this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		this.context = context;
		this.apiRequestParameterHelper = apiRequestParameterHelper;
		this.academicYearReadService = academicYearReadService;
	}
    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createNewAcademicYear(final String apiRequestBodyAsJson) {
    	
    	final CommandWrapper commandRequest = new CommandWrapperBuilder().createAcademicYear().withJson(apiRequestBodyAsJson).build();
    	
    	final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
    	
    	return this.toApiJsonSerializer.serialize(result);
    }
    
    
    @PUT
    @Path("{academicYearId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateAcademicYear(@PathParam("academicYearId") final Long academicYearId, final String jsonRequestBody) {

        final CommandWrapper commandRequest = new CommandWrapperBuilder().updateAcademicYear(academicYearId).withJson(jsonRequestBody).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);

        return this.toApiJsonSerializer.serialize(result);
    }
    
    
    @DELETE
    @Path("{academicYearId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String deleteAcademicYear(@PathParam("academicYearId") final Long academicYearId) {
        final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteAcademicYear(academicYearId).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);
    }
    
    
    @GET
    @Path("{academicYearId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveOne(@PathParam("academicYearId") final Long academicYearId, @Context final UriInfo uriInfo) {

        this.context.authenticatedUser().validateHasReadPermission(EDXAcademicYearApiConstants.ACADEMIC_YEAR_RESOURCE_NAME);

        final EDXAcademicYearData academicYearData = this.academicYearReadService.retriveOne(academicYearId);

        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());

        return this.toApiJsonSerializer.serialize(settings, academicYearData, EDXAcademicYearApiConstants.ACADEMIC_YEAR_RESPONSE_DATA_PARAMETERS);
    }
    
    
    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrievAllAcademicYears(@Context final UriInfo uriInfo) {

        this.context.authenticatedUser().validateHasReadPermission(EDXAcademicYearApiConstants.ACADEMIC_YEAR_RESOURCE_NAME);

        final Collection<EDXAcademicYearData> academicYearData = this.academicYearReadService.retriveAll();

        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());

        return this.toApiJsonSerializer.serialize(settings, academicYearData, EDXAcademicYearApiConstants.ACADEMIC_YEAR_RESPONSE_DATA_PARAMETERS);
    }
    
    @POST
    @Path("{academicYearId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String handleCommands(@PathParam("academicYearId") final Long academicYearId, @QueryParam("command") final String commandParam,
            final String apiRequestBodyAsJson) {

        String jsonApiRequest = apiRequestBodyAsJson;
        if (StringUtils.isBlank(jsonApiRequest)) {
            jsonApiRequest = "{}";
        }

        final CommandWrapperBuilder builder = new CommandWrapperBuilder().withJson(jsonApiRequest);

        CommandProcessingResult result = null;
        if (is(commandParam, "activate")) {
            final CommandWrapper commandRequest = builder.activateAcademicYear(academicYearId).build();
            result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        }else if(is(commandParam, "close")){
        	 final CommandWrapper commandRequest = builder.closeAcademicYear(academicYearId).build();
             result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        }

        if (result == null) { throw new UnrecognizedQueryParamException("command", commandParam, new Object[] { "activate" }); }

        return this.toApiJsonSerializer.serialize(result);
    }
    
    
    private boolean is(final String commandParam, final String commandValue) {
        return StringUtils.isNotBlank(commandParam) && commandParam.trim().equalsIgnoreCase(commandValue);
    }
    
    
}
