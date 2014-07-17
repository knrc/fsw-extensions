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

package org.jboss.integrationplatform.fsw.extension.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.registry.Resource.ResourceEntry;
import org.jboss.dmr.ModelNode;
import org.jboss.integrationplatform.fsw.configuration.FSWConfiguration;
import org.jboss.msc.service.ServiceController;

/**
 * Add a configuration
 *
 * @author Kevin Conner
 */
public class ConfigurationAdd extends AbstractAddStepHandler {

    static final ConfigurationAdd INSTANCE = new ConfigurationAdd();

    @Override
    protected void populateModel(final OperationContext context, final ModelNode operation, final Resource resource) throws  OperationFailedException {
        final ModelNode model = resource.getModel();
        
        model.setEmptyObject();
        for (SimpleAttributeDefinition attr : ConfigurationDefinition.ALL_ATTRIBUTES) {
            attr.validateAndSet(operation, model);
        }
    }
    
    @Override
    protected void performRuntime(final OperationContext context, final ModelNode operation, final ModelNode model,
        final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers)
        throws OperationFailedException {

        final String name = model.get(Constants.ATTRIBUTE_NAME).asString();
        
        final Resource resource = context.readResource(PathAddress.EMPTY_ADDRESS, true);
        
        final Map<String, String> propertyMap = new HashMap<String, String>();
        
        final Set<ResourceEntry> properties = resource.getChildren(Constants.MODEL_PROPERTY);
        for (ResourceEntry property: properties) {
            final ModelNode propertyModel = property.getModel();
            final String propertyName = propertyModel.get(Constants.ATTRIBUTE_NAME).asString();
            final String propertyValue = propertyModel.get(Constants.ATTRIBUTE_VALUE).asString();
            
            propertyMap.put(propertyName, propertyValue);
        }
        
        SubsystemLogger.ROOT_LOGGER.configurationInformation(name, propertyMap);
        
        FSWConfiguration.getInstance().setConfiguration(name, propertyMap);
    }
    
    protected boolean requiresRuntimeVerification() {
        return false;
    }
}
