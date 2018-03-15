/*
 * Copyright 2018 Google Inc.
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
package org.gwtproject.uibinder.test.view.newuibinder;

import org.gwtproject.uibinder.client.UiBinder;
import org.gwtproject.uibinder.client.UiFactory;
import org.gwtproject.uibinder.client.UiHandler;
import org.gwtproject.uibinder.client.UiTemplate;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 */
public class NewSimpleFormView implements IsWidget {

  @UiTemplate("org.gwtproject.uibinder.test.view.SimpleFormView.ui.xml")
  interface MyUiBinder extends UiBinder<VerticalPanel, NewSimpleFormView> {
  }

  private MyUiBinder binder = new NewSimpleFormView_MyUiBinderImpl();

  private Widget widget;

  @Override
  public Widget asWidget() {
    if (widget == null) {
      widget = binder.createAndBindUi(this);
    }
    return widget;
  }

  @UiFactory
  protected Label createButtonLabel(String theText) {
    return new Label(theText);
  }

  @UiHandler("simpleButton")
  protected void onSimpleButtonClick(ClickEvent event) {
    Window.alert("Button Clicked");
  }
}
