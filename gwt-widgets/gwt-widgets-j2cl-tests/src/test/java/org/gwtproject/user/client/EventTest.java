/*
 * Copyright 2009 Google Inc.
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
package org.gwtproject.user.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.j2cl.junit.apt.J2clTestInput;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.event.dom.client.*;
import org.gwtproject.event.shared.HandlerRegistration;
import org.gwtproject.user.client.Event.NativePreviewEvent;
import org.gwtproject.user.client.Event.NativePreviewHandler;
import org.gwtproject.user.client.ui.Label;
import org.gwtproject.user.client.ui.RootPanel;

/** Test Case for {@link Event}. */
@J2clTestInput(EventTest.class)
public class EventTest extends GWTTestCase {

  /** A NativePreviewHandler used for testing. */
  private static class TestNativePreviewHandler implements NativePreviewHandler {
    private boolean doCancel;
    private boolean doPreventCancel;
    private boolean isFired = false;

    /**
     * Construct a new {@link TestNativePreviewHandler}.
     *
     * @param doCancel if true, cancel the event
     * @param doPreventCancel if true, prevent the event from being canceled
     */
    public TestNativePreviewHandler(boolean doCancel, boolean doPreventCancel) {
      this.doCancel = doCancel;
      this.doPreventCancel = doPreventCancel;
    }

    @Override
    public void onPreviewNativeEvent(NativePreviewEvent event) {
      assertFalse(isFired);
      isFired = true;
      if (doCancel) {
        event.cancel();
      }
      if (doPreventCancel) {
        event.consume();
      }
    }

    public void assertIsFired(boolean expected) {
      assertEquals(expected, isFired);
    }
  }

