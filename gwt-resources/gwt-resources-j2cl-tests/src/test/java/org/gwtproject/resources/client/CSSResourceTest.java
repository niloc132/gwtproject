/*
 * Copyright 2008 Google Inc.
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
package org.gwtproject.resources.client;

import static org.gwtproject.resources.client.ClientBundle.Source;
import static org.gwtproject.resources.client.CssResource.Import;
import static org.gwtproject.resources.client.CssResource.ImportedWithPrefix;
import static org.gwtproject.resources.client.CssResource.Shared;

import org.junit.Ignore;

import static junit.framework.TestCase.*;

/** Contains various full-stack tests of the CssResource system. */
@Ignore
public class CSSResourceTest {

  public void testChildResources() {
    Resources parentResource = new CSSResourceTest_ResourcesImpl();
    ChildResources childResource = new CSSResourceTest_ChildResourcesImpl();

    assertEquals(parentResource.dataMethod().getName(), childResource.dataMethod().getName());
  }

  public void testConcatenatedResource() {
    ConcatenatedResources r = new CSSResourceTest_ConcatenatedResourcesImpl();
    String text = r.css().getText();
    assertTrue(text.contains(".partA"));
    assertTrue(text.contains(".partB"));
  }

  public void testCss() {
    FullTestCss css = Resources.INSTANCE.css();
    String text = css.getText();

    report(text);

    // Check the sprite
    assertTrue(text.contains("height:16px"));
    assertTrue(text.contains("width:16px"));

    // Check the value() expansion
    assertTrue(text.contains("offset-left:\"guard\" 16px !important;"));
    assertTrue(text.contains("offset:16px 16px;"));

    // Make sure renaming works
    assertFalse("replacement".equals(css.replacement()));
    assertTrue(text.contains("." + css.replacement()));
    assertTrue(text.contains("." + css.replacement() + ":after"));
    assertTrue(text.contains("." + css.replacementNotJavaIdent()));

    // Make sure renaming for multi-class selectors (.foo.bar) works
    assertFalse("multiClassA".equals(css.multiClassA()));
    assertFalse("multiClassB".equals(css.multiClassB()));
    assertTrue(text.contains("." + css.multiClassA() + "." + css.multiClassB()));

    // Check static if evaluation
    assertTrue(text.contains("static:PASSED;"));
    assertFalse(text.contains("FAIL"));

    // Check runtime if evaluation
    assertTrue(text.contains("runtime:PASSED;"));

    // Check interestingly-named idents
    assertTrue(text.contains("-some-wacky-extension"));
    assertTrue(text.contains("another-extension:-bar"));
    assertTrue(text.contains("-unescaped-hyphen:-is-better"));
    assertTrue(text.contains("with_underscore:_is_better"));
    assertTrue(text.contains("ns\\:tag"));
    assertTrue(text.contains("ns\\:tag:pseudo"));
    assertTrue(text.contains("ns\\:tag::double-pseudo"));
    assertTrue(text.contains("ns\\:tag::-webkit-scrollbar"));

    // Check escaped string values
    assertTrue(text.contains("\"Hello\\\\\\\" world\""));

    // Check values
    assertFalse(text.contains("0.0;"));
    assertFalse(text.contains("0.0px;"));
    assertFalse(text.contains("0px;"));
    assertTrue(text.contains("background-color:#fff;"));
    assertTrue(text.contains("content:\"bar\";"));

    // Check invalid CSS values
    assertTrue(
        text.contains(
            "top:expression(document.compatMode==\"CSS1Compat\" ? documentElement.scrollTop:document.body.scrollTop \\ 2);"));

    // Check data URL expansion
    assertTrue(
        text.contains(
            "backgroundTopLevel:url('"
                + Resources.INSTANCE.dataMethod().getSafeUri().asString()
                + "')"));

    assertTrue(
        text.contains(
            "backgroundNested:url('"
                + Resources.INSTANCE.nested().dataMethod().getSafeUri().asString()
                + "')"));
    assertTrue(
        text.contains(
            "backgroundCustom:url('"
                + Resources.INSTANCE.customDataMethod().getSafeUri().asString()
                + "')"));
    assertTrue(
        text.contains(
            "backgroundImage:url('"
                + Resources.INSTANCE.spriteMethod().getSafeUri().asString()
                + "')"));
    assertTrue(
        text.contains(
            "backgroundImageNested:url('"
                + Resources.INSTANCE.nested().spriteMethod().getSafeUri().asString()
                + "')"));
    assertTrue(
        text.contains(
            "backgroundImageCustom:url('"
                + Resources.INSTANCE.customSpriteMethod().getSafeUri().asString()
                + "')"));

    // Check @eval expansion
    assertTrue(text.contains(red() + ";"));

    // Check @def substitution
    assertTrue(text.contains("50px"));
    // Check @def substitution into function arguments
    // Note that GWT transforms rgb(R, G, B) into #rrggbb form.
    assertTrue(text.contains("-moz-linear-gradient(left, #007f00, #00007f 50%);"));
    assertTrue(text.contains("-webkit-linear-gradient(left, #007f00, #00007f 50%);"));
    assertTrue(text.contains("linear-gradient(to right, #007f00, #00007f 50%);"));

    // Check merging semantics
    assertTrue(text.indexOf("static:PASSED") < text.indexOf("runtime:PASSED"));
    assertTrue(text.indexOf("before:merge") != -1);
    assertTrue(text.indexOf("before:merge") < text.indexOf("after:merge"));
    assertTrue(text.indexOf("." + css.mayCombine() + ",." + css.mayCombine2()) != -1);
    assertTrue(text.indexOf("merge:merge") != -1);
    assertTrue(text.indexOf("merge:merge") < text.indexOf("." + css.mayNotCombine()));
    assertTrue(text.indexOf("may-not-combine") < text.indexOf("prevent:true"));
    assertTrue(text.indexOf("prevent:true") < text.indexOf("prevent-merge:true"));
    assertTrue(text.indexOf("prevent:true") < text.indexOf("." + css.mayNotCombine2()));

    // Check commonly-used CSS3 constructs
    assertTrue(text.contains("background-color:rgba(0, 0, 0, 0.5);"));

    // Check external references
    assertEquals("externalA", css.externalA());
    assertTrue(text.contains(".externalA ." + css.replacement()));
    assertTrue(text.contains(".externalB"));
    assertTrue(text.contains(".externalC"));

    // Test font-face contents
    assertTrue(text.contains("url(Foo.otf) format(\"opentype\");"));
  }

