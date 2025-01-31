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
package org.gwtproject.user.cellview.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.gwtproject.animation.client.Animation;
import org.gwtproject.aria.client.Roles;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.dom.client.BrowserEvents;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.style.shared.Display;
import org.gwtproject.dom.style.shared.Overflow;
import org.gwtproject.dom.style.shared.Position;
import org.gwtproject.dom.style.shared.Unit;
import org.gwtproject.event.dom.client.KeyCodes;
import org.gwtproject.i18n.client.LocalizableResource.DefaultLocale;
import org.gwtproject.i18n.client.Messages;
import org.gwtproject.i18n.shared.cldr.LocaleInfo;
import org.gwtproject.resources.client.ClientBundle;
import org.gwtproject.resources.client.CssResource;
import org.gwtproject.resources.client.CssResource.ImportedWithPrefix;
import org.gwtproject.resources.client.ImageResource;
import org.gwtproject.resources.client.ImageResource.ImageOptions;
import org.gwtproject.resources.client.ImageResource.RepeatStyle;
import org.gwtproject.safecss.shared.SafeStyles;
import org.gwtproject.safecss.shared.SafeStylesBuilder;
import org.gwtproject.safehtml.client.SafeHtmlTemplates;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.user.client.Event;
import org.gwtproject.user.client.ui.AbstractImagePrototype;
import org.gwtproject.user.client.ui.Focusable;
import org.gwtproject.user.client.ui.HasAnimation;
import org.gwtproject.user.client.ui.SimplePanel;
import org.gwtproject.view.client.TreeViewModel;

/**
 * A view of a tree.
 *
 * <p>This widget will <em>only</em> work in standards mode, which requires that the HTML page in
 * which it is run have an explicit &lt;!DOCTYPE&gt; declaration.
 *
 * <p>
 *
 * <h3>Examples</h3>
 *
 * <dl>
 *   <dt>Trivial example
 *   <dd>{@example com.google.gwt.examples.cellview.CellTreeExample}
 *   <dt>Complex example
 *   <dd>{@example com.google.gwt.examples.cellview.CellTreeExample2}
 * </dl>
 */
public class CellTree extends AbstractCellTree implements HasAnimation, Focusable {

  /** Resources that match the GWT standard style theme. */
  public interface BasicResources extends Resources {
    BasicResources INSTANCE = new CellTree_BasicResourcesImpl();

    @ImageOptions(flipRtl = true)
    ImageResource cellTreeClosedItem();

    @ImageOptions(flipRtl = true)
    @Source("cellTreeLoadingBasic.gif")
    ImageResource cellTreeLoading();

    @ImageOptions(flipRtl = true)
    ImageResource cellTreeOpenItem();

    /** The styles used in this widget. */
    @Source(BasicStyle.DEFAULT_CSS)
    BasicStyle cellTreeStyle();
  }

  /** Constants for labeling the cell tree. Provides just English messages by default. */
  @DefaultLocale("en_US")
  public interface CellTreeMessages extends Messages {
    CellTreeMessages INSTANCE = new CellTree_CellTreeMessages_en_US();

    @DefaultMessage("Show more")
    String showMore();

    @DefaultMessage("Empty")
    String emptyTree();
  }

  /** A node animation. */
  public abstract static class NodeAnimation extends Animation {

    /** The default animation delay in milliseconds. */
    private static final int DEFAULT_ANIMATION_DURATION = 450;

    /** The duration of the animation in milliseconds. */
    private int duration = DEFAULT_ANIMATION_DURATION;

    NodeAnimation() {}

    /**
     * Return the duration of the animation in milliseconds.
     *
     * @see #setDuration(int)
     */
    public int getDuration() {
      return duration;
    }

    /**
     * Set the duration of the animation in milliseconds.
     *
     * @param duration the duration in milliseconds
     * @see #getDuration()
     */
    public void setDuration(int duration) {
      this.duration = duration;
    }

    /**
     * Animate a tree node into its new state.
     *
     * @param node the node to animate
     * @param isAnimationEnabled true to animate
     */
    abstract void animate(CellTreeNodeView<?> node, boolean isAnimationEnabled);
  }

