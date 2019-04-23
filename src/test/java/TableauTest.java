/*
 * Tests functionality of Tableau creation and manipulation
 */

import math.linear.basic.ObjectiveFunctionType;
import math.linear.basic.Relation;
import math.linear.problem.Problem;
import math.linear.problem.ProblemEquation;
import math.linear.problem.ProblemObjectiveFunction;
import math.linear.simplex.EquationTableauRow;
import math.linear.simplex.GenericTableauRow;
import math.linear.simplex.ObjectiveFunctionTableauRow;
import math.linear.simplex.Tableau;
import math.linear.simplex.TableauBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class TableauTest
{
    /**
     * Test preparation of a tableau for a simple case: two equations:
     *   1*x_1 + 7*x_2 - 4*x_3 <= 50
     *   6*x_1 +2*x_2 +x_3 <= 20
     *
     *   Objective function Z = 3*x_1 + 4*x_3
     *   Target: find maximum.
     */
    @Test
    public void testTwoLessOrEqualFindMax() {

        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.LESS_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.LESS_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        List<GenericTableauRow> rows = tableau.getRows();
        Assert.assertEquals(3, rows.size());

        List<EquationTableauRow> equationTableauRows = tableau.getEquationRows();
        Assert.assertEquals(2, equationTableauRows.size());

        Assert.assertEquals(BigDecimal.valueOf(50.d), rows.get(0).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(1.d), rows.get(0).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(7.d), rows.get(0).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(-4.d), rows.get(0).getCoefficients().get(3));

        Assert.assertEquals(BigDecimal.valueOf(20.d), rows.get(1).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(6.d), rows.get(1).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(2.d), rows.get(1).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(1.d), rows.get(1).getCoefficients().get(3));


        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(-3.d), objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(-4.d), objFuncRow.getCoefficients().get(3));

        Assert.assertEquals(3, objFuncRow.getIncomingVariableIndex());
        Assert.assertFalse(objFuncRow.isOptimal() );
    }

    /**
     * Test preparation of a tableau for a simple case: two equations:
     *   1*x_1 + 7*x_2 - 4*x_3 <= 50
     *   6*x_1 +2*x_2 +x_3 = 20
     *
     *   Objective function Z = 3*x_1 + 4*x_3
     *   Target: find maximum.
     */
    @Test
    public void testOneLessOrEqualOneEqualFindMax() {

        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.LESS_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        //There must be 4 equations: 2 initial equations + 1 objecitve function + 1 auxiliery function
        List<GenericTableauRow> rows = tableau.getRows();
        Assert.assertEquals(4, rows.size());

        List<EquationTableauRow> equationTableauRows = tableau.getEquationRows();
        Assert.assertEquals(2, equationTableauRows.size());

        Assert.assertEquals(BigDecimal.valueOf(50.d), rows.get(0).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(1.d), rows.get(0).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(7.d), rows.get(0).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(-4.d), rows.get(0).getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ONE, rows.get(0).getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(0).getCoefficients().get(5));

        Assert.assertEquals(BigDecimal.valueOf(20.d), rows.get(1).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(6.d), rows.get(1).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(2.d), rows.get(1).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(1.d), rows.get(1).getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(1).getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ONE, rows.get(1).getCoefficients().get(5));


        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(-3.d), objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(-4.d), objFuncRow.getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(5));

        objFuncIdx = tableau.getAuxiliaryFunctionIndex();
        objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ONE, objFuncRow.getCoefficients().get(5));
    }

    /**
     * Test preparation of a tableau for a simple case: two equations:
     *   1*x_1 + 7*x_2 - 4*x_3 <= 50
     *   6*x_1 +2*x_2 +x_3 >= 20
     *
     *   Objective function Z = 3*x_1 + 4*x_3
     *   Target: find maximum.
     */
    @Test
    public void testOneLessOrEqualOneGreateOrEqualFindMax() {

        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.LESS_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.GREATER_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        //There must be 4 equations: 2 initial equations + 1 objecitve function + 1 auxiliery function
        List<GenericTableauRow> rows = tableau.getRows();
        Assert.assertEquals(4, rows.size());

        List<EquationTableauRow> equationTableauRows = tableau.getEquationRows();
        Assert.assertEquals(2, equationTableauRows.size());

        Assert.assertEquals(BigDecimal.valueOf(50.d), rows.get(0).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(1.d), rows.get(0).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(7.d), rows.get(0).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(-4.d), rows.get(0).getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ONE, rows.get(0).getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(0).getCoefficients().get(5));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(0).getCoefficients().get(5));

        Assert.assertEquals(BigDecimal.valueOf(20.d), rows.get(1).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(6.d), rows.get(1).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(2.d), rows.get(1).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(1.d), rows.get(1).getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(1).getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ONE.negate(), rows.get(1).getCoefficients().get(5));
        Assert.assertEquals(BigDecimal.ONE, rows.get(1).getCoefficients().get(6));


        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(-3.d), objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(-4.d), objFuncRow.getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(5));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(6));

        objFuncIdx = tableau.getAuxiliaryFunctionIndex();
        objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(5));
        Assert.assertEquals(BigDecimal.ONE, objFuncRow.getCoefficients().get(6));
    }

    /**
     * Test preparation of a tableau for a simple case: two equations:
     *   1*x_1 + 7*x_2 - 4*x_3 <= - 30
     *   6*x_1 +2*x_2 +x_3 = -20
     *
     *   Objective function Z = 3*x_1 + 4*x_3
     *   Target: find maximum.
     */
    @Test
    public void testOneGreaterOrEqualNegativeOneEqualNegativeFindMax() {

        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.GREATER_OR_EQUAL, -30.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.EQUAL, -20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MAXIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        //There must be 4 equations: 2 initial equations + 1 objecitve function + 1 auxiliery function
        List<GenericTableauRow> rows = tableau.getRows();
        Assert.assertEquals(4, rows.size());

        List<EquationTableauRow> equationTableauRows = tableau.getEquationRows();
        Assert.assertEquals(2, equationTableauRows.size());

        Assert.assertEquals(BigDecimal.valueOf(30.d), rows.get(0).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(-1.d), rows.get(0).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(-7.d), rows.get(0).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(4.d), rows.get(0).getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ONE, rows.get(0).getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(0).getCoefficients().get(5));

        Assert.assertEquals(BigDecimal.valueOf(20.d), rows.get(1).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(-6.d), rows.get(1).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(-2.d), rows.get(1).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(-1.d), rows.get(1).getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(1).getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ONE, rows.get(1).getCoefficients().get(5));


        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(-3.d), objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(-4.d), objFuncRow.getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(5));

        objFuncIdx = tableau.getAuxiliaryFunctionIndex();
        objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ONE, objFuncRow.getCoefficients().get(5));
    }

    /**
     * Test preparation of a tableau for a simple case: two equations:
     *   1*x_1 + 7*x_2 - 4*x_3 <= 50
     *   6*x_1 +2*x_2 +x_3 <= 20
     *
     *   Objective function Z = 3*x_1 + 4*x_3
     *   Target: find minimum.
     */
    @Test
    public void testTwoGreaterOrEqualFindMin() {

        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.GREATER_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.GREATER_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        //There must be 4 equations: 2 initial equations + 1 objecitve function + 1 auxiliery function
        List<GenericTableauRow> rows = tableau.getRows();
        Assert.assertEquals(4, rows.size());

        List<EquationTableauRow> equationTableauRows = tableau.getEquationRows();
        Assert.assertEquals(2, equationTableauRows.size());

        Assert.assertEquals(BigDecimal.valueOf(50.d), rows.get(0).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(1.d), rows.get(0).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(7.d), rows.get(0).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(-4.d), rows.get(0).getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ONE.negate(), rows.get(0).getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(0).getCoefficients().get(5));
        Assert.assertEquals(BigDecimal.ONE, rows.get(0).getCoefficients().get(6));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(0).getCoefficients().get(7));

        Assert.assertEquals(BigDecimal.valueOf(20.d), rows.get(1).getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(6.d), rows.get(1).getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(2.d), rows.get(1).getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(1.d), rows.get(1).getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(1).getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ONE.negate(), rows.get(1).getCoefficients().get(5));
        Assert.assertEquals(BigDecimal.ZERO, rows.get(1).getCoefficients().get(6));
        Assert.assertEquals(BigDecimal.ONE, rows.get(1).getCoefficients().get(7));


        int objFuncIdx = tableau.getObjectiveFunctionIndex();
        ObjectiveFunctionTableauRow objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.valueOf(3.d), objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.valueOf(0.d), objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.valueOf(4.d), objFuncRow.getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(5));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(6));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(7));

        objFuncIdx = tableau.getAuxiliaryFunctionIndex();
        objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(5));
        Assert.assertEquals(BigDecimal.ONE, objFuncRow.getCoefficients().get(6));
        Assert.assertEquals(BigDecimal.ONE, objFuncRow.getCoefficients().get(7));
    }

    @Test
    public void testPivot(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.GREATER_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.GREATER_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        Tableau tableau = tableauBuilder.build();

        tableau.pivot(1, 2);

        List<GenericTableauRow> rows = tableau.getRows();
        Assert.assertEquals(4, rows.size());

        MathContext mc = new MathContext(3);

        Assert.assertEquals(BigDecimal.valueOf(-20.d).doubleValue(), rows.get(0).getCoefficients().get(0).doubleValue(),1e-4);
        Assert.assertEquals(BigDecimal.valueOf(-20.d).doubleValue(), rows.get(0).getCoefficients().get(1).doubleValue(),1e-4);
        Assert.assertEquals(BigDecimal.valueOf(0.d).doubleValue(), rows.get(0).getCoefficients().get(2).doubleValue(),1e-4);
        Assert.assertEquals(BigDecimal.valueOf(-7.5d).doubleValue(), rows.get(0).getCoefficients().get(3).doubleValue(), 1e-4);

        Assert.assertEquals(BigDecimal.valueOf(10.d).doubleValue(), rows.get(1).getCoefficients().get(0).doubleValue(), 1e-4);
        Assert.assertEquals(BigDecimal.valueOf(3.d).doubleValue(), rows.get(1).getCoefficients().get(1).doubleValue(), 1e-4);
        Assert.assertEquals(BigDecimal.valueOf(1.d).doubleValue(), rows.get(1).getCoefficients().get(2).doubleValue(),1e-4);
        Assert.assertEquals(BigDecimal.valueOf(0.5d).floatValue(), rows.get(1).getCoefficients().get(3).doubleValue(),1e-4);

    }

    @Test
    public void testNoEquations(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        try {
            Tableau tableau = tableauBuilder.build();
            Assert.fail("An exception must be thrown here");
        } catch(IllegalStateException ex){
            Assert.assertEquals("No equations assigned",ex.getMessage());
        }
    }

    @Test
    public void testNoObjectiveFunction(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.GREATER_OR_EQUAL, 50.d));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        try {
            Tableau tableau = tableauBuilder.build();
            Assert.fail("An exception must be thrown here");
        } catch(IllegalStateException ex){
            Assert.assertEquals("No objective function assigned",ex.getMessage());
        }
    }

    @Test
    public void testInconsistentEquations(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.GREATER_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d}, Relation.GREATER_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        try {
            Tableau tableau = tableauBuilder.build();
            Assert.fail("An exception must be thrown here");
        } catch(RuntimeException ex){
            Assert.assertEquals("Equations system is inconsistent. Equations must have equal length.",ex.getMessage());
        }
    }


    @Test
    public void testPivotBadRow(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.GREATER_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d,3.d}, Relation.GREATER_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        try {
            Tableau tableau = tableauBuilder.build();
            tableau.pivot(6,1);
            Assert.fail("An exception must be thrown here");
        } catch(IllegalArgumentException ex){
            Assert.assertEquals("Row number is out of range.",ex.getMessage());
        }
    }

    @Test
