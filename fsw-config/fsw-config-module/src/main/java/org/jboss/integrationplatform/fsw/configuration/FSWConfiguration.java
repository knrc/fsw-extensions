/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.integrationplatform.fsw.configuration;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Common configurations
 */
public class FSWConfiguration {
    private final ConcurrentHashMap<String, Map<String, String>> configurations = new ConcurrentHashMap<String, Map<String, String>>();
    
    private static final FSWConfiguration INSTANCE = new FSWConfiguration();
    
    public static FSWConfiguration getInstance() {
        return INSTANCE;
    }
    
    public Map<String, String> getConfiguration(final String name) {
        return configurations.get(name);
    }
    
    public Map<String, String> setConfiguration(final String name, final Map<String, String> properties) {
        ConfigurationLogger.ROOT_LOGGER.settingConfiguration(name, properties);
        return configurations.put(name, Collections.unmodifiableMap(properties));
    }
    
    public Map<String, String> removeConfiguration(final String name) {
        ConfigurationLogger.ROOT_LOGGER.removingConfiguration(name);
        return configurations.remove(name);
    }
}