  /** A ClientBundle that provides images for this widget. */
  public interface Resources extends ClientBundle {

    Resources INSTANCE = new CellTree_ResourcesImpl();

    /** An image indicating a closed branch. */
    @ImageOptions(flipRtl = true)
    @Source("cellTreeClosedArrow.png")
    ImageResource cellTreeClosedItem();

    /** An image indicating that a node is loading. */
    @ImageOptions(flipRtl = true)
    ImageResource cellTreeLoading();

    /** An image indicating an open branch. */
    @ImageOptions(flipRtl = true)
    @Source("cellTreeOpenArrow.png")
    ImageResource cellTreeOpenItem();

    /** The background used for selected items. */
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
    ImageResource cellTreeSelectedBackground();

    /** The styles used in this widget. */
    @Source(Style.DEFAULT_CSS)
    Style cellTreeStyle();
  }

  /** A {@link NodeAnimation} that reveals the contents of child nodes. */
  public static class RevealAnimation extends NodeAnimation {

    /**
     * Create a new {@link RevealAnimation}.
     *
     * @return the new animation
     */
    public static RevealAnimation create() {
      return new RevealAnimation();
    }

    /** The container that holds the content, includind the children. */
    Element contentContainer;

    /** The target height when opening, the start height when closing. */
    int height;

    /** True if the node is opening, false if closing. */
    boolean opening;

    /** The container that holds the child container. */
    private Element animFrame;

    /** The container that holds the children. */
    private Element childContainer;

    /** Not instantiable. */
    private RevealAnimation() {}

    @Override
    protected void onComplete() {
      cleanup();
    }

    @Override
    protected void onStart() {
      if (opening) {
        animFrame.getStyle().setHeight(1.0, Unit.PX);
        animFrame.getStyle().setPosition(Position.RELATIVE);
        animFrame.getStyle().clearDisplay();
        height = contentContainer.getScrollHeight();
      } else {
        height = contentContainer.getOffsetHeight();
      }
    }

    @Override
    protected void onUpdate(double progress) {
      if (opening) {
        double curHeight = progress * height;
        animFrame.getStyle().setHeight(curHeight, Unit.PX);
      } else {
        double curHeight = (1.0 - progress) * height;
        animFrame.getStyle().setHeight(curHeight, Unit.PX);
      }

      // Remind IE6 that we want the overflow to be hidden.
      animFrame.getStyle().setOverflow(Overflow.HIDDEN);
      animFrame.getStyle().setPosition(Position.RELATIVE);
    }

    /**
     * Animate a {@link CellTreeNodeView} into its new state.
     *
     * @param node the {@link CellTreeNodeView} to animate
     * @param isAnimationEnabled true to animate
     */
    @Override
    void animate(CellTreeNodeView<?> node, boolean isAnimationEnabled) {
      // Cancel any pending animations.
      cancel();

      // Initialize the fields.
      this.opening = node.isOpen();
      animFrame = node.ensureAnimationFrame();
      contentContainer = node.ensureContentContainer();
      childContainer = node.ensureChildContainer();

      if (isAnimationEnabled) {
        // Animated.
        int duration = getDuration();
        int childCount = childContainer.getChildCount();
        if (childCount < 4) {
          // Reduce the duration if there are less than four items or it will
          // look really slow.
          duration = (int) ((childCount / 4.0) * duration);
        }
        run(duration);
      } else {
        // Non animated.
        cleanup();
      }
    }

    /** Put the node back into a clean state and clear fields. */
    private void cleanup() {
      if (opening) {
        animFrame.getStyle().clearDisplay();
      } else {
        animFrame.getStyle().setDisplay(Display.NONE);
        childContainer.setInnerHTML("");
      }
      animFrame.getStyle().clearHeight();
      animFrame.getStyle().clearPosition();
      this.contentContainer = null;
      this.childContainer = null;
      this.animFrame = null;
    }
  }

  /** A {@link NodeAnimation} that slides children into view. */
  public static class SlideAnimation extends RevealAnimation {
    /**
     * Create a new {@link RevealAnimation}.
     *
     * @return the new animation
     */
    public static SlideAnimation create() {
      return new SlideAnimation();
    }

