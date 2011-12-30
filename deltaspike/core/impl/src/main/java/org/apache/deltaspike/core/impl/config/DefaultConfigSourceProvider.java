/*
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
package org.apache.deltaspike.core.impl.config;

import org.apache.deltaspike.core.spi.config.ConfigSource;
import org.apache.deltaspike.core.spi.config.ConfigSourceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation which uses:
 * <ol>
 * <li>SystemPropertyConfigSource</li>
 * <li>EnvironmentPropertyConfigSource</li>
 * <li>LocalJndiConfigSource</li>
 * <li>PropertyFileConfigSource</li>
 * </ol>
 */
public class DefaultConfigSourceProvider implements ConfigSourceProvider
{
    private List<ConfigSource> configSources = new ArrayList<ConfigSource>();

    /**
     * Default constructor which adds the {@link ConfigSource} implementations which are supported by default
     */
    public DefaultConfigSourceProvider()
    {
        this.configSources.add(new SystemPropertyConfigSource());
        this.configSources.add(new EnvironmentPropertyConfigSource());
        this.configSources.add(new LocalJndiConfigSource());
        this.configSources.add(new PropertyFileConfigSource());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConfigSource> getConfigSources()
    {
        return this.configSources;
    }
}