  /** A custom {@link Label} used for testing. */
  private static class TestLabel extends Label implements HasDoubleClickHandlers {
    @Override
    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
      return addDomHandler(handler, DoubleClickEvent.getType());
    }
  }

  /** Debug information used to test events. */
  private static class EventInfo {
    private int fireCount = 0;
  }

  @Override
  public String getModuleName() {
    return "org.gwtproject.user.Widgets";
  }

  /**
   * Test that concurrent removal of a {@link NativePreviewHandler} does not trigger an exception.
   * The handler should not actually be removed until the end of the event loop.
   */
  public void testConcurrentRemovalOfNativePreviewHandler() {
    // Add handler0
    final TestNativePreviewHandler handler0 = new TestNativePreviewHandler(false, false);
    final HandlerRegistration reg0 = Event.addNativePreviewHandler(handler0);

    // Add handler 1
    final TestNativePreviewHandler handler1 =
        new TestNativePreviewHandler(false, false) {
          @Override
          public void onPreviewNativeEvent(NativePreviewEvent event) {
            super.onPreviewNativeEvent(event);
            handler0.assertIsFired(false);
            reg0.removeHandler();
          }
        };
    HandlerRegistration reg1 = Event.addNativePreviewHandler(handler1);
    assertTrue(Event.fireNativePreviewEvent(null));

    // Verify both handlers fired even though one was removed
    handler0.assertIsFired(true);
    handler1.assertIsFired(true);
    reg1.removeHandler();
  }

  /**
   * Test that a double click results in exactly one simulated click event in IE. See issue 3392 for
   * more info.
   */
  public void testDoubleClickEvent() {
    TestLabel label = new TestLabel();
    RootPanel.get().add(label);

    // Add some handlers
    final EventInfo clickInfo = new EventInfo();
    label.addClickHandler(event -> clickInfo.fireCount++);
    final EventInfo dblclickInfo = new EventInfo();
    label.addDoubleClickHandler(event -> dblclickInfo.fireCount++);

    // Fire a click event
    NativeEvent clickEvent =
        Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
    label.getElement().dispatchEvent(clickEvent);
    NativeEvent dblclickEvent =
        Document.get().createDblClickEvent(0, 0, 0, 0, 0, false, false, false, false);
    label.getElement().dispatchEvent(dblclickEvent);

    // Verify the results
    assertEquals(1, clickInfo.fireCount);
    assertEquals(1, dblclickInfo.fireCount);
    RootPanel.get().remove(label);
  }

  /**
   * Test that {@link Event#fireNativePreviewEvent(NativeEvent)} returns the correct value if the
   * native event is canceled.
   */
  public void testFireNativePreviewEventCancel() {
    TestNativePreviewHandler handler0 = new TestNativePreviewHandler(true, false);
    TestNativePreviewHandler handler1 = new TestNativePreviewHandler(true, false);
    HandlerRegistration reg0 = Event.addNativePreviewHandler(handler0);
    HandlerRegistration reg1 = Event.addNativePreviewHandler(handler1);
    assertFalse(Event.fireNativePreviewEvent(null));
    handler0.assertIsFired(true);
    handler1.assertIsFired(true);
    reg0.removeHandler();
    reg1.removeHandler();
  }

  /**
   * Test that {@link Event#fireNativePreviewEvent(NativeEvent)} returns the correct value if the
   * native event is prevented from being canceled, even if another handler cancels the event.
   */
  public void testFireNativePreviewEventPreventCancel() {
    TestNativePreviewHandler handler0 = new TestNativePreviewHandler(false, true);
    TestNativePreviewHandler handler1 = new TestNativePreviewHandler(true, false);
    HandlerRegistration reg0 = Event.addNativePreviewHandler(handler0);
    HandlerRegistration reg1 = Event.addNativePreviewHandler(handler1);
    assertTrue(Event.fireNativePreviewEvent(null));
    handler0.assertIsFired(true);
    handler1.assertIsFired(true);
    reg0.removeHandler();
    reg1.removeHandler();
  }

  /**
   * Test that {@link Event#fireNativePreviewEvent(NativeEvent)} fires handlers in reverse order,
   * and that the legacy EventPreview fires only if it is at the top of the stack.
   */
  public void testFireNativePreviewEventReverseOrder() {
    final TestNativePreviewHandler handler0 =
        new TestNativePreviewHandler(false, false) {
          @Override
          public void onPreviewNativeEvent(NativePreviewEvent event) {
            super.onPreviewNativeEvent(event);
            assertFalse(event.isFirstHandler());
          }
        };
    final TestNativePreviewHandler handler1 =
        new TestNativePreviewHandler(false, false) {
          @Override
          public void onPreviewNativeEvent(NativePreviewEvent event) {
            super.onPreviewNativeEvent(event);
            handler0.assertIsFired(false);
            assertFalse(event.isFirstHandler());
          }
        };
    final TestNativePreviewHandler handler2 =
        new TestNativePreviewHandler(false, false) {
          @Override
          public void onPreviewNativeEvent(NativePreviewEvent event) {
            super.onPreviewNativeEvent(event);
            handler0.assertIsFired(false);
            handler1.assertIsFired(false);
            assertFalse(event.isFirstHandler());
          }
        };
    final TestNativePreviewHandler handler3 =
        new TestNativePreviewHandler(false, false) {
          @Override
          public void onPreviewNativeEvent(NativePreviewEvent event) {
            super.onPreviewNativeEvent(event);
            handler0.assertIsFired(false);
            handler1.assertIsFired(false);
            handler2.assertIsFired(false);
            assertTrue(event.isFirstHandler());
          }
        };
    HandlerRegistration reg0 = Event.addNativePreviewHandler(handler0);
    HandlerRegistration reg1 = Event.addNativePreviewHandler(handler1);
    HandlerRegistration reg2 = Event.addNativePreviewHandler(handler2);
    HandlerRegistration reg3 = Event.addNativePreviewHandler(handler3);
    assertTrue(DOM.previewEvent(null));
    handler0.assertIsFired(true);
    handler1.assertIsFired(true);
    handler2.assertIsFired(true);
    handler3.assertIsFired(true);
    reg0.removeHandler();
    reg1.removeHandler();
    reg2.removeHandler();
    reg3.removeHandler();
  }

  /** Test removal of a {@link NativePreviewHandler} works. */
  public void testFireNativePreviewEventRemoval() {
    TestNativePreviewHandler handler0 = new TestNativePreviewHandler(false, false);
    TestNativePreviewHandler handler1 = new TestNativePreviewHandler(false, false);
    HandlerRegistration reg0 = Event.addNativePreviewHandler(handler0);
    HandlerRegistration reg1 = Event.addNativePreviewHandler(handler1);
    reg1.removeHandler();
    assertTrue(Event.fireNativePreviewEvent(null));
    handler0.assertIsFired(true);
    handler1.assertIsFired(false);
    reg0.removeHandler();
  }

  /**
   * Test that {@link Event#fireNativePreviewEvent(NativeEvent)} returns the correct value even if
   * another event is fired while handling the current event.
   */
  public void testFireNativePreviewEventWithInterupt() {
    NativePreviewHandler handler =
        new NativePreviewHandler() {
          private boolean first = true;

          @Override
          public void onPreviewNativeEvent(NativePreviewEvent event) {
            if (first) {
              event.cancel();
              first = false;
              assertTrue(Event.fireNativePreviewEvent(null));
              assertTrue(event.isCanceled());
            }
          }
        };
    HandlerRegistration reg = Event.addNativePreviewHandler(handler);
    reg.removeHandler();
  }

  /**
   * Test that {@link Event#fireNativePreviewEvent(NativeEvent)} returns the correct value if the
   * native event is not canceled.
   */
  public void testFireNativePreviewEventWithoutCancel() {
    TestNativePreviewHandler handler0 = new TestNativePreviewHandler(false, false);
    TestNativePreviewHandler handler1 = new TestNativePreviewHandler(false, false);
    HandlerRegistration reg0 = Event.addNativePreviewHandler(handler0);
    HandlerRegistration reg1 = Event.addNativePreviewHandler(handler1);
    assertTrue(Event.fireNativePreviewEvent(null));
    handler0.assertIsFired(true);
    handler1.assertIsFired(true);
    reg0.removeHandler();
    reg1.removeHandler();
  }

  /**
   * Test that {@link Event#fireNativePreviewEvent(NativeEvent)} returns the correct value if no
   * handlers are present.
   */
  public void testFireNativePreviewEventWithoutHandlers() {
    assertTrue(Event.fireNativePreviewEvent(null));
  }

  public void testGetTypeInt() {
    assertEquals(Event.ONBLUR, Event.getTypeInt("blur"));
    assertEquals(Event.ONCHANGE, Event.getTypeInt("change"));
    assertEquals(Event.ONCLICK, Event.getTypeInt("click"));
    assertEquals(Event.ONERROR, Event.getTypeInt("error"));
    assertEquals(Event.ONFOCUS, Event.getTypeInt("focus"));
    assertEquals(Event.ONKEYDOWN, Event.getTypeInt("keydown"));
    assertEquals(Event.ONKEYPRESS, Event.getTypeInt("keypress"));
    assertEquals(Event.ONKEYUP, Event.getTypeInt("keyup"));
    assertEquals(Event.ONLOAD, Event.getTypeInt("load"));
    assertEquals(Event.ONMOUSEDOWN, Event.getTypeInt("mousedown"));
    assertEquals(Event.ONMOUSEMOVE, Event.getTypeInt("mousemove"));
    assertEquals(Event.ONMOUSEOUT, Event.getTypeInt("mouseout"));
    assertEquals(Event.ONMOUSEOVER, Event.getTypeInt("mouseover"));
    assertEquals(Event.ONMOUSEUP, Event.getTypeInt("mouseup"));
    assertEquals(-1, Event.getTypeInt("undefined"));
  }

  /** Test the accessors in {@link NativePreviewEvent}. */
  public void testNativePreviewEventAccessors() {
    // cancelNativeEvent
    {
      NativePreviewEvent event = new NativePreviewEvent();
      assertFalse(event.isCanceled());
      event.cancel();
      assertTrue(event.isCanceled());
    }

    // preventCancelNativeEvent
    {
      NativePreviewEvent event = new NativePreviewEvent();
      assertFalse(event.isConsumed());
      event.consume();
      assertTrue(event.isConsumed());
    }

    // revive
    {
      NativePreviewEvent event = new NativePreviewEvent();
      event.cancel();
      event.consume();
      assertTrue(event.isCanceled());
      assertTrue(event.isConsumed());
      event.revive();
      assertFalse(event.isCanceled());
      assertFalse(event.isConsumed());
    }
  }

  /** Test that the singleton instance of {@link NativePreviewEvent} is revived correctly. */
  public void testReviveNativePreviewEvent() {
    // Fire the event and cancel it
    TestNativePreviewHandler handler0 = new TestNativePreviewHandler(true, true);
    HandlerRegistration reg0 = Event.addNativePreviewHandler(handler0);
    Event.fireNativePreviewEvent(null);
    handler0.assertIsFired(true);
    reg0.removeHandler();

    // Fire the event again, but don't cancel it
    TestNativePreviewHandler handler1 =
        new TestNativePreviewHandler(false, false) {
          @Override
          public void onPreviewNativeEvent(NativePreviewEvent event) {
            assertFalse(event.isCanceled());
            assertFalse(event.isConsumed());
            assertTrue(event.isFirstHandler());
            super.onPreviewNativeEvent(event);
          }
        };
    HandlerRegistration reg1 = Event.addNativePreviewHandler(handler1);
    assertTrue(Event.fireNativePreviewEvent(null));
    handler1.assertIsFired(true);
    reg1.removeHandler();
  }
}
