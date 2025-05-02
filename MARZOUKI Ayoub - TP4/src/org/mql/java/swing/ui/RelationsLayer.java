package org.mql.java.swing.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.mql.java.swing.business.ProjectProcessor;
import org.mql.java.swing.business.UMLDiagramsGenerator;
import org.mql.java.swing.ui.relations.cls.Aggregation;
import org.mql.java.swing.ui.relations.cls.Composition;
import org.mql.java.swing.ui.relations.cls.Dependency;
import org.mql.java.swing.ui.relations.cls.Generalization;
import org.mql.java.swing.ui.relations.cls.Realization;
import org.mql.java.swing.ui.relations.pack.Merge;

public class RelationsLayer extends JPanel {
	 public static HashMap<String, String> mergeRelations = ProjectProcessor.mergeRelations;
	 public static HashMap<String, PackageDiagram> packageNameToDiagramMap = UMLDiagramsGenerator.packageNameToDiagramMap;
	 
	 public static HashMap<String, String> generalizationRelations = ProjectProcessor.generalizationRelations;
	 public static HashMap<String, ClassDiagram> parentChildClasses = UMLDiagramsGenerator.parentChildClasses;
	 
	 public static HashMap<String, String> aggregationRelations = ProjectProcessor.aggregationRelations;
	 public static HashMap<String, ClassDiagram> classAggregationMappings = UMLDiagramsGenerator.classAggregationMappings;
	 
	 public static HashMap<String, String> compositionRelations = ProjectProcessor.aggregationRelations;
	 public static HashMap<String, ClassDiagram> classCompositionMappings = UMLDiagramsGenerator.classAggregationMappings;
	 
	 public static HashMap<String, String> realizationRelations = ProjectProcessor.realizationRelations;
	 public static HashMap<String, ClassDiagram> classRealizationMappings = UMLDiagramsGenerator.classRealizationMappings;
	 
	 public static HashMap<String, String> dependencyRelations = ProjectProcessor.realizationRelations;
	 public static HashMap<String, ClassDiagram> classDependencyMappings = UMLDiagramsGenerator.classRealizationMappings;
	 

	 Component[] components = Frame.getDiagramsLayer().getComponents();
	    
	 @Override
	 public void paintComponent(Graphics g) {
		 
	     drawMergeRelations(g);
	     drawGeneralizationRelations(g);
	     drawAggregationRelations(g);
	     drawCompositionRelations(g);
	     drawRealizationRelations(g);
	     drawDependencyRelations(g);
	     
	     revalidate();
	    }
	 
	 private void drawMergeRelations(Graphics g) {
		 int sourceIndex = -1;
		 int targetIndex = -1;
		 for (Map.Entry<String, String> mergeRelationsEntry : mergeRelations.entrySet()) {
			    String sourcePackageName = mergeRelationsEntry.getKey();
			    String targetPackageName = mergeRelationsEntry.getValue();

			    // Directly retrieve the diagrams
			    PackageDiagram sourcePackageDiagram = packageNameToDiagramMap.get(sourcePackageName);
			    PackageDiagram targetPackageDiagram = packageNameToDiagramMap.get(targetPackageName);
			    
			    for (int i = 0; i < components.length; i++) {
			        if (components[i] == sourcePackageDiagram) {
			            sourceIndex = i;
			        }
			        if (components[i] == targetPackageDiagram) {
			            targetIndex = i;
			        }
			    }
			    
			    Merge.draw(g, sourcePackageDiagram, sourceIndex, targetIndex);
			}
	 }
	 
