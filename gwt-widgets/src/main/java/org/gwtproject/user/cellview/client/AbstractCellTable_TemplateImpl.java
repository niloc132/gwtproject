package org.gwtproject.user.cellview.client;

/**
 * This class is generated from org.gwtproject.user.cellview.client.AbstractCellTable.Template, do not edit manually
 */
public class AbstractCellTable_TemplateImpl implements org.gwtproject.user.cellview.client.AbstractCellTable.Template {
  
  /**
   * @Template("<div style=\"outline:none;\">{0}</div>")
   */
  public org.gwtproject.safehtml.shared.SafeHtml div(
        org.gwtproject.safehtml.shared.SafeHtml arg0) {
    StringBuilder sb = new java.lang.StringBuilder();
    sb.append("<div style=\"outline:none;\">");
    sb.append(arg0.asString());
    sb.append("</div>");
return new org.gwtproject.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}


/**
 * @Template("<table><tbody>{0}</tbody></table>")
 */
public org.gwtproject.safehtml.shared.SafeHtml tbody(
      org.gwtproject.safehtml.shared.SafeHtml arg0) {
  StringBuilder sb = new java.lang.StringBuilder();
  sb.append("<table><tbody>");
  sb.append(arg0.asString());
  sb.append("</tbody></table>");
return new org.gwtproject.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}


/**
 * @Template("<td class=\"{0}\">{1}</td>")
 */
public org.gwtproject.safehtml.shared.SafeHtml td(
      java.lang.String arg0, 
      org.gwtproject.safehtml.shared.SafeHtml arg1) {
  StringBuilder sb = new java.lang.StringBuilder();
  sb.append("<td class=\"");
  sb.append(org.gwtproject.safehtml.shared.SafeHtmlUtils.htmlEscape(arg0));
  sb.append("\">");
  sb.append(arg1.asString());
  sb.append("</td>");
return new org.gwtproject.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}


/**
 * @Template("<td class=\"{0}\" align=\"{1}\" valign=\"{2}\">{3}</td>")
 */
public org.gwtproject.safehtml.shared.SafeHtml tdBothAlign(
      java.lang.String arg0, 
      java.lang.String arg1, 
      java.lang.String arg2, 
      org.gwtproject.safehtml.shared.SafeHtml arg3) {
  StringBuilder sb = new java.lang.StringBuilder();
  sb.append("<td class=\"");
  sb.append(org.gwtproject.safehtml.shared.SafeHtmlUtils.htmlEscape(arg0));
  sb.append("\" align=\"");
  sb.append(org.gwtproject.safehtml.shared.SafeHtmlUtils.htmlEscape(arg1));
  sb.append("\" valign=\"");
  sb.append(org.gwtproject.safehtml.shared.SafeHtmlUtils.htmlEscape(arg2));
  sb.append("\">");
  sb.append(arg3.asString());
  sb.append("</td>");
return new org.gwtproject.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}


/**
 * @Template("<td class=\"{0}\" align=\"{1}\">{2}</td>")
 */
public org.gwtproject.safehtml.shared.SafeHtml tdHorizontalAlign(
      java.lang.String arg0, 
      java.lang.String arg1, 
      org.gwtproject.safehtml.shared.SafeHtml arg2) {
  StringBuilder sb = new java.lang.StringBuilder();
  sb.append("<td class=\"");
  sb.append(org.gwtproject.safehtml.shared.SafeHtmlUtils.htmlEscape(arg0));
  sb.append("\" align=\"");
  sb.append(org.gwtproject.safehtml.shared.SafeHtmlUtils.htmlEscape(arg1));
  sb.append("\">");
  sb.append(arg2.asString());
  sb.append("</td>");
return new org.gwtproject.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}


/**
 * @Template("<td class=\"{0}\" valign=\"{1}\">{2}</td>")
 */
public org.gwtproject.safehtml.shared.SafeHtml tdVerticalAlign(
      java.lang.String arg0, 
      java.lang.String arg1, 
      org.gwtproject.safehtml.shared.SafeHtml arg2) {
  StringBuilder sb = new java.lang.StringBuilder();
  sb.append("<td class=\"");
  sb.append(org.gwtproject.safehtml.shared.SafeHtmlUtils.htmlEscape(arg0));
  sb.append("\" valign=\"");
  sb.append(org.gwtproject.safehtml.shared.SafeHtmlUtils.htmlEscape(arg1));
  sb.append("\">");
  sb.append(arg2.asString());
  sb.append("</td>");
return new org.gwtproject.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}


/**
 * @Template("<table><tfoot>{0}</tfoot></table>")
 */
public org.gwtproject.safehtml.shared.SafeHtml tfoot(
      org.gwtproject.safehtml.shared.SafeHtml arg0) {
  StringBuilder sb = new java.lang.StringBuilder();
  sb.append("<table><tfoot>");
  sb.append(arg0.asString());
  sb.append("</tfoot></table>");
return new org.gwtproject.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}


/**
 * @Template("<table><thead>{0}</thead></table>")
 */
public org.gwtproject.safehtml.shared.SafeHtml thead(
      org.gwtproject.safehtml.shared.SafeHtml arg0) {
  StringBuilder sb = new java.lang.StringBuilder();
  sb.append("<table><thead>");
  sb.append(arg0.asString());
  sb.append("</thead></table>");
return new org.gwtproject.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}


/**
 * @Template("<tr class=\"{0}\">{1}</tr>")
 */
public org.gwtproject.safehtml.shared.SafeHtml tr(
      java.lang.String arg0, 
      org.gwtproject.safehtml.shared.SafeHtml arg1) {
  StringBuilder sb = new java.lang.StringBuilder();
  sb.append("<tr class=\"");
  sb.append(org.gwtproject.safehtml.shared.SafeHtmlUtils.htmlEscape(arg0));
  sb.append("\">");
  sb.append(arg1.asString());
  sb.append("</tr>");
return new org.gwtproject.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}

}
