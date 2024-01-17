import java.lang.RuntimeException;

class OversellException extends InventoryException {
    public OversellException(String msg) { super(msg); }
}