	 private void drawGeneralizationRelations(Graphics g) {
		 int packageSourceIndex = -1;
		 int packageTargetIndex = -1;
		 
		 int classSourceIndex = -1;
		 int classTargetIndex = -1;
		 
		 for (Map.Entry<String, String> generalizationRelationsEntry : generalizationRelations.entrySet()) {
			    String sourceClassName = generalizationRelationsEntry.getKey();
			    String targetClassName = generalizationRelationsEntry.getValue();

			    ClassDiagram sourceClassDiagram = parentChildClasses.get(sourceClassName);
			    ClassDiagram targetClassDiagram = parentChildClasses.get(targetClassName);
			  
			    
			    PackageDiagram sourcePackageDiagram = getClassPackage(sourceClassDiagram);
	            PackageDiagram targetPackageDiagram = getClassPackage(targetClassDiagram);

			    
			    classSourceIndex = getClassIndexInPackage(sourceClassDiagram, sourcePackageDiagram);
		        classTargetIndex = getClassIndexInPackage(targetClassDiagram, targetPackageDiagram);
			    
			    for (int i = 0; i < components.length; i++) {
			        if (components[i] == sourcePackageDiagram) {
			        	packageSourceIndex = i;
			        }
			        if (components[i] == targetPackageDiagram) {
			        	packageTargetIndex = i;
			        }
			    }
			    
			    boolean classesAreAdjacent = (sourcePackageDiagram == targetPackageDiagram) && 
                        (Math.abs(classSourceIndex - classTargetIndex) == 1);
			    
			    boolean pointsLeft = (classSourceIndex > classTargetIndex); // direction = 0
			    boolean pointsRight = (classSourceIndex < classTargetIndex); // direction = 1
			    
			    boolean sourceUnderTarget = (packageSourceIndex > packageTargetIndex);
			    
			    int verticalLine = calculateVerticalLine();
			    
			    
			    int[] sourcePositionXY;
		        int[] targetPositionXY;

		        if (classesAreAdjacent) {
		            if (pointsLeft) {
		                // Draw from left border of source to right border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 0);
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 1);
		                
		                Generalization.drawAdjacent(g, targetPositionXY, sourcePositionXY, 0);
		            } else if (pointsRight) {
		                // Draw from right border of source to left border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 1); // Source: left border
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 0); // Target: right border
		                
		                Generalization.drawAdjacent(g, sourcePositionXY, targetPositionXY, 1);
		            }
		        } else {
		            // Non-adjacent classes
		            sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, !sourceUnderTarget);
		            targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, sourceUnderTarget);
		            
		            Generalization.draw(g, sourcePositionXY, targetPositionXY, 0, verticalLine);
		        }
			    
			}
		}
	 
	 private void drawAggregationRelations(Graphics g) {
		 int packageSourceIndex = -1;
		 int packageTargetIndex = -1;
		 
		 int classSourceIndex = -1;
		 int classTargetIndex = -1;
		 
		 for (Map.Entry<String, String> generalizationRelationsEntry : aggregationRelations.entrySet()) {
			    String sourceClassName = generalizationRelationsEntry.getKey();
			    String targetClassName = generalizationRelationsEntry.getValue();

			    ClassDiagram sourceClassDiagram = classAggregationMappings.get(sourceClassName);
			    ClassDiagram targetClassDiagram = classAggregationMappings.get(targetClassName);
			  
			    
			    PackageDiagram sourcePackageDiagram = getClassPackage(sourceClassDiagram);
	            PackageDiagram targetPackageDiagram = getClassPackage(targetClassDiagram);

			    
			    classSourceIndex = getClassIndexInPackage(sourceClassDiagram, sourcePackageDiagram);
		        classTargetIndex = getClassIndexInPackage(targetClassDiagram, targetPackageDiagram);
			    
			    for (int i = 0; i < components.length; i++) {
			        if (components[i] == sourcePackageDiagram) {
			        	packageSourceIndex = i;
			        }
			        if (components[i] == targetPackageDiagram) {
			        	packageTargetIndex = i;
			        }
			    }
			    
			    boolean classesAreAdjacent = (sourcePackageDiagram == targetPackageDiagram) && 
                        (Math.abs(classSourceIndex - classTargetIndex) == 1);
			    
			    boolean pointsLeft = (classSourceIndex > classTargetIndex); // direction = 0
			    boolean pointsRight = (classSourceIndex < classTargetIndex); // direction = 1
			    
			    boolean sourceUnderTarget = (packageSourceIndex > packageTargetIndex);
			    
			    int verticalLine = calculateVerticalLine();
			    
			    
			    int[] sourcePositionXY;
		        int[] targetPositionXY;

		        if (classesAreAdjacent) {
		            if (pointsLeft) {
		                // Draw from left border of source to right border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 0);
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 1);
		                
		                Aggregation.drawAdjacent(g, targetPositionXY, sourcePositionXY, 0);
		            } else if (pointsRight) {
		                // Draw from right border of source to left border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 1); // Source: left border
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 0); // Target: right border
		                
		                Aggregation.drawAdjacent(g, sourcePositionXY, targetPositionXY, 1);
		            }
		        } else {
		            // Non-adjacent classes
		            sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, !sourceUnderTarget);
		            targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, sourceUnderTarget);
		            
		            Aggregation.draw(g, sourcePositionXY, targetPositionXY, 0, verticalLine);
		        }
			    
			}
		}
	 
	 private void drawCompositionRelations(Graphics g) {
		 int packageSourceIndex = -1;
		 int packageTargetIndex = -1;
		 
		 int classSourceIndex = -1;
		 int classTargetIndex = -1;
		 
		 for (Map.Entry<String, String> generalizationRelationsEntry : compositionRelations.entrySet()) {
			    String sourceClassName = generalizationRelationsEntry.getKey();
			    String targetClassName = generalizationRelationsEntry.getValue();

			    ClassDiagram sourceClassDiagram = classCompositionMappings.get(sourceClassName);
			    ClassDiagram targetClassDiagram = classCompositionMappings.get(targetClassName);
			  
			    
			    PackageDiagram sourcePackageDiagram = getClassPackage(sourceClassDiagram);
	            PackageDiagram targetPackageDiagram = getClassPackage(targetClassDiagram);

			    
			    classSourceIndex = getClassIndexInPackage(sourceClassDiagram, sourcePackageDiagram);
		        classTargetIndex = getClassIndexInPackage(targetClassDiagram, targetPackageDiagram);
			    
			    for (int i = 0; i < components.length; i++) {
			        if (components[i] == sourcePackageDiagram) {
			        	packageSourceIndex = i;
			        }
			        if (components[i] == targetPackageDiagram) {
			        	packageTargetIndex = i;
			        }
			    }
			    
			    boolean classesAreAdjacent = (sourcePackageDiagram == targetPackageDiagram) && 
                        (Math.abs(classSourceIndex - classTargetIndex) == 1);
			    
			    boolean pointsLeft = (classSourceIndex > classTargetIndex); // direction = 0
			    boolean pointsRight = (classSourceIndex < classTargetIndex); // direction = 1
			    
			    boolean sourceUnderTarget = (packageSourceIndex > packageTargetIndex);
			    
			    int verticalLine = calculateVerticalLine();
			    
			    
			    int[] sourcePositionXY;
		        int[] targetPositionXY;

		        if (classesAreAdjacent) {
		            if (pointsLeft) {
		                // Draw from left border of source to right border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 0);
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 1);
		                
		                Composition.drawAdjacent(g, targetPositionXY, sourcePositionXY, 0);
		            } else if (pointsRight) {
		                // Draw from right border of source to left border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 1); // Source: left border
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 0); // Target: right border
		                
		                Composition.drawAdjacent(g, sourcePositionXY, targetPositionXY, 1);
		            }
		        } else {
		            // Non-adjacent classes
		            sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, !sourceUnderTarget);
		            targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, sourceUnderTarget);
		            
		            Composition.draw(g, sourcePositionXY, targetPositionXY, 0, verticalLine);
		        }
			    
			}
		}
	 
	 private void drawRealizationRelations(Graphics g) {
		 int packageSourceIndex = -1;
		 int packageTargetIndex = -1;
		 
		 int classSourceIndex = -1;
		 int classTargetIndex = -1;
		 
		 for (Map.Entry<String, String> generalizationRelationsEntry : realizationRelations.entrySet()) {
			    String sourceClassName = generalizationRelationsEntry.getKey();
			    String targetClassName = generalizationRelationsEntry.getValue();

			    ClassDiagram sourceClassDiagram = classRealizationMappings.get(sourceClassName);
			    ClassDiagram targetClassDiagram = classRealizationMappings.get(targetClassName);
			  
			    
			    PackageDiagram sourcePackageDiagram = getClassPackage(sourceClassDiagram);
	            PackageDiagram targetPackageDiagram = getClassPackage(targetClassDiagram);

			    
			    classSourceIndex = getClassIndexInPackage(sourceClassDiagram, sourcePackageDiagram);
		        classTargetIndex = getClassIndexInPackage(targetClassDiagram, targetPackageDiagram);
			    
			    for (int i = 0; i < components.length; i++) {
			        if (components[i] == sourcePackageDiagram) {
			        	packageSourceIndex = i;
			        }
			        if (components[i] == targetPackageDiagram) {
			        	packageTargetIndex = i;
			        }
			    }
			    
			    boolean classesAreAdjacent = (sourcePackageDiagram == targetPackageDiagram) && 
                        (Math.abs(classSourceIndex - classTargetIndex) == 1);
			    
			    boolean pointsLeft = (classSourceIndex > classTargetIndex); // direction = 0
			    boolean pointsRight = (classSourceIndex < classTargetIndex); // direction = 1
			    
			    boolean sourceUnderTarget = (packageSourceIndex > packageTargetIndex);
			    
			    int verticalLine = calculateVerticalLine();
			    
			    
			    int[] sourcePositionXY;
		        int[] targetPositionXY;

		        if (classesAreAdjacent) {
		            if (pointsLeft) {
		                // Draw from left border of source to right border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 0);
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 1);
		                
		                Realization.drawAdjacent(g, targetPositionXY, sourcePositionXY, 0);
		            } else if (pointsRight) {
		                // Draw from right border of source to left border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 1); // Source: left border
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 0); // Target: right border
		                
		                Realization.drawAdjacent(g, sourcePositionXY, targetPositionXY, 1);
		            }
		        } else {
		            // Non-adjacent classes
		            sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, !sourceUnderTarget);
		            targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, sourceUnderTarget);
		            
		            Realization.draw(g, sourcePositionXY, targetPositionXY, 0, verticalLine);
		        }
			    
			}
		}
	 
	 private void drawDependencyRelations(Graphics g) {
		 int packageSourceIndex = -1;
		 int packageTargetIndex = -1;
		 
		 int classSourceIndex = -1;
		 int classTargetIndex = -1;
		 
		 for (Map.Entry<String, String> generalizationRelationsEntry : dependencyRelations.entrySet()) {
			    String sourceClassName = generalizationRelationsEntry.getKey();
			    String targetClassName = generalizationRelationsEntry.getValue();

			    ClassDiagram sourceClassDiagram = classDependencyMappings.get(sourceClassName);
			    ClassDiagram targetClassDiagram = classDependencyMappings.get(targetClassName);
			  
			    
			    PackageDiagram sourcePackageDiagram = getClassPackage(sourceClassDiagram);
	            PackageDiagram targetPackageDiagram = getClassPackage(targetClassDiagram);

			    
			    classSourceIndex = getClassIndexInPackage(sourceClassDiagram, sourcePackageDiagram);
		        classTargetIndex = getClassIndexInPackage(targetClassDiagram, targetPackageDiagram);
			    
			    for (int i = 0; i < components.length; i++) {
			        if (components[i] == sourcePackageDiagram) {
			        	packageSourceIndex = i;
			        }
			        if (components[i] == targetPackageDiagram) {
			        	packageTargetIndex = i;
			        }
			    }
			    
			    boolean classesAreAdjacent = (sourcePackageDiagram == targetPackageDiagram) && 
                        (Math.abs(classSourceIndex - classTargetIndex) == 1);
			    
			    boolean pointsLeft = (classSourceIndex > classTargetIndex); // direction = 0
			    boolean pointsRight = (classSourceIndex < classTargetIndex); // direction = 1
			    
			    boolean sourceUnderTarget = (packageSourceIndex > packageTargetIndex);
			    
			    int verticalLine = calculateVerticalLine();
			    
			    
			    int[] sourcePositionXY;
		        int[] targetPositionXY;

		        if (classesAreAdjacent) {
		            if (pointsLeft) {
		                // Draw from left border of source to right border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 0);
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 1);
		                
		                Dependency.drawAdjacent(g, targetPositionXY, sourcePositionXY, 0);
		            } else if (pointsRight) {
		                // Draw from right border of source to left border of target
		                sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, 1); // Source: left border
		                targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, 0); // Target: right border
		                
		                Dependency.drawAdjacent(g, sourcePositionXY, targetPositionXY, 1);
		            }
		        } else {
		            // Non-adjacent classes
		            sourcePositionXY = calculateClassPosition(sourcePackageDiagram, sourceClassDiagram, packageSourceIndex, classSourceIndex, !sourceUnderTarget);
		            targetPositionXY = calculateClassPosition(targetPackageDiagram, targetClassDiagram, packageTargetIndex, classTargetIndex, sourceUnderTarget);
		            
		            Dependency.draw(g, sourcePositionXY, targetPositionXY, 0, verticalLine);
		        }
			    
			}
		}
	 
	 
	 
	 private int calculateVerticalLine() {
		 return (900 - 200 - ClassDiagram.getClassDiagramHeight()) + 30 / 2 ; // vgap of flow layout + gap between packages / 2
	 }
	 
	 
	private PackageDiagram getClassPackage(ClassDiagram classDiagram) {
		    // Iterate through all components in the frame (or container)
		    for (Component c : components) {
		        if (c instanceof PackageDiagram) {
		            // Get the components inside the PackageDiagram directly
		            Component[] comp = ((Container) c).getComponents();
		            
		            // Iterate through the components inside the PackageDiagram
		            for (Component component : comp) {
		                if (component instanceof JPanel) {
		                    // Get the components inside the JPanel directly
		                    Component[] comp2 = ((Container) component).getComponents();
		                    
		                    // Check if classDiagram is inside the JPanel
		                    for (Component cc : comp2) {
		                        if (cc == classDiagram) {
		                            // If found, return the parent PackageDiagram
		                            return (PackageDiagram) c;
		                        }
		                    }
		                }
		            }
		        }
		    }
		    
		    return null;  // Return null if no PackageDiagram contains the classDiagram
		}

	 private int getClassIndexInPackage(ClassDiagram classDiagram, PackageDiagram packageDiagram) {
		    // Start searching from the PackageDiagram's children
		    return findClassIndex(classDiagram, packageDiagram, 0);
		}

		// Helper method to recursively search for the ClassDiagram and calculate its index
		private int findClassIndex(ClassDiagram classDiagram, Container container, int currentIndex) {
		    // Get all components in the current container
		    Component[] components = container.getComponents();

		    for (Component component : components) {
		        if (component instanceof ClassDiagram && component == classDiagram) {
		            // If the component is the ClassDiagram we're looking for, return its index
		            return currentIndex;
		        } else if (component instanceof Container) {
		            // If the component is a container (e.g., JPanel), recursively search its children
		            int index = findClassIndex(classDiagram, (Container) component, currentIndex);
		            if (index != -1) {
		                // If the ClassDiagram is found in the nested container, return its index
		                return index;
		            }
		        }

		        // Increment the index for each component
		        currentIndex++;
		    }

		    // If the ClassDiagram is not found, return -1
		    return -1;
		}

	 	
	 	public static int[] calculateClassPosition(PackageDiagram pack, ClassDiagram classDiagram, int packageIndex, int classIndex, int direction) {
	 	    int packageHeight = 900;
	 	    int gapBetweenPackages = 30;
	 		
	 	    int x;
	 	    
	 	    if (direction == 0 ) {
	 	    	// x will be the left border of the class
	 	    	x = Merge.diagramsLayerHGap +  pack.getLeftMargin() + classIndex * (200 * pack.getNumberOfPackageDiagrams() + classDiagram.getClassDiagramWidth() + 13) + 14; // 13 being HGap of flow layout, 14 unknown for now
	 	    } else {
	 	    	// x will be the right border of the class
	 	    	x = Merge.diagramsLayerHGap +  pack.getLeftMargin() + classIndex * (200 * pack.getNumberOfPackageDiagrams() + classDiagram.getClassDiagramWidth() + 13) + 14 + classDiagram.getClassDiagramWidth(); // 13 being HGap of flow layout, 14 unknown for now
	 	    }
	 	    
	 	    // y will be the middle of either of the borders
	 	    int y = Merge.diagramsLayerVGap + packageIndex * (gapBetweenPackages + packageHeight) + 200 + classDiagram.getClassDiagramHeight() / 2;
	 	    return new int[] { x, y };
	 	}
	 	
	 	public static int[] calculateClassPosition(PackageDiagram pack, ClassDiagram classDiagram, int packageIndex, int classIndex, boolean sourceUnderTarget) {
	 	    // Calculate the x and y positions based on package and class indices
	 	    int packageHeight = 900;
	 	    int gapBetweenPackages = 30;
	 		
	 	    
	 	    // x will be the at middle of either the top or bottom borders
	 		int x = Merge.diagramsLayerHGap +  pack.getLeftMargin() + classIndex * (200 * pack.getNumberOfPackageDiagrams() + classDiagram.getClassDiagramWidth() + 13) + 14 + classDiagram.getClassDiagramWidth() / 2; // 13 being HGap of flow layout, 14 unknown for now
	 		
	 		int y;
	 		if (sourceUnderTarget) {
//	 	    y will reach the upper border of the class in this case
	 			y = Merge.diagramsLayerVGap + packageIndex * (gapBetweenPackages + packageHeight) + 200 + classDiagram.getClassDiagramHeight(); // 200 being Vgap of flow layout (vertical distance between class diagram and its container) 
	 	    } else {
	 			y = Merge.diagramsLayerVGap + packageIndex * (gapBetweenPackages + packageHeight) + 200;
	 	    }
	 	    return new int[] { x, y };
	 	}
	    

//	 public void addAggregation(Aggregation agg) {
//	    	aggregationRelations.add(agg);
//	 }

}
