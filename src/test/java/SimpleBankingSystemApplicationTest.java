import com.mlpj.simple.banking.system.SimpleBankingSystemApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;

import static com.mlpj.simple.banking.system.util.Constants.INITIAL_PROMPT;
import static com.mlpj.simple.banking.system.util.Constants.INVALID_INPUT;

class SimpleBankingSystemApplicationTest {

    @Test
    void testMainValidInput() {
        String[] arguments = new String[]{"-i", "CONSOLE"};
        try(InputStream gotoAddTransaction = new ByteArrayInputStream("T".getBytes());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){

            System.setIn(gotoAddTransaction);

            PrintStream out = new PrintStream(byteArrayOutputStream);
            System.setOut(out);

            SimpleBankingSystemApplication.main(arguments);

            String output = byteArrayOutputStream.toString(Charset.defaultCharset());
            Assertions.assertEquals(INITIAL_PROMPT.trim(), output.trim());
        } catch (Exception e) {
            // do nothing
        }
    }

    @Test
    void testMainInValidInput() {
        String[] arguments = new String[]{"-i", "CONSOLE"};
        try(InputStream gotoAddTransaction = new ByteArrayInputStream("invalid".getBytes())){

            System.setIn(gotoAddTransaction);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(byteArrayOutputStream);
            System.setOut(out);
            SimpleBankingSystemApplication.main(arguments);

            String output = byteArrayOutputStream.toString(Charset.defaultCharset());
            Assertions.assertEquals(INVALID_INPUT.trim(), output.trim());
        } catch (Exception e) {
            // do nothing
        }
    }
}
