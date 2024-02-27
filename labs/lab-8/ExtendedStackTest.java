import Testing.ErrCounter;

/* Pointless and unecessary class Java make you have anyway. */
class ExtendedStackTest {

    public static void main(String [] args)
    {
        ExtendedStackTester st = new ExtendedStackTester();

        ErrCounter err = new ErrCounter("ExtendedStackTest");
        st.stack_test(err);
        st.extended_test(err);
        
        err.finalMessage();
    }

}

