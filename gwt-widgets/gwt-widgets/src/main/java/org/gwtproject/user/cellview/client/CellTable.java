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

import java.util.HashSet;
import java.util.Set;
import org.gwtproject.cell.client.Cell;
import org.gwtproject.dom.client.BrowserEvents;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.TableCellElement;
import org.gwtproject.dom.client.TableColElement;
import org.gwtproject.dom.client.TableElement;
import org.gwtproject.dom.client.TableRowElement;
import org.gwtproject.dom.client.TableSectionElement;
import org.gwtproject.dom.style.shared.Display;
import org.gwtproject.dom.style.shared.TableLayout;
import org.gwtproject.dom.style.shared.Unit;
import org.gwtproject.resources.client.ClientBundle;
import org.gwtproject.resources.client.CssResource;
import org.gwtproject.resources.client.CssResource.ImportedWithPrefix;
import org.gwtproject.resources.client.ImageResource;
import org.gwtproject.resources.client.ImageResource.ImageOptions;
import org.gwtproject.resources.client.ImageResource.RepeatStyle;
import org.gwtproject.user.cellview.client.LoadingStateChangeEvent.LoadingState;
import org.gwtproject.user.client.ui.DeckPanel;
import org.gwtproject.user.client.ui.Image;
import org.gwtproject.user.client.ui.SimplePanel;
import org.gwtproject.user.client.ui.Widget;
import org.gwtproject.view.client.ProvidesKey;

/**
 * A tabular view that supports paging and columns.
 *
 * <p>
 *
 * <h3>Columns</h3>
 *
 * The {@link Column} class defines the {@link Cell} used to render a column. Implement {@link
 * Column#getValue(Object)} to retrieve the field value from the row object that will be rendered in
 * the {@link Cell}.
 *
 * <p>
 *
 * <h3>Headers and Footers</h3>
 *
 * A {@link org.gwtproject.user.cellview.client.Header} can be placed at the top (header) or bottom
 * (footer) of the {@link CellTable}. You can specify a header as text using {@link
 * #addColumn(Column, String)}, or you can create a custom {@link
 * org.gwtproject.user.cellview.client.Header} that can change with the value of the cells, such as
 * a column total. The {@link Header} will be rendered every time the row data changes or the table
 * is redrawn. If you pass the same header instance (==) into adjacent columns, the header will span
 * the columns.
 *
 * <p>
 *
 * <h3>Examples</h3>
 *
 * <dl>
 *   <dt>Trivial example
 *   <dd>{@example com.google.gwt.examples.cellview.CellTableExample}
 *   <dt>Handling user input with trivial FieldUpdater example
 *   <dd>{@example com.google.gwt.examples.cellview.CellTableFieldUpdaterExample}
 *   <dt>Handling user input with complex FieldUpdater example
 *   <dd>{@example com.google.gwt.examples.cellview.CellTableFieldUpdaterExampleComplex}
 *   <dt>Pushing data with List Data Provider (backed by {@link java.util.List})
 *   <dd>{@example com.google.gwt.examples.view.ListDataProviderExample}
 *   <dt>Pushing data asynchronously with Async Data Provider
 *   <dd>{@example com.google.gwt.examples.view.AsyncDataProviderExample}
 *   <dt>Writing a custom data provider
 *   <dd>{@example com.google.gwt.examples.view.RangeChangeHandlerExample}
 *   <dt>Using a key provider to track objects as they change
 *   <dd>{@example com.google.gwt.examples.view.KeyProviderExample}
 * </dl>
 *
 * @param <T> the data type of each row
 */