    /** Not instantiable. */
    private SlideAnimation() {}

    @Override
    protected void onComplete() {
      contentContainer.getStyle().clearPosition();
      contentContainer.getStyle().clearTop();
      contentContainer.getStyle().clearWidth();
      super.onComplete();
    }

    @Override
    protected void onStart() {
      super.onStart();
      if (opening) {
        contentContainer.getStyle().setTop(-height, Unit.PX);
      } else {
        contentContainer.getStyle().setTop(0, Unit.PX);
      }
      contentContainer.getStyle().setPosition(Position.RELATIVE);
    }

    @Override
    protected void onUpdate(double progress) {
      super.onUpdate(progress);
      if (opening) {
        double curTop = (1.0 - progress) * -height;
        contentContainer.getStyle().setTop(curTop, Unit.PX);
      } else {
        double curTop = progress * -height;
        contentContainer.getStyle().setTop(curTop, Unit.PX);
      }
    }
  }

  /** Styles used by this widget. */
  @ImportedWithPrefix("gwt-CellTree")
  public interface Style extends CssResource {
    /** The path to the default CSS styles used by this resource. */
    String DEFAULT_CSS = "org/gwtproject/user/cellview/client/CellTree.gss";

    /** Applied to the empty message. */
    String cellTreeEmptyMessage();

    /** Applied to tree items. */
    String cellTreeItem();

    /** Applied to open/close icon. */
    String cellTreeItemImage();

    /** Applied to the wrapper around the image and value. */
    String cellTreeItemImageValue();

    /** Applied to the value in an item. */
    String cellTreeItemValue();

    /** Applied to the keyboard selected item. */
    String cellTreeKeyboardSelectedItem();

    /** Applied to open tree items. */
    String cellTreeOpenItem();

    /** Applied to selected tree items. */
    String cellTreeSelectedItem();

    /** Applied to the show more button. */
    String cellTreeShowMoreButton();

    /** Applied to top level items. */
    String cellTreeTopItem();

    /** Applied to open/close icon at the top level. */
    String cellTreeTopItemImage();

    /** Applied to the wrapper around the image and value of top level items. */
    String cellTreeTopItemImageValue();

    /** Applied to the widget. */
    String cellTreeWidget();
  }

  /** Styles used by {@link BasicResources}. */
  @ImportedWithPrefix("gwt-CellTree")
  interface BasicStyle extends Style {
    /** The path to the default CSS styles used by this resource. */
    String DEFAULT_CSS = "org/gwtproject/user/cellview/client/CellTreeBasic.gss";
  }

  interface Template extends SafeHtmlTemplates {

    CellTree.Template INSTANCE = new CellTree_TemplateImpl();

    SafeHtml imageWrapper(String classes, SafeStyles cssLayout, SafeHtml image);
  }

  /** The default number of children to show under a tree node. */
  private static final int DEFAULT_LIST_SIZE = 25;

  private static Resources DEFAULT_RESOURCES = new CellTree_ResourcesImpl();

  private static Template template;

  private static Resources getDefaultResources() {
    return DEFAULT_RESOURCES;
  }

  /** A boolean indicating whether or not a cell is being edited. */
  boolean cellIsEditing;

  /** A boolean indicating that the widget has focus. */
  boolean isFocused;

  /** Set to true while the elements are being refreshed. Events are ignored during this time. */
  boolean isRefreshing;

  /** The hidden root node in the tree. Visible for testing. */
  final CellTreeNodeView<?> rootNode;

  private char accessKey = 0;

  /** The animation. */
  private NodeAnimation animation;

  /** The HTML used to generate the closed image. */
  private final SafeHtml closedImageHtml;

  /** The HTML used to generate the closed image for the top items. */
  private final SafeHtml closedImageTopHtml;

  /** The default number of children to display under each node. */
  private int defaultNodeSize = DEFAULT_LIST_SIZE;

  /** The maximum width of the open and closed images. */
  private final int imageWidth;

  /** Indicates whether or not animations are enabled. */
  private boolean isAnimationEnabled;