public void testPivotBadColumn(){
    Problem problem = Problem.getInstance();
    problem.setPrecision(16);
    problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.GREATER_OR_EQUAL, 50.d));
    problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d,3.d}, Relation.GREATER_OR_EQUAL, 20.d));
    problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MINIMUM));

    TableauBuilder tableauBuilder = TableauBuilder.getInstance();
    tableauBuilder.setProbliem(problem);
    try {
        Tableau tableau = tableauBuilder.build();
        tableau.pivot(1,-2);
        Assert.fail("An exception must be thrown here");
    } catch(IllegalArgumentException ex){
        Assert.assertEquals("Column number is out of range.",ex.getMessage());
    }
}

    @Test
    public void testNoSolutionException(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.GREATER_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d,3.d}, Relation.GREATER_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        try {
            Tableau tableau = tableauBuilder.build();
            tableau.pivot(-1,1);
            Assert.fail("An exception must be thrown here");
        } catch(RuntimeException ex){
            Assert.assertEquals("Solution does not exist.",ex.getMessage());
        }
    }
    @Test
    public void testPivotObjFunction(){
        Problem problem = Problem.getInstance();
        problem.setPrecision(16);
        problem.addEquation(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.GREATER_OR_EQUAL, 50.d));
        problem.addEquation(ProblemEquation.make(new double[]{6.d,2.d,3.d}, Relation.GREATER_OR_EQUAL, 20.d));
        problem.addObjectiveFunction(ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MINIMUM));

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setProbliem(problem);
        try {
            Tableau tableau = tableauBuilder.build();
            tableau.pivot(2,1);
            Assert.fail("An exception must be thrown here");
        } catch(IllegalArgumentException ex){
            Assert.assertEquals("Objective function cannot be the pivot row.",ex.getMessage());
        }
    }

}
