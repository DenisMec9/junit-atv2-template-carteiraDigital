import com.example.DigitalWallet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class Estorno {

    private static DigitalWallet carteiraVerificada(double saldo) {
        var w = new DigitalWallet("Ana", saldo);
        w.verify();
        w.unlock();
        return w;
    }

    @Test
    void refundValidoAumentaSaldo() {
        var w = carteiraVerificada(10.0);
        assumeTrue(w.isVerified());
        assumeFalse(w.isLocked());

        w.refund(5.5);
        assertEquals(15.5, w.getBalance(), 1e-9);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -0.01, -2.0})
    void refundInvalidoLanca(double valor) {
        var w = carteiraVerificada(0.0);
        assertThrows(IllegalArgumentException.class, () -> w.refund(valor));
    }

    @ParameterizedTest(name = "Sequência de estornos — inicial={0} -> final={2}")
    @MethodSource("sequenciasDeEstorno")
    void refundSequencias(double saldoInicial, List<Double> estornos, double saldoFinalEsperado) {
        var w = carteiraVerificada(saldoInicial);
        assumeTrue(w.isVerified());
        assumeFalse(w.isLocked());

        for (double v : estornos) w.refund(v);
        assertEquals(saldoFinalEsperado, w.getBalance(), 1e-9);
    }

    static Stream<Arguments> sequenciasDeEstorno() {
        return Stream.of(
                Arguments.of(100.0, List.of(10.0, 20.0, 0.01), 130.01),
                Arguments.of(0.0,   List.of(1.0, 2.0, 3.0),      6.0),
                Arguments.of(50.0,  List.of(5.5, 4.5),          60.0)
        );
    }

    @Test
    void refundSemVerificacaoLanca() {
        var w = new DigitalWallet("Ana", 0.0);
        assertThrows(IllegalStateException.class, () -> w.refund(1.0));
    }

    @Test
    void refundBloqueadoLanca() {
        var w = new DigitalWallet("Ana", 0.0);
        w.verify();
        w.lock();
        assertThrows(IllegalStateException.class, () -> w.refund(1.0));
    }
}