public class CellTable<T> extends AbstractCellTable<T>
    implements AbstractCellTable.TableSectionChangeHandler {

  /** Resources that match the GWT standard style theme. */
  public interface BasicResources extends Resources {

    BasicResources INSTANCE = new CellTable_BasicResourcesImpl();

    /** The styles used in this widget. */
    @Override
    @Source(BasicStyle.DEFAULT_CSS)
    BasicStyle cellTableStyle();
  }

  /** A ClientBundle that provides images for this widget. */
  public interface Resources extends ClientBundle {

    Resources INSTANCE = new CellTable_ResourcesImpl();
    /** The background used for footer cells. */
    @Source("cellTableHeaderBackground.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
    ImageResource cellTableFooterBackground();

    /** The background used for header cells. */
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
    ImageResource cellTableHeaderBackground();

    /** The loading indicator used while the table is waiting for data. */
    @ImageOptions(flipRtl = true)
    ImageResource cellTableLoading();

    /** The background used for selected cells. */
    @Source("cellListSelectedBackground.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
    ImageResource cellTableSelectedBackground();

    /** Icon used when a column is sorted in ascending order. */
    @Source("sortAscending.png")
    @ImageOptions(flipRtl = true)
    ImageResource cellTableSortAscending();

    /** Icon used when a column is sorted in descending order. */
    @Source("sortDescending.png")
    @ImageOptions(flipRtl = true)
    ImageResource cellTableSortDescending();

    /** The styles used in this widget. */
    @Source(Style.DEFAULT_CSS)
    Style cellTableStyle();
  }

  /** Styles used by this widget. */
  @ImportedWithPrefix("gwt-CellTable")
  public interface Style extends CssResource {
    /** The path to the default CSS styles used by this resource. */
    String DEFAULT_CSS = "org/gwtproject/user/cellview/client/CellTable.gss";

    /** Applied to every cell. */
    String cellTableCell();

    /** Applied to even rows. */
    String cellTableEvenRow();

    /** Applied to cells in even rows. */
    String cellTableEvenRowCell();

    /** Applied to the first column. */
    String cellTableFirstColumn();

    /** Applied to the first column footers. */
    String cellTableFirstColumnFooter();

    /** Applied to the first column headers. */
    String cellTableFirstColumnHeader();

    /** Applied to footers cells. */
    String cellTableFooter();

    /** Applied to headers cells. */
    String cellTableHeader();

    /** Applied to the hovered row. */
    String cellTableHoveredRow();

    /** Applied to the cells in the hovered row. */
    String cellTableHoveredRowCell();

    /** Applied to the keyboard selected cell. */
    String cellTableKeyboardSelectedCell();

    /** Applied to the keyboard selected row. */
    String cellTableKeyboardSelectedRow();

    /** Applied to the cells in the keyboard selected row. */
    String cellTableKeyboardSelectedRowCell();

    /** Applied to the last column. */
    String cellTableLastColumn();

    /** Applied to the last column footers. */
    String cellTableLastColumnFooter();

    /** Applied to the last column headers. */
    String cellTableLastColumnHeader();

    /** Applied to the loading indicator. */
    String cellTableLoading();

    /** Applied to odd rows. */
    String cellTableOddRow();

    /** Applied to cells in odd rows. */
    String cellTableOddRowCell();

    /** Applied to selected rows. */
    String cellTableSelectedRow();

    /** Applied to cells in selected rows. */
    String cellTableSelectedRowCell();

    /** Applied to header cells that are sortable. */
    String cellTableSortableHeader();

    /** Applied to header cells that are sorted in ascending order. */
    String cellTableSortedHeaderAscending();

    /** Applied to header cells that are sorted in descending order. */
    String cellTableSortedHeaderDescending();

    /** Applied to the table. */
    String cellTableWidget();
  }

  /** Styles used by {@link BasicResources}. */
  @ImportedWithPrefix("gwt-CellTable")
  interface BasicStyle extends Style {
    /** The path to the default CSS styles used by this resource. */
    String DEFAULT_CSS = "org/gwtproject/user/cellview/client/CellTableBasic.gss";
  }

  /** Adapter class to convert {@link Resources} to {@link AbstractCellTable.Resources}. */
  private static class ResourcesAdapter implements AbstractCellTable.Resources {

    private final CellTable.Resources resources;
    private final StyleAdapter style;

    public ResourcesAdapter(CellTable.Resources resources) {
      this.resources = resources;
      this.style = new StyleAdapter(resources.cellTableStyle());
    }

    @Override
    public ImageResource sortAscending() {
      return resources.cellTableSortAscending();
    }

    @Override
    public ImageResource sortDescending() {
      return resources.cellTableSortDescending();
    }

    @Override
    public AbstractCellTable.Style style() {
      return style;
    }
  }

  /** Adapter class to convert {@link Style} to {@link AbstractCellTable.Style}. */
  private static class StyleAdapter implements AbstractCellTable.Style {
    private final CellTable.Style style;

    public StyleAdapter(CellTable.Style style) {
      this.style = style;
    }

    @Override
    public String cell() {
      return style.cellTableCell();
    }

    @Override
    public String evenRow() {
      return style.cellTableEvenRow();
    }

    @Override
    public String evenRowCell() {
      return style.cellTableEvenRowCell();
    }

    @Override
    public String firstColumn() {
      return style.cellTableFirstColumn();
    }

    @Override
    public String firstColumnFooter() {
      return style.cellTableFirstColumnFooter();
    }

    @Override
    public String firstColumnHeader() {
      return style.cellTableFirstColumnHeader();
    }

    @Override
    public String footer() {
      return style.cellTableFooter();
    }

    @Override
    public String header() {
      return style.cellTableHeader();
    }

    @Override
    public String hoveredRow() {
      return style.cellTableHoveredRow();
    }

    @Override
    public String hoveredRowCell() {
      return style.cellTableHoveredRowCell();
    }

    @Override
    public String keyboardSelectedCell() {
      return style.cellTableKeyboardSelectedCell();
    }

    @Override
    public String keyboardSelectedRow() {
      return style.cellTableKeyboardSelectedRow();
    }

    @Override
    public String keyboardSelectedRowCell() {
      return style.cellTableKeyboardSelectedRowCell();
    }

    @Override
    public String lastColumn() {
      return style.cellTableLastColumn();
    }

    @Override
    public String lastColumnFooter() {
      return style.cellTableLastColumnFooter();
    }

    @Override
    public String lastColumnHeader() {
      return style.cellTableLastColumnHeader();
    }

    @Override
    public String oddRow() {
      return style.cellTableOddRow();
    }

    @Override
    public String oddRowCell() {
      return style.cellTableOddRowCell();
    }

    @Override
    public String selectedRow() {
      return style.cellTableSelectedRow();
    }

    @Override
    public String selectedRowCell() {
      return style.cellTableSelectedRowCell();
    }

    @Override
    public String sortableHeader() {
      return style.cellTableSortableHeader();
    }

    @Override
    public String sortedHeaderAscending() {
      return style.cellTableSortedHeaderAscending();
    }

    @Override
    public String sortedHeaderDescending() {
      return style.cellTableSortedHeaderDescending();
    }

    @Override
    public String widget() {
      return style.cellTableWidget();
    }
  }

  /** The default page size. */
  private static final int DEFAULT_PAGESIZE = 15;

  private static Resources DEFAULT_RESOURCES;

  private static Resources getDefaultResources() {
    if (DEFAULT_RESOURCES == null) {
      DEFAULT_RESOURCES = new CellTable_ResourcesImpl();
    }
    return DEFAULT_RESOURCES;
  }

  /**
   * Create the default loading indicator using the loading image in the specified {@link
   * Resources}.
   *
   * @param resources the resources
   * @return a widget loading indicator
   */
  private static Widget createDefaultLoadingIndicator(Resources resources) {
    ImageResource loadingImg = resources.cellTableLoading();
    return (loadingImg == null) ? null : new Image(loadingImg);
  }

  final TableColElement colgroup;
  private final SimplePanel emptyTableWidgetContainer = new SimplePanel();
  private final SimplePanel loadingIndicatorContainer = new SimplePanel();

  /** A {@link DeckPanel} to hold widgets associated with various loading states. */
  private final DeckPanel messagesPanel = new DeckPanel();

  private final Style style;
  private final TableElement table;
  private TableSectionElement tbody;
  private final TableSectionElement tbodyLoading;
  private final TableCellElement tbodyLoadingCell;
  private TableSectionElement tfoot;
  private TableSectionElement thead;
  private boolean colGroupEnabled = true;
  private boolean removeColumnsOnHide = false;

  /** Constructs a table with a default page size of 15. */
  public CellTable() {
    this(DEFAULT_PAGESIZE);
  }

  /**
   * Constructs a table with the given page size.
   *
   * @param pageSize the page size
   */
  public CellTable(final int pageSize) {
    this(pageSize, getDefaultResources());
  }

  /**
   * Constructs a table with a default page size of 15, and the given {@link ProvidesKey key
   * provider}.
   *
   * @param keyProvider an instance of ProvidesKey<T>, or null if the record object should act as
   *     its own key
   */
  public CellTable(ProvidesKey<T> keyProvider) {
    this(DEFAULT_PAGESIZE, keyProvider);
  }

  /**
   * Constructs a table with the given page size with the specified {@link Resources}.
   *
   * @param pageSize the page size
   * @param resources the resources to use for this widget
   */
  public CellTable(int pageSize, Resources resources) {
    this(pageSize, resources, null);
  }

  /**
   * Constructs a table with the given page size and the given {@link ProvidesKey key provider}.
   *
   * @param pageSize the page size
   * @param keyProvider an instance of ProvidesKey<T>, or null if the record object should act as
   *     its own key
   */
  public CellTable(int pageSize, ProvidesKey<T> keyProvider) {
    this(pageSize, getDefaultResources(), keyProvider);
  }

  /**
   * Constructs a table with the given page size, the specified {@link Resources}, and the given key
   * provider.
   *
   * @param pageSize the page size
   * @param resources the resources to use for this widget
   * @param keyProvider an instance of ProvidesKey<T>, or null if the record object should act as
   *     its own key
   */
  public CellTable(final int pageSize, Resources resources, ProvidesKey<T> keyProvider) {
    this(pageSize, resources, keyProvider, createDefaultLoadingIndicator(resources));
  }

  /**
   * Constructs a table with the specified page size, {@link Resources}, key provider, and loading
   * indicator.
   *
   * @param pageSize the page size
   * @param resources the resources to use for this widget
   * @param keyProvider an instance of ProvidesKey<T>, or null if the record object should act as
   *     its own key
   * @param loadingIndicator the widget to use as a loading indicator, or null to disable
   */
  public CellTable(
      final int pageSize,
      Resources resources,
      ProvidesKey<T> keyProvider,
      Widget loadingIndicator) {
    this(pageSize, resources, keyProvider, loadingIndicator, true, true);
  }

  /**
   * Constructs a table with the specified page size, {@link Resources}, key provider, and loading
   * indicator.
   *
   * @param pageSize the page size
   * @param resources the resources to use for this widget
   * @param keyProvider an instance of ProvidesKey<T>, or null if the record object should act as
   *     its own key
   * @param loadingIndicator the widget to use as a loading indicator, or null to disable
   * @param enableColGroup enable colgroup element. This is used when the table is using fixed
   *     layout and when column style is added. Ignoring this element will boost rendering
   *     performance. Note that when colgroup is disabled, {@link #setColumnWidth}, {@link
   *     setTableLayoutFixed} and {@link addColumnStyleName} are no longer supported
   * @param attachLoadingPanel attaching the table section that contains the empty table widget and
   *     the loading indicator. Attaching this to the table significantly improve the rendering
   *     performance in webkit based browsers but also introduces significantly larger latency in
   *     IE. If the panel is not attached to the table, it won't be displayed. But the user can call
   *     {@link #getTableLoadingSection} and attach it to other elements outside the table element
   */
  public CellTable(
      final int pageSize,
      Resources resources,
      ProvidesKey<T> keyProvider,
      Widget loadingIndicator,
      boolean enableColGroup,
      boolean attachLoadingPanel) {
    super(
        Document.get().createTableElement(),
        pageSize,
        new ResourcesAdapter(resources),
        keyProvider);
    this.style = resources.cellTableStyle();
    this.style.ensureInjected();
    this.colGroupEnabled = enableColGroup;

    table = getElement().cast();
    table.setCellSpacing(0);
    if (enableColGroup) {
      colgroup = Document.get().createColGroupElement();
      table.appendChild(colgroup);
    } else {
      colgroup = null;
    }
    thead = table.createTHead();
    // Some browsers create a tbody automatically, others do not.
    if (table.getTBodies().getLength() > 0) {
      tbody = table.getTBodies().getItem(0);
    } else {
      tbody = Document.get().createTBodyElement();
      table.appendChild(tbody);
    }
    tbodyLoading = Document.get().createTBodyElement();
    if (attachLoadingPanel) {
      table.appendChild(tbodyLoading);
    }
    tfoot = table.createTFoot();

    // Attach the messages panel.
    {
      tbodyLoadingCell = Document.get().createTDElement();
      TableRowElement tr = Document.get().createTRElement();
      tbodyLoading.appendChild(tr);
      tr.appendChild(tbodyLoadingCell);
      tbodyLoadingCell.setAlign("center");
      tbodyLoadingCell.appendChild(messagesPanel.getElement());
      adopt(messagesPanel);
      messagesPanel.add(emptyTableWidgetContainer);
      messagesPanel.add(loadingIndicatorContainer);
      loadingIndicatorContainer.setStyleName(style.cellTableLoading());
    }

    // Set the loading indicator.
    setLoadingIndicator(loadingIndicator); // Can be null.

    // Sink events.
    Set<String> eventTypes = new HashSet<String>();
    eventTypes.add(BrowserEvents.MOUSEOVER);
    eventTypes.add(BrowserEvents.MOUSEOUT);
    org.gwtproject.user.cellview.client.CellBasedWidgetImpl.get().sinkEvents(this, eventTypes);
  }

  @Override
  public void addColumnStyleName(int index, String styleName) {
    assertColumnGroupEnabled("Cannot add column style when colgroup is disabled");
    ensureTableColElement(index).addClassName(styleName);
  }

  /**
   * Return the height of the table body.
   *
   * @return an int representing the body height
   */
  public int getBodyHeight() {
    return tbody.getClientHeight();
  }

  /**
   * Return the height of the table header.
   *
   * @return an int representing the header height
   */
  public int getHeaderHeight() {
    return thead.getClientHeight();
  }

  /**
   * Return the section that display loading indicator and the empty table widget. If
   * attachLoadingPanel is set to false in the constructor, this section may not be attached to any
   * element.
   */
  public TableSectionElement getTableLoadingSection() {
    return tbodyLoading;
  }

  @Override
  public void onTableBodyChange(TableSectionElement newTBody) {
    tbody = newTBody;
  }

  @Override
  public void onTableFootChange(TableSectionElement newTFoot) {
    tfoot = newTFoot;
  }

  @Override
  public void onTableHeadChange(TableSectionElement newTHead) {
    thead = newTHead;
  }

  @Override
  public void removeColumnStyleName(int index, String styleName) {
    assertColumnGroupEnabled("Cannot remove column style when colgroup is disabled");
    if (index >= colgroup.getChildCount()) {
      return;
    }
    ensureTableColElement(index).removeClassName(styleName);
  }

  /**
   * {@inheritDoc}
   *
   * <p>The layout behavior depends on whether or not the table is using fixed layout.
   *
   * @see #setTableLayoutFixed(boolean)
   */
  @Override
  public void setColumnWidth(Column<T, ?> column, String width) {
    assertColumnGroupEnabled("Cannot set column width when colgroup is disabled");
    // Overridden to add JavaDoc comments about fixed layout.
    super.setColumnWidth(column, width);
  }

  /**
   * {@inheritDoc}
   *
   * <p>The layout behavior depends on whether or not the table is using fixed layout.
   *
   * @see #setTableLayoutFixed(boolean)
   */
  @Override
  public void setColumnWidth(Column<T, ?> column, double width, Unit unit) {
    // Overridden to add JavaDoc comments about fixed layout.
    super.setColumnWidth(column, width, unit);
  }

  @Override
  public void setEmptyTableWidget(Widget widget) {
    emptyTableWidgetContainer.setWidget(widget);
    super.setEmptyTableWidget(widget);
  }

  @Override
  public void setLoadingIndicator(Widget widget) {
    loadingIndicatorContainer.setWidget(widget);
    super.setLoadingIndicator(widget);
  }

  /**
   * Enable or disable fixed table layout.
   *
   * <p>
   *
   * <h1>Fixed Table Layout</h1>
   *
   * When using the fixed table layout, cell contents are truncated as needed, which allows you to
   * set the exact width of columns and the table. The default column width is 0 (invisible). In
   * order to see all columns, you must set the width of the table (recommended 100%), or set the
   * width of every column in the table. The following conditions are true for fixed layout tables:
   *
   * <ul>
   *   <li>If the widths of <b>all</b> columns are set, the width becomes a weight and the columns
   *       are resized proportionally.
   *   <li>If the widths of <b>some</b> columns are set using absolute values (PX), those columns
   *       are fixed and the remaining width is divided evenly over the other columns. If there is
   *       no remaining width, the other columns will not be visible.
   *   <li>If the width of some columns are set in absolute values (PX) and others are set in
   *       relative values (PCT), the absolute columns will be fixed and the remaining width is
   *       divided proportionally over the PCT columns. This allows users to define how the
   *       remaining width is allocated.
   * </ul>
   *
   * @param isFixed true to use fixed table layout, false not to
   * @see <a href="http://www.w3.org/TR/CSS2/tables.html#width-layout">W3C HTML Specification</a>
   */
  public void setTableLayoutFixed(boolean isFixed) {
    if (isFixed && !colGroupEnabled) {
      throw new IllegalStateException("Cannot set table to fixed layout when colgroup is disabled");
    }
    if (isFixed) {
      table.getStyle().setTableLayout(TableLayout.FIXED);
    } else {
      table.getStyle().clearTableLayout();
    }
  }

  /**
   * Set the width of the width and specify whether or not it should use fixed table layout. See
   * {@link #setTableLayoutFixed(boolean)} for more information about fixed layout tables.
   *
   * @param width the width of the table
   * @param isFixedLayout true to use fixed width layout, false not to
   * @see #setTableLayoutFixed(boolean)
   * @see <a href="http://www.w3.org/TR/CSS2/tables.html#width-layout">W3C HTML Specification</a>
   */
  public final void setWidth(String width, boolean isFixedLayout) {
    super.setWidth(width);
    setTableLayoutFixed(isFixedLayout);
  }

  /**
   * Configures how the colgroup is updated when a column is removed. If true, removing a column
   * will also remove the corresponding col element from the colgroup. If false, removing a column
   * will leave the col element on the colgroup and set it to zero width and display hidden.
   *
   * <p>For legacy reasons, the default is false even though it is known to cause some column sizing
   * issues in Firefox.
   */
  public void setRemoveColumnsOnHide(boolean removeColumnsOnHide) {
    this.removeColumnsOnHide = removeColumnsOnHide;
  }

  @Override
  protected void doSetColumnWidth(int column, String width) {
    // This is invoked when column width is set (which will throw an exception if colgroup is not
    // enabled), and refreshColumnWidth/clearColumnWidth. The latter two are no op if setColumnWidth
    // is not invoked first.
    if (colGroupEnabled) {
      if (width == null) {
        ensureTableColElement(column).getStyle().clearWidth();
      } else {
        ensureTableColElement(column).getStyle().setProperty("width", width);
      }
    }
  }

  @Override
  protected void doSetHeaderVisible(boolean isFooter, boolean isVisible) {
    setVisible(isFooter ? tfoot : thead, isVisible);
  }

  @Override
  protected TableSectionElement getTableBodyElement() {
    return tbody;
  }

  @Override
  protected TableSectionElement getTableFootElement() {
    return tfoot;
  }

  @Override
  protected TableSectionElement getTableHeadElement() {
    return thead;
  }

  /**
   * Called when the loading state changes.
   *
   * @param state the new loading state
   */
  @Override
  protected void onLoadingStateChanged(LoadingState state) {
    Widget message = null;
    if (state == LoadingState.LOADING) {
      // Loading indicator.
      message = loadingIndicatorContainer;
    } else if (state == LoadingState.LOADED && getPresenter().isEmpty()) {
      // Empty table.
      message = emptyTableWidgetContainer;
    }

    // Switch out the message to display.
    if (message != null) {
      messagesPanel.showWidget(messagesPanel.getWidgetIndex(message));
    }

    // Adjust the colspan of the messages panel container.
    tbodyLoadingCell.setColSpan(Math.max(1, getRealColumnCount()));

    // Show the correct container.
    showOrHide(getChildContainer(), message == null);
    showOrHide(tbodyLoading, message != null);

    // Fire an event.
    super.onLoadingStateChanged(state);
  }

  @Override
  protected void refreshColumnWidths() {
    super.refreshColumnWidths();

    /*
     * Clear the display for all columns that appear in the table. And update or remove the cols of
     * the colgroup that are no longer in the table according to the removeCOlumnsOnHide flag.
     */
    if (colGroupEnabled) {
      int colCount = colgroup.getChildCount();
      int lastColumn = getRealColumnCount();
      for (int i = 0; i < lastColumn; i++) {
        ensureTableColElement(i).getStyle().clearDisplay();
      }
      for (int i = colCount - 1; i >= lastColumn; i--) {
        if (removeColumnsOnHide) {
          colgroup.removeChild(colgroup.getChild(i));
        } else {
          doSetColumnWidth(i, "0px");
          ensureTableColElement(i).getStyle().setDisplay(Display.NONE);
        }
      }
    }
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    doAttach(messagesPanel);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    doDetach(messagesPanel);
  }

  /**
   * Assert if colgroup is enabled, and throw an exception with the supplied message if it's not
   * enabled.
   */
  private void assertColumnGroupEnabled(String message) {
    if (!colGroupEnabled) {
      throw new IllegalStateException(message);
    }
  }

  /**
   * Get the {@link TableColElement} at the specified index, creating it if necessary.
   *
   * @param index the column index
   * @return the {@link TableColElement}
   */
  private TableColElement ensureTableColElement(int index) {
    // Ensure that we have enough columns.
    for (int i = colgroup.getChildCount(); i <= index; i++) {
      colgroup.appendChild(Document.get().createColElement());
    }
    return colgroup.getChild(index).cast();
  }
}
