import math.linear.basic.ObjectiveFunction;
import math.linear.basic.ObjectiveFunctionType;
import org.junit.Assert;
import org.junit.Test;

public class ObjectiveFunctionTest {
    @Test
    public void testCreate(){
        double[] val1 = new double[] {1., 2., 3.};
        ObjectiveFunction fun1 = ObjectiveFunction.create(val1, ObjectiveFunctionType.MAXIMUM);
        Assert.assertEquals(1., fun1.getValueAt(0), 0.0d);
        Assert.assertEquals(2., fun1.getValueAt(1), 0.0d);
        Assert.assertEquals(3., fun1.getValueAt(2), 0.0d);
        Assert.assertArrayEquals(val1,fun1.getValues(),0.0d);
        Assert.assertEquals(fun1.getType(),ObjectiveFunctionType.MAXIMUM);

        Assert.assertFalse("Is optimal", fun1.isOptimal());
    }

    @Test
    public void testModify(){
        double[] val1 = new double[] {1., 2., 3.};
        ObjectiveFunction fun1 = ObjectiveFunction.create(val1, ObjectiveFunctionType.MINIMUM);
        ObjectiveFunction fun2 = fun1.getCanonical();
        Assert.assertTrue("Not optimal", fun1.isOptimal());
    }

    @Test
    public void testCombination(){
        double[] val1 = new double[] {1., 2., 3.};
        double[] val2 = new double[] {-1., 2., 7.};
        ObjectiveFunction fun1 = ObjectiveFunction.create(val1, ObjectiveFunctionType.MINIMUM);
        ObjectiveFunction fun2 = fun1.add(val2);
        Assert.assertEquals(0., fun2.getValueAt(0), 0.0d);
        Assert.assertEquals(4., fun2.getValueAt(1), 0.0d);
        Assert.assertEquals(10., fun2.getValueAt(2), 0.0d);
    }
}
