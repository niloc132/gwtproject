/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwtproject.validation.client.impl;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.TraversableResolver;
import javax.validation.ValidatorFactory;
import javax.validation.spi.BootstrapState;

import org.gwtproject.validation.client.spi.GwtConfigurationState;
import org.gwtproject.validation.client.spi.GwtValidationProvider;

/**
 * Base GWT {@link Configuration}.
 */
public abstract class BaseGwtConfiguration implements
                                           Configuration<BaseGwtConfiguration> {

  protected final GwtValidationProvider provider;
  protected final BootstrapState state;
  protected final Map<String, String> properties = new HashMap<>();
  protected ConstraintValidatorFactory constraintValidatorFactory;
  protected MessageInterpolator messageInterpolator;
  protected TraversableResolver traversableResolver;

  public BaseGwtConfiguration(GwtValidationProvider gwtValidationProvider,
                              BootstrapState state) {
    provider = gwtValidationProvider;
    this.state = state;
  }

  @Override
  public final BaseGwtConfiguration addProperty(String name, String value) {
    properties.put(name, value);
    return this;
  }

  @Override
  public final ValidatorFactory buildValidatorFactory() {
    GwtConfigurationState configurationState = new GwtConfigurationState( //
                                                                          constraintValidatorFactory, //
                                                                          messageInterpolator, //
                                                                          properties, //
                                                                          traversableResolver);
    return provider.buildValidatorFactory(configurationState);
  }

  /**
   * <b>{@link ConstraintValidatorFactory} is unsupported in GWT.</b>
   * Constraint validators are instead created using GWT.create- with no factory.
   */
  @Override
  public final BaseGwtConfiguration constraintValidatorFactory(
          ConstraintValidatorFactory constraintValidatorFactory) {
    this.constraintValidatorFactory = constraintValidatorFactory;
    return this;
  }

  @Override
  public final ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
    throw new UnsupportedOperationException("BaseGwtConfiguration getDefaultConstraintValidatorFactory");
    //return GWT.create(ConstraintValidatorFactory.class);
  }

  @Override
  public final MessageInterpolator getDefaultMessageInterpolator() {
    throw new UnsupportedOperationException("BaseGwtConfiguration getDefaultMessageInterpolator");
    //return GWT.create(MessageInterpolator.class);
  }

  @Override
  public final TraversableResolver getDefaultTraversableResolver() {
    throw new UnsupportedOperationException("BaseGwtConfiguration getDefaultTraversableResolver");
    //return new DefaultTraversableResolver();
  }

  @Override
  public final BaseGwtConfiguration ignoreXmlConfiguration() {
    // Always ignore XML anyway
    return this;
  }

  @Override
  public final BaseGwtConfiguration messageInterpolator(
          MessageInterpolator interpolator) {
    this.messageInterpolator = interpolator;
    return this;
  }

  @Override
  public final BaseGwtConfiguration traversableResolver(
          TraversableResolver resolver) {
    this.traversableResolver = resolver;
    return this;
  }

}