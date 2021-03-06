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

package org.jberet.schedule;

import java.io.Serializable;
import java.util.Properties;
import javax.ejb.ScheduleExpression;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class JobScheduleConfig implements Serializable {
    private static final long serialVersionUID = 7225109864510680914L;

    final String jobName;

    final long jobExecutionId;

    final Properties jobParameters;

    final ScheduleExpression scheduleExpression;

    final long initialDelay;

    final long afterDelay;

    final long interval;

    final boolean persistent;

    public JobScheduleConfig() {
        this(null, 0, null, null, 0, 0, 0, false);
    }

    JobScheduleConfig(final String jobName,
                             final long jobExecutionId,
                             final Properties jobParameters,
                             final ScheduleExpression scheduleExpression,
                             final long initialDelay,
                             final long afterDelay,
                             final long interval,
                             final boolean persistent) {
        this.jobName = jobName;
        this.jobExecutionId = jobExecutionId;
        this.jobParameters = jobParameters;
        this.scheduleExpression = scheduleExpression;
        this.initialDelay = initialDelay;
        this.afterDelay = afterDelay;
        this.interval = interval;
        this.persistent = persistent;
    }

    public boolean isRepeating() {
        return afterDelay > 0 || interval > 0 || scheduleExpression != null;
    }

    public String getJobName() {
        return jobName;
    }

    public long getJobExecutionId() {
        return jobExecutionId;
    }

    public Properties getJobParameters() {
        return jobParameters;
    }

    public ScheduleExpression getScheduleExpression() {
        return scheduleExpression;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public long getAfterDelay() {
        return afterDelay;
    }

    public long getInterval() {
        return interval;
    }

    public boolean isPersistent() {
        return persistent;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JobScheduleConfig that = (JobScheduleConfig) o;

        if (jobExecutionId != that.jobExecutionId) return false;
        if (initialDelay != that.initialDelay) return false;
        if (afterDelay != that.afterDelay) return false;
        if (interval != that.interval) return false;
        if (persistent != that.persistent) return false;
        if (jobName != null ? !jobName.equals(that.jobName) : that.jobName != null) return false;
        if (jobParameters != null ? !jobParameters.equals(that.jobParameters) : that.jobParameters != null)
            return false;
        return scheduleExpression != null ? scheduleExpression.equals(that.scheduleExpression) : that.scheduleExpression == null;

    }

    @Override
    public int hashCode() {
        int result = jobName != null ? jobName.hashCode() : 0;
        result = 31 * result + (int) (jobExecutionId ^ (jobExecutionId >>> 32));
        result = 31 * result + (jobParameters != null ? jobParameters.hashCode() : 0);
        result = 31 * result + (scheduleExpression != null ? scheduleExpression.hashCode() : 0);
        result = 31 * result + (int) (initialDelay ^ (initialDelay >>> 32));
        result = 31 * result + (int) (afterDelay ^ (afterDelay >>> 32));
        result = 31 * result + (int) (interval ^ (interval >>> 32));
        result = 31 * result + (persistent ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JobScheduleInfo{" +
                "jobName='" + jobName + '\'' +
                ", jobExecutionId=" + jobExecutionId +
                ", jobParameters=" + jobParameters +
                ", initialDelay=" + initialDelay +
                ", afterDelay=" + afterDelay +
                ", interval=" + interval +
                ", persistent=" + persistent +
                ", scheduleExpression='" + scheduleExpression + '\'' +
                '}';
    }
}
