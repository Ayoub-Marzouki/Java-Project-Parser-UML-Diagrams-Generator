<h1>Java Project Parser & UML Diagrams Generator</h1>

<h2>Navigation</h2>
<ul>
  <li><a href="#overview">Overview</a></li>
  <li><a href="#usage">Usage</a></li>
  <li><a href="#how-it-works">How it works</a></li>
  <li><a href="#features">Features</a></li>
  <li><a href="#requirements">Requirements</a></li>
</ul>

<br>

<h2 id="overview">Overview</h2>
<p>
  This tool provides 2 services : <br>
  + A Swing-based visualizer that generates UML-style diagrams based on a given source folder path. The latter is the user's input. <br>
  + A parser that parses Java projects to extract structured information about classes, interfaces, enums, annotations, etc, and their relationships, and exports the data to an XML file.  <br><br>
  This system allows for an organized representation of a project, which can be useful for visualization, analysis, or further processing.
</p>

<br>

<h2 id="usage">How To Use</h2>

<h3>XML Export</h3>
<p>To parse and export a project:</p>
<pre><code>Project project = JavaProjectParser.parse("path/to/project");
XMLWriter.write(project, new File("output.xml"));</code></pre>

![image](https://github.com/user-attachments/assets/41c88269-0d14-4fd7-b13b-b1df9dbd7654)

<p>The result can be found under "output.xml" file or whatever's the name that you chose, if you chose one: </p>

![image](https://github.com/user-attachments/assets/dccfa834-11a3-4745-b88f-2b9e7a78c257)

<br>

<h3>UML Diagrams Generator</h3>
<p>1 - First, it is necessary that you compile the project that you want to use the tool on.</p>
<p>2 - Second, you're going to need to include the project’s build output to the project's classpath : <br>
  <p>+ Simply right click on the project, go to properties :</p>
  
  ![image](https://github.com/user-attachments/assets/98828aa8-3e84-472e-9f49-6f34826118d0)

  <p>+ Select "Java Build Path" :</p> 
  
  ![image](https://github.com/user-attachments/assets/b9de6fc9-a336-43cf-874e-80ae5ad189e8)
  
  <p>+ Add Class Folder (bin) to ClassPath :</p> 

![image](https://github.com/user-attachments/assets/2b7472d1-cd41-490b-91d9-01b2e6584087)


</p>
<p>+ Finally, Launch the Swing GUI. Provide the source directory of the project. Click to generate diagrams. Example : </p>

![image](https://github.com/user-attachments/assets/03924120-120e-4711-83da-28c4df950615)

<br>

![image](https://github.com/user-attachments/assets/43a07ba7-7ed2-4a70-ad89-046eaf569ae8)

<br>

![image](https://github.com/user-attachments/assets/8a11cdcb-346a-4311-990f-41577362b38b)

![image](https://github.com/user-attachments/assets/a017a22b-db5c-42f3-88a3-5037e907bfd5)





<p>
  A thorough explanation can be found in 'Frame-details' txt file, where the entire drawing logic is described. <br>
  Basically, the frame is like a set of lines. Each line is reserved for a package diagram, inside each of these are class diagrams. <br>
  Some space in the right side of the frame that is reserved to package diagram relations. <br>
  Between each package diagram and another is some space, it is reserved to class diagram relations. <br>
  This specific structures makes sure that there will be no colliding of diagrams nor relations, this way everything will be perfectly visible and understandable.
</p>

<h2 id="how-it-works">How it works</h2>

<h3 id="architectural-approach">Architectural Approach : Model-Delegate Pattern + Modular Monolith Design</h3>

<h4>Model-Delegate Pattern</h4>
<p>
  This project implements Oracle's recommended
  <a href="https://www.oracle.com/java/technologies/a-swing-architecture.html">Model-Delegate architecture</a>
  with modern adaptations:
</p>

<div class="architecture-comparison">
  <div class="pattern">
    <strong>Model Components</strong>
    <ul>
      <li><code>xml.model.*</code> classes (Project, Class, Relationship)</li>
      <li><code>swing.business.ProjectProcessor</code></li>
      <li><code>UMLClassMetadataExtractor</code></li>
    </ul>
    <p>↳ Store core data and business logic</p>
  </div>
  <div class="pattern">
    <strong>Delegate Components</strong>
    <ul>
      <li><code>swing.ui.*Diagram</code> classes</li>
      <li><code>relations.*</code> renderers</li>
      <li><code>RelationsLayer</code></li>
    </ul>
    <p>↳ Handle presentation and user interaction</p>
  </div>
</div>

<p>Key implementation details:</p>
<ul>
  <li><strong>Separation:</strong> Models never reference Swing classes directly</li>
  <li><strong>Observer Pattern:</strong> UI components listen to model changes via property change events</li>
  <li><strong>Custom Delegates:</strong> Each relationship type (<code>Aggregation</code>, <code>Generalization</code>) implements a consistent rendering interface</li>
</ul>

<h4>Modular Monolith Design</h4>
<p>
  While structured as a single codebase, the system follows modular principles:
</p>

<div class="modular-grid">
  <div class="module">
    <strong>Core Modules</strong>
    <ul>
      <li>XML Processing (<code>org.mql.java.xml</code>)</li>
      <li>Diagram Rendering (<code>org.mql.java.swing</code>)</li>
      <li>Utilities (<code>org.mql.java.util</code>)</li>
    </ul>
    <p>The point being, the project is packaged by features, each totally independent of the other.</p>
  </div>
  <div class="module">
    <strong>Key Modular Features</strong>
    <ul>
      <li>Pluggable relationship renderers</li>
      <li>Interchangeable XML formats</li>
      <li>Independent test harness</li>
    </ul>
  </div>
</div>

<p>Monolithic advantages with modular flexibility:</p>
<ul>
  <li><strong>Shared Utilities:</strong> Common geometry/drawing code in <code>util</code> package</li>
  <li><strong>Consistent Lifecycle:</strong> Single JVM instance manages all components</li>
  <li><strong>Easier Testing:</strong> Integrated mock data (<code>sample.data</code>)</li>
</ul>

<p>Architectural tradeoffs:</p>
<table>
  <thead>
    <tr>
      <th>Advantage</th>
      <th>Implementation</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Modular testability</td>
      <td>Each <code>relations.*</code> renderer can be tested independently</td>
    </tr>
    <tr>
      <td>Monolithic efficiency</td>
      <td>In-memory model shared between XML and Swing layers</td>
    </tr>
    <tr>
      <td>Flexible scaling</td>
      <td>New diagram types can be added without core changes</td>
    </tr>
  </tbody>
</table>

<p>
  This hybrid approach combines Swing's proven patterns with modern maintainability requirements, allowing both:
</p>
<ul>
  <li>Quick feature development within the monolith</li>
  <li>Safe refactoring through module boundaries</li>
</ul>


<h3>Project Structure</h3>
<pre><code>
UML-Diagrams-Generator/
└── org/
    └── mql/
        └── java/
            ├── sample/
            │   └── data/
            │       └── main/
            │           └── pack/
            │               ├── Class1.java         (extends Class2)
            │               ├── Class2.java         (implements Sub1Sub1Class1)
            │               ├── InnerClass.java
            │               ├── Class3.java
            │               │
            │               ├── sub1/
            │               │   ├── Sub1Class1.java
            │               │   └── Sub1Class2.java
            │               │
            │               ├── sub1/
            │               │   └── sub1/
            │               │       ├── Sub1Sub1Class2.java
            │               │       └── Sub1Sub1Class1.java  (interface)
            │               │
            │               └── sub2/
            │                   ├── Sub2Class1.java
            │                   └── Sub2Class2.java
            │
            ├── application/
            │   ├── SwingMain.java
            │   └── XMLMain.java
            │
            ├── swing/
            │   ├── business/
            │   │   ├── ProjectProcessor.java
            │   │   ├── Structure.java
            │   │   └── UMLClassMetadataExtractor.java
            │   │
            │   ├── Testing.java
            │   │
            │   └── ui/
            │       ├── ClassDiagram.java      (extends JPanel)
            │       ├── Frame.java            (extends JFrame)
            │       ├── PackageDiagram.java    (extends JPanel)
            │       ├── RelationsLayer.java    (extends JPanel)
            │       │
            │       ├── relations/
            │       │   ├── cls/
            │       │   │   ├── Aggregation.java
            │       │   │   ├── Association.java
            │       │   │   ├── AssociationClass.java
            │       │   │   ├── Composition.java
            │       │   │   ├── Dependency.java
            │       │   │   ├── Generalization.java
            │       │   │   └── Realization.java
            │       │   │
            │       │   └── pack/
            │       │       ├── Access.java
            │       │       ├── Import.java
            │       │       └── Merge.java
            │
            ├── util/
            │   ├── SwingUtilities.java
            │   └── Direction.java        (enum)
            │
            └── xml/
                ├── business/
                │   ├── JavaProjectParser.java
                │   └── XMLParser.java
                │
                └── model/
                    ├── Annotation.java
                    ├── Class.java
                    ├── Enum.java
                    ├── Interface.java
                    ├── Package.java
                    ├── Project.java
                    ├── Relationship.java
                    └── Type.java          (enum)
</code>
</pre>

<p>This repository is organized into several logical layers, each containing a thorough explanation & description in a txt file, each with its own package hierarchy and responsibilities:</p>

<ul>
  <li>
    <strong><code>org.mql.java.sample.data</code></strong><br>
    A small sample Java project used for testing and demonstration.  
    Contains a multi-level package tree with:
    <ul>
      <li><code>Class1.java</code> (extends Class2)</li>
      <li><code>Class2.java</code> (implements Sub1Sub1Class1)</li>
      <li><code>InnerClass.java</code>, <code>Class3.java</code></li>
      <li><code>sub1</code> and nested <code>sub1.sub1</code> packages with classes and one interface</li>
      <li><code>sub2</code> package with additional classes</li>
    </ul>
  </li>

  <li>
    <strong><code>org.mql.java.application</code></strong><br>
    Entry points for XML export and Swing visualization:
    <ul>
      <li><code>XMLMain.java</code> – loads a project, invokes the XML parser, writes output.</li>
      <li><code>SwingMain.java</code> – launches the Swing GUI (<code>Frame</code>), hooks up the “Generate Diagrams” button.</li>
    </ul>
  </li>

  <li>
    <strong><code>org.mql.java.swing</code></strong><br>
    <em>Business and UI for the UML diagram generator.</em>
    <ul>
      <li>
        <strong>business</strong><br>
        Handles source-folder traversal, metadata extraction, and diagram orchestration:
        <ul>
          <li><code>ProjectProcessor.java</code> – scans <code>src</code> folders, builds lists of packages &amp; classes.</li>
          <li><code>Structure.java</code> – shared data structures or helpers.</li>
          <li><code>UMLClassMetadataExtractor.java</code> – uses reflection to read fields, methods, superclasses, interfaces.</li>
        </ul>
      </li>
      <li>
        <strong>ui</strong><br>
        Swing components that render the diagrams:
        <ul>
          <li><code>Frame.java</code> – main window with input field, buttons, and a scrollable, zoomable canvas.</li>
          <li><code>ClassDiagram.java</code> – draws a single class box (name, fields, methods).</li>
          <li><code>PackageDiagram.java</code> – wraps multiple <code>ClassDiagram</code> instances in a package box.</li>
          <li><code>RelationsLayer.java</code> – overlays arrows and labels for all relationships.</li>
          <li>
            <code>relations.cls</code> – strategy classes for <code>Aggregation</code>, <code>Composition</code>, <code>Dependency</code>, <code>Generalization</code>, <code>Realization</code>, etc.
          </li>
          <li>
            <code>relations.pack</code> – handles package-level connections like <code>Merge</code>, <code>Import</code>, etc.
          </li>
        </ul>
      </li>
      <li>
        <strong>util</strong><br>
        Shared utilities for drawing and geometry:
        <ul>
          <li><code>SwingUtilities.java</code> – text centering, dashed strokes, arrow/diamond-drawing helpers.</li>
          <li><code>Direction.java</code> – enum for arrow directions.</li>
        </ul>
      </li>
      <li>
        <strong>Testing.java</strong><br>
        A small harness for manual or unit tests of the Swing components.
      </li>
    </ul>
  </li>

  <li>
    <strong><code>org.mql.java.xml</code></strong><br>
    Parses and models the project as XML:
    <ul>
      <li>
        <strong>business</strong><br>
        <code>JavaProjectParser.java</code> – scans <code>src</code> by text, builds a <code>Project</code> model;  
        <code>XMLParser.java</code> – reads an existing XML into the model.
      </li>
      <li>
        <strong>model</strong><br>
        Data classes and enums representing:
        <ul>
          <li><code>Project.java</code>, <code>Package.java</code>, <code>Class.java</code>, <code>Interface.java</code>, <code>Enum.java</code>, <code>Annotation.java</code></li>
          <li><code>Relationship.java</code> &amp; <code>Type.java</code> (relationship kinds)</li>
        </ul>
      </li>
    </ul>
  </li>
</ul>

<p>Together, these layers let you:</p>
<ol>
  <li><strong>Parse</strong> any Java project into a rich in-memory model (classes, packages, relationships).</li>
  <li><strong>Export</strong> that model as structured XML for interoperability.</li>
  <li><strong>Visualize</strong> the same model in a Swing GUI, complete with zoom, scrolling, and UML-style diagrams.</li>
</ol>



<h2 id="features">Features</h2>
<ul>
  <li>Parses all <code>.java</code> files within a project directory</li>
  <li>Detects classes, interfaces, enums, annotations and such</li>
  <li>Identifies all potential relationships such as inheritance, implementation, association, generalization, aggregation, ...</li>
  <li>Exports extracted data to XML using <code>XMLStreamWriter</code></li>
  <li>Swing GUI for rendering UML diagrams</li>
</ul>

<h2 id="requirements">Requirements</h2>
<ul>
  <li>Java 8 or later</li>
  <li>Eclipse IDE (for use and testing)</li>
</ul>