  /**
   * The {@link CellTreeNodeView} whose children are currently being selected using the keyboard.
   */
  private CellTreeNodeView<?> keyboardSelectedNode;

  /** The HTML used to generate the loading image. */
  private final SafeHtml loadingImageHtml;

  /** The HTML used to generate the open image. */
  private final SafeHtml openImageHtml;

  /** The HTML used to generate the open image for the top items. */
  private final SafeHtml openImageTopHtml;

  /** The styles used by this widget. */
  private final Style style;

  private int tabIndex;

  /**
   * Construct a new {@link CellTree}.
   *
   * @param <T> the type of data in the root node
   * @param viewModel the {@link TreeViewModel} that backs the tree
   * @param rootValue the hidden root value of the tree
   */
  public <T> CellTree(TreeViewModel viewModel, T rootValue) {
    this(viewModel, rootValue, getDefaultResources());
  }

  /**
   * Construct a new {@link CellTree}. Uses default translations that means that messages will be
   * always in English.
   *
   * @param <T> the type of data in the root node
   * @param viewModel the {@link TreeViewModel} that backs the tree
   * @param rootValue the hidden root value of the tree
   * @param resources the resources used to render the tree
   */
  public <T> CellTree(TreeViewModel viewModel, T rootValue, Resources resources) {
    this(viewModel, rootValue, resources, CellTreeMessages.INSTANCE);
  }

  /**
   * Construct a new {@link CellTree}.
   *
   * @param <T> the type of data in the root node
   * @param viewModel the {@link TreeViewModel} that backs the tree
   * @param rootValue the hidden root value of the tree
   * @param resources the resources used to render the tree
   * @param messages translation messages. Users should inherit an empty interface from {@link
   *     CellTreeMessages} and add annotations needed for their specific translation systems. Then
   *     create the new interface with GWT.create and pass as this argument.
   */
  public <T> CellTree(
      TreeViewModel viewModel, T rootValue, Resources resources, CellTreeMessages messages) {
    this(viewModel, rootValue, resources, messages, DEFAULT_LIST_SIZE);
  }

  /**
   * Construct a new {@link CellTree}.
   *
   * @param <T> the type of data in the root node
   * @param viewModel the {@link TreeViewModel} that backs the tree
   * @param rootValue the hidden root value of the tree
   * @param resources the resources used to render the tree
   * @param messages translation messages. Users should inherit an empty interface from {@link
   *     CellTreeMessages} and add annotations needed for their specific translation systems. Then
   *     create the new interface with GWT.create and pass as this argument.
   * @param defaultNodeSize default number of children to display beneath each child node
   */
  public <T> CellTree(
      TreeViewModel viewModel,
      T rootValue,
      Resources resources,
      CellTreeMessages messages,
      int defaultNodeSize) {
    super(viewModel);
    this.defaultNodeSize = defaultNodeSize;
    if (template == null) {
      template = Template.INSTANCE;
    }
    this.style = resources.cellTreeStyle();
    this.style.ensureInjected();
    initWidget(new SimplePanel());
    setStyleName(this.style.cellTreeWidget());

    // Initialize the open and close images strings.
    ImageResource treeOpen = resources.cellTreeOpenItem();
    ImageResource treeClosed = resources.cellTreeClosedItem();
    ImageResource treeLoading = resources.cellTreeLoading();
    openImageHtml = getImageHtml(treeOpen, false);
    closedImageHtml = getImageHtml(treeClosed, false);
    openImageTopHtml = getImageHtml(treeOpen, true);
    closedImageTopHtml = getImageHtml(treeClosed, true);
    loadingImageHtml = getImageHtml(treeLoading, false);
    imageWidth =
        Math.max(Math.max(treeOpen.getWidth(), treeClosed.getWidth()), treeLoading.getWidth());

    // We use one animation for the entire tree.
    setAnimation(SlideAnimation.create());

    // Add event handlers.
    Set<String> eventTypes = new HashSet<String>();
    eventTypes.add(BrowserEvents.FOCUS);
    eventTypes.add(BrowserEvents.BLUR);
    eventTypes.add(BrowserEvents.KEYDOWN);
    eventTypes.add(BrowserEvents.KEYUP);
    eventTypes.add(BrowserEvents.MOUSEDOWN);
    eventTypes.add(BrowserEvents.CLICK);
    CellBasedWidgetImpl.get().sinkEvents(this, eventTypes);

    // Associate a view with the item.
    org.gwtproject.user.cellview.client.CellTreeNodeView<T> root =
        new CellTreeNodeView<T>(this, null, null, getElement(), rootValue, messages);
    keyboardSelectedNode = rootNode = root;
    root.setOpen(true, false);

    Roles.getTreeRole().set(getElement());
  }

