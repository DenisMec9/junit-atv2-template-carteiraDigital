import com.example.DigitalWallet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SaldoInicial {

    @Test
    void saldoInicialConfigurado() {
        var w = new DigitalWallet("Ana", 100.0);
        assertEquals(100.0, w.getBalance(), 1e-9);
    }

    @Test
    void construtorSaldoNegativoLanca() {
        assertThrows(IllegalArgumentException.class,
                () -> new DigitalWallet("Ana", -0.01));
    }
}
