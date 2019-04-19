/*
 * Tests functinality of Tableau creation
 */

import math.linear.basic.ObjectiveFunctionType;
import math.linear.basic.Relation;
import math.linear.basic.problem.ProblemEquation;
import math.linear.basic.problem.ProblemObjectiveFunction;
import math.linear.basic.tableau.EquationTableauRow;
import math.linear.basic.tableau.GenericTableauRow;
import math.linear.basic.tableau.ObjectiveFunctionTableauRow;
import math.linear.basic.tableau.Tableau;
import math.linear.basic.tableau.TableauBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TableauTest
{
    /**
     * Test preparation of a tableau for a simple case: two equations:
     *   1*x_1 + 7*x_2 - 4*x_3 = 50
     *   6*x_1 +2*x_2 +x_3 = 20
     *
     *   Objective function Z = 3*x_1 + 4*x_3
     *   Target: find maximum.
     */
    @Test
    public void testSimple() {
        List<ProblemEquation> equations = new ArrayList<>();
        equations.add(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.LESS_OR_EQUAL, 50.d));
        equations.add(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.LESS_OR_EQUAL, 20.d));

        ProblemObjectiveFunction objFunc = ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MAXIMUM);

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setEquations(equations);
        tableauBuilder.setObjectiveFunction(objFunc);
        Tableau tableau = tableauBuilder.build();

        Assert.assertEquals(false, tableau.isTwoPhases());

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
    }

    /**
     * Test preparation of a tableau for a simple case: two equations:
     *   1*x_1 + 7*x_2 - 4*x_3 = 50
     *   6*x_1 +2*x_2 +x_3 = 20
     *
     *   Objective function Z = 3*x_1 + 4*x_3
     *   Target: find maximum.
     */
    @Test
    public void testOnEqual() {
        List<ProblemEquation> equations = new ArrayList<>();
        equations.add(ProblemEquation.make(new double[]{1.d,7.d, -4.d}, Relation.LESS_OR_EQUAL, 50.d));
        equations.add(ProblemEquation.make(new double[]{6.d,2.d, 1.d}, Relation.EQUAL, 20.d));

        ProblemObjectiveFunction objFunc = ProblemObjectiveFunction.make(new double[]{3.d,0.d,4.d}, ObjectiveFunctionType.MAXIMUM);

        TableauBuilder tableauBuilder = TableauBuilder.getInstance();
        tableauBuilder.setEquations(equations);
        tableauBuilder.setObjectiveFunction(objFunc);
        Tableau tableau = tableauBuilder.build();

        Assert.assertEquals(true, tableau.isTwoPhases());

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

        objFuncIdx = tableau.getAuxilieryFunctionIndex();
        objFuncRow = (ObjectiveFunctionTableauRow) rows.get(objFuncIdx);
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(0));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(1));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(2));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(3));
        Assert.assertEquals(BigDecimal.ZERO, objFuncRow.getCoefficients().get(4));
        Assert.assertEquals(BigDecimal.ONE, objFuncRow.getCoefficients().get(5));
    }


}