  public static String red() {
    return "orange";
  }

  public void report(String s) {
    // Can be filled in if debugging.
    System.out.println(s);
  }

  public void testDefines() {
    Resources r = new CSSResourceTest_ResourcesImpl();
    CssWithDefines defines = r.deftest();

    assertEquals(1, defines.rawInt());
    assertEquals(1.5F, defines.rawFloat());
    assertEquals(1.5, defines.rawDouble());

    assertEquals(50, defines.lengthInt());
    assertEquals(1.5, defines.lengthFloat());

    assertEquals(50, defines.percentInt());
    assertEquals(50.5, defines.percentFloat());

    assertEquals("100px", defines.lengthString());
    assertEquals("#f00", defines.colorString());

    assertEquals(10, defines.overrideInt());
    assertNotNull(defines.overrideIntClass());
    assertFalse("10px".equals(defines.overrideIntClass()));
    assertFalse("10".equals(defines.overrideIntClass()));

    assertTrue(
        // css
        "1px solid rgba(0, 0, 0, 0.2)".equals(defines.multiValueBorderDef())
            ||
            // gss
            "1px solid rgba(0,0,0,0.2)".equals(defines.multiValueBorderDef()));
  }

  public void testEnsureInjected() {
    Resources r = new CSSResourceTest_ResourcesImpl();
    assertTrue(r.empty().ensureInjected());

    r = new CSSResourceTest_ResourcesImpl();
    assertFalse(r.empty().ensureInjected());

    r = new CSSResourceTest_ChildResourcesImpl();
    assertTrue(r.empty().ensureInjected());
  }

