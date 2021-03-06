/*
 * Copyright (c) 2016 Red Hat, Inc. and/or its affiliates.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Cheng Fang - Initial API and implementation
 */

package org.jberet.rest.client;

import java.net.URI;
import java.util.Properties;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.jberet.rest.entity.JobExecutionEntity;
import org.jberet.rest.entity.JobInstanceEntity;
import org.jberet.rest.entity.StepExecutionEntity;
import org.jberet.rest.resource.JobExecutionResource;
import org.jberet.rest.resource.JobInstanceResource;
import org.jberet.rest.resource.JobResource;
import org.jberet.rest.resource.JobScheduleResource;
import org.jberet.schedule.JobSchedule;
import org.jberet.schedule.JobScheduleConfig;

public class BatchClient {
    private final Client client;
    private final String restUrl;

    public BatchClient(final String restUrl) {
        this(ClientBuilder.newClient(), restUrl);
    }

    public BatchClient(final Client client, final String restUrl) {
        this.client = client;
        this.restUrl = restUrl;
    }

    public Client getClient() {
        return client;
    }

    public String getRestUrl() {
        return restUrl;
    }

    public JobExecutionEntity startJob(final String jobXmlName, final Properties queryParams) throws Exception {
        final URI uri = getJobUriBuilder("start").resolveTemplate("jobXmlName", jobXmlName).build();
        WebTarget target = target(uri, queryParams);
        return target.request().post(Entity.entity(null, MediaType.APPLICATION_JSON_TYPE), JobExecutionEntity.class);
    }

    public JobExecutionEntity restartJobExecution(final long jobExecutionId, final Properties queryParams) throws Exception {
        final URI uri = getJobExecutionUriBuilder("restart").resolveTemplate("jobExecutionId", jobExecutionId).build();
        WebTarget target = target(uri, queryParams);
        return target.request().post(Entity.entity(null, MediaType.APPLICATION_JSON_TYPE), JobExecutionEntity.class);
    }

    public JobExecutionEntity restartJobExecution(final String jobXmlName, final Properties queryParams) throws Exception {
        final URI uri = getJobUriBuilder("restart").resolveTemplate("jobXmlName", jobXmlName).build();
        WebTarget target = target(uri, queryParams);
        return target.request().post(Entity.entity(null, MediaType.APPLICATION_JSON_TYPE), JobExecutionEntity.class);
    }

    public JobInstanceEntity[] getJobInstances(final String jobName, final int start, final int count)
            throws Exception {
        final URI uri = getJobInstanceUriBuilder(null).build();
        final WebTarget target = target(uri)
                .queryParam("jobName", jobName)
                .queryParam("start", start)
                .queryParam("count", count);
        return target.request().get(JobInstanceEntity[].class);
    }

    public WebTarget target(final URI uri) {
        return client.target(uri);
    }

    public WebTarget target(final URI uri, final Properties props) {
        WebTarget result = client.target(uri);

        if (props == null) {
            return result;
        } else {
            for (final String k : props.stringPropertyNames()) {
                result = result.queryParam(k, props.getProperty(k));
            }
        }
        return result;
    }

    public JobExecutionEntity getJobExecution(final long jobExecutionId) {
        final URI uri = getJobExecutionUriBuilder(null).path(String.valueOf(jobExecutionId)).build();
        final WebTarget target = client.target(uri);
        return target.request().get(JobExecutionEntity.class);
    }

    public StepExecutionEntity[] getStepExecutions(final long jobExecutionId) {
        final URI uri = getJobExecutionUriBuilder("getStepExecutions")
                .resolveTemplate("jobExecutionId", jobExecutionId).build();
        WebTarget target = target(uri);
        return target.request().get(StepExecutionEntity[].class);
    }

    public JobSchedule getJobSchedule(final String scheduleId) {
        final URI uri = getJobScheduleUriBuilder("getJobSchedule")
                .resolveTemplate("scheduleId", scheduleId).build();
        WebTarget target = target(uri);
        return target.request().accept(MediaType.APPLICATION_JSON_TYPE).get(JobSchedule.class);
    }

    public JobSchedule[] getJobSchedules() {
        final URI uri = getJobScheduleUriBuilder("getJobSchedules").build();
        final WebTarget target = target(uri);
        return target.request().accept(MediaType.APPLICATION_JSON_TYPE).get(JobSchedule[].class);
    }

    public boolean cancelJobSchedule(final String scheduleId) {
        final URI uri = getJobScheduleUriBuilder("cancel")
                .resolveTemplate("scheduleId", scheduleId).build();
        WebTarget target = target(uri);
        return target.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(null, MediaType.APPLICATION_JSON_TYPE), boolean.class);
    }

    public JobSchedule schedule(final JobScheduleConfig scheduleConfig) {
        final URI uri;
        if (scheduleConfig.getJobName() != null) {
            uri = getJobUriBuilder("schedule").resolveTemplate("jobXmlName", scheduleConfig.getJobName()).build();
        } else {
            uri = getJobExecutionUriBuilder("schedule")
                    .resolveTemplate("jobExecutionId", scheduleConfig.getJobExecutionId()).build();
        }
        WebTarget target = target(uri);
        return target.request().post(Entity.json(scheduleConfig), JobSchedule.class);
    }


    public UriBuilder getUriBuilder(final Class<?> cls, final String methodName) {
        UriBuilder uriBuilder = UriBuilder.fromPath(restUrl).path(cls);
        if (methodName != null) {
            uriBuilder = uriBuilder.path(cls, methodName);
        }
        return uriBuilder;
    }

    public UriBuilder getJobUriBuilder(final String methodName) {
        return getUriBuilder(JobResource.class, methodName);
    }

    public UriBuilder getJobInstanceUriBuilder(final String methodName) {
        return getUriBuilder(JobInstanceResource.class, methodName);
    }

    public UriBuilder getJobExecutionUriBuilder(final String methodName) {
        return getUriBuilder(JobExecutionResource.class, methodName);
    }

    public UriBuilder getJobScheduleUriBuilder(final String methodName) {
        return getUriBuilder(JobScheduleResource.class, methodName);
    }

}
