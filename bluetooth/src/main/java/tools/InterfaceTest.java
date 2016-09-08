package tools;

/**
 * Created by 张佳亮 on 2016/8/11.
 */
public class InterfaceTest {

    public InterfaceTest() {
    }

    public void deal(){
        testListener.test();
    }

    public TestListener testListener;
    public interface TestListener{
        void test();
    }

    public void setTestListener(TestListener testListener) {
        this.testListener = testListener;
    }
}