  public void testFileChoice() {
    // resource without @Source annotation
    ResourceFileChooser css = Resources.INSTANCE.resourceFileChooser();
    // should use the css file.
    String expectedCss = "." + css.myClass() + "{width:5px;}";
    assertEquals(expectedCss, css.getText());

    // resource with @Source annotation targeting one .css file
    css = Resources.INSTANCE.resourceFileChooserWithSourceTargetingOneCssFile();
    // should use the css file.
    assertEquals(expectedCss, css.getText());

    // resource with @Source annotation targeting one .gss file
    css = Resources.INSTANCE.resourceFileChooserWithSourceTargetingOneGssFile();
    // should use the css file instead of the gss file
    assertEquals(expectedCss, css.getText());

    // resource with @Source annotation targeting several .css files
    css = Resources.INSTANCE.resourceFileChooserWithSourceTargetingCssFiles();
    // should use the css files.
    expectedCss = "." + css.myClass() + "{width:5px;padding:5px;}";
    assertEquals(expectedCss, css.getText());

    // resource with @Source annotation targeting several .gss files
    css = Resources.INSTANCE.resourceFileChooserWithSourceTargetingGssFiles();
    // should use the css files instead of the gss files
    assertEquals(expectedCss, css.getText());
  }

  public void testMultipleBundles() {
    Resources r1 = new CSSResourceTest_ResourcesImpl();
    SiblingResources r2 = new CSSResourceTest_SiblingResourcesImpl();

    assertEquals(r1.a().replacement(), r2.a().replacement());
    assertEquals(r1.b().replacement(), r2.b().replacement());

    assertEquals(r1.a().sharedClass(), r2.b().sharedClass());
    assertFalse(r1.a().sharedOverrideClass().equals(r2.b().sharedOverrideClass()));
    assertFalse(r1.a().unsharedClass().equals(r2.b().unsharedClass()));

    String text = r1.descendants().getText();
    report(text);
    assertEquals("foo", r1.descendants().foo());
    assertTrue(text.contains("." + r1.a().local() + " ." + r1.b().local()));
    assertTrue(text.contains("." + r1.descendants().foo()));
    assertTrue(text.contains(".bar"));
  }

  public void testSiblingCSS() {
    SiblingResources r = new CSSResourceTest_SiblingResourcesImpl();

    assertFalse(r.a().replacement().equals(r.b().replacement()));
    assertFalse(r.a().local().equals(r.b().local()));

    String a = r.a().getText();
    String b = r.b().getText();

    report(a);
    report(b);

    assertTrue(a.contains(".other"));
    assertTrue(b.contains(".other"));
    assertTrue(a.contains(r.a().local()));
    assertTrue(b.contains(r.b().local()));
    assertFalse(a.contains(r.b().local()));
    assertFalse(b.contains(r.a().local()));
  }

  @Resource
  interface ChildResources extends Resources {
    ChildResources INSTANCE = new CSSResourceTest_ChildResourcesImpl();

    @Override
    @Source("16x16.png")
    ImageResource spriteMethod();
  }

  @Resource
  interface ConcatenatedResources {
    @Source(value = {"concatenatedA.css", "concatenatedB.css"})
    CssResource css();
  }

  interface CssWithDefines extends CssResource {
    String colorString();

    double lengthFloat();

    int lengthInt();

    String lengthString();

    int overrideInt();

    @ClassName("overrideInt")
    String overrideIntClass();

    double percentFloat();

    int percentInt();

    double rawDouble();

    float rawFloat();

    int rawInt();

    String multiValueBorderDef();
  }

  /** Regenerate this interface by running InterfaceGenerator over test.css. */
  interface FullTestCss extends CssResource {
    String affectedBySprite();

    String blahA();

    String css3Color();

    String externalA();

    String externalB();

    String externalC();

    String extraSpriteClass();

    @ClassName("FAIL")
    String fAIL();

    @ClassName("may-combine")
    String mayCombine();

    @ClassName("may-combine2")
    String mayCombine2();

    @ClassName("may-merge")
    String mayMerge();

    @ClassName("may-not-combine")
    String mayNotCombine();

