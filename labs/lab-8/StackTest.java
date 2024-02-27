import Testing.ErrCounter;

/* Pointless and unecessary class Java make you have anyway. */
class StackTest {

    public static void main(String [] args)
    {
        StackTester st = new StackTester();

        ErrCounter err = new ErrCounter("StackTest");
        st.stack_test(err);
        
        err.finalMessage();
    }

}

