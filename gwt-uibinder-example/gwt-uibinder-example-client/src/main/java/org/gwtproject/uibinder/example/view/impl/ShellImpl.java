/*
 * Copyright 2018 Vertispan LLC
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
package org.gwtproject.uibinder.example.view.impl;

import org.gwtproject.uibinder.client.UiBinder;
import org.gwtproject.uibinder.client.UiField;
import org.gwtproject.uibinder.client.UiTemplate;
import org.gwtproject.uibinder.example.view.Shell;
import org.gwtproject.uibinder.example.view.SupplementalView;

import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class ShellImpl implements Shell {

  @UiTemplate
  interface MyUiBinder extends UiBinder<DockLayoutPanel, ShellImpl> {
  }

  private DockLayoutPanel container;

  @UiField(provided = true)
  SupplementalView supplementalView;

  @UiField
  ScrollPanel mainArea;

  private MyUiBinder uiBinder = new ShellImpl_MyUiBinderImpl();

  @Inject
  public ShellImpl(SupplementalView supplementalView) {
    this.supplementalView = supplementalView;
  }

  @Override
  public Widget asWidget() {
    if (container == null) {
      container = uiBinder.createAndBindUi(this);
    }
    return container;
  }
}
