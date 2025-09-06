package dbTests;

import org.testng.annotations.Test;
import utilities.TestBaseDbServer;

import java.util.List;
import static org.testng.Assert.*;

public class dbSanity extends TestBaseDbServer {
    @Test
    public void orderIsPersistedWithCorrectTotals() {
        // ... call API to create order ...
        String orderId = "ORD-123"; // from API response

        List<Integer> totals = db.query(
                "SELECT total_cents FROM orders WHERE order_id = ?",
                rs -> rs.getInt("total_cents"),
                orderId
        );
        assertEquals(totals.size(), 1, "order should exist");
        assertTrue(totals.get(0) > 0, "total should be positive");
    }
}
