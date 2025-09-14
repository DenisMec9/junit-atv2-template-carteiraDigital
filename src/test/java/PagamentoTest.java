import com.example.DigitalWallet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class Pagamento {

    private static DigitalWallet carteiraVerificada(double saldo) {
        var w = new DigitalWallet("Ana", saldo);
        w.verify();
        w.unlock();
        return w;
    }

    @ParameterizedTest(name = "pay: saldoInicial={0}, valor={1} -> esperado={2}")
    @CsvSource({
            "100.0, 30.0, true",
            "50.0,  80.0, false",
            "10.0,  10.0, true"
    })
    void pagamentoComAssumptions(double saldoInicial, double valor, boolean esperado) {
        var w = carteiraVerificada(saldoInicial);

        assumeTrue(w.isVerified());
        assumeFalse(w.isLocked());

        boolean ok = w.pay(valor);
        assertEquals(esperado, ok);

        double saldoEsperado = esperado ? (saldoInicial - valor) : saldoInicial;
        assertEquals(saldoEsperado, w.getBalance(), 1e-9);
    }

    @ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(doubles = {0.0, -0.01, -5.0})
    void pagamentoValorInvalidoLanca(double valor) {
        var w = carteiraVerificada(100.0);
        assertThrows(IllegalArgumentException.class, () -> w.pay(valor));
    }

    @Test
    void pagamentoSemVerificacaoLanca() {
        var w = new DigitalWallet("Ana", 50.0);
        assertThrows(IllegalStateException.class, () -> w.pay(10.0));
    }

    @Test
    void pagamentoBloqueadoLanca() {
        var w = new DigitalWallet("Ana", 50.0);
        w.verify();
        w.lock();
        assertThrows(IllegalStateException.class, () -> w.pay(10.0));
    }
}
