import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RedBlackTreeTest {
    private RedBlackTree<Integer> rbt;
    private RedBlackTree<Integer> rbtFull;
    private RedBlackTree<Integer> rbtFullMinus;
    private Integer value;
    private int countOfElem;
    private List<Integer> list;

    @Before
    public void initTest() {
        rbt = new RedBlackTree<Integer>();
        list = new ArrayList<Integer>();
    }

    @Before
    public void initFullTreeTest() {
        rbtFull = new RedBlackTree<Integer>();
        rbtFullMinus = new RedBlackTree<Integer>();
        value = 0;
        countOfElem = 10000;

        for (int i = 0; i <= countOfElem; i++) {
            value = i;
            rbtFull.add(value);
            rbtFullMinus.add(-value);
        }
    }

    @Test
    public void blackHeightWithIncreaseValueTest() {
        for (int i = 0; i <= countOfElem; i++) {
            value = i;
            assertTrue(rbtFull.returnBlackHeightOfElem(value) <= (int)(Math.log(countOfElem + 1) / Math.log(2)));
        }
    }

    @Test
    public void blackHeightWithDecreaseValueTest() {
        for (int i = 1; i <= countOfElem; i++) {
            value = -i;
            assertTrue(rbtFullMinus.returnBlackHeightOfElem(value) <= (int)(Math.log(countOfElem + 1) / Math.log(2)));
        }
    }

    @Test
    public void blackRootColorEmptyTest() {
        assertTrue(rbt.isColorOfRootBlack());
    }

    @Test
    public void blackRootColorTest() {
        for (int i = 0; i <= countOfElem; i++) {
            value = i;
            rbt.add(value);
            assertTrue(rbt.isColorOfRootBlack());
        }
    }

    @Test
    public void RedAreNotNeighborWithRedInFullTreeTest() {
        for (int i = 0; i <= countOfElem; i++) {
            if (rbtFull.isColorOfElementRed(i)){
                assertFalse(rbtFull.isColorOfLeftChildRed(i));
                assertFalse(rbtFull.isColorOfRightChildRed(i));
                assertFalse(rbtFull.isColorOfParentRed(i));
            }
        }
    }

    @Test
    public void RedAreNotNeighborWithRedInFillingTreeTest() {
        int count = 1000;
        for (int i = 0; i <= count; i++) {
            value = i;
            rbt.add(i);
            for (int j = 0; j <= i; j++) {
                if (rbt.isColorOfElementRed(j)) {
                    assertFalse(rbt.isColorOfLeftChildRed(j));
                    assertFalse(rbt.isColorOfRightChildRed(j));
                    assertFalse(rbt.isColorOfParentRed(j));
                }
            }
        }
    }

    @Test
    public void nullListTest() {
        rbt.add(null);
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void emptyListTest() {
        List<Integer> list = new ArrayList<Integer>();
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void oneNodeListTest() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);

        rbt.add(1);
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void twoIncreasingNodeListTest() {
        Integer integerArray[] = {1, 2};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(1);
        rbt.add(2);
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void threeIncreasingNodeForOneLeftRotateListTest() {
        Integer integerArray[] = {1, 2, 3};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(1);
        rbt.add(2);
        rbt.add(3); //left rotate
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void twoDecreasingNodeListTest() {
        Integer integerArray[] = {2, 3};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(3);
        rbt.add(2);
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void threeDecreasingNodeForOneRightRotateListTest() {
        Integer integerArray[] = {1, 2, 3};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(3);
        rbt.add(2);
        rbt.add(1); //right rotate
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void fourRightNodeWithChangeColorListTest() {
        Integer integerArray[] = {0, 0, 0, 0};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(0);
        rbt.add(0);
        rbt.add(0); //left rotate
        rbt.add(0); //black grand, red dad, red uncle -> black grand, black dad, black uncle
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void fourLeftNodeWithChangeColorListTest() {
        Integer integerArray[] = {7, 7, 8, 9};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(9);
        rbt.add(7);
        rbt.add(8); //left rotate and right rotate
        rbt.add(7); //black grand, red dad, red uncle -> black grand, black dad, black uncle
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void fiveIncreaseNodeWithChangeColorListTest() {
        Integer integerArray[] = {4, 4, 4, 4, 5};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(4);
        rbt.add(4);
        rbt.add(4); //left rotate
        rbt.add(5); //black grand, red dad, red uncle -> black grand, black dad, black uncle
        rbt.add(4); //right rotate and left rotate
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void fiveEqualNodeWithChangeColorListTest() {
        Integer integerArray[] = {4, 4, 4, 4, 4};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(4);
        rbt.add(4);
        rbt.add(4); //left rotate
        rbt.add(4); //black grand, red dad, red uncle -> black grand, black dad, black uncle
        rbt.add(4); //right rotate
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void sevenNodeWithRightRotateAfterRootListTest() {
        Integer integerArray[] = {4, 5, 6, 6, 6, 7, 8};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(5);
        rbt.add(4);
        rbt.add(7);
        rbt.add(6); //black grand, red dad, red uncle -> black grand, black dad, black uncle
        rbt.add(8);
        rbt.add(6); //black grand, red dad, red uncle -> black grand, black dad, black uncle "B_4 B_5 B_6 R_6 R_7 B_8 "
        rbt.add(6); //right rotate and left rotate
        assertEquals(list, rbt.listFromTree());
    }

    @Test
    public void eigthNodeChangeRootListTest() {
        Integer integerArray[] = {4, 5, 6, 6, 6, 6, 7, 8};
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(integerArray));

        rbt.add(5);
        rbt.add(4);
        rbt.add(7);
        rbt.add(6); //black grand, red dad, red uncle -> black grand, black dad, black uncle
        rbt.add(8);
        rbt.add(6); //black grand, red dad, red uncle -> black grand, black dad, black uncle "B_4 B_5 B_6 R_6 R_7 B_8 "
        rbt.add(6); //right rotate and left rotate
        rbt.add(6); //right rotate and left rotate
        assertEquals(list, rbt.listFromTree());
    }

    @After
    public void restoreStreams() {
        ;
    }
}
