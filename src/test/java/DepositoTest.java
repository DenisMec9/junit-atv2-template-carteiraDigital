import com.example.DigitalWallet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class Deposito {

    @ParameterizedTest
    @ValueSource(doubles = {10.0, 0.01, 999.99})
    void depositoValidoAtualizaSaldo(double amount) {
        var w = new DigitalWallet("Ana", 0.0);
        w.deposit(amount);
        assertEquals(amount, w.getBalance(), 1e-9);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.01, -10.0})
    void depositoInvalidoLanca(double amount) {
        var w = new DigitalWallet("Ana", 0.0);
        assertThrows(IllegalArgumentException.class, () -> w.deposit(amount));
    }
}