  /**
   * Get the animation used to open and close nodes in this tree if animations are enabled.
   *
   * @return the animation
   * @see #isAnimationEnabled()
   * @see #setAnimation(NodeAnimation)
   */
  public NodeAnimation getAnimation() {
    return animation;
  }

  /**
   * Get the default maximum number of children to display under each tree node.
   *
   * @return the default node size
   * @see #setDefaultNodeSize(int)
   */
  public int getDefaultNodeSize() {
    return defaultNodeSize;
  }

  @Override
  public TreeNode getRootTreeNode() {
    return rootNode.getTreeNode();
  }

  public int getTabIndex() {
    return tabIndex;
  }

  public boolean isAnimationEnabled() {
    return isAnimationEnabled;
  }

  @Override
  public void onBrowserEvent(Event event) {
    CellBasedWidgetImpl.get().onBrowserEvent(this, event);
    if (isRefreshing) {
      // Ignore spurious events (onblur) while replacing elements.
      return;
    }
    super.onBrowserEvent(event);

    String eventType = event.getType();
    if (BrowserEvents.FOCUS.equals(eventType)) {
      // Remember the focus state.
      isFocused = true;
      onFocus();
    } else if (BrowserEvents.BLUR.equals(eventType)) {
      // Remember the blur state.
      isFocused = false;
      onBlur();
    } else if (BrowserEvents.KEYDOWN.equals(eventType) && !cellIsEditing) {
      int keyCode = event.getKeyCode();
      switch (keyCode) {
          // Handle keyboard navigation.
        case KeyCodes.KEY_DOWN:
        case KeyCodes.KEY_UP:
        case KeyCodes.KEY_RIGHT:
        case KeyCodes.KEY_LEFT:
          handleKeyNavigation(keyCode);

          // Prevent scrollbars from scrolling.
          event.preventDefault();
          return;
        case 32:
          // Prevent scrollbars from scrolling.
          event.preventDefault();
      }
    }

    final Element target = event.getEventTarget().cast();
    ArrayList<Element> chain = new ArrayList<Element>();
    collectElementChain(chain, getElement(), target);

    final boolean isMouseDown = BrowserEvents.MOUSEDOWN.equals(eventType);
    final boolean isClick = BrowserEvents.CLICK.equals(eventType);
    final org.gwtproject.user.cellview.client.CellTreeNodeView<?> nodeView =
        findItemByChain(chain, 0, rootNode);
    if (nodeView != null) {
      Element showMoreElem = nodeView.getShowMoreElement();
      if (isMouseDown) {
        if (!nodeView.isRootNode() && nodeView.getImageElement().isOrHasChild(target)) {
          // Open the node when the open image is clicked.
          nodeView.setOpen(!nodeView.isOpen(), true);
          return;
        } else if (showMoreElem != null && showMoreElem.isOrHasChild(target)) {
          // Show more rows when clicked.
          nodeView.showMore();
          return;
        }
      } else if (isClick && showMoreElem != null && showMoreElem.isOrHasChild(target)) {
        // Prevents strict-CSP violation due to javascript:'' link target
        event.preventDefault();
      }

      // Forward the event to the cell
      if (nodeView != rootNode && nodeView.getSelectionElement().isOrHasChild(target)) {
        // Move the keyboard focus to the clicked item.
        if (isClick) {
          /*
           * If the selected element is natively focusable, then we do not want to
           * steal focus away from it.
           */
          boolean isFocusable = CellBasedWidgetImpl.get().isFocusable(target);
          isFocused = isFocused || isFocusable;
          keyboardSelect(nodeView, !isFocusable);
        }

        nodeView.fireEventToCell(event);
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>Setting the key to (int) 0 will disable the access key.
   *
   * @see #getAccessKey()
   */
  public void setAccessKey(char key) {
    this.accessKey = key;
    keyboardSelectedNode.setKeyboardSelected(true, false);
  }

  /**
   * Set the animation used to open and close nodes in this tree. You must call {@link
   * #setAnimationEnabled(boolean)} to enable or disable animation.
   *
   * @param animation a {@link NodeAnimation}
   * @see #setAnimationEnabled(boolean)
   * @see #getAnimation()
   */
  public void setAnimation(NodeAnimation animation) {
    assert animation != null : "animation cannot be null";
    this.animation = animation;
  }

  public void setAnimationEnabled(boolean enable) {
    this.isAnimationEnabled = enable;
    if (!enable && animation != null) {
      animation.cancel();
    }
  }

  /**
   * Set the default number of children to display beneath each child node. If more nodes are
   * available, a button will appear at the end of the list allowing the user to show more items.
   * Changing this value will not affect other tree nodes that are already open (including the
   * hidden root node).
   *
   * @param defaultNodeSize the max
   * @see #getDefaultNodeSize()
   */
  public void setDefaultNodeSize(int defaultNodeSize) {
    this.defaultNodeSize = defaultNodeSize;
  }

  public void setFocus(boolean focused) {
    keyboardSelectedNode.setKeyboardSelected(true, true);
  }

  /**
   * Sets the node that will be selected when the CellTree gains keyboard focus.
   *
   * @param parentNode a node in the tree that is currently open
   * @param childIndex the index of the child to select
   * @param stealFocus if true, also change keyboard focus to this CellTree.
   */
  public void setKeyboardSelectedTreeNode(TreeNode parentNode, int childIndex, boolean stealFocus) {
    org.gwtproject.user.cellview.client.CellTreeNodeView nodeView = getCellTreeNodeView(parentNode);
    // Just to ensure necessary checks are done, e.g.
    //   assertNotDestroyed();checkChildBounds(childIndex);flush();
    nodeView.getTreeNode().getChildValue(childIndex);

    keyboardSelect(nodeView.getChildNode(childIndex), stealFocus);
  }

  public void setTabIndex(int index) {
    this.tabIndex = index;
    keyboardSelectedNode.setKeyboardSelected(true, false);
  }

  /**
   * Get the access key.
   *
   * @return the access key, or -1 if not set
   * @see #setAccessKey(char)
   */
  protected char getAccessKey() {
    return accessKey;
  }

  /** Called when the keyboard selected node loses focus. */
  protected void onBlur() {
    keyboardSelectedNode.setKeyboardSelectedStyle(false);
  }

  /** Called when the keyboard selected node gains focus. */
  protected void onFocus() {
    keyboardSelectedNode.setKeyboardSelectedStyle(true);
  }

  /** Cancel a pending animation. */
  void cancelTreeNodeAnimation() {
    animation.cancel();
  }

  /**
   * Get the HTML to render the closed image.
   *
   * @param isTop true if the top element, false if not
   * @return the HTML string
   */
  SafeHtml getClosedImageHtml(boolean isTop) {
    return isTop ? closedImageTopHtml : closedImageHtml;
  }

  /**
   * Get the width required for the images.
   *
   * @return the maximum width required for images.
   */
  int getImageWidth() {
    return imageWidth;
  }

  /** Return the node that has keyboard selection. */
  org.gwtproject.user.cellview.client.CellTreeNodeView<?> getKeyboardSelectedNode() {
    return keyboardSelectedNode;
  }

  /** Returns the TreeNode that is selected when the CellTree has keyboard focus. */
  public TreeNode getKeyboardSelectedTreeNode() {
    return keyboardSelectedNode == null ? null : keyboardSelectedNode.getTreeNode();
  }

  /** Return the HTML to render the loading image. */
  SafeHtml getLoadingImageHtml() {
    return loadingImageHtml;
  }

  /**
   * Get the HTML to render the open image.
   *
   * @param isTop true if the top element, false if not
   * @return the HTML string
   */
  SafeHtml getOpenImageHtml(boolean isTop) {
    return isTop ? openImageTopHtml : openImageHtml;
  }

  /** Return the Style used by the tree. */
  Style getStyle() {
    return style;
  }

  /**
   * Select a node using the keyboard.
   *
   * @param node the new node to select
   * @param stealFocus true to steal focus, false not to
   */
  void keyboardSelect(CellTreeNodeView<?> node, boolean stealFocus) {
    if (isKeyboardSelectionDisabled()) {
      return;
    }

    // Deselect the old node if it not destroyed.
    if (keyboardSelectedNode != null && !keyboardSelectedNode.isDestroyed()) {
      keyboardSelectedNode.setKeyboardSelected(false, false);
    }
    keyboardSelectedNode = node;
    keyboardSelectedNode.setKeyboardSelected(true, stealFocus);
  }

  /**
   * Animate the current state of a {@link CellTreeNodeView} in this tree.
   *
   * @param node the node to animate
   */
  void maybeAnimateTreeNode(CellTreeNodeView<?> node) {
    if (animation != null) {
      animation.animate(node, node.consumeAnimate() && isAnimationEnabled() && !node.isRootNode());
    }
  }

  /** If this widget has focus, reset it. */
  void resetFocus() {
    CellBasedWidgetImpl.get()
        .resetFocus(
            new Scheduler.ScheduledCommand() {
              public void execute() {
                if (isFocused
                    && !keyboardSelectedNode.isDestroyed()
                    && !keyboardSelectedNode.resetFocusOnCell()) {
                  keyboardSelectedNode.setKeyboardSelected(true, true);
                }
              }
            });
  }

  /** Collects parents going up the element tree, terminated at the tree root. */
  private void collectElementChain(ArrayList<Element> chain, Element hRoot, Element hElem) {
    if ((hElem == null) || (hElem == hRoot)) {
      return;
    }

    collectElementChain(chain, hRoot, hElem.getParentElement());
    chain.add(hElem);
  }

  private CellTreeNodeView getCellTreeNodeView(TreeNode treeNode) {
    if (!(treeNode instanceof org.gwtproject.user.cellview.client.CellTreeNodeView.TreeNodeImpl)) {
      throw new UnsupportedOperationException("Operation not supported for " + treeNode.getClass());
    }

    CellTreeNodeView nodeView = ((CellTreeNodeView.TreeNodeImpl) treeNode).getNodeView();
    if (!nodeView.belongsToTree(this)) {
      throw new IllegalArgumentException("The tree node does not belong to the tree.");
    }

    return nodeView;
  }

  private CellTreeNodeView<?> findItemByChain(
      ArrayList<Element> chain, int idx, CellTreeNodeView<?> parent) {
    if (idx == chain.size()) {
      return parent;
    }

    Element hCurElem = chain.get(idx);
    for (int i = 0, n = parent.getChildCount(); i < n; ++i) {
      org.gwtproject.user.cellview.client.CellTreeNodeView<?> child = parent.getChildNode(i);
      if (child.getElement() == hCurElem) {
        CellTreeNodeView<?> retItem = findItemByChain(chain, idx + 1, child);
        if (retItem == null) {
          return child;
        }
        return retItem;
      }
    }

    return findItemByChain(chain, idx + 1, parent);
  }

  /**
   * Get the HTML representation of an image.
   *
   * @param res the {@link ImageResource} to render as HTML
   * @param isTop true if the image is for a top level element.
   * @return the rendered HTML
   */
  private SafeHtml getImageHtml(ImageResource res, boolean isTop) {
    // Build the classes.
    StringBuilder classesBuilder = new StringBuilder(style.cellTreeItemImage());
    if (isTop) {
      classesBuilder.append(" ").append(style.cellTreeTopItemImage());
    }

    // Build the css.
    SafeStylesBuilder cssBuilder = new SafeStylesBuilder();
    if (LocaleInfo.getCurrentLocale().isRTL()) {
      cssBuilder.appendTrustedString("right: 0px;");
    } else {
      cssBuilder.appendTrustedString("left: 0px;");
    }
    cssBuilder.appendTrustedString("width: " + res.getWidth() + "px;");
    cssBuilder.appendTrustedString("height: " + res.getHeight() + "px;");

    AbstractImagePrototype proto = AbstractImagePrototype.create(res);
    SafeHtml image = proto.getSafeHtml();
    return template.imageWrapper(classesBuilder.toString(), cssBuilder.toSafeStyles(), image);
  }

  /**
   * Handle keyboard navigation.
   *
   * @param keyCode the key code that was pressed
   */
  // @VisibleForTesting
  void handleKeyNavigation(int keyCode) {
    org.gwtproject.user.cellview.client.CellTreeNodeView<?> parent =
        keyboardSelectedNode.getParentNode();
    int parentChildCount = (parent == null) ? 0 : parent.getChildCount();
    int index = keyboardSelectedNode.getIndex();
    int childCount = keyboardSelectedNode.getChildCount();
    boolean isRtl = LocaleInfo.getCurrentLocale().isRTL();
    keyCode = KeyCodes.maybeSwapArrowKeysForRtl(keyCode, isRtl);

    switch (keyCode) {
      case KeyCodes.KEY_DOWN:
        if (keyboardSelectedNode.isOpen() && childCount > 0) {
          // Select first child.
          keyboardSelect(keyboardSelectedNode.getChildNode(0), true);
        } else if (index < parentChildCount - 1) {
          // Next sibling.
          keyboardSelect(parent.getChildNode(index + 1), true);
        } else {
          // Next available sibling of parent hierarchy.
          org.gwtproject.user.cellview.client.CellTreeNodeView<?> curParent = parent;
          org.gwtproject.user.cellview.client.CellTreeNodeView<?> nextSibling = null;
          while (curParent != null && curParent != rootNode) {
            org.gwtproject.user.cellview.client.CellTreeNodeView<?> grandparent =
                curParent.getParentNode();
            if (grandparent == null) {
              break;
            }
            int curParentIndex = grandparent.indexOf(curParent);
            if (curParentIndex < grandparent.getChildCount() - 1) {
              nextSibling = grandparent.getChildNode(curParentIndex + 1);
              break;
            }
            curParent = grandparent;
          }
          if (nextSibling != null) {
            keyboardSelect(nextSibling, true);
          }
        }
        break;
      case KeyCodes.KEY_UP:
        if (index > 0) {
          // Deepest node of previous sibling hierarchy.
          org.gwtproject.user.cellview.client.CellTreeNodeView<?> prevSibling =
              parent.getChildNode(index - 1);
          while (prevSibling.isOpen() && prevSibling.getChildCount() > 0) {
            prevSibling = prevSibling.getChildNode(prevSibling.getChildCount() - 1);
          }
          keyboardSelect(prevSibling, true);
        } else if (parent != null && parent != rootNode) {
          // Parent.
          keyboardSelect(parent, true);
        }
        break;
      case KeyCodes.KEY_RIGHT:
        keyboardNavigateDeep();
        break;
      case KeyCodes.KEY_LEFT:
        keyboardNavigateShallow();
        break;
    }
  }

  /**
   * Navigate to a deeper node. If the node is closed, open it. If it is open, move to the first
   * child.
   */
  private void keyboardNavigateDeep() {
    if (!keyboardSelectedNode.isLeaf()) {
      boolean isOpen = keyboardSelectedNode.isOpen();
      if (isOpen && keyboardSelectedNode.getChildCount() > 0) {
        // First child.
        keyboardSelect(keyboardSelectedNode.getChildNode(0), true);
      } else if (!isOpen) {
        // Open the node.
        keyboardSelectedNode.setOpen(true, true);
      }
    }
  }

  /**
   * Navigate to a shallower node. If the node is open, close it. If it is closed, move to the
   * parent.
   */
  private void keyboardNavigateShallow() {
    org.gwtproject.user.cellview.client.CellTreeNodeView<?> parent =
        keyboardSelectedNode.getParentNode();
    if (keyboardSelectedNode.isOpen()) {
      // Close the node.
      keyboardSelectedNode.setOpen(false, true);
    } else if (parent != null && parent != rootNode) {
      // Select the parent.
      keyboardSelect(parent, true);
    }
  }
}