    @ClassName("may-not-combine2")
    String mayNotCombine2();

    @ClassName("may-not-merge")
    String mayNotMerge();

    @ClassName("may-not-merge-or-combine-because-of-this")
    String mayNotMergeOrCombineBecauseOfThis();

    String multiClassA();

    String multiClassB();

    String nestedSprite();

    String replacement();

    @ClassName("replacement-not-java-ident")
    String replacementNotJavaIdent();

    String spriteClass();

    @ClassName("this-does-not-matter")
    String thisDoesNotMatter();
  }

  @Resource
  interface SiblingResources extends ClientBundle {
    @Source("siblingTestA.css")
    MyCssResourceA a();

    @Source("siblingTestB.css")
    MyCssResourceB b();
  }

  interface HasDescendants extends CssResource {
    String foo();
  }

  interface MyCssResource extends CssResource, MyNonCssResource {
    @Override
    @ClassName("replacement-not-java-ident")
    String nameOverride();
  }

  interface MyCssResourceA extends MyCssResource, SharedClasses {
    String local();

    // This shouldn't make a difference
    @Override
    String replacement();
  }

  @ImportedWithPrefix("gwt-MyCssResourceB")
  interface MyCssResourceB extends MyCssResource, SharedClasses {
    String local();

    @Override
    String sharedOverrideClass();
  }

  /*
   * Check type inheritance.
   */
  interface MyCssResourceWithSprite extends MyCssResource {
    String externalA();

    String extraSpriteClass();

    String multiClassA();

    String multiClassB();
  }

  interface MyNonCssResource {
    String nameOverride();

    String replacement();
  }

  /**
   * CssResource used to test that the compiler will pick up the right resource (.css or .gss)
   * according to the module configuration.
   */
  interface ResourceFileChooser extends CssResource {
    String myClass();
  }

  @Resource
  interface NestedResources extends ClientBundle {
    @Source("32x32.png")
    DataResource dataMethod();

    @Source("16x16.png")
    ImageResource spriteMethod();
  }

  @Resource
  interface Resources {
    Resources INSTANCE = new CSSResourceTest_ResourcesImpl();

    @Source("siblingTestA.css")
    MyCssResourceA a();

    @Source("siblingTestB.css")
    MyCssResourceB b();

    @Source("test.css")
    FullTestCss css();

    @Source("32x32.png")
    CustomDataResource customDataMethod();

    @Source("16x16.png")
    CustomImageResource customSpriteMethod();

    @Source("32x32.png")
    DataResource dataMethod();

    // Test default extensions
    CssWithDefines deftest();

    @Source("unrelatedDescendants.css")
    @Import(value = {MyCssResourceA.class, MyCssResourceB.class})
    HasDescendants descendants();

    // Make sure an empty, no-op CssResource works
    CssResource empty();

    // Test nested ClientBundles
    NestedResources nested(); // TODO gwt:package ok, tests & debug nope

    @Source("16x16.png")
    ImageResource spriteMethod();

    ResourceFileChooser resourceFileChooser();

    @Source("resourceFileChooser.css")
    ResourceFileChooser resourceFileChooserWithSourceTargetingOneCssFile();

    @Source("resourceFileChooser.gss")
    ResourceFileChooser resourceFileChooserWithSourceTargetingOneGssFile();

    @Source({"resourceFileChooser.css", "resourceFileChooser2.css"})
    ResourceFileChooser resourceFileChooserWithSourceTargetingCssFiles();

    @Source({"resourceFileChooser.gss", "resourceFileChooser2.gss"})
    ResourceFileChooser resourceFileChooserWithSourceTargetingGssFiles();

    @Source({"resourceFileChooser.css", "resourceFileChooser2.css", "resourceFileChooser3.css"})
    ResourceFileChooser resourceFileChooserWithSourceTargetingCssFilesWithoutGssFile();
  }

  interface SharedBase extends CssResource {
    String unsharedClass();
  }

  @ImportedWithPrefix("gwt-Shared")
  @Shared
  interface SharedClasses extends SharedBase {
    String sharedClass();

    String sharedOverrideClass();
  }
}
