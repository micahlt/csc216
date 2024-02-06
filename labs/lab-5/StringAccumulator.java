/*
 * This accumulates integers.
 */
class StringAccumulator implements Accumulator<String>
{
    private String data;
    StringAccumulator() { data = ""; }
    public void accumulate(String more) { data += more; }
    public String get() { return data; }

}